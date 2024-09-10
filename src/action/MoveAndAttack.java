package action;

import getInfo.GetClosestPlayer;
import jsclub.codefest2024.sdk.Hero;
import jsclub.codefest2024.sdk.algorithm.PathUtils;
import jsclub.codefest2024.sdk.base.Node;
import jsclub.codefest2024.sdk.model.GameMap;
import jsclub.codefest2024.sdk.model.players.Player;

import java.util.List;

public class MoveAndAttack {
    public static void moveAndAttack(Player closestPlayer, Node currentNode, Hero hero, List<Player> otherPlayers, List<Node> restrictedNodes) {
        try {
            GameMap gameMap = hero.getGameMap();

            Player player = gameMap.getCurrentPlayer();

            // Check for nearby enemies to shoot
            if ((currentNode.getX() - closestPlayer.getX() == 0 ||
                    currentNode.getY() - closestPlayer.getY() == 0) &&
                    Math.abs(currentNode.getX() - closestPlayer.getX()) + Math.abs(currentNode.getY() - closestPlayer.getY()) <= hero.getInventory().getGun().getRange()) {
                if (closestPlayer.getY() > currentNode.getY()) {
                    hero.shoot("u");
                } else if (closestPlayer.getY() < currentNode.getY()) {
                    hero.shoot("d");
                } else if (closestPlayer.getX() < currentNode.getX()) {
                    hero.shoot("l");
                } else if (closestPlayer.getX() > currentNode.getX()) {
                    hero.shoot("r");
                }
            } else {
                closestPlayer = GetClosestPlayer.get(otherPlayers, player);
                hero.move(PathUtils.getShortestPath(gameMap, restrictedNodes, currentNode, closestPlayer, false));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
