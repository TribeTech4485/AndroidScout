from bluetooth import *

server_sock=BluetoothSocket( RFCOMM )
server_sock.bind(("",PORT_ANY))
server_sock.listen(1)

port = server_sock.getsockname()[1]

uuid = "94f39d29-7d6d-437d-973b-fba39e49d4ee"

advertise_service( server_sock, "4485-M8",
                   service_id = uuid,
                   service_classes = [ uuid, SERIAL_PORT_CLASS ],
                   profiles = [ SERIAL_PORT_PROFILE ], 
#                   protocols = [ OBEX_UUID ] 
                    )

while True:
	filePath = "TeamData.list"
	f = open(filePath,"a+")
	f.write("\n\n!-----------------------\n\n")
	large_data = ""
	client_sock, client_info = server_sock.accept()
	while True:          
		print "Waiting for connection on RFCOMM channel %d" % port
	
		#client_sock, client_info = server_sock.accept()
		print "Accepted connection from ", client_info	

	
		try:
		        data = client_sock.recv(1024)
        		if len(data) == 0: break
	     		print "received [%s]" % data

			large_data += data

			if "#" in data: 
				print "Writing data to [%s]...." % filePath
				f.write(large_data + "\n")
				f.close()

				print "disconnected"

				client_sock.close()
				print "all done"

				break

			#large_data += data

			print "all data received: [%s]" % large_data

		


		except IOError:
			pass

		except KeyboardInterrupt:

			print "disconnected"

			f.close()
			client_sock.close()
			server_sock.close()
			print "all done"

			break
