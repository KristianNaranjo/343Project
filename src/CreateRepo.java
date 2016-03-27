import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;
import java.util.Scanner;
import java.io.PrintWriter;
public class CreateRepo {
	private static PrintWriter out = null;
	public static void main(String[] args) {
		System.out.print("\nr343> ");
		Scanner in = new Scanner(System.in);
		String input = in.nextLine();
		if (input.split(" ").length == 4){
			String [] user = input.split(" ");
			String cmd = user[0];
			String repo = user[1];
			File src = new File(user[2]);
			File target = new File(user[3]);
			if (cmd.equals("create") && repo.equals("repo")){
				if(src.exists() && target.exists()){
					if (!src.equals(target)){
					initialize(user[2], user[3]);
					System.out.print("repo created");
					}
					else
						System.out.println("The source folder cannot be the target folder.");
				}
				else System.out.print("Either source folder or target folder does not exist.");
			}
		}
		if(!input.equals("exit"))
			main(args);
		in.close();
		out.close();
	}
	
	static void initialize(String src, String target){
				String repo = "repo343";
				// create repo directory in target directory
				File theDir = new File(target+"/"+repo);
				createDir(theDir);
				
				// create manifest directory
				File manifest = new File(target+"/"+repo+"/manifests");
				createDir(manifest);
				
				// save manifest path
				String manPath = target+"/"+repo+"/manifests/"+getDate().replace(':', '-')+".txt";
				try {
					out = new PrintWriter(manPath);
				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
				}
				// access target folder specified by user
				File srcDir = new File(src);
				
				// create tree directory in repo
				File dupTree = new File(target+"/"+repo+"/"+srcDir.getName());
				createDir(dupTree);
				
				// duplicate tree inside repo
				String repoTreePath = target+"/"+repo+"/"+src;
				copyTree(srcDir,repoTreePath);
				out.println();
				out.println("Previous Project tree: None");
				out.flush();
	}

	static void copyTree(File folder,String path){
		PrintWriter artOut = null;
		Scanner artIn = null;
		String artID = "";
		String leafPath = "";
		
		for (final File fileEntry : folder.listFiles()) {
			leafPath = path+"/"+fileEntry.getName();
	        if (fileEntry.isDirectory()) {
	        	// add 
	        	createDir(new File(leafPath));
	            copyTree(fileEntry,leafPath);
	        } else {
	        	File leafFolder = new File(leafPath);
	        	createDir(leafFolder);
		        artID = Integer.toString(getCheckSum(fileEntry));
		        // create artifact file
		    	createFile(leafPath+"/"+artID+".txt");
		    	// output artifact path to manifest
		    	out.println(leafPath+"/"+artID+".txt\n");
		    	out.flush();
		    	// write to artifact
		    	try {
		    		// output to artifact
					artOut = new PrintWriter(leafPath+"/"+artID+".txt");
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
	static int getCheckSum(File file){
		// get AID based on file name
		String fName = file.getName();
		int artID = 0;
		// get checksum of file name by getting its ascii value
		for(int i = 0; i < fName.length(); i++){
			if (fName.charAt(i) == '.')
				break;
			artID += (int)fName.charAt(i);
		}
		// make sure AID is between 0 and 255
		artID %= 256;
		return artID;
	}
	
	static void createFile(String path){
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
	
	static void createDir(File theDir){
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
