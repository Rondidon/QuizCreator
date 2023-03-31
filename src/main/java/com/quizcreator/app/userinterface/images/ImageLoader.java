package com.quizcreator.app.userinterface.images;

import javafx.scene.image.Image;
import org.jetbrains.annotations.Nullable;

public class ImageLoader {
    @Nullable
    public Image load(final String fileName) {
        try {
            final var resource = getClass().getResource(fileName);
            return resource == null ? null : new Image(resource.toExternalForm());
        } catch (final Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
