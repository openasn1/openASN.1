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
package com.github.openasn1.codec.util.bit;

/**
 * @author Marc Weyland
 *
 */
abstract public class BitFieldImpl implements BitField {
	private boolean isOctetAligned = false;
	private int encodingLength = 0;

	public boolean isOctetAligned() {
		return this.isOctetAligned;
	}

	public void setOctetAligned(boolean isOctetAligned) {
		this.isOctetAligned = isOctetAligned;
	}
	
	public int getEncodingLength() {
		return this.encodingLength;
	}
	
	public void setEncodingLength(int length) {
		this.encodingLength = length;
	}

}