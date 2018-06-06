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
import com.github.openasn1.parser.generated.syntaxtree.Tag;
import com.github.openasn1.parser.generated.syntaxtree.TagClass;

/**
 * @author Clayton Hoss
 * 
 */
public class TagAttributes {

	public static boolean isContextSpecificClass(Tag n) {
		return !(n.nodeOptional.present());
	}

	public static boolean isUniversalClass(Tag n) {
		if (!n.nodeOptional.present()) {
			return false;
		}
		TagClass tc = (TagClass) (n.nodeOptional.node);
		return (((NodeToken) (tc.nodeChoice.choice)).kind == ASN1ParserConstants.UNIVERSAL_TKN);
	}

	public static boolean isApplicationClass(Tag n) {
		if (!n.nodeOptional.present()) {
			return false;
		}
		TagClass tc = (TagClass) (n.nodeOptional.node);
		return (((NodeToken) (tc.nodeChoice.choice)).kind == ASN1ParserConstants.APPLICATION_TKN);
	}

	public static boolean isPrivateClass(Tag n) {
		if (!n.nodeOptional.present()) {
			return false;
		}
		TagClass tc = (TagClass) (n.nodeOptional.node);
		return (((NodeToken) (tc.nodeChoice.choice)).kind == ASN1ParserConstants.PRIVATE_TKN);
	}

}
