
package dk.dtu.compute.se.pisd.roborally.controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import dk.dtu.compute.se.pisd.designpatterns.observer.Observer;
import dk.dtu.compute.se.pisd.designpatterns.observer.Subject;

import dk.dtu.compute.se.pisd.roborally.RoboRally;

import dk.dtu.compute.se.pisd.roborally.fileaccess.LoadBoard;
import dk.dtu.compute.se.pisd.roborally.model.Board;
import dk.dtu.compute.se.pisd.roborally.model.ElementType;
import dk.dtu.compute.se.pisd.roborally.model.Heading;
import dk.dtu.compute.se.pisd.roborally.model.Player;

import javafx.application.Platform;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceDialog;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileNotFoundException;
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
    final private List<Integer> BOARD_NUMBER = Arrays.asList(1);
    final private List<String> COUNTINUE_OR_NOT = Arrays.asList("Yes", "N0");
    private String gameName;

    /*
    HttpClient httpClient =
            HttpClient.newBuilder().version(HttpClient.Version.HTTP_1_1)
                    .connectTimeout(Duration.ofSeconds(20)).build();*/
    final private RoboRally roboRally;

    private GameController gameController;


    public AppController(@NotNull RoboRally roboRally) {
        this.roboRally = roboRally;
    }

    public void newGame() {
        ChoiceDialog<Integer> dialog = new ChoiceDialog<>(PLAYER_NUMBER_OPTIONS.get(0), PLAYER_NUMBER_OPTIONS);
        dialog.setTitle("Player number");
        dialog.setHeaderText("Select number of players");
        Optional<Integer> result = dialog.showAndWait();


        //ChoiceDialog<Integer> boardDialog = new ChoiceDialog<>();
        //boardDialog.setTitle("Boards");
        //boardDialog.setHeaderText("Choose board");
        //Optional<Integer> boardResult = boardDialog.showAndWait();

        if (result.isPresent()) {
            if (gameController != null) {
                if (!stopGame()) {
                    return;
                }
            }

            Board board = setupBaseBoard();
            gameController = new GameController(board);

            int no = result.get();
            for (int i = 0; i < no; i++) {
                Player player = new Player(PLAYER_COLORS.get(i), "Player " + (i + 1));
                board.addPlayer(player);
                player.setSpace(board.getSpace(i % board.width, i), board);

            }

            gameController.startProgrammingPhase();

            roboRally.createBoardView(gameController);
        }
    }
    public void newGame(Board board){
        gameController = new GameController(board);
        gameController.reinitializeBoard(board.getPhase(), board.getCurrentPlayer(), board.getStep());
        roboRally.createBoardView(gameController);

    }



    private Board setupBaseBoard(){
        Board board = new Board(8,8);
        board.getSpace(1, 3).setTypeGear(false, true); // Turn right
        board.getSpace(4, 4).setTypeGear(true, false); // Turn left
        board.getSpace(1, 3).setTypeGear(false, true); // Turn right
        board.getSpace(4,0).setTypeCheckpoint(0, false);
        board.getSpace(5,0).setTypeCheckpoint(1,true);
        board.getSpace(2,1).fillConveyorBelt(6, 1, 2, 1 , board, null);
        board.getSpace(1,6).setTypeConveyor(3, 3);
        board.getSpace(1,6).fillConveyorBelt(3, 3, 1, 6 , board, null);
        board.getSpace(7,6).setTypeConveyor(5, 6);
        board.getSpace(7,6).fillConveyorBelt(5, 6, 7, 6 , board, null);
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
    public boolean stopGame()  {
        if (gameController != null) {

            // here we save the game (without asking the user).
            //saveGame();
//saveGame();
            gameController = null;
            roboRally.createBoardView(null);
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
    }

    public boolean isGameRunning() {
        return gameController != null;
    }


    @Override
    public void update(Subject subject) {
        // XXX do nothing for now
    }

}
