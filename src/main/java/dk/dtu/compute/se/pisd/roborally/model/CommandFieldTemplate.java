package dk.dtu.compute.se.pisd.roborally.model;

public class CommandFieldTemplate {

    public CommandCard card;

    /* public CommandCardField[] getCards() {
        return cards;
    }

    public void setCards(CommandCardField[] cards) {
        this.cards = cards;
    }*/


    public CommandCardField[] cards;
     public CommandFieldTemplate(CommandCard card){
         this.card=card;

     }
    }
