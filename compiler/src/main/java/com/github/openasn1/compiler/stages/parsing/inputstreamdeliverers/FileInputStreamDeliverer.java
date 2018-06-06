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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import org.apache.log4j.Logger;

/**
 * @author Clayton Hoss
 * 
 */
public class FileInputStreamDeliverer implements InputStreamDeliverer {

	private static Logger LOGGER = Logger.getLogger("FileInputStreamDeliverer");

	private File currentFile;

	private boolean fileAlreadyGot = false;

	public FileInputStreamDeliverer(File f) {
		this.currentFile = f;
	}

	/**
	 * @see com.github.openasn1.compiler.stages.parsing.inputstreamdeliverers.InputStreamDeliverer#getNextFile()
	 */
	public InputStream getNextFile() {
		if (isFileAlreadyGot()) {
			return null;
		}
		try {
			setFileAlreadyGot(true);
			return new BufferedInputStream(
					new FileInputStream(getCurrentFile()));
		} catch (FileNotFoundException e) {
			LOGGER.error("File not found: ", e);
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * @see com.github.openasn1.compiler.stages.parsing.inputstreamdeliverers.InputStreamDeliverer#regetCurrentFile()
	 */
	public InputStream regetCurrentFile() {
		if (!isFileAlreadyGot()) {
			return null;
		}

		try {
			return new BufferedInputStream(
					new FileInputStream(getCurrentFile()));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}

	public boolean hasMoreFiles() {
		return !isFileAlreadyGot();
	}

	/**
	 * @see com.github.openasn1.compiler.stages.parsing.inputstreamdeliverers.InputStreamDeliverer#getCurrentFileName()
	 */
	public String getCurrentFileName() {
		return getCurrentFile().getAbsolutePath();
	}

	/**
	 * @return the currentFile
	 */
	private File getCurrentFile() {
		return this.currentFile;
	}

	/**
	 * @return the fileAlreadyGot
	 */
	private boolean isFileAlreadyGot() {
		return this.fileAlreadyGot;
	}

	/**
	 * @param fileAlreadyGot
	 *            the fileAlreadyGot to set
	 */
	private void setFileAlreadyGot(boolean fileAlreadyGot) {
		this.fileAlreadyGot = fileAlreadyGot;
	}

}
