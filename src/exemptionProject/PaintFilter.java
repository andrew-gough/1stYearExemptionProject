package exemptionProject;

import javax.swing.*;  
import java.awt.*;  
import java.awt.event.*;  

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;
import javax.swing.border.EtchedBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class PaintFilter extends Filter implements ChangeListener, MouseListener, MouseMotionListener, ActionListener{
	//Fields for the main body of the Filter
	private ImagePanel imagePanel;
	private JFrame frame;
	private Color currentColour;
	private JScrollPane scrollPane;
	private ImageViewer owner;
	private OFImage inputImage;
	private OFImage currentImage;
	private String currentBrush;
	private int brushSize;

	//Fields for the colourPalete
	private JFrame paleteFrame;
	private JButton blackButton;
	private JButton whiteButton;
	private JButton redButton;
	private JButton greenButton;
	private JButton blueButton;
	private JButton orangeButton;
	private JButton yellowButton;
	private JButton magentaButton;
	private JPanel colourIndicator;

	//Fields for the Specific Colour Selector

	private JFrame colourSelectorFrame;
	private JLabel redLabel;
	private JTextField redColour;
	private JLabel greenLabel;
	private JTextField greenColour;
	private JLabel blueLabel;
	private JTextField blueColour;
	private JButton colourSelectorButton;

	//Field for the Size Adjustor
	private JFrame sizeFrame;
	private JLabel sizeLabel;
	private JLabel sizeShowerLabel;
	private JSlider sizeSlider;

	//Fields for the ImageBrushes
	private BrushManager brushManager;


	//Default Constructor
	public PaintFilter(String Name, ImageViewer ownerInput){
		super(Name);
		owner = ownerInput;
		brushManager = new BrushManager();
		imagePanel = new ImagePanel();
		currentColour = Color.black;
		currentBrush = "Filler";
		brushSize = 10;
	}



	public boolean setImage(OFImage image){
		try{
			inputImage = image;
			currentImage = OFImage.getCopy(inputImage); 
			makeFrame();
			imagePanel.setImage(currentImage);
			return true;
		}
		catch(Exception e){
			return false;
		}

	}

	public OFImage getOutput(){
		return currentImage;
	}

	private void setCurrentRGB(Color rgb){
		currentColour = rgb;
		try{
			updateIndicator();
		}catch(Exception e){

		}
	}

	private void makeSizeAdjustor(){
		sizeFrame = new JFrame();

		JPanel contentPane = (JPanel)sizeFrame.getContentPane();
		contentPane.setLayout(new BorderLayout(6, 6));

		JPanel gridPane = new JPanel(new GridLayout(2,1));

		sizeLabel = new JLabel("Size Selector:");

		sizeShowerLabel = new JLabel(String.valueOf(brushSize));

		gridPane.add(sizeLabel);
		gridPane.add(sizeShowerLabel);

		contentPane.add(gridPane,BorderLayout.WEST);

		sizeSlider = new JSlider();
		sizeSlider.setMajorTickSpacing(50);
		sizeSlider.setMaximum(50);
		sizeSlider.setMinimum(1);
		sizeSlider.setValue(brushSize);
		sizeSlider.setOrientation(JSlider.VERTICAL);
		sizeSlider.addChangeListener(this);

		contentPane.add(sizeSlider,BorderLayout.EAST);

		sizeFrame.pack();
		sizeFrame.setVisible(true);



	}

	private void makeSpecificColourPalete(){
		colourSelectorFrame = new JFrame();
		JPanel contentPane = (JPanel)colourSelectorFrame.getContentPane();
		contentPane.setLayout(new BorderLayout(6, 6));

		JPanel gridPane = new JPanel(new GridLayout(3,2));


		redLabel = new JLabel("Red Value:");
		gridPane.add(redLabel);

		redColour = new JTextField();
		gridPane.add(redColour);


		greenLabel = new JLabel("Green Value:");
		gridPane.add(greenLabel);

		greenColour = new JTextField();
		gridPane.add(greenColour);

		blueLabel = new JLabel("Blue Value:");
		gridPane.add(blueLabel);

		blueColour = new JTextField();
		gridPane.add(blueColour);

		contentPane.add(gridPane,BorderLayout.WEST);

		colourSelectorButton = new JButton("Select Colours");
		colourSelectorButton.addActionListener(this);

		contentPane.add(colourSelectorButton,BorderLayout.EAST);

		colourSelectorFrame.pack();
		colourSelectorFrame.setVisible(true);


	}




	private void makeColourPalete(){
		paleteFrame = new JFrame();
		JPanel contentPane = (JPanel)paleteFrame.getContentPane();
		contentPane.setLayout(new GridLayout(5,2));

		blackButton = new JButton();
		blackButton.setBackground(Color.black);
		blackButton.setPreferredSize(new Dimension(30,20));
		blackButton.addActionListener(this);
		contentPane.add(blackButton);

		whiteButton = new JButton();
		whiteButton.setBackground(Color.white);
		whiteButton.addActionListener(this);
		contentPane.add(whiteButton);

		redButton = new JButton();
		redButton.setBackground(Color.red);
		redButton.addActionListener(this);
		contentPane.add(redButton);

		greenButton = new JButton();		
		greenButton.setBackground(Color.green);
		greenButton.addActionListener(this);
		contentPane.add(greenButton);

		blueButton = new JButton();
		blueButton.setBackground(Color.blue);
		blueButton.addActionListener(this);
		contentPane.add(blueButton);

		orangeButton = new JButton();
		orangeButton.setBackground(Color.orange);
		orangeButton.addActionListener(this);
		contentPane.add(orangeButton);

		yellowButton = new JButton();
		yellowButton.setBackground(Color.yellow);
		yellowButton.addActionListener(this);
		contentPane.add(yellowButton);

		magentaButton = new JButton();
		magentaButton.setBackground(Color.magenta);
		magentaButton.addActionListener(this);
		contentPane.add(magentaButton);

		JLabel colourLbl = new JLabel("Current Colour:");
		contentPane.add(colourLbl);

		colourIndicator = new JPanel();
		colourIndicator.setBackground(currentColour);
		contentPane.add(colourIndicator);


		//paleteFrame.setMinimumSize(new Dimension(100,400));
		paleteFrame.pack();
		paleteFrame.setMinimumSize(new Dimension(100,200));
		paleteFrame.setVisible(true);
		// black, white, red, green, blue, orange, yellow, purple


	}



	private void makeFrame(){
		//Sets up the Frame for the PaintFilter
		frame = new JFrame("Paint Filter");
		JPanel contentPane = (JPanel)frame.getContentPane();
		makeMenu(frame);

		//Sets up the image panel within the Painting Frame
		imagePanel = new ImagePanel();
		imagePanel.setBorder(new EtchedBorder());
		imagePanel.addMouseListener(this);
		imagePanel.addMouseMotionListener(this);	
		imagePanel.setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
		contentPane.add(imagePanel, BorderLayout.CENTER);

		frame.setVisible(true);
		frame.pack();
	}

	private void updateIndicator(){
		colourIndicator.setBackground(currentColour);
	}

	private void applyBrush(Point p,String from, MouseEvent me){


		if(SwingUtilities.isLeftMouseButton(me)){
			if(from == "Drag"){
				if(currentBrush == "ColourSelector"){
					applyPaintSelector(p);
				}
				if(currentBrush == "Paint"){
					applyPaintBrush(p);
				}
			}

			if(from == "Click"){
				if(currentBrush == "Filler"){
					try{
						applyFiller(p,currentImage.getPixel(p.x, p.y));
						refreshImage();
					}catch(java.lang.StackOverflowError e){
						//Recursion has gone too deep into the stack and the JVM can't handle it - respond by refreshing the currentImage
						refreshImage();
					}
				}

				if(currentBrush == "ColourSelector"){
					applyPaintSelector(p);
				}

				if(currentBrush == "Paint"){
					applyPaintBrush(p);
				}

			}
		}

		if(SwingUtilities.isRightMouseButton(me)){

			applyEraser(p);

		}

	}

	private boolean colourClose(Color color1, Color color2, int threshold){
		if((color2.getRed()<=color1.getRed()*(200-threshold)/100)&&(color2.getRed()>=color1.getRed()*threshold/100)){

		}else{
			return false;
		}
		if((color2.getGreen()<=color1.getGreen()*(200-threshold)/100)&&(color2.getGreen()>=color1.getGreen()*threshold/100)){

		}else{
			return false;
		}
		if((color2.getBlue()<=color1.getBlue()*(200-threshold)/100)&&(color2.getBlue()>=color1.getBlue()*threshold/100)){
			return true;	
		}
		return false;
	}


	//This method fills the space selected by calling itself recursively

	private void applyFiller(Point p,Color filledColour){
		//
		//		System.out.println(filledColour);
		//		System.out.println(currentImage.getPixel(p.x, p.y));
		//		System.out.println("Recursively at: x = "+ p.x + " y = "+p.y);
		//
		//		System.out.println(currentColour);
		//		System.out.println(currentImage.getPixel(p.x+1,p.y));
		//		System.out.println(filledColour);
		//
		currentImage.setPixel(p.x,p.y,currentColour);
		try{	
			if (colourClose(currentImage.getPixel(p.x+1, p.y),filledColour , 60)){
				applyFiller(new Point(p.x+1,p.y),filledColour);
			}
		}catch(java.lang.ArrayIndexOutOfBoundsException e){
		}
		try{ 
			if (colourClose(currentImage.getPixel(p.x-1, p.y),filledColour , 60)){
				applyFiller(new Point(p.x-1,p.y),filledColour);
			}
		}catch(java.lang.ArrayIndexOutOfBoundsException e){

		}
		try{
			if (colourClose(currentImage.getPixel(p.x, p.y+1),filledColour , 60)){
				applyFiller(new Point(p.x,p.y+1),filledColour);
			}
		}catch(java.lang.ArrayIndexOutOfBoundsException e){

		}
		try{
			if (colourClose(currentImage.getPixel(p.x, p.y-1),filledColour , 60)){
				applyFiller(new Point(p.x,p.y-1),filledColour);
			}
		}catch(java.lang.ArrayIndexOutOfBoundsException e){

		}
	}




	private void applyPaintBrush(Point p){
		try{
			//			currentImage.setPixel(p.x-1,p.y+1,currentColour);
			//			currentImage.setPixel(p.x, p.y+1, currentColour);
			//			currentImage.setPixel(p.x-1, p.y+1, currentColour);
			//
			//			currentImage.setPixel(p.x-1,p.y,currentColour);
			//			currentImage.setPixel(p.x, p.y, currentColour);
			//			currentImage.setPixel(p.x-1, p.y, currentColour);
			//
			//			currentImage.setPixel(p.x-1,p.y-1,currentColour);
			//			currentImage.setPixel(p.x, p.y-1, currentColour);
			//			currentImage.setPixel(p.x-1, p.y-1, currentColour);
			if (brushSize>1){
				Graphics2D g = currentImage.createGraphics();
				g.setColor(currentColour);
				g.fillOval(p.x, p.y, brushSize,brushSize);	
			}else{
				currentImage.setPixel(p.x,p.y,currentColour);
			}
			refreshImage();


		}catch(java.lang.ArrayIndexOutOfBoundsException ArrayEx){
			//This happens if the brush strays outside of the boundries, this is fine
		}
	}

	private void applyPaintSelector(Point p){
		currentColour = currentImage.getPixel(p.x, p.y);
		try{
			updateIndicator();
		}catch(NullPointerException e){
			//This is fine, just because the colourPalete hasn't been called yet!
		}
	}
	private void applyEraser(Point p){
		try{
			//			currentImage.setPixel(p.x+1,p.y+1,inputImage.getPixel(p.x+1, p.y+1));
			//			currentImage.setPixel(p.x, p.y+1, inputImage.getPixel(p.x, p.y+1));
			//			currentImage.setPixel(p.x-1, p.y+1, inputImage.getPixel(p.x-1, p.y+1));
			//
			//			currentImage.setPixel(p.x+1,p.y,inputImage.getPixel(p.x+1, p.y));
			//			currentImage.setPixel(p.x, p.y, inputImage.getPixel(p.x, p.y));
			//			currentImage.setPixel(p.x-1, p.y, inputImage.getPixel(p.x-1, p.y));
			//
			//			currentImage.setPixel(p.x+1,p.y-1,inputImage.getPixel(p.x+1, p.y-1));
			//			currentImage.setPixel(p.x, p.y-1, inputImage.getPixel(p.x, p.y-1));
			//			currentImage.setPixel(p.x-1, p.y-1, inputImage.getPixel(p.x-1, p.y-1));
			
			//http://stackoverflow.com/questions/9007977/draw-circle-using-pixels-applied-in-an-image-with-for-loop
			refreshImage();
		}catch(java.lang.ArrayIndexOutOfBoundsException ArrayEx){
			//This happens if the brush strays outside of the boundries, this is fine
		}
	}


	private void refreshImage(){
		imagePanel.repaint();
	}




	private void makeMenu(JFrame frame1){
		final int SHORTCUT_MASK =
				Toolkit.getDefaultToolkit().getMenuShortcutKeyMask();

		JMenuBar menubar = new JMenuBar();
		frame.setJMenuBar(menubar);

		JMenu menu;
		JMenuItem item;

		// create the File menu
		menu = new JMenu("Actions");
		menubar.add(menu);

		item = new JMenuItem("Return Image");
		item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, SHORTCUT_MASK));
		item.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				owner.actionPerformed(new java.awt.event.ActionEvent(this,0,"getPaintedImage"));
				try{
					paleteFrame.dispose();
				}catch(NullPointerException npe){
					//It's fine to be here, just means that the palete frame is already closed 
				}try{
					colourSelectorFrame.dispose();
				}catch(NullPointerException npe){
					//Again it's fine to be here, just means that the colour frame is already closed 
				}
				frame.dispose();
			}
		});
		menu.add(item);

		item = new JMenuItem("Quit");
		item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, SHORTCUT_MASK));
		item.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				owner.actionPerformed(new java.awt.event.ActionEvent(this,0,"paintExited"));
				try{
					paleteFrame.dispose();
				}catch(NullPointerException npe){
					//Again it's fine to be here, just means that the palete frame is already closed 
				}try{
					colourSelectorFrame.dispose();
				}catch(NullPointerException npe){
					//Again it's fine to be here, just means that the palete frame is already closed 
				}try{
					sizeFrame.dispose();
				}catch(NullPointerException npe){
					//Again it's fine to be here, just means that the palete frame is already closed 
				}
				frame.dispose();
			}
		});
		menu.add(item);

		item = new JMenuItem("Toggle Colour Palete");
		item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, SHORTCUT_MASK));
		item.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(paleteFrame == null){
					makeColourPalete();
				}else{
					paleteFrame.dispose();
					paleteFrame = null;
				}
			}
		});
		menu.add(item);

		item = new JMenuItem("Toggle Size Selector");
		item.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(sizeFrame == null){
					makeSizeAdjustor();
				}else{
					sizeFrame.dispose();
					sizeFrame = null;
				}
			}
		});
		menu.add(item);

		item = new JMenuItem("Select Specific Colour");
		item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, SHORTCUT_MASK));
		item.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(colourSelectorFrame == null){
					makeSpecificColourPalete();
				}else{
					colourSelectorFrame.dispose();
					colourSelectorFrame = null;
				}
			}
		});
		menu.add(item);

		menu = new JMenu("Brushes");
		menubar.add(menu);

		item = new JMenuItem("Paint Brush");
		item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, SHORTCUT_MASK));
		item.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				currentBrush = "Paint";
			}
		});
		menu.add(item);

		item = new JMenuItem("Filler");
		item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, SHORTCUT_MASK));
		item.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				currentBrush = "Filler";
			}
		});
		menu.add(item);

		item = new JMenuItem("Colour Sampler");
		item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, SHORTCUT_MASK));
		item.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				currentBrush = "ColourSelector";
			}
		});
		menu.add(item);

	}

	@Override
	//Method to set the Image for this filter
	public void apply(OFImage image) {
		setImage(image);
	}



	@Override
	//Shouldn't ever use this method
	public OFImage applyReturn(OFImage image) {
		return null;
	}



	@Override
	public void actionPerformed(ActionEvent ae) {
		if(ae.getSource() == blackButton){
			currentColour = Color.black;
			updateIndicator();
		}
		if(ae.getSource() == whiteButton){
			currentColour = Color.white;
			updateIndicator();
		}
		if(ae.getSource() == redButton){
			currentColour = Color.red;
			updateIndicator();
		}
		if(ae.getSource() == greenButton){
			currentColour = Color.green;
			updateIndicator();
		}
		if(ae.getSource() == blueButton){
			currentColour = Color.blue;
			updateIndicator();
		}
		if(ae.getSource() == orangeButton){
			currentColour = Color.orange;
			updateIndicator();
		}
		if(ae.getSource() == yellowButton){
			currentColour = Color.yellow;
			updateIndicator();
		}
		if(ae.getSource() == magentaButton){
			currentColour = Color.magenta;
			updateIndicator();
		}
		if(ae.getSource() == colourSelectorButton){
			try{
				setCurrentRGB(new Color(Integer.parseInt(redColour.getText()),Integer.parseInt(greenColour.getText()),Integer.parseInt(blueColour.getText())));
				colourSelectorFrame.dispose();
			}catch(Exception e){
				JOptionPane.showMessageDialog(frame,"Make sure the values are all between 0 and 255");
			}
		}
	}



	@Override
	public void mouseDragged(MouseEvent me) {
		applyBrush(me.getPoint(),"Drag",me);

	}


	@Override
	public void mouseClicked(MouseEvent me) {
		// TODO Auto-generated method stub
		applyBrush(me.getPoint(),"Click",me);
	}


	@Override
	public void mouseMoved(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}






	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}



	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}



	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}



	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}



	@Override
	public void stateChanged(ChangeEvent ce) {
		if(ce.getSource() == sizeSlider){
			brushSize = sizeSlider.getValue();
			System.out.println(brushSize);
			sizeShowerLabel.setText(String.valueOf(brushSize));
		}

	}
}
