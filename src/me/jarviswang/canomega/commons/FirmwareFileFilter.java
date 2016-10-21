package me.jarviswang.canomega.commons;

import java.io.File;

import javax.swing.filechooser.FileFilter;

public class FirmwareFileFilter extends FileFilter {

	@Override
	public boolean accept(File f) {
		if (f.isDirectory()) {
			return true;
		}
		return (f.getName().toLowerCase().endsWith(".bin"));
	}

	@Override
	public String getDescription() {

		return "Binary Files (*.bin)";
	}

}
