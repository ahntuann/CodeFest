package checker;

import jsclub.codefest2024.sdk.algorithm.PathUtils;
import jsclub.codefest2024.sdk.base.Node;
import jsclub.codefest2024.sdk.model.GameMap;
import jsclub.codefest2024.sdk.model.weapon.Weapon;

public class InSafePlaceChecker {
    public static boolean check(Weapon targetW, GameMap gameMap) {
        return (PathUtils.checkInsideSafeArea(new Node(targetW.getX(), targetW.getY()), gameMap.getDarkAreaSize() * 3 / 2, gameMap.getMapSize()));

    }
}
