package dk.dtu.compute.se.pisd.roborally.model;

import dk.dtu.compute.se.pisd.roborally.controller.GameController;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PriorityAntennaTest {

    private final int TEST_WIDTH = 8;
    private final int TEST_HEIGHT = 8;

    private GameController gameController;

    @BeforeEach
    void setUp() {
        Board board = new Board(TEST_WIDTH, TEST_HEIGHT);
        gameController = new GameController(board);
        for (int i = 0; i < 6; i++) {
            Player player = new Player(board, null,"Player " + i);
            board.addPlayer(player);
            player.setSpace(board.getSpace(i, i));
            player.setHeading(Heading.values()[i % Heading.values().length]);
        }
        board.setCurrentPlayer(board.getPlayer(0));
    }

    @AfterEach
    void tearDown() {
        gameController = null;

    }

    @Test
    public void testSetPriorityAntenna(){
        Board board = gameController.board;
        PriorityAntenna priorityAntenna = new PriorityAntenna(7,7);
        //assertEquals(board.getSpace(2,5), );
    }
    @Test
    void closestPlayer() {
        Board board = gameController.board;
        Player player1 = board.getPlayer(0);
        Player player2 = board.getPlayer(1);
        List<Player> players = new ArrayList<>();
        players.add(player1);
        players.add(player2);

        PriorityAntenna priorityAntenna = new PriorityAntenna(7,7);
        Player closestPlayer = priorityAntenna.(players);
        assertEquals(player1, closestPlayer );

            }


    }
