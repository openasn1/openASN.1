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


/**
 * @author Marc Weyland
 *
 */
public class StaticBitField extends BitFieldImpl {
	protected int[] bits = null;
	protected int size = 0;
	
	private StaticBitField() {
		
	}

	public StaticBitField(int size) {
		if (size < 1) {
			throw new IllegalArgumentException("The size of the bitfield has to be >= 1");
		}

		this.bits = new int[(int)Math.ceil(size / 32.0f)];
		this.size = size;
	}
	
	public StaticBitField(BitField bitField) {
		this(bitField.getSize());
		
		for (int i=0; i<bitField.getSize(); i++) {
			setBit(i, bitField.getBit(i));
		}
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
			int inverseIndex = (i-startBit);
			result |= getBit(endBit-inverseIndex) << inverseIndex;
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
		
		for (int i=startBit; i <= endBit; i++) {
			if ((value & (1 << (endBit - i))) == 0) {
				setBit(i, 0);	
			} else {
				setBit(i, 1);
			}
		}
	}
	
	public void setBit(int index, int value) {
		if ((index < 0) || (index >= getSize())) { 
			throw new IndexOutOfBoundsException("The index may not be negative or greater or equal than the bitfield size");
		}
		if ((value < 0) || (value > 1)) {
			throw new IllegalArgumentException("The bit value may only be '0' or '1'");
		}

		if (value == 0) {
			this.bits[getArrayPosition(index)] &= ~(1 << getHighOrderBitPosition(index));
		} else {
			this.bits[getArrayPosition(index)] |= (1 << getHighOrderBitPosition(index));
		}
	}
	
	public int getBit(int index) {
		if ((index < 0) || (index >= getSize())) { 
			throw new IndexOutOfBoundsException("The index may not be negative or greater or equal than the bitfield size");
		}
		
		if ((this.bits[getArrayPosition(index)] & (1 << getHighOrderBitPosition(index))) == 0) {
			return 0;
		}
		
		return 1;
	}
	
	protected int getArrayPosition(int index) {
		return index / 32;
	}

	protected int getBitPosition(int index) {
		return index % 32;
	}

	protected int getHighOrderBitPosition(int index) {
		return 31 - getBitPosition(index);
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
	
	@Override
	public StaticBitField clone() {
		StaticBitField bitField = new StaticBitField();
		
		bitField.bits = new int[this.bits.length];
		System.arraycopy(this.bits, 0, bitField.bits, 0, this.bits.length);
		
		bitField.size = getSize();
		
		return bitField;
	}
}