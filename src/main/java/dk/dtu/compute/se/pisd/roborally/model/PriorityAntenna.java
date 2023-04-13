package dk.dtu.compute.se.pisd.roborally.model;

public class PriorityAntenna extends BoardElement {

    public PriorityAntenna(Space spacelocation) {
        super(spacelocation);
        spacelocation.setBoardElement(this);

    }

    @Override
    public String getType() {
        return "PriorityAntenna";
    }
}