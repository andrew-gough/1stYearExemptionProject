package exemptionProject;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

import java.io.File;

import java.util.List;
import java.util.ArrayList;

/**
 * ImageViewer is the main class of the image viewer application. It builds and
 * displays the application GUI and initialises all other components.
 * 
 * To start the application, create an object of this class.
 * 
 * @author Michael Kölling and David J. Barnes.
 * @version 3.1
 */
public class ImageViewer 
{
	// static fields:
	private static final String VERSION = "Version 3.1.1";
	private static JFileChooser fileChooser = new JFileChooser(System.getProperty("user.dir"));

	// own fields:
	private CropFilter crop;
	private SlideshowMain slideshow;
	private ArrayList<OFImage> undoFunction ;
	private ArrayList<OFImage> redoFunction ;
	private File selectedFile;
	private Dimension screenSize;

	// fields:
	private JScrollPane scrollPane;
	private JFrame frame;
	private ImagePanel imagePanel;
	private JLabel filenameLabel;
	private JLabel statusLabel;
	private JButton smallerButton;
	private JButton largerButton;
	private OFImage currentImage;

	private List<Filter> filters;

	/**
	 * Create an ImageViewer and display its GUI on screen.
	 */
	public ImageViewer() 
	{
		screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		slideshow = new SlideshowMain();
		crop = new CropFilter("Crop", this);
		undoFunction = new ArrayList<OFImage>();
		redoFunction = new ArrayList<OFImage>();
		currentImage = null;
		filters = createFilters();
		makeFrame();
		setButtonsEnabled(false);
	}

	// ---- added functions for exemptionProj ----

	private void checkFrameSize(){
		if((screenSize.height<frame.getHeight())||(screenSize.width<frame.getWidth())){
			frame.setExtendedState(frame.getExtendedState() | JFrame.MAXIMIZED_BOTH);
		}
	}


	/**
	 * Refresh Function - Closes the current file and opens a new instance of the file
	 * Clearing all undos and redos
	 */

	private void refresh(){
		int selectedValue =JOptionPane.showConfirmDialog(null, "Do you wish to save before refreshing? \n(Refreshing clears all changes)", "Do you wish to save?", 
				JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);

		if (selectedValue == JOptionPane.CANCEL_OPTION){
			return;
		}

		if (selectedValue == JOptionPane.YES_OPTION){
			if (saveAs() == false){
				JOptionPane.showMessageDialog(null,"Save failed for some reason /n Refresh Failed");
				return;
			}
		}
		close();
		resetUndo();
		resetRedo();
		currentImage = ImageFileManager.loadImage(selectedFile);
		imagePanel.setImage(currentImage);
		setButtonsEnabled(true);
		showFilename(selectedFile.getPath());
		showStatus("Refreshed File!");
		frame.pack();
		checkFrameSize();
	}

	/**
	 * Add current image to Undo ArrayList - First requires an argument while the second
	 * uses the currentImage
	 */

	private void addUndo(OFImage Image){
		undoFunction.add(Image);
	}

	private void addUndo(){
		undoFunction.add(currentImage);
	}


	/**
	 *  Copies previous image (Top of Undo ArrayList) to currentImage
	 */
	private boolean doUndo(){
		if (undoFunction.size() > 0){
			addRedo(currentImage);
			currentImage = undoFunction.get(undoFunction.size() - 1);
			undoFunction.remove(undoFunction.size() - 1);
			imagePanel.setImage(currentImage);
			frame.repaint();
			setButtonsEnabled(true);
			showStatus("Undo Successful");
			frame.pack();
			checkFrameSize();
			return true;
		}else
		{
			showStatus("Undo Unsuccessful - No Changes to Make.");
			return false;
		}

	}
	/*
	 * Clears Undo ArrayList
	 */

	private void resetUndo(){
		undoFunction.clear();
	}

	/**
	 * Add current image to Redo ArrayList
	 */

	private void addRedo(OFImage Image){
		redoFunction.add(Image);
	}

	private boolean doRedo(){
		if (redoFunction.size() > 0){
			addUndo();
			currentImage = redoFunction.get(redoFunction.size() - 1);
			imagePanel.setImage(currentImage);
			redoFunction.remove(redoFunction.size() - 1);
			setButtonsEnabled(true);
			showStatus("Redo Successful");
			frame.pack();
			checkFrameSize();
			return true;
		}else
		{
			showStatus("Redo Unsuccessful - Nothing to Redo");
			return false;
		}
	}

	private void resetRedo(){
		redoFunction.clear();
	}


	// ---- implementation of menu functions ----

	/**
	 * Open function: open a file chooser to select a new image file,
	 * and then display the chosen image.
	 */
	private void openFile()
	{
		int returnVal = fileChooser.showOpenDialog(frame);

		if(returnVal != JFileChooser.APPROVE_OPTION) {
			return;  // cancelled
		}
		selectedFile = fileChooser.getSelectedFile();
		currentImage = ImageFileManager.loadImage(selectedFile);

		if(currentImage == null) {   // image file was not a valid image
			JOptionPane.showMessageDialog(frame,
					"The file was not in a recognized image file format.",
					"Image Load Error",
					JOptionPane.ERROR_MESSAGE);
			return;
		}
		imagePanel.setImage(currentImage);
		setButtonsEnabled(true);
		showFilename(selectedFile.getPath());
		showStatus("File loaded.");
		frame.pack();
		checkFrameSize();
	}

	public void openFile(File input)
	{

		selectedFile = input;
		currentImage = ImageFileManager.loadImage(selectedFile);

		if(currentImage == null) {   // image file was not a valid image
			JOptionPane.showMessageDialog(frame,
					"The file was not in a recognized image file format.",
					"Image Load Error",
					JOptionPane.ERROR_MESSAGE);
			return;
		}
		imagePanel.setImage(currentImage);
		setButtonsEnabled(true);
		showFilename(selectedFile.getPath());
		showStatus("File loaded.");
		frame.pack();
		checkFrameSize();
	}

	/**
	 * Close function: close the current image.
	 */
	private void close()
	{
		currentImage = null;
		imagePanel.clearImage();
		showFilename(null);
		setButtonsEnabled(false);
	}

	/**
	 * Save As function: save the current image to a file.
	 */
	private boolean saveAs()
	{
		if(currentImage != null) {
			int returnVal = fileChooser.showSaveDialog(frame);

			if(returnVal != JFileChooser.APPROVE_OPTION) {
				return false;  // cancelled
			}
			File selectedFile = fileChooser.getSelectedFile();
			ImageFileManager.saveImage(currentImage, selectedFile);

			showFilename(selectedFile.getPath());
			return true;
		}
		return false;
	}

	/**
	 * Quit function: quit the application.
	 */
	private void quit()
	{
		System.exit(0);
	}

	/**
	 * Apply a given filter to the current image.
	 * 
	 * @param filter   The filter object to be applied.
	 */
	private void applyFilter(Filter filter)
	{
		resetRedo();
		addUndo(OFImage.getCopy(currentImage));
		if(currentImage != null) {
			if ((filter.getName() == "Rotate Image")){
				currentImage = filter.applyReturn(currentImage);
			}
			filter.apply(currentImage);
			imagePanel.setImage(currentImage);
			frame.repaint();
			if(filter.getName() != "Crop"){
				showStatus("Applied: " + filter.getName());
			}
			else{
				showStatus("Crop Window Opened");
			}
		}
		else {
			showStatus("No image loaded.");
		}
	}

	/**
	 * 'About' function: show the 'about' box.
	 */
	private void showAbout()
	{
		JOptionPane.showMessageDialog(frame, 
				"ImageViewer\n" + VERSION + "\nUpdates By Andrew Gough",
				"About ImageViewer", 
				JOptionPane.INFORMATION_MESSAGE);
	}

	/**
	 * Make the current picture larger.
	 */
	private void makeLarger()
	{
		resetRedo();
		addUndo();
		if(currentImage != null) {
			// create new image with double size
			int width = currentImage.getWidth();
			int height = currentImage.getHeight();
			OFImage newImage = new OFImage(width * 2, height * 2);

			// copy pixel data into new image
			for(int y = 0; y < height; y++) {
				for(int x = 0; x < width; x++) {
					Color col = currentImage.getPixel(x, y);
					newImage.setPixel(x * 2, y * 2, col);
					newImage.setPixel(x * 2 + 1, y * 2, col);
					newImage.setPixel(x * 2, y * 2 + 1, col);
					newImage.setPixel(x * 2+1, y * 2 + 1, col);
				}
			}

			currentImage = newImage;
			imagePanel.setImage(currentImage);
			frame.pack();
			checkFrameSize();
		}
	}


	/**
	 * Make the current picture smaller.
	 */
	private void makeSmaller()
	{
		resetRedo();
		addUndo();
		if(currentImage != null) {
			// create new image with double size
			int width = currentImage.getWidth() / 2;
			int height = currentImage.getHeight() / 2;
			OFImage newImage = new OFImage(width, height);

			// copy pixel data into new image
			for(int y = 0; y < height; y++) {
				for(int x = 0; x < width; x++) {
					newImage.setPixel(x, y, currentImage.getPixel(x * 2, y * 2));
				}
			}

			currentImage = newImage;
			imagePanel.setImage(currentImage);
			frame.pack();
			checkFrameSize();
		}
	}

	// ---- support methods ----

	/**
	 * Show the file name of the current image in the fils display label.
	 * 'null' may be used as a parameter if no file is currently loaded.
	 * 
	 * @param filename  The file name to be displayed, or null for 'no file'.
	 */
	private void showFilename(String filename)
	{
		if(filename == null) {
			filenameLabel.setText("No file displayed.");
		}
		else {
			filenameLabel.setText("File: " + filename);
		}
	}


	/**
	 * Show a message in the status bar at the bottom of the screen.
	 * @param text The status message.
	 */
	private void showStatus(String text)
	{
		statusLabel.setText(text);
	}


	/**
	 * Enable or disable all toolbar buttons.
	 * 
	 * @param status  'true' to enable the buttons, 'false' to disable.
	 */
	private void setButtonsEnabled(boolean status)
	{
		smallerButton.setEnabled(status);
		largerButton.setEnabled(status);
	}


	/**
	 * Create a list with all the known filters.
	 * @return The list of filters.
	 */
	private List<Filter> createFilters()
	{
		List<Filter> filterList = new ArrayList<Filter>();
		filterList.add(new DarkerFilter("Darker"));
		filterList.add(new LighterFilter("Lighter"));
		filterList.add(new ThresholdFilter("Threshold"));
		filterList.add(new InvertFilter("Invert"));
		filterList.add(new SolarizeFilter("Solarize"));
		filterList.add(new SmoothFilter("Smooth"));
		filterList.add(new PixelizeFilter("Pixelize"));
		filterList.add(new MirrorFilter("Mirror"));
		filterList.add(new GrayScaleFilter("Grayscale"));
		filterList.add(new EdgeFilter("Edge Detection"));
		filterList.add(new FishEyeFilter("Fish Eye"));
		filterList.add(new RotationalFilter("Rotate Image", "clockwise"));
		filterList.add(crop);

		return filterList;
	}

	// ---- Swing stuff to build the frame and all its components and menus ----

	/**
	 * Create the Swing frame and its content.
	 */
	private void makeFrame()
	{
		frame = new JFrame("ImageViewer");
		JPanel contentPane = (JPanel)frame.getContentPane();
		contentPane.setBorder(new EmptyBorder(12, 12, 12, 12));

		makeMenuBar(frame);

		// Specify the layout manager with nice spacing
		contentPane.setLayout(new BorderLayout(6, 6));

		// Create the image pane in the center within a JScrollPane
		imagePanel = new ImagePanel();
		imagePanel.setBorder(new EtchedBorder());

		scrollPane = new JScrollPane(imagePanel);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		contentPane.add(scrollPane, BorderLayout.CENTER);

		// Create two labels at top and bottom for the file name and status messages
		filenameLabel = new JLabel();
		contentPane.add(filenameLabel, BorderLayout.NORTH);

		statusLabel = new JLabel(VERSION);
		contentPane.add(statusLabel, BorderLayout.SOUTH);

		// Create the toolbar with the buttons
		JPanel toolbar = new JPanel();
		toolbar.setLayout(new GridLayout(0, 1));

		smallerButton = new JButton("Smaller");
		smallerButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) { makeSmaller(); }
		});
		toolbar.add(smallerButton);

		largerButton = new JButton("Larger");
		largerButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) { makeLarger(); }
		});
		toolbar.add(largerButton);

		// Add toolbar into panel with flow layout for spacing
		JPanel flow = new JPanel();
		flow.add(toolbar);

		contentPane.add(flow, BorderLayout.WEST);

		// building is done - arrange the components      
		showFilename(null);
		setButtonsEnabled(false);
		frame.pack();

		// place the frame at the center of the screen and show
		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		frame.setLocation(d.width/2 - frame.getWidth()/2, d.height/2 - frame.getHeight()/2);
		frame.setVisible(true);
	}

	/**
	 * Create the main frame's menu bar.
	 * 
	 * @param frame   The frame that the menu bar should be added to.
	 */
	private void makeMenuBar(JFrame frame)
	{
		final int SHORTCUT_MASK =
				Toolkit.getDefaultToolkit().getMenuShortcutKeyMask();

		JMenuBar menubar = new JMenuBar();
		frame.setJMenuBar(menubar);

		JMenu menu;
		JMenuItem item;

		// create the File menu
		menu = new JMenu("File");
		menubar.add(menu);

		item = new JMenuItem("Open...");
		item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, SHORTCUT_MASK));
		item.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) { openFile(); }
		});
		menu.add(item);

		item = new JMenuItem("Close");
		item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W, SHORTCUT_MASK));
		item.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) { close(); }
		});
		menu.add(item);
		menu.addSeparator();

		item = new JMenuItem("Refresh");
		item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, SHORTCUT_MASK));
		item.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) { refresh(); }
		});
		menu.add(item);
		menu.addSeparator();

		item = new JMenuItem("Undo");
		item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, SHORTCUT_MASK));
		item.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) { doUndo(); }
		});
		menu.add(item);

		item = new JMenuItem("Redo");
		item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Y, SHORTCUT_MASK));
		item.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) { doRedo(); }
		});
		menu.add(item);
		menu.addSeparator();

		item = new JMenuItem("Save As...");
		item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, SHORTCUT_MASK));
		item.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) { saveAs(); }
		});
		menu.add(item);
		menu.addSeparator();

		item = new JMenuItem("Quit");
		item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, SHORTCUT_MASK));
		item.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) { quit(); }
		});
		menu.add(item);


		// create the Filter menu
		menu = new JMenu("Filter");
		menubar.add(menu);

		for(final Filter filter : filters) {
			item = new JMenuItem(filter.getName());
			item.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) { 
					applyFilter(filter);
				}
			});
			menu.add(item);
		}

		// create the added Functions Menu
		menu = new JMenu("Added");
		menubar.add(menu);

		item = new JMenuItem("Crop");
		item.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				applyFilter(crop);
			}
		});
		menu.add(item);


		item = new JMenuItem("Slideshow");
		item.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				slideshow.makeFrame();
			}
		});
		menu.add(item);

		// create the Help menu
		menu = new JMenu("Help");
		menubar.add(menu);

		item = new JMenuItem("About ImageViewer...");
		item.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) { showAbout(); }
		});
		menu.add(item);
	}


	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals("getCroppedImage")){
			currentImage = crop.getOutput();
			imagePanel.setImage(currentImage);
			frame.repaint();
			showStatus("Applied: Crop");
		}
		if(e.getActionCommand().equals("cropExited")){
			showStatus("Crop Exited Prematurely");
			undoFunction.remove(undoFunction.size() - 1);
		}
		if(e.getActionCommand().equals("getPreviewCroppedImage")){
			currentImage = crop.getOutput();
			imagePanel.setImage(currentImage);
			frame.repaint();
			showStatus("Crop Preview:");
		}
	}


}