package DeepManager;

import DeepNetwork.*;
import DeepThread.DeepLogger;
import DeepThread.TorrentFolder;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class DeepManager extends Thread implements ThreadStuff{
    private HashMap<String, DeepTorrentManager> torrents;
    private BlockingQueue<String> doneQueue;
    private BlockingQueue<Request> fromUI;
    private BlockingQueue<Response> toUI;

    private static DeepManager DM;
    private String server;
    private int port;

    public DeepManager(BlockingQueue<Request> fromUI, BlockingQueue<Response> toUI) {
        torrents = new HashMap<>();
        doneQueue = new LinkedBlockingQueue<>();
        this.fromUI = fromUI;
        this.toUI = toUI;
        server = "ada";
        port = 6345;
    }

    public static synchronized DeepManager getInstance(BlockingQueue<Request> fromUI,
                                                       BlockingQueue<Response> toUI) {
        File file = new File(TorrentFolder.getSegments(), ".dm");

        if (DM == null)
            if(file.exists()){
                Gson gson = new Gson();
                try (FileReader reader = new FileReader(file)) {
                    DM = gson.fromJson(reader,DeepManager.class);
                }
                catch (Exception e){
                    e.printStackTrace();
                    DeepLogger.log(e.getMessage());
                }
            } else
                DM = new DeepManager(fromUI, toUI);

        return DM;
    }

    @Override
    public void run(){
        System.out.println("~DeepManager Started~");
        boolean on = true;
        while(on){ //user is not ended

                if(fromUI.size() > 0) {
                    Request r = fromUI.poll();

                    // user.request1 (get new torrent list)
                    if (r instanceof GetTorrentListRequest) {
                        ArrayList<String> test = new ArrayList<>();
                        test.add("test1.file");
                        test.add("test2.file");
                        test.add("test3.file");
                        toUI.add(new GetTorrentListResponse(test));
                    }

                    // user.request2 (get torrent)

                    if(r instanceof GetTorrentFileRequest){
                        startTorrent(((GetTorrentFileRequest) r).getFilename());
                    }

                    // user shutdown
                    if (r instanceof ShutDownRequest) {
                        for(DeepTorrentManager dtm : torrents.values()){
                            BlockingQueue<Request> q = dtm.getFromDM();
                            q.add(new ShutDownRequest());
                        }
                        on = false;
                        System.out.println("~DeepManager Closed~");
                    }
                }

                // check done queue
                while (!doneQueue.isEmpty()) {
                    String s = doneQueue.poll();
                    toUI.offer(new GetFilePieceResponse(s, 0, null));
                }


                try {
                    sleep(5);
                } catch (InterruptedException e){
                    DeepLogger.log(e.getMessage());
                }
            //call back for UI
        }
    }

    @Override
    public synchronized void closeThread(boolean flag, String file){
        //todo
        //if file still needs requesting
        if(flag){
            doneQueue.add(file);
        }

    }

    private void startTorrent(String filename) {
        if (!torrents.containsKey(filename)) {
            DeepTorrentManager dtm = new DeepTorrentManager(filename, server, port, new LinkedBlockingQueue<>(), this);
            torrents.put(filename, dtm);
            dtm.start();
        }
        else
            DeepLogger.log("DeepManager: startTorrent: Torrent Manager " + filename + " already created.");
    }

}