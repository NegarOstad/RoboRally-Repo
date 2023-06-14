package dk.dtu.compute.se.pisd.roborally.model;


    public class ConveyorBelt extends SpaceAction {
        int endX;
        int endY;
        public ConveyorBelt(int endX, int endY) {
            super();
            this.endX = endX;
            this.endY = endY;
        }

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

