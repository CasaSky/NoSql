import com.github.koraktor.steamcondenser.exceptions.SteamCondenserException;
import com.github.koraktor.steamcondenser.exceptions.WebApiException;
import com.github.koraktor.steamcondenser.steam.community.SteamGame;
import com.github.koraktor.steamcondenser.steam.community.SteamGroup;
import com.github.koraktor.steamcondenser.steam.community.SteamId;
import org.json.JSONException;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by abq304 on 08.06.2016.
 */
public class SteamUI extends JFrame implements Runnable{
    private JTabbedPane sstatistics;
    private JPanel panel1;
    private JList scanOutputList;
    private JTable PlayerTable;
    private JTextField textField1;
    private JTable gamestable;
    private JButton Search;
    private JTextPane playersFetchedTextPane;
    private JTextPane gamesFetchedTextPane;
    private JTextPane totalOnlinePlayersTextPane;
    private JTextArea playerInspectorName;
    private JTextArea PlayerInspectorShort;
    private JTextArea GamesCount;
    private JTextPane textPane1;
    private JTextPane textPane2;
    private JTextPane textPane3;
    private DefaultListModel listModel;
    private DefaultTableModel tableModellPlayer;
    private DefaultTableModel tableModellGame;


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
    }

    public void run() {

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
}
