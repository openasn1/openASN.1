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
package com.github.openasn1.codec.util.bit;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author Marc Weyland
 *
 */
public class BasicBitInputStream implements BitInputStream {
	private InputStream in;

	private int buffer;

	private int nextBit = 8;

	public BasicBitInputStream(InputStream in) {
		this.in = in;
	}
	
	public int getPendingBits() {
		return 8-this.nextBit;
	}	

	/**
	 * @see com.github.openasn1.codec.util.bit.BitInputStreamInterface#readBytes(int)
	 */
	synchronized public int[] readBytes(int bytes) throws IOException {
		if (this.in == null) {
			throw new IOException("Already closed");
		}

		int[] byteData = new int[bytes-1];
		
		for (int i = 0; i < bytes; i++) {
			byteData[i] = readByte();
		}
		
		return byteData;
	}

	/**
	 * @see com.github.openasn1.codec.util.bit.BitInputStreamInterface#readByte()
	 */
	synchronized public int readByte() throws IOException {
		if (this.in == null) {
			throw new IOException("Already closed");
		}

		if (this.nextBit == 8) {
			this.buffer = this.in.read();

			if (this.buffer == -1)
				throw new EOFException();

			return this.buffer;
		}

		int value = 0;
		
		for (int i = 7; i>=0; i--) {
			value |= readBit() << i;
		}
		return value;
	}
	
	/**
	 * @see com.github.openasn1.codec.util.bit.BitInputStreamInterface#readBits(int)
	 */
	synchronized public int readBits(int bits) throws IOException {
		if (this.in == null) {
			throw new IOException("Already closed");
		}
		if (bits > 32) {
			throw new RuntimeException("A maximum of 32 bits must be read");
		}

		if (bits == 8) {
			return readByte();
		}
		
		byte value = 0;
		
		for (int i=0; i<bits; i++) {
			value |= readBit() << (bits-1-i);
		}

		return value;
	}
	
	/**
	 * @see com.github.openasn1.codec.util.bit.BitInputStreamInterface#readBit()
	 */
	synchronized public int readBit() throws IOException {
		if (this.in == null)
			throw new IOException("Already closed");

		if (this.nextBit == 8) {
			this.buffer = this.in.read();

			if (this.buffer == -1)
				throw new EOFException();

			this.nextBit = 0;
		}

		int bit = this.buffer & (1 << (7-this.nextBit));
		this.nextBit++;

		bit = bit == 0 ? 0 : 1;

		return bit;
	}
	
	synchronized public void skipRemainingBits() {
		this.nextBit = 8;
	}

	/**
	 * @see com.github.openasn1.codec.util.bit.BitInputStreamInterface#close()
	 */
	public void close() throws IOException {
		this.in.close();
		this.in = null;
	}
}
