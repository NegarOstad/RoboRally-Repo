package dk.dtu.compute.se.pisd.roborally.model;

public abstract class BoardElement {
    Space space;
    Board board;

    BoardElement(Board board, Space space) {
        this.board=board;
        this.space=space;
    }

    Space spaceLocation;

    public BoardElement(Space spaceLocation) {
        this.spaceLocation = spaceLocation;
    }
    public abstract String getType();
}