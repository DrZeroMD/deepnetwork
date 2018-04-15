/*-----------------------------*
	04/10/2018
	Mukunda Mensah
	Dr. Linda Null
	COMP 512
-------------------------------*/

import DeepManager.DeepThreadManager;
import DeepManager.ServerStartup;
import DeepNetwork.PortResponse;
import DeepThread.*;

import java.io.DataOutputStream;
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
            DeepThreadManager manager = new DeepThreadManager(100);
            System.out.println("~DeepServer Started~");

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
