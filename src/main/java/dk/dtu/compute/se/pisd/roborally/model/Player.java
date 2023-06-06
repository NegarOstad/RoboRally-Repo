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

    //final public Board board;

    private String name;
    private String color;

    private Space space;
    private Heading heading = SOUTH;

    private CommandCardField[] program;
    private CommandCardField[] cards;

    private int tokenCount = 0;

    private boolean registerIsEmpty = false;


    public CommandCardField[] getCards() {
        return cards;
    }

    public void setCards(CommandCard[] commandCards) {
        for (int i = 0; i < cards.length; i++) {
            cards[i].setCard(commandCards[i]);
        }
    }

    public Player(/*@NotNull Board board,*/ String color, @NotNull String name) {
       // this.board = board;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name != null && !name.equals(this.name)) {
            this.name = name;
            notifyChange();
            if (space != null) {
                space.playerChanged();
            }
        }
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
        notifyChange();
        if (space != null) {
            space.playerChanged();
        }
    }

    public Space getSpace() {
        return space;
    }

    public void setSpace(Space space, Board board) {
        boolean moveIsValid = false;
        Space oldSpace = this.space; //holds player's space before move
        if (space != oldSpace) {
            if(space.getPlayer() == null) {
                moveIsValid = true;

            } else {
                if(pushRobot(space.getPlayer(), board))
                    moveIsValid = true;
            }

            if(moveIsValid) {
                if (oldSpace != null) {
                    oldSpace.setPlayer(null , board); // sets the Player for the player's space before move to null so that the robot disappears
                }
                if (space != null) {
                    space.setPlayer(this , board);
                }

                this.space = space; // makes player's space the space passed as argument
                notifyChange();

            } else {
                System.out.println("Invalid move.");
            }
        }
    }

    private boolean pushRobot(Player opponent, Board board){
        boolean canBePushed = false;
        int x = opponent.getSpace().x;
        int y = opponent.getSpace().y;
        Space newSpace = opponent.getSpace();
        switch(heading) {
            case NORTH:
                if (y - 1 >= 0) {
                    newSpace = board.getSpace(x, y - 1);
                    canBePushed = true;
                }
                break;
            case SOUTH:
                if(y + 1 < board.height) {
                    newSpace = board.getSpace(x, y + 1);
                    canBePushed = true;
                }
                break;
            case EAST:
                if(x + 1 < board.width) {
                    newSpace = board.getSpace(x + 1, y);
                    canBePushed = true;
                }
                break;
            case WEST:
                if(x - 1 >= 0) {
                    newSpace = board.getSpace(x - 1, y);
                    canBePushed = true;
                }
                break;
        }
        if(canBePushed)
            opponent.setSpace(newSpace, board);
        return canBePushed;
    }


    public Heading getHeading() {
        return heading;
    }

    public void setHeading(@NotNull Heading heading) {
        if (heading != this.heading) {
            this.heading = heading;
            notifyChange();
            if (space != null) {
                space.playerChanged();
            }
        }
    }

    public CommandCardField getProgramField(int i) {
        return program[i];
    }

    public CommandCardField getCardField(int i) {
        return cards[i];
    }

    public void addToken() {
        tokenCount++;
    }

    public void setTokenCount(int tokenCount){
        this.tokenCount = tokenCount;
    }

    public int getTokenCount() {
        return tokenCount;
    }

    public void setTestRegister(int ver, Board board) {
        if (ver == 1) {
            program[0].setCard(new CommandCard(Command.FORWARD));
            program[1].setCard(new CommandCard(Command.FAST_FORWARD));

        } else if (ver == 2){
            program[0].setCard(new CommandCard(Command.LEFT));
            program[1].setCard(new CommandCard(Command.LEFT));

        } else if (ver == 3){
            program[0].setCard(new CommandCard(Command.FAST_FORWARD));
            program[1].setCard(new CommandCard(Command.LEFT));
            program[2].setCard(new CommandCard(Command.LEFT));
            program[3].setCard(new CommandCard(Command.FAST_FORWARD));


        } else {
            program[0].setCard(new CommandCard(Command.FAST_FORWARD));
            program[1].setCard(new CommandCard(Command.FORWARD));
            program[2].setCard(new CommandCard(Command.LEFT));
            program[3].setCard(new CommandCard(Command.FAST_FORWARD));
            program[4].setCard(new CommandCard(Command.FORWARD));

        }
        board.setPhase(Phase.ACTIVATION);

    }

    public void setEndOfRegister(boolean registerIsEmpty){
        this.registerIsEmpty = registerIsEmpty;
    }

    public boolean getRegisterStatus() {
        return registerIsEmpty;
    }

}
