package dk.dtu.compute.se.pisd.roborally.model;

public class CommandFieldTemplate {

    private CommandCard card;

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
     public CommandFieldTemplate(CommandCard card){
         this.card=card;

     }
    }
