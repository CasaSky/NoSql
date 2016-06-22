import com.github.koraktor.steamcondenser.steam.community.SteamGame;
import com.github.koraktor.steamcondenser.steam.community.SteamId;
import io.innerloop.neo4j.client.Connection;
import io.innerloop.neo4j.client.Neo4jClient;
import io.innerloop.neo4j.client.Neo4jServerException;
import io.innerloop.neo4j.client.RowStatement;


/**
 * Created by abq304 on 08.06.2016.
 */
public class DbManager {
        Neo4jClient client;

        Connection connection;
        public DbManager() {
                client = new Neo4jClient("http://localhost:7474/db/data");
        }
        public void insertSteamUser(SteamId id){
               try {
                       connection = client.getConnection();
                       String create = "CREATE ("+id.getNickname()+":User {id: '"+id.getSteamId64() +"' , name:'" + id.getNickname() + "'})";
                       RowStatement statementInserUser = new RowStatement(create);
                       connection.add(statementInserUser);
                       connection.flush();
                       connection.commit();
               }catch(Neo4jServerException e) {

               }
        }

        public void insertSteamGame(Integer gameId, SteamGame steamGame) {

                try {
                        connection = client.getConnection();
                        String createGame = "CREATE (game:Game {id: '"+gameId+"' , spielname:'" + steamGame.getName() + "'})";
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
                        String createPlay = "MATCH (u:User {id:'"+id.getSteamId64()+"'}), (d:Game {id:'"+gameId+"'}) CREATE UNIQUE (u)-[:PLAYS]->(d)";
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
                String createFriend = "MATCH (u:User {id:'"+id1.getSteamId64()+"'}), (d:User {id:'"+id2.getSteamId64()+"'}) CREATE UNIQUE (u)-[:FRIEND]-(d)";
                        RowStatement statementInsertFriend = new RowStatement(createFriend);
                        connection.add(statementInsertFriend);
                        connection.flush();
                        connection.commit();
        }catch(Neo4jServerException e) {

        }

        }


        /*public StatementResult getPlayers(){
                return null;
        }*/

        /*public List<StatementResult> getAllFriends(){
                return null;
        }*/

        /*public void updateTimePlayed(SteamId id){
                String value = "";
                String deciderString = "fine";
                deciderString = jedis.set(""+id.getSteamId64()+"",""+id.getHoursPlayed()+",","NX");

                if(deciderString == null){
                        String currentValue = jedis.get(""+id.getSteamId64()+"");
                        currentValue = currentValue + ","+id.getHoursPlayed()+"";
                        jedis.set(""+id.getSteamId64()+"",currentValue,"XX");
                }
        }*/

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
