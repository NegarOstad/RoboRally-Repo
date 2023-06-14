package dk.dtu.compute.se.pisd.roborally.model;

public class Wall extends SpaceAction {

    private Space currentSpace;

    private Heading heading;

    public Wall(Heading heading){
        this.heading = heading;
    }

    public Heading getHeading() {
        return heading;
    }

    @Override
    public void doAction(Player currentPlayer, Board board) {

    }
}
