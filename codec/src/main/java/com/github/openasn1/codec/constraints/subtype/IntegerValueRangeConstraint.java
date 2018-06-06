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
package com.github.openasn1.codec.constraints.subtype;

/**
 * @author Marc Weyland
 *
 */
public class IntegerValueRangeConstraint extends ValueRangeConstraint<Integer> implements Cloneable {
	public IntegerValueRangeConstraint() {
		
	}

	public IntegerValueRangeConstraint(Integer lowerBound, Integer upperBound) {
		super(lowerBound, upperBound, false, false);
	}

	public IntegerValueRangeConstraint(Extrema lowerBoundExtrema, Integer upperBound) {
		super(lowerBoundExtrema, upperBound, false, false);		
	}

	public IntegerValueRangeConstraint(Integer lowerBound, Extrema upperBoundExtrema) {
		super(lowerBound, upperBoundExtrema, false, false);
	}

	public IntegerValueRangeConstraint(Extrema lowerBoundExtrema, Extrema upperBoundExtrema) {
		super(lowerBoundExtrema, upperBoundExtrema, false, false);
	}

	public IntegerValueRangeConstraint(Integer lowerBound, Integer upperBound, boolean lowerBoundExcluded, boolean upperBoundExcluded) {
		super(lowerBound, upperBound, lowerBoundExcluded, upperBoundExcluded);
	}

	public IntegerValueRangeConstraint(Extrema lowerBoundExtrema, Integer upperBound, boolean lowerBoundExcluded, boolean upperBoundExcluded) {
		super(lowerBoundExtrema, upperBound, lowerBoundExcluded, upperBoundExcluded);		
	}

	public IntegerValueRangeConstraint(Integer lowerBound, Extrema upperBoundExtrema, boolean lowerBoundExcluded, boolean upperBoundExcluded) {
		super(lowerBound, upperBoundExtrema, lowerBoundExcluded, upperBoundExcluded);
	}

	public IntegerValueRangeConstraint(Extrema lowerBoundExtrema, Extrema upperBoundExtrema, boolean lowerBoundExcluded, boolean upperBoundExcluded) {
		super(lowerBoundExtrema, upperBoundExtrema, lowerBoundExcluded, upperBoundExcluded);
	}
	
	/**
	 * @see java.lang.Object#clone()
	 */
	@Override
	public IntegerValueRangeConstraint clone() throws CloneNotSupportedException {
		IntegerValueRangeConstraint clone = new IntegerValueRangeConstraint();
		
		clone.setLowerBound(getLowerBound());
		clone.setUpperBound(getUpperBound());
		clone.setLowerBoundExcluded(isLowerBoundExcluded());
		clone.setUpperBoundExcluded(isUpperBoundExcluded());
		clone.setLowerBoundExtrema(getLowerBoundExtrema());
		clone.setUpperBoundExtrema(getUpperBoundExtrema());
		
		return clone;
	}
}