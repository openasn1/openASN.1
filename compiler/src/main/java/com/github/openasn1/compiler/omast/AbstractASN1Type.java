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

import com.github.openasn1.parser.generated.syntaxtree.Node;

/**
 * @author Clayton Hoss
 *
 */
public abstract class AbstractASN1Type implements OMVisitable {

	private String name;

	private Node correspondingASN1Node;

	private AbstractASN1Type parent = null;

	/**
	 * @return the correspondingASN1Node
	 */
	public Node getCorrespondingASN1Node() {
		return this.correspondingASN1Node;
	}

	/**
	 * @param correspondingASN1Node
	 *            the correspondingASN1Node to set
	 */
	public void setCorrespondingASN1Node(Node correspondingASN1Node) {
		this.correspondingASN1Node = correspondingASN1Node;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the parent
	 */
	public AbstractASN1Type getParent() {
		return this.parent;
	}

	/**
	 * @param parent the parent to set
	 */
	void setParent(AbstractASN1Type parent) {
		this.parent = parent;
	}

}
