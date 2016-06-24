import com.github.koraktor.steamcondenser.exceptions.SteamCondenserException;
import com.github.koraktor.steamcondenser.exceptions.WebApiException;
import com.github.koraktor.steamcondenser.steam.community.SteamGame;
import com.github.koraktor.steamcondenser.steam.community.SteamGroup;
import com.github.koraktor.steamcondenser.steam.community.SteamId;
import io.innerloop.neo4j.client.Graph;
import io.innerloop.neo4j.client.Node;
import org.json.JSONException;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

/**
 * Created by abq304 on 08.06.2016.
 */
public class SteamUI extends JFrame implements Runnable{
    private JTabbedPane sstatistics;
    private JPanel panel1;
    private JList scanOutputList;
    private JTable PlayerTable;
    private JTable gamestable;
    private JTextField InspectorID;
    private JTextField inspectorNumberOfGames;
    private JTextField inspectorNoOfFriends;
    private JTextField inspectorKategory;
    private JTextField InspectorName;
    private JLabel Name;
    private JTextField InspectorHoursPlayed;
    private JTextField IdtoLoad;
    private JButton loadButton;
    private JButton setCategory;
    private JTextField gameID;
    private JTextField kategory;
    private DefaultListModel listModel;
    private DefaultTableModel tableModellPlayer;
    private DefaultTableModel tableModellGame;
    private ScanThread manager;

    public SteamUI() {
        super("Steam Nosql");
        setContentPane(panel1);
        pack();
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setVisible(true);
        tableModellGame = new DefaultTableModel();
        tableModellGame.addColumn("GameID");
        tableModellGame.addColumn("Gamename");
        tableModellGame.addColumn("Shortname");
        tableModellGame.addColumn("playercount");

        listModel = new DefaultListModel();
        tableModellPlayer = new DefaultTableModel();
        tableModellPlayer.addColumn("PlayerID");
        tableModellPlayer.addColumn("Playername");
        tableModellPlayer.addColumn("Realname");
        loadButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    load();
                } catch (SteamCondenserException e1) {
                    e1.printStackTrace();
                }
            }
        });
        setCategory.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setKategory();
            }
        });
    }

    public void run() {

    }

    public void setKategory(){

        String Kategory = this.kategory.getText();
        Long value = Long.valueOf(this.gameID.getText());
        this.manager.setKategory(Kategory,value);
    }

    public void setManager(ScanThread manager){
        this.manager = manager;
    }

    public void addGame(SteamGame game) throws JSONException, WebApiException {
        String[] tablearray = new String[4];
        tablearray[0] = String.valueOf(game.getAppId());
        tablearray[1] = game.getName();
        tablearray[2] = game.getShortName();
        tablearray[3] = String.valueOf(game.getPlayerCount());
        tableModellGame.addRow(tablearray);
        gamestable.setModel(tableModellGame);
    }

    public void load() throws SteamCondenserException {
        Graph graph = this.manager.getPlayer(Long.valueOf(IdtoLoad.getText()));
        Graph graph2 = this.manager.getFriendsOf(Long.valueOf(IdtoLoad.getText()));
        for(Node elem : graph.getNodes()){
            for(Map.Entry<String,Object> InnerElem : elem.getProperties().entrySet()){
                System.out.println(InnerElem.getKey());
                System.out.println(InnerElem.getValue());
                if(InnerElem.getKey().equals("id")){

                    this.InspectorHoursPlayed.setText(String.valueOf(this.getHourstimePlayed(Long.valueOf(InnerElem.getValue().toString()))));
                }
                if(InnerElem.getKey().equals("name")){
                    this.InspectorName.setText(InnerElem.getValue().toString());
                }
            }
        }



        int numberOfFriends = 0;
        for (Node elem : graph2.getNodes()){
            numberOfFriends++;
            for(Map.Entry<String,Object> InnerElem2 : elem.getProperties().entrySet()){
                // do something with his friends here
            }
        }
        String kategory = "";

        if(Integer.valueOf(this.inspectorNoOfFriends.getText()) < 10){
            kategory = kategory + "Soziophobe";
        }
        if(Integer.valueOf(this.inspectorNoOfFriends.getText()) > 10){
            kategory = kategory + "Antisocial";
        }
        if(Integer.valueOf(this.inspectorNoOfFriends.getText()) > 20){
            kategory = kategory + "Introvert";
        }
        if(Integer.valueOf(this.inspectorNoOfFriends.getText()) > 30){
            kategory = kategory + "Social";
        }

        if(Integer.valueOf(this.InspectorHoursPlayed.getText()) < 10){
            kategory = kategory + "Occasion Gamer";
        }
        else if(Integer.valueOf(this.InspectorHoursPlayed.getText()) > 40){
            kategory = kategory + " Hardcore Gamer";
        }
        else if(Integer.valueOf(this.InspectorHoursPlayed.getText()) > 30){
            kategory = kategory + " Core Gamer";
        }
        else if(Integer.valueOf(this.InspectorHoursPlayed.getText()) > 10){
            kategory = kategory + " Gamer";
        }



        this.inspectorKategory.setText(kategory);
    }

    public void AddPlayer(SteamId id) {
        listModel.addElement(id.getNickname());
        scanOutputList.setModel(listModel);
        String[] tablearray = new String[3];
        tablearray[0] = String.valueOf( id.getSteamId64());
        tablearray[1] = id.getNickname();
        tablearray[2] = id.getRealName();
        tableModellPlayer.addRow(tablearray);
        PlayerTable.setModel(tableModellPlayer);

    }

    public void AddGame(SteamGame game){

    }


    public JList getScanOutputList() {
        return scanOutputList;
    }

    public void setScanOutputList(JList scanOutputList) {
        this.scanOutputList = scanOutputList;
    }

    public int getHourstimePlayed(Long value) throws SteamCondenserException {
        SteamId id = SteamId.create(value);
        int dub = 0;
        int ammountofGames = 0;
        for(Map.Entry<Integer,SteamGame> elem : id.getGames().entrySet()){
             dub = dub + id.getRecentPlaytime(elem.getKey()) / 60;
            ammountofGames++;
        }

        inspectorNumberOfGames.setText(String.valueOf(ammountofGames));
        this.inspectorNoOfFriends.setText(String.valueOf(id.getFriends().length));
        return dub;

    }


}
