package net.TheDgtl.Stargate;

import org.bukkit.Material;

public class BloxPopulator {
	private Blox blox;
	private Material nextMat;
	private byte nextData;
	
	public BloxPopulator(Blox b, Material m) {
		blox = b;
		nextMat = m;
		nextData = 0;
	}
	
	public BloxPopulator(Blox b, Material m, byte d) {
		blox = b;
		nextMat = m;
		nextData = d;
	}
	
	public void setBlox(Blox b) {
		blox = b;
	}
	
	public void setMat(Material m) {
		nextMat = m;
	}
	
	public void setData(byte d) {
		nextData = d;
	}
	
	public Blox getBlox() {
		return blox;
	}
	
	public Material getMat() {
		return nextMat;
	}
	
	public byte getData() {
		return nextData;
	}
	
}
