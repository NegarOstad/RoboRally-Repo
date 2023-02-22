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

import static dk.dtu.compute.se.pisd.roborally.model.Heading.SOUTH;

/**
 * ...
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 *
 */
public class Player extends Subject {

    final public static int NO_REGISTERS = 5;
    final public static int NO_CARDS = 8;

    final public Board board;

    private String name;
    private String color;

    private Space space;
    private Heading heading = SOUTH;

    private CommandCardField[] program;
    private CommandCardField[] cards;

    /**
     * Constructs a player and creates two CommandCardField arrays for program cards and the
     * @param board board on which the robot moves
     * @param color color of the robot
     * @param name name of the player
     */
    public Player(@NotNull Board board, String color, @NotNull String name) {
        this.board = board;
        this.name = name;
        this.color = color;

        this.space = null;

        program = new CommandCardField[NO_REGISTERS];
        for (int i = 0; i < program.length; i++) {
            program[i] = new CommandCardField(this);
        }

        cards = new CommandCardField[NO_CARDS];
        for (int i = 0; i < cards.length; i++) {
            cards[i] = new CommandCardField(this);
        }
    }

    /**
     * This method is used to access the player's name
     * @return name of the player
     */
    public String getName() {
        return name;
    }

    /**
     * This method is used to set the player's name and notify the observer of the change
     * if the input is different from the current name.
     * SPACE?!
     * @param name of the player
     */
    public void setName(String name) {
        if (name != null && !name.equals(this.name)) {
            this.name = name;
            notifyChange();
            if (space != null) {
                space.playerChanged();
            }
        }
    }

    /**
     * This method is used to access the color of the player's robot
     * @return color of the robot
     */
    public String getColor() {
        return color;
    }
    /**
     * This method is used to set the color of the player's robot
     * @param color of the robot
     */
    public void setColor(String color) {
        this.color = color;
        notifyChange();
        if (space != null) {
            space.playerChanged();
        }
    }

    /**
     * This method is used to access the space on which the player is located
     * @return the space where the player is located
     */
    public Space getSpace() {
        return space;
    }

    /**
     * This method is used update both the space the Player's robot is moving to
     * and the space the robot is moving from
     * @param space the space where the player is to be located
     */
    public void setSpace(Space space) {
        Space oldSpace = this.space;
        if (space != oldSpace &&
                (space == null || space.board == this.board)) {
            this.space = space;
            if (oldSpace != null) {
                oldSpace.setPlayer(null);
            }
            if (space != null) {
                space.setPlayer(this);
            }
            notifyChange();
        }
    }

    /**
     * This method is used to access the direction in which the player is pointed
     * @return  SOUTH, WEST, NORTH or EAST
     */
    public Heading getHeading() {
        return heading;
    }

    /**
     * This method is used to set the direction in which the player is pointed
     * @param heading one of the set constants SOUTH, WEST, NORTH or EAST
     */
    public void setHeading(@NotNull Heading heading) {
        if (heading != this.heading) {
            this.heading = heading;
            notifyChange();
            if (space != null) {
                space.playerChanged();
            }
        }
    }

    /**
     * This method can be used to access a particular program field
     * @param i index of the program array which is to be accessed
     * @return CommandCardField program[i]
     */
    public CommandCardField getProgramField(int i) {
        return program[i];
    }

    /**
     * This method can be used to access a particular card available in the cards array
     * which holds the cards available for use in programming the player's robot???
     * If this is the case, shouldn't NO_CARDS = 9 ??
     * @param i index of the cards array which is to be accessed
     * @return CommandCardField cards[i]
     */
    public CommandCardField getCardField(int i) {
        return cards[i];
    }

}
