package getInfo;

import jsclub.codefest2024.sdk.Hero;
import jsclub.codefest2024.sdk.base.Node;
import jsclub.codefest2024.sdk.model.GameMap;
import jsclub.codefest2024.sdk.model.enemies.Enemy;
import jsclub.codefest2024.sdk.model.obstacles.Obstacle;

import java.util.ArrayList;
import java.util.List;

public class GetRestrictedNodes {
    public static List<Node> getNodes(Hero hero) {
        GameMap gameMap = hero.getGameMap();

        List<Obstacle> restrictedList = new ArrayList<>();
        restrictedList.addAll(gameMap.getListIndestructibleObstacles());
        restrictedList.addAll(gameMap.getListChests());
        restrictedList.addAll(gameMap.getListTraps());

        List<Node> restrictedNodes = new ArrayList<>();

        for (Obstacle o : restrictedList) {
            restrictedNodes.add(new Node(o.getX(), o.getY()));
        }

        for (Enemy e : gameMap.getListEnemies()) {
            restrictedNodes.add(new Node(e.getX(), e.getY()));
        }

        return restrictedNodes;
    }
}
