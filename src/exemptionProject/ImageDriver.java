package exemptionProject;

import java.io.File;

public class ImageDriver {
	public static void main(String[] args) {
		ImageViewer iv = new ImageViewer();
		iv.openFile(new File ("GettingAllThe.jpg"));
	}
}