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
 *   along with openASN.1. If not, see <http://www.gnu.com.github.licenses/>.
 * 
 */
package com.github.openasn1.codec.coder;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import com.github.openasn1.codec.coder.typecoder.EnumeratedItemTypeInformation;
import com.github.openasn1.codec.coder.typecoder.TypeEncoder;
import com.github.openasn1.codec.coder.typecoder.TypeInformation;

/**
 * @author Marc Weyland
 * 
 */
public interface ASN1Encoder {
	public void encodeAsBoolean(boolean value, TypeInformation typeInformation) throws IOException;
	public void encodeAsInteger(int value, TypeInformation typeInformation) throws IOException;
	public <T extends Enum> void encodeAsEnumerated(HashMap<T, EnumeratedItemTypeInformation> enumTypeMap, T value, TypeInformation typeInformation) throws IOException;
	// public void encodeAsReal(double value, TypeInformation typeInformation) throws IOException;
	// public void encodeAsBitString(???, TypeInformation typeInformation) throws IOException;
	public void encodeAsOctetString(byte[] values, TypeInformation typeInformation) throws IOException;
	public void encodeAsSequence(List<TypeEncoder> componentEncoderList, TypeInformation typeInformation) throws IOException;
	public <T> void encodeAsSequenceOf(TypeEncoder<T> encoderPrototype, List<T> elements, TypeInformation typeInformation) throws IOException;
	public void encodeAsSet(List<TypeEncoder> componentEncoderList, TypeInformation typeInformation) throws IOException;
	public <T> void encodeAsSetOf(TypeEncoder<T> encoderPrototype, List<T> elements, TypeInformation typeInformation) throws IOException;
	public <T> void encodeAsChoice(List<TypeEncoder> componentEncoderList, TypeInformation typeInformation) throws IOException;
	public void encodeAsObjectIdentifier(List<Integer> value, TypeInformation typeInformation) throws IOException;
	// abstract public void encodeAsRelativeOID(???, TypeInformation typeInformation) throws IOException;
	// abstract public void encodeAsEmbeddedPDV(???, TypeInformation typeInformation) throws IOException;
	// abstract public void encodeAsExternal(???, TypeInformation typeInformation) throws IOException;

	// known-multiplier character string types
		public void encodeAsNumericString(String value, TypeInformation typeInformation) throws IOException;
		public void encodeAsPrintableString(String value, TypeInformation typeInformation) throws IOException;
		public void encodeAsVisibleString(String value, TypeInformation typeInformation) throws IOException;
		public void encodeAsIA5String(String value, TypeInformation typeInformation) throws IOException;
		public void encodeAsBMPString(String value, TypeInformation typeInformation) throws IOException;
		// abstract public void encodeAsUniversalString(String value, TypeInformation typeInformation) throws IOException;

	// not known-multiplier character string types
		// abstract public void encodeAsTeletexString(String value, TypeInformation typeInformation) throws IOException;
		// abstract public void encodeAsT61String(String value, TypeInformation typeInformation) throws IOException;
		// abstract public void encodeAsVideotexString(String value, TypeInformation typeInformation) throws IOException;
		// abstract public void encodeAsGraphicString(String value, TypeInformation typeInformation) throws IOException;
		public void encodeAsGeneralString(String value, TypeInformation typeInformation) throws IOException;
		// abstract public void encodeAsUTF8String(String value, TypeInformation typeInformation) throws IOException;

	public void encodeAsNull(TypeInformation typeInformation) throws IOException;
	
	public void finish() throws IOException;
}
