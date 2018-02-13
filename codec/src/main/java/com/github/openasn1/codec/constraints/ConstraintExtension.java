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
package com.github.openasn1.codec.constraints;

import com.github.openasn1.codec.constraints.visitor.ConstraintVisitor;

/**
 * @author Marc Weyland
 *
 */
public class ConstraintExtension implements Constraint {
	private Constraint extensionRootConstraint = null;
	private Constraint extensionAdditionConstraint = null;
	
	/**
	 * ConstraintExtension conforming to the production
	 * "(" extensionRootConstraint, ...(, extensionAdditionConstraint)? ")"
	 * 
	 * @param extensionRootConstraint is extensionRootConstraint
	 * @param extensionAdditionConstraint is extensionAdditionConstraint
	 */
	public ConstraintExtension(Constraint extensionRootConstraint, Constraint extensionAdditionConstraint) {
		if (extensionRootConstraint == null) {
			throw new RuntimeException("The extensionRootConstraint may not be null");
		}
		setExtensionRootConstraint(extensionRootConstraint);
		setExtensionAdditionConstraint(extensionAdditionConstraint);
	}

	/**
	 * ConstraintExtension conforming to the production
	 * "(" extensionRootConstraint, ... ")"
	 * 
	 * @param extensionRootConstraint is extensionRootConstraint
	 */
	public ConstraintExtension(Constraint extensionRootConstraint) {
		this(extensionRootConstraint, null);
	}
	
	/**
	 * @return the extensionAdditionConstraint
	 */
	public Constraint getExtensionAdditionConstraint() {
		return this.extensionAdditionConstraint;
	}

	/**
	 * @param extensionAdditionConstraint the extensionAdditionConstraint to set
	 */
	private void setExtensionAdditionConstraint(Constraint extensionAdditionConstraint) {
		this.extensionAdditionConstraint = extensionAdditionConstraint;
	}
	
	/**
	 * @return if the extensionAdditionConstraint is present
	 */
	public boolean hasExtensionAdditionConstraint() {
		return getExtensionAdditionConstraint() != null;
	}

	/**
	 * @return the extensionRootConstraint
	 */
	public Constraint getExtensionRootConstraint() {
		return this.extensionRootConstraint;
	}

	/**
	 * @param extensionRootConstraint the extensionRootConstraint to set
	 */
	private void setExtensionRootConstraint(Constraint extensionRootConstraint) {
		this.extensionRootConstraint = extensionRootConstraint;
	}

	public void accept(ConstraintVisitor visitor) {
		visitor.visit(this);
	}
}
