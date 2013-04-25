package exemptionProject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class BrushManager {
	private File settingsFile;
	private ArrayList<File> brushFiles;
	private File brushFolderLocation;
	private ArrayList<String> brushNames;
	private ArrayList<String> brushTypes;
	private BufferedReader reader;

	public BrushManager(){
		brushFiles = new ArrayList<File>();
		brushNames = new ArrayList<String>();
		brushTypes = new ArrayList<String>();

		brushFolderLocation = new File("Brush Folder");
		loadBrushFiles();
	}
	
	public int getNumberOfBrushes(){
		return brushNames.size();
	}
	
	
	public String getBrushType(int index){
		return brushTypes.get(index);
	}
	
	public OFImage getBrush(int index){
		File brushFile = brushFiles.get(index);
		return ImageFileManager.loadImage(brushFile);
	}

	public int findIndex(String brushName){
			int index = -1;
			for(int i = 0;i<brushNames.size();i++){
				if(brushNames.get(i).equals(brushName)){
					index = i;
				}
			}
			if (index == -1){
				System.out.println("For some reason that String didn't have a corresponding index, whit?");
			}	
			return index;
	}
	
	public boolean refresh(){
		return loadBrushFiles();
	}
	
	public ArrayList<String> getBrushNames(){
		return brushNames;
	}
	
	private boolean loadBrushFiles(){
		brushFiles.clear();
		brushNames.clear();
		brushTypes.clear();
		try{
			boolean foundSettings = false;
			if(brushFolderLocation.exists()){
				
			}else{
				brushFolderLocation.mkdir();
			}
			File[] brushArrayFiles=brushFolderLocation.listFiles();
			for (int i = 0; i < brushArrayFiles.length; i++) 
			{
				if (brushArrayFiles[i].isFile()) 
				{
					if (brushArrayFiles[i].getName().equals("settings.txt"))
					{
						foundSettings = true;
						settingsFile = brushArrayFiles[i];
					}else{
						brushFiles.add(brushArrayFiles[i]);
					}
				}
			}
			if(!foundSettings){
				createSettingsFile(false);
			}

			loadSettingsFile();
			
			if(brushFiles.size()>brushNames.size()){
				appendDataToSettingsFile();
				loadSettingsFile();
			}
			return true;
		}
		catch(Exception e){
			return false;
		}
	}

	private void loadSettingsFile(){
		try {
			if (settingsFile == null){
				return;
			}else{
				FileReader fr = new FileReader(settingsFile);
				reader = new BufferedReader(fr);
				String line = reader.readLine();
				while(line != null) {            
					if(line.startsWith("#")||line.equals("#")){
						//System.out.println("This is a commented out line in the settings file!");
					}else{
						//This is true if the file actually contains a useful settings line
						if(line.startsWith("Name-")){
							//This activates if the line has the name tag
							brushNames.add(line.substring(5));

						}

						if(line.startsWith("Colour-")){
							//This activates if the line has the colour tag
							if(line.substring(7).equals("palete")||line.substring(7).equals("native")){
								brushTypes.add(line.substring(7));
							}else{
								System.out.println("The settings file seems to be corrupt - please delete it and the program will generate a new one");
							}

						}						
					}
					line = reader.readLine();
				}
				reader.close();
			}	
		}
		catch(FileNotFoundException e) {
			System.out.println("The file could not be found!");
		}
		catch(IOException e) {
			System.out.println("Something went wrong with reading or closing");
		}
		
	}


	private void appendDataToSettingsFile(){
		//		//This method goes through the settings file - and any brushes that are in the brush folder but 
		//		//Not in the settings file will be automaticly generated a default settings section for
		//		if(brushFiles.size()>brushNames.size()){
		//			//If we get to this point then we know that there is at least 1 brush with  missing  config data
		//			//So in this method we will then re-write the config file
		//			//At this point we should have the same size of brushNames/brushTypes
		//			//This line re-writes the settings file with the default info
		//			createSettingsFile(true);
		//			try {
		//				//writer is going to be used in appending mode to add data to the end of the settings file
		//				FileWriter writer = new FileWriter(settingsFile,true);
		//				BufferedWriter bw = new BufferedWriter(writer);
		//				int i = 0;
		//				while(i<brushNames.size()){
		//				bw.newLine();
		//				bw.write("Name-"+brushNames.get(i));
		//				bw.newLine();
		//				bw.write("Colour-"+brushTypes.get(i));
		//				bw.newLine();
		//				bw.write("#");
		//				i++;
		//				}
		//				//By now we're written all the file data which already existed
		//				//Now we want to add the extra brush data which didn't exist at first
		try{
			FileWriter writer = new FileWriter(settingsFile,true);
			BufferedWriter bw = new BufferedWriter(writer);
			ArrayList<String> brushFileNames = new ArrayList<String>();
			int i = 0;
			while(i < brushFiles.size()){
				brushFileNames.add(brushFiles.get(i).getName());
				i++;
			}
			brushFileNames.removeAll(brushNames);

			//So now the brushFileNames ArrayList only contains the brushes
			//which don't have a config section yet

			i = 0;
			while(i<brushFileNames.size()){
				bw.newLine();
				bw.write("Name-"+brushFileNames.get(i));
				bw.newLine();
				bw.write("Colour-palete");
				bw.newLine();
				bw.write("#");
				i++;
			}

			bw.close();

		}
		catch(IOException e) {
			System.out.println("Something went wrong with accessing the file!");
		}

	}


	private void createSettingsFile(boolean rewrite){
		File settingsFile = new File(brushFolderLocation,"settings.txt");
		try{
			if(!settingsFile.createNewFile()){
				throw new IOException("File already existed");
			}
		}catch(IOException e){
			System.out.println("The File already exists!");
			if (!rewrite){
				return;
			}
		}
		catch(Exception e){
			System.out.println("Didn't work for whatever reason!");
		}

		try {
			FileWriter writer = new FileWriter(settingsFile);
			BufferedWriter bw = new BufferedWriter(writer);
			bw.write("#");
			bw.newLine();
			bw.write("#This is the settings file for the paint brushes");
			bw.newLine();
			bw.write("#");
			bw.newLine();
			bw.write("#The following is an example settings file for a brush called brush.bmp");
			bw.newLine();
			bw.write("#With the colour to be selected by the colour palete");
			bw.newLine();
			bw.write("#");
			bw.newLine();
			bw.write("#Name-brush.bmp");
			bw.newLine();
			bw.write("#Colour-palete");
			bw.newLine();
			bw.write("#");
			bw.newLine();
			bw.write("#The following is an example settings file for a brush called brush2.jpeg");
			bw.newLine();
			bw.write("#With the colour to be selected by the native colour of the image");
			bw.newLine();
			bw.write("#");
			bw.newLine();
			bw.write("#Name-brush2.jpeg");
			bw.newLine();
			bw.write("#Colour-native");
			bw.newLine();
			bw.write("#");
			bw.close();
		}
		catch(IOException e) {
			System.out.println("Something went wrong with accessing the file!");
		}

	}




}
