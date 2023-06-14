package dk.dtu.compute.se.pisd.roborally.model;


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

    /**
     * Performs the gear action for the current player on the board.
     * rotates the player's heading either to the left or to the right,
     * depending on the assigned rotation option.
     *
     * @param currentPlayer The player who is performing the gear action.
     * @param board         The game board on which the action is being performed.
     */
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
