/*
 *  This file is part of the initial project provided for the
 *  course "Project in Software Development (02362)" held at
 *  DTU Compute at the Technical University of Denmark.
 *
 *  Copyright (C) 2019, 2020: Ekkart Kindler, ekki@dtu.dk
 *
 *  This software is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; version 2 of the License.
 *
 *  This project is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this project; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 */
package dk.dtu.compute.se.pisd.roborally.model;

import dk.dtu.compute.se.pisd.designpatterns.observer.Subject;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static dk.dtu.compute.se.pisd.roborally.model.Phase.INITIALISATION;

/**
 * ...
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 *
 */
public class  Board extends Subject {

    public final int width;

    public final int height;

    // public final String boardName;

    private Integer gameId;

    private Space[][] spaces;

    private final List<Player> players = new ArrayList<>();

    private Player current;

    private Phase phase;

    private int step;

    private boolean stepMode;

    private boolean winnerIsFound = false;

    private boolean OutOfBounds;

    PriorityAntenna priorityAntenna;

    public Board(int width, int height/*, @NotNull String boardName*/) {
        //this.boardName = boardName;
        this.width = width;
        this.height = height;
        spaces = new Space[width][height];

        for (int x = 0; x < width; x++) {
            for(int y = 0; y < height; y++) {
                Space space = new Space(x, y);
                spaces[x][y] = space;

            }
        }
        this.stepMode = false;
        this.OutOfBounds = false;
        phase = INITIALISATION;
        step = 0;
    }

    public Board(int width, int height, SpaceTemplate[][] spaceTemplates) {
        // this.boardName = boardName;
        this.width = width;
        this.height = height;
        spaces = new Space[width][height];
        setSpaces(spaceTemplates);

    }

    private void setSpaces(SpaceTemplate[][] spaceTemplates) {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int spaceX = spaceTemplates[x][y].x;
                int spaceY = spaceTemplates[x][y].y;
                Space space = new Space(spaceX, spaceY);
                spaces[x][y] = space;
                setSpaceType(spaceTemplates[x][y].type, spaces[x][y], spaceTemplates[x][y]);

            }
        }
    }

    private void setSpaceType(ElementType type, Space space, SpaceTemplate spaceTemplate){
        switch(type) {
            case Gear:
                Gear gear = (Gear)spaceTemplate.sa;
                space.setTypeGear(gear.isTurnLeft(), gear.isTurnRight());
                break;
            case Checkpoint:
                Checkpoint checkpoint = (Checkpoint)spaceTemplate.sa;
                space.setTypeCheckpoint(checkpoint.getIndex(), checkpoint.isLastCheckpoint());
                break;

            case Wall:
                space.setTypeWall();
                break;

            case ConveyorBelt:
                ConveyorBelt conveyorBelt = (ConveyorBelt)spaceTemplate.sa;
                space.setTypeConveyor(conveyorBelt.endX, conveyorBelt.endY);
                System.out.println(spaceTemplate.heading);
                space.setHeading(spaceTemplate.heading);
                break;

            case PriorityAntenna:
                priorityAntenna = (PriorityAntenna) spaceTemplate.sa;
                space.setTypePriorityAntenna();
                break;
        }
    }

    public Integer getGameId() {
        return gameId;
    }

    public void setOutOfBounds(boolean outOfBounds) {
        OutOfBounds = outOfBounds;
    }

    public void setGameId(int gameId) {
        if (this.gameId == null) {
            this.gameId = gameId;
        } else {
            if (!this.gameId.equals(gameId)) {
                throw new IllegalStateException("A game with a set id may not be assigned a new id!");
            }
        }
    }


    public Space getSpace(int x, int y) {
        if (x >= 0 && x < width &&
                y >= 0 && y < height) {
            return spaces[x][y];
        } else {
            return null;
        }
    }

    public Space[][] getSpaces() {
        return spaces;
    }

   /* public SpaceTemplate[][] getSpaceTemplates() {
        return spaceTemplates;
    }*/

    /*public void setSpaceType(int x, int y, ElementType inputType){
        spaces[x][y].setType(inputType);
    }*/

    public int getPlayerCount() {
        return players.size();
    }

    public void addPlayer(@NotNull Player player) {
        if (!players.contains(player)) {
            players.add(player);
            notifyChange();
        }
    }

    public Player getPlayer(int i) {
        if (i >= 0 && i < players.size()) {
            return players.get(i);
        } else {
            return null;
        }
    }

    public List<Player> getPlayerList(){
        return players;
    }

    public Player getCurrentPlayer() {
        return current;
    }

    public void setCurrentPlayer(Player player) {
        if (player != this.current && players.contains(player)) {
            this.current = player;
            notifyChange();
        }
    }

    public int getNextPlayersNumber(List<Player> listOfClosestPlayers){
        // public int getNextPlayersNumber(Player closestPlayer){
        int index = players.indexOf(listOfClosestPlayers);
        return (index + 1) % players.size();
    }
    public Phase getPhase() {
        return phase;
    }

    public void setPhase(Phase phase) {
        if (phase != this.phase) {
            this.phase = phase;
            notifyChange();
        }
    }

    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        if (step != this.step) {
            this.step = step;
            notifyChange();
        }
    }

    public boolean isStepMode() {
        return stepMode;
    }

    public void setStepMode(boolean stepMode) {
        if (stepMode != this.stepMode) {
            this.stepMode = stepMode;
            notifyChange();
        }
    }

    public int getPlayerNumber(@NotNull Player player) {
        return players.indexOf(player);

    }

    /**
     * Returns the neighbour of the given space of the board in the given heading.
     * The neighbour is returned only, if it can be reached from the given space
     * (no walls or obstacles in either of the involved spaces); otherwise,
     * null will be returned.
     *
     * @param space the space for which the neighbour should be computed
     * @param heading the heading of the neighbour
     * @return the space in the given direction; null if there is no (reachable) neighbour
     */
    public Space getNeighbour(@NotNull Space space, @NotNull Heading heading) {
        int x = space.x;
        int y = space.y;
        switch (heading) {
            case SOUTH:
                y = (y + 1) % height;
                break;
            case WEST:
                x = (x + width - 1) % width;
                break;
            case NORTH:
                y = (y + height - 1) % height;
                break;
            case EAST:
                x = (x + 1) % width;
                break;
        }

        return getSpace(x, y);
    }

    public String getStatusMessage() {
        if(winnerIsFound) {
            return current.getName() + " is the winner!";
        } else if(OutOfBounds) {
            return "out of bounds";
        }else {
            return "Phase: " + getPhase().name() +
                    ", Player = " + getCurrentPlayer().getName() +
                    ", Step: " + getStep() +
                    ", Token count: " + getCurrentPlayer().getTokenCount();
        }
    }

    public void setWinnerStatus(boolean winnerIsFound) {
        this.winnerIsFound = winnerIsFound;
    }
    public void setTypePriorityAntenna(int x, int y) {
        priorityAntenna = (PriorityAntenna)this.getSpace(x, y).setTypePriorityAntenna();

    }

    public PriorityAntenna getPriorityAntenna() {
        return priorityAntenna;
    }







    public boolean getWinnerStatus(){
        return winnerIsFound;
    }

    public List<Player> getPlayers() {
        return players;
    }



   /* public void setSpaceType(int x, int y, ElementType inputType) {
        switch (inputType){
            case Gear:
                setSpaceType(x, y, ElementType.Gear);
                break;

            case Checkpoint:
                setSpaceType(x, y, ElementType.Checkpoint);
                break;

            case ConveyorBelt:
                setSpaceType(x, y, ElementType.ConveyorBelt);
                break;

            default: setSpaceType(x, y, ElementType.Normal);


        }
    }*/
}