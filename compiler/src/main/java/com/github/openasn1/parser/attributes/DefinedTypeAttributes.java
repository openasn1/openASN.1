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
import com.github.openasn1.parser.generated.syntaxtree.DefinedType;
import com.github.openasn1.parser.generated.syntaxtree.ExternalTypeReference;
import com.github.openasn1.parser.generated.syntaxtree.NodeToken;

/**
 * @author Clayton Hoss
 * 
 */
public class DefinedTypeAttributes {

	public static boolean isExternalTypeReference(DefinedType n) {
		return (n.nodeChoice.choice instanceof ExternalTypeReference);
	}

	public static boolean isLocalTypeReference(DefinedType n) {
		if (!(n.nodeChoice.choice instanceof NodeToken)) {
			return false;
		}
		return ((NodeToken) n.nodeChoice.choice).kind == ASN1ParserConstants.TYPEREFERENCE;
	}

	public static String getSymbol(DefinedType n) {
		if (isLocalTypeReference(n)) {
			return ((NodeToken) n.nodeChoice.choice).tokenImage;
		}
		if (isExternalTypeReference(n)) {
			ExternalTypeReference n0 = (ExternalTypeReference) n.nodeChoice.choice;
			String symbol = n0.modulereference.nodeToken.tokenImage;
			symbol = symbol + "." + n0.nodeToken1.tokenImage;
			return symbol;
		}
		return null;
	}
}
