package dk.dtu.compute.se.pisd.roborally.model;

import static dk.dtu.compute.se.pisd.roborally.model.Heading.SOUTH;

public class PlayerTemplate {
    public String name;
    public String color;
    public SpaceTemplate spaceTemplate;
    public Heading heading;
    public int tokenCount = 0;
    public boolean registerIsEmpty = false;
    public PlayerTemplate(String name, String color, SpaceTemplate spaceTemplate, Heading heading, int tokenCount, boolean registerIsEmpty){
        this.name = name;
        this.color = color;
        this.spaceTemplate = spaceTemplate;
        this.heading = heading;
        this.tokenCount = tokenCount;
        this.registerIsEmpty = registerIsEmpty;
    }
}
