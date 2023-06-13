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
package dk.dtu.compute.se.pisd.roborally.controller;
import dk.dtu.compute.se.pisd.roborally.fileaccess.api.Repository;
import dk.dtu.compute.se.pisd.roborally.model.Player;
import dk.dtu.compute.se.pisd.roborally.model.*;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * ...
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 *
 */
public class GameController {


    Repository api = Repository.getInstance();
     final public Board board;
    int x = 0;
    int y = 0;
    int counter = 0;

    List<Player> priorityList;

    public GameController(@NotNull Board board) {
        this.board = board;
    }

    /**
     * This is just some dummy controller operation to make a simple move to see something
     * happening on the board. This method should eventually be deleted!
     *
     * @param space the space to which the current player should move
     */
    public void moveCurrentPlayerToSpace(@NotNull Space space)  {
        // TODO Assignment V1: method should be implemented by the students:
        //   - the current player should be moved to the given space
        //     (if it is free()
        //   - and the current player should be set to the player
        //     following the current player
        //   - the counter of moves in the game should be increased by one
        //     if the player is moved

    }
    public void reinitializeBoard(Phase phase, Player current, int step){
        board.setPhase(phase);
        board.setCurrentPlayer(current);
        board.setStep(step);

    }

    // XXX: V2
    public void startProgrammingPhase() {
        board.setPhase(Phase.PROGRAMMING);
        board.setCurrentPlayer(board.getPlayer(0));
        board.setStep(0);

        for (int i = 0; i < board.getPlayerCount(); i++) {
            Player player = board.getPlayer(i);
            if (player != null) {
                for (int j = 0; j < Player.NO_REGISTERS; j++) {
                    CommandCardField field = player.getProgramField(j);
                    field.setCard(null);
                    field.setVisible(true);
                }
                for (int j = 0; j < Player.NO_CARDS; j++) {
                    CommandCardField field = player.getCardField(j);
                    field.setCard(generateRandomCommandCard());
                    field.setVisible(true);
                }
            }
        }
    }

    // XXX: V2
    private CommandCard generateRandomCommandCard() {
        Command[] commands = Command.values();
        int random = (int) (Math.random() * commands.length);
        return new CommandCard(commands[random]);
    }

    // XXX: V2
    public void finishProgrammingPhase() {
        makeProgramFieldsInvisible();
        makeProgramFieldsVisible(0);
        board.setPhase(Phase.ACTIVATION);
        //board.setCurrentPlayer(board.getPlayer(0));
        priorityList = board.getPriorityAntenna().calcClosestPlayers(board.getPlayerList());
        board.setCurrentPlayer(priorityList.get(0));
        board.setOutOfBounds(false); // ????

        board.setStep(0);
    }

    // XXX: V2
    private void makeProgramFieldsVisible(int register) {
        if (register >= 0 && register < Player.NO_REGISTERS) {
            for (int i = 0; i < board.getPlayerCount(); i++) {
                Player player = board.getPlayer(i);
                CommandCardField field = player.getProgramField(register);
                field.setVisible(true);
            }
        }
    }

    // XXX: V2
    private void makeProgramFieldsInvisible() {
        for (int i = 0; i < board.getPlayerCount(); i++) {
            Player player = board.getPlayer(i);
            for (int j = 0; j < Player.NO_REGISTERS; j++) {
                CommandCardField field = player.getProgramField(j);
                field.setVisible(false);
            }
        }
    }

    // XXX: V2
    public void executePrograms() {
        board.setStepMode(false);
        continuePrograms();
    }

    // XXX: V2
    public void executeStep() {
        board.setStepMode(true);
        continuePrograms();

    }

    // XXX: V2
    private void continuePrograms() {
        do {
            executeNextStep();
            String name = board.getGameId().toString();
            System.out.println("Name when saving" + name);
            api.saveBoard(board, String.valueOf(board.getGameId()));

        } while (board.getPhase() == Phase.ACTIVATION && !board.isStepMode());
    }


    // XXX: V2
    private void executeNextStep() {
        System.out.println("Counter : " + counter + ", Current step: " + board.getStep() + ", Current priority player: " + priorityList.get(counter).getName());
        Player currentPlayer = priorityList.get(counter);
        board.setCurrentPlayer(currentPlayer);

        if (board.getPhase() == Phase.ACTIVATION && currentPlayer != null) {
            int step = board.getStep();

            if (step >= 0 && step < Player.NO_REGISTERS) { //DOES THIS IF END OF REGISTERS NOT REACHED
                CommandCard card = currentPlayer.getProgramField(step).getCard();
                if (step != Player.NO_REGISTERS - 1)
                    if (currentPlayer.getProgramField(step + 1).getCard() == null)
                        currentPlayer.setEndOfRegister(true);

                if (card != null) {
                    Command command = card.command;
                    System.out.println("Current command: " + command);
                    executeCommand(currentPlayer, command);
                    if (currentPlayer.getSpace().getBoardElement() != null)
                        currentPlayer.getSpace().getBoardElement().doAction(currentPlayer, board);
                    currentPlayer.setEndOfRegister(false); // CHANGE THIS TO ONLY SET TO FALSE WHEN TURN IS OVER!!!
                }

                if (counter < board.getPlayerCount() - 1) { // DOES THIS IF THERE IS A NEXT PLAYER
                    counter++;

                } else {   // ELSE DOES THIS IF ALL PLAYERS HAVE ACTIVATED THEIR CARD IN REGISTER CORRESPONDING TO GIVEN STEP
                    step++;
                    counter = 0;
                    priorityList = board.getPriorityAntenna().calcClosestPlayers(board.getPlayerList());

                    if (step < Player.NO_REGISTERS) { // DOES THIS IF NOT ALL REGISTERS HAVE BEEN STEPPED TO
                        makeProgramFieldsVisible(step);
                        board.setStep(step);

                    } else { // OR ELSE GOES BACK TO PROGRAMMING PHASE
                        startProgrammingPhase();
                    }
                }
            } else {
                // this should not happen
                assert false;
            }
        } else {
            // this should not happen
            assert false;
        }
    }
    public void executeCommandOptionAndContinue(Command option) {
        board.setPhase(Phase.ACTIVATION);
        Player currentPlayer = board.getCurrentPlayer();
            int step = board.getStep();

                //CommandCard card = currentPlayer.getProgramField(step).getCard();
                if(step != Player.NO_REGISTERS-1)
                    if(currentPlayer.getProgramField(step+1).getCard() == null)
                        currentPlayer.setEndOfRegister(true);


                    switch(option) {
                        case LEFT:
                            executeCommand(currentPlayer, Command.LEFT);
                            break;
                        case RIGHT:
                            executeCommand(currentPlayer, Command.RIGHT);
                            break;
                    }

    }



    // XXX: V2
    private void executeCommand(@NotNull Player player, Command command) {
        if (player != null && command != null) {

            switch (command) {
                case FORWARD:
                    this.moveForward(player);
                    break;
                case RIGHT:
                    this.turnRight(player);
                    break;
                case LEFT:
                    this.turnLeft(player);
                    break;
                case FAST_FORWARD:
                    this.fastForward(player);
                    break;

               case LEFT_RIGHT:

                    board.setPhase(Phase.PLAYER_INTERACTION);

                    break;


                default:
                    // DO NOTHING (for now)
            }
        }
    }

    public void moveForward(@NotNull Player player) {
        Heading currentHeading = player.getHeading();
        Space currentSpace = player.getSpace();
        boolean wallIsBlocking = currentSpace.getType().equals(ElementType.Wall)
                && ((Wall)currentSpace.getBoardElement()).getHeading().equals(currentHeading);
        int newX = currentSpace.x;
        int newY = currentSpace.y;

        switch (currentHeading){
            case SOUTH:
                    if (!wallIsBlocking)
                        newY = newY + 1;
                    if(newY >= 0 && newY < board.height)
                        player.setSpace(board.getSpace(newX, newY), board);
                break;

            case NORTH:
                    if(!wallIsBlocking)
                        newY = newY - 1;
                    if(newY >= 0 && newY < board.height)
                        player.setSpace(board.getSpace(newX, newY), board);
                break;

            case WEST:
                    if (!wallIsBlocking)
                        newX = newX - 1;
                      if(newX >= 0 && newX < board.width)
                          player.setSpace(board.getSpace(newX, newY), board);
                break;

            case EAST:
                    if (!wallIsBlocking)
                        newX = newX + 1;
                    if(newX >= 0 && newX < board.width)
                        player.setSpace(board.getSpace(newX, newY), board);
                break;

            default:
                //DO NOTHING
        }

    }

    // TODO Assignment V2
    public void fastForward(@NotNull Player player) {
        Heading currentHeading = player.getHeading();
        Space currentSpace = player.getSpace();
        boolean wallIsBlocking = currentSpace.getType().equals(ElementType.Wall)
                && ((Wall)currentSpace.getBoardElement()).getHeading().equals(currentHeading);
        int newX = currentSpace.x;
        int newY = currentSpace.y;

        for (int i = 0; i < 2 ; i++) {
            switch (currentHeading){
                case SOUTH:
                    if (!wallIsBlocking)
                        newY = newY + 1;
                    if(newY >= 0 && newY < board.height)
                        player.setSpace(board.getSpace(newX, newY), board);
                    break;

                case NORTH:
                    if(!wallIsBlocking)
                        newY = newY - 1;
                    if(newY >= 0 && newY < board.height)
                        player.setSpace(board.getSpace(newX, newY), board);
                    break;

                case WEST:
                    if (!wallIsBlocking)
                        newX = newX - 1;
                    if(newX >= 0 && newX < board.width)
                        player.setSpace(board.getSpace(newX, newY), board);
                    break;

                case EAST:
                    if (!wallIsBlocking)
                        newX = newX + 1;
                    if(newX >= 0 && newX < board.width)
                        player.setSpace(board.getSpace(newX, newY), board);
                    break;

                default:
                    //DO NOTHING
            }

        }

    }


    // TODO Assignment V2
    public void turnRight(@NotNull Player player) {
        Heading currentHeading = player.getHeading();
        player.setHeading(currentHeading.next());

    }

    // TODO Assignment V2
    public void turnLeft(@NotNull Player player) {
        Heading currentHeading = player.getHeading();
        player.setHeading(currentHeading.prev());

    }

    public boolean moveCards(@NotNull CommandCardField source, @NotNull CommandCardField target) {
        CommandCard sourceCard = source.getCard();
        CommandCard targetCard = target.getCard();
        if (sourceCard != null && targetCard == null) {
            target.setCard(sourceCard);
            source.setCard(null);
            return true;
        } else {
            return false;
        }
    }

}
