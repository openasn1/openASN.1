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
public class EmptyBitField extends BitFieldImpl {
	private static EmptyBitField instance = null;

	private EmptyBitField() { }
	
	public static EmptyBitField getInstance() { 
		if (instance == null) {
			instance = new EmptyBitField();
		}
		
		return instance;
	}

	public int getSize() {
		return 0;
	}
	
	public int getValue(int startBit, int endBit) {
		throw new RuntimeException("An EmptyBitField has no value!");
	}

	public int getValue() {
		throw new RuntimeException("An EmptyBitField has no value!");
	}

	public void setValue(int startBit, int endBit, int value) {
		throw new RuntimeException("An EmptyBitField has no value!");
	}
	
	public void setBit(int index, int value) {
		throw new RuntimeException("Bits cannot be set on an EmptyBitField!");
	}
	
	public int getBit(int index) {
		throw new RuntimeException("Bits cannot be retrieved from an EmptyBitField!");
	}
	
	@Override
	public String toString() {
		return "";
	}
	
	@Override
	public EmptyBitField clone() {
		return new EmptyBitField();
	}
}