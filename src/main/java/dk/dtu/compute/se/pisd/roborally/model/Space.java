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
package dk.dtu.compute.se.pisd.roborally.model;

import dk.dtu.compute.se.pisd.designpatterns.observer.Subject;

/**
 * ...
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 *
 */
public class Space extends Subject {

    public final Board board;
    ElementType type;
    public final int x;
    public final int y;
    private Player player;
    private BoardElement boardElement;


    public Space(Board board, int x, int y) {
        this.board = board;
        this.x = x;
        this.y = y;
        player = null;
        boardElement = null;
        type = ElementType.Normal;
    }

    public void setTypeWall(){
        type = ElementType.Wall;
        //boardElement  = new Wall();
    }

    public void setTypeCheckpoint(int index, Board board, boolean isLastCheckpoint) {
        type = ElementType.Checkpoint;
        boardElement = new Checkpoint(index, board, isLastCheckpoint);

    }

    public void setTypeConveyor(Space end) {
        type = ElementType.ConveyorBelt;

        boardElement = new ConveyorBelt(end);
    }

    public void setTypeGear(Heading heading){
        type = ElementType.Gear;

        boardElement = new Gear(heading);
    }

    public void setTypePriorityAntenna(){
        type = ElementType.PriorityAntenna;

        boardElement = new PriorityAntenna();
    }

    public ElementType getType() {
        return type;
    }

    public void setBoardElement(BoardElement boardElement) {
        this.boardElement = boardElement;
    }

    public BoardElement getBoardElement() {
        return boardElement;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        Player oldPlayer = this.player;
        if (player != oldPlayer &&
                (player == null || board == player.board)) {
            this.player = player;
            if (oldPlayer != null) {
                // this should actually not happen
                oldPlayer.setSpace(null);
            }
            if (player != null) {
                player.setSpace(this);
            }
            notifyChange();
        }
    }

    void playerChanged() {
        // This is a minor hack; since some views that are registered with the space
        // also need to update when some player attributes change, the player can
        // notify the space of these changes by calling this method.
        notifyChange();

    }
    /*
    public boolean hasACheckpoint() {
        if(boardElement == null)
            return false;
        return boardElement.getType().equals("Checkpoint");
    }
    */
}
