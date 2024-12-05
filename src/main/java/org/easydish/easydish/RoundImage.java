package org.easydish.easydish;

import javafx.scene.SnapshotParameters;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class RoundImage
{
    static void roundImage(ImageView imageView, int arc)
    {
        double size = Math.min(imageView.getFitWidth(), imageView.getFitHeight());
        Rectangle clip = new Rectangle(size, size);
        clip.setArcWidth(arc);
        clip.setArcHeight(arc);
        imageView.setClip(clip);

        SnapshotParameters parameters = new SnapshotParameters();
        parameters.setFill(Color.TRANSPARENT);
        WritableImage image = imageView.snapshot(parameters, null);

        imageView.setClip(null);
        imageView.setImage(image);

        imageView.setFitWidth(size);
        imageView.setFitHeight(size);
    }
}
