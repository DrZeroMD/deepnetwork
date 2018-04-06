import DeepNetwork.GetTorrentFileResponse;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class TorrentFileThread extends Thread {
    private ServerSocket responseSocket;

    TorrentFileThread(ServerSocket responseSocket){
        this.responseSocket = responseSocket;
    }

    @Override
    public void run(){
        try{
            //Accept connections
            Socket socket = responseSocket.accept();

            //Set Response todo
            GetTorrentFileResponse response = new GetTorrentFileResponse("","");

            //Reply
            ObjectOutputStream stream = new ObjectOutputStream(socket.getOutputStream());
            stream.writeObject(response);

            //Close
            stream.close();
            socket.close();

        } catch (IOException e){
            e.printStackTrace();

        }
    }
}