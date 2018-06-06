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
package com.github.openasn1.compiler.stages.omast.naming;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Clayton Hoss
 *
 */
public class NamingUtils {
	private static Set<String> JAVAKEYWORDSET;

	private static String[] JAVAKEYWORDS = { "abstract", "assert", "boolean",
			"break", "byte", "case", "catch", "char", "class", "const",
			"continue", "default", "do", "double", "else", "enum", "extends",
			"false", "final", "finally", "float", "for", "goto", "if",
			"implements", "import", "instanceof", "int", "interface", "long",
			"native", "new", "null", "package", "private", "protected",
			"public", "return", "short", "static", "strictfp", "super",
			"switch", "synchronized", "this", "throw", "throws", "transient",
			"true", "try", "void", "volatile", "while" };

	public static boolean isJavaKeyword(String string) {
		return NamingUtils.getJavaKeywordSet().contains(string);

	}

	private static Set<String> getJavaKeywordSet() {
		if (NamingUtils.JAVAKEYWORDSET == null) {
			Set<String> set = new HashSet<String>(Arrays.asList(NamingUtils.JAVAKEYWORDS));
			NamingUtils.JAVAKEYWORDSET = set;
		}
		return NamingUtils.JAVAKEYWORDSET;
	}
}
