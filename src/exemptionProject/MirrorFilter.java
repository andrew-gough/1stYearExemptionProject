package exemptionProject;

import java.awt.Color;

/**
 * An image filter to mirror (flip) the image horizontally.
 * 
 * @author Michael Kölling and David J. Barnes.
 * @version 1.0
 */
public class MirrorFilter extends Filter
{
	/**
	 * Constructor for objects of class MirrorFilter.
     * @param name The name of the filter.
	 */
	public MirrorFilter(String name)
    {
        super(name);
	}

    /**
     * Apply this filter to an image.
     * 
     * @param  image  The image to be changed by this filter.
     */
	public void apply(OFImage image)
	{
		
		//This code Mirrors Vertically
//		int height = image.getHeight();
//		int width = image.getWidth();
//		if (height > width){
//			for(int x = 0; x < width; x++) {
//				for(int y = 0; y < height / 2; y++) {
//					Color top = image.getPixel(x, y);
//					image.setPixel(x, y, image.getPixel(x,height - 1 - y));
//					image.setPixel(x, height - 1 - y, top);
//				}
//			}
//		}
		
		//This code Mirrors Horizontally
		int height = image.getHeight();
		int width = image.getWidth();
		if (height > width){
			for(int y = 0; y < height; y++) {
				for(int x = 0; x < width / 2; x++) {
					Color left = image.getPixel(x, y);
					image.setPixel(x, y, image.getPixel(width - 1 - x, y));
					image.setPixel(width - 1 - x, y, left);
				}
			}
		}
		
	}
	
    public OFImage applyReturn(OFImage image){
    return image;
    }
}
