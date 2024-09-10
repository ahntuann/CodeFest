package action;

import checker.DistanceCalculator;
import checker.InSafePlaceChecker;
import jsclub.codefest2024.sdk.Hero;
import jsclub.codefest2024.sdk.algorithm.PathUtils;
import jsclub.codefest2024.sdk.base.Node;
import jsclub.codefest2024.sdk.model.GameMap;
import jsclub.codefest2024.sdk.model.players.Player;
import jsclub.codefest2024.sdk.model.weapon.Weapon;

import java.util.List;

public class GetGuner {
    public static void getGun(Hero hero, Node currentNode, List<Node> restrictedNodes, List<Node> otherPlayesNode, Player player) {

        try {
            GameMap gameMap = hero.getGameMap();
            Weapon isUseGun = hero.getInventory().getGun();
            final boolean[] pickedUpGun = {isUseGun != null};

            System.out.println("inventory: " + isUseGun);
            System.out.println("is picked up: " + pickedUpGun[0]);

            if (!pickedUpGun[0]) {
                List<Weapon> gunList = gameMap.getAllGun();
                Weapon targetGun = gunList.get(0);

                for (Weapon gun : gunList) {
                    if (InSafePlaceChecker.check(gun, gameMap) &&
                            DistanceCalculator.calc(gun, player) < DistanceCalculator.calc(targetGun, player))
                        targetGun = gun;
                }

                if (currentNode.getX() == targetGun.getX() && currentNode.getY() == targetGun.getY()) {
                    hero.pickupItem();
                    pickedUpGun[0] = true;
                } else {
                    restrictedNodes.addAll(otherPlayesNode);
                    hero.move(PathUtils.getShortestPath(gameMap, restrictedNodes, currentNode, targetGun, false));
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
