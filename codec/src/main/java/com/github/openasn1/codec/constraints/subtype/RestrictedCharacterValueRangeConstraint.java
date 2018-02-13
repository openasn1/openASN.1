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
package com.github.openasn1.codec.constraints.subtype;


/**
 * @author Marc Weyland
 *
 */
public class RestrictedCharacterValueRangeConstraint extends ValueRangeConstraint<Character> {
	public RestrictedCharacterValueRangeConstraint(Character lowerBound, Character upperBound) {
		this(lowerBound, upperBound, false, false);
	}
	
	public RestrictedCharacterValueRangeConstraint(Character lowerBound, Extrema upperBoundExtrema, boolean lowerBoundExcluded, boolean upperBoundExcluded) {
		super(lowerBound, upperBoundExtrema, lowerBoundExcluded, upperBoundExcluded);
	}

	public RestrictedCharacterValueRangeConstraint(Extrema lowerBoundExtrema, Character upperBound, boolean lowerBoundExcluded, boolean upperBoundExcluded) {
		super(lowerBoundExtrema, upperBound, lowerBoundExcluded, upperBoundExcluded);
	}

	public RestrictedCharacterValueRangeConstraint(Extrema lowerBoundExtrema, Extrema upperBoundExtrema, boolean lowerBoundExcluded, boolean upperBoundExcluded) {
		super(lowerBoundExtrema, upperBoundExtrema, lowerBoundExcluded, upperBoundExcluded);
	}

	public RestrictedCharacterValueRangeConstraint(Character lowerBound, Character upperBound, boolean lowerBoundExcluded, boolean upperBoundExcluded) {
		/**
		 * NOTE: The minimum and maximum value is the minimum rsp. maximum character value.
		 * 		 ValueConstraints applied to IA5Strings (BMPString, NumericString, 
		 * 		 PrintableString, VisibleString, UTF8String, UniversalString) 
		 * 		 will be modeled like a serial constraint
		 * 		 IA5StringConstraint ResrtictedCharacterValueRangeConstraint.  
		 */
		super(lowerBound, upperBound, lowerBoundExcluded, upperBoundExcluded);
	}
}