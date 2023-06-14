package dk.dtu.compute.se.pisd.roborally.model;

/**
 * This enumerator represents the assigned state of each space on the board.
 * The space can either be normal, or have one of the elements and obstacles,
 * that help the game's progression.
 */
public enum ElementType {
    Checkpoint, ConveyorBelt, Gear, Normal, Wall, PriorityAntenna
}
