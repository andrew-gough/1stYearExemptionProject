package exemptionProject;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;
import javax.swing.border.EtchedBorder;

public class PaintFilter extends Filter implements ActionListener{
	//Fields for the main body of the Filter
	private ImagePanel imagePanel;
	private JFrame frame;
	private Color currentColour;
	private JScrollPane scrollPane;
	private ImageViewer owner;
	private OFImage inputImage;
	private OFImage outputImage;

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


	//Default Constructor
	public PaintFilter(String Name, ImageViewer ownerInput){
		super(Name);
		this.owner = ownerInput;
		imagePanel = new ImagePanel();
		currentColour = Color.black;
	}



	public boolean setImage(OFImage image){
		try{
			makeFrame();
			imagePanel.setImage(image);
			makeColourPalete();
			return true;
		}
		catch(Exception e){
			return false;
		}
		
	}

	public void getOutputImage(){

	}




	private void makeColourPalete(){
		paleteFrame = new JFrame("Colour Palete");
		JPanel contentPane = (JPanel)paleteFrame.getContentPane();
		contentPane.setLayout(new GridLayout(4,2));
		
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


		contentPane.add(imagePanel, BorderLayout.CENTER);

		frame.setVisible(true);
		frame.pack();
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

		item = new JMenuItem("Quit");
		item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, SHORTCUT_MASK));
		item.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("Quit Worked!");
				frame.dispose();
			}
		});
		menu.add(item);

		item = new JMenuItem("Toggle Colour Palete");
		item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, SHORTCUT_MASK));
		item.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println(currentColour);
				if(paleteFrame == null){
					makeColourPalete();
				}else{
					paleteFrame.dispose();
					System.out.println(paleteFrame);
					paleteFrame = null;
				}
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
		}
		if(ae.getSource() == whiteButton){
			currentColour = Color.white;
		}
		if(ae.getSource() == redButton){
			currentColour = Color.red;
		}
		if(ae.getSource() == greenButton){
			currentColour = Color.green;
		}
		if(ae.getSource() == blueButton){
			currentColour = Color.blue;
		}
		if(ae.getSource() == orangeButton){
			currentColour = Color.orange;
		}
		if(ae.getSource() == yellowButton){
			currentColour = Color.yellow;
		}
		if(ae.getSource() == magentaButton){
			currentColour = Color.magenta;
		}
	}


}
