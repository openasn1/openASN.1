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
package com.github.openasn1.compiler.omast;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Clayton Hoss
 *
 */
public class EnumASN1Type extends AbstractASN1Type {

	private Map<String, Integer> enums = new LinkedHashMap<String, Integer>();

	/**
	 * @see com.github.openasn1.compiler.omast.OMVisitable#accept(com.github.openasn1.compiler.omast.OMVisitor)
	 */
	public void accept(OMVisitor vis){
		vis.visit(this);
	}

	/**
	 * @return the enums
	 */
	public Map<String, Integer> getEnums() {
		return this.enums;
	}

	/**
	 * @param key
	 * @param value
	 * @return
	 * @see java.util.HashMap#put(java.lang.Object, java.lang.Object)
	 */
	public void addEnum(String key, Integer value) {
		this.enums.put(key, value);
	}

	/**
	 * @param enums the enums to set
	 */
	public void setEnums(Map<String, Integer> enums) {
		this.enums = enums;
	}


}
