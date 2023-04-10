package dk.dtu.compute.se.pisd.roborally.model;
import java.util.List;

public abstract class BoardElement {

    private int[] priorityAntennaCoordinates;

    public int closestPlayer(List<Player> players) {
        if (players.isEmpty()) {
            throw new IllegalArgumentException("Player list is empty");
        }


        return 0;
    }
}