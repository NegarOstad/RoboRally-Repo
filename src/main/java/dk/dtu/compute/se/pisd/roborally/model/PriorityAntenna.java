package dk.dtu.compute.se.pisd.roborally.model;

import java.util.List;

public class PriorityAntenna extends BoardElement {
    @Override
    public void doAction(Player currentPlayer) {

    }

    public PriorityAntenna() {

    }
    public Player closestPlayer(List<Player> players) {
        /*if (players.isEmpty()) {
            System.out.println("Player list is empty, this will return NULL.");
        }
         */
        Player closestPlayer = players.get(0);
        double minDistance = Double.MAX_VALUE;

        // Will loop through each player in the list
        for (Player player : players) {

            // the distance between the player and the priority antenna is calculated using the Pythagorean theorem
            double distance = Math.sqrt(Math.pow(player.getSpace().x - spacelocation.x, 2)
                    + Math.pow(player.getSpace().y - spacelocation.y, 2));

            // Will update the minimum distance and closest player ID if this player is closer than the previous closest player
            if (distance < minDistance) {
                minDistance = distance;
                closestPlayer = player;
            }
        }
        //returns the closest player
            return closestPlayer;
    }
    public String getType() {
        return "PriorityAntenna";
    }

}