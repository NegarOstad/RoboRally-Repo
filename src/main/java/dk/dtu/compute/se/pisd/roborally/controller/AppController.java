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

import dk.dtu.compute.se.pisd.roborally.fileaccess.LoadBoard;
import dk.dtu.compute.se.pisd.roborally.model.Board;
import dk.dtu.compute.se.pisd.roborally.model.Heading;
import dk.dtu.compute.se.pisd.roborally.model.Player;

import javafx.application.Platform;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceDialog;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.*;

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

    private String gameID;

    private boolean isGameSaved = false;

    final private RoboRally roboRally;
    HttpClient httpClient =
            HttpClient.newBuilder().version(HttpClient.Version.HTTP_1_1)
                    .connectTimeout(Duration.ofSeconds(20)).build();

    private GameController gameController;


    public AppController(@NotNull RoboRally roboRally) {
        this.roboRally = roboRally;
    }


    public void newGame() throws IOException, InterruptedException {
        ChoiceDialog<Integer> dialog = new ChoiceDialog<>(PLAYER_NUMBER_OPTIONS.get(0), PLAYER_NUMBER_OPTIONS);
        dialog.setTitle("Player number");
        dialog.setHeaderText("Select number of players");
        Optional<Integer> userChoice = dialog.showAndWait();
        int result = userChoice.orElse(0);
        LoadBoard.newGame(result);

        //// Add new Board
        ChoiceDialog<Integer> boardDialog = new ChoiceDialog<>(BOARD_NUMBER.get(0) ,BOARD_NUMBER );
        boardDialog.setTitle("Boards");
        boardDialog.setHeaderText("Choose one board");
        Optional<Integer> boardResult = boardDialog.showAndWait();


        if (userChoice.isPresent()) {
            if (gameController != null) {
                // The UI should not allow this, but in case this happens anyway.
                // give the user the option to save the game or abort this operation!
                if (!stopGame()) {
                    return;
                }
            }

            //createGame();
        }
    }

    private void createGame(Board board){
        gameController = new GameController(board);
        gameController.reinitializeBoard(board.getPhase(), board.getCurrentPlayer(), board.getStep());
        roboRally.createBoardView(gameController);

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
            LoadBoard.saveBoard(gameController.board, result);
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

    public void loadGame() throws IOException, InterruptedException {

        // XXX needs to be implemented eventually
        // for now, we just create a new game
        if (gameController == null) {
            // newGame(LoadBoard.loadBoard("mygame"));
            gameFiles = LoadBoard.getList();
            ChoiceDialog dialog = new ChoiceDialog(gameFiles[0], gameFiles);

            //ChoiceDialog<String> dialog = new ChoiceDialog<>();
            //dialog.getItems().addAll(LOAD_GAME);
            dialog.setTitle("Load Game");
            dialog.setHeaderText("Which game do you want to continue?");
            dialog.setContentText("Saved Games:");
            Optional<String> userChoice = dialog.showAndWait();
            String result = userChoice.orElse("");
            //Optional result = dialog.showAndWait();
            //String fullpath = filename+result.toString();
            System.out.println(userChoice);
            System.out.println(result);
            createGame(LoadBoard.loadBoard(result));

        }
    }

    /***
     * Getting a list of all the files in the resource folder
     * Based on a solution found on Stackoverflow (https://stackoverflow.com/questions/5694385/getting-the-filenames-of-all-files-in-a-folder)
     * used together with a solution from  Baeldung (https://www.baeldung.com/java-filename-without-extension)
     */
    /*private void setFileNames(){
        File resources = new File("src/main/resources/boards/");
        File[] listOfFiles = resources.listFiles();
        for(int i = 0 ; i < listOfFiles.length ; i++){
            if(listOfFiles[i].isFile()) {
                String filename = Files.getNameWithoutExtension(listOfFiles[i].getName());
                gameFiles.add(filename);
            }
        }
    }*/

    /**
     * Stop playing the current game, giving the user the option to save
     * the game or to cancel stopping the game. The method returns true
     * if the game was successfully stopped (with or without saving the
     * game); returns false, if the current game was not stopped. In case
     * there is no current game, false is returned.
     *
     * @return true if the current game was stopped, false otherwise
     */

    /*


     */
    public boolean stopGame()  {
        if (gameController != null) {

            // here we save the game (without asking the user).

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
