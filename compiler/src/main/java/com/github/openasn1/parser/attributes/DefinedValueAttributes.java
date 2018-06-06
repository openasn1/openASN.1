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

import com.github.openasn1.parser.generated.syntaxtree.DefinedValue;
import com.github.openasn1.parser.generated.syntaxtree.ExternalValueReference;
import com.github.openasn1.parser.generated.syntaxtree.NodeToken;
import com.github.openasn1.parser.generated.syntaxtree.valuereference;
import com.github.openasn1.parser.generated.visitor.DepthFirstVisitor;

/**
 * @author Clayton Hoss
 * 
 */
public class DefinedValueAttributes {

	public static class DefinedValueVisitor extends DepthFirstVisitor {

		private String symbol;

		private NodeToken valueReferenceToken = null;

		private boolean isExternalValueReference;

		/**
		 * @see com.github.openasn1.parser.generated.visitor.DepthFirstVisitor#visit(com.github.openasn1.parser.generated.syntaxtree.ExternalValueReference)
		 */
		@Override
		public void visit(ExternalValueReference n) {
			String symbol = n.modulereference.nodeToken.tokenImage;
			symbol = symbol + "." + n.valuereference.nodeToken.tokenImage;
			setValueReferenceToken(n.valuereference.nodeToken);
			setSymbol(symbol);
			setExternalValueReference(true);
		}

		/**
		 * @see com.github.openasn1.parser.generated.visitor.DepthFirstVisitor#visit(com.github.openasn1.parser.generated.syntaxtree.valuereference)
		 */
		@Override
		public void visit(valuereference n) {
			setExternalValueReference(false);
			String symbol = n.nodeToken.tokenImage;
			setValueReferenceToken(n.nodeToken);
			setSymbol(symbol);
		}

		/**
		 * @return the symbol
		 */
		public String getSymbol() {
			return this.symbol;
		}

		/**
		 * @param symbol
		 *            the symbol to set
		 */
		private void setSymbol(String symbol) {
			this.symbol = symbol;
		}

		/**
		 * @return the isExternalValueReference
		 */
		public boolean isExternalValueReference() {
			return this.isExternalValueReference;
		}

		/**
		 * @param isExternalValueReference
		 *            the isExternalValueReference to set
		 */
		private void setExternalValueReference(boolean isExternalValueReference) {
			this.isExternalValueReference = isExternalValueReference;
		}

		/**
		 * @return the symbolToken
		 */
		public NodeToken getValueReferenceToken() {
			return this.valueReferenceToken;
		}

		/**
		 * @param symbolToken
		 *            the symbolToken to set
		 */
		private void setValueReferenceToken(NodeToken symbolToken) {
			this.valueReferenceToken = symbolToken;
		}
	}

	public static DefinedValueVisitor getVisitor(DefinedValue n) {
		DefinedValueVisitor vis = new DefinedValueVisitor();
		n.accept(vis);
		return vis;
	}

	public static String getSymbol(DefinedValue n) {
		return getVisitor(n).getSymbol();
	}

	public static boolean isExternalValueReference(DefinedValue n) {
		return getVisitor(n).isExternalValueReference();
	}

	public static NodeToken getValueReferenceToken(DefinedValue n) {
		return getVisitor(n).getValueReferenceToken();
	}

}
