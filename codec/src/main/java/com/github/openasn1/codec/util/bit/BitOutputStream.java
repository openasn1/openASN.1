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

/**
 * @author Marc Weyland
 *
 */
public interface BitOutputStream {
	/**
	 * Writes an array of bytes.
	 *  
	 * @param byteArray is byteArray
	 * @throws IOException is the exception
	 */
	public abstract void writeBytes(byte[] byteArray) throws IOException;

	/**
	 * Writes only a part of a given byte.
	 * 
	 * @param byteData is byteData
	 * @param start	The start of the byte part. start=0 means the high order bit of the byte 
	 * @param end	The end of the byte part. end=7 means the low order bit of the byte 
	 * @throws IOException is the exception
	 */
	public abstract void writeBytePartly(byte byteData, int start, int end)
			throws IOException;

	/**
	 * Writes a whole byte
	 * 
	 * @param byteData is byteData
	 * @throws IOException is the exception
	 */
	public abstract void writeByte(byte byteData) throws IOException;

	/**
	 * Writes only as much bits as neccessary to encode the value of the byte
	 *  
	 * @param byteData is byteData
	 * @throws IOException is the exception
	 */
	public abstract void writeByteCompact(byte byteData) throws IOException;

	/**
	 * Writes the bits from a BitField object
	 *  
	 * @param bitField is bitfield
	 * @throws IOException is the exception
	 */
	public abstract void writeBits(BitField bitField) throws IOException;

	/**
	 * Writes a single bit.
	 * 
	 * Values might be '1' or '0'.
	 *  
	 * @param bit is int
	 * @throws IOException is the exception
	 */	
	public abstract void writeBit(int bit) throws IOException;

	/**
	 * Flushes the buffer to be written out.
	 *  
	 * @throws IOException is the exception
	 */
	public abstract void flush() throws IOException;

	/**
	 * Closes the stream.
	 *  
	 * @throws IOException is the exception
	 */
	public abstract void close() throws IOException;
	
	/**
	 * Returns the amount of pending bits currently not written.
	 *  
	 * @return int the amount of pending bits
	 */
	public abstract int getPendingBits();
}