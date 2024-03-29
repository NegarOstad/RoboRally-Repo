package dk.dtu.compute.se.pisd.roborally.model;

import dk.dtu.compute.se.pisd.roborally.controller.GameController;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class PriorityAntennaTest {

    private final int TEST_WIDTH = 8;
    private final int TEST_HEIGHT = 8;

    private GameController gameController;

    @BeforeEach
    void setUp() {
        Board board = new Board(TEST_WIDTH, TEST_HEIGHT);
        gameController = new GameController(board);
        for (int i = 0; i < 2; i++) {
            Player player = new Player( null, "Player " + i);
            board.addPlayer(player);
            player.setSpace(board.getSpace(i, i), board);
            player.setHeading(Heading.values()[i % Heading.values().length]);
        }
        board.setCurrentPlayer(board.getPlayer(0));
        board.setTypePriorityAntenna(7, 7);
    }

    @AfterEach
    void tearDown() {
        gameController = null;

    }

    @Test
    void calculateDistance() {
        Board board = gameController.board;
        Player player1 = board.getPlayer(0);
        double a = Math.abs(board.getPriorityAntenna().x - player1.getSpace().x);
        double b = Math.abs(board.getPriorityAntenna().y - player1.getSpace().y);
        double distance = (int) Math.sqrt(Math.pow(a, 2) + Math.pow(b, 2));
        assertEquals((int) Math.sqrt(98), distance);


    }


    @Test
    void closestPlayerRegisters() {

        Board board = gameController.board;
        List<Player> listofcalcClosestPlayers = board.getPlayerList();
        List<Player> calcClosestPlayers = board.getPriorityAntenna().calcClosestPlayers(listofcalcClosestPlayers);
        assertEquals("Player 1", calcClosestPlayers.get(0).getName());

        listofcalcClosestPlayers.get(0).setTestRegister(2, board);
        listofcalcClosestPlayers.get(1).setTestRegister(1, board);
        System.out.println(calcClosestPlayers.get(0).getName());

        board.setPhase(Phase.ACTIVATION);
        //gameController.executeNextStep();
        listofcalcClosestPlayers = board.getPlayerList();
        calcClosestPlayers = board.getPriorityAntenna().calcClosestPlayers(listofcalcClosestPlayers);
        assertEquals("Player 0", calcClosestPlayers.get(1).getName());
        System.out.println(calcClosestPlayers.get(1).getName());

    }

}



