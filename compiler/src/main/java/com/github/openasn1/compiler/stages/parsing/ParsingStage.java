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

import java.io.InputStream;

import org.apache.log4j.Logger;
import com.github.openasn1.compiler.Exceptions.CompilationStoppingException;
import com.github.openasn1.compiler.astutils.ASN1ASTStorage;
import com.github.openasn1.parser.generated.ASN1Parser;
import com.github.openasn1.parser.generated.ParseException;
import com.github.openasn1.parser.generated.syntaxtree.Node;


/**
 * @author Clayton Hoss
 * 
 */
public class ParsingStage {

	private static Logger LOGGER = Logger.getLogger("ParsingStep");

	private ParseFilesDeliverer parseFileDeliverer;

	public ParsingStage(String[] files) {
		setParseFileDeliverer(new ParseFilesDeliverer(files));
	}

	public ParsingStage(String file) {
		String[] files = new String[1];
		files[0] = file;
		setParseFileDeliverer(new ParseFilesDeliverer(files));
	}

	public ASN1ASTStorage parseFiles() throws CompilationStoppingException {
		ASN1ASTStorage storage = new ASN1ASTStorage();
		InputStream stream = getParseFileDeliverer().getNextFileToParse();
		while (stream != null) {
			LOGGER.info("Parsing File "
					+ getParseFileDeliverer().getCurrentFileName());
			try {
				ASN1Parser p = new ASN1Parser(stream);
				Node n = p.parse();
				storage.addAstToStorage(getParseFileDeliverer()
						.getCurrentFileName(), n);
			} catch (ParseException e) {
				LOGGER.error("Error Parsing File "
						+ getParseFileDeliverer().getCurrentFileName(), e);
			}

			stream = getParseFileDeliverer().getNextFileToParse();
		}

		return storage;
	}

	/**
	 * @param parseFileDeliverer
	 *            the parseFileDeliverer to set
	 */
	private void setParseFileDeliverer(ParseFilesDeliverer parseFileDeliverer) {
		this.parseFileDeliverer = parseFileDeliverer;
	}

	/**
	 * @return the parseFileDeliverer
	 */
	private ParseFilesDeliverer getParseFileDeliverer() {
		return this.parseFileDeliverer;
	}
}
