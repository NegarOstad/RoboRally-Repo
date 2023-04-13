package dk.dtu.compute.se.pisd.roborally.model;
import java.util.List;


public abstract class BoardElement {
    Space spacelocation;
    public BoardElement(Space space) {
        this.spacelocation = space;
    }
    public abstract String getType();

    private int[] priorityAntennaCoordinates;

    // Creates a list called closestPlayer where the players are listed in ascending order which player has shortest distance to the priority antenna
    public int closestPlayer(List<Player> players) {
        if (players.isEmpty()) {
            return -1;
        }
        int closestPlayerID = 0;
        double minDistance = Double.MAX_VALUE;

        // Will loop through each player in the list
        for (Player player : players) {
            // Get the coordinates of the player
            int[] playerCoordinates = player.getspace();

            // the distance between the player and the priority antenna is calculated using the Pythagorean theorem
            double distance = Math.sqrt(Math.pow(playerCoordinates[0] - priorityAntennaCoordinates[0], 2)
                    + Math.pow(playerCoordinates[1] - priorityAntennaCoordinates[1], 2));

            // Will update the minimum distance and closest player ID if this player is closer than the previous closest player
            if (distance < minDistance) {
                minDistance = distance;
                closestPlayerID = player.getID();
            }
        }
        //returns the closest player
        return closestPlayerID;
    }
}
