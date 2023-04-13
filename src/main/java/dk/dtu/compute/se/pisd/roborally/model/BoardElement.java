package dk.dtu.compute.se.pisd.roborally.model;

public abstract class BoardElement {
    BoardElement(Board board, Space space) {
    }

    Space spaceLocation;

    public BoardElement(Space spaceLocation) {
        this.spaceLocation = spaceLocation;


        public abstract String getType ();
    }
}