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

public abstract class AbstractOMVisitor implements OMVisitor {

	/**
	 * @see com.github.openasn1.compiler.omast.OMVisitor#visit(com.github.openasn1.compiler.omast.ComplexASN1Type)
	 */
	public void visit(ComplexASN1Type type) {
		for (AbstractASN1Type t : type.getChildren()) {
			t.accept(this);
		}
	}

	/**
	 * @see com.github.openasn1.compiler.omast.OMVisitor#visit(com.github.openasn1.compiler.omast.SimpleASN1Type)
	 */
	public void visit(SimpleASN1Type type) {

	}

	/**
	 * @see com.github.openasn1.compiler.omast.OMVisitor#visit(com.github.openasn1.compiler.omast.ReferencedASN1Type)
	 */
	public void visit(ReferencedASN1Type type) {
	}

	/**
	 * @see com.github.openasn1.compiler.omast.OMVisitor#visit(com.github.openasn1.compiler.omast.ASN1TypeAssignment)
	 */
	public void visit(ASN1TypeAssignment type) {
		type.getAsn1type().accept(this);

	}

	/**
	 * @see com.github.openasn1.compiler.omast.OMVisitor#visit(com.github.openasn1.compiler.omast.ASN1TypeAssignmentList)
	 */
	public void visit(ASN1TypeAssignmentList type) {
		for (AbstractASN1Type t : type.getList()) {
			t.accept(this);
		}
	}

	/**
	 * @see com.github.openasn1.compiler.omast.OMVisitor#visit(com.github.openasn1.compiler.omast.AggregatedASN1Type)
	 */
	public void visit(AggregatedASN1Type type) {
		type.getChild().accept(this);
	}

	/**
	 * @see com.github.openasn1.compiler.omast.OMVisitor#visit(com.github.openasn1.compiler.omast.ASN1ImportListFromModule)
	 */
	public void visit(ASN1ImportListFromModule type) {

	}

	/**
	 * @see com.github.openasn1.compiler.omast.OMVisitor#visit(com.github.openasn1.compiler.omast.ASN1Module)
	 */
	public void visit(ASN1Module type) {
		for (AbstractASN1Type t : type.getImports()) {
			t.accept(this);
		}

		type.getTypeAssingmentList().accept(this);
	}

	/* (non-Javadoc)
	 * @see com.github.openasn1.compiler.omast.OMVisitor#visit(com.github.openasn1.compiler.omast.EnumASN1Type)
	 */
	public void visit(EnumASN1Type type) {
	}

}
