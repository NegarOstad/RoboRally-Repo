package dk.dtu.compute.se.pisd.roborally.model;

import java.util.ArrayList;
import java.util.List;


/**
 * The PriorityAntenna class represents a space action of a priority antenna in the RoboRally game.
 * This action allows players to calculate the closest players to the antenna location.
 *
 * @author
 * Melissa Woo, s224311@dtu.dk
 * Bayan Al Dowairi, s224329@dtu.dk
 * Amira Omar, s205821@dtu.dk
 * Besma Al Jwadi, s224325@dtu.dk
 * Negar Ostad, s224283@dtu.dk
 */

public class PriorityAntenna extends SpaceAction {
    public int x;
    public int y;


    @Override
    public void doAction(Player currentPlayer, Board board) {

    }

    public PriorityAntenna(int x, int y) {
        this.x = x;
        this.y = y;
    }
    /**
     * Calculates the closest players to the priority antenna location based on their distances.
     *
     * @param players The list of players to calculate their distances.
     * @return The sorted list of players, starting from the closest to the priority antenna.
     */
    public List<Player> calcClosestPlayers(List<Player> players) {
        List<Player> sortedPlayers = new ArrayList<>();
        List<Player> tempPlayersList = new ArrayList<>();
        for(int i = 0 ; i < players.size() ; i++){
            tempPlayersList.add(players.get(i));

        }

        //double a = Math.abs(this.x - tempPlayersList.get(0).getSpace().x);
        //double b = Math.abs(this.y - tempPlayersList.get(0).getSpace().y);
        //double minDistance = Math.sqrt(Math.pow(a,2) + Math.pow(b, 2));
        Player currentClosest = tempPlayersList.get(0);

        for (int i = 0; i < players.size(); i++) {
            double minDistance = 999999;
            for (Player player : tempPlayersList) {
                //currentClosest = player;
                double a = Math.abs(this.x - player.getSpace().x);
                double b = Math.abs(this.y - player.getSpace().y);
                //added a random number to avoid players of the same distance the priority antenna
                double distance = Math.sqrt(Math.pow(a,2) + Math.pow(b, 2)) + Math.random()/100;
                if (distance <= minDistance) {
                    currentClosest = player;
                    minDistance = distance;
                }
            }
            sortedPlayers.add(currentClosest);
            tempPlayersList.remove(currentClosest);
        }
        for(Player p : sortedPlayers){
            System.out.print(p.getName() + ", ");
        }
        System.out.println();
        return sortedPlayers;
    }

    public String getType() {
        return "PriorityAntenna";
    }
}

