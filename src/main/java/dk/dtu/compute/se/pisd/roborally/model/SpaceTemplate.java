package dk.dtu.compute.se.pisd.roborally.model;

public class SpaceTemplate {
    public ElementType type;
    public final int x;
    public final int y;
    public SpaceAction sa;
    public Heading heading;



    public SpaceTemplate(int x, int y, ElementType type,  SpaceAction sa, Heading heading){
        this.x = x;
        this.y = y;
        this.sa = sa;
        this.type = type;
        this.heading = heading;

    }
}