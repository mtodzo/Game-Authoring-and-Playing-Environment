package engine.sprites;

import javafx.scene.image.ImageView;

public interface FrontEndSprite {

    /**
     * Needed to add the ImageView to the Panel
     * @return The towers ImageView
     */
    public ImageView getImageView();
    
    public String getName();
    
}
