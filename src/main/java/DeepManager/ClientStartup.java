package DeepManager;

import DeepNetwork.GetPort;
import DeepThread.DeepLogger;
import DeepThread.TorrentFolder;

public class ClientStartup {
    public static void main(String[] args){
        // What is the structure??
        new TorrentFolder(); //todo Is this okay? .segments, .torrents fine. To_Torrents? Change to downloads
        new GetPort();
        //what todo !!
        new DeepLogger(System.currentTimeMillis());
    }
}