/*-----------------------------*
	04/10/2018
	Mukunda Mensah
	Dr. Linda Null
	COMP 512
-------------------------------*/

import DeepServer.DeepServerManager;
import DeepServer.ServerStartup;
import DeepNetwork.PortResponse;
import DeepThread.*;

import java.io.ObjectInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.ServerSocket;

class DeepServer {
    private static final int PORT = 6345;

    public static void main(String [] args) {

        ServerStartup.main(null);

        try {
            ServerSocket serverSocket = new ServerSocket(PORT);
            //serverSocket.setSoTimeout(10000); //this is 10 seconds
            PortResponse newPort;
            Socket socket;
            DeepServerManager manager = new DeepServerManager(100);
            DeepLogger.log("~DeepServer Started~");

            while(true){
                // accept
                socket = serverSocket.accept();

                try {
                    ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
                    Object object = input.readObject();
                    newPort = manager.reception(object);

                    if(newPort.getPort() != -1) {
                        ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
                        output.writeObject(newPort);
                        output.close();
                        DeepLogger.log("Sending new port # " + newPort.getPort());
                        int x = 0;
                    }

                    input.close();

                } catch (ClassNotFoundException e){
                    DeepLogger.log(e.getMessage());
                }

                // reset
                socket.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
            DeepLogger.log(e.getMessage());
        }
    }
}
