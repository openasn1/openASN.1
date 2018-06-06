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
package com.github.openasn1.codec.util.converter;

/**
 * @author Marc Weyland
 *
 */
public class HexConverter {
	private static final String[] digits = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F" };

	public static byte[] decode(final String hexString) {
		if (hexString.length() == 0) {
			final byte[] nullByte = {0};
			return nullByte;
		}
		
		final byte[] result = new byte[(int)Math.ceil(hexString.length() / 2.0f)];
		int index = 0;
		
		final char[] chars = hexString.toUpperCase().toCharArray();
		
		for (int i = 0; i < chars.length; i+=2) {
			result[index] = (byte)((HexConverter.toByte(chars[i]) << 4) | HexConverter.toByte(chars[i+1])); 
			index++;
		}
		
		return result;
	}
	
	public static String encode(final byte item) {
		return HexConverter.toHex(item);
	}
	
	public static String encode(final byte[] byteArray) {
		final StringBuffer buffer = new StringBuffer();

		for (final byte item : byteArray) {
			buffer.append(HexConverter.toHex(item));
		}

		return buffer.toString();
	}
	
	private static byte toByte(final char item) {
		if ((item >= '0') && (item <= '9')) {
			return (byte)(item - '0');  
		}
		
		return (byte)(item - 'A' + 10);
	}

	private static String toHex(final byte item) {
		int value = item;

		value &= 0x000000ff;

		return HexConverter.digits[(byte) ((value & 0xf0) >> 4)] + HexConverter.digits[(byte) (value & 0x0f)];
	}
}