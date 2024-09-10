import action.GetGuner;
import action.MoveAndAttack;
import action.SortDistancer;
import getInfo.GetClosestPlayer;
import getInfo.GetOtherPlayerNodes;
import getInfo.GetRestrictedNodes;
import io.socket.emitter.Emitter;
import jsclub.codefest2024.sdk.Hero;
import jsclub.codefest2024.sdk.base.Node;
import jsclub.codefest2024.sdk.model.GameMap;
import jsclub.codefest2024.sdk.model.players.Player;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class Main {
    private static final String SERVER_URL = "https://cf-server.jsclub.dev";
    private static final String GAME_ID = "173501";
    private static final String PLAYER_NAME = "go go";
    private static final Logger log = LogManager.getLogger(Main.class);
    private static int step = 0;



    public static void main(String[] args) throws IOException {
        Hero hero = new Hero(GAME_ID, PLAYER_NAME);

        Emitter.Listener onMapUpdate = new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                try {
                    System.out.println(++step);
//                    Game map
                    GameMap gameMap = hero.getGameMap();
                    gameMap.updateOnUpdateMap(args[0]);

//                    Current Player
                    Player player = gameMap.getCurrentPlayer();

//                    Other Player
                    List<Player> otherPlayers = new ArrayList<>();
                    List<Player> tempOtherPlayers = gameMap.getOtherPlayerInfo();

                    for (Player p : tempOtherPlayers) {
                        if (p.getIsAlive())
                            otherPlayers.add(p);
                    }

                    List<Player> playerOrdered = SortDistancer.sorter(otherPlayers, player);

                    System.out.println(playerOrdered);

//                    Other Player Nodes
                    List<Node> otherPlayerNodes = GetOtherPlayerNodes.getNodes(otherPlayers);

//                    Current Node
                    Node currentNode = new Node(player.getX(), player.getY());

//                    Restricted Nodes
                    List<Node> restrictedNodes = GetRestrictedNodes.getNodes(hero);

//                    Move to safe palace


//                    Get Gun
                    GetGuner.getGun(hero, currentNode, restrictedNodes, otherPlayerNodes, player);

//                    Get player closest
                    Player closestPlayer = GetClosestPlayer.get(otherPlayers, player);

//                    Move And Attack
                    MoveAndAttack.moveAndAttack(closestPlayer, currentNode, hero, otherPlayers, restrictedNodes);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        };


        hero.setOnMapUpdate(onMapUpdate);
        hero.start(SERVER_URL);
    }
}



