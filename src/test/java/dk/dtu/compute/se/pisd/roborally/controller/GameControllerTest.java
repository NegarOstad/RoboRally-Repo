package dk.dtu.compute.se.pisd.roborally.controller;

import dk.dtu.compute.se.pisd.roborally.model.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

class GameControllerTest {

    private final int TEST_WIDTH = 8;
    private final int TEST_HEIGHT = 8;

    private GameController_stub gameController;

    Checkpoint[] checkpoints = new Checkpoint[2];


    @BeforeEach
    void setUp() {
        Board board = new Board(TEST_WIDTH, TEST_HEIGHT);
        gameController = new GameController_stub(board);
        for (int i = 0; i < 1; i++) {
            Player player = new Player(null,"Player " + i);
            board.addPlayer(player);
            player.setSpace(board.getSpace(i, i), board);
            player.setHeading(Heading.values()[i % Heading.values().length]);
        }
        board.setCurrentPlayer(board.getPlayer(0));
        board.getSpace(0,3).setTypeCheckpoint(0, false);
        board.getSpace(0,4).setTypeCheckpoint(1, true);
        board.getSpace(5,0).setTypeCheckpoint(1, true);

        /*checkpoints[0] = new Checkpoint(board.getSpace(0,3));
        checkpoints[0].setIndex(0);
        checkpoints[1] = new Checkpoint(board.getSpace(5, 0));
      checkpoints[1].setIndex(1);*/
    }

    @AfterEach
    void tearDown() {
        gameController = null;
    }


    @Test
    void moveForward() {
        Board board = gameController.board;
        Player current = board.getCurrentPlayer();

        gameController.moveForward(current);

        Assertions.assertEquals(current, board.getSpace(0, 1).getPlayer(), "Player " + current.getName() + " should beSpace (0,1)!");
        Assertions.assertEquals(Heading.SOUTH, current.getHeading(), "Player 0 should be heading SOUTH!");
        Assertions.assertNull(board.getSpace(0, 0).getPlayer(), "Space (0,0) should be empty!");
    }

    @Test
    void moveAwayAndBackToStartPosition() {
        Board board = gameController.board;
        Player player1 = board.getCurrentPlayer();
        player1.setTestRegister(3, board);
        board.setPhase(Phase.ACTIVATION);
        gameController.executePrograms();
        Assertions.assertEquals(player1, board.getSpace(0, 0).getPlayer(), "Player " + player1.getName() + " should beSpace (0,0)!");


    }

    @Test
    void fastForward() {
        Board board = gameController.board;
        Player current = board.getCurrentPlayer();

        gameController.fastForward(current);

        Assertions.assertEquals(current, board.getSpace(0, 2).getPlayer(), "Player " + current.getName() + " should beSpace (0,2)!");
        Assertions.assertEquals(Heading.SOUTH, current.getHeading(), "Player 0 should be heading SOUTH!");
        Assertions.assertNull(board.getSpace(0, 0).getPlayer(), "Space (0,0) should be empty!");
    }

    @Test
    void turnRight() {
        Board board = gameController.board;
        Player current = board.getCurrentPlayer();

        gameController.turnRight(current);

        Assertions.assertEquals(Heading.WEST, current.getHeading(), "Player " + current.getName() + " should be oriented westward");

    }

    @Test
    void turnLeft() {
        Board board = gameController.board;
        Player current = board.getCurrentPlayer();

        gameController.turnLeft(current);

        Assertions.assertEquals(Heading.EAST, current.getHeading(), "Player " + current.getName() + " should be oriented Eastward");

    }


        @Test
        void testGear_turnLeft() {
            // Arrange
            boolean turnLeft = true;
            boolean turnRight = false;
            Player currentPlayer = new Player("red", "Peter");
            currentPlayer.setHeading(Heading.NORTH);

            Gear gear = new Gear(turnLeft, turnRight);

            // Act
            gear.doAction(currentPlayer, null);

            // Assert
            Assertions.assertEquals(Heading.WEST, currentPlayer.getHeading());
        }

        @Test
    void testGear_turnRight() {
        // Arrange
        boolean turnLeft = false;
        boolean turnRight = true;
        Player currentPlayer = new Player("red", "Bob");
        currentPlayer.setHeading(Heading.SOUTH);

        Gear gear = new Gear(turnLeft, turnRight);

        // Act
        gear.doAction(currentPlayer, null);

        // Assert
        Assertions.assertEquals(Heading.WEST, currentPlayer.getHeading());
    }

    @Test
    void stopPlayerOneTurn() {
        Board board = gameController.board;
        board.setGameId(123);
        board.getSpace(0, 1).setTypeWall(Heading.SOUTH);
        // Space space = gameController.board.getSpace(0,1);
        //  space.setTypeWall();
        Player currentPlayer = board.getCurrentPlayer();
        System.out.println("Player x is " + currentPlayer.getSpace().x + " player y is " + currentPlayer.getSpace().y + " player heading is " + currentPlayer.getHeading());
        currentPlayer.setTestRegister(1, board);
        board.setPhase(Phase.ACTIVATION);

        gameController.executePrograms();
        System.out.println("Player x is " + currentPlayer.getSpace().x + " player y is " + currentPlayer.getSpace().y + " player heading is " + currentPlayer.getHeading());

        // Wall TestWall = new Wall(space);
        // TestWall.stopPlayerOneTurn(currentPlayer);
        Assertions.assertEquals(0, currentPlayer.getSpace().x);
        Assertions.assertEquals(0, currentPlayer.getSpace().y);

    }

    @Test
    void currentPlayerGets1CheckpointToken() {
        Board board = gameController.board;
        Player current = board.getCurrentPlayer();

        current.addToken();

        Assertions.assertEquals(1, current.getTokenCount(), "Token count should be 1.");

    }

    @Test
    void currentPlayerLandsOnFirstCheckpointAtRegistersEndAndGets1Token() {
        gameController.setTesting(true);
        Board board = gameController.board;
        board.setGameId(123);
        Player current = board.getCurrentPlayer();
        current.isLocal();
        gameController.addToPriorityList(current);

        current.setTestRegister(1, board);
        board.setPhase(Phase.ACTIVATION);
        gameController.executePrograms();

        Assertions.assertEquals(3, current.getSpace().y, "Player at location y = 3.");
        Assertions.assertEquals(1, current.getTokenCount(), "Player should have 1 token");
        gameController.setTesting(false);
    }

    @Test
    void currentPlayerLandsOnSecondCheckpointBeforeFirstAndGetsNoTokens() {
        Board board = gameController.board;
        Player player1 = board.getCurrentPlayer();

        player1.setTestRegister(5, board);
        board.setPhase(Phase.ACTIVATION);
        gameController.executePrograms();

        player1.setTestRegister(1, board);
        board.setPhase(Phase.ACTIVATION);
        gameController.executePrograms();

        player1.setTestRegister(4, board);
        board.setPhase(Phase.ACTIVATION);
        gameController.executePrograms();

        player1.setTestRegister(4, board);
        board.setPhase(Phase.ACTIVATION);
        gameController.executePrograms();
        System.out.println("Player location: " + player1.getSpace().x + ", " + player1.getSpace().y);
        //Assertions.assertEquals(true, player1.getSpace().hasACheckpoint());
        Assertions.assertEquals(5, player1.getSpace().x, "Player at location x = 5.");
        Assertions.assertEquals(0, player1.getSpace().y, "Player at location y = 0.");
        Assertions.assertEquals(0, player1.getTokenCount(), "Player should have no tokens");
    }


    @Test
    void winnerIsFound() {
        Board board = gameController.board;
        Player player1 = board.getCurrentPlayer();
        gameController.setTesting(true);
        player1.setTestRegister(1, board);
        board.setPhase(Phase.ACTIVATION);
        gameController.executePrograms();

        player1.setTestRegister(2, board);
        board.setPhase(Phase.ACTIVATION);
        gameController.executePrograms();

        player1.setTestRegister(3, board);
        board.setPhase(Phase.ACTIVATION);
        gameController.executePrograms();

        player1.setTestRegister(4, board);
        board.setPhase(Phase.ACTIVATION);
        gameController.executePrograms();
        System.out.println("Player location: " + player1.getSpace().x + ", " + player1.getSpace().y);

        Assertions.assertEquals(true, ((Checkpoint
                )player1.getSpace().getBoardElement()).isLastCheckpoint());
    }

}



