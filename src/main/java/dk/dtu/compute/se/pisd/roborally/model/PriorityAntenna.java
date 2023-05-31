package dk.dtu.compute.se.pisd.roborally.model;

        import java.util.ArrayList;
        import java.util.List;

public class PriorityAntenna extends BoardElement {
    public int x;
    public int y;

    List<Player> sortedPlayers = new ArrayList<>();

    @Override
    public void doAction(Player currentPlayer) {

    }

    public PriorityAntenna(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public List<Player> calcClosestPlayers(List<Player> players) {
        List<Player> tempPlayersList = new ArrayList<>();
        for(int i = 0 ; i < players.size() ; i++){
            tempPlayersList.add(players.get(i));

        }

        double a = Math.abs(this.x - tempPlayersList.get(0).getSpace().x);
        double b = Math.abs(this.y - tempPlayersList.get(0).getSpace().y);
        double minDistance = Math.sqrt(Math.pow(a,2) + Math.pow(b, 2));
        Player currentClosest = tempPlayersList.get(0);

        for (int i = 0; i < players.size(); i++) {
            for (Player player : tempPlayersList) {
                currentClosest = player;
                a = Math.abs(this.x - player.getSpace().x);
                b = Math.abs(this.y - player.getSpace().y);
                double distance = Math.sqrt(Math.pow(a,2) + Math.pow(b, 2));
                if (distance <= minDistance) {
                    currentClosest = player;
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


/*

    public List<Player> calcClosestPlayers(List<Player> players) {
        List<Player> tempPlayersList = new ArrayList<>(players);
        sortedPlayers.clear();

        while (!tempPlayersList.isEmpty()) {
            double minDistance = Double.POSITIVE_INFINITY;
            Player currentClosest = null;

            for (Player player : tempPlayersList) {
                double a = Math.abs(this.x - player.getSpace().x);
                double b = Math.abs(this.y - player.getSpace().y);
                double distance = Math.sqrt(Math.pow(a, 2) + Math.pow(b, 2));

                if (distance < minDistance) {
                    minDistance = distance;
                    currentClosest = player;
                }
            }

            sortedPlayers.add(currentClosest);
            tempPlayersList.remove(currentClosest);
        }

        return sortedPlayers;
    }


}
*/


