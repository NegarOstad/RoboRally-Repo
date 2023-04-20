package dk.dtu.compute.se.pisd.roborally.model;

import java.util.List;

public class PriorityAntenna extends BoardElement {
    private int x;
    private int y;

    List<Player> sortedPlayers;
    @Override
    public void doAction(Player currentPlayer) {

    }

    public PriorityAntenna(int x , int y) {
        this.x = x;
        this.y = y;

    }

    /*public Player getClosestPlayer(int i) {
        return sortedPlayers[i];
    }*/
    private List<Player> calcClosestPlayers(List<Player> players) {
        /*if (players.isEmpty()) {
            System.out.println("Player list is empty, this will return NULL.");
        }
         */
        //Player closestPlayer = players.get(0);

        int minDistance = (int)Math.sqrt(Math.pow(players.get(0).getSpace().x, 2)
                + Math.pow(players.get(0).getSpace().y, 2));
        Player currentClosest = players.get(0);
        for(int i = 0 ; i < players.size() ; i++) {
            for (Player player : players) {
                currentClosest = player;
                // the distance between the player and the priority antenna is calculated using the Pythagorean theorem
                int distance = (int) Math.sqrt(Math.pow(player.getSpace().x - x, 2)
                        + Math.pow(player.getSpace().y - y, 2));

                // Will update the minimum distance and closest player ID if this player is closer than the previous closest player
                if (distance <= minDistance) {
                    currentClosest = player;

                }
            }
            sortedPlayers.add(currentClosest);
            players.remove(currentClosest);

        }

        //returns the closest player
            return sortedPlayers;
    }
    public String getType() {
        return "PriorityAntenna";
    }

}