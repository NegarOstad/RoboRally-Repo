package dk.dtu.compute.se.pisd.roborally.model;

/**
 * Represents a conveyor belt space action in the game.
 * Conveyor belts move players to neighboring spaces in a specified direction.
 * The end coordinates of the conveyor belt determine where players are moved to.
 */


    public class ConveyorBelt extends SpaceAction {
        int endX;
        int endY;
        public ConveyorBelt(int endX, int endY) {
            super();
            this.endX = endX;
            this.endY = endY;
        }

    /**
     * Executes the conveyor belt action for the current player.
     * The action moves the player to the neighboring space in the direction of the current space's heading.
     * If the neighboring space has a heading, the player's heading is updated accordingly.
     *
     * @param currentPlayer The player who is performing the conveyor belt action.
     * @param board         The game board on which the action is being performed.
     */
        @Override
        public void doAction(Player currentPlayer, Board board) {
            Space space = currentPlayer.getSpace();
            Heading heading = space.getHeading();
            Space next = board.getNeighbour(space, heading);

            currentPlayer.setSpace(next, board);
            Heading nextHeading = next.getHeading();
            if (nextHeading != null)
                currentPlayer.setHeading(nextHeading);
        }

        public int getEndX() {
            return endX;
        }

        public int getEndY() {
            return endY;
        }
    }

