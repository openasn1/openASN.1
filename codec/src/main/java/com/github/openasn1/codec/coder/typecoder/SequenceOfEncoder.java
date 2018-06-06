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
import java.util.List;

import com.github.openasn1.codec.coder.ASN1Encoder;


/**
 * @author Marc Weyland
 *
 * @param <T>
 */
public class SequenceOfEncoder<T> extends TypeEncoder<List<T>> {
	private TypeEncoder<T> encoderPrototype = null;
	
	public SequenceOfEncoder(TypeEncoder<T> encoderPrototype, List<T> encodables, TypeInformation typeInformation) {
		super(encodables, typeInformation);
		
		setEncoderPrototype(encoderPrototype);
	}

	private TypeEncoder<T> getEncoderPrototype() {
		return this.encoderPrototype;
	}

	private void setEncoderPrototype(TypeEncoder<T> encoderPrototype) {
		this.encoderPrototype = encoderPrototype;
	}

	@Override
	public void encode(ASN1Encoder encoder) throws IOException {
		encoder.encodeAsSequenceOf(getEncoderPrototype(), getEncodable(), getTypeInformation());
	}
}