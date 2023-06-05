package dk.dtu.compute.se.pisd.roborally.model;

import static dk.dtu.compute.se.pisd.roborally.model.Heading.SOUTH;

public class PlayerTemplate {
    public String name;
    public String color;
    public SpaceTemplate spaceTemplate;

    public Heading heading;

    public CommandFieldTemplate[] getCommandTemplate() {
        return commandTemplate;
    }

    public void setCommandTemplate(CommandFieldTemplate[] commandTemplate) {
        this.commandTemplate = commandTemplate;
    }

    public CommandCardField[] getCards() {
        return cards;
    }

    public void setCards(CommandCardField[] cards) {
        this.cards = cards;
    }

    public CommandFieldTemplate[] commandTemplate;
    public CommandCardField[] cards;
    public int tokenCount = 0;
    public boolean registerIsEmpty = false;
    public PlayerTemplate(String name, String color, SpaceTemplate spaceTemplate, Heading heading,
                         CommandFieldTemplate[] commandTemplate,int tokenCount, boolean registerIsEmpty){
        this.name = name;
        this.color = color;
        this.spaceTemplate = spaceTemplate;
        this.heading = heading;
        this.commandTemplate=commandTemplate;
        this.tokenCount = tokenCount;
        this.registerIsEmpty = registerIsEmpty;
    }
}
