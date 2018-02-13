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
import java.util.List;
import java.util.HashMap;

import com.github.openasn1.codec.coder.typecoder.EnumeratedItemTypeInformation;
import com.github.openasn1.codec.coder.typecoder.TypeDecoder;
import com.github.openasn1.codec.coder.typecoder.TypeInformation;

/**
 * @author Marc Weyland
 * 
 */
public interface ASN1Decoder {
	public boolean decodeBoolean(TypeInformation typeInformation) throws IOException ;
	public int decodeInteger(TypeInformation typeInformation) throws IOException ;
	public <T extends Enum> T decodeEnumerated(HashMap<T, EnumeratedItemTypeInformation> enumTypeMap, TypeInformation typeInformation) throws IOException;

	public byte[] decodeOctetString(TypeInformation typeInformation) throws IOException;

	public void decodeSequence(List<TypeDecoder> componentDecoderList, TypeInformation typeInformation) throws IOException;
	public <T> List<T> decodeSequenceOf(TypeDecoder<T> decoderPrototype, TypeInformation typeInformation) throws IOException;

	public void decodeSet(List<TypeDecoder> componentDecoderList, TypeInformation typeInformation) throws IOException;
	public <T> List<T> decodeSetOf(TypeDecoder<T> decoderPrototype, TypeInformation typeInformation) throws IOException;

	public void decodeChoice(List<TypeDecoder> componentDecoderList, TypeInformation typeInformation) throws IOException;

	public List<Integer> decodeObjectIdentifier(TypeInformation typeInformation) throws IOException ;

	public String decodeNumericString(TypeInformation typeInformation) throws IOException ;
	public String decodePrintableString(TypeInformation typeInformation) throws IOException ;
	public String decodeVisibleString(TypeInformation typeInformation) throws IOException ;
	public String decodeIA5String(TypeInformation typeInformation) throws IOException ;
	public String decodeBMPString(TypeInformation typeInformation) throws IOException ;
	public String decodeGeneralString(TypeInformation typeInformation) throws IOException ;

	public Class<Void> decodeNull(TypeInformation typeInformation) throws IOException;

	public void finish() throws IOException;
}