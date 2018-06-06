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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import org.apache.log4j.Logger;
import com.github.openasn1.parser.utils.EmptyEnumration;


/**
 * @author Clayton Hoss
 * 
 */
public class ZipFileInputStreamDeliverer implements InputStreamDeliverer {

	private static Logger LOGGER = Logger
			.getLogger("ZipFileInputStreamDeliverer");

	private static String FILEPATTERN = ".asn";

	private ZipFile currentzipfile = null;

	private ZipEntry currentzipentry;

	private Enumeration<? extends ZipEntry> zipstate = new EmptyEnumration<ZipEntry>();

	public ZipFileInputStreamDeliverer(File zipfile) {
		try {
			this.setCurrentzipfile(new ZipFile(zipfile));
			this.setZipstate(this.getCurrentzipfile().entries());
		} catch (ZipException e) {
			LOGGER.error("Invalid Zip File ", e);
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @see com.github.openasn1.compiler.stages.parsing.inputstreamdeliverers.InputStreamDeliverer#getNextFile()
	 */
	public InputStream getNextFile() {
		ZipEntry ze = getNextZipEntryInZipFile();
		setCurrentzipentry(ze);
		if (ze == null) {
			return null;
		}
		return getInputStreamForZipEntry(ze);
	}

	/**
	 * @param ze
	 * @return
	 */
	private InputStream getInputStreamForZipEntry(ZipEntry ze) {
		try {
			return getCurrentzipfile().getInputStream(ze);
		} catch (IOException e) {
			LOGGER.error("IO Exception while accessing ZipFile", e);
			e.printStackTrace();
			return null;
		}
	}

	private ZipEntry getNextZipEntryInZipFile() {
		if (!getZipstate().hasMoreElements()) {
			return null;
		}
		ZipEntry ze = getZipstate().nextElement();
		if (ze.isDirectory()) {
			return getNextZipEntryInZipFile();
		}
		if (!ze.getName().toLowerCase().endsWith(FILEPATTERN)) {
			return getNextZipEntryInZipFile();
		}
		return ze;

	}

	/**
	 * 
	 * @see com.github.openasn1.compiler.stages.parsing.inputstreamdeliverers.InputStreamDeliverer#regetCurrentFile()
	 */
	public InputStream regetCurrentFile() {
		if (getCurrentzipentry() == null) {
			return null;
		}
		return getInputStreamForZipEntry(getCurrentzipentry());
	}
	
	public boolean hasMoreFiles() {
		return getZipstate().hasMoreElements();
	}

	/**
	 * 
	 * @see com.github.openasn1.compiler.stages.parsing.inputstreamdeliverers.InputStreamDeliverer#getCurrentFileName()
	 */
	public String getCurrentFileName() {
		if (getCurrentzipentry() == null) {
			return "";
		}
		return getCurrentzipfile().getName() + File.separator
				+ getCurrentzipentry().getName();
	}

	/**
	 * @return the currentzipfile
	 */
	private ZipFile getCurrentzipfile() {
		return this.currentzipfile;
	}

	/**
	 * @param currentzipfile
	 *            the currentzipfile to set
	 */
	private void setCurrentzipfile(ZipFile currentzipfile) {
		this.currentzipfile = currentzipfile;
	}

	/**
	 * @return the zipstate
	 */
	private Enumeration<? extends ZipEntry> getZipstate() {
		return this.zipstate;
	}

	/**
	 * @param zipstate
	 *            the zipstate to set
	 */
	private void setZipstate(Enumeration<? extends ZipEntry> zipstate) {
		this.zipstate = zipstate;
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
