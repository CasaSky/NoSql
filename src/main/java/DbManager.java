import com.github.koraktor.steamcondenser.steam.community.SteamGame;
import com.github.koraktor.steamcondenser.steam.community.SteamId;
import org.neo4j.driver.v1.AuthTokens;
import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.GraphDatabase;
import org.neo4j.driver.v1.Session;

/**
 * Created by abq304 on 08.06.2016.
 */
public class DbManager {
    Driver driver = GraphDatabase.driver("bolt://localhost", AuthTokens.basic("neo4j", "neo4j"));

    Session session = driver.session();

    public void insertSteamUser(SteamId id){
        session.run("CREATE ("+id.getSteamId64()+":User { name: '"+id.getNickname()+"' })");
    }

    public void insertSteamGame(Integer gameId, SteamGame steamGame) {
        session.run("CREATE ("+gameId+":Game { name: '"+steamGame.getName()+"' })");
    }

    public void insertSpielBeziehung(SteamId id, Integer gameId) {
        session.run("MATCH ("+id.getSteamId64()+":User), ("+gameId+":Game)\n" +
                "CREATE ("+id.getSteamId64()+")-[:Spielt]->("+gameId+")");
    }

    public void insertFriendRelation(SteamId id1, SteamId id2) {
        session.run("MATCH ("+id1.getSteamId64()+":User), ("+id2.getSteamId64()+":User)\n" +
                "CREATE ("+id1.getSteamId64()+")<-[:FRIEND]->("+id2.getSteamId64()+")");
    }


}
