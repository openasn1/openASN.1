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

import java.util.ArrayList;

/**
 * @author Marc Weyland
 *
 */
public class DynamicBitField extends BitFieldImpl {
	private static int FIELD_SIZE = 32; 
	protected ArrayList<StaticBitField> fieldList = new ArrayList<StaticBitField>();
	protected int size = 0;

	public DynamicBitField() {
	}
	
	public DynamicBitField(BitField bitField) {
		appendBits(bitField);
	}
	
	public int getSize() {
		return this.size;
	}
	
	public int getValue() {
		return getValue(0, getSize()-1);
	}
	
	public int getValue(int startBit, int endBit) {
		if (startBit < 0) {
			throw new IndexOutOfBoundsException("The bit indices may not be negative");
		}

		if (startBit > endBit) {
			throw new IllegalArgumentException("The startBit may not be larger than the endBit");
		}

		if (endBit >= getSize()) {
			throw new IndexOutOfBoundsException("The bit indices may not be greater than the size of the bit field");
		}

		if ((startBit - endBit) > 31) {
			throw new RuntimeException("The size exceeds the scale of an integer");
		}
		
		int result = 0;
		
		for (int i = startBit; i <= endBit; i++) {
			int offsetIndex = (i-startBit);
			result |= getBit(endBit-offsetIndex) << offsetIndex;
		}
		
		return result;
	}

	public void setValue(int startBit, int endBit, int value) {
		if (startBit < 0) {
			throw new IndexOutOfBoundsException("The bit indices may not be negative");
		}

		if (endBit >= getSize()) {
			throw new IndexOutOfBoundsException("The bit indices may not be greater than the size of the bit field");
		}
		
		if ((endBit - startBit) > 31) {
			throw new RuntimeException("Range exceeds the scale of an integer");
		}

		if (endBit < startBit) {
			throw new RuntimeException("The endBit=" + endBit + " may not be smaller than the startBit=" + startBit);
		}
		
		for (int i=startBit; i <= endBit; i++) {
			if ((value & (1 << (endBit - i))) == 0) {
				setBit(i, 0);
			} else {
				setBit(i, 1);
			}
		}
	}
	
	public void setBit(int index, int value) {
		getFieldByIndex(index).setBit(getFieldBitPosition(index), value);
	}
	
	public int getBit(int index) {
		return getFieldByIndex(index).getBit(getFieldBitPosition(index));
	}
	

	
	private StaticBitField getFieldByIndex(int index) {
		StaticBitField bitField = null;
		
		try {
			bitField = this.fieldList.get(getFieldIndex(index));
		} catch (RuntimeException e) {
			throw new RuntimeException("The specified index is invalid. Maybe there are no bits in the bitfield");
		}
		
		return bitField;
	}
	
	private int getFieldIndex(int index) {
		return index / FIELD_SIZE;
	}
	
	private int getFieldBitPosition(int index) {
		return index % FIELD_SIZE;
	}
	
	
	@Override
	public String toString() {
		StringBuffer output = new StringBuffer();
		
		for (int i = 0; i < getSize(); i++) {
			if (getBit(i) == 1) {
				output.append("1");
			} else {
				output.append("0");
			}
		}
		
		return output.toString();
	}
	
	public void appendValue(int value, int bits) {
		if (bits < 0) {
			throw new IndexOutOfBoundsException("Only a positive amount of bits may be appended");
		}

		if (bits > 32) {
			throw new IndexOutOfBoundsException("An integer only has 32 bits");
		}
		
		appendBits(0, bits);
		setValue(getSize()-bits, getSize()-1, value);
	}
	
	public void appendBit(int bitValue) {
		ensureCapacity(getSize()+1);
		this.size++;
		setBit(getSize()-1, bitValue);
	}

	public void appendBits(int bitValue, int bits) {
		for (int i=0; i<bits; i++) {
			appendBit(bitValue);
		}
	}
	
	public void appendBits(BitField bitField) {
		for (int i=0; i < bitField.getSize(); i++) {
			appendBit(bitField.getBit(i));
		}
	}
	
	public void prependBit(int value) {
		DynamicBitField bitField = new DynamicBitField();
		bitField.appendBit(value);
		bitField.appendBits(this);
		this.fieldList = bitField.fieldList;
		this.size = bitField.size;
	}

	public void prependBits(BitField bitField) {
		DynamicBitField newBitField = new DynamicBitField();
		newBitField.appendBits(bitField);
		newBitField.appendBits(this);
		this.fieldList = newBitField.fieldList;
		this.size = newBitField.size;
	}

	private void ensureCapacity(int size) {
		if (size < getSize()) {
			throw new IllegalArgumentException("The capacity may not be smaller as the current size");
		}
		
		if (size <= this.fieldList.size() * FIELD_SIZE) {
			return;
		}
		
		int sizeDifference = size - getSize();
		int newBitFields = (int)Math.ceil(sizeDifference / (float)FIELD_SIZE);
		
		for (int i=0; i<newBitFields; i++) {
			this.fieldList.add(new StaticBitField(FIELD_SIZE));
		}
	}
	
	@Override
	public DynamicBitField clone() {
		return new DynamicBitField(this);
	}
}