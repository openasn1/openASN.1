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

import com.github.openasn1.parser.generated.syntaxtree.BuiltinValue;
import com.github.openasn1.parser.generated.syntaxtree.DefinedValue;
import com.github.openasn1.parser.generated.syntaxtree.IntegerValue;
import com.github.openasn1.parser.generated.syntaxtree.ReferencedValue;
import com.github.openasn1.parser.generated.syntaxtree.Value;

/**
 * @author Clayton Hoss
 * 
 */
public class ValueAttributes {

	public static boolean isBuiltinValue(Value n) {

		return (n.nodeChoice.choice instanceof BuiltinValue);

	}

	public static BuiltinValue getBuiltinValue(Value n) {
		if (!isBuiltinValue(n)) {
			return null;
		}
		return (BuiltinValue) n.nodeChoice.choice;
	}

	public static boolean isReferencedValue(Value n) {
		return (n.nodeChoice.choice instanceof ReferencedValue);
	}

	public static ReferencedValue getReferencedValue(Value n) {
		if (!isReferencedValue(n)) {
			return null;
		}
		return (ReferencedValue) n.nodeChoice.choice;
	}

	public static boolean isDefinedValue(Value n) {
		return isReferencedValue(n);
	}

	public static DefinedValue getDefinedValue(Value n) {
		return getReferencedValue(n).definedValue;
	}

	public static boolean isIntegerValue(Value n) {
		if (!isBuiltinValue(n)) {
			return false;
		}

		return (getBuiltinValue(n).nodeChoice.choice instanceof IntegerValue);
	}

	public static Integer getValueAsInteger(Value n) {
		if (!isIntegerValue(n)) {
			return null;
		}
		IntegerValue iv = (IntegerValue) getBuiltinValue(n).nodeChoice.choice;
		Integer result = new Integer(iv.signedNumber.nodeToken.tokenImage);
		if (iv.signedNumber.nodeOptional.present()) {
			result = -result;
		}
		return result;
	}
}
