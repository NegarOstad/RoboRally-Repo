package dk.dtu.compute.se.pisd.roborally.view;

import dk.dtu.compute.se.pisd.roborally.controller.AppController;
import javafx.scene.control.Button;

public class RoboRallyUpdate extends Button {
    AppController appController;

    public RoboRallyUpdate(AppController appController){
        this.appController = appController;
        this.setText("Update");
        this.setOnAction(e -> {
            try {
                appController.newGame();
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });

    }



}
