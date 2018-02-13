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
package com.github.openasn1.codec.util.converter;

/**
 * @author Marc Weyland
 *
 */
public class BitConverter {
	public static String encode(byte value) {
		StringBuffer buffer = new StringBuffer();
		
		for (int i = 7; i >= 0; i--) {
			if ((value & (1 << i)) > 0) {
				buffer.append("1");
			} else {
				buffer.append("0");
			}
		}
		
		return buffer.toString();
	}

	public static String encode(byte[] valueArray) {
		StringBuffer buffer = new StringBuffer();

		for (byte value : valueArray) {
			buffer.append(encode(value));
		}
		
		return buffer.toString();
	}

	private static byte getByteFromString(String bitString) {
		byte result = 0;
		int position = 0;
		
		for (int i = bitString.length() - 1; i >= 0; i--) {
			if (bitString.charAt(i) == '1') {
				result |= 1 << position;
			}
			position++;
		}
		
		return result;
	}

	public static byte[] decode(String bitString) {
		if (bitString.length() == 0) {
			byte[] nullByte = {0};
			return nullByte;
		}
		
		int size = (int)Math.ceil(bitString.length() / 8.0f);
		byte[] result = new byte[size];
		
		for (int i = 0; i < size; i++) {
			result[i] = getByteFromString(bitString.substring(i*8, Math.min(i*8 + 8, bitString.length())));
		}
		
		return result;
	}
}
