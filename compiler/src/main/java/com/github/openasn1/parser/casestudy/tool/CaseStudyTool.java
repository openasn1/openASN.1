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
package com.github.openasn1.parser.casestudy.tool;

import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.SimpleTimeZone;

import com.github.openasn1.parser.casestudy.DirectoryScanner;
import com.github.openasn1.parser.casestudy.ItuDirectoryScanner;
import com.github.openasn1.parser.generated.ASN1Parser;
import com.github.openasn1.parser.generated.ParseException;
import com.github.openasn1.parser.generated.syntaxtree.Node;


/**
 * @author Clayton Hoss
 * 
 */
public class CaseStudyTool {

	private DirectoryScanner directoryscanner;

	private int parsedfiles = 0;

	private int filesamount = 0;

	private long elapsedtime;

	private int protocolsAmount = 0;

	private int parsedProtocols = 0;

	private boolean isLastProtocolWithoutError = false;

	private String lastProtocolPackage = "";
	
	private String currentProtocolPackage = "";

	/**
	 * @return the currentProtocolPackage
	 */
	protected String getCurrentProtocolPackage() {
		return this.currentProtocolPackage;
	}

	public CaseStudyTool(String dir) {
		setDirectoryscanner(new ItuDirectoryScanner(dir));
	}

	public void scandir() {
		long starttime = System.currentTimeMillis();

		parseFilesInDir();

		long endtime = System.currentTimeMillis();
		setElapsedtime(endtime - starttime);
		printresults();

	}

	/**
	 * 
	 */
	private void printresults() {
		DecimalFormat dcf = new DecimalFormat("0.00");
		DateFormat df = new SimpleDateFormat("HH:mm:ss");

		Date elapsedtimeasdate = new Date(getElapsedtime());
		df.setTimeZone(new SimpleTimeZone(0, "Time Measurement"));
		System.out.print("Elapsed time: " + (getElapsedtime()) + " ms or ");
		System.out.println(df.format(elapsedtimeasdate) + " hours");
		System.out.println("Parsed " + getParsedfiles() + " files from "
				+ getFilesamount() + " total");
		double percentile = ((double) getParsedfiles() / (double) getFilesamount()) * 100;
		System.out.println(dcf.format(percentile) + "% files parsed");
		System.out.println();
		System.out.println("Parsed " + getParsedProtocols()
				+ " protocols from " + getProtocolsAmount() + " total");
		percentile = ((double) getParsedProtocols() / (double) getProtocolsAmount()) * 100;
		System.out.println(dcf.format(percentile) + "% protocols parsed");
	}

	/**
	 * 
	 */
	private void parseFilesInDir() {
		InputStream res = null;
		try {
			res = getDirectoryscanner().getNextFileToParse();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		initProgressBar();
		while (res != null) {
			try {
				checkIfNewProtocol();
				updateProgressBar();
				setFilesamount(getFilesamount() + 1);
				try {
					Node astroot = ASN1Parser.parseStream(res);
					analyzeAST(astroot);
					setParsedfiles(getParsedfiles() + 1);
				} catch (ParseException e) {
					setLastProtocolWithoutError(false);
					handleParseException(e);
					if (!shouldParseMoreFiles(e)) {
						break;
					}
				}
				res = getDirectoryscanner().getNextFileToParse();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		incProtocolCounter();
	}

	protected void initProgressBar() {
	
	}

	protected void updateProgressBar() {
		
	}

	private void checkIfNewProtocol() {
		if (!getLastProtocolPackage().equals(
				getDirectoryscanner().getCurrentZipFileNameRelativeToBasePath())) {
		 incProtocolCounter();
		}
	}
	private void incProtocolCounter() {
		setCurrentProtocolPackage(getDirectoryscanner().getCurrentZipFileNameRelativeToBasePath());
		newProtocolEncountered();
		setLastProtocolPackage(getDirectoryscanner().getCurrentZipFileNameRelativeToBasePath());
		if (isLastProtocolWithoutError() == true) {
			setParsedProtocols(getParsedProtocols() + 1);
		}
		setProtocolsAmount(getProtocolsAmount() + 1);
		setLastProtocolWithoutError(true);
	}

	/**
	 * Method that is called after sucessfully parsing an ASN.1 File
	 * 
	 * @param astroot
	 */
	protected void analyzeAST(Node astroot) {

	}

	protected void newProtocolEncountered() {

	}

	/**
	 * Handles a ParseException and invokes the further actions (logging the
	 * exception, etc)
	 * 
	 * @param e
	 */
	protected void handleParseException(ParseException e) {

	}

	/**
	 * This method is called when a parseException occurs. Returns a boolean, if
	 * more files should be parsed
	 * 
	 * @param e
	 * @return
	 */
	protected boolean shouldParseMoreFiles(ParseException e) {
		return true;
	}

	/**
	 * @param directoryscanner
	 *            the directoryscanner to set
	 */
	private void setDirectoryscanner(DirectoryScanner directoryscanner) {
		this.directoryscanner = directoryscanner;
	}

	/**
	 * @return the directoryscanner
	 */
	protected DirectoryScanner getDirectoryscanner() {
		return this.directoryscanner;
	}

	/**
	 * @param parsedfiles
	 *            the parsedfiles to set
	 */
	private void setParsedfiles(int parsedfiles) {
		this.parsedfiles = parsedfiles;
	}

	/**
	 * @return the parsedfiles
	 */
	private int getParsedfiles() {
		return this.parsedfiles;
	}

	/**
	 * @param filesamount
	 *            the filesamount to set
	 */
	private void setFilesamount(int filesamount) {
		this.filesamount = filesamount;
	}

	/**
	 * @return the filesamount
	 */
	protected int getFilesamount() {
		return this.filesamount;
	}

	/**
	 * @param elapsedtime
	 *            the elapsedtime to set
	 */
	private void setElapsedtime(long elapsedtime) {
		this.elapsedtime = elapsedtime;
	}

	/**
	 * @return the elapsedtime
	 */
	private long getElapsedtime() {
		return this.elapsedtime;
	}

	/**
	 * @param protocolsAmount
	 *            the protocolsAmount to set
	 */
	private void setProtocolsAmount(int protocolsAmount) {
		this.protocolsAmount = protocolsAmount;
	}

	/**
	 * @return the protocolsAmount
	 */
	private int getProtocolsAmount() {
		return this.protocolsAmount;
	}

	/**
	 * @param parsedProtocols
	 *            the parsedProtocols to set
	 */
	private void setParsedProtocols(int parsedProtocols) {
		this.parsedProtocols = parsedProtocols;
	}

	/**
	 * @return the parsedProtocols
	 */
	private int getParsedProtocols() {
		return this.parsedProtocols;
	}

	/**
	 * @param isLastProtocolWithoutError
	 *            the isLastProtocolWithoutError to set
	 */
	private void setLastProtocolWithoutError(boolean isLastProtocolWithoutError) {
		this.isLastProtocolWithoutError = isLastProtocolWithoutError;
	}

	/**
	 * @return the isLastProtocolWithoutError
	 */
	protected boolean isLastProtocolWithoutError() {
		return this.isLastProtocolWithoutError;
	}

	/**
	 * @param lastProtocolPackage
	 *            the lastProtocolPackage to set
	 */
	private void setLastProtocolPackage(String lastProtocolPackage) {
		this.lastProtocolPackage = lastProtocolPackage;
	}

	/**
	 * @return the lastProtocolPackage
	 */
	protected String getLastProtocolPackage() {
		return this.lastProtocolPackage;
	}

	/**
	 * @param currentProtocolPackage the currentProtocolPackage to set
	 */
	private void setCurrentProtocolPackage(String currentProtocolPackage) {
		this.currentProtocolPackage = currentProtocolPackage;
	}
}
