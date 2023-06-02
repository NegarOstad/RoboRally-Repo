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
package dk.dtu.compute.se.pisd.roborally.view;

import dk.dtu.compute.se.pisd.designpatterns.observer.Subject;
import dk.dtu.compute.se.pisd.roborally.model.ElementType;
import dk.dtu.compute.se.pisd.roborally.model.Heading;
import dk.dtu.compute.se.pisd.roborally.model.Player;
import dk.dtu.compute.se.pisd.roborally.model.Space;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.StrokeLineCap;
import org.jetbrains.annotations.NotNull;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * ...
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 *
 */
public class SpaceView extends StackPane implements ViewObserver {

    final public static int SPACE_HEIGHT = 45; // 60; // 75;
    final public static int SPACE_WIDTH = 45;  // 60; // 75;

    public final Space space;

    private List<ImageView> imageViews;

    //private ImageHolder imageHolder = new ImageHolder();

    public SpaceView(@NotNull Space space)  {
        this.space = space;
        this.imageViews = new ArrayList<>();

        // XXX the following styling should better be done with styles
        this.setPrefWidth(SPACE_WIDTH);
        this.setMinWidth(SPACE_WIDTH);
        this.setMaxWidth(SPACE_WIDTH);

        this.setPrefHeight(SPACE_HEIGHT);
        this.setMinHeight(SPACE_HEIGHT);

        String path = System.getProperty("user.dir");
        String fullPath = path + "\\src\\main\\java\\dk\\dtu\\compute\\se\\pisd\\roborally\\view\\Images\\";

         /*System.out.println(fullPath);

       if (space.getType() == ElementType.ConveyorBelt) {
            int i;
            for (i = space.x; i <= space.x; i++)

            addImage(fullPath + "conveyorbelt.png",0, 0, 0);
        } else if (space.getType() == ElementType.Checkpoint) {
            addImage(fullPath + "checkpoint.png",0, 0, 0);
        } else if (space.getType() == ElementType.Gear) {
            addImage(fullPath + "gear.png",0,0,0);
        } else if (space.getType() == ElementType.Wall) {
            addImage(fullPath + "wall.png",0,0,0);
        }*/


        if (space.getType() == ElementType.Gear) {
            this.setStyle("-fx-background-color: blue;");
        } else if (space.getType() == ElementType.ConveyorBelt) {
            this.setStyle("-fx-background-color: pink;");
        } else if (space.getType() == ElementType.Checkpoint) {
            this.setStyle("-fx-background-color: purple;");
        } else if (space.getType() == ElementType.Wall) {
            this.setStyle("-fx-background-color: green;");
        } else if ((space.x + space.y) % 2 == 0) {
            this.setStyle("-fx-background-color: white;");
        } else {
            this.setStyle("-fx-background-color: black;");
        }



        /*ImageView imageView = null;
        if (space.x == 0 && space.y == 0) {
            imageView = new ImageView(new Image("C:\\Users\\aljwa\\Desktop\\conveyorbelt.png"));
            this.getChildren().add(imageView);
        }*/


        // updatePlayer();

        // This space view should listen to changes of the space
       space.attach(this);
       update(space);


    }

    private void updatePlayer() {
        //this.getChildren().clear();

        for(int i = 0; i <this.getChildren().size(); i++){
            if(this.getChildren().get(i).getClass().equals(Polygon.class))
                this.getChildren().remove(i);
        }

        Player player = space.getPlayer();
        if (player != null) {
            Polygon arrow = new Polygon(0.0, 0.0,
                    10.0, 20.0,
                    20.0, 0.0 );
            try {
                arrow.setFill(Color.valueOf(player.getColor()));
            } catch (Exception e) {
                arrow.setFill(Color.MEDIUMPURPLE);
            }

            arrow.setRotate((90*player.getHeading().ordinal())%360);
            this.getChildren().add(arrow);
        }
    }

    public void addImage(String imagePath, double rotate, int x, int y) {
        Image image = new Image(imagePath);
        ImageView imageView = new ImageView(image);
        imageView.setX(x);
        imageView.setY(y);
        this.setRotate(0);
        this.imageViews.add(imageView);
        this.getChildren().add(imageView);

    }

    /*
   private void addImageToSpace(String imagePath, int x, int y, double rotation, double width, double height) {
       imageHolder.addImage(imagePath, x, y, rotation, width, height);
   }


     */
    @Override
    public void updateView(Subject subject) {
        if (subject == this.space) {
            updatePlayer();
        }
    }

}
