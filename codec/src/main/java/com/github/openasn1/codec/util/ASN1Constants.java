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
package com.github.openasn1.codec.util;

/**
 * @author Marc Weyland
 *
 */
public final class ASN1Constants {
	public static final int SIZE_1K = 1024;
	public static final int SIZE_2K = 2048;
	public static final int SIZE_4K = 4096;
	public static final int SIZE_8K = 8192;
	public static final int SIZE_16K = 16384;
	public static final int SIZE_32K = 32768;
	public static final int SIZE_48K = 49152;
	public static final int SIZE_64K = 65536;
	public static final int SIZE_ONE_OCTET = 256;
	public static final int SIZE_TWO_OCTETS = SIZE_ONE_OCTET << 8;
	public static final int SIZE_THREE_OCTETS = SIZE_ONE_OCTET << 16;
	public static final int SIZE_FOUR_OCTETS = SIZE_ONE_OCTET << 24;
	public static final int BITPATTERN_01000000 = 64;
	public static final int BITPATTERN_10000000 = 128;
	public static final int BITPATTERN_11000000 = 192;
	public static final int BITPATTERN_00001111 = 15;	
	public static final int BITPATTERN_00111111 = 63;
	public static final int BITPATTERN_11111111 = 255;	
	public static final int BITPATTERN_01111111 = 127;	
}
