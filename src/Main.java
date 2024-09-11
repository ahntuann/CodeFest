import action.GetGuner;
import action.MoveAndAttack;
import action.SortDistancer;
import checker.HavePlayerIn11Checker;
import getInfo.GetClosestPlayer;
import getInfo.GetOtherPlayerNodes;
import getInfo.GetRestrictedNodes;
import getInfo.GetStepApproach;
import io.socket.emitter.Emitter;
import jsclub.codefest2024.sdk.Hero;
import jsclub.codefest2024.sdk.base.Node;
import jsclub.codefest2024.sdk.model.GameMap;
import jsclub.codefest2024.sdk.model.players.Player;
import myClass.PlayerAndDistance;
import myClass.PlayerAndID;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class Main {
    private static final String SERVER_URL = "https://cf-server.jsclub.dev";
    private static final String GAME_ID = "138064";
    private static final String PLAYER_NAME = "go go";
    private static final Logger log = LogManager.getLogger(Main.class);
    public static HashMap<Integer, Integer> countStep;


    public static void main(String[] args) throws IOException {
        Hero hero = new Hero(GAME_ID, PLAYER_NAME);
        countStep = GetStepApproach.setupStep();

        Emitter.Listener onMapUpdate = new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                try {
//                    Game map
                    GameMap gameMap = hero.getGameMap();
                    gameMap.updateOnUpdateMap(args[0]);
                    System.out.println();
//                    Current Player
                    Player player = gameMap.getCurrentPlayer();

//                    Other Player
                    List<Player> otherPlayers = new ArrayList<>();
                    List<PlayerAndID> otherPlayersAndID = new ArrayList<>();
                    List<Player> tempOtherPlayers = gameMap.getOtherPlayerInfo();
                    int n = tempOtherPlayers.size();
                    System.out.println(tempOtherPlayers);

                    for (int i = 0; i < n; i++) {
                        Player p = tempOtherPlayers.get(i);

                        if (p.getIsAlive()) {
                            otherPlayers.add(p);
                            otherPlayersAndID.add(new PlayerAndID(p, i));
                        }
                    }

//                    Other Player Distance To Current Node
                    List<PlayerAndDistance> playerOrdered = SortDistancer.sorter(otherPlayers, player);

//                    Other Player Nodes
                    List<Node> otherPlayerNodes = GetOtherPlayerNodes.getNodes(otherPlayers);

//                    Current Node
                    Node currentNode = new Node(player.getX(), player.getY());

//                    Restricted Nodes
                    List<Node> restrictedNodes = GetRestrictedNodes.getNodes(hero);

//                    Check if other players come to us or not
                    countStep = GetStepApproach.checkStep(hero, restrictedNodes, countStep, otherPlayersAndID, currentNode);

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



