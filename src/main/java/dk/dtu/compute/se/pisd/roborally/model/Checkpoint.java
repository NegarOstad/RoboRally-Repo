package dk.dtu.compute.se.pisd.roborally.model;

public class Checkpoint extends BoardElement {
    private int index;
    public Checkpoint(int inputIndex) {
        super();
        index = inputIndex;
    }

    public int getIndex() {
        return index;
    }
}
