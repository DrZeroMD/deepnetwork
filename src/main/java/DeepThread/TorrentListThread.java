package DeepThread;

import DeepNetwork.GetTorrentListResponse;
import DeepServer.ServerThreadStuff;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class TorrentListThread extends Thread{
    private ServerSocket responseSocket;
    private ServerThreadStuff callingThread;

    public TorrentListThread(ServerThreadStuff callingThread, ServerSocket responseSocket){
        this.callingThread = callingThread;
        this.responseSocket = responseSocket;
    }

    @Override
    public void run(){
        try{
            //Accept connections
            Socket socket = responseSocket.accept();

            //Retrieve data
            GetTorrentListResponse response = TorrentList.get();

            //Send data back
            ObjectOutputStream stream = new ObjectOutputStream(socket.getOutputStream());
            stream.writeObject(response);

            //Close
            stream.close();
            socket.close();

        } catch (IOException e){
            e.printStackTrace();
            DeepLogger.log(e.getMessage());

        }

        callingThread.closeThread();
    }
}
