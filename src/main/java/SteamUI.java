import com.github.koraktor.steamcondenser.exceptions.SteamCondenserException;
import com.github.koraktor.steamcondenser.steam.community.SteamId;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by abq304 on 08.06.2016.
 */
public class SteamUI extends JFrame {
    private JTabbedPane tabbedPane1;
    private JPanel panel1;
    private JButton scanButton;
    private JList scanOutputList;
    private JButton StopButton;



    public SteamUI() {
        super("Steam Nosql");
        setContentPane(panel1);
        pack();



        setDefaultCloseOperation(EXIT_ON_CLOSE);
        scanButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {

            }
        });
    }
}
