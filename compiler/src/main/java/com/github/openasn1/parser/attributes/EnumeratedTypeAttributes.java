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

import java.util.LinkedHashSet;
import java.util.Set;

import com.github.openasn1.parser.generated.syntaxtree.EnumeratedType;
import com.github.openasn1.parser.generated.syntaxtree.NodeToken;
import com.github.openasn1.parser.generated.visitor.DepthFirstVisitor;

/**
 * @author Clayton Hoss
 * 
 */
public class EnumeratedTypeAttributes {

	static class EnumerationStringSetVisitor extends DepthFirstVisitor {

		private Set<String> enumerationStrings = new LinkedHashSet<String>();

		/**
		 * @see com.github.openasn1.parser.generated.visitor.DepthFirstVisitor#visit(com.github.openasn1.parser.generated.syntaxtree.NodeToken)
		 */
		@Override
		public void visit(NodeToken n) {
			if (NodeTokenAttribues.isIdentifier(n))
				getEnumerationStrings().add(n.tokenImage);
		}

		/**
		 * @return the enumerationStrings
		 */
		Set<String> getEnumerationStrings() {
			return this.enumerationStrings;
		}

	}

	public static Set<String> getEnumerationStrings(EnumeratedType n) {
		EnumerationStringSetVisitor vis = new EnumerationStringSetVisitor();
		n.enumerations.accept(vis);
		return vis.getEnumerationStrings();
	}
}
