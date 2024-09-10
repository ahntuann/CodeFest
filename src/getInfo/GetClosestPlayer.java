package getInfo;

import checker.DistanceCalculator;
import jsclub.codefest2024.sdk.base.Node;
import jsclub.codefest2024.sdk.model.Element;
import jsclub.codefest2024.sdk.model.players.Player;

import java.util.List;

public class GetClosestPlayer {
    public static Player get(List<Player> elements, Element curPos) {
        Player result = elements.get(0);
        int minDist = DistanceCalculator.calc(result, curPos);


        for (Player e : elements) {
            int tempDist = DistanceCalculator.calc(e, curPos);
            if (tempDist < minDist) {
                minDist = tempDist;
                result = e;
            }
        }


        return result;
    }
}
