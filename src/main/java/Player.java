import com.github.koraktor.steamcondenser.steam.community.SteamGroup;
import com.github.koraktor.steamcondenser.steam.community.SteamId;
import com.sun.prism.impl.Disposer;
import org.neo4j.driver.v1.Record;
import org.neo4j.driver.v1.StatementResult;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by abq304 on 22.06.2016.
 */
public class Player {
    private long playerID;
    private String nickName;
    private List<Player> friends;
    private List<SteamGroup> groups;
    private List<game> games;
    private List<Double> timePlayed;


    public Player(StatementResult result){
        while (result.hasNext()){
            Record record = result.next();
            this.playerID = Long.valueOf(record.get("title").toString());
            this.nickName = record.get("name").toString();

        }
    }

    public void addFriend(Player player){
        this.friends.add(player);
        player.addFriend(this);
    }

    public void addGame(game game){
        this.games.add(game);
    }

    public void addTimePlayed(double value){
        timePlayed.add(value);
    }










}
