import com.github.koraktor.steamcondenser.exceptions.SteamCondenserException;
import com.github.koraktor.steamcondenser.exceptions.WebApiException;
import com.github.koraktor.steamcondenser.steam.community.SteamGame;
import com.github.koraktor.steamcondenser.steam.community.SteamId;
import org.json.JSONException;

import javax.swing.*;
import java.util.*;

/**
 * Created by abq304 on 08.06.2016.
 */
public class ScanThread implements Runnable {
    //private JList guiList ;
    private SteamId crazycat;
    public List<Long> listDone;
    public List<Long> listDue;
   // private DefaultListModel listModel;
    private List<SteamId> tempList;
    private static final Long id = new Long("76561197983553019");
    private JList scanOutputList;
    public boolean isServicerequired() {
        return servicerequired;
    }

    private ArrayList<SteamId> allSteamID;
    private ArrayList<SteamGame> allGames;

    private SteamUI ui;
    private DbManager dbManager;

    public void setServicerequired(boolean servicerequired) {
        this.servicerequired = servicerequired;
    }

    private boolean servicerequired = true;

    public ScanThread(SteamUI ui) throws SteamCondenserException {
        this.ui = ui;

        allGames = new ArrayList<SteamGame>();
        allSteamID = new ArrayList<SteamId>();

        this.servicerequired = true;
        this.crazycat = SteamId.create(id);
        listDone = new ArrayList<Long>();
        listDue = new ArrayList<Long>();
        tempList = new ArrayList<SteamId>();
        listDue.add(crazycat.getSteamId64());
        dbManager = new DbManager();
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
            try {
                fetchAllFriends();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void fetchAllFriends() throws JSONException {
        Long tempIdLong = listDue.get(0);
        if (tempIdLong == null) {
            throw new IllegalArgumentException("List due is Empty");
        }
        listDue.remove(0);
        try {
            dbManager.insertSteamUser(SteamId.create(tempIdLong)); // NEO4J
            tempList = getFriends(SteamId.create(tempIdLong));
            this.listDone.add(tempIdLong);
        } catch (SteamCondenserException e) {
            e.printStackTrace();
        }
        for(SteamId currentSteamId : tempList){

            Long currentSteamIdLong = currentSteamId.getSteamId64();
            if(!(listDone.contains(currentSteamIdLong))){
                try {
                    dbManager.insertSteamUser(SteamId.create(currentSteamIdLong));
                    dbManager.insertFriendRelation(SteamId.create(tempIdLong), SteamId.create(currentSteamIdLong));
                } catch (SteamCondenserException e) {
                    e.printStackTrace();
                }

                SteamId actualSteamId  = null;
                try {
                    actualSteamId = SteamId.create(currentSteamIdLong);
                    String nickname = actualSteamId.getNickname();
                    //System.out.println(nickname);
                    if (actualSteamId.getPrivacyState().equals("public")) {
                        this.listDue.add(currentSteamIdLong);
                        //fetchAllGamesOf(actualSteamId);
                        //ui.AddPlayer(actualSteamId);
                    }
                    //System.out.println(currentSteamId);
                } catch (SteamCondenserException e) {
                    e.printStackTrace();
                }

            }
        }
    }


    public void putinListifnotpresent(SteamGame game) throws JSONException, WebApiException {
        boolean existsInList = false;
        for (SteamGame elem : allGames){
            if(elem.getAppId() == game.getAppId()){
                existsInList = true;
                System.out.println("<------------------------we got a double----------------------->");
            }
        }

        if(!(existsInList)){
            allGames.add(game);
            ui.addGame(game);
        }
    }



    public void fetchAllGamesOf(SteamId id) throws SteamCondenserException, JSONException {
        HashMap games = id.getGames();
        Set<Map.Entry> sets = (Set<Map.Entry>) games.entrySet();
        for (Map.Entry<Integer, SteamGame> entry : sets) {
            dbManager.insertSteamGame(entry.getKey(), entry.getValue());
            dbManager.insertSpielBeziehung(id, entry.getKey());

        }
    }
}
