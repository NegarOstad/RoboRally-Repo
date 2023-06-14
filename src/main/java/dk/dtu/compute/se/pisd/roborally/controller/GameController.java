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
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

/**
 * ...
 *
 * @author
 * Melissa Woo, s224311@dtu.dk
 * Bayan Al Dowairi, s224329@dtu.dk
 * Amira Omar, s205821@dtu.dk
 * Besma Al Jwadi, s224325@dtu.dk
 * Negar Ostad, s224283@dtu.dk
 *
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

    public void reinitializeBoard(Phase phase, Player current, int step){
        board.setPhase(phase);
        board.setCurrentPlayer(current);
        board.setStep(step);

    }

    /***
     * Gives the players a random set of cards to program from and resets all cards in
     * the 5 programming registers to null
     */
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

    private CommandCard generateRandomCommandCard() {
        Command[] commands = Command.values();
        int random = (int) (Math.random() * commands.length);
        return new CommandCard(commands[random]);
    }


    /***
     * Deactivates the program fields and tells the server that this user is ready to activate
     * Makes a list of players ordered by closeness to the priority antenna,
     * @throws Exception
     */
    public void finishProgrammingPhase() throws Exception {
        makeProgramFieldsInvisible();
        makeProgramFieldsVisible(0);
       api.setReady(board.getGameId());

        board.setPhase(Phase.ACTIVATION);
        //board.setCurrentPlayer(board.getPlayer(0));
        priorityList = board.getPriorityAntenna().calcClosestPlayers(board.getPlayerList());
        board.setCurrentPlayer(priorityList.get(0));
        board.setOutOfBounds(false); // ????

        board.setStep(0);
    }


    private void makeProgramFieldsVisible(int register) {
        if (register >= 0 && register < Player.NO_REGISTERS) {
            for (int i = 0; i < board.getPlayerCount(); i++) {
                Player player = board.getPlayer(i);
                CommandCardField field = player.getProgramField(register);
                field.setVisible(true);
            }
        }
    }

    private void makeProgramFieldsInvisible() {
        for (int i = 0; i < board.getPlayerCount(); i++) {
            Player player = board.getPlayer(i);
            for (int j = 0; j < Player.NO_REGISTERS; j++) {
                CommandCardField field = player.getProgramField(j);
                field.setVisible(false);
            }
        }
    }

    /***
     * Checks with server if all users finished programming. Checks with server if it is the
     * users turn to execute and if so, goes to execute all cards in register
     */
    public void executePrograms() {
        System.out.println(api.areAllReady(board.getGameId()));
        if(api.areAllReady(board.getGameId())){
            int turn = api.getTurn(board.getGameId());
            if (priorityList.get(turn).isLocal()) {
                System.out.println("TURN" + turn);
                board.setStepMode(false);
                continuePrograms();
            }

        }

    }

    // has been disabled for this project
    public void executeStep() {
        board.setStepMode(true);
        continuePrograms();

    }

    /***
     * Executes all steps in users register and saves a copy of the game state
     * in the server after each step
     */
    private void continuePrograms() {
        do {
            executeNextStep();
            api.saveBoard(board, String.valueOf(board.getGameId()));

        } while (board.getPhase() == Phase.ACTIVATION && !board.isStepMode());
    }


    /***
     * Handles all condition checks involved in executing a register and keeps track of and updates the
     * value of the fields involved. This includes the current step in the register, the command
     * card to be executed, if the player has landed on a checkpoint and if they win the game. It also
     * performs the action, if the space landed on contains a board element
     */
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

                    if(board.getWinnerStatus()) {
                        displayWinnerMessage(currentPlayer.getName());
                        board.setPhase(Phase.END); // disables all buttons
                        return;
                    }

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
                        api.setExecuted(board.getGameId());
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

    private void displayWinnerMessage(String name){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Game is won!");
        alert.setContentText(name + " is the winner!\n Press 'Stop Game' to return to the main menu.");
        alert.showAndWait();

    }

    /***
     * Used to execute an interactive card which requires user input.
     * @param option the option which the user has clicked
     */
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


    /***
     * Directs the program to the correct method according to the command to be executed
     * @param player the player which the command is to be executed on
     * @param command the command to execute
     */
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

    /***
     * Moves the player a single space in the direction they are currently facing
     * @param player player to be moved
     */
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

    /***
     * Moves the player for up to two spaces in the direction they are currently facing
     * @param player player to be moved
     */
    public void fastForward(@NotNull Player player) {
        Heading currentHeading = player.getHeading();
        int newX = player.getSpace().x;
        int newY = player.getSpace().y;

        for (int i = 0; i < 2 ; i++) {
            Space currentSpace = player.getSpace();
            boolean wallIsBlocking = currentSpace.getType().equals(ElementType.Wall)
                    && ((Wall)currentSpace.getBoardElement()).getHeading().equals(currentHeading);

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


    /***
     * Turns the player right (fx if facing SOUTH, turns to the WEST)
     * @param player player to be rotated
     */
    public void turnRight(@NotNull Player player) {
        Heading currentHeading = player.getHeading();
        player.setHeading(currentHeading.next());

    }

    /***
     * Turns the player left (fx if facing SOUTH, turns to the EAST)
     * @param player player to be rotated
     */
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
