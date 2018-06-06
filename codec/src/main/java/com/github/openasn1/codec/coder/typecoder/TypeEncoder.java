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

import com.github.openasn1.codec.coder.ASN1Encoder;


/**
 * @author Marc Weyland
 *
 * @param <Type>
 */
public abstract class TypeEncoder<Type> extends TypeCoder {
	private Type encodable;

	protected TypeEncoder() {
		
	}

	public TypeEncoder(Type encodable, TypeInformation typeInformation) {
		super(typeInformation);
		this.setEncodable(encodable);
	}

	public Type getEncodable() {
		return this.encodable;
	}

	public void setEncodable(Type encodable) {
		this.encodable = encodable;
	}
	
	public abstract void encode(ASN1Encoder encoder) throws IOException;	
}