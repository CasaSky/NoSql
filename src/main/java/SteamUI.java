import com.github.koraktor.steamcondenser.exceptions.SteamCondenserException;
import com.github.koraktor.steamcondenser.steam.community.SteamId;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by abq304 on 08.06.2016.
 */
public class SteamUI extends JFrame implements Runnable{
    private JTabbedPane tabbedPane1;
    private JPanel panel1;
    private JList scanOutputList;


    public SteamUI() {
        super("Steam Nosql");
        setContentPane(panel1);
        pack();
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setVisible(true);
    }

    public void run() {

    }

    public JList getScanOutputList() {
        return scanOutputList;
    }

    public void setScanOutputList(JList scanOutputList) {
        this.scanOutputList = scanOutputList;
    }
}
