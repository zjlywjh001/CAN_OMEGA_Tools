package me.jarviswang.canomega.commons;

import java.io.File;

import javax.swing.filechooser.FileFilter;

public class LogFileFilter extends FileFilter {

	@Override
	public boolean accept(File f) {
		if (f.isDirectory()) {
			return true;
		}
		return (f.getName().toLowerCase().endsWith(".csv"));
	}

	@Override
	public String getDescription() {

		return "CSV Files (*.csv)";
	}

}
