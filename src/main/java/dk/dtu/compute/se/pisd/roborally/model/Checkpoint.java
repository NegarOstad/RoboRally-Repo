package dk.dtu.compute.se.pisd.roborally.model;

public class Checkpoint extends BoardElement {
    private int index;

    private boolean isLastCheckpoint;
    public Checkpoint(int inputIndex, boolean isLastCheckpoint) {
        super();
        index = inputIndex;
        this.isLastCheckpoint = isLastCheckpoint;
    }


    public int getIndex() {
        return index;
    }

    public boolean isLastCheckpoint () {
        return isLastCheckpoint;
    }

    @Override
    public void doAction(Player currentPlayer) {


    }
}
