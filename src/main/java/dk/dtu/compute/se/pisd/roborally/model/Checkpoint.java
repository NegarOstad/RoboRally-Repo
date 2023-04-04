package dk.dtu.compute.se.pisd.roborally.model;

public class Checkpoint extends BoardElement {
    public Checkpoint(Space spaceLocation) {
        super(spaceLocation);

    }

    public String getType() {
        return "Checkpoint";
    }


}
