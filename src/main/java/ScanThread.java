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
            catch (SteamCondenserException e2) {
                e2.printStackTrace();
            }
        }
    }

    private void fetchAllFriends() throws JSONException, SteamCondenserException {
        Long tempIdLong = listDue.get(0);
        SteamId tempSteamId = SteamId.create(tempIdLong);
        if (tempIdLong == null) {
            throw new IllegalArgumentException("List due is Empty");
        }
        listDue.remove(0);

        dbManager.insertSteamUser(tempSteamId); // NEO4J
        tempList = getFriends(tempSteamId);
        this.listDone.add(tempIdLong);

        for(SteamId currentSteamId : tempList){
            Long currentSteamIdLong = currentSteamId.getSteamId64();
            SteamId actualSteamId = SteamId.create(currentSteamIdLong);
            if(!(listDone.contains(currentSteamIdLong))){
                dbManager.insertSteamUser(actualSteamId);
                dbManager.insertFriendRelation(tempSteamId, actualSteamId);

                if (actualSteamId.getPrivacyState().equals("public")) {
                    this.listDue.add(currentSteamIdLong);
                    //fetchAllGamesOf(actualSteamId);
                    ui.AddPlayer(actualSteamId);
                }
                    //System.out.println(currentSteamId);
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
