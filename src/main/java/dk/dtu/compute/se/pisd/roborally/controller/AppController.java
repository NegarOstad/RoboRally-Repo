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
import dk.dtu.compute.se.pisd.roborally.model.Heading;
import dk.dtu.compute.se.pisd.roborally.model.Player;

import javafx.application.Platform;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.layout.VBox;
import org.jetbrains.annotations.NotNull;

import java.util.*;

import static java.lang.Integer.parseInt;


/**
 * ...
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 *
 */
public class AppController implements Observer {

    final private List<Integer> PLAYER_NUMBER_OPTIONS = Arrays.asList(2, 3, 4, 5, 6);
    final private List<String> PLAYER_COLORS = Arrays.asList("red", "green", "blue", "orange", "grey", "magenta");
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

    int playerNum;

    int numberOfPlayersJoined;
    public AppController(@NotNull RoboRally roboRally) {
        this.roboRally = roboRally;
    }


    public void newGame() throws Exception {
        //// Ask to choose how many players can play in this game
        ChoiceDialog<Integer> dialog = new ChoiceDialog<>(PLAYER_NUMBER_OPTIONS.get(0), PLAYER_NUMBER_OPTIONS);
        dialog.setTitle("Player number");
        dialog.setHeaderText("Select number of players");
        Optional<Integer> count = dialog.showAndWait();
        playerCount = count.orElse(0);
        if (!(count.isEmpty())){
            //// Add new Board ask to choose board
            List<String> boardOptions = List.of(repository.getList("boardOptions"));
            ChoiceDialog<String> boardDialog = new ChoiceDialog<>(boardOptions.get(0) ,boardOptions);
            boardDialog.setTitle("Boards");
            boardDialog.setHeaderText("Choose one board");
            Optional<String> num = boardDialog.showAndWait();
            String boardNum = num.orElse("");
            if(!(num.isEmpty())){
                gameId = repository.newGameId(playerCount, boardNum);
                playerNum = 1;
                numberOfPlayersJoined = 1;
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setContentText("Your game ID is: " + gameId);
                alert.showAndWait();
                //// Here should implements after press ok
                System.out.println("GameID: " + gameId);

                goToWaitingRoom();

            }
           else {
               boardDialog.close();
            }

        }
        else {
            dialog.close();
        }


        }


    public void joinGame() throws Exception {

        if (gameController == null) {
            List<String> availableGames = List.of(repository.availableGamesList());
            ChoiceDialog dialog = new ChoiceDialog(availableGames.get(0), availableGames);

            dialog.setTitle("Join Game");
            dialog.setHeaderText("Which of the following games do you wish to join?");
            dialog.setContentText("Available Games:");
            Optional<String> userChoice = dialog.showAndWait();
            String chosenGameId = userChoice.orElse("");

            System.out.println("Chosen game is: " + chosenGameId);
            if (!(chosenGameId.isEmpty())){
                int userChoiceInt = parseInt(chosenGameId);
                String[] joinInfo = repository.joinGameWithID(userChoiceInt).split(",");
                gameId = parseInt(chosenGameId);
                playerNum = parseInt(joinInfo[0]);
                //System.out.println("Join info [1] player count before parse: " + joinInfo[1]);
                playerCount =  parseInt(joinInfo[1]);
                //System.out.println("Gameid: " + gameId + ", Player num: " + playerNum + ", PlayerCount : " + playerCount);
                goToWaitingRoom();
            }
            else {
                dialog.close();
            }

        }

    }

    private void goToWaitingRoom() throws Exception {

        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("You are in the waiting room!");
        dialog.setHeaderText("Click the 'Update' button to check if the required amount of players have joined.");
        Label playerCountLabel = new Label("Number of players joined: " + numberOfPlayersJoined);
        VBox vBox = new VBox(playerCountLabel);
        dialog.getDialogPane().setContent(vBox);

        // Create an 'Update' button
        ButtonType updateButton = new ButtonType("Update", ButtonType.OK.getButtonData());
        dialog.getDialogPane().getButtonTypes().add(updateButton);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == updateButton) {
                // Call your method here
                //dialog.close();
                try {
                    updateGameState();
                    goToWaitingRoom();
                    dialog.close();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
            return null;
        });

        dialog.showAndWait();

    }

    private void updateGameState() throws Exception {
        System.out.println("Update performed!");
        if(repository.gameIsReady(gameId)){
            Board board = repository.getBoard(String.valueOf(gameId), "boardOptions");
            setUpPlayers(playerCount, board);
            startGame(board, "new");
        } else {
            //show the count of player that are already joined
            numberOfPlayersJoined = repository.getPlayerCount(gameId);

        }

    }


  /*  public void startNewGame() throws Exception {
        Board board = repository.getBoard("boardOptions", chosenGame);
        System.out.println("GameID: " + gameId);
        setUpPlayers(playerCount, board);

        gameController = new GameController(board);
        gameController.startProgrammingPhase();
        roboRally.createBoardView(gameController);
    }*/




        private void setUpPlayers(int noPlayers, Board board ){

        for (int i = 0; i < noPlayers; i++) {
            Player player = new Player(PLAYER_COLORS.get(i), "Player " + (i + 1));
            if(playerNum == i+1)
                player.setLocal(true);
            board.addPlayer(player);
            player.setSpace(board.getSpace(i % board.width, i), board);

        }

    }



    private Board setupBaseBoard(){
        Board board = new Board(8,8);
        board.getSpace(1,3).setTypeGear(Heading.NORTH);
        board.getSpace(4,4).setTypeGear(Heading.EAST);
        board.getSpace(1,3).setTypeGear(Heading.SOUTH);
        board.getSpace(4,0).setTypeCheckpoint(0, false);
        board.getSpace(5,0).setTypeCheckpoint(1,true);
        board.getSpace(2,1).fillConveyorBelt(6, 1, 2, 1 , board);
        board.getSpace(1,6).setTypeConveyor(3, 3);
        board.getSpace(1,6).fillConveyorBelt(3, 3, 1, 6 , board);
        board.getSpace(7,6).setTypeConveyor(5, 6);
        board.getSpace(7,6).fillConveyorBelt(5, 6, 7, 6 , board);
        board.getSpace(0,5).setTypeWall();
        board.getSpace(5,3).setTypeWall();
        board.setTypePriorityAntenna(7, 7);
        return  board;
    }

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
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setContentText("Game is saved.");
            alert.showAndWait();
            //exit();
        }
        if(result == null ){
            textInputDialog.close();
        }
    }

    public void loadGame() throws Exception {

        if (gameController == null) {
            gameFiles = repository.getList("templates");
            ChoiceDialog dialog = new ChoiceDialog(gameFiles[0], gameFiles);
            System.out.println("choice dialog set");
            dialog.setTitle("Load Game");
            dialog.setHeaderText("Which game do you want to continue?");
            dialog.setContentText("Saved Games:");
            Optional<String> userChoice = dialog.showAndWait();
            String result = userChoice.orElse("");
            Board board = null;
            if(!(result.isEmpty())){
                System.out.println(userChoice);
                System.out.println(result);
                try {
                    board = repository.loadGame(result);

                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }else {
                dialog.close();
            }

            System.out.println("we made it after dialog.close");
            if(board != null) {
                List<Integer> playerNums = makePlayerNumList(board.getPlayerCount());
                dialog = new ChoiceDialog(playerNums.get(0), playerNums);
                dialog.setTitle("Load Game");
                dialog.setHeaderText("Which player do you want to be?");
                dialog.setContentText("Available players:");
                Optional<Integer> playerChoice = dialog.showAndWait();
                int chosenPlayer = playerChoice.orElse(0);
                setLocalPlayer(board, chosenPlayer - 1);
                startGame(board, "load");
            } else {
                dialog.close();
            }
        }
    }

    private List<Integer> makePlayerNumList(int playerCount) {
            List<Integer> nums = new ArrayList<>();
            for (int i = 0; i < playerCount; i++) {
                nums.add(i+1);
            }
        return nums;
    }


    private void setLocalPlayer(Board board, int chosenPlayer) {
            board.getPlayer(chosenPlayer).setLocal(true);
    }


    private void startGame(Board board, String startType){
        gameController = new GameController(board);
        if(startType.equals("load"))
            gameController.reinitializeBoard(board.getPhase(), board.getCurrentPlayer(), board.getStep());
        else if (startType.equals("new"))
            gameController.startProgrammingPhase();
        else
            System.out.println("Start Type not recognized.");

        roboRally.createBoardView(gameController);

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

    public void exit() {
        if (gameController != null) {
            if (!isGameSaved) {
                Alert alert = new Alert(AlertType.CONFIRMATION);
                alert.setTitle("Do you want to exit and save the game?");
                alert.setContentText("Do you want to save the game before leaving?");
                Optional<ButtonType> userOption = alert.showAndWait();

                if (userOption.isPresent() && userOption.get() == ButtonType.OK) {
                    saveGame();
                }
            }
             // If the user did not cancel, the RoboRally application will exit after the option to save the game
            // Exits the game when saved, aswell as when game is not saved
            if (stopGame()) {
                Platform.exit();
            }
        } else {
            Platform.exit();
        }
    }


    /*public void exit()  {
        if (gameController != null) {
            if (!isGameSaved) {
                saveGame();
            }

            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Exit RoboRally?");
            alert.setContentText("Do you want to exit RoboRally?");
            Optional<ButtonType> result = alert.showAndWait();

            if (!result.isPresent() || result.get() != ButtonType.OK) {
                return; // return without exiting the application

            }
        }

        // If the user did not cancel, the RoboRally application will exit
        // after the option to save the game
        if (gameController == null || stopGame()) {

            Platform.exit();
        }
    }
  /*  public void saveGame() {
        TextInputDialog textInputDialog = new TextInputDialog();
        textInputDialog.setHeaderText("Save Game");
        textInputDialog.setContentText("Enter a name for your game:");
        textInputDialog.showAndWait();
        String result = textInputDialog.getResult();
        LoadBoard.saveBoard(gameController.board, result);
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setContentText("Game is saved.");
        alert.showAndWait();
        exit();
    }

    public void loadGame()  {
        // XXX needs to be implemented eventually
        // for now, we just create a new game
        if (gameController == null) {

            newGame(LoadBoard.loadBoard("mygame"));

        }
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
   /* public boolean stopGame()  {
        if (gameController != null) {

            // here we save the game (without asking the user).
            gameController = null;
            roboRally.createBoardView(null);

            HttpRequest httpRequest =
                    HttpRequest.newBuilder().GET().uri(URI.create("http://10.209.204.5:8080/stop/" ))
                            .build();
            httpClient.sendAsync(httpRequest, HttpResponse.BodyHandlers.ofString()).thenAccept(System.out::println).join();
            return true;
        }
        return false;
    }

    public void exit()  {
        if (gameController != null) {
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Exit RoboRally?");
            alert.setContentText("Do you want to exit RoboRally?");
            Optional<ButtonType> result = alert.showAndWait();

            if (!result.isPresent() || result.get() != ButtonType.OK) {
                return; // return without exiting the application
            }
        }

        // If the user did not cancel, the RoboRally application will exit
        // after the option to save the game
        if (gameController == null || stopGame()) {
            Platform.exit();
        }
    }*/

    public boolean isGameRunning() {
        return gameController != null;
    }


    @Override
    public void update(Subject subject) {
        // XXX do nothing for now
    }

}
