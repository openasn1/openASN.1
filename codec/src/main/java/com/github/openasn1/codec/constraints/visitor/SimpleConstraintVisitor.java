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

import java.util.HashMap;

import com.github.openasn1.codec.constraints.Constraint;


/**
 * @author Marc Weyland
 *
 */
public class SimpleConstraintVisitor extends ConstraintVisitorImpl {
	private HashMap<Class, Constraint> constraintList = new HashMap<Class, Constraint>(); 

	private boolean isExtensible = false;

	public SimpleConstraintVisitor(ConstraintVisitorImpl parentConstraintVisitor) {
		super(parentConstraintVisitor);
	}

	protected HashMap<Class, Constraint> getConstraintList() {
		return this.constraintList;
	}

	public boolean isExtensible() {
		return this.isExtensible;
	}

	protected void setExtensible(boolean isExtensible) {
		this.isExtensible = isExtensible;
	}
	
	public boolean hasAncientVisitorVisited(Class<? extends Constraint> constraintClass) {
		if (! hasParentConstraintVisitor()) {
			return false;
		}
		
		SimpleConstraintVisitor ancientVisitor = (SimpleConstraintVisitor)getAncientConstraintVisitor(SimpleConstraintVisitor.class);
		
		if (ancientVisitor == null) {
			return false;
		}
		
		if (ancientVisitor.getConstraintList().containsKey(constraintClass)) {
			return true;
		}
		
		return ancientVisitor.hasAncientVisitorVisited(constraintClass);
	}
	
	public SimpleConstraintVisitor getAncientVisitorWhoVisited(Class<? extends SimpleConstraintVisitor> ancientVisitorClass, Class<? extends Constraint> constraintClass) {
		if (! hasParentConstraintVisitor()) {
			return null;
		}
		
		SimpleConstraintVisitor ancientVisitor = (SimpleConstraintVisitor)getAncientConstraintVisitor(ancientVisitorClass);
		
		if (ancientVisitor == null) {
			return null;
		}
		
		if (ancientVisitor.getConstraintList().containsKey(constraintClass)) {
			return ancientVisitor;
		}
		
		return ancientVisitor.getAncientVisitorWhoVisited(ancientVisitorClass, constraintClass);
	}	

	/**
	 * @see com.github.openasn1.codec.constraints.visitor.ConstraintVisitorImpl#visitDefault(com.github.openasn1.codec.constraints.Constraint)
	 */
	@Override
	public void visitDefault(Constraint object) {
		getConstraintList().put(object.getClass(), object);
	}
}