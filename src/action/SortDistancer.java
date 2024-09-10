package action;

import checker.DistanceCalculator;
import jsclub.codefest2024.sdk.model.Element;
import jsclub.codefest2024.sdk.model.players.Player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class SortDistancer {
    public static List<Player> sorter(List<Player> elements, Player player) {
        List<Integer> dists = new ArrayList<>();
        int n = elements.size();
        for (int i = 0; i < n; i++) {
            Player e = elements.get(i);

            dists.set(i, DistanceCalculator.calc(e, player));
        }

        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n - i + 1; j++) {
                if (dists.get(i) > dists.get(j)) {
                    int tmp = dists.get(i);
                    Player tmpE = elements.get(i);

                    dists.set(i, dists.get(j));
                    dists.set(j, tmp);

                    elements.set(i, elements.get(j));
                    elements.set(j, tmpE);
                }
            }
        }

        return elements;
    }

}
