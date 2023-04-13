package dk.dtu.compute.se.pisd.roborally.model;


    public class ConveyorBelt extends BoardElement{
        private Space endSpace;

        public ConveyorBelt(Board board, Space space, Space endSpace) {
        super(board, space);
        this.endSpace = endSpace;
    }
    public void movePlayer(Player currentPlayer){
            currentPlayer.setSpace(endSpace);
    }

        @Override
        public String getType() {
            return "Conveyor";
        }
    /*  private int moves;
    private Player player;
    private Space space;
    private Heading direction;


    public ConveyorBelt(Board board, Space space, int moves, Heading direction) {
        super(board, space);
        this.moves = moves;
        this.direction = direction;
    }

    public Heading getDirection() {
        return direction;
    }

    public void setDirection(Heading direction) {
        this.direction = direction;
    }

    public Space getSpace() {
        return space;
    }

    public void movePlayer(Player player) {
        if (player != null && getSpace() != null && getSpace().getPlayer() == player) {
            Space nextSpace = getSpace().board.getNeighbour(space, direction);
            if (nextSpace != null)
                nextSpace.setPlayer(player);
        }
    }

    @Override
    public String getSymbol() {
        switch (direction) {
            case NORTH:
                return "↑";
            case EAST:
                return "→";
            case SOUTH:
                return "↓";
            case WEST:
                return "←";
            default:
                return "░";
        }
    }*/
}

