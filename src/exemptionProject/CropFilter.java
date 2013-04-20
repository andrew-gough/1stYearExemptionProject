package exemptionProject;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


import javax.swing.event.*;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;

import rangeSlider.RangeSlider;

public class CropFilter extends Filter implements ActionListener , ChangeListener {

	private ImageViewer owner;

	private int horiMin;
	private int horiMax;
	private int vertMin;
	private int vertMax;

	private JFrame frame;
	private ImagePanel imagePanel;
	private OFImage inputImage;
	private OFImage displayImage;
	private OFImage newImage;
	private JButton exit;
	private JButton crop;
	private JButton preview;
	private RangeSlider horizontalSlider;
	private RangeSlider verticalSlider;



	public CropFilter(String name, ImageViewer mainApp)
	{
		super(name);
		owner = mainApp;
	}

	//Method which gets run;
	@Override
	public void apply(OFImage image) {
		inputImage = image;
		horiMin = 0;
		horiMax = 0;
		vertMin = 0;
		vertMax = 0;
		makeFrame(image);
	}




	public OFImage applyReturn(OFImage image) {
		return null;
	}


	private void makeFrame(OFImage Image){
		frame = new JFrame("Cropping");
		frame.setResizable(false);
		JPanel contentPane = (JPanel)frame.getContentPane();
		contentPane.setBorder(new EmptyBorder(12, 12, 12, 12));
		contentPane.setLayout(new BorderLayout(6, 6));
		imagePanel = new ImagePanel(Image.getWidth(),Image.getHeight(),Image);
		imagePanel.setBorder(new EtchedBorder());
		contentPane.add(imagePanel, BorderLayout.CENTER);
		horizontalSlider = new RangeSlider(0,Image.getWidth()-1);
		horizontalSlider.addChangeListener(this);
		verticalSlider = new RangeSlider(1,0,Image.getHeight()-1);
		verticalSlider.addChangeListener(this);
		contentPane.add(horizontalSlider,BorderLayout.NORTH);
		contentPane.add(verticalSlider,BorderLayout.EAST);
		crop = new JButton("Crop");
		crop.addActionListener(this);
		preview = new JButton("Preview");
		preview.addActionListener(this);
		exit = new JButton("Exit");
		exit.addActionListener(this);
		JPanel lowerButtons = new JPanel(new GridLayout(1,3));
		lowerButtons.add(crop);
		lowerButtons.add(preview);
		lowerButtons.add(exit);
		contentPane.add(lowerButtons,BorderLayout.SOUTH);


		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		frame.setLocation(d.width/2 - frame.getWidth()/2, d.height/2 - frame.getHeight()/2);
		frame.setVisible(true);
		frame.pack();
		
		if((Toolkit.getDefaultToolkit().getScreenSize().height<frame.getHeight())||(Toolkit.getDefaultToolkit().getScreenSize().width<frame.getWidth())){
			frame.setExtendedState(frame.getExtendedState() | JFrame.MAXIMIZED_BOTH);
		}
		
	}

	public OFImage getOutput(){
		return newImage;
	}





	public void crop(){
		//System.out.println("The width is: " + (horiMax - horiMin) + " The height is: " + (vertMax-vertMin));
		newImage = new OFImage((horiMax-horiMin),(vertMax-vertMin),OFImage.TYPE_INT_RGB);
		for(int ii = 0;ii<(vertMax-vertMin);ii++){
			for(int i = 0;i<(horiMax-horiMin);i++){
				newImage.setRGB(i, ii, inputImage.getRGB(horiMin+i,inputImage.getHeight()-(vertMin+ii)));
			}
		}
		// for some reason the image is the wrong way up - this method call corrects this
		horizontalMirror();
	}

	private void horizontalMirror(){
		int height = newImage.getHeight();
		int width = newImage.getWidth();
		for(int x = 0; x < width; x++) {
			for(int y = 0; y < height / 2; y++) {
				Color top = newImage.getPixel(x, y);
				newImage.setPixel(x, y, newImage.getPixel(x,height - 1 - y));
				newImage.setPixel(x, height - 1 - y, top);
			}
		}
	}




	private void updateWindow(){
		imagePanel.setImage(displayImage);
		frame.repaint();
	}

	private void drawLines(){

		displayImage = OFImage.getCopy(inputImage);


		for(int i=0;i<displayImage.getHeight();i++ ){
			displayImage.setPixel(horiMin, i, Color.black);

			displayImage.setPixel(horiMax, i, Color.black);

		}

		for(int i=0;i<displayImage.getWidth();i++ ){
			displayImage.setPixel(i, displayImage.getHeight() - vertMin - 1, Color.black);

			displayImage.setPixel(i, displayImage.getHeight() - vertMax - 1, Color.black);
		}

		updateWindow();

	}



	@Override
	public void actionPerformed(ActionEvent ae) {
		if (ae.getSource() == crop){
			crop();
			frame.dispose();
			this.owner.actionPerformed(new java.awt.event.ActionEvent(this,0,"getCroppedImage"));
		}
		if (ae.getSource() == exit){
			this.owner.actionPerformed(new java.awt.event.ActionEvent(this,0,"cropExited"));
			frame.dispose();
		}
		if (ae.getSource() == preview){
			crop();
			this.owner.actionPerformed(new java.awt.event.ActionEvent(this,0,"getPreviewCroppedImage"));
		}


	}

	@Override
	public void stateChanged(ChangeEvent ae) {
		if(ae.getSource() == horizontalSlider){
			horiMin = horizontalSlider.getLowerValue();
			horiMax = horizontalSlider.getUpperValue();
			drawLines();
		}
		if(ae.getSource() == verticalSlider){
			vertMin = verticalSlider.getLowerValue();
			vertMax = verticalSlider.getUpperValue();
			drawLines();
		}

	}










}
