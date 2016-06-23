import com.github.koraktor.steamcondenser.exceptions.SteamCondenserException;
import com.github.koraktor.steamcondenser.steam.community.SteamGame;
import com.github.koraktor.steamcondenser.steam.community.SteamId;
import io.innerloop.neo4j.client.Connection;
import io.innerloop.neo4j.client.Neo4jClient;
import io.innerloop.neo4j.client.Neo4jServerException;
import io.innerloop.neo4j.client.RowStatement;
import redis.clients.jedis.Jedis;


/**
 * Created by abq304 on 08.06.2016.
 */
public class DbManager {
        Neo4jClient client;
        Jedis jedis;
        Connection connection;

        public DbManager() {

                client = new Neo4jClient("http://localhost:7474/db/data");
                jedis = new Jedis("localhost");
        }

        public void insertSteamUser(SteamId id){
               try {
                       connection = client.getConnection();
                       String create = "MERGE (user:User {id: '"+id.getSteamId64() +"' , name:'" + clearName(id.getNickname()) + "'})";
                       RowStatement statementInserUser = new RowStatement(create);
                       connection.add(statementInserUser);
                       connection.flush();
                       connection.commit();
               }catch(Neo4jServerException e) {
                       System.out.println(e.toString());

               }
        }

        public String clearName(String name) {
                return name.replace(" ","_").replace("'","");

        }

        public void insertSteamGame(Integer gameId, SteamGame steamGame) {

                try {
                        connection = client.getConnection();
                        String createGame = "MERGE (game:Game {id: '"+gameId+"' , spielname:'" + clearName(steamGame.getName()) + "'})";
                        RowStatement statementInserGame = new RowStatement(createGame);
                        connection.add(statementInserGame);
                        connection.flush();
                        connection.commit();
                }catch(Neo4jServerException e) {
                }
}

        public void insertSpielBeziehung(SteamId id, Integer gameId) {
                try {
                        connection = client.getConnection();
                        String createPlay = "MATCH (user:User {id:'"+id.getSteamId64()+"'}), (game:Game {id:'"+gameId+"'}) CREATE UNIQUE (user)-[:PLAYS]->(game)";
                        RowStatement statementInsertPlay = new RowStatement(createPlay);
                        connection.add(statementInsertPlay);
                        connection.flush();
                        connection.commit();
                }catch(Neo4jServerException e) {

                }
        }

        public void insertFriendRelation(SteamId id1, SteamId id2) {
                try{
                connection = client.getConnection();
                String createFriend = "MATCH (user:User {id:'"+id1.getSteamId64()+"'}), (d:User {id:'"+id2.getSteamId64()+"'}) CREATE UNIQUE (user)-[:FRIEND]-(d)";
                        RowStatement statementInsertFriend = new RowStatement(createFriend);
                        connection.add(statementInsertFriend);
                        connection.flush();
                        connection.commit();
                }catch(Neo4jServerException e) {
                        System.out.println("------>DbManager.insertSteamGame");

                }
        }


        /*public StatementResult getPlayers(){
                return null;
        }*/

        /*public List<StatementResult> getAllFriends(){
                return null;
        }*/

        public void updateTimePlayed(SteamId userid, Integer value) throws SteamCondenserException {

                String deciderString = "fine";

                if (value!=0)
                System.out.println("------->"+value);
                deciderString = jedis.set(""+userid.getSteamId64(),value.toString());
/*
                if(deciderString == null){
                        String currentValue = jedis.get(""+id.getSteamId64()+"");
                        currentValue = currentValue + ","+id.getHoursPlayed()+"";
                        jedis.set(""+id.getSteamId64()+"",currentValue,"XX");
                }*/
        }

        /*public List<Double> getTimePlayed(SteamId id){
                return this.getTimePlayed(id.getSteamId64());
        }*/

        /*public List<Double> getTimePlayed(Long value){
                ArrayList<Double> returnList = new ArrayList<Double>();
                String splitstring;
                splitstring = jedis.get(value.toString());
                String[] stringArray;
                stringArray = splitstring.split(",");
                for(int i = 0; i <= stringArray.length;i++){
                        returnList.add(Double.valueOf(stringArray[i]));
                }
                return returnList;
        }*/
 }
