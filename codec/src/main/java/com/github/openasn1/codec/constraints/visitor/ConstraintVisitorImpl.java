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

import com.github.openasn1.codec.constraints.Constraint;
import com.github.openasn1.codec.constraints.ConstraintExcept;
import com.github.openasn1.codec.constraints.ConstraintExtension;
import com.github.openasn1.codec.constraints.ConstraintIntersection;
import com.github.openasn1.codec.constraints.ConstraintSerialisation;
import com.github.openasn1.codec.constraints.ConstraintUnion;
import com.github.openasn1.codec.constraints.subtype.PermittedAlphabetConstraint;
import com.github.openasn1.codec.constraints.subtype.SingleValueConstraint;
import com.github.openasn1.codec.constraints.subtype.SizeConstraint;
import com.github.openasn1.codec.constraints.subtype.ValueRangeConstraint;

/**
 * @author Marc Weyland
 *
 */
public class ConstraintVisitorImpl implements ConstraintVisitor {
	private ConstraintVisitorImpl parentVisitor = null;

	// public ConstraintVisitorImpl() { }
	
	public ConstraintVisitorImpl(ConstraintVisitorImpl parentVisitor) {
		this.parentVisitor = parentVisitor;
	}
	
	public ConstraintVisitorImpl getParentConstraintVisitor() {
		return this.parentVisitor;
	}

	public boolean hasParentConstraintVisitor() {
		return getParentConstraintVisitor() != null;
	}
	
	public ConstraintVisitorImpl getAncientConstraintVisitor(Class<? extends ConstraintVisitorImpl> constraintVisitorClass) {
		if (! hasParentConstraintVisitor()) {
			return null;
		}
		
		if (constraintVisitorClass.isAssignableFrom(getParentConstraintVisitor().getClass())) {
			return getParentConstraintVisitor();
		}
		
		return getParentConstraintVisitor().getAncientConstraintVisitor(constraintVisitorClass);
	}

	public boolean isChildOf(Class<? extends ConstraintVisitor> constraintVisitorClass) {
		if (! hasParentConstraintVisitor()) {
			return false;
		}
		
		if (constraintVisitorClass.isAssignableFrom(getParentConstraintVisitor().getClass())) {
			return true;
		}
		
		return getParentConstraintVisitor().isChildOf(constraintVisitorClass);
	}

	public <T> void visit(ValueRangeConstraint<T> constraint) {
		visitDefault(constraint);
	}

	public void visit(SizeConstraint constraint) {
		visitDefault(constraint);
	}

	public <T> void visit(SingleValueConstraint<T> constraint) {
		visitDefault(constraint);
	}

	public void visit(PermittedAlphabetConstraint constraint) {
		visitDefault(constraint);
	}

	public void visit(ConstraintSerialisation constraint) {
		visitDefault(constraint);
	}

	public void visit(ConstraintIntersection constraint) {
		visitDefault(constraint);
	}

	public void visit(ConstraintUnion constraint) {
		visitDefault(constraint);
	}

	public void visit(ConstraintExcept constraint) {
		visitDefault(constraint);
	}

	public void visit(ConstraintExtension constraint) {
		visitDefault(constraint);
	}

	public void visitDefault(Constraint object) {
		
	}

}
