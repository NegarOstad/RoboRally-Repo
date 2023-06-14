package dk.dtu.compute.se.pisd.roborally.view;
import javafx.scene.image.*;
import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.List;
import java.util.*;
import java.util.stream.Collectors;
/*

public class ImageHolder extends Pane {
    private static final Map<String, Image> IMAGES = new HashMap<>();
    private List<ImageView> imageView;

    public ImageHolder() {
        addImage("C:\\Users\\aljwa\\Desktop\\Laser.png", 2, 2, 0, 1, 1);
        addImage("C:\\Users\\aljwa\\Desktop\\BlueConvey.png", 5, 5, 0, 1, 1);
    }

    public void addImage(String imagePath, int x, int y, double rotation, double width, double height) {
        Image image = IMAGES.computeIfAbsent(imagePath, Image::new);
        ImageView imageView = new ImageView(image);
        double cellWidth = getWidth() / getColumns();
        double cellHeight = getHeight() / getRows();
        imageView.setX(cellWidth * x);
        imageView.setY(cellHeight * y);
        imageView.setRotate(rotation);
        imageView.setFitWidth(cellWidth * width);
        imageView.setFitHeight(cellHeight * height);
        this.getChildren().add(imageView);
    }

    public List<ImageView> getImageViewList() {
        return imageView;
    }
}


 */