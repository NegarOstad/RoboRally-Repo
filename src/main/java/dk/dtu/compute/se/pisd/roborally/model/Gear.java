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
    private boolean turnRight;

    public Gear(boolean turnLeft, boolean turnRight) {
        this.turnLeft = turnLeft;
        this.turnRight = turnRight;
    }

    public boolean isTurnLeft() {
        return turnLeft;
    }

    public boolean isTurnRight() {
        return turnRight;
    }



    @Override
    public void doAction(Player currentPlayer, Board board) {
        if (turnLeft) {
            Heading currentHeading = currentPlayer.getHeading();
            currentPlayer.setHeading(currentHeading.prev());
        } else {
            Heading currentHeading = currentPlayer.getHeading();
            currentPlayer.setHeading(currentHeading.next());
        }
    }
}