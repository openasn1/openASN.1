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
public class AggregatedASN1Type extends AbstractASN1Type {

	private AbstractASN1Type child;

	private AggregatedASN1TypeEnum asn1Type;

	/**
	 * @see com.github.openasn1.compiler.omast.OMVisitable#accept(com.github.openasn1.compiler.omast.OMVisitor)
	 */
	public void accept(OMVisitor vis) {
		vis.visit(this);
	}

	/**
	 * @return the asn1Type
	 */
	public AggregatedASN1TypeEnum getAsn1Type() {
		return this.asn1Type;
	}

	/**
	 * @param asn1Type the asn1Type to set
	 */
	public void setAsn1Type(AggregatedASN1TypeEnum asn1Type) {
		this.asn1Type = asn1Type;
	}

	/**
	 * @return the type
	 */
	public AbstractASN1Type getChild() {
		return this.child;
	}

	/**
	 * @param type the type to set
	 */
	public void setChild(AbstractASN1Type type) {
		type.setParent(this);
		this.child = type;
	}

}
