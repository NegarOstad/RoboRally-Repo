package dk.dtu.compute.se.pisd.roborally.model;

public class Wall extends SpaceAction {

    private Space currentSpace;



   /* public void stopPlayerOneTurn(Player currentPlayer, Space currentSpace) {

        currentPlayer.setSpace(currentSpace);
    }*/

    @Override
    public void doAction(Player currentPlayer, Board board) {
        currentPlayer.setSpace(currentSpace, board);
    }
}
