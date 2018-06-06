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

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.github.openasn1.parser.generated.ParseException;


/**
 * @author Clayton Hoss
 * 
 */
public class SingleErrorPrintingCaseStudyTool extends CaseStudyTool {

	public SingleErrorPrintingCaseStudyTool(String dir) {
		super(dir);
	}

	/**
	 * @see com.github.openasn1.parser.casestudy.tool.CaseStudyTool#handleParseException(com.github.openasn1.parser.generated.ParseException)
	 */
	@Override
	protected void handleParseException(ParseException e) {
		System.out.println(getDirectoryscanner().getCurrentFileName());
		e.printStackTrace();
		try {
			OutputStream out = new BufferedOutputStream(new FileOutputStream(
					"casestudyresults/errors/errorfile.asn"));
			InputStream in = new BufferedInputStream(getDirectoryscanner()
					.regetCurrentFileToParse());
			int b = in.read();
			while (b != -1) {
				out.write(b);
				b = in.read();
			}
			out.close();
			in.close();

			FileWriter fw = new FileWriter(
					"casestudyresults/errors/errorfile.txt");
			fw.write(e.toString());
			fw.close();

		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e2) {
			e2.printStackTrace();
		}
	}

	/**
	 * @see com.github.openasn1.parser.casestudy.tool.CaseStudyTool#shouldParseMoreFiles(com.github.openasn1.parser.generated.ParseException)
	 */
	@Override
	protected boolean shouldParseMoreFiles(ParseException e) {
		return false;
	}

}
