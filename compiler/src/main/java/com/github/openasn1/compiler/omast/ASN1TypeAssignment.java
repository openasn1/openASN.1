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
package com.github.openasn1.compiler.omast;

/**
 * @author Clayton Hoss
 *
 */
public class ASN1TypeAssignment extends AbstractASN1Type {

	private AbstractASN1Type asn1type;

	private boolean primitive;

	/**
	 * @param asn1type is asn1type
	 * @param primitive is primitive
	 */
	public ASN1TypeAssignment(AbstractASN1Type asn1type, boolean primitive) {
		super();
		asn1type.setParent(this);
		this.asn1type = asn1type;
		this.primitive = primitive;
	}

	public void accept(OMVisitor vis) {
		vis.visit(this);
	}

	/**
	 * @return the asn1type
	 */
	public AbstractASN1Type getAsn1type() {
		return this.asn1type;
	}

	/**
	 * @return the primitive
	 */
	public boolean isPrimitive() {
		return this.primitive;
	}

}
