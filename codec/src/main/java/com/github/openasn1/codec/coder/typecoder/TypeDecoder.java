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

import com.github.openasn1.codec.coder.ASN1Decoder;


/**
 * @author Marc Weyland
 *
 * @param <Type>
 */
public abstract class TypeDecoder<Type> extends TypeCoder {
	private Type decodedValue;

	public TypeDecoder(TypeInformation typeInformation) {
		super(typeInformation);
	}

	public Type getDecodedValue() {
		return this.decodedValue;
	}

	public void setDecodedValue(Type decodedValue) {
		this.decodedValue = decodedValue;
	}
	
	public abstract Type decode(ASN1Decoder decoder) throws IOException;

//	public TypeDecoder<Type> createStub() {
//		TypeDecoder<Type> newInstance = null;
//
//		try {
//			newInstance = (TypeDecoder<Type>)super.clone();
//		} catch (CloneNotSupportedException e) {
//			e.printStackTrace();
//		}
//		
//		newInstance.setDecodedValue(null);
//		
//		return newInstance;
//	}
}