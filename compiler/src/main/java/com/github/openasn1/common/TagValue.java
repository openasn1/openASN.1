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
package com.github.openasn1.common;

public class TagValue {
	private int index;

	private TagClassEnum tagClass;

	private TaggingModeEnum taggingMode;

	public TagValue(TaggingModeEnum taggingMode, TagClassEnum tagClass,
			int index) {
		if (index < 0) {
			throw new RuntimeException("The tag index may not be negative!");
		}

		this.index = index;
		this.tagClass = tagClass;
		this.taggingMode = taggingMode;
	}

	public int getIndex() {
		return this.index;
	}

	@Override
	public String toString() {
		return this.tagClass + " " + this.index + " " + this.taggingMode;
	}

	public TagClassEnum getTagClass() {
		return this.tagClass;
	}

	public TaggingModeEnum getTaggingMode() {
		return this.taggingMode;
	}
}