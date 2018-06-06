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
package com.github.openasn1.parser;

import java.io.FileInputStream;

import com.github.openasn1.parser.casestudy.tool.FeatureReporting.FeatureCountingVisitor;
import com.github.openasn1.parser.generated.ASN1Parser;
import com.github.openasn1.parser.generated.syntaxtree.Node;

/**
 * @author Clayton Hoss
 * 
 */
public class ParserInvoker {

	public static void main(String[] args) {
		System.out.println("Reading Input File");
		try {
			String path;
			if (args.length < 1) {
				path = "asn1specifications/ASN1-Object-Identifier-Module.txt";
			} else {
				path = args[0];
			}
			ASN1Parser p = new ASN1Parser(new FileInputStream(path));
			// p.parse();
			Node start = p.parse();
			start.accept(new FeatureCountingVisitor());
		} catch (Exception e) {
			System.out.println("Oops.");
			System.out.println(e.getMessage());
			e.printStackTrace();
		}

	}
}
