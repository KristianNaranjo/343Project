import java.io.File;
import java.util.Scanner;

public class CommandLine {
	public static void main(String[] args) {
		System.out.print("\nr343> ");
		Scanner in = new Scanner(System.in);
		String input = in.nextLine();
		if (input.split(" ").length == 4){ // won't enter if "exit" is input
			String [] user = input.split(" "); // "exit" can't be split
			String cmd = user[0];
			String cmdSpecifier = user[1];
			File src = new File(user[2]);
			File target = new File(user[3]);
			if (cmd.equals("create") && cmdSpecifier.equals("repo")){
				if(src.exists() && target.exists()){ // check if src and target exist
					if (!src.equals(target)){
						// sample input: create repo ../FRED/mypt ./
						CreateRepo repo = new CreateRepo();
						repo.initialize(user[2], user[3]); // create repo
						System.out.print("repo created"); // create repo is successful
					}
					else // Src can't be target because then it will copy files indefinitely
						System.out.println("The source folder cannot be the target folder.");
				}
				else System.out.print("Either source folder or target folder does not exist.");
			}
			
			else if (cmd.equals("check")){
				Check check = new Check();
				if(cmdSpecifier.equals("in")){ // check in has been entered
					// source is an existing project tree
					// the target is the top folder of the repo
					//Ex:  check in ../FRED/mypt ./repo343
					check.checkIn(src, target.getName());
					System.out.print("check-in successful");
				}
				else if(cmdSpecifier.equals("out")){// check out has been entered
					// take in repoFolder as source folder
					// take in target (receives fresh copy of ptree)
					//Ex: check out ./repo343 ../JACK
					check.checkOut(src, target);
					System.out.print("check-out successful");
				}
			}
		}
		else if(input.split(" ").length == 3){
			String[] user = input.split(" ");
			if (user[0].equals("merge")){
				Merge merge = new Merge();
				// take in the repo folder as the src
				File repo = new File(user[1]);
				// take in a target folder to merge to
				File target = new File(user[2]);
				// Ex: merge ./repo343 ../JACK
				merge.merge(repo,target);
				System.out.println("Merge successful");
			}
		}
		if(!input.equals("exit")) // quit if "exit" is entered
			main(args); // return to beginning
		in.close();
	}
}
