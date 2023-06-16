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

import dk.dtu.compute.se.pisd.designpatterns.observer.Observer;
import dk.dtu.compute.se.pisd.designpatterns.observer.Subject;

import dk.dtu.compute.se.pisd.roborally.RoboRally;

import dk.dtu.compute.se.pisd.roborally.fileaccess.api.Repository;
import dk.dtu.compute.se.pisd.roborally.model.Board;
import dk.dtu.compute.se.pisd.roborally.model.Player;

import javafx.application.Platform;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.List;

import static java.lang.Integer.parseInt;


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
public class AppController implements Observer {

    final private List<Integer> PLAYER_NUMBER_OPTIONS = Arrays.asList(2, 3, 4, 5, 6);
    final private List<String> PLAYER_COLORS = Arrays.asList("hotpink", "purple", "lightblue", "salmon", "lavender", "magenta");
    final private List<Integer> BOARD_NUMBER = Arrays.asList(1,2);
    final private List<String> COUNTINUE_OR_NOT = Arrays.asList("Yes" , "N0");
    private String[] gameFiles;
    private String gameName ;
    private int gameId;

    Repository repository = Repository.getInstance();

    private boolean isGameSaved = false;

    final private RoboRally roboRally;

    private GameController gameController;

    int playerCount;
    int localPlayerNum;

    int numberOfPlayersJoined;

    String existingBoardName;

    boolean loadExisting;
    public AppController(@NotNull RoboRally roboRally) {
        this.roboRally = roboRally;
    }

    /***
     * Gets a player count and board choice from user and requests server to create a new game.
     */
    public void newGame() throws Exception {
        setPlayerCountFromChoice();
        // GET BOARD CHOICE FROM USER
        if (playerCount != 0) {
            String boardName = getBoardChoiceFromUser();

            // SET UP GAME AND FIRST PLAYER NUMBER
            if (!(boardName.isEmpty())) {
                loadExisting = false;
                gameId = repository.newGameId(playerCount, boardName, loadExisting);
                localPlayerNum = 1;
                numberOfPlayersJoined = 1;
                String gameIdString = "Your game ID is: " + gameId;
                createAlert(AlertType.INFORMATION, gameIdString);
                System.out.println("GameID: " + gameId);

                goToWaitingRoom();
            }
        }
    }

        private void setPlayerCountFromChoice(){
            ChoiceDialog<Integer> dialog = createChoiceDialog(PLAYER_NUMBER_OPTIONS.get(0), PLAYER_NUMBER_OPTIONS,
                    "Player number", "Select number of players", " ");
            Optional<Integer> count = dialog.showAndWait();
            if(count.isEmpty()){
                dialog.close();
            } else {
                    playerCount = count.orElse(0);
                }
        }

        private String getBoardChoiceFromUser() throws Exception {
            List<String> boardOptions = List.of(repository.getList("boardOptions"));
            ChoiceDialog<String> boardDialog = createChoiceDialog(boardOptions.get(0), boardOptions,
                    "Boards", "Choose one board", " ");
            Optional<String> num = boardDialog.showAndWait();
            if(num.isEmpty()){
                boardDialog.close();
            }
            return num.orElse("");
        }

    /***
     * Gets a choice from user from a list of games waiting for players
     * and uses it to get player number for user's robot and total amount of players
     * in chosen game from the server
     */
    public void joinGame() throws Exception {

        if (gameController == null) {
           String chosenGameId = getUserChoiceJoinGame();
            loadExisting = repository.getLoadExisting(chosenGameId);
            System.out.println("Chosen game is: " + chosenGameId);
            if (!(chosenGameId.isEmpty())){
                int userChoiceInt = parseInt(chosenGameId);
                setJoinInfo(chosenGameId, repository.joinGameWithID(userChoiceInt).split(","));
                goToWaitingRoom();

            }

        }

    }

        private String getUserChoiceJoinGame() throws Exception {
            List<String> availableGames = List.of(repository.availableGamesList());
            ChoiceDialog dialog = createChoiceDialog(availableGames.get(0), availableGames,
                    "Join Game", "Which of the following games do you wish to join?",
                    "Available games:");

            Optional<String> userChoice = dialog.showAndWait();
            if(userChoice.isEmpty()){
                dialog.close();
            }
            return userChoice.orElse("");
        }

        private void setJoinInfo(String chosenGameId, String[] joinInfo) throws Exception {
            gameId = parseInt(chosenGameId);
            localPlayerNum = parseInt(joinInfo[0]);
            playerCount =  parseInt(joinInfo[1]);
            numberOfPlayersJoined  = repository.getPlayerCount(gameId);
        }

    /***
     * Creates a dialog window that serves as a waiting room. Contains an update button,
     * which asks the server if the expected amount of players has joined. If so, creates
     * a new board and players, and starts the game, or else shows the amount of players
     * currently waiting
     */
    private void goToWaitingRoom() throws Exception {
        //CREATE DIALOG WINDOW FOR WAITING ROOM
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("You are in the waiting room!");
        dialog.setHeaderText("Click the 'Update' button to check if the required amount of players have joined.");
        Label playerCountLabel = new Label("Number of players joined: " + numberOfPlayersJoined);

        //CREATE UPDATE BUTTON
        Button updateButton = new Button("Update");
        VBox vBox = new VBox(playerCountLabel, updateButton);
        dialog.getDialogPane().setContent(vBox);

        //MAKE UPDATE BUTTON CHECK IF GAME IS STILL WAITING FOR PLAYERS OR IF READY CLOSE DIALOG
        updateButton.setOnAction(event -> {

                try {
                    if(waitingForPlayers()){
                        playerCountLabel.setText("Number of players joined: " + numberOfPlayersJoined);
                    } else {
                        System.out.println("reached here");
                        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.CANCEL);
                        dialog.close();
                    }

                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

        });

        dialog.show();

    }

    private boolean waitingForPlayers() throws Exception {
        System.out.println("Update performed!");
        if(repository.gameIsReady(gameId)){
            if(loadExisting){
                Board board = repository.loadGameState(existingBoardName);
                board.setGameId(gameId);
                for(int i = 0 ; i < board.getPlayers().size() ; i++) {
                    if (localPlayerNum == i + 1) {
                        board.getPlayer(i).setLocal(true);
                    }
                }
                startGame(board, "load");
            } else {
                Board board = repository.getNewBoard(String.valueOf(gameId));
                board.setGameId(gameId);
                setUpPlayers(playerCount, board);
                startGame(board, "new");
            }
            return false;
        } else {
            //show the count of player that are already joined
            numberOfPlayersJoined = repository.getPlayerCount(gameId);
            return true;
        }

    }


        private void setUpPlayers(int noPlayers, Board board ){

        for (int i = 0; i < noPlayers; i++) {
            Player player = new Player(PLAYER_COLORS.get(i), "Player " + (i + 1));
            if(localPlayerNum == i+1)
                player.setLocal(true);
            board.addPlayer(player);
            player.setSpace(board.getSpace(i % board.width, i), board);

        }

    }

    /***
     * Prompts the user to enter a file name to save the game as a json string or to cancel
     */
    public void saveGame() {
        TextInputDialog textInputDialog = new TextInputDialog();
        textInputDialog.setHeaderText("Save Game");
        textInputDialog.setContentText("Enter a name for your game:");
        textInputDialog.showAndWait();
        String result = textInputDialog.getResult();
        System.out.println(result);

        if(result != null ){
            repository.saveBoard(gameController.board, result);
            isGameSaved = true;
            createAlert(AlertType.INFORMATION, "Game is saved.");
        } else {
            textInputDialog.close();
        }
    }

    /***
     * Creates a temporary board to deserialize from a game state saved on server and adds
     * the appropriate information to the current game's board and its players.
     * @throws Exception if cannot load the select game state
     */
    public void updateGameState() throws Exception {
            Board updatedBoard;
        try {
            updatedBoard = repository.loadGameState(String.valueOf(gameController.board.getGameId()));
            //gameController.board.setPhase(updatedBoard.getPhase());
            gameController.board.setStep(updatedBoard.getStep());
            gameController.board.setCurrentPlayer(updatedBoard.getCurrentPlayer());
            for(Player p : gameController.board.getPlayers()){
                for(Player p_up : updatedBoard.getPlayers()) {
                    if(p.getName().equals(p_up.getName())) {
                        p.updateSpace(p_up.getSpace(), gameController.board);
                        p.setTokenCount(p_up.getTokenCount());
                        p.setHeading(p_up.getHeading());
                    }
                }
            }
            //update(gameController.board);
            System.out.println("We went into the try of the updateGameState");

        } catch (Exception e) {
            throw new RuntimeException(e);
        }


    }

    /***
     * Prompts the user to choose a board to load game from and recreates that board
     * and the game state surrounding it. It then starts a game
     * @throws Exception if cannot load chosen board
     */
    public void loadGame() throws Exception {

        if (gameController == null) {
            existingBoardName = getUserBoardChoice();

                System.out.println(existingBoardName);
                try {
                    loadExisting = true;
                    gameId = repository.newGameId(3, existingBoardName, true);
                    goToWaitingRoom();
                   // board = repository.loadGameState(result);

                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

            }
            // choosing specific player
           /* if(board != null) {
                int chosenPlayer = getUserPlayerChoice(board);
                setLocalPlayer(board, chosenPlayer - 1);
                startGame(board, "load");

            } */
    }

        private String getUserBoardChoice() throws Exception {
            List<String> gameFileList = Arrays.stream(repository.getList("templates")).toList();
            ChoiceDialog dialog = createChoiceDialog(gameFileList.get(0), gameFileList, "Load Game",
                    "Which game do you want to continue?",
                    "Saved Games:");

            Optional<String> userChoice = dialog.showAndWait();
            if(userChoice.isEmpty())
                dialog.close();
            return userChoice.orElse("");
        }

        private int getUserPlayerChoice(Board board) {
            List<Integer> playerNums = makePlayerNumList(board.getPlayerCount());
            ChoiceDialog<Integer> dialog = createChoiceDialog(playerNums.get(0), playerNums, "Load Game",
                    "Which player do you want to be?", "Available players:");

            Optional<Integer> playerChoice = dialog.showAndWait();
            return playerChoice.orElse(0);

        }

            private List<Integer> makePlayerNumList(int playerCount) {
                    List<Integer> nums = new ArrayList<>();
                    for (int i = 0; i < playerCount; i++) {
                        nums.add(i+1);
                    }
                return nums;
            }

    /***
     * Sets the player chosen by the user to be the local player for this game
     * @param board Board object which the game is operating on
     * @param chosenPlayer the player, the user wants to play as
     */
        private void setLocalPlayer(Board board, int chosenPlayer) {
                board.getPlayer(chosenPlayer).setLocal(true);
        }

    /***
     * Starts a new game either from scratch or reinitializing an existing board
     * @param board Board that the game to be started should be played on
     * @param startType if "load" then reinitialize both game state and existing players
     *                  if "new" then create new board and views as well as new players in their
     *                  default start positions
     */
        private void startGame(Board board, String startType){
            gameController = new GameController(board);
            if(startType.equals("load"))
                System.out.println("loading existing game");
            else if (startType.equals("new"))
                gameController.startProgrammingPhase();
            else
                System.out.println("Start Type not recognized.");

            roboRally.createBoardView(gameController);

        }

        private <T> ChoiceDialog<T> createChoiceDialog(T defaultChoice, List<T> choices, String title,
                                                       String headerText, String contentText) {
            ChoiceDialog<T> dialog = new ChoiceDialog<>(defaultChoice, choices);
            dialog.setTitle(title);
            dialog.setHeaderText(headerText);
            dialog.setContentText(contentText);
            return dialog;
        }

        private void createAlert(AlertType alertType, String contentText){
            Alert alert = new Alert(alertType);
            alert.setContentText(contentText);
            alert.showAndWait();
        }

    /**
     * Stop playing the current game, giving the user the option to save
     * the game or to cancel stopping the game. The method returns true
     * if the game was successfully stopped (with or without saving the
     * game); returns false, if the current game was not stopped. In case
     * there is no current game, false is returned.
     *
     * @return true if the current game was stopped, false otherwise
     */

    public boolean stopGame()  {
        if (gameController != null) {

            // here we save the game (without asking the user).
            saveGame();
            
            gameController = null;
            roboRally.createBoardView(null);
            return true;
        }
        return false;
    }

    public void exit() throws Exception {
        if (gameController != null) {
            if (isGameSaved) {
                // If the game is already saved, directly exit the game
                Platform.exit();
            } else {
                Alert alert = new Alert(AlertType.CONFIRMATION);
                alert.setTitle("Exit RoboRally?");
                alert.setContentText("Do you want to save before exiting Robo Rally?");
                Optional<ButtonType> userOption = alert.showAndWait();

                if (userOption.isPresent() && userOption.get() == ButtonType.CANCEL) {
                    // If the user chooses to exit without saving, stop the game and exit
                        repository.deleteGame(gameId); // delete game from server resources folder
                        Platform.exit();

                } else if (userOption.isPresent() && userOption.get() == ButtonType.OK) {
                    //If the user chooses to exit and save, save the game then stop game and exit
                    saveGame();
                    Platform.exit();
                }

            }
        }
    }

    public boolean isGameRunning() {
        return gameController != null;
    }


    @Override
    public void update(Subject subject) {
        // XXX do nothing for now
    }

}
