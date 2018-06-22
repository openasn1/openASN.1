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

import java.util.ArrayList;
import java.util.List;

/**
 * @author Clayton Hoss
 *
 */
public class ASN1Module extends AbstractASN1Type {

	private ASN1TypeAssignmentList typeAssingmentList;

	private List<ASN1ImportListFromModule> imports = new ArrayList<ASN1ImportListFromModule>();

	/**
	 * @see com.github.openasn1.compiler.omast.OMVisitable#accept(com.github.openasn1.compiler.omast.OMVisitor)
	 */
	public void accept(OMVisitor vis) {
		vis.visit(this);

	}


	/**
	 * @return the typeAssingmentList
	 */
	public ASN1TypeAssignmentList getTypeAssingmentList() {
		return this.typeAssingmentList;
	}

	/**
	 * @param typeAssingmentList
	 *            the typeAssingmentList to set
	 */
	public void setTypeAssingmentList(ASN1TypeAssignmentList typeAssingmentList) {
		typeAssingmentList.setParent(this);
		this.typeAssingmentList = typeAssingmentList;
	}

	/**
	 * @return the imports
	 */
	public List<ASN1ImportListFromModule> getImports() {
		return this.imports;
	}


	/**
	 * @param importList is the import list
	 * @return boolean value
	 * @see java.util.List#add(java.lang.Object)
	 */
	public boolean add(ASN1ImportListFromModule importList) {
		importList.setParent(this);
		return this.imports.add(importList);
	}


}