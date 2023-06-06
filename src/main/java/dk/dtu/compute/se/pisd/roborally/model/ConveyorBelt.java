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
            currentPlayer.setSpace(board.getSpace(endX, endY), board);
        }
    }

