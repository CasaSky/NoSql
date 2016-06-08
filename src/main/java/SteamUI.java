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
    private JButton scanButton;
    private JList scanOutputList;
    private JButton StopButton;
    private ScanThread scanThread;

    private Thread workerThread;
    private boolean isOn = false;


    public SteamUI(Thread workerThread, ScanThread scanThread) {
        super("Steam Nosql");
        setContentPane(panel1);
        pack();

        this.workerThread = workerThread;
        doActions();
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setVisible(true);
        this.scanThread = scanThread;
    }

    public void run() {

    }

    public void doActions() {
        scanButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                if(isOn) {
                    //workerThread.stop();
                    isOn = false;
                    scanThread.setServicerequired(false);
                }
                else {
                    //workerThread.start();
                    isOn = true;
                    scanThread.setServicerequired(true);
                }
            }
        });
    }
}
