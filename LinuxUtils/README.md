# Setting up inux Utilities for scouting with Android Scout

First create a directory for the utilites
	
	"mkdir ~/Scouting"
	"cd ~/Scouting"

Download the Android Scout repository
	
	"git clone http://github.com/TribeTech4485/AndroidScout.git"

Move the LinuxUtils directory to the parent direcotry of AndroidScout
	
	"mv AndroidScout/LinuxUtils/ ."


Install the dependencies for the python utility and to build the parser
	
	"sudo apt-get update && sudo apt-get upgrade -y"
	"sudo apt-get install python python-dev python-bluez bluez"
	"sudo apt-get install cmake build-essential gcc"


Build the parser
	
	"cd LinuxUtils/SimpleParserCpp"
	"cmake ."
	"make"

Move the parser into the parent directory of AndroidScout
	
	"mv SimpleParser ../.."

Move "bt_text_rcv.py" to the parent directory of AndroidScout
	
	"cd ../.."
	"mv LinuxUtils/rf_text_rcv.py ."

Now we can write a bash script to run everything
	
	"nano scout.sh"

Add the following content to the file:
	
	#!/bin/sh
	
	sudo python rf_text_rcv.py &
	./SimpleParser TeamData.list TeamDataTable.csv y

Now hit control+X to exit, when prompted you will hit Y for 'yes' to save your file.

Now make the script executable
	
	"chmod +x scout.sh" 

Run the script
	
	"./scout.sh"
	
If there was no error you are done!

If there was an error, find the location of the bluetooth service
	
	"systemctl status bluetooth.service"
	
Edit the bluetooth.service (as root) and look for: "ExecStart=/usr/libexec/bluetooth/bluetoothd
Append --compat to the end of the line
The line should now look like this
	
	ExecStart=/usr/libexec/bluetooth/bluetoothd --compat
	
Now exit and save like before.
