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
package com.github.openasn1.codec.manager;

import com.github.openasn1.codec.coder.ASN1Decoder;
import com.github.openasn1.codec.coder.ASN1Encoder;

/**
 * The BaseCodingManager class holds an default encoder and decoder if 
 * provided. 
 *  
 * @author Marc Weyland
 * 
 */
abstract public class BaseCodingManager {
	private ASN1Encoder defaultEncoder;
	private ASN1Decoder defaultDecoder;

	public BaseCodingManager() {
	}
	
	public BaseCodingManager(ASN1Encoder encoder, ASN1Decoder decoder) {
		this.defaultEncoder = encoder;
		this.defaultDecoder = decoder;
	}

	public ASN1Decoder getDefaultDecoder() {
		return defaultDecoder;
	}

	public void setDefaultDecoder(ASN1Decoder defaultDecoder) {
		this.defaultDecoder = defaultDecoder;
	}

	public ASN1Encoder getDefaultEncoder() {
		return this.defaultEncoder;
	}

	public void setDefaultEncoder(ASN1Encoder defaultEncoder) {
		this.defaultEncoder = defaultEncoder;
	}
}
