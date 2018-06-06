/*
 *   openASN.1 - an open source ASN.1 toolkit for java
 *
 *   Copyright (C) 2007 Clayton Hoss, Marc Weyland
 *
 *   This file is part of openASN.1
 *
 *   openASN.1 is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU Lesser General Public License as 
 *   published by the Free Software Foundation, either version 3 of 
 *   the License, or (at your option) any later version.
 *
 *   openASN.1 is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *   GNU Lesser General Public License for more details.
 *
 *   You should have received a copy of the GNU Lesser General Public License
 *   along with openASN.1. If not, see <http://www.gnu.org/licenses/>.
 * 
 */
package com.github.openasn1.compiler.stages.parsing.inputstreamdeliverers;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import org.apache.log4j.Logger;

/**
 * @author Clayton Hoss
 * 
 */
public class DirectoryInputStreamDeliverer implements InputStreamDeliverer {

	private static Logger LOGGER = Logger
			.getLogger("DirectoryInputStreamDeliverer");

	private File[] fileList;

	private int nextFileIndex = 0;

	class ASN1FileFilter implements FileFilter {

		public boolean accept(File f) {
			return f.getAbsolutePath().toLowerCase().endsWith(".asn");
		}

	}

	public DirectoryInputStreamDeliverer(File f) {
		if (!f.isDirectory()) {
			this.fileList = new File[0];
		}

		this.fileList = f.listFiles(new ASN1FileFilter());

	}

	/**
	 * 
	 * @see com.github.openasn1.compiler.stages.parsing.inputstreamdeliverers.InputStreamDeliverer#getNextFile()
	 */
	public InputStream getNextFile() {
		int index = getNextFileIndex();
		setNextFileIndex(index + 1);
		return getInputStreamFromIndex(index);
	}

	private InputStream getInputStreamFromIndex(int index) {
		if ((index < 0) || (index >= getFileList().length)) {
			return null;
		}
		try {
			return new BufferedInputStream(new FileInputStream(
					getFileList()[index]));
		} catch (FileNotFoundException e) {
			LOGGER.error("File not found: ", e);
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 
	 * @see com.github.openasn1.compiler.stages.parsing.inputstreamdeliverers.InputStreamDeliverer#regetCurrentFile()
	 */
	public InputStream regetCurrentFile() {
		return getInputStreamFromIndex(getNextFileIndex() - 1);
	}

	/**
	 * 
	 * @see com.github.openasn1.compiler.stages.parsing.inputstreamdeliverers.InputStreamDeliverer#getCurrentFileName()
	 */
	public String getCurrentFileName() {
		if (getNextFileIndex() == 0) {
			return "";
		}
		return getFileList()[getNextFileIndex() - 1].getAbsolutePath();
	}

	public boolean hasMoreFiles() {
		return getNextFileIndex() < getFileList().length;
	}

	/**
	 * @return the fileList
	 */
	private File[] getFileList() {
		return this.fileList;
	}

	/**
	 * @return the nextFileIndex
	 */
	private int getNextFileIndex() {
		return this.nextFileIndex;
	}

	/**
	 * @param nextFileIndex
	 *            the nextFileIndex to set
	 */
	private void setNextFileIndex(int nextFileIndex) {
		this.nextFileIndex = nextFileIndex;
	}

}
