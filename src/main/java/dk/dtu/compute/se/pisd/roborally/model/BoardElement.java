package dk.dtu.compute.se.pisd.roborally.model;

import java.util.List;

public abstract class BoardElement {
    public Space spacelocation;

    public BoardElement(Space space) {
     spacelocation = space;
    }
    public abstract String getType();


}