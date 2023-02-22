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
public class CommandCardField extends Subject {

    final public Player player;

    private CommandCard card;

    private boolean visible;

    /**
     * this is the constructor for the CommandCardField class,
     * which uses a player as a parameter, and initializes a card,
     * which is set to null and is visible to all players.
     * @param player
     */
    public CommandCardField(Player player) {
        this.player = player;
        this. card = null;
        this.visible = true;
    }

    /**
     * This object is using a getter method,
     * to retrieve the values of the private Card field, which it returns
     * @return
     */
    public CommandCard getCard() {
        return card;
    }

    /**
     * The card field uses a setter method,
     * to check and notify if a card has been changed from the current value of the card.
     * @param card
     */
    public void setCard(CommandCard card) {
        if (card != this.card) {
            this.card = card;
            notifyChange();
        }
    }

    /**
     * this is a getter method which lets other methods access it indirectly,
     * using a boolean method to indicate if the field visible is true or false.
     * it returns the value of the visible field
     * @return
     */
    public boolean isVisible() {
        return visible;
    }

    /**
     * this object uses a setter method, where it uses an if statement, to check if the value of the visibility is
     * different from the current value, and updates it if yes. It checks by using the protected method notifyChange()
     * @param visible
     */
    public void setVisible(boolean visible) {
        if (visible != this.visible) {
            this.visible = visible;
            notifyChange();
        }
    }
}
