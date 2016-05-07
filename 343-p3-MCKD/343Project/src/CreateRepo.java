import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;
import java.util.Scanner;
import java.io.PrintWriter;
public class CreateRepo {
	public static PrintWriter out = null;
	
	void initialize(String src, String target){
				String repo = "repo343";
				// create repo directory in target directory
				File theDir = new File(target+"/"+repo);
				createDir(theDir);
				// create manifest directory
				File manifest = new File(target+"/"+repo+"/manifests/");
				createDir(manifest);
				// access target folder specified by user
				File srcDir = new File(src);
				// create tree directory in repo
				File dupTree = new File(target+"/"+repo+"/"+srcDir.getName());
				createDir(dupTree);
				createMan(target, repo, new File(srcDir.getParent()));
				// duplicate tree inside repo
				String repoTreePath = target+"/"+repo+"/"+srcDir.getName();
				copyTree(srcDir,repoTreePath); 	// copy ptree into repo
				out.println();
				out.println("Parent Tree: None"); // no parent when creating repo
				out.flush();
	}
	
	void createMan(String target, String repo, File src){
		// save manifest path
		String manPath = target+"/"+repo+"/manifests/"+getDate().replace(':', '-')+" Location- "+
		src.getName().replaceAll("\\.", "").replace("\\", "")+".txt";
		try {
			out = new PrintWriter(manPath);
			// Display current Project Tree
			out.println("Project Tree: "+src);
			out.flush();
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
	}

	void copyTree(File folder,String path){
		PrintWriter artOut = null;  // artifact to be made
		Scanner artIn = null;		// file to save to an artifact
		String artID = "";			// artifact ID for name of file
		String leafPath = "";		// path of each artifact
		
		for (final File fileEntry : folder.listFiles()) {
			leafPath = path+"/"+fileEntry.getName();
			String[]spl = fileEntry.getName().split("\\.");
			String ext = "";
			if (spl.length > 0){
				ext = spl[spl.length-1];
			}
	        if (fileEntry.isDirectory()) {
	        	// add 
	        	createDir(new File(leafPath));
	            copyTree(fileEntry,leafPath);
	        } else {
	        	File leafFolder = new File(leafPath);
	        	createDir(leafFolder);
		        artID = Integer.toString(getCheckSum(fileEntry));
		        	// create artifact file
		        File artifact = new File(leafPath+"/"+artID+"."+ext);
		    	createFile(leafPath+"/"+artID+"."+ext);
		    		// output artifact path to manifest
		    	out.println(artifact.getPath()+"\n");
		    	out.flush();
		    		// write to artifact
		    	try {
		    		// output to artifact
					artOut = new PrintWriter(leafPath+"/"+artID+"."+ext);
					// input from file in src
					artIn = new Scanner(fileEntry);
					while(artIn.hasNextLine())
						artOut.println(artIn.nextLine());
					artOut.flush();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
	        }
	    }
	}
	int getCheckSum(File file){
		Scanner in = null;
		try {
			in = new Scanner(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		int artID = 0;
		// get checksum of file by getting sum of the byte values of each char
		while(in.hasNextLine()){
			String currentLine = in.nextLine();
			for (int i = 0;i < currentLine.length();i++){
				artID += (int)currentLine.charAt(i);
				// make sure AID is between 0 and 255
				artID %= 256;
			}
		}
		return artID;
	}
	
	void createFile(String path){
		File file = new File(path);
		// if the directory does not exist, create it
		if (!file.exists()) {
		    try{
		    	file.createNewFile();
		    } 
		    catch(SecurityException se){
		    } catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	static String getDate(){
		Date date = new Date();
	    return date.toString();
	}
	
	void createDir(File theDir){
		// if the directory does not exist, create it
		if (!theDir.exists()) {
		    try{
		        theDir.mkdir();
		    } 
		    catch(SecurityException se){
		    }        
		}
	}
}
