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
package com.github.openasn1.compiler.utils.reflectivevisitor;

import org.apache.log4j.Logger;

import com.github.openasn1.codec.constraints.Constraint;
import com.github.openasn1.codec.constraints.ConstraintExtension;
import com.github.openasn1.codec.constraints.ConstraintIntersection;
import com.github.openasn1.codec.constraints.ConstraintUnion;
import com.github.openasn1.codec.constraints.subtype.IntegerValueRangeConstraint;
import com.github.openasn1.codec.constraints.subtype.PermittedAlphabetConstraint;
import com.github.openasn1.codec.constraints.subtype.RestrictedCharacterValueRangeConstraint;
import com.github.openasn1.codec.constraints.subtype.SingleValueConstraint;
import com.github.openasn1.codec.constraints.subtype.SizeConstraint;

public class ConstraintReflectiveVisitor implements ReflectiveVisitor<Constraint> {
	private static Logger LOGGER = Logger.getLogger(ConstraintReflectiveVisitor.class.getName());

	private StringBuffer textBuffer = new StringBuffer();
	
	public void visit(ConstraintExtension type) {
		textBuffer.append("new ConstraintExtension(");
		this.process(type.getExtensionRootConstraint());
		if (type.hasExtensionAdditionConstraint()) {
			textBuffer.append(", ");
			this.process(type.getExtensionAdditionConstraint());
		}
		textBuffer.append(")");
	}
	
	public void visit(IntegerValueRangeConstraint type) {
		String lower = null;
		String upper = null;
		if (type.hasDefinedLowerBound()) {
			lower = type.getDefinedLowerBound().toString();
		} else {
			lower = type.getLowerBoundExtrema().name();
		}
		
		if (type.hasDefinedUpperBound()) {
			upper = type.getDefinedUpperBound().toString();
		} else {
			upper = type.getUpperBoundExtrema().name();
		}

		textBuffer.append("new IntegerValueRangeConstraint(" + lower + ", " + upper + ")");
	}

	public void visit(RestrictedCharacterValueRangeConstraint type) {
		String lower = null;
		String upper = null;
		if (type.hasDefinedLowerBound()) {
			lower = type.getDefinedLowerBound().toString();
		} else {
			lower = type.getLowerBoundExtrema().name();
		}
		
		if (type.hasDefinedUpperBound()) {
			upper = type.getDefinedUpperBound().toString();
		} else {
			upper = type.getUpperBoundExtrema().name();
		}

		textBuffer.append("new RestrictedCharacterValueRangeConstraint('" + lower + "', '" + upper + "')");
	}
	
	public void visit(SingleValueConstraint type) {
		if (type.getValue() instanceof Integer) {
			textBuffer.append("new SingleValueConstraint<Integer>(" + type.getValue() + ")");
		} else {
			textBuffer.append("new SingleValueConstraint<String>(\"" + type.getValue() + "\")");
		}
	}
	
	public void visit(PermittedAlphabetConstraint type) {
		textBuffer.append("new PermittedAlphabetConstraint(");
		if (type.getConstraint() != null) {
			this.process(type.getConstraint());	
		}
		textBuffer.append(")");
	}

	public void visit(SizeConstraint type) {
		textBuffer.append("new SizeConstraint(");
		if (type.getConstraint() != null) {
			this.process(type.getConstraint());
		}
		textBuffer.append(")");
	}

	public void visit(ConstraintIntersection type) {
		textBuffer.append("new ConstraintIntersection(");
		int index = 0;
		for (Constraint constraint : type.getList()) {
			this.process(constraint);
			index++;
			if (index < type.getList().size()) {
				textBuffer.append(", ");		
			}
		}
		textBuffer.append(")");
	}
	
	public void visit(ConstraintUnion type) {
		textBuffer.append("new ConstraintUnion(");
		int index = 0;
		for (Constraint constraint : type.getList()) {
			this.process(constraint);
			index++;
			if (index < type.getList().size()) {
				textBuffer.append(", ");		
			}			
		}
		textBuffer.append(")");
	}
	

	public void visit(Object type) {
		LOGGER.debug("defaupt visiting object (" + type.toString() + ")!");
	}
	
	public boolean process(Constraint object) {
		return ReflectiveVisitorDispatcherImpl.getInstance().dispatch(this, object);
	}
	
	public String getOutput() {
		return textBuffer.toString();
	}
}