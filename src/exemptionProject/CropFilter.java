package exemptionProject;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.event.*;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;

public class CropFilter implements ActionListener , ChangeListener {

	private int horiMin;
	private int horiMax;
	private int vertMin;
	private int vertMax;

	private JFrame frame;
	private ImagePanel imagePanel;
	private OFImage inputImage;
	private OFImage displayImage;
	private OFImage newImage;
	private JButton crop;
	private RangeSlider horizontalSlider;
	private RangeSlider verticalSlider;



	public CropFilter()
	{

	}




	public boolean applyReturn(OFImage image) {
		makeFrame(image);
		return true;
	}


	private void makeFrame(OFImage Image){
		frame = new JFrame("Cropping");
		JPanel contentPane = (JPanel)frame.getContentPane();
		contentPane.setBorder(new EmptyBorder(12, 12, 12, 12));
		contentPane.setLayout(new BorderLayout(6, 6));
		imagePanel = new ImagePanel(Image.getWidth(),Image.getHeight(),Image);
		imagePanel.setBorder(new EtchedBorder());
		contentPane.add(imagePanel, BorderLayout.CENTER);

		horizontalSlider = new RangeSlider(0,Image.getWidth());
		horizontalSlider.addChangeListener(this);
		verticalSlider = new RangeSlider(1,0,Image.getWidth());
		verticalSlider.addChangeListener(this);
		contentPane.add(horizontalSlider,BorderLayout.NORTH);
		contentPane.add(verticalSlider,BorderLayout.EAST);
		crop = new JButton("Crop");
		contentPane.add(crop,BorderLayout.SOUTH);
		crop.addActionListener(this);
		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		frame.setLocation(d.width/2 - frame.getWidth()/2, d.height/2 - frame.getHeight()/2);
		frame.setVisible(true);
		frame.pack();
	}

	public OFImage getOutput(){
		return newImage;
	}


	public void crop(){
		newImage = new OFImage((horiMax-horiMin),(vertMax-vertMin),OFImage.TYPE_INT_RGB);
		for(int i = 0;i<(horiMax-horiMin);i++){
			for(int ii = 0;ii<(vertMax-vertMin);ii++){
				newImage.setRGB(i, ii, inputImage.getRGB(horiMin+i, vertMin+ii));
			}
		}
	}

	public void updateWindow(){
		imagePanel.setImage(displayImage);
		frame.repaint();
	}

	public void drawLines(){

		displayImage = OFImage.getCopy(inputImage);

		System.out.println(displayImage.getHeight());
		System.out.println(displayImage.getWidth());



		for(int i=0;i<=displayImage.getHeight();i++ ){
			displayImage.setPixel(horiMin-1, i, Color.black);
			displayImage.setPixel(horiMin, i, Color.black);
			displayImage.setPixel(horiMin+1, i, Color.black);

			displayImage.setPixel(horiMax-1, i, Color.black);
			displayImage.setPixel(horiMax, i, Color.black);
			displayImage.setPixel(horiMax+1, i, Color.black);
		}

		for(int i=0;i<=displayImage.getWidth();i++ ){
			displayImage.setPixel(i, vertMin-1, Color.black);
			displayImage.setPixel(i, vertMin, Color.black);
			displayImage.setPixel(i, vertMin+1, Color.black);

			displayImage.setPixel(i, vertMax-1, Color.black);
			displayImage.setPixel(i, vertMax, Color.black);
			displayImage.setPixel(i, vertMax+1, Color.black);
		}

		updateWindow();

		//		System.out.println( "Horizontal: Minimum = " + horiMin + " Maximum = " + horiMax); 
		//		System.out.println( "Vertical: Minimum = " + vertMin + " Maximum = " + vertMax);

	}

	@Override
	public void actionPerformed(ActionEvent ae) {
		if (ae.getSource() == crop){
			crop();
		}


	}

	@Override
	public void stateChanged(ChangeEvent ae) {
		if(ae.getSource() == horizontalSlider){
			horiMin = horizontalSlider.getValue();
			horiMax = horizontalSlider.getUpperValue();
			drawLines();
		}
		if(ae.getSource() == verticalSlider){
			vertMin = verticalSlider.getValue();
			vertMax = verticalSlider.getUpperValue();
			drawLines();
		}

	}






}


