import java.io.File;
import java.util.Scanner;

public class CreateRepo {

	public static void main(String[] args) {
		System.out.print("\nr343> ");
		Scanner in = new Scanner(System.in);
		String input = in.nextLine();
		String [] user = input.split(" ");
		String cmd = user[0];
		String dirName = user[1];
		String src = user[2];
		String target = user[3];
		
		File theDir = new File(target+"/"+dirName);

		// if the directory does not exist, create it
		if (!theDir.exists()) {
		    System.out.println("creating "+dirName);
		    boolean result = false;

		    try{
		        theDir.mkdir();
		        result = true;
		    } 
		    catch(SecurityException se){
		        //handle it
		    }        
		    if(result) {    
		        System.out.println("DIR created");  
		    }
		}
		
		File subDir = new File(target+"/"+dirName+"/manifests");

		// if the directory does not exist, create it
		if (!subDir.exists()) {
		    System.out.println("creating manifests");
		    boolean result = false;

		    try{
		        subDir.mkdir();
		        result = true;
		    } 
		    catch(SecurityException se){
		        //handle it
		    }        
		    if(result) {    
		        System.out.println("manifests created");  
		    }
		}
		System.out.println(subDir.getPath());
		// access target folder specified by user
		File srcDir = new File(src);
		
		// duplicate tree into repo
		File dupTree = new File(target+"/"+dirName+"/"+src);

		// if the directory does not exist, create it
		if (!dupTree.exists()) {
		    System.out.println("creating duplicate of ptree into repo");
		    boolean result = false;
		    try{
		        dupTree.mkdir();
		        result = true;
		    } 
		    catch(SecurityException se){
		    }        
		    if(result) {    
		        System.out.println("duplicate ptree created");  
		    }
		}
		String repoTreePath = target+"/"+dirName+"/"+src;
		copyTree(srcDir,repoTreePath);
	}

	static void copyTree(File folder,String path){
		for (final File fileEntry : folder.listFiles()) {
	        if (fileEntry.isDirectory()) {
	            copyTree(fileEntry,path);
	        } else {
	        	String leafPath = path+"/"+fileEntry.getName();
	        	File leafFolder = new File(leafPath);
	        	if (!leafFolder.exists()) {
	    		    System.out.println("creating leaf folder");
	    		    boolean result = false;
	    		    try{
	    		        leafFolder.mkdir();
	    		        result = true;
	    		    } 
	    		    catch(SecurityException se){
	    		    }        
	    		    if(result) {    
	    		        System.out.println("leaf folder created");  
	    		    }
	    		}
	        }
	    }
	}
}
