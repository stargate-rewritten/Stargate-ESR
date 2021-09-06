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
import org.bukkit.configuration.file.FileConfiguration;

public class Refactorer {
	/*
	 * This name stays
	 * NOT USED CURRENTLY
	 */

	private int configVersion;
	private FileConfiguration defaultConfig;
	private FileConfiguration config;
	private Stargate stargate;

	public Refactorer(FileConfiguration config, Stargate stargate) {
		this.config = config;
		this.stargate = stargate;
		configVersion = config.getInt("configVersion");
		stargate.saveResource("config.yml", true);
		stargate.reloadConfig();
		defaultConfig = stargate.getConfig();
	}

	/**
	 * Used in debug, when you want to see the state of the currently stored
	 * configuration
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
	
	public void run() {
		switch (configVersion) {
		case 0:
		case 4:
			Modificator retCon0_10_8 = new RetCon0_10_8();
			recursiveConfigScroller("", "", retCon0_10_8);
		default:
			defaultConfig.set("configVersion", Stargate.CURRENTCONFIGVERSION);
		}
		try {
			defaultConfig.save(new File(stargate.getDataFolder(), "config.yml"));
		} catch (IOException e) {
			e.printStackTrace();
		};
	}
	
	private void recursiveConfigScroller(String oldPath, String newPath, Modificator mod) {
		ConfigurationSection section = config.getConfigurationSection(oldPath);
		Set<String> keys = section.getKeys(false);
		Map<String, Object> sectionMap = section.getValues(false);
		for (String key : keys) {
			Object[] oldSetting = new Object[] { key, sectionMap.get(key) };
			
			Object[] newSetting = mod.getNewSetting(oldSetting);
			String newSubPath = newPath + "." + newSetting[0];
			
			if (section.isConfigurationSection(key)) {
				recursiveConfigScroller(oldPath + "." + key, newSubPath, mod);
				continue;
			}
			defaultConfig.set(newSubPath, newSetting[1]);
		}
	}
	
	
	static private String ENDOFCOMMENT = "_endOfComment_";
	static private String STARTOFCOMMENT = "comment_";
	
	void addComments() {
		InputStream stream;
		File configFile = new File(stargate.getDataFolder(), "config.yml");
		try {
			stream = new FileInputStream(configFile);
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return;
		}
		InputStreamReader reader = new InputStreamReader(stream, StandardCharsets.UTF_8);
		BufferedReader bReader = new BufferedReader(reader);
		
		String finalText = "";
		try {
			String line;
			boolean isSkippingComment = false;
			while ((line = bReader.readLine()) != null) {
				if (isSkippingComment) {
					Stargate.debug("Refactorer.fancySave", "ignoring line " + line);
					if(line.contains(ENDOFCOMMENT))
						isSkippingComment = false;
					continue;
				}
				String possibleComment = line.strip();
				if (possibleComment.startsWith(STARTOFCOMMENT)) {
					int indent = countSpaces(line);
					String key = possibleComment.split(":")[0];
					String comment = defaultConfig.getString(key);
					String[] commentLines = comment.split("\n");
					line = "";
					Stargate.debug("Refactorer.fancySave",
							"Initial comment: |\n" + comment);
					/*
					 * Go through every line, except the last one, which is just going to be a
					 * ENDOFCOMMENT identifier
					 */
					for (int i = 0; i < commentLines.length - 1; i++) {
						line = line + "\n" + " ".repeat(indent) + "# " + commentLines[i];
					}
					isSkippingComment = true;
				}
				finalText = finalText + line + "\n";
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			OutputStream writerStream = new FileOutputStream(configFile);
			OutputStreamWriter writer= new OutputStreamWriter(writerStream, StandardCharsets.UTF_8);
			writer.write(finalText);
			writer.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
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
	
	private interface Modificator {
		/**
		 * 
		 * @param oldSetting a string where index 0 is the key, and index 1 is the value
		 * @return new setting where index 0 is the key, and index 1 is the value
		 */
		Object[] getNewSetting(Object[] oldSetting);
	}

	static private class RetCon0_10_8 implements Modificator {
		private static HashMap<String, String> RETCON0_10_8 = new HashMap<>();
		static {
		}

		@Override
		public Object[] getNewSetting(Object[] oldSetting) {
			Object[] newSetting = oldSetting.clone();
			String newKey = RETCON0_10_8.get(oldSetting[0]);
			if(newKey != null)
				newSetting[0] = newKey;
			newSetting[1] = oldSetting[1];
			return newSetting;
		}
	}
}
