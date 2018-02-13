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
package com.github.openasn1.codec.constraints.visitor;

import com.github.openasn1.codec.constraints.ConstraintIntersection;
import com.github.openasn1.codec.constraints.ConstraintUnion;
import com.github.openasn1.codec.constraints.character.RestrictedCharacterStringInfo;
import com.github.openasn1.codec.constraints.subtype.PermittedAlphabetConstraint;
import com.github.openasn1.codec.constraints.subtype.SingleValueConstraint;
import com.github.openasn1.codec.constraints.subtype.SizeConstraint;
import com.github.openasn1.codec.constraints.subtype.ValueRangeConstraint;

/**
 * @author Marc Weyland
 *
 */
public class RestrictedCharacterStringConstraintVisitor extends SimpleConstraintVisitor {
	private IntegerConstraintVisitor sizeConstraintVisitor = null;
	private RestrictedCharacterStringInfo characterStringInfo;
	
	public RestrictedCharacterStringConstraintVisitor(ConstraintVisitorImpl parentConstraintVisitor, RestrictedCharacterStringInfo characterStringInfo) {
		super(parentConstraintVisitor);
		
		this.characterStringInfo = characterStringInfo;
	}
	
	private RestrictedCharacterStringInfo getCharacterStringInfo() {
		return this.characterStringInfo;
	}

	private IntegerConstraintVisitor getSizeConstraintVisitor() {
		return this.sizeConstraintVisitor;
	}

	private void setSizeConstraintVisitor(IntegerConstraintVisitor sizeConstraintVisitor) {
		this.sizeConstraintVisitor = sizeConstraintVisitor;
	}

	/**
	 * Returns if the constraints restrict the size of the string to a minimum value
	 * 
	 * @return boolean true if character string has a lower size limit
	 */
	public boolean hasMinimumLength() {
		return true;
	}
	
	/**
	 * Returns the minimum string size of the character string
	 * 
	 * @return int the minimum string size of the character string
	 */
	public int getMinimumLength() {
		if (isFixedLength()) {
			return getFixedLength();
		}

		if (getSizeConstraintVisitor() == null) {
			return 0;
		}

		return getSizeConstraintVisitor().getMinimum();
	}

	/**
	 * Returns if the constraints restrict the size of the string to a maximum value
	 * 
	 * @return boolean true if character string has an upper size limit
	 */
	public boolean hasMaximumLength() {
		if (isFixedLength()) {
			return true;
		}
		
		if (getSizeConstraintVisitor() == null) {
			return false;
		}

		return getSizeConstraintVisitor().hasMaximum();		
	}
	
	/**
	 * Returns the maximum string size of the character string
	 * 
	 * @return int the maximum string size of the character string
	 */
	public int getMaximumLength() {
		if (isFixedLength()) {
			return getFixedLength();
		}

		if (getSizeConstraintVisitor() == null) {
			throw new RuntimeException("Maximum size cannot be retrieved since there is no size constraint");
		}

		return getSizeConstraintVisitor().getMaximum();
	}	
	
	/**
	 * Returns if the constraints restrict the size of the string to a fixed value
	 * 
	 * @return boolean true if character string has a fixed size
	 */
	public boolean isFixedLength() {
		if (getSizeConstraintVisitor() == null) {
			return false;
		}
		
		return getSizeConstraintVisitor().restrictsToSingleValue();
	}
	
	/**
	 * Returns the fixed size of the character string
	 * 
	 * @return int the fixed size of the character string
	 */
	public int getFixedLength() {
		if (getSizeConstraintVisitor() == null) {
			throw new RuntimeException("Fixed size cannot be retrieved since there is no size constraint");
		}

		return getSizeConstraintVisitor().getSingleValue();
	}

	/**
	 * Returns if the constraints restrict the character string to a single string value
	 * 
	 * @return boolean true if character string is restricted to a single string value
	 */
	public boolean restrictsToSingleValue() {
		if (! getConstraintList().containsKey(SingleValueConstraint.class)) {
			return false;
		}
		
		return true;
	}
	
	/**
	 * Returns the amount of permitted characters
	 * 
	 * @return the amount of permitted characters
	 */
	public int getAlphabetSize() {
		if (! getConstraintList().containsKey(PermittedAlphabetConstraint.class)) {
			return getCharacterStringInfo().getAlphabetSize();
		}
		
		return getUpperAlphabetIndex() - getLowerAlphabetIndex() + 1;
	}
	
	/**
	 * Returns the character index from the permitted alphabet 
	 * 
	 * @param character the character whose index should be returned
	 * @return the index of the given character 
	 */
	public int getCharacterIndex(char character) {
		return getCharacterStringInfo().getCharacterIndex(character);
	}
	
	/**
	 * Returns the lower character index of the permitted alphabet
	 * 
	 * @return the lower character index of the permitted alphabet
	 */
	public int getLowerAlphabetIndex() {
		if (! getConstraintList().containsKey(ValueRangeConstraint.class)) {
			return getCharacterStringInfo().getLowerIndex();
		}
		
		ValueRangeConstraint constraint = (ValueRangeConstraint)getConstraintList().get(ValueRangeConstraint.class);
		
		int min = getCharacterStringInfo().getLowerIndex();

		if (constraint.hasDefinedLowerBound()) {
			char lowerBound = (Character)constraint.getDefinedLowerBound(); 
			min = getCharacterStringInfo().getCharacterIndex(lowerBound);
			if (constraint.isLowerBoundExcluded()) {
				min++;
			}
		}
		
		return min;
	}

	/**
	 * Returns the upper character index of the permitted alphabet
	 * 
	 * @return int the upper character index of the permitted alphabet
	 */
	public int getUpperAlphabetIndex() {
		if (! getConstraintList().containsKey(ValueRangeConstraint.class)) {
			return getCharacterStringInfo().getUpperIndex();
		}
		
		ValueRangeConstraint constraint = (ValueRangeConstraint)getConstraintList().get(ValueRangeConstraint.class);
		
		int max = getCharacterStringInfo().getUpperIndex();

		if (constraint.hasDefinedUpperBound()) {
			char upperBound = (Character)constraint.getDefinedUpperBound(); 
			max = getCharacterStringInfo().getCharacterIndex(upperBound);
			if (constraint.isUpperBoundExcluded()) {
				max--;
			}
		}

		return max;
	}


	@Override
	public <T> void visit(SingleValueConstraint<T> constraint) {
		getConstraintList().put(SingleValueConstraint.class, constraint);
	}

	@Override
	public <T> void visit(ValueRangeConstraint<T> constraint) {
		getConstraintList().put(ValueRangeConstraint.class, constraint);
	}

	@Override
	public void visit(SizeConstraint constraint) {
		getConstraintList().put(SizeConstraint.class, constraint);
		
		IntegerConstraintVisitor visitor = new IntegerConstraintVisitor(null);
		constraint.getConstraint().accept(visitor);
		
		setSizeConstraintVisitor(visitor);
	}

	@Override
	public void visit(PermittedAlphabetConstraint constraint) {
		getConstraintList().put(PermittedAlphabetConstraint.class, constraint);
	}

	@Override
	public void visit(ConstraintUnion constraint) {
		
	}

	@Override
	public void visit(ConstraintIntersection constraint) {

	}
}