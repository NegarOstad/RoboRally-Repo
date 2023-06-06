package dk.dtu.compute.se.pisd.roborally.model;

public class SpaceTemplate {
    public ElementType type;
    public final int x;
    public final int y;
    public SpaceAction sa;


    public SpaceTemplate(int x, int y, ElementType type,  SpaceAction sa) {
        this.x = x;
        this.y = y;
        this.sa = sa;
        this.type = type;

    }
}
