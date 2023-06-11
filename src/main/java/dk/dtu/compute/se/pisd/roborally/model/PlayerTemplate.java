package dk.dtu.compute.se.pisd.roborally.model;

import static dk.dtu.compute.se.pisd.roborally.model.Heading.SOUTH;

public class PlayerTemplate {
    public String name;
    public String color;
    public SpaceTemplate spaceTemplate;
    public Heading heading;
    public boolean isLocal;

    public CommandCard[] getCommandCards() {
        return commandCards;
    }

    public void setCommandCards(CommandCardField[] commandCardFields) {
        commandCards = new CommandCard[commandCardFields.length];
        for (int i = 0; i < commandCardFields.length; i++) {
            commandCards[i] = commandCardFields[i].getCard();

        }

    }

    public CommandCard[] commandCards;
    public int tokenCount = 0;
    public boolean registerIsEmpty = false;
    public PlayerTemplate(String name, String color, SpaceTemplate spaceTemplate, Heading heading
            , int tokenCount, boolean registerIsEmpty){
        this.name = name;
        this.color = color;
        this.spaceTemplate = spaceTemplate;
        this.heading = heading;
       // this.commandTemplate=commandTemplate;
        this.tokenCount = tokenCount;
        this.registerIsEmpty = registerIsEmpty;
    }

}
