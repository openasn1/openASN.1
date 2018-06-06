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
package com.github.openasn1.parser.attributes;

import com.github.openasn1.parser.generated.ASN1ParserConstants;
import com.github.openasn1.parser.generated.syntaxtree.NodeToken;
import com.github.openasn1.parser.generated.syntaxtree.RestrictedCharacterStringType;

/**
 * @author Clayton Hoss
 * 
 */
public class RestrictedCharacterStringTypeAttributes {

	public static boolean isBMPString(RestrictedCharacterStringType n) {
		return ((NodeToken) n.nodeChoice.choice).kind == ASN1ParserConstants.BMPSTRING_TKN;
	}

	public static boolean isGeneralString(RestrictedCharacterStringType n) {
		return ((NodeToken) n.nodeChoice.choice).kind == ASN1ParserConstants.GENERALSTRING_TKN;
	}

	public static boolean isGraphicString(RestrictedCharacterStringType n) {
		return ((NodeToken) n.nodeChoice.choice).kind == ASN1ParserConstants.GRAPHICSTRING_TKN;
	}

	public static boolean isIA5String(RestrictedCharacterStringType n) {
		return ((NodeToken) n.nodeChoice.choice).kind == ASN1ParserConstants.IA5STRING_TKN;
	}

	public static boolean isIso64String(RestrictedCharacterStringType n) {
		return ((NodeToken) n.nodeChoice.choice).kind == ASN1ParserConstants.ISO646STRING_TKN;
	}

	public static boolean isNumericString(RestrictedCharacterStringType n) {
		return ((NodeToken) n.nodeChoice.choice).kind == ASN1ParserConstants.NUMERICSTRING_TKN;
	}

	public static boolean isPrintableString(RestrictedCharacterStringType n) {
		return ((NodeToken) n.nodeChoice.choice).kind == ASN1ParserConstants.PRINTABLESTRING_TKN;
	}

	public static boolean isTeletexString(RestrictedCharacterStringType n) {
		return ((NodeToken) n.nodeChoice.choice).kind == ASN1ParserConstants.TELETEXSTRING_TKN;
	}

	public static boolean isT61String(RestrictedCharacterStringType n) {
		return ((NodeToken) n.nodeChoice.choice).kind == ASN1ParserConstants.T61STRING_TKN;
	}

	public static boolean isUTF8String(RestrictedCharacterStringType n) {
		return ((NodeToken) n.nodeChoice.choice).kind == ASN1ParserConstants.UTF8STRING_TKN;
	}

	public static boolean isUniversalString(RestrictedCharacterStringType n) {
		return ((NodeToken) n.nodeChoice.choice).kind == ASN1ParserConstants.UNIVERSALSTRING_TKN;
	}

	public static boolean isVideotexString(RestrictedCharacterStringType n) {
		return ((NodeToken) n.nodeChoice.choice).kind == ASN1ParserConstants.VIDEOTEXSTRING_TKN;
	}

	public static boolean isVisibleString(RestrictedCharacterStringType n) {
		return ((NodeToken) n.nodeChoice.choice).kind == ASN1ParserConstants.VISIBLESTRING_TKN;
	}

}
