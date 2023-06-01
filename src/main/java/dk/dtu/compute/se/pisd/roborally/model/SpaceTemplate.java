package dk.dtu.compute.se.pisd.roborally.model;

public class SpaceTemplate {
    ElementType type;
    public final int x;
    public final int y;
    public BoardElement boardElement;


    public SpaceTemplate(int x, int y) {
        this.x = x;
        this.y = y;
        boardElement = null;
        type = ElementType.Normal;
    }
}
