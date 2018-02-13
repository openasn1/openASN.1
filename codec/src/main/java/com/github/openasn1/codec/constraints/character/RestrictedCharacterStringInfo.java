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

import java.security.InvalidParameterException;

/**
 * @author Marc Weyland
 *
 */
abstract public class RestrictedCharacterStringInfo {
	abstract public int getLowerIndex();
	abstract public int getUpperIndex();
	abstract protected int getCharacterTableCharacterIndex(char character);
	abstract protected char getCharacterTableCharacterFromIndex(int index);
	
	public int getAlphabetSize() {
		return getUpperIndex() - getLowerIndex() + 1;
	}

	public int getCharacterIndex(char character) {
		int characterIndex = getCharacterTableCharacterIndex(character);
		
		if (characterIndex < getLowerIndex() || characterIndex > getUpperIndex()) {
			throw new InvalidParameterException("Invalid character supplied");
		}
		
		return characterIndex;
	}
	
	public char getCharacterFromIndex(int index) {
		if (index < getLowerIndex() || index > getUpperIndex()) {
			throw new InvalidParameterException("Invalid index supplied");
		}

		return getCharacterTableCharacterFromIndex(index);
	}

	public int getNormalizedCharacterIndex(char character) {
		return getCharacterIndex(character) - getLowerIndex();
	}
}
