package dk.dtu.compute.se.pisd.roborally.model;

/**
 *
 * @author
 * Melissa Woo, s224311@dtu.dk
 * Bayan Al Dowairi, s224329@dtu.dk
 * Amira Omar, s205821@dtu.dk
 * Besma Al Jwadi, s224325@dtu.dk
 * Negar Ostad, s224283@dtu.dk
 */

public abstract class SpaceAction {

    /**
     * Performs the action that's associated with the current space on the game board.
     *
     * @param currentPlayer The player who triggered the action.
     * @param board The game board on which the action is being performed.
     */

    public abstract void doAction(Player currentPlayer, Board board);

}