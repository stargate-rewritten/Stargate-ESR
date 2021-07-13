package net.TheDgtl.Stargate;

import java.util.HashMap;
import java.util.Set;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

public class Refactorer {
	/*
	 * This name stays
	 */

	private int configVersion;
	private FileConfiguration defaultConfig;
	private FileConfiguration config;
	private Stargate stargate;

	public Refactorer(FileConfiguration config, Stargate stargate) {
		this.config = config;
		configVersion = config.getInt("configVersion");
		stargate.saveDefaultConfig();
		defaultConfig = stargate.getConfig();
		this.stargate = stargate;
	}

	public void run() {
		switch (configVersion) {
		case 0:
		case 4:
			Modificator retCon0_10_8 = new RetCon0_10_8();
			recursiveConfigScroller("", "", retCon0_10_8);
		}
		stargate.saveConfig();
	}

	private void recursiveConfigScroller(String oldPath, String newPath, Modificator mod) {
		ConfigurationSection section = config.getConfigurationSection(oldPath);
		Set<String> keys = section.getKeys(false);
		for (String key : keys) {
			String[] oldSetting = new String[] { key, section.getString(key) };
			String[] newSetting = mod.getNewSetting(oldSetting);
			String newSubPath = newPath + "." + newSetting[0];
			
			if (section.isConfigurationSection(key)) {
				recursiveConfigScroller(oldPath + "." + key, newSubPath, mod);
				continue;
			}
			defaultConfig.set(newSubPath, newSetting[1]);
		}
	}

	private interface Modificator {
		/**
		 * 
		 * @param oldSetting a string where index 0 is the key, and index 1 is the value
		 * @return new setting where index 0 is the key, and index 1 is the value
		 */
		String[] getNewSetting(String[] oldSetting);
	}

	static private class RetCon0_10_8 implements Modificator {
		private static HashMap<String, String> RETCON0_10_8 = new HashMap<>();
		static {
			RETCON0_10_8.put("test", "yes");
		}

		@Override
		public String[] getNewSetting(String[] oldSetting) {
			String[] newSetting = new String[2];
			newSetting[0] = RETCON0_10_8.get(oldSetting[0]);
			newSetting[1] = oldSetting[1];
			return newSetting;
		}
	}
}
