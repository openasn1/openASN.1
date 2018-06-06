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
package com.github.openasn1.codec.constraints.visitor;

import com.github.openasn1.codec.constraints.subtype.SingleValueConstraint;
import com.github.openasn1.codec.constraints.subtype.ValueRangeConstraint;

/**
 * @author Marc Weyland
 *
 */
public class IntegerConstraintVisitor extends SimpleConstraintVisitor {
	public IntegerConstraintVisitor(ConstraintVisitorImpl parentConstraintVisitor) {
		super(parentConstraintVisitor);
	}
	
	public boolean restrictsToSingleValue() {
		if (! getConstraintList().containsKey(SingleValueConstraint.class)) {
			return false;
		}
		
		return true;
	}
	
	public int getSingleValue() {
		if (! restrictsToSingleValue()) {
			throw new RuntimeException("Single value cannot be retrieved since there is no single value constraint");
		}
		
		SingleValueConstraint constraint = (SingleValueConstraint)getConstraintList().get(SingleValueConstraint.class);
		
		return (Integer)constraint.getValue();
	}
	
	public boolean hasMinimum() {
		if (restrictsToSingleValue()) {
			return true;
		}
		
		if (! getConstraintList().containsKey(ValueRangeConstraint.class)) {
			return false;
		}
		
		ValueRangeConstraint constraint = (ValueRangeConstraint)getConstraintList().get(ValueRangeConstraint.class);
		
		if (constraint.hasDefinedLowerBound()) {
			return false;
		}
		
		return true;
	}

	public boolean hasMaximum() {
		if (restrictsToSingleValue()) {
			return true;
		}
		
		if (! getConstraintList().containsKey(ValueRangeConstraint.class)) {
			return false;
		}
		
		ValueRangeConstraint constraint = (ValueRangeConstraint)getConstraintList().get(ValueRangeConstraint.class);
		
		if (constraint.hasDefinedUpperBound()) {
			return false;
		}
		
		return true;
	}
	
	public int getMinimum() {
		if (! getConstraintList().containsKey(ValueRangeConstraint.class)) {
			throw new RuntimeException("Minimum cannot be retrieved since there is no value range");
		}
		
		ValueRangeConstraint constraint = (ValueRangeConstraint)getConstraintList().get(ValueRangeConstraint.class);

		return (Integer)constraint.getDefinedLowerBound();
	}

	public int getMaximum() {
		if (! getConstraintList().containsKey(ValueRangeConstraint.class)) {
			throw new RuntimeException("Minimum cannot be retrieved since there is no value range");
		}
		
		ValueRangeConstraint constraint = (ValueRangeConstraint)getConstraintList().get(ValueRangeConstraint.class);

		return (Integer)constraint.getDefinedUpperBound();
	}

	// getExtensionRootConstraints()
	// getExtensionAdditionConstraints()
	// isWithinExtensionRoot(value)
	// isWithinExtensionAddition(value)	
	
	@Override
	public <T> void visit(SingleValueConstraint<T> constraint) {
		getConstraintList().put(SingleValueConstraint.class, constraint);
	}

	@Override
	public <T> void visit(ValueRangeConstraint<T> constraint) {
		getConstraintList().put(ValueRangeConstraint.class, constraint);
		
		
	}
}