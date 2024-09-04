import io.socket.emitter.Emitter;
import jsclub.codefest2024.sdk.Hero;
import jsclub.codefest2024.sdk.algorithm.PathUtils;
import jsclub.codefest2024.sdk.base.Node;
import jsclub.codefest2024.sdk.model.Element;
import jsclub.codefest2024.sdk.model.GameMap;
import jsclub.codefest2024.sdk.model.Inventory;
import jsclub.codefest2024.sdk.model.enemies.Enemy;
import jsclub.codefest2024.sdk.model.obstacles.Obstacle;
import jsclub.codefest2024.sdk.model.players.Player;
import jsclub.codefest2024.sdk.model.weapon.Weapon;

import java.io.IOException;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;

public class Main {
    private static final String SERVER_URL = "https://cf-server.jsclub.dev";
    private static final String GAME_ID = "139324";
    private static final String PLAYER_NAME = "top1";

    public static void main(String[] args) throws IOException {
        Hero hero = new Hero(GAME_ID, PLAYER_NAME);

        Emitter.Listener onMapUpdate = new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                    try {
                        GameMap gameMap = hero.getGameMap();
                        gameMap.updateOnUpdateMap(args[0]);
                        Player player = gameMap.getCurrentPlayer();
                        List<Player> otherPlayers = gameMap.getOtherPlayerInfo();
                        List<Element> restricedList = new ArrayList<>();
                        restricedList.addAll(gameMap.getListChests());
                        restricedList.addAll(gameMap.getListTraps());
                        restricedList.addAll(gameMap.getListEnemies());
                        Node currentNode = new Node(player.getX(), player.getY());
                        List<Node> restrictedNodes = new ArrayList<>();

                        List<Node> otherPlayesNode = new ArrayList<>();

                        for (Player p : otherPlayers) {
                            if(p.getIsAlive()){
                                otherPlayesNode.add(new Node(p.getX(), p.getY()));
                            }
                        }
                        for (Element o : restricedList) {
                            restrictedNodes.add(new Node(o.getX(), o.getY()));
                        }

                        Weapon isUseGun = hero.getInventory().getGun();
                        final boolean[] pickedUpGun = {isUseGun != null};

                        System.out.println("inventory: " + isUseGun);
                        System.out.println("is picked up: " + pickedUpGun[0]);

                        if (!pickedUpGun[0]) {
                            List<Weapon> gunList = gameMap.getAllGun();
                            Weapon someGun = gunList.get(0);

                            if (currentNode.getX() == someGun.getX() && currentNode.getY() == someGun.getY()) {
                                hero.pickupItem();
                                pickedUpGun[0] = true;
                            } else {
                                restrictedNodes.addAll(otherPlayesNode);
                                hero.move(PathUtils.getShortestPath(gameMap, restrictedNodes, currentNode, someGun, false));
                            }
                        }

                        List<Player> enermies = hero.getGameMap().getOtherPlayerInfo();

                        Player e1 = enermies.get(0);

                        hero.move(PathUtils.getShortestPath(gameMap, restrictedNodes, currentNode, e1, true));
                        System.out.println(e1);

                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

//                try {
//                    Inventory inventory = hero.getInventory();
//                    GameMap gameMap = hero.getGameMap();
//                    Player player = gameMap.getCurrentPlayer();
//                    System.out.println(player == null);
//                    Node currentNode = new Node();
//
//                    List<Obstacle> obstacles = gameMap.getListIndestructibleObstacles();
//                    obstacles.addAll(gameMap.getListChests());
//                    obstacles.addAll(gameMap.getListTraps());
//                    List<Enemy> otherEnemies = gameMap.getListEnemies();
//
//                    List<Node> restrictedNodes = new ArrayList<>();
//                    for (Obstacle o : obstacles) {
//                        restrictedNodes.add(new Node(o.getX(), o.getX()));
//                    }
//
//                    for (Enemy e : otherEnemies) {
//                        restrictedNodes.add(new Node(e.getX(), e.getY()));
//                    }
//
//                    List<Player> otherPlayers = gameMap.getOtherPlayerInfo();
//                    List<Node> otherPlayerNodes = new ArrayList<>();
//
//                    for (Player p : otherPlayers) {
//                        otherPlayerNodes.add(new Node(p.getX(), p.getY()));
//                    }
//
//                    Weapon ownGun = inventory.getGun();
//                    boolean []isPickGun = {ownGun != null};
//
//                    if (!isPickGun[0]) {
//                        Weapon someGun = gameMap.getAllGun().get(0);
//
//                        if (currentNode.getX() == someGun.getX() && currentNode.getY() == someGun.getY()) {
//                            hero.pickupItem();
//                            isPickGun[0] = true;
//                        } else {
//                            restrictedNodes.addAll(otherPlayerNodes);
//                            hero.move(PathUtils.getShortestPath(gameMap, restrictedNodes, currentNode, someGun, false));
//                        }
//                    }
//                } catch (IOException e) {
//                    throw new RuntimeException(e);
//                }
            }
        };

        hero.setOnMapUpdate(onMapUpdate);
        hero.start(SERVER_URL);
    }
}