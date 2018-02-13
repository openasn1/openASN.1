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
package com.github.openasn1.codec.constraints.character;

import java.util.HashMap;

/**
 * @author Marc Weyland
 *
 */
public class PrintableStringInfo extends RestrictedCharacterStringInfo {
	private static PrintableStringInfo instance = null;
	
	private char[] characterTable = {'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z',
									 'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z',
									 '0','1','2','3','4','5','6','7','8','9',
									 ' ','\'','(',')','+',',','-','.','/',':','=','?'};
									 
	private HashMap<Character, Integer> indexTable = new HashMap<Character, Integer>(characterTable.length);
	
	private PrintableStringInfo() {
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
		return 73;
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
			instance = new PrintableStringInfo();
		}
		return instance;
	}
}
