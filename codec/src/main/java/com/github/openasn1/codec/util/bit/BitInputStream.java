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
public interface BitInputStream {
	/**
	 * Returns the amount of pending bits currently not read.
	 *  
	 * @return int the amount of pending bits
	 */
	public abstract int getPendingBits();	

	public abstract int[] readBytes(int bytes) throws IOException;

	public abstract int readByte() throws IOException;

	public abstract int readBits(int bits) throws IOException;

	public abstract int readBit() throws IOException;

	public abstract void close() throws IOException;
	
	public void skipRemainingBits();
}