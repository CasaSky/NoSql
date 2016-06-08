import com.github.koraktor.steamcondenser.exceptions.SteamCondenserException;

import javax.swing.*;

/**
 * Created by abq304 on 08.06.2016.
 */
public class Main {

    public static void main(String[] args) throws SteamCondenserException {



        SteamUI ui = new SteamUI();

        ScanThread scanThread = new ScanThread(ui.getScanOutputList());
        Thread t1 = new Thread(scanThread);
        Thread t2 = new Thread(ui);
        t2.start();
        t1.start();


    }
}
