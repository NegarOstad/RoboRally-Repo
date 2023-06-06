package dk.dtu.compute.se.pisd.roborally.model;

import dk.dtu.compute.se.pisd.designpatterns.observer.Subject;
import java.util.ArrayList;
import java.util.List;

import static dk.dtu.compute.se.pisd.roborally.model.Phase.INITIALISATION;

public class BoardTemplate extends Subject {

    public int width;
    public int height;


    public SpaceTemplate[][] spaceTemplates;
    //public CommandFieldTemplate commandTemplate;

    public List<PlayerTemplate> playerTemplates = new ArrayList<>();
    public PlayerTemplate currentTemplate;

    public Phase phase = INITIALISATION;

    public int step;

    public boolean stepMode;

    public boolean winnerIsFound;


    public BoardTemplate(int width, int height, Space[][] spaces, List<Player> players, Player current) {
        this.width = width;
        this.height = height;
        spaceTemplates = new SpaceTemplate[width][height];
        for (int x = 0; x < width; x++) {
            for(int y = 0; y < height; y++) {
                ElementType type = spaces[x][y].getType();
                SpaceAction sa = spaces[x][y].getBoardElement();
                SpaceTemplate spaceTemplate = new SpaceTemplate(x, y, type, sa);
                spaceTemplates[x][y] = spaceTemplate;
            }
        }
        setPlayerTemplates(players, current);

    }

    private void setPlayerTemplates(List<Player> players, Player current){

        for(Player p : players){
            int spaceX = p.getSpace().x;
            int spaceY = p.getSpace().y;

               // CommandFieldTemplate[] temp = new CommandFieldTemplate[current.getCards().length];
            CommandFieldTemplate[] temp;
            if (p.getCards() != null) {

                /*int i = 0;
                for (CommandCardField c : p.getCards()) {
                    if (p.getCards()[i] != null)
                        temp[i].card = c.getCard();
                    i++;
                }*/
            }
            PlayerTemplate newPlayer = new PlayerTemplate(p.getName(), p.getColor(), spaceTemplates[spaceX][spaceY], p.getHeading(), p.getTokenCount(), p.getRegisterStatus());
            playerTemplates.add(newPlayer);
            newPlayer.setCommandCards(p.getCards());
            if(p.equals(current)){
                currentTemplate = newPlayer;
            }
        }
    }

}
