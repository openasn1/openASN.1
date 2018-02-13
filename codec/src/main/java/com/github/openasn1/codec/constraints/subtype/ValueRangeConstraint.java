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

import com.github.openasn1.codec.constraints.ConstraintImpl;
import com.github.openasn1.codec.constraints.visitor.ConstraintVisitor;

/**
 * Value range constraint
 *  
 * @author Marc Weyland
 * 
 * @see "X.680-0207 47.4"
 */
abstract public class ValueRangeConstraint<T> extends ConstraintImpl {
	public enum Extrema {
		MINIMUM, MAXIMUM
	}
	
	private Extrema lowerBoundExtrema = null;
	private Extrema upperBoundExtrema = null;

	private T lowerBound;
	private T upperBound;
	
	private boolean lowerBoundExcluded;
	private boolean upperBoundExcluded;

	/**
	 * Instantiating a value range with no parameters means that we have
	 * an empty range.
	 */
	protected ValueRangeConstraint() {
		
	}
	
	public ValueRangeConstraint(T lowerBound, T upperBound, boolean lowerBoundExcluded, boolean upperBoundExcluded) {
		this.lowerBoundExcluded = lowerBoundExcluded;
		this.upperBoundExcluded = upperBoundExcluded;
		this.lowerBound = lowerBound;
		this.upperBound = upperBound;
	}

	public ValueRangeConstraint(Extrema lowerBoundExtrema, T upperBound, boolean lowerBoundExcluded, boolean upperBoundExcluded) {
		this.lowerBoundExcluded = lowerBoundExcluded;
		this.upperBoundExcluded = upperBoundExcluded;
		this.lowerBoundExtrema = lowerBoundExtrema;
		this.upperBound = upperBound;
	}

	public ValueRangeConstraint(T lowerBound, Extrema upperBoundExtrema, boolean lowerBoundExcluded, boolean upperBoundExcluded) {
		this.lowerBoundExcluded = lowerBoundExcluded;
		this.upperBoundExcluded = upperBoundExcluded;
		this.lowerBound = lowerBound;
		this.upperBoundExtrema = upperBoundExtrema;
	}

	public ValueRangeConstraint(Extrema lowerBoundExtrema, Extrema upperBoundExtrema, boolean lowerBoundExcluded, boolean upperBoundExcluded) {
		this.lowerBoundExcluded = lowerBoundExcluded;
		this.upperBoundExcluded = upperBoundExcluded;
		this.lowerBoundExtrema = lowerBoundExtrema;
		this.upperBoundExtrema = upperBoundExtrema;
	}
	
	public boolean isEmpty() {
		return (! hasDefinedLowerBound() && (! hasLowerBoundExtrema())); 
	}
	
	/**
	 * @return the lowerBound
	 */
	protected T getLowerBound() {
		return this.lowerBound;
	}

	/**
	 * @param lowerBound the lowerBound to set
	 */
	protected void setLowerBound(T lowerBound) {
		this.lowerBound = lowerBound;
	}

	/**
	 * @return the upperBound
	 */
	protected T getUpperBound() {
		return this.upperBound;
	}

	/**
	 * @param upperBound the upperBound to set
	 */
	protected void setUpperBound(T upperBound) {
		this.upperBound = upperBound;
	}

	/**
	 * @param lowerBoundExcluded the lowerBoundExcluded to set
	 */
	protected void setLowerBoundExcluded(boolean lowerBoundExcluded) {
		this.lowerBoundExcluded = lowerBoundExcluded;
	}

	/**
	 * @param lowerBoundExtrema the lowerBoundExtrema to set
	 */
	protected void setLowerBoundExtrema(Extrema lowerBoundExtrema) {
		this.lowerBoundExtrema = lowerBoundExtrema;
	}

	/**
	 * @param upperBoundExcluded the upperBoundExcluded to set
	 */
	protected void setUpperBoundExcluded(boolean upperBoundExcluded) {
		this.upperBoundExcluded = upperBoundExcluded;
	}

	/**
	 * @param upperBoundExtrema the upperBoundExtrema to set
	 */
	protected void setUpperBoundExtrema(Extrema upperBoundExtrema) {
		this.upperBoundExtrema = upperBoundExtrema;
	}

	public boolean hasDefinedLowerBound() {
		return this.lowerBound != null; 
	}

	public boolean hasDefinedUpperBound() {
		return this.upperBound != null; 
	}

	/**
	 * If the lower bound is not an extrema then the function
	 * returns the defined lower bound. Otherwise it returns 
	 * null.
	 * 
	 * Example: 
	 * 	(MIN..5) would give null
	 *  (1..5) would give (Integer)1
	 *  
	 * @return the defined lower bound or null
	 */
	public T getDefinedLowerBound() {
		return this.lowerBound;
	}

	/**
	 * If the upper bound is not an extrema then the function
	 * returns the defined upper bound. Otherwise it returns 
	 * null.
	 *  
	 * Example: 
	 * 	(MIN..MAX) would give null
	 *  (1..5) would give (Integer)5
	 *    
	 * @return the defined upper bound or null
	 */	
	public T getDefinedUpperBound() {
		return this.upperBound;
	}

	public boolean isLowerBoundExcluded() {
		return this.lowerBoundExcluded;
	}

	public boolean isUpperBoundExcluded() {
		return this.upperBoundExcluded;
	}

	public boolean hasLowerBoundExtrema() {
		return getLowerBoundExtrema() != null;
	}

	public boolean hasUpperBoundExtrema() {
		return getUpperBoundExtrema() != null;
	}

	public Extrema getLowerBoundExtrema() {
		return this.lowerBoundExtrema;
	}

	public Extrema getUpperBoundExtrema() {
		return this.upperBoundExtrema;
	}

	@Override
	public void accept(ConstraintVisitor visitor) {
		visitor.visit(this);
	}

}
