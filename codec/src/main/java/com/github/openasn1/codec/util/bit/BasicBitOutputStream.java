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

import java.io.IOException;
import java.io.OutputStream;

/**
 * @author Marc Weyland
 *
 */
public class BasicBitOutputStream implements BitOutputStream {
	private OutputStream out;
	private int buffer;
	private int bitCount;

	public BasicBitOutputStream(OutputStream out) {
		this.out = out;
	}
	
	public int getPendingBits() {
		return this.bitCount;
	}

	public int getPaddingBits() {
		return (8 - getPendingBits()) % 8;
	}
	
	/**
	 * @see com.github.openasn1.codec.util.bit.BitOutputStream#writeBytes(byte[])
	 */
	synchronized public void writeBytes(byte[] byteArray) throws IOException {
		for (byte byteData : byteArray) {
			writeByte(byteData);
		}
	}
	
	/**
	 * @see com.github.openasn1.codec.util.bit.BitOutputStream#writeBytePartly(byte, int, int)
	 */
	synchronized public void writeBytePartly(byte byteData, int start, int end) throws IOException {
		if (start < 0 || end > 7 || start > end) {
			throw new RuntimeException("Invalid byte part position (" + start + ", " + end + ")");
		}
		
		for (int i=end; i>=start; i--) {
			if ((byteData & (1 << i)) > 0) {
				writeBit(1);
			} else {
				writeBit(0);
			}
		}
	}

	/**
	 * @see com.github.openasn1.codec.util.bit.BitOutputStream#writeByte(byte)
	 */
	synchronized public void writeByte(byte byteData) throws IOException {
		if (this.bitCount == 0) {
			this.buffer = byteData;
			this.bitCount = 8;
			flushBuffer();
			return;
		}
		
		for (int i = 7; i>=0; i--) {
			writeBit((byteData & (1 << i)) >> i);
		}
	}

	/**
	 * @see com.github.openasn1.codec.util.bit.BitOutputStream#writeByteCompact(byte)
	 */
	synchronized public void writeByteCompact(byte byteData) throws IOException {
		for (int i = 0; i <= Math.ceil(Math.log(byteData)); i--) {
			writeBit((byteData & (1 << i)) >> i);
		}
	}

	/**
	 * @see com.github.openasn1.codec.util.bit.BitOutputStream#writeBits(com.github.openasn1.codec.util.bit.BitField)
	 */
	synchronized public void writeBits(BitField bitField) throws IOException {
		for (int i = 0; i < bitField.getSize(); i++) {
			writeBit(bitField.getBit(i));
		}
	}

	/**
	 * @see com.github.openasn1.codec.util.bit.BitOutputStream#writeBit(int)
	 */
	synchronized public void writeBit(int bit) throws IOException {
		if (this.out == null)
			throw new IOException("Already closed");

		if (bit != 0 && bit != 1) {
			throw new IOException(bit + " is not a bit");
		}

		this.buffer |= bit << (7-this.bitCount);
		this.bitCount++;

		if (this.bitCount == 8) {
			flushBuffer();
		}
	}
	
	/**
	 * Flushes the internal buffer.
	 *  
	 * @throws IOException
	 */
	private void flushBuffer() throws IOException {
		if (this.bitCount > 0) {
			this.out.write((byte) this.buffer);
			this.bitCount = 0;
			this.buffer = 0;
		}
	}

	/**
	 * @see com.github.openasn1.codec.util.bit.BitOutputStream#flush()
	 */
	public void flush() throws IOException {
		flushBuffer();
		this.out.flush();
	}

	/**
	 * @see com.github.openasn1.codec.util.bit.BitOutputStream#close()
	 */
	public void close() throws IOException {
		flush();
		this.out.close();
		this.out = null;
	}
}