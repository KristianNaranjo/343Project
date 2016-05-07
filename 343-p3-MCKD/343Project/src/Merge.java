import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;

public class Merge {
	void merge(File repo, File target){
		Scanner in = new Scanner(System.in);
		// isolate the target name by itself to compare with later
		String[] targetSplit = target.getName().split("/");
		String targetName = targetSplit[targetSplit.length - 1];
		File targetMan = null;
		File manDir = new File("./repo343/manifests");
		System.out.println("Choose a source manifest file to be merged.");
		int i = 1;	// used to show user what number to input
		String srcName = "";
		// display manifest files to user
		for(File manFile : manDir.listFiles()){
			String[] locSplit = manFile.getName().split("Location- ");
			String locationName = locSplit[locSplit.length - 1];
			String[] extSplit = locationName.split("\\.");
			locationName = "";
			for(int j=0; j < extSplit.length - 1; j++){
				locationName += extSplit[j];	// separate from extension
			}
			if(locationName.equals(targetName)){
				targetMan = manFile; // gives most recent check in from target
			}
			if(i == 1){ // gives the first location name
				srcName = locationName; // the first location is always the src
			}
			if(locationName.equals(srcName)){ // only shows manifest files from the source
			System.out.println(i + ". " + manFile.getName());
			i++;
			}
		}
		System.out.println("Enter the number associated with your desired manifest file.");
		int manIndex = in.nextInt() - 1;
		File srcManFile = manDir.listFiles()[manIndex];
		// all parents of src
		LinkedList<String> srcLineage = new LinkedList<String>();
		srcLineage = getFullpath(srcManFile, srcLineage);
		// all parents of target
		LinkedList<String> targetLin = new LinkedList<String>();
		targetLin = getFullpath(targetMan, targetLin);
		// common ancestor
		File grandpa = getGrandpa(srcLineage, targetLin);
		// get name of ptree
		Check check = new Check();
		String ptree = check.getTreeName(repo);
		// get all files in each manifest
		ArrayList<File> srcLeaves = getLeaves(srcManFile);
		ArrayList<File> targetLeaves = getLeaves(targetMan);
		ArrayList<File> grandLeaves = getLeaves(grandpa);
		// location to copy to
		File targetCopy = new File(target+"/"+ptree);
		
		for(File srcLeaf : srcLeaves){
			Boolean targHasSrc = false; // check if file exists in both
			for(File targetLeaf : targetLeaves){
				if(getLeafName(targetLeaf).equals(getLeafName(srcLeaf))){
					targHasSrc = true;
					if(!getLeafID(targetLeaf).equals(getLeafID(srcLeaf))){ // conflict
						// copy conflict files to target
						copyFile("_MT",targetLeaf, targetCopy);
						copyFile("_MR",srcLeaf, targetCopy);
						File grandLeaf = getGrandpaLeaf(getLeafName(targetLeaf), grandLeaves);
						copyFile("_MG",grandLeaf, targetCopy);
						File replacedLeaf = new File(targetCopy+"/"+getLeafName(targetLeaf));
						// get rid of file in target after file_MT has been added
						if(replacedLeaf.exists())
							replacedLeaf.delete();
					}
				}
			}
			if(!targHasSrc)
				copyFile("",srcLeaf,targetCopy); // insert src file into target if target doesn't have it
		}
	}
	
	void copyFile(String fileSpecifier, File file, File target){
		String fileName = getLeafName(file);
		// separate extension
		String[] fNameSplit = fileName.split("\\.");
		String extension = fNameSplit[fNameSplit.length -1];
		fileName = "";
		// get the main file name
		for(int i = 0; i < fNameSplit.length - 1; i++){
			fileName += fNameSplit[i]; // add in parts in case file name has periods
		}
		fileName += fileSpecifier + "." + extension; // rename file
		Scanner in = null;
		PrintWriter copy = null;
		try { // copy contents of file
			in = new Scanner(file);
			copy = new PrintWriter(target+"/"+fileName);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		while(in.hasNextLine()){
			copy.println(in.nextLine());
			copy.flush();
		}
	}
	
	// used to find the grandparent conflict file
	File getGrandpaLeaf(String fileName, ArrayList<File> grandLeaves){
		for(File grandLeaf : grandLeaves){
			if(getLeafName(grandLeaf).equals(fileName)){
				return grandLeaf;
			}
		}
		return null;
	}
	
	// add all leaves in a manifest to a list
	ArrayList<File> getLeaves(File manifest){
		ArrayList<File> leafPaths = new ArrayList<File>();
		Scanner manIn = null;
		try {
			manIn = new Scanner(manifest);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		while(manIn.hasNextLine()){
			String line = manIn.nextLine();
			if(!(line.isEmpty() || line.contains("Parent Tree:") || line.contains("Project Tree:"))){
				leafPaths.add(new File(line)); // add leaf paths
			}
		}
		return leafPaths;
	}
	
	String getLeafName(File leaf){
		// split file path into sections
		// second to last section is the leaf folder
		String leafName = "";
		String[] leafSplit = leaf.getPath().split("\\\\");
		leafName = leafSplit[leafSplit.length - 2];
		return leafName;
	}
	
	String getLeafID(File leaf){
		// split file path into sections
		// last section is the artifact file
		String leafName = "";
		String[] leafSplit = leaf.getPath().split("\\\\");
		leafName = leafSplit[leafSplit.length - 1];
		return leafName;
	}
	
	// get the lineage of a manifest file by recursively finding parents
	LinkedList<String> getFullpath(File file, LinkedList<String> fullpath){ // takes in a manifest file
		Scanner in = null;
		try {
			in = new Scanner(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		while(in.hasNextLine()){ // get all parent manifest file names
			String line = in.nextLine();
			if(line.contains("Parent Tree: ")){
				String[] splitPar = line.split("Parent Tree: ");
				String parent = splitPar[1];
				fullpath.addFirst(parent);
				File parentFile = new File(parent);
				if(parent.equals("None")){
					return fullpath;
				}
				getFullpath(parentFile, fullpath);
			}
		}
		return fullpath;
	}
	// get the common ancestor of two lineages
	File getGrandpa(LinkedList<String> src, LinkedList<String> target){
		// set a minSize to not go out of bounds of list
		int minSize = src.size();
		if(target.size() < minSize)
			minSize = target.size();
		int i = 0;
		File grandpa = null;
		while( (i < minSize) && (src.get(i).equals(target.get(i))) ){
			grandpa = new File(src.get(i)); // will be the last common ancestor
			i++;
		}
		return grandpa;
	}
}
