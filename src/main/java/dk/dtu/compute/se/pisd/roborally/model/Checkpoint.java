package dk.dtu.compute.se.pisd.roborally.model;

public class Checkpoint extends BoardElement {

    int index;
    public Checkpoint(Space spaceLocation) {
        super(spaceLocation);
        spaceLocation.setBoardElement(this);

    }

    public String getType() {
        return "Checkpoint";
    }

    public void setIndex(int i) {
        index = i;
    }

}
