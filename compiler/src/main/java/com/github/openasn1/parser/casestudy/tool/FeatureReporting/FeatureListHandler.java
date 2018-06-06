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
package com.github.openasn1.parser.casestudy.tool.FeatureReporting;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Clayton Hoss
 * 
 */
public class FeatureListHandler {
	private Map<FeatureList, Integer> featureList = new HashMap<FeatureList, Integer>();

	/**
	 * 
	 */
	public FeatureListHandler() {
		for (FeatureList f : FeatureList.values()) {
			getFeatureList().put(f, new Integer(0));
		}
	}

	public void add(FeatureListHandler summand) {
		for (FeatureList f : FeatureList.values()) {
			Integer summand1 = this.getFeatureList().get(f);
			Integer summand2 = summand.getFeatureList().get(f);
			this.getFeatureList().put(f, new Integer(summand1 + summand2));
		}

	}

	public void incFeatureCount(FeatureList f) {
		Integer i = getFeatureList().get(f);
		i = new Integer(i.intValue() + 1);
		getFeatureList().put(f, i);
	}
	
	public Integer getFeature(FeatureList f) {
		return getFeatureList().get(f);
	}

	/**
	 * @return the featureList
	 */
	private Map<FeatureList, Integer> getFeatureList() {
		return this.featureList;
	}

}
