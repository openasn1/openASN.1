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
package com.github.openasn1.codec.constraints;

import com.github.openasn1.codec.constraints.visitor.ConstraintVisitor;

/**
 * @author Marc Weyland
 *
 */
public class ConstraintIntersection extends ConstraintList implements Constraint {

	/**
	 * Creates the constraint INTERSECTION with a variable amount of constraints 
	 * 
	 * NOTE: we don't have a simple binary intersection. We use varargs to encompass
	 * 		 the need to consider all constraints in a intersection list at a time
	 * 
	 * 	     e.g.: "(A ^ B ^ C)" could be written as "((A ^ B) ^ C)" using two
	 * 			   INTERSECTION. But now A, B and C are all part of the same intersection.
	 * 			   Being semantically equivalent but easing the work for further
	 * 			   processing in the visitors.  
	 * 
	 * @param operands
	 */
	public ConstraintIntersection(Constraint ... constraints) {
		super(constraints);
	}

	public void accept(ConstraintVisitor visitor) {
		visitor.visit(this);
	}
}
