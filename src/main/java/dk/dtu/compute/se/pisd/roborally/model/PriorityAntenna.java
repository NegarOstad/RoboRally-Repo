package dk.dtu.compute.se.pisd.roborally.model;

        import java.util.ArrayList;
        import java.util.List;

public class PriorityAntenna extends BoardElement {
    public int x;
    public int y;



    @Override
    public void doAction(Player currentPlayer) {

    }

    public PriorityAntenna(int x, int y) {
        this.x = x;
        this.y = y;
    }

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
                double distance = Math.sqrt(Math.pow(a,2) + Math.pow(b, 2));
                if (distance <= minDistance) {
                    currentClosest = player;
                    minDistance = distance;
                }
            }
            sortedPlayers.add(currentClosest);
            tempPlayersList.remove(currentClosest);
        }
        return sortedPlayers;
    }

    public String getType() {
        return "PriorityAntenna";
    }
}

