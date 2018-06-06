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
package com.github.openasn1.codec.constraints.character;

import java.util.HashMap;

/**
 * Represents the NumericString character string information
 * 
 * @author Marc Weyland
 * 
 * @see "X.680-0207 37.2"
 */
public class NumericStringInfo extends RestrictedCharacterStringInfo {
	private static NumericStringInfo instance = null;
	
	private char[] characterTable = {'0','1','2','3','4','5','6','7','8','9',
									 ' '};
									 
	private HashMap<Character, Integer> indexTable = new HashMap<Character, Integer>(characterTable.length);
	
	private NumericStringInfo() {
		for (int i = 0; i < characterTable.length; i++) {
			indexTable.put(characterTable[i], i);
		}
	}
	
	@Override
	public int getLowerIndex() {
		return 0;
	}

	@Override
	public int getUpperIndex() {
		return 10;
	}

	@Override
	protected int getCharacterTableCharacterIndex(char character) {
		if (indexTable.containsKey(character)) {
			return indexTable.get(character);
		}
		return -1;
	}
	
	@Override
	protected char getCharacterTableCharacterFromIndex(int index) {		
		return (char)characterTable[index];
	}	

	public static RestrictedCharacterStringInfo getInstance() {
		if (instance == null) {
			instance = new NumericStringInfo();
		}
		return instance;
	}
}
