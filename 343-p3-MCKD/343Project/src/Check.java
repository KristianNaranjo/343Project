import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;
public class Check {
	void checkIn(File src, String target){
		String repoTreePath = target+"/"+src.getName();
		String targRepo = "";
		CreateRepo repo = new CreateRepo(); 	// use CreateRepo's methods
		repo.createMan(target,targRepo,new File(src.getParent())); 	// create manifest file
		repo.createDir(new File(repoTreePath)); // createDir if ptree is new
		repo.copyTree(src, repoTreePath); 		// add artifacts if there are changes
		File man = new File(target+"/manifests");
		/** index of Parent Tree is man.list().length - 2 because the current
		 * manifest file has already been created which is at index
		 * man.list().length - 1. Therefore the preceding tree is one less
		*/
		// parent from check in
		// parent in this case just means location, not previous project tree
		String srcParent = src.getParent().replaceAll("\\.", "").replace("\\", "");
		ArrayList<File> manTree = new ArrayList<File>();
		for (File manFile : man.listFiles()){
			String[] manNameSplit = manFile.getName().split("Location- ");
			String parent = manNameSplit[manNameSplit.length - 1];
			String[] splitParent = parent.split("\\.");
			parent = splitParent[0];
			// get manifest files that match the parent of the src checked in
			if(parent.equals(srcParent)){
				manTree.add(manFile);
			}
		}
		repo.out.println("Parent Tree: "+ manTree.get(manTree.size() - 2));
			// error if there is not at least one pre-existing manifest file
		repo.out.flush(); // flush buffer
	}
	// fix sub folder problem
	void checkOut(File src, File target){
		
		Scanner in = new Scanner(System.in);
		String ptree = getTreeName(src);
		File treeDir = new File(src+"/"+ptree);
		File manDir = new File(src+"/manifests");
		ArrayList<File> paths = getPaths(manDir); // gives possible manifest files
		ArrayList<File>leafPaths = new ArrayList<File>();
		int i = 1;
		for(File leafPath : paths){ 			// Display manifest paths for user to select
			System.out.println(i+". "+leafPath.getName());
			i++;
		}
		System.out.println("Enter the number associated with your desired manifest file.");
		Scanner manIn = null;
		try {
			int manIndex = 0;
				// error check for out-of-range of list
			do{
			manIndex = in.nextInt();
			}while(manIndex<=0 || manIndex > paths.size());
			
			manIn = new Scanner(paths.get(manIndex - 1));
			while(manIn.hasNextLine()){
				String line = manIn.nextLine();
				if(!(line.isEmpty() || line.contains("Parent Tree:") || line.contains("Project Tree:"))){
					leafPaths.add(new File(line)); // add leaf paths
				}
			}
			// leaf file locations have been saved by this point
			
			CreateRepo repo = new CreateRepo(); // use methods from CreateRepo
			// create the target if it does not already exist
			repo.createDir(target);
			// targetTree is the tree created from the original ptree
			File targetTree = new File(target+"/"+ptree);
			repo.createDir(targetTree);
			copyTree(leafPaths,targetTree,ptree); // copy files from ptree to target
			repo.createMan(src.getName(),"", target); // create manifest file
			for(File entry : leafPaths){
				repo.out.println(entry);
				repo.out.flush();
			}
			repo.out.println("Parent Tree: "+ paths.get(manIndex-1));
			repo.out.flush();
		} catch (FileNotFoundException e) {
			System.out.println("File does not exist.");
		}
	}
	
	ArrayList<File> getPaths(File folder){ // find all file paths within a folder
		ArrayList<File> paths = new ArrayList<File>();
		for (final File fileEntry : folder.listFiles()) {
	        if (fileEntry.isDirectory()) {
	            getPaths(fileEntry);
	        } else {
	        	paths.add(fileEntry);
	        }
	    }
		return paths;
	}
	
	void copyTree(ArrayList<File> leafFile,File targetTree, String ptree){ //take in ptree
		PrintWriter newLeaf = null;
		Scanner artIn = null;
		for (final File fileEntry : leafFile) {
			try { // split using ptree the path and check if there are subfolders
				artIn = new Scanner(fileEntry);
				File leafParent = new File(fileEntry.getParent());
				File subDir = new File(leafParent.getParent());
				// check if a there is a subfolder between a file and the ptree
				if(!subDir.getName().equals(ptree)){
					CreateRepo repo = new CreateRepo();
					File subFolder = new File(targetTree+"/"+subDir.getName());
					repo.createDir(subFolder);
					newLeaf = new PrintWriter(targetTree+"/"+subDir.getName()+"/"+leafParent.getName());
					while(artIn.hasNextLine()){
						newLeaf.println(artIn.nextLine());
					}
					newLeaf.flush();
					/** files are added to the subfolder
					so the loop can continue to the next iteration*/
					continue;
				}
				newLeaf = new PrintWriter(targetTree+"/"+leafParent.getName());
				while(artIn.hasNextLine()){
					newLeaf.println(artIn.nextLine());
				}
				newLeaf.flush();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
	    }
	}
	
	String getTreeName(File repo){ // get a project tree name in a repository
		String treeName = "tree";
		for(File fileEntry : repo.listFiles()){
			if(!fileEntry.equals("manifests"))
				treeName = fileEntry.getName();
		}
		return treeName;
	}
	
}
