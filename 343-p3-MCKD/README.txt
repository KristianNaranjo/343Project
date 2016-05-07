Program Name:
343Project

Team MCKD:
Kristian Naranjo
Chi Leung To
Chelsea Soldan
Melanie Khim

Contact Info:
Kristian - kristiannaranjo@gmail.com
Chi - migicman@gmail.com
Chelsea - csoldan14@gmail.com
Melanie - MelKhim.MK@gmail.com

Project Part 3

Intro:
This is the third part of our VCS (Version Control System) project. It adds a new use case which allows users 
to merge to existing branches together. The merge operation takes in a source directory (the repository) and
a target direction (a previous check-out location). The user specifies which source manifest file to use for
the merge. The contents of that manifest file are merged with the most recent check-in of the target file.
If there is no conflict between artifact IDs, then there is nothing to merge. However, if there is a conflict,
the files with different artifact IDs are copied to the target along with the corresponding file from the
grandpa tree.


External Requirements:
You must first have java installed on your computer. You must also have either access to an IDE(integrated
development environment) such as Eclipse or be able to run java through the command line. 

Build - 1.2
Updates and fixes:
-createRepo,check-in,check-out have been fixed to work as intended
- merge use case has been added
- check-out has also been updated to copy subfolders and not just the contents of a subfolder as before

Installation :
To install, open your Java IDE. Then, import the project folder into
your workspace by clicking File -> Import -> Import existing project into workspace.
Then find the project folder called 343Project and select import.

Setup:
For IDE:
Once the project has been imported, you may open it any time from the IDE by clicking
File -> Open Project. Then find the project named 343Project. Once opened, you may run it
by pressing the run button.


Usage:
For previous use cases:

To use this program you must enter a command and a set of arguments, similar to a command line.
In order to create a repository, type "create repo", then type in the name of the source folder
that is the project tree(In this case you can use mypt or mypt2). Then enter the destination
for the repo to be created. An example of this input would be "create repo ../FRED/mypt ./"

To use the updated check-in use case:
	- type: check in targetTree repoDirectory
	-Example: check in ../FRED/mypt ./repo343

To use the updated check-out use case:
	- type: check out sourceRepo target location
	- Example: check out ./repo343 ../JACK
	- you will then be prompted to enter the name of the ptree in the repo (Ex: mypt)
	- you can then choose a manifest file to check-out from

For current use case(MERGE):
To merge versions of files together, you must supply the source folder(the repo) and a target
folder(a directory previously checked-out to). Then you can choose which source manifest file
to merge with.

To merge:
	- type: merge sourceRepo targetFolder
	- Example: merge ./repo343 ../JACK
	- You will then choose a source manifest file to merge with


Extra Features:
You may enter "exit" at any time to leave the command line.

Bugs:
-On check-out files will only be copied if they are within one subfolder.
	if they lie within a folder that is in a subfolder, then they won't be copied