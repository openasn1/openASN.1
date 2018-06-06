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
package com.github.openasn1.codec.coder.typecoder;

import java.io.IOException;
import java.util.HashMap;

import com.github.openasn1.codec.coder.ASN1Encoder;


/**
 * @author Marc Weyland
 *
 * @param <T>
 */
public class EnumeratedEncoder<T extends Enum> extends TypeEncoder<T> {
	private HashMap<T, EnumeratedItemTypeInformation> enumTypeMap;
	
	public EnumeratedEncoder(HashMap<T, EnumeratedItemTypeInformation> enumTypeMap, T encodable, TypeInformation typeInformation) {
		super(encodable, typeInformation);
		this.enumTypeMap = enumTypeMap;
	}
	
	private HashMap<T, EnumeratedItemTypeInformation> getEnumTypeMap() {
		return this.enumTypeMap;
	}

	@Override
	public void encode(ASN1Encoder encoder) throws IOException {
		encoder.encodeAsEnumerated(getEnumTypeMap(), getEncodable(), getTypeInformation());
	}
}
