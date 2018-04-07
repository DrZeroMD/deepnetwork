import DeepNetwork.GetPeersRequest;
import DeepNetwork.GetPeersResponse;
import DeepNetwork.GetTorrentFileResponse;
import DeepNetwork.Request;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class GetPeersThread extends Thread{
    private ServerSocket responseSocket;
    private GetPeersRequest request;

    GetPeersThread(ServerSocket responseSocket, Request request){
        this.responseSocket = responseSocket;
        this.request = (GetPeersRequest) request;
    }

    @Override
    public void run(){
        try{
            //Accept connections
            Socket socket = responseSocket.accept();

            //Set Response
            GetPeersResponse response = Peers.get(request.getFilename());

            //Reply
            ObjectOutputStream stream = new ObjectOutputStream(socket.getOutputStream());
            stream.writeObject(response);

            //Close
            stream.close();
            socket.close();

        } catch (IOException e){
            e.printStackTrace();
            DeepLogger.log(e.getMessage());
        }
    }
}
