package dk.dtu.compute.se.pisd.roborally.model;

public abstract class BoardElement {
    public abstract void doAction(Player currentPlayer, Board board);

/*
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
    ElementType Gear = ElementType.Gear;
    ElementType Conveyor_belt = ElementType.ConveyorBelt;
    ElementType Check_point = ElementType.Checkpoint;

 */

}