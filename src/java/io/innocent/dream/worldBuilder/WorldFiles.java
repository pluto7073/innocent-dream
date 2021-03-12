package io.innocent.dream.worldBuilder;

import java.io.File;

import io.innocent.dream.InnocentDream;

public class WorldFiles {
	
	public static boolean testForWorldFolder(int slot) {
		File worldFolder = new File(InnocentDream.path + "\\worlds\\World" + slot);
		return worldFolder.exists();
	}

}
