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
public class Board extends Subject {

    public final int width;

    public final int height;

    public final String boardName;

    private Integer gameId;

    private final Space[][] spaces;

    private final List<Player> players = new ArrayList<>();

    private Player current;

    private Phase phase = INITIALISATION;

    private int step = 0;

    private boolean stepMode;

    public int getCounter() {
        return counter;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }

    private int counter = 0;

    /**   what is space and spaces???????????
     * The Board constructor with three parameters.
     * It initializes 'Board' with creating a 2D array 'space',
     * with dementions 'width' and 'height'.
     * 'boardName' could not be null.
     * 'stepMode' field is set to 'false'.
     * @param width
     * @param height
     * @param boardName
     */
    public Board(int width, int height, @NotNull String boardName) {
        this.boardName = boardName;
        this.width = width;
        this.height = height;
        spaces = new Space[width][height];
        for (int x = 0; x < width; x++) {
            for(int y = 0; y < height; y++) {
                Space space = new Space(this, x, y);
                spaces[x][y] = space;
            }
        }
        this.stepMode = false;
    }

    /**
     * The Board constructor with two parameters.
     * The constructor which intializes 'Board' by a default name.
     * It calls the first costructor with a defalt 'boardName',
     * value of 'defaultbard'
     * @param width
     * @param height
     */
    public Board(int width, int height) {
        this(width, height, "defaultboard");
    }

    /**
     * The methode has a return type of 'Integer'
     * This methode returns gameId.
     * The methode allowes to return null, if the gameId field is not set.
     * If methode wase a type of 'int', it could not return null.
     * @return
     */
    public Integer getGameId() {
        return gameId;
    }

    /**
     * This method sets gameId, if it is already null.
     * If the 'gameId' field is already set,
     * it will throw an IllegalStateException with this message:
     * "A game with a set id may not be assigned a new id!"
     * @param gameId
     */
    public void setGameId(int gameId) {
        if (this.gameId == null) {
            this.gameId = gameId;
        } else {
            if (!this.gameId.equals(gameId)) {
                throw new IllegalStateException("A game with a set id may not be assigned a new id!");
            }
        }
    }

    /**
     * This method returns a 'Space' object,
     * which har the coordinate 'x' and 'y',
     * if 'x' and 'y' are within the bounds of the board,
     * otherwise, it returns null,
     * which means it does not exist on the board.
     * @param x
     * @param y
     * @return
     */
    public Space getSpace(int x, int y) {
        if (x >= 0 && x < width &&
                y >= 0 && y < height) {
            return spaces[x][y];
        } else {
            return null;
        }
    }

    /**
     * This methode returns the number of the players currently in the game,
     * by calling the method size()
     * @return
     */
    public int getPlayersNumber() {
        return players.size();
    }

    /**
     * This method adds player to the borad with calling the method add(player).
     * The methode check two things before adding the player:
     * 1. if the 'board' instance variable of the 'player' is equal to the current 'board.
     * 2. if the 'player' is not already in the 'players'
     * Finaly calls the methode notifyChange().
     * @param player
     */
    public void addPlayer(@NotNull Player player) {
        if (player.board == this && !players.contains(player)) {
            players.add(player);
            notifyChange();
        }
    }

    /**
     * This methode returns the 'players' with index i.
     * If i >= 0 and i < the number of the player,
     * it calls methode get(i) and returns it.
     * Othervise it will return null, which means this 'players' does not exist.
     * @param i
     * @return
     */
    public Player getPlayer(int i) {
        if (i >= 0 && i < players.size()) {
            return players.get(i);
        } else {
            return null;
        }
    }

    /**
     * This methode return the current player.
     * @return
     */
    public Player getCurrentPlayer() {
        return current;
    }

    /**
     * This methode setsthe player as the current player.
     *It checkes two things befor setting:
     * 1. If the palyer is nor the current player.
     * 2. If the player is contained in the 'players'.
     * Finaly it calls the methode notifyChange(),
     * @param player
     */
    public void setCurrentPlayer(Player player) {
        if (player != this.current && players.contains(player)) {
            this.current = player;
            notifyChange();
        }
    }

    /**
     * The methode returns the phase.
     * @return
     */
    public Phase getPhase() {
        return phase;
    }

    /**
     * The methode sets 'phase',
     * if it is not the curentlly phase.
     * Finaly it calls the methode notifyChange(),
     * @param phase
     */
    public void setPhase(Phase phase) {
        if (phase != this.phase) {
            this.phase = phase;
            notifyChange();
        }
    }

    /**
     * The methode returns the current step number of the game.
     * @return
     */
    public int getStep() {
        return step;
    }

    /**
     ** The methode sets 'step',
     * if it is not the curent step.
     * Finaly it calls the methode notifyChange(),
     * @param step
     */
    public void setStep(int step) {
        if (step != this.step) {
            this.step = step;
            notifyChange();
        }
    }

    /**
     * Thes methode returns the 'stepMode',
     * which is a boolean type.
     * @return
     */
    public boolean isStepMode() {
        return stepMode;
    }

    /**
     * This methode sets game to setMode or not setMode,
     * depending on the boolean parameter 'stepMode',
     * if it is not the curent stepMode.
     * Finaly it calls the methode notifyChange(),
     * @param stepMode
     */
    public void setStepMode(boolean stepMode) {
        if (stepMode != this.stepMode) {
            this.stepMode = stepMode;
            notifyChange();
        }
    }

    /**
     * This methode takes an argument of the type of 'player',
     * and it will return its index,
     * if the 'board' field of the 'player is equal to the current instance of the board.
     * Otherwise it will return -1,
     * which means the player does not exist.
     * @param player
     * @return
     */
    public int getPlayerNumber(@NotNull Player player) {
        if (player.board == this) {
            return players.indexOf(player);
        } else {
            return -1;
        }
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
        // This is actually a view aspect, but for making the first task easy for
        // the students, this method gives a string representation of the current
        // status of the game

        // TODO Assignment V1: this string could eventually be refined
        //      The status line should show more information based on
        //      situation; for now, introduce a counter to the Board,
        //      which is counted up every time a player makes a move; the
        //      status line should show the current player and the number
        //      of the current move!
        return "Player = " + getCurrentPlayer().getName();
    }

    // TODO Assignment V1: add a counter along with a getter and a setter, so the
    //      state the board (game) contains the number of moves, which then can
    //      be used to extend the status message including the number of

}
