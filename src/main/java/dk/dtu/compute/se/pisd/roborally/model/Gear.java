package dk.dtu.compute.se.pisd.roborally.model;

/*public class Gear extends SpaceAction {
    private Heading EndDirection;
    public Gear(Heading EndDirection) {
        this.EndDirection= EndDirection;
    }
    public void turnPlayer(Player currentPlayer){
        currentPlayer.setHeading(EndDirection);
    }

    @Override
    public void doAction(Player currentPlayer, Board board) {
        currentPlayer.setHeading(EndDirection);
    }


    public Heading getHeading() {
        return EndDirection;
    }*/

public class Gear extends SpaceAction {
    private boolean turnLeft;

    public Gear(boolean turnLeft) {
        this.turnLeft = turnLeft;
    }

    public void setTurnLeft(boolean turnLeft) {
        this.turnLeft = turnLeft;
    }

    public boolean isTurnLeft() {
        return turnLeft;
    }

    @Override
    public void doAction(Player currentPlayer, Board board) {
        if (turnLeft) {
            Heading currentHeading = currentPlayer.getHeading();
            currentPlayer.setHeading(currentHeading.next());
        } else {
            Heading currentHeading = currentPlayer.getHeading();
            currentPlayer.setHeading(currentHeading.prev());
        }
    }
}