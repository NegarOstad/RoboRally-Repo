package dk.dtu.compute.se.pisd.roborally.model;

public class PriorityAntenna extends BoardElement {
    Space space;

    public PriorityAntenna(Space space) {
        super(space);
        space.setBoardElement(this);

    }

    @Override
    public String getType() {
        return "PriorityAntenna";
    }
}