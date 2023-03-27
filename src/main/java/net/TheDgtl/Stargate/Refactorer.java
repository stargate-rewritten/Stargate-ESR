package net.TheDgtl.Stargate;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;

public class Refactorer {
	/*
	 * This name stays
	 * NOT USED CURRENTLY
	 */

	private int configVersion;
	private Map<String,Object> configHashMap;
	private Stargate stargate;

	public Refactorer(FileConfiguration config, Stargate stargate) {
		this.configHashMap = config.getValues(true);
		this.stargate = stargate;
	}

	/**
	 * Used in debug, when you want to see the state of the currently stored
	 * configuration
	 * 
	 * Displays the current text in the config file
	 */
	public void dispConfig() {
		InputStream stream;
		try {
			stream = new FileInputStream(new File(stargate.getDataFolder(), "config.yml"));
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
			return;
		}
		InputStreamReader reader = new InputStreamReader(stream, StandardCharsets.UTF_8);
		BufferedReader bReader = new BufferedReader(reader);
		String line;
		try {
			line = bReader.readLine();
			while(line != null) {
				Stargate.debug("Refactorer", line);
				line = bReader.readLine();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				bReader.close();
				reader.close();
				stream.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public Map<String,Object> getNewConfigMap() {
		
                Object nonCastedConfigVersion = configHashMap.get("configVersion");
		configVersion = (nonCastedConfigVersion == null) ? -1 : (int) nonCastedConfigVersion;
		
		/*
		 * This section is here for future refactoring (includes the Modificator stuff)
		 */
		switch (configVersion) {
		case 0:
		case 4:
		default:
			configHashMap.put("configVersion", Stargate.CURRENTCONFIGVERSION);
		}
		
		return configHashMap;
	}
	
	
	static private String ENDOFCOMMENT = "_endOfComment_";
	static private String STARTOFCOMMENT = "comment_";
	
	public FileConfiguration writeNewConfig(Map<String,Object> configMap) {
		
		stargate.saveResource("config.yml", true);
		stargate.reloadConfig();
		FileConfiguration config = stargate.getConfig();
		
		ConfigParser parser = new ConfigParser();
		parser.insertNewData(config, configMap);
		try {
			String text = parser.concatText(config);
			parser.writeText(text);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			parser.close();
		}
		return config;
	}
	
	/**
	 * Count the spaces at the start of a line. there's probably an already existing
	 * method for this, but meh
	 * 
	 * @param line
	 * @return
	 */
	private int countSpaces(String line) {
		int spaceAmount = 0;
		for(char aChar : line.toCharArray()) {
			if(aChar == ' ')
				spaceAmount++;
			else
				break;
		}
		return spaceAmount;
	}
	
	/**
	 * Used to generate a new config file from all the known data
	 */
	private class ConfigParser{
		InputStream iStream;
		InputStreamReader reader;
		BufferedReader bReader;
		OutputStream writerStream;
		OutputStreamWriter writer;
		private File configFile;
		
		ConfigParser(){
			this.configFile = new File(stargate.getDataFolder(), "config.yml");
		}
		
		/**
		 * Modifies the specified configuration, also modifies config file
		 * @param config
		 * @param configMap
		 */
		public void insertNewData(FileConfiguration config, Map<String, Object> configMap) {
			for(String key : config.getKeys(true)) {
				Object value = configMap.get(key);
				if(value != null) {
					config.set(key, value);
				}
			}
			try {
				config.save(new File(stargate.getDataFolder(), "config.yml"));
			} catch (IOException e) {
				e.printStackTrace();
			};
		}
	
		public String concatText(FileConfiguration config) throws IOException {
			String finalText = "";
			BufferedReader reader = getReader();
			String line;
			boolean isSkippingComment = false;
			while ((line = bReader.readLine()) != null) {
				if (isSkippingComment) {
					Stargate.debug("Refactorer.fancySave", "ignoring line " + line);
					if (line.contains(ENDOFCOMMENT))
						isSkippingComment = false;
					continue;
				}
				String possibleComment = line.strip();
				if (possibleComment.startsWith(STARTOFCOMMENT)) {
					String key = possibleComment.split(":")[0];
					int indent = countSpaces(line);
					String comment = readComment(key, indent, config);
					finalText = finalText + comment + "\n";
					isSkippingComment = true;
					continue;
				}
				finalText = finalText + line + "\n";
			}
			return finalText;
		}
		
		public void writeText(String text) throws IOException {
			OutputStreamWriter writer = getWriter();
			writer.write(text);
		}
		
		/**
		 * Close all resources used by this class
		 */
		public void close() {
			try {
				bReader.close();
				reader.close();
				iStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (NullPointerException e) {}

			try {
				writer.close();
				writerStream.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NullPointerException e) {}
		}

		private String readComment(String key, int indent, FileConfiguration config) {
			String comment = config.getString(key);
			String[] commentLines = comment.split("\n");
			Stargate.debug("Refactorer.fancySave", "Initial comment: |\n" + comment);
			/*
			 * Go through every line, except the last one, which is just going to be a
			 * ENDOFCOMMENT identifier
			 */
			String clearedComment = "";
			for (int i = 0; i < commentLines.length - 1; i++) {
				clearedComment = clearedComment + "\n" + " ".repeat(indent) + "# " + commentLines[i];
			}
			return clearedComment;
		}

		private BufferedReader getReader() throws FileNotFoundException {
			iStream = new FileInputStream(configFile);
			reader = new InputStreamReader(iStream, StandardCharsets.UTF_8);
			return bReader = new BufferedReader(reader);
		}
		
		private OutputStreamWriter getWriter() throws FileNotFoundException {
			writerStream = new FileOutputStream(configFile);
			return writer = new OutputStreamWriter(writerStream, StandardCharsets.UTF_8);
		}
	}

	/**
	 * A modificator is meant to be able to completely change the location of every key and its relevant setting.
	 * It can also change the setting itself if necessary.
	 */
	private abstract class Modificator {
		/**
		 * 
		 * @param oldSetting a string where index 0 is the key, and index 1 is the value
		 * @return new setting where index 0 is the key, and index 1 is the value
		 */
		protected abstract Setting getNewSetting(Setting oldSetting);
		
		public HashMap<String,Object> refactor(Map<String,Object> config) {
			HashMap<String,Object> newConfig = new HashMap<>();
			for(String branch : config.keySet()){
				Setting oldSetting = new Setting(branch, config.get(branch));
				Setting newSetting = getNewSetting(oldSetting);
				newConfig.put(newSetting.key, newSetting.value);
			}
			return newConfig;
		}
		
		
		/**
		 * A convenience class
		 */
		protected class Setting{
			private final String key;
			private final Object value;
			
			Setting(String key,Object value){
				this.key = key; this.value = value;
			}
		}
	}
}
