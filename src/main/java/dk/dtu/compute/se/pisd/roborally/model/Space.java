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
 * The Space class represents a space on the game board in the Robo Rally game.
 * It extends the Subject class, allowing it to notify observers of any changes.
 *
 * @author
 * Melissa Woo, s224311@dtu.dk
 * Bayan Al Dowairi, s224329@dtu.dk
 * Amira Omar, s205821@dtu.dk
 * Besma Al Jwadi, s224325@dtu.dk
 * Negar Ostad, s224283@dtu.dk
 *
 */
public class Space extends Subject {

    //public final Board board;
    ElementType type;
    Heading heading = null;

    public final int x;
    public final int y;
    private Player player;
    private SpaceAction spaceAction;


    public Space(int x, int y) {
        //this.board = board;
        this.x = x;
        this.y = y;
        player = null;
        spaceAction = null;
        type = ElementType.Normal;
    }

    public void setTypeWall(Heading heading){
        type = ElementType.Wall;
        spaceAction = new Wall(heading);
    }

    public void setTypeCheckpoint(int index, boolean isLastCheckpoint) {
        type = ElementType.Checkpoint;
        spaceAction = new Checkpoint(index, isLastCheckpoint);

    }

    /**
     * Retrieves the index of the checkpoint associated with the space.
     *
     * @return The index of the checkpoint.
     * @throws IllegalStateException If no checkpoint is set in this space.
     */
    public int getCheckpointIndex() {
        if (type == ElementType.Checkpoint && spaceAction instanceof Checkpoint) {
            return ((Checkpoint) spaceAction).getIndex();
        }
        // Return a special value or throw an exception to indicate that no checkpoint is set
        throw new IllegalStateException("No checkpoint is set in this space.");
    }


    public void setTypeConveyor(int endX, int endY) {
        type = ElementType.ConveyorBelt;
        spaceAction = new ConveyorBelt(endX, endY);

    }


    /**
     * Fills the space with a conveyor belt and sets the corresponding space action.
     * Recursively fills the spaces along the conveyor belt's path.
     *
     * @param endX The x coordinate of the destination space.
     * @param endY The y coordinate of the destination space.
     * @param x The current x coordinate during recursion.
     * @param y The current y coordinate during recursion.
     * @param board The game board.
     * @param heading The heading of the conveyor belt.
     */
    public void fillConveyorBelt(int endX, int endY, int x, int y , Board board, Heading heading){
        type = ElementType.ConveyorBelt;
        spaceAction = new ConveyorBelt(endX, endY);

        if (x == endX) {
            if (endY == y) {
                this.heading = heading;
                return;
            }
            if(y < endY){
                y++;
                this.heading = Heading.SOUTH;
                board.getSpace(x, y).fillConveyorBelt(endX,  endY, x, y, board, this.heading);
            } else {
                y--;
                this.heading = Heading.NORTH;
                board.getSpace(x, y).fillConveyorBelt( endX,  endY, x, y, board, this.heading);
            }
        } else if (x > endX) {
            x--;
            this.heading = Heading.WEST;
            board.getSpace(x, y).fillConveyorBelt( endX,  endY, x, y , board, this.heading);
        } else {
            x++;
            this.heading = Heading.EAST;
            board.getSpace(x, y).fillConveyorBelt( endX,  endY, x, y , board, this.heading);
        }

    }

    public Heading getHeading() {
        return heading;
    }

    public void setHeading(Heading heading) {

        this.heading = heading;
    }

    public SpaceAction getSpaceAction() {
        return spaceAction;
    }

    public void setSpaceAction(SpaceAction spaceAction) {
        this.spaceAction = spaceAction;
    }

    /*public void setTypeGear(Heading heading){
        type = ElementType.Gear;
        spaceAction = new Gear(heading);
    }*/

    /**
     * Sets the type of the space to a gear and assigns the corresponding space action.
     *
     * @param turnLeft Indicates if the gear turns the player left.
     * @param turnRight Indicates if the gear turns the player right.
     */

        public void setTypeGear(boolean turnLeft, boolean turnRight) {
            type = ElementType.Gear;
            spaceAction = new Gear(turnLeft, turnRight);
        }



    public SpaceAction setTypePriorityAntenna(){
        type = ElementType.PriorityAntenna;
        spaceAction = new PriorityAntenna(7,7);
        return spaceAction;
        //board.setTypePriorityAntenna();
    }

    public ElementType getType() {
        return type;
    }

    public void setBoardElement(SpaceAction spaceAction) {
        this.spaceAction = spaceAction;
    }

    public SpaceAction getBoardElement() {
        return spaceAction;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player , Board board) {
        Player oldPlayer = this.player;
        if (player != oldPlayer) {
            this.player = player;
           /* if (oldPlayer != null) {
                // this should actually not happen
                oldPlayer.setSpace(null);
            }
            if (player != null) {
                player.setSpace(this);
            }*/
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