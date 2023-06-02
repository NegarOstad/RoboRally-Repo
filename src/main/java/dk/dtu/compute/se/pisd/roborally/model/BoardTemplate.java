package dk.dtu.compute.se.pisd.roborally.model;

import dk.dtu.compute.se.pisd.designpatterns.observer.Subject;
import java.util.ArrayList;
import java.util.List;

import static dk.dtu.compute.se.pisd.roborally.model.Phase.INITIALISATION;

public class BoardTemplate extends Subject {

    public int width;
    public int height;


    public SpaceTemplate[][] spaceTemplates;

    public List<PlayerTemplate> playerTemplates = new ArrayList<>();

    public PlayerTemplate currentTemplate;


    public BoardTemplate(int width, int height, Space[][] spaces) {
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

    }

    public void setPlayerTemplates(List<Player> players){

        for(Player p : players){
            int spaceX = p.getSpace().x;
            int spaceY = p.getSpace().y;
            PlayerTemplate newPlayer = new PlayerTemplate(p.getName(), p.getColor(), spaceTemplates[spaceX][spaceY], p.getHeading(), p.getTokenCount(), p.getRegisterStatus());
            playerTemplates.add(newPlayer);
        }
    }

    public void setCurrentPlayerTemplate(Player current) {
        for (int i = 0; i < playerTemplates.size(); i++) {
            if(playerTemplates.get(i).name.equals(current.getName())){
                currentTemplate = playerTemplates.get(i);
            }
        }
    }

}
