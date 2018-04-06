import DeepNetwork.GetPort;

import java.io.File;
import java.util.ArrayList;

public class ServerStartup {
    public static void main(String[] args){
        //create to_torrent and .torrents dir
        new TorrentFolder();
        ArrayList<String> torrents = MakeTorrents.makeAllTorrents();
        new TorrentList(torrents);
        new GetPort();
    }
}
