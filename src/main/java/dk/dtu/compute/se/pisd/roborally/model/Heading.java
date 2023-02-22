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

/**
 * ...
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 *
 */

/**
 * An enumerating class called Heading, which allows for controlling the directions of movement in the game:
 * south, west, north, east
 */
public enum Heading {

    SOUTH, WEST, NORTH, EAST;

    /**
     * The Heading next returns the next value in the enum class, where it uses the ordinal method to move to the right
     * of the list of constants (south, west, north, east)
     * ex. if you are on south, the object adds 1, thus moving you to west.
     * @return
     */

    public Heading next() {
        return values()[(this.ordinal() + 1) % values().length];
    }

    /**
     * This object uses the same method as the previous one with the ordinal method, only subtracting 1 from the value,
     * to move you a step to the left of the list of constants:
     * ex: if you stand on East, you'll be moved to North.
     * @return
     */

    public Heading prev() {
        return values()[(this.ordinal() + values().length - 1) % values().length];
    }
}
