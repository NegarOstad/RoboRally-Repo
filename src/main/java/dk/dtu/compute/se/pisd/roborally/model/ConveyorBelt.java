package dk.dtu.compute.se.pisd.roborally.model;


    public class ConveyorBelt extends BoardElement {
        Space endSpace;
        public ConveyorBelt(Space inputEndSpace) {
            super();
            endSpace = inputEndSpace;
        }

        public void movePlayer(Player currentPlayer) {
            currentPlayer.setSpace(endSpace);
        }

        @Override
        public void doAction(Player currentPlayer) {
            currentPlayer.setSpace(endSpace);
        }
    }

