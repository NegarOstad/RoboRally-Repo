package dk.dtu.compute.se.pisd.roborally.model;

public class Gear extends BoardElement {
    private Heading EndDirection;
    public Gear(Heading EndDirection) {
        this.EndDirection= EndDirection;
    }
    public void turnPlayer(Player currentPlayer){
        currentPlayer.setHeading(EndDirection);
    }

}
