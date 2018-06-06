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
import com.github.openasn1.parser.generated.syntaxtree.NodeToken;
import com.github.openasn1.parser.generated.syntaxtree.Tag;
import com.github.openasn1.parser.generated.syntaxtree.TaggedType;
import com.github.openasn1.parser.generated.syntaxtree.Type;
import com.github.openasn1.parser.generated.visitor.DepthFirstVisitor;

/**
 * @author Clayton Hoss
 * 
 */
public class TaggedTypeAttributes {

	public static class TagVisitor extends DepthFirstVisitor {

		private Type type;

		private Tag tag;

		private boolean isManualyImplicit = false;

		private boolean isManualyExplicit = false;

		/**
		 * @see com.github.openasn1.parser.generated.visitor.DepthFirstVisitor#visit(com.github.openasn1.parser.generated.syntaxtree.NodeToken)
		 */
		@Override
		public void visit(NodeToken n) {
			setManualyImplicit(n.kind == ASN1ParserConstants.IMPLICIT_TKN);
			setManualyExplicit(n.kind == ASN1ParserConstants.EXPLICIT_TKN);
			return;
		}

		/**
		 * @see com.github.openasn1.parser.generated.visitor.DepthFirstVisitor#visit(com.github.openasn1.parser.generated.syntaxtree.Tag)
		 */
		@Override
		public void visit(Tag n) {
			setTag(n);
			return;
		}

		/**
		 * @see com.github.openasn1.parser.generated.visitor.DepthFirstVisitor#visit(com.github.openasn1.parser.generated.syntaxtree.Type)
		 */
		@Override
		public void visit(Type n) {
			setType(n);
			return;
		}

		/**
		 * @return the tag
		 */
		public Tag getTag() {
			return this.tag;
		}

		/**
		 * @param tag
		 *            the tag to set
		 */
		private void setTag(Tag tag) {
			this.tag = tag;
		}

		/**
		 * @return the type
		 */
		public Type getType() {
			return this.type;
		}

		/**
		 * @param type
		 *            the type to set
		 */
		private void setType(Type type) {
			this.type = type;
		}

		/**
		 * @param isManualyExplicit
		 *            the isManualyExplicit to set
		 */
		private void setManualyExplicit(boolean isManualyExplicit) {
			this.isManualyExplicit = isManualyExplicit;
		}

		/**
		 * @param isManualyImplicit
		 *            the isManualyImplicit to set
		 */
		private void setManualyImplicit(boolean isManualyImplicit) {
			this.isManualyImplicit = isManualyImplicit;
		}

		/**
		 * @return the isManualyExplicit
		 */
		public boolean isManualyExplicit() {
			return this.isManualyExplicit;
		}

		/**
		 * @return the isManualyImplicit
		 */
		public boolean isManualyImplicit() {
			return this.isManualyImplicit;
		}

		/**
		 * @return the isTaggingModeManuallySet
		 */
		public boolean isTaggingModeManuallySet() {
			return (isManualyExplicit() || isManualyImplicit());
		}
	}

	public static boolean isManualImplicitTag(TaggedType n) {
		return getVisitor(n).isManualyImplicit();
	}

	public static boolean isManualExplicitTag(TaggedType n) {
		return getVisitor(n).isManualyExplicit();
	}

	public static Tag getTag(TaggedType n) {
		return getVisitor(n).getTag();
	}

	public static Type getType(TaggedType n) {
		return getVisitor(n).getType();
	}

	public static TagVisitor getVisitor(TaggedType n) {
		TagVisitor vis = new TagVisitor();
		n.accept(vis);
		return vis;
	}

}
