package dk.dtu.compute.se.pisd.roborally.model;

public class SpaceTemplate {
    public ElementType type;
    public final int x;
    public final int y;
    //public BoardElement be;


    public SpaceTemplate(int x, int y, ElementType type/*,  BoardElement be*/) {
        this.x = x;
        this.y = y;
        //this.be = be;
        this.type = type;

    }
}
