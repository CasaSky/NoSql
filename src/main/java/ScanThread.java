import com.github.koraktor.steamcondenser.exceptions.SteamCondenserException;
import com.github.koraktor.steamcondenser.steam.community.SteamGame;
import com.github.koraktor.steamcondenser.steam.community.SteamId;
import org.neo4j.driver.v1.Session;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Created by abq304 on 08.06.2016.
 */
public class ScanThread implements Runnable {
    private JList guiList ;
    private SteamId crazycat;
    private List<Long> listDone;
    private List<Long> listDue;
    private DefaultListModel listModel;
    private List<SteamId> tempList;
    private static final Long id = new Long("76561198061287993");
    private JList scanOutputList;
    public boolean isServicerequired() {
        return servicerequired;
    }

    public void setServicerequired(boolean servicerequired) {
        this.servicerequired = servicerequired;
    }

    private boolean servicerequired = true;

    public ScanThread(JList scanOutputList) throws SteamCondenserException {
        //this.guiList = guiList;
        this.servicerequired = true;
        this.crazycat = SteamId.create(id);
        this.scanOutputList = scanOutputList;
        listDone = new ArrayList<Long>();
        listDue = new ArrayList<Long>();
        tempList = new ArrayList<SteamId>();
        listDue.add(crazycat.getSteamId64());
        listModel = new DefaultListModel();
    }

    public List<SteamId> getFriends(SteamId id) {
        if (id == null)
            throw new IllegalArgumentException("id must be not null!");
        List<SteamId> result = null;
        try {
            result = Arrays.asList(id.getFriends());
        } catch (SteamCondenserException e) {
            e.printStackTrace();
        }
        return result;
    }

    public void run() {
        while(servicerequired) {
            fetchAllFriends();
            addIfNotInList();
        }
    }

    private void addIfNotInList(){
        scanOutputList.setModel(listModel);
    }

    private void fetchAllFriends() {
        //TODO ist User in Neo4J vorhanden, speichern
        Long tempIdLong = listDue.get(0);
        if (tempIdLong == null) {
            throw new IllegalArgumentException("List due is Empty");
        }
        listDue.remove(0);
        try {
            tempList = getFriends(SteamId.create(tempIdLong));
            this.listDone.add(tempIdLong);
        } catch (SteamCondenserException e) {
            e.printStackTrace();
        }
        for(SteamId currentSteamId : tempList){
            Long currentSteamIdLong = currentSteamId.getSteamId64();
            if(!(listDone.contains(currentSteamIdLong))){
                //TODO ist User in Neo4J, speichern
                //TODO Kennt-Beziehunng von 2 Usern nicht vorhanden, einfügen
                SteamId actualSteamId  = null;
                try {
                    actualSteamId = SteamId.create(currentSteamIdLong);
                    String nickname = actualSteamId.getNickname();
                    //System.out.println(nickname);
                    if (actualSteamId.getPrivacyState().equals("public")) {
                        this.listDue.add(currentSteamIdLong);
                        listModel.addElement(nickname);
                        fetchAllGamesOf(actualSteamId);
                    }
                    //System.out.println(currentSteamId);
                } catch (SteamCondenserException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    public void fetchAllGamesOf(SteamId id) throws SteamCondenserException {
        HashMap games = id.getGames();
        for (Object game : games.values()) {
            if(game instanceof SteamGame){
                SteamGame steamGame = (SteamGame) game;
                //TODO SteamGame in die DB speichern, spielbeziehung : id -> game
                System.out.println(steamGame.getName());
            }

        }
    }
}
