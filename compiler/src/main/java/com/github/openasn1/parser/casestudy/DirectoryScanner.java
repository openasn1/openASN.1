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
import java.io.FileFilter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Stack;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import com.github.openasn1.parser.casestudy.filefilters.ASNZipFilter;
import com.github.openasn1.parser.casestudy.filefilters.DirectoryFilter;
import com.github.openasn1.parser.utils.EmptyEnumration;

/**
 * Searches for *.asn files in zip files named *pkg.asn.zip returns the next
 * file until finished (hopefully);
 * 
 * @author Clayton Hoss
 * 
 */
public class DirectoryScanner {
	private String basepath = "asn1\\database";

	private static FileFilter ZIPFILEFILTER = new ASNZipFilter();

	private static FileFilter DIRECTORYFILTER = new DirectoryFilter();

	private Enumeration<? extends ZipEntry> zipstate = new EmptyEnumration<ZipEntry>();

	private ZipFile currentzipfile = null;

	private String filepattern = ".asn";

	private Stack<DirectoryScanningState> state = new Stack<DirectoryScanningState>();

	private String currentFileName;

	private ZipEntry currentzipentry;

	public DirectoryScanner() {
		init();
	}

	public DirectoryScanner(String basepath) {
		this.setBasepath(basepath);
		init();
	}

	private void init() {
		File f = new File(getBasepath());
		addDirectoryToStack(f);
	}

	private void addDirectoryToStack(File basedir) {
		File[] files = basedir.listFiles(DirectoryScanner.ZIPFILEFILTER);
		File[] dirs = basedir.listFiles(DirectoryScanner.DIRECTORYFILTER);
		files = modifyFileList(files);
		dirs = modifyDirList(dirs);
		this.getState().add(new DirectoryScanningState(dirs, files));
	}

	protected File[] modifyFileList(File[] files) {
		return files;
	}

	protected File[] modifyDirList(File[] dirs) {
		return dirs;
	}

	private DirectoryScanningState getCurrentState() {
		return this.getState().peek();
	}

	private File getNextZipFile() {
		DirectoryScanningState state = getCurrentState();
		if (state.hasMoreFiles()) {
			return state.getNextFile();
		}
		if (state.hasMoreDirs()) {
			File dir = state.getNextDir();
			addDirectoryToStack(dir);
			return getNextZipFile();
		}

		this.getState().pop();
		if (!this.getState().empty()) {
			return getNextZipFile();
		}
		return null;
	}

	private ZipEntry getNextZipEntryInZipFile() {
		if (!getZipstate().hasMoreElements()) {
			return null;
		}
		ZipEntry ze = this.getZipstate().nextElement();
		if (ze.isDirectory()) {
			return getNextZipEntryInZipFile();
		}
		if (!ze.getName().toLowerCase().endsWith(this.getFilepattern())) {
			return getNextZipEntryInZipFile();
		}
		return ze;

	}

	private ZipEntry getNextZipEntry() throws IOException {
		ZipEntry ze = getNextZipEntryInZipFile();
		if (ze != null) {
			return ze;
		}
		File zipfile = getNextZipFile();

		if (zipfile == null) {
			return null;
		}
		try {
			this.setCurrentzipfile(new ZipFile(zipfile));
			this.setZipstate(this.getCurrentzipfile().entries());
		} catch (ZipException e) {
			throw new IOException(e.toString());
		}
		return getNextZipEntry();
	}

	public InputStream getNextFileToParse() throws IOException {
		ZipEntry ze = getNextZipEntry();
		setCurrentzipentry(ze);
		if (ze == null) {
			return null;
		}
		setCurrentFileName(getCurrentzipfile().getName() + File.separator
				+ ze.getName());
		return this.getCurrentzipfile().getInputStream(ze);
	}

	public InputStream regetCurrentFileToParse() throws IOException {
		if (getCurrentzipentry() == null) {
			return null;
		}
		return this.getCurrentzipfile().getInputStream(getCurrentzipentry());
	}

	/**
	 * @param basepath
	 *            the basepath to set
	 */
	private void setBasepath(String basepath) {
		this.basepath = basepath;
	}

	/**
	 * @return the basepath
	 */
	private String getBasepath() {
		return this.basepath;
	}

	/**
	 * @param currentFileName
	 *            the currentFileName to set
	 */
	private void setCurrentFileName(String currentFileName) {
		this.currentFileName = currentFileName;
	}

	/**
	 * @return the currentFileName
	 */
	public String getCurrentFileName() {
		return this.currentFileName;
	}

	public String getCurrentFileNameRelativeToBasePath() {
		return getCurrentFileName().substring(getBasepath().length() + 1);
	}

	/**
	 * @param currentzipfile
	 *            the currentzipfile to set
	 */
	private void setCurrentzipfile(ZipFile currentzipfile) {
		this.currentzipfile = currentzipfile;
	}

	/**
	 * @return the currentzipfile
	 */
	private ZipFile getCurrentzipfile() {
		return this.currentzipfile;
	}

	/**
	 * @return the current Zip File's Name
	 */
	public String getCurrentZipFileName() {
		return getCurrentzipfile().getName();
	}

	public String getCurrentZipFileNameRelativeToBasePath() {
		return getCurrentZipFileName().substring(getBasepath().length() + 1);
	}

	/**
	 * @param currentzipstate
	 *            the currentzipstate to set
	 */
	private void setZipstate(Enumeration<? extends ZipEntry> currentzipstate) {
		this.zipstate = currentzipstate;
	}

	/**
	 * @return the currentzipstate
	 */
	private Enumeration<? extends ZipEntry> getZipstate() {
		return this.zipstate;
	}

	/**
	 * @return the filepattern
	 */
	private String getFilepattern() {
		return this.filepattern;
	}

	/**
	 * @return the state
	 */
	private Stack<DirectoryScanningState> getState() {
		return this.state;
	}

	/**
	 * @return the currentzipentry
	 */
	private ZipEntry getCurrentzipentry() {
		return this.currentzipentry;
	}

	/**
	 * @param currentzipentry
	 *            the currentzipentry to set
	 */
	private void setCurrentzipentry(ZipEntry currentzipentry) {
		this.currentzipentry = currentzipentry;
	}

}
