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
package com.github.openasn1.parser.casestudy.tool.FeatureReporting;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.github.openasn1.parser.casestudy.tool.CaseStudyTool;
import com.github.openasn1.parser.generated.ParseException;
import com.github.openasn1.parser.generated.syntaxtree.Node;


/**
 * @author Clayton Hoss
 * 
 */
public class FeatureReportingCaseStudyTool extends CaseStudyTool {

	private List<String> failedProtocols = new ArrayList<String>();

	private List<String> passedProtocols = new ArrayList<String>();

	private Map<String, FeatureListHandler> protocolFeatureList = new HashMap<String, FeatureListHandler>();

	private FeatureListHandler currentFeatureList;

	/**
	 * @return the failedProtocols
	 */
	private List<String> getFailedProtocols() {
		return this.failedProtocols;
	}

	@Override
	protected void newProtocolEncountered() {
		if (getLastProtocolPackage().equals("")) {
			setCurrentFeatureList(new FeatureListHandler());
			return;
		}
		getProtocolFeatureList().put(getLastProtocolPackage(),
				getCurrentFeatureList());
		if (isLastProtocolWithoutError()) {
			getPassedProtocols().add(getLastProtocolPackage());
		} else {
			getFailedProtocols().add(getLastProtocolPackage());
		}
		setCurrentFeatureList(new FeatureListHandler());
	}

	@Override
	protected void handleParseException(ParseException e) {
		super.handleParseException(e);

		InputStream input;
		try {
			input = getDirectoryscanner().regetCurrentFileToParse();

			StringBuffer fileContent = new StringBuffer();

			int asciiCharacter;

			while ((asciiCharacter = input.read()) >= 0) {
				fileContent.append((char) asciiCharacter);
			}
			String content = fileContent.toString();

			addFeatureByTextualAnalysis(content,
					FeatureList.InformationObjectClass,
					"^\\s*[A-Z](-?[A-Za-z0-9])*\\s*::=\\s*CLASS\\s*\\{");
			addFeatureByTextualAnalysis(
					content,
					FeatureList.InformationObject,
					"^\\s*[a-z](-?[A-Za-z0-9])*\\s*(\\{[^\\}]*\\})?\\s*[A-Z](-?[A-Za-z0-9])*\\s*::=\\s*\\{[^&\\}]*&[^\\}]*\\}");
			addFeatureByTextualAnalysis(
					content,
					FeatureList.InformationObject,
					"^\\s*[a-z](-?[A-Za-z0-9])*\\s*(\\{[^\\}]*\\})?\\s*[A-Z](-?[A-Za-z0-9])*\\s*::=\\s*\\{\\s*[A-Z][^\\}]*\\}");
			addFeatureByTextualAnalysis(content,
					FeatureList.UserConstraintsConstrainedBy,
					"CONSTRAINED\\s*BY\\s*\\{");

			addFeatureByTextualAnalysis(content,
					FeatureList.ContentsConstraintEncodedBy,
					"ENCODED\\s*BY\\s*(\\{|[a-z](-?[A-Za-z0-9])*)");
			addFeatureByTextualAnalysis(content,
					FeatureList.ContentsConstraintContaining,
					"CONTAINING\\s*(\\{|[A-Z](-?[A-Za-z0-9])*)");
			addFeatureByTextualAnalysis(content,
					FeatureList.ContentsConstraint,
					"ENCODED\\s*BY\\s*(\\{|[a-z](-?[A-Za-z0-9])*)");
			addFeatureByTextualAnalysis(content,
					FeatureList.ContentsConstraint,
					"CONTAINING\\s*(\\{|[A-Z](-?[A-Za-z0-9])*)");

			addFeatureByTextualAnalysis(
					content,
					FeatureList.Parameterization,
					"^\\s*[A-Za-z](-?[A-Za-z0-9])*\\s*\\{[^\\}]*\\}\\s*([A-Z](-?[A-Za-z0-9])*)?\\s*::=");
			addFeatureByTextualAnalysis(content,
					FeatureList.ClassTypeIdentifyer,
					"\\s*TYPE\\-IDENTIFIER\\s*");

		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	private void addFeatureByTextualAnalysis(String content,
			FeatureList feature, String regexp) {
		Pattern pattern = Pattern.compile(regexp, Pattern.DOTALL
				| Pattern.MULTILINE);
		Matcher matcher = pattern.matcher(content);
		while (matcher.find()) {
			if (feature == FeatureList.Parameterization) {
				if (matcher.group(2) != null
						&& (!matcher.group(2).equals("DEFINITIONS"))) {
					continue;
				}
			}
			getCurrentFeatureList().incFeatureCount(feature);
		}
	}

	@Override
	protected void analyzeAST(Node astroot) {
		FeatureCountingVisitor vis = new FeatureCountingVisitor();
		astroot.accept(vis);
		getCurrentFeatureList().add(vis.getFeatureList());
		getCurrentFeatureList().incFeatureCount(FeatureList.ParsedFiles);
	}

	public FeatureReportingCaseStudyTool(String dir) {
		super(dir);
	}

	/**
	 * @return the passedProtocols
	 */
	private List<String> getPassedProtocols() {
		return this.passedProtocols;
	}

	/**
	 * @return the protocolFeatureList
	 */
	private Map<String, FeatureListHandler> getProtocolFeatureList() {
		return this.protocolFeatureList;
	}

	/**
	 * @return the currentFeatureList
	 */
	private FeatureListHandler getCurrentFeatureList() {
		return this.currentFeatureList;
	}

	/**
	 * @param currentFeatureList
	 *            the currentFeatureList to set
	 */
	private void setCurrentFeatureList(FeatureListHandler currentFeatureList) {
		this.currentFeatureList = currentFeatureList;
	}

	@Override
	protected void updateProgressBar() {
		if ((getFilesamount() % 160) == 0) {
			System.out.println(getFilesamount() / 160);
		}
		getCurrentFeatureList().incFeatureCount(FeatureList.Files);
	}

	@Override
	public void scandir() {
		super.scandir();
		PrintStream def = System.out;
		System.setOut(createOutputPrintStream());
		System.out.println();
		System.out.println("FeatureMatrix (passed protocols)");
		System.out.println("----------------");
		System.out.print("Name;");
		for (FeatureList f : FeatureList.values()) {
			System.out.print(f.toString() + ";");
		}
		System.out.println();

		for (String key : getPassedProtocols()) {
			System.out.print(key + ";");
			FeatureListHandler fh = getProtocolFeatureList().get(key);
			for (FeatureList f : FeatureList.values()) {
				System.out.print(fh.getFeature(f) + ";");
			}
			System.out.println();
		}

		System.out.println();
		System.out.println();
		System.out.println("FeatureMatrix (failed protocols)");
		System.out.println("----------------");
		System.out.print("Name;");
		for (FeatureList f : FeatureList.values()) {
			System.out.print(f.toString() + ";");
		}
		System.out.println();

		for (String key : getFailedProtocols()) {
			System.out.print(key + ";");
			FeatureListHandler fh = getProtocolFeatureList().get(key);
			for (FeatureList f : FeatureList.values()) {
				System.out.print(fh.getFeature(f) + ";");
			}
			System.out.println();
		}
		System.out.flush();
		System.setOut(def);
	}

	private PrintStream createOutputPrintStream() {
		PrintStream file;
		try {
			 file = new PrintStream("casestudyresults/FeatureMatrix.csv");
		} catch (FileNotFoundException e) {
			file = System.out;
			e.printStackTrace();
		}
		return file;
	}

}
