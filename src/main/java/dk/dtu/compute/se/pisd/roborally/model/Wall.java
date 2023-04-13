package dk.dtu.compute.se.pisd.roborally.model;

public class Wall extends BoardElement{

    private Space currentSpace;

    public Wall(Space currentSpace) {
        this.currentSpace = currentSpace;
    }

    public void stopPlayerOneTurn(Player currentPlayer) {

        currentPlayer.setSpace(currentSpace);
    }
}
