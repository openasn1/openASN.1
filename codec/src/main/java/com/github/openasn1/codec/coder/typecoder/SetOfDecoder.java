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
package com.github.openasn1.codec.coder.typecoder;

import java.io.IOException;
import java.util.List;

import com.github.openasn1.codec.coder.ASN1Decoder;


/**
 * @author Marc Weyland
 *
 * @param <T> is a generic set type
 */
public class SetOfDecoder<T> extends TypeDecoder<List<T>> {
	private TypeDecoder<T> decoderPrototype = null;
	
	public SetOfDecoder(TypeDecoder<T> decoderPrototype, TypeInformation typeInformation) {
		super(typeInformation);
		
		setDecoderPrototype(decoderPrototype);
	}	

	private TypeDecoder<T> getDecoderPrototype() {
		return this.decoderPrototype;
	}

	private void setDecoderPrototype(TypeDecoder<T> decoderPrototype) {
		this.decoderPrototype = decoderPrototype;
	}

	@Override
	public List<T> decode(ASN1Decoder decoder) throws IOException {
		return decoder.decodeSetOf(getDecoderPrototype(), getTypeInformation());
	}
}