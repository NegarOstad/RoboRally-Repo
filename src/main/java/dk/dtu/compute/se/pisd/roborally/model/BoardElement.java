package dk.dtu.compute.se.pisd.roborally.model;

public abstract class BoardElement {
    Space spaceLocation;
    BoardElement(Space spaceLocation) {
        this.spaceLocation = spaceLocation;
    }

    public abstract String getType();
}
