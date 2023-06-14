package dk.dtu.compute.se.pisd.roborally.model;

/***
 * This class extends the SpaceAction class and provides functionality for handling checkpoint actions.
 */
public class Checkpoint extends SpaceAction {
    private int index;

    //Board board;
    private boolean isLastCheckpoint;

    /**
     *
     * @param inputIndex  The index of the checkpoint
     * @param isLastCheckpoint  Indicates if this checkpoint is the last one
     */
    public Checkpoint(int inputIndex/*, Board board*/, boolean isLastCheckpoint) {
        super();
        index = inputIndex;
        this.isLastCheckpoint = isLastCheckpoint;
        //this.board = board;
    }


    public int getIndex() {
        return index;
    }

    public boolean isLastCheckpoint () {
        return isLastCheckpoint;
    }

    /**
     *
     * Performs the checkpoint action for the current player.
     * It adds a token to the player's count and checks if the player has reached the last checkpoint.
     * If the player reaches the last checkpoint, they are declared as the winner.

     * @param currentPlayer  The player performing the action
     * @param board
     */
    @Override
    public void doAction(Player currentPlayer, Board board) {
        System.out.println("We made it here");
        boolean validateAddToken = (board.getStep() == Player.NO_REGISTERS - 1 || currentPlayer.getRegisterStatus())
                && currentPlayer.getTokenCount() == ((Checkpoint)currentPlayer.getSpace().getBoardElement()).getIndex();
        if (validateAddToken) {
            currentPlayer.addToken();
            System.out.println("Adding a token on checkpoint #" +((Checkpoint)currentPlayer.getSpace().getBoardElement()).getIndex() +" , so token count is " + currentPlayer.getTokenCount());
            boolean isWinner = ((Checkpoint)currentPlayer.getSpace().getBoardElement()).isLastCheckpoint()
                    && currentPlayer.getTokenCount() == ((Checkpoint)currentPlayer.getSpace().getBoardElement()).getIndex()+1;
            if(isWinner) {
                System.out.println(currentPlayer.getName() + " is a winner!");
                board.setWinnerStatus(true);
            }
        }

    }
}
