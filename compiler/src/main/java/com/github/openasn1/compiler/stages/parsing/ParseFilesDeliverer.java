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
package com.github.openasn1.compiler.stages.parsing;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import org.apache.log4j.Logger;
import com.github.openasn1.compiler.stages.parsing.inputstreamdeliverers.DirectoryInputStreamDeliverer;
import com.github.openasn1.compiler.stages.parsing.inputstreamdeliverers.FileInputStreamDeliverer;
import com.github.openasn1.compiler.stages.parsing.inputstreamdeliverers.InputStreamDeliverer;
import com.github.openasn1.compiler.stages.parsing.inputstreamdeliverers.NullInputStreamDeliverer;
import com.github.openasn1.compiler.stages.parsing.inputstreamdeliverers.ZipFileInputStreamDeliverer;


/**
 * @author Clayton Hoss
 * 
 */
public class ParseFilesDeliverer {

	private static Logger LOGGER = Logger.getLogger("ParseFilesDeliverer");

	private ListIterator<? extends File> globalFileState;

	private InputStreamDeliverer currentSubDeliverer;

	public ParseFilesDeliverer(List<File> files) {
		init(files);
	}

	public ParseFilesDeliverer(String[] files) {
		List<File> list = new ArrayList<File>();
		for (int i = 0; i < files.length; i++) {
			File file = new File(files[i]);
			if (!file.exists()) {
				LOGGER.error("File does not exist! " + file.toString());
			} else {
				list.add(file);
			}
		}
		init(list);
	}

	private void init(List<File> files) {
		setGlobalFileState(files.listIterator());
		changeInputStreamDeliverer();
	}

	public InputStream getNextFileToParse() {
		InputStream is = getCurrentSubDeliverer().getNextFile();

		while (is == null && getGlobalFileState().hasNext()) {
			changeInputStreamDeliverer();
			is = getCurrentSubDeliverer().getNextFile();
		}
		return is;
	}

	public String getCurrentFileName() {
		return getCurrentSubDeliverer().getCurrentFileName();
	}

	private void changeInputStreamDeliverer() {
		if (!getGlobalFileState().hasNext()) {
			setCurrentSubDeliverer(new NullInputStreamDeliverer());
			return;
		}
		File f = getGlobalFileState().next();
		if (f.isDirectory()) {
			setCurrentSubDeliverer(new DirectoryInputStreamDeliverer(f));
			return;
		}
		
		if (f.isFile()) {
			if (isZipFile(f)) {
				setCurrentSubDeliverer(new ZipFileInputStreamDeliverer(f));
				return;
			}
			setCurrentSubDeliverer(new FileInputStreamDeliverer(f));
		}
	
	}

	private boolean isZipFile(File f) {
		return (f.getPath().endsWith(".zip"));
	}

	/**
	 * @param globalFileState
	 *            the globalFileState to set
	 */
	private void setGlobalFileState(ListIterator<? extends File> globalFileState) {
		this.globalFileState = globalFileState;
	}

	/**
	 * @return the globalFileState
	 */
	private ListIterator<? extends File> getGlobalFileState() {
		return this.globalFileState;
	}

	/**
	 * @param currentSubDeliverer
	 *            the currentSubDeliverer to set
	 */
	private void setCurrentSubDeliverer(InputStreamDeliverer currentSubDeliverer) {
		this.currentSubDeliverer = currentSubDeliverer;
	}

	/**
	 * @return the currentSubDeliverer
	 */
	private InputStreamDeliverer getCurrentSubDeliverer() {
		return this.currentSubDeliverer;
	}

}