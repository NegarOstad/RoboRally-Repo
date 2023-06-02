package dk.dtu.compute.se.pisd.roborally.model;

public class Gear extends SpaceAction {
    private Heading EndDirection;
    public Gear(Heading EndDirection) {
        this.EndDirection= EndDirection;
    }
    /*public void turnPlayer(Player currentPlayer){
        currentPlayer.setHeading(EndDirection);
    }*/

    @Override
    public void doAction(Player currentPlayer, Board board) {
        currentPlayer.setHeading(EndDirection);
    }

    public Heading getHeading() {
        return EndDirection;
    }
}
