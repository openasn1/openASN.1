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
package com.github.openasn1.parser.casestudy;

import java.io.File;

/**
 * This class holds the current state of the directory scanning operation
 * 
 * @author Clayton Hoss
 * 
 */
public class DirectoryScanningState {

	private File[] dirs;

	private File[] files;

	private int dirpos;

	private int filepos;

	public DirectoryScanningState(File[] dirs, File[] files) {
		setFiles(files);
		setDirs(dirs);
		setDirpos(0);
		setFilepos(0);
	}

	public boolean hasMoreFiles() {
		return (getFiles().length > getFilepos());
	}

	public boolean hasMoreDirs() {
		return (getDirs().length > getDirpos());
	}

	/**
	 * @return the dirpos
	 */
	private int getDirpos() {
		return this.dirpos;
	}

	/**
	 * @param dirpos
	 *            the dirpos to set
	 */
	private void setDirpos(int dirpos) {
		this.dirpos = dirpos;
	}

	/**
	 * @return the dirs
	 */
	private File[] getDirs() {
		return this.dirs;
	}

	/**
	 * @param dirs
	 *            the dirs to set
	 */
	private void setDirs(File[] dirs) {
		this.dirs = dirs;
	}

	/**
	 * @return the filepos
	 */
	private int getFilepos() {
		return this.filepos;
	}

	/**
	 * @param filepos
	 *            the filepos to set
	 */
	private void setFilepos(int filepos) {
		this.filepos = filepos;
	}

	/**
	 * @return the files
	 */
	private File[] getFiles() {
		return this.files;
	}

	/**
	 * @param files
	 *            the files to set
	 */
	private void setFiles(File[] files) {
		this.files = files;
	}

	public File getNextDir() {
		if (!hasMoreDirs()) {
			return null;
		}

		int oldpos = getDirpos();
		setDirpos(oldpos + 1);
		return getDirs()[oldpos];
	}

	public File getNextFile() {
		if (!hasMoreFiles()) {
			return null;
		}

		int oldpos = getFilepos();
		setFilepos(oldpos + 1);
		return getFiles()[oldpos];
	}
}
