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

import com.github.openasn1.compiler.Exceptions.CompilationStoppingException;
import com.github.openasn1.parser.generated.syntaxtree.DefinedValue;
import com.github.openasn1.parser.generated.syntaxtree.NamedNumber;
import com.github.openasn1.parser.generated.syntaxtree.NodeSequence;
import com.github.openasn1.parser.generated.syntaxtree.NodeToken;
import com.github.openasn1.parser.generated.syntaxtree.SignedNumber;
import com.github.openasn1.parser.generated.visitor.DepthFirstVisitor;

/**
 * @author Clayton Hoss
 * 
 */
public class NamedNumberAttributes {

	public static class IntegerValueExtractinVisitor extends DepthFirstVisitor {

		private int value;

		/**
		 * @see com.github.openasn1.parser.generated.visitor.DepthFirstVisitor#visit(com.github.openasn1.parser.generated.syntaxtree.DefinedValue)
		 */
		@Override
		public void visit(DefinedValue n) {
			throw new CompilationStoppingException(
					"Defined Values are not supported");
		}

		/**
		 * @see com.github.openasn1.parser.generated.visitor.DepthFirstVisitor#visit(com.github.openasn1.parser.generated.syntaxtree.SignedNumber)
		 */
		@Override
		public void visit(SignedNumber n) {
			int result = Integer.parseInt(n.nodeToken.tokenImage);
			if (n.nodeOptional.present()) {
				result = result * -1;
			}
			setValue(result);
		}

		/**
		 * @return the value
		 */
		public int getValue() {
			return this.value;
		}

		/**
		 * @param value
		 *            the value to set
		 */
		private void setValue(int value) {
			this.value = value;
		}

	}

	public static String getName(NamedNumber node) {
		NodeSequence ns = (NodeSequence) node.nodeChoice.choice;
		NodeToken nt = (NodeToken) ns.nodes.firstElement();
		return nt.tokenImage;
	}

	public static int getValue(NamedNumber node) {
		IntegerValueExtractinVisitor vis = new IntegerValueExtractinVisitor();
		node.accept(vis);
		return vis.getValue();
	}
}
