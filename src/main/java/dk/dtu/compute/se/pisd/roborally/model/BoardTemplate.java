package dk.dtu.compute.se.pisd.roborally.model;

import dk.dtu.compute.se.pisd.designpatterns.observer.Subject;
import java.util.ArrayList;
import java.util.List;

import static dk.dtu.compute.se.pisd.roborally.model.Phase.INITIALISATION;

public class BoardTemplate extends Subject {

    public int width;
    public int height;


    public SpaceTemplate[][]  spaceTemplates;
    //public CommandFieldTemplate commandTemplate;

    public List<PlayerTemplate> playerTemplates = new ArrayList<>();
    public PlayerTemplate currentTemplate;

    public Phase phase;

    public int step;

    public boolean stepMode;

    public boolean winnerIsFound;


    public BoardTemplate(Board board, Space[][] spaces, List<Player> players, Player current) {
        this.width = board.width;
        this.height = board.height;
        spaceTemplates = new SpaceTemplate[width][height];
        for (int x = 0; x < width; x++) {
            for(int y = 0; y < height; y++) {
                ElementType type = spaces[x][y].getType();
                SpaceAction sa = spaces[x][y].getBoardElement();
                Heading heading = spaces[x][y].getHeading();
                SpaceTemplate spaceTemplate = new SpaceTemplate(x, y, type, sa, heading);
                spaceTemplates[x][y] = spaceTemplate;
            }
        }
        setPlayerTemplates(players, current);
        phase = board.getPhase();
        step = board.getStep();
        stepMode = board.isStepMode();
        winnerIsFound = board.getWinnerStatus();

    }

    private void setPlayerTemplates(List<Player> players, Player current){

        for(Player p : players){
            int spaceX = p.getSpace().x;
            int spaceY = p.getSpace().y;

            // CommandFieldTemplate[] temp = new CommandFieldTemplate[current.getCards().length];

            PlayerTemplate newPlayer = new PlayerTemplate(p.getName(), p.getColor(), spaceTemplates[spaceX][spaceY], p.getHeading(), p.getTokenCount(), p.getRegisterStatus());
            playerTemplates.add(newPlayer);
            newPlayer.setCommandCards(p.getCards());
            if(p.equals(current)){
                currentTemplate = newPlayer;
            }
        }
    }

}