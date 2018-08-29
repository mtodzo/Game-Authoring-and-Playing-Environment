package xml.serialization;

import javafx.scene.image.ImageView;

/**
 * Wrapper class for ImageViews. Accesses all important data of ImageView and saves it to data that can be serialized by XStream. Classes that have ImageViews
 * (e.g. Sprite) also have ImageViewSerializers that are created at the same time.
 * 
 * @author Brendan Cheng
 *
 */

public interface ImageViewSerializer {

    /**
     * Serializes a ImageView (i.e. image filepath, x/y coordinates, etc.) to fields that can be serialized by XStream
     * 
     * @param image the ImageView to be serialized
     */
    public void fromImageView(ImageView image);
    
    /**
     * Uses local fields to create a new ImageView that exactly replicates original one that was saved
     * 
     * @return identical copy of original ImageView
     */
    public ImageView toImageView();
    
}
