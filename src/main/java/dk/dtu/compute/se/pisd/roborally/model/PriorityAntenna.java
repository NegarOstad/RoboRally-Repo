package dk.dtu.compute.se.pisd.roborally.model;

public class PriorityAntenna extends BoardElement {
    public PriorityAntenna(Space spaceLocation) {
        super(spaceLocation);
        spaceLocation.setBoardElement(this);

    }
}