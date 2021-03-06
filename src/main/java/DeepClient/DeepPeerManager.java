package DeepClient;

import DeepThread.DeepLogger;

import java.io.IOException;
import java.net.InetAddress;
import java.util.*;


public class DeepPeerManager {
    private ArrayList<String> array;
    private Queue<String> priority;
    private int dead;
    private int used;

    DeepPeerManager(){
        array = new ArrayList<>();
        priority = null;
    }

    public void setPeers(ArrayList<String> peers){
        array = peers;
        used = array.size();
        dead = array.size();
    }

    public String getPeer(){
        String peer = getPriorityPeer();

        if(peer != null)
            return peer;

        return getRandomPeer();
    }

    public List<String> getArray() {
        return array.subList(0,dead);
    }

    public void setPriority(Collection<String> priority){ this.priority = new LinkedList<>(priority); }

    public boolean isEmpty(){
        return dead == 0;
    }

    public boolean checkPeers(String webserver){

        try {
            String host = InetAddress.getLocalHost().getHostName();
            if(array.remove(host)) {
                DeepLogger.log("Cannot download something from yourself");
                if(used > array.size())
                    used--;
                if(dead > array.size())
                    dead--;
            }

        } catch (IOException e){
            DeepLogger.log(e.getMessage());
        }

        return array.size() == 1 && array.contains(webserver);
    }

    public void dead(String host){
        if(dead != 0) {
            int index = array.indexOf(host);
            if(index < dead) {
                dead--;
                swap(index, dead);
            } else
                DeepLogger.log("Host already dead");
        } else
            DeepLogger.log("Out of peers");
    }

    private String swap(int x, int y){
        String temp = array.set(y, array.get(x));
        return array.set(x, temp);
    }

    private String getRandomPeer(){
        if(used == 0)
            used = dead;

        Random rand = new Random();
        int index = rand.nextInt(used);
        used--;
        return swap(index,used);
    }

    private String getPriorityPeer(){
        // if queue does not exist
        if(priority == null)
            return null;

        String peer = priority.poll();

        // if queue is empty
        if(peer != null)
            priority.add(peer);

        return peer;
    }

}
