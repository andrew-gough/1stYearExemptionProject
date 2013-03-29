package exemptionProject;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;



public class SlideshowMain implements ActionListener{
	ArrayList<OFImage> slideshowList = new ArrayList<OFImage>();

	//declaring GUI Variables

	private ArrayList<Integer> list;
	private Timer timer;
	private int timerLoop;
	private Random randomGen;
	private boolean shuffleLooping;
	private boolean looping;

	private JButton back;
	private JButton add;
	private JButton remove;
	private JButton exit;
	private JButton shuffle;
	private JButton loop;
	private JButton forward;

	private JFrame frame;
	private ImagePanel imagePanel;
	private int currentIndex;
	private OFImage currentImage;



	public SlideshowMain()
	{
		shuffleLooping = false;
		looping = false;
		currentIndex = 0;
		currentImage = null;
	}




	private void addFiles(){
		JFileChooser chooser = new JFileChooser(System.getProperty("user.dir"));
		chooser.setMultiSelectionEnabled(true);
		int returnVal = chooser.showOpenDialog(frame);

		if(returnVal != JFileChooser.APPROVE_OPTION) {
			return;  // cancelled
		}
		File[] images = chooser.getSelectedFiles();

		int ii = 0;
		for(int i=0;i<images.length;i++){
			slideshowList.add(ImageFileManager.loadImage(images[i]));
			ii =+ ii;
		}

		selectImage(0);
	}

	private void selectImage(int number){
		currentIndex = number;
		currentImage = slideshowList.get(number);
		imagePanel.setImage(currentImage);
		frame.pack();


	}

	public void makeFrame(){
		frame = new JFrame("Slideshow");
		JPanel contentPane = (JPanel)frame.getContentPane();
		contentPane.setBorder(new EmptyBorder(12, 12, 12, 12));


		// Specify the layout manager with nice spacing
		contentPane.setLayout(new BorderLayout(6, 6));

		// Create the image pane in the center
		imagePanel = new ImagePanel();
		imagePanel.setBorder(new EtchedBorder());
		contentPane.add(imagePanel, BorderLayout.NORTH);


		back = new JButton("Previous Image");
		back.addActionListener(this);

		add = new JButton("Add Image(s)");
		add.addActionListener(this);

		remove = new JButton("Remove Image(s)");
		remove.addActionListener(this);

		exit = new JButton("Exit Slideshow");
		exit.addActionListener(this);

		shuffle = new JButton("Shuffle Slideshow");
		shuffle.addActionListener(this);

		loop = new JButton("Loop Slideshow");
		loop.addActionListener(this);

		forward = new JButton("Next Image");
		forward.addActionListener(this);

		JPanel p1 = new JPanel(new GridLayout(1,7));

		p1.add(back);
		p1.add(add);
		p1.add(remove);
		p1.add(exit);
		p1.add(shuffle);
		p1.add(loop);
		p1.add(forward);

		contentPane.add(p1, BorderLayout.SOUTH);

		// place the frame at the center of the screen and show
		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		frame.setLocation(d.width/2 - frame.getWidth()/2, d.height/2 - frame.getHeight()/2);
		frame.pack();
		frame.setVisible(true);

	}


	private void shuffleFunction(){
		list = new ArrayList<Integer>();
		for (int i = 0; i<slideshowList.size();i++){
			list.add(i);
		}
		Collections.shuffle(list);
		timer = new Timer(2000,this);
		timer.start();
	}

	private void loopFunction(){
		timer = new Timer(2000,this);
		timer.start();
	}

	private void loopInput(){
		currentImage = slideshowList.get(timerLoop-1);
		imagePanel.setImage(currentImage);
		frame.pack();
	}

	private void shuffleInput(){
		currentImage = slideshowList.get(list.get(timerLoop-1));
		imagePanel.setImage(currentImage);
		frame.pack();
	}

	@Override
	public void actionPerformed(ActionEvent ae) {
		if(ae.getSource() == back){
			if(currentIndex!=0){
				selectImage(currentIndex - 1);
			}else{
				selectImage(slideshowList.size() - 1);
			}
			frame.pack();
		}

		if(ae.getSource() == add){
			addFiles();
		}

		if(ae.getSource() == remove){

		}

		if(ae.getSource() == exit){
			frame.dispose();
		}

		if(ae.getSource() == shuffle){
			if (looping == true){
				timer.stop();
				looping = false;
			}
			if(slideshowList.size() != 0){
				if(shuffleLooping == false){
					shuffleLooping = true;	
					shuffleFunction();
				}else{
					timer.stop();
					shuffleLooping = false;
				}
			}else{
				System.out.println("Shuffle can't be done as there are no images in the slideshow");
			}
		}


		if(ae.getSource() == loop){
			if (shuffleLooping == true){
				timer.stop();
				shuffleLooping = false;
			}
			if(slideshowList.size() != 0){
				if(looping == false){
					looping = true;	
					loopFunction();
				}else{
					timer.stop();
					looping = false;
				}
			}else{
				System.out.println("Looping can't be done as there are no images in the slideshow");
			}
		}


		if(ae.getSource() == forward){
			if(currentIndex!=slideshowList.size()-1){
				selectImage(currentIndex + 1);
			}else{
				selectImage(0);
			}
			frame.pack();
		}
		if(ae.getSource() == timer){
			if(shuffleLooping == true){
				timerLoop++;
				if(timerLoop <= slideshowList.size()){
					shuffleInput();
				}else{
					timerLoop = 0;
					shuffleFunction();
				}
			}
			if(looping == true){
				timerLoop++;
				if(timerLoop <= slideshowList.size()){
					loopInput();
				}else{
					timerLoop = 0;
					loopFunction();
				}
			}
		}
	}
}


