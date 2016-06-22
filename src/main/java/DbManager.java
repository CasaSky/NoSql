import com.github.koraktor.steamcondenser.steam.community.SteamGame;
import com.github.koraktor.steamcondenser.steam.community.SteamId;
import org.neo4j.driver.v1.*;
import redis.clients.jedis.Jedis;

import java.beans.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by abq304 on 08.06.2016.
 */
public class DbManager {
        Driver driver = GraphDatabase.driver("bolt://localhost", AuthTokens.basic("neo4j", "neo4j"));
        Session session = driver.session();
        Jedis jedis = new Jedis("localhost");


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


        public StatementResult getPlayers(){
                return null;
        }

        public List<StatementResult> getAllFriends(){
                return null;
        }

        public void updateTimePlayed(SteamId id){
                String value = "";
                String deciderString = "fine";
                deciderString = jedis.set(""+id.getSteamId64()+"",""+id.getHoursPlayed()+",","NX");

                if(deciderString == null){
                        String currentValue = jedis.get(""+id.getSteamId64()+"");
                        currentValue = currentValue + ","+id.getHoursPlayed()+"";
                        jedis.set(""+id.getSteamId64()+"",currentValue,"XX");
                }
        }

        public List<Double> getTimePlayed(SteamId id){
                return this.getTimePlayed(id.getSteamId64());
        }

        public List<Double> getTimePlayed(Long value){
                ArrayList<Double> returnList = new ArrayList<Double>();
                String splitstring;
                splitstring = jedis.get(value.toString());
                String[] stringArray;
                stringArray = splitstring.split(",");
                for(int i = 0; i <= stringArray.length;i++){
                        returnList.add(Double.valueOf(stringArray[i]));
                }
                return returnList;
        }
 }
