package exemptionProject;
import javax.swing.*;
public class RotationalFilter extends Filter {


	/**
	 * Constructor for objects of class RotationalFilter.
	 * @param name The name of the filter.
	 */
	public RotationalFilter(String name)
	{
		super(name);
	}

	@Override
	public void apply(OFImage image){	
	}

	public OFImage applyReturn(OFImage img) {
		
		String[] options = {"Clockwise","AntiClockwise","Cancel"};
		int returnVal = JOptionPane.showOptionDialog(null,
                "Clockwise or AntiClockwise?",
                "Please Select a type",
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                null);
		
		if ((returnVal != 0) &&(returnVal != 1)){
			return img;
		}
		
		int height = img.getHeight();
		int width = img.getWidth();
		OFImage rotated = new OFImage(height, width,OFImage.TYPE_INT_RGB);

		// Clockwise 90 degrees
		if(returnVal == 0){
			
			
	
			for(int i=0; i<width; i++)
				for(int j=0; j<height; j++)
					rotated.setRGB(height-1-j, i, img.getRGB(i, j));	
		

		}
		
		
		//AntiClockwise 90Degrees
		if (returnVal == 1){

			for(int i=0; i<width; i++)
				for(int j=0; j<height; j++)
					rotated.setRGB(j, width-1-i, img.getRGB(i, j));	
		}

		
		return rotated;
	
	}
}




