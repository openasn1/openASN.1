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
package com.github.openasn1.parser.attributes;

import com.github.openasn1.parser.generated.ASN1ParserConstants;
import com.github.openasn1.parser.generated.syntaxtree.ComponentType;
import com.github.openasn1.parser.generated.syntaxtree.Node;
import com.github.openasn1.parser.generated.syntaxtree.NodeOptional;
import com.github.openasn1.parser.generated.syntaxtree.NodeSequence;
import com.github.openasn1.parser.generated.syntaxtree.NodeToken;
import com.github.openasn1.parser.generated.syntaxtree.Type;
import com.github.openasn1.parser.generated.syntaxtree.Value;
import com.github.openasn1.parser.generated.visitor.DepthFirstVisitor;

/**
 * @author Clayton Hoss
 * 
 */
public class ComponentTypeAttributes {

	static class NodeAttributeVisitor extends DepthFirstVisitor {

		private Boolean isOptional = null;

		private Boolean hasDefaultValue = null;

		private Type type = null;

		private Value value = null;

		/**
		 * @see com.github.openasn1.parser.generated.visitor.DepthFirst#visit(com.github.openasn1.parser.generated.syntaxtree.NodeToken)
		 */
		@Override
		public void visit(NodeToken n) {
			setOptional(n.kind == ASN1ParserConstants.OPTIONAL_TKN);
			setHasDefaultValue(n.kind == ASN1ParserConstants.DEFAULT_TKN);
		}

		/**
		 * @see com.github.openasn1.parser.generated.visitor.DepthFirstVisitor#visit(com.github.openasn1.parser.generated.syntaxtree.Type)
		 */
		@Override
		public void visit(Type n) {
			this.type = n;
			return;
		}

		/**
		 * @see com.github.openasn1.parser.generated.visitor.DepthFirstVisitor#visit(com.github.openasn1.parser.generated.syntaxtree.Value)
		 */
		@Override
		public void visit(Value n) {
			this.value = n;
			return;
		}

		/**
		 * @return the isOptional
		 */

		public Boolean isOptional() {
			return this.isOptional;
		}

		/**
		 * @param isOptional
		 *            the isOptional to set
		 */
		private void setOptional(boolean isOptional) {
			this.isOptional = isOptional;
		}

		/**
		 * @return the hasDefaultValue
		 */
		public boolean hasDefaultValue() {
			return this.hasDefaultValue;
		}

		/**
		 * @param hasDefaultValue
		 *            the hasDefaultValue to set
		 */
		private void setHasDefaultValue(boolean hasDefaultValue) {
			this.hasDefaultValue = hasDefaultValue;
		}

		/**
		 * @return the type
		 */
		public Type getType() {
			return this.type;
		}

		/**
		 * @return the value
		 */
		public Value getValue() {
			return this.value;
		}

	}

	public static boolean isComponentsOf(ComponentType n) {
		NodeSequence ns = (NodeSequence) n.nodeChoice.choice;
		if (!NodeClassTypeInformation.isNodeToken(ns.elementAt(0))) {
			return false;
		}

		if (((NodeToken) ns.elementAt(0)).kind == ASN1ParserConstants.COMPONENTS_TKN) {
			return true;
		}

		return false;

	}

	public static boolean isNamedType(ComponentType n) {
		NodeSequence ns = (NodeSequence) n.nodeChoice.choice;
		return NodeClassTypeInformation.isNamedType(ns.elementAt(0));
	}

	public static boolean isComponentType(Node n) {
		return (n instanceof ComponentType);
	}

	public static ComponentType getComponentType(Node n) {
		if (!isComponentType(n)) {
			return null;
		}
		return (ComponentType) n;
	}

	public static boolean hasDefaultValue(ComponentType n) {
		if (!isNamedType(n)) {
			return false;
		}
		NodeSequence ns = (NodeSequence) n.nodeChoice.choice;
		NodeOptional no = (NodeOptional) ns.elementAt(1);
		if (!no.present()) {
			return false;
		}
		NodeAttributeVisitor vi = new NodeAttributeVisitor();
		no.accept(vi);
		return vi.hasDefaultValue();
	}

	public static Type getType(ComponentType n) {

		NodeAttributeVisitor vi = new NodeAttributeVisitor();
		n.accept(vi);
		return vi.getType();
	}

	public static boolean isOptional(ComponentType n) {
		if (!isNamedType(n)) {
			return false;
		}
		NodeSequence ns = (NodeSequence) n.nodeChoice.choice;
		NodeOptional no = (NodeOptional) ns.elementAt(1);
		if (!no.present()) {
			return false;
		}

		NodeAttributeVisitor vi = new NodeAttributeVisitor();
		no.accept(vi);
		return vi.isOptional();
	}

}
