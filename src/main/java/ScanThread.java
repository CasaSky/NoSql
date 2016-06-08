import com.github.koraktor.steamcondenser.exceptions.SteamCondenserException;
import com.github.koraktor.steamcondenser.steam.community.SteamId;

import javax.swing.*;

/**
 * Created by abq304 on 08.06.2016.
 */
public class ScanThread implements Runnable {
    private JList guiList ;
    private SteamId crazycat


    public boolean isServiceRequired() {
        return serviceRequired;
    }

    public void setServiceRequired(boolean serviceRequired) {
        this.serviceRequired = serviceRequired;
    }

    private boolean serviceRequired;
    public ScanThread(JList guiList) throws SteamCondenserException {
        this.guiList = guiList;
        this.serviceRequired = false;

    }



    public void run() {
        while(serviceRequired){

        }
    }
}
