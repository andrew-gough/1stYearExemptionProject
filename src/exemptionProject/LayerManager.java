package exemptionProject;

import java.awt.Color;
import java.awt.Graphics2D;
import java.io.File;
import java.util.ArrayList;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

public class LayerManager {


	private ArrayList<ArrayList<OFImage>> undoFunctionLayer;
	private ArrayList<ArrayList<OFImage>> redoFunctionLayer;
	private ArrayList<OFImage> currentImageLayer;
	private ArrayList<String> layerNames;

	public LayerManager(){
		layerNames = new ArrayList<String>();
		undoFunctionLayer = new ArrayList<ArrayList<OFImage>>();
		redoFunctionLayer = new ArrayList<ArrayList<OFImage>>();
		currentImageLayer = new ArrayList<OFImage>();
	}

	public boolean addNewLayer(String name){

		if(layerExist(name)){
				System.out.println("A Layer already exists with this name, try again!");
		return false;
		}
		layerNames.add(name);
		undoFunctionLayer.add(null);
		redoFunctionLayer.add(null);
		currentImageLayer.add(null);
		return true;
	}

	public boolean importNewLayer(String name){
		for(int i = 0;i<layerNames.size();i++){
			if(layerNames.get(i).equals(name)){
				System.out.println("A Layer already exists with this name, try again!");
				return false;
			}
		}
		
		JFileChooser fileChooser = new JFileChooser(System.getProperty("user.dir"));

		int returnVal = fileChooser.showOpenDialog(null);

		if(returnVal != JFileChooser.APPROVE_OPTION) {
			System.out.println("File Chooser was exited prematurely");  // cancelled
		}
		File selectedFile = fileChooser.getSelectedFile();
		OFImage currentImage = ImageFileManager.loadImage(selectedFile);

		if(currentImage == null) {   // image file was not a valid image
			JOptionPane.showMessageDialog(null,
					"The file was not in a recognized image file format.",
					"Image Load Error",
					JOptionPane.ERROR_MESSAGE);
		}
		
		if(!name.isEmpty()){
			layerNames.add(name);
		}else{
			layerNames.add(selectedFile.getName());
		}
		
		undoFunctionLayer.add(new ArrayList<OFImage>());
		redoFunctionLayer.add(new ArrayList<OFImage>());
		currentImageLayer.add(currentImage);
		return true;
	}

	public void setFirstLayer(OFImage image){
		layerNames.add("Opened Image");
		undoFunctionLayer.add(null);
		redoFunctionLayer.add(null);
		currentImageLayer.add(image);
	}
	
	//Setting the layer attributes with the layer's name
	
	public void setUndoFunctionLayer(ArrayList<OFImage> element, String layer){
		System.out.println(layer);
		undoFunctionLayer.set(findIndex(layer), element);
	}
	
	public void setRedoFunctionLayer(ArrayList<OFImage> element, String layer){
		redoFunctionLayer.set(findIndex(layer), element);
	}
	
	public void setCurrentImageLayer(OFImage element, String layer){
		currentImageLayer.set(findIndex(layer), element);
	}
	
	//Setting the layer attributes with the layer's index
	
	public void setUndoFunctionLayerIndex(ArrayList<OFImage> element, int index){
		undoFunctionLayer.set(index, element);
	}
	
	public void setRedoFunctionLayerIndex(ArrayList<OFImage> element, int index){
		redoFunctionLayer.set(index, element);
	}
	
	public void setCurrentImageLayerIndex(OFImage element, int index){
		currentImageLayer.set(index, element);
	}
	
	public void renameLayerIndex(String newName, String oldName){
		if (newName == null){
			return;
		}
		layerNames.set(findIndex(oldName),newName);
	}

	private int findIndex(String name){
		int index = -1;
		for(int i = 0;i<layerNames.size();i++){
			if(layerNames.get(i).equals(name)){
				index = i;
			}
		}
		if (index == -1){
			System.out.println("For some reason that String didn't have a corresponding index, whit?");
		}	
		return index;
	}

	public ArrayList<OFImage> getUndoFunction(String name){
		try{
			return undoFunctionLayer.get((findIndex(name)));
		}catch(Exception e){
			System.out.println("Invalid String for name");
			return null;
		}
	}


	public ArrayList<OFImage> getRedoFunction(String name){
		try{
			return redoFunctionLayer.get((findIndex(name)));
		}catch(Exception e){
			System.out.println("Invalid String for name");
			return null;
		}
	}

	public OFImage getCurrentImage(String name){
		try{
			return currentImageLayer.get((findIndex(name)));
		}catch(Exception e){
			System.out.println("Invalid String for name");
			return null;
		}
	}

	
	public ArrayList<String> getLayerNames(){
		return layerNames;
	}

	

	//Returns true if layer exists - false if it does not
	
	private boolean layerExist(String layerName){
		for(int i = 0;i<layerNames.size();i++){
			if(layerNames.get(i).equals(layerName)){
				return true;
			}
		}
		return false;
	}
	
	public void clearAllLayers(){
		undoFunctionLayer.clear();
		redoFunctionLayer.clear();
		currentImageLayer.clear();
		layerNames.clear();
	}

}
