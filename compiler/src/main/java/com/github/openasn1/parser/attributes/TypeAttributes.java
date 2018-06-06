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

import com.github.openasn1.compiler.astutils.ASN1ASTNodeInfos;
import com.github.openasn1.compiler.astutils.helpers.IdentifierMapHelper;
import com.github.openasn1.parser.generated.ASN1ParserConstants;
import com.github.openasn1.parser.generated.syntaxtree.AdditionalEnumeration;
import com.github.openasn1.parser.generated.syntaxtree.AlternativeTypeLists;
import com.github.openasn1.parser.generated.syntaxtree.BuiltinType;
import com.github.openasn1.parser.generated.syntaxtree.ChoiceType;
import com.github.openasn1.parser.generated.syntaxtree.ComponentType;
import com.github.openasn1.parser.generated.syntaxtree.ComponentTypeLists;
import com.github.openasn1.parser.generated.syntaxtree.Constraint;
import com.github.openasn1.parser.generated.syntaxtree.DefinedType;
import com.github.openasn1.parser.generated.syntaxtree.EnumeratedType;
import com.github.openasn1.parser.generated.syntaxtree.Enumerations;
import com.github.openasn1.parser.generated.syntaxtree.ExtensionAdditionAlternatives;
import com.github.openasn1.parser.generated.syntaxtree.ExtensionAdditions;
import com.github.openasn1.parser.generated.syntaxtree.NamedType;
import com.github.openasn1.parser.generated.syntaxtree.Node;
import com.github.openasn1.parser.generated.syntaxtree.NodeToken;
import com.github.openasn1.parser.generated.syntaxtree.NormalConstrainedType;
import com.github.openasn1.parser.generated.syntaxtree.ReferencedType;
import com.github.openasn1.parser.generated.syntaxtree.RootAlternativeTypeList;
import com.github.openasn1.parser.generated.syntaxtree.RootComponentTypeList;
import com.github.openasn1.parser.generated.syntaxtree.RootEnumeration;
import com.github.openasn1.parser.generated.syntaxtree.SequenceType;
import com.github.openasn1.parser.generated.syntaxtree.TaggedType;
import com.github.openasn1.parser.generated.syntaxtree.Type;
import com.github.openasn1.parser.generated.visitor.DepthFirstVisitor;

/**
 * @author Clayton Hoss
 * 
 */
public class TypeAttributes {

	public static class IsTypeExtensibleVisitor extends DepthFirstVisitor {

		private boolean extensible = false;

		private ASN1ASTNodeInfos nodeInfos;

		/**
		 * @param nodeInfos
		 */
		public IsTypeExtensibleVisitor(ASN1ASTNodeInfos nodeInfos) {
			super();
			this.nodeInfos = nodeInfos;
		}

		/**
		 * @see com.github.openasn1.parser.generated.visitor.DepthFirstVisitor#visit(com.github.openasn1.parser.generated.syntaxtree.NodeToken)
		 */
		@Override
		public void visit(NodeToken n) {
			if (n.kind == ASN1ParserConstants.ELLIPSIS_TKN) {
				setExtensible(true);
			}
			super.visit(n);
		}

		/**
		 * @return the extensible
		 */
		public boolean isExtensible() {
			return this.extensible;
		}

		/**
		 * @param extensible
		 *            the extensible to set
		 */
		private void setExtensible(boolean extensible) {
			this.extensible = extensible;
		}

		/**
		 * @see com.github.openasn1.parser.generated.visitor.DepthFirstVisitor#visit(com.github.openasn1.parser.generated.syntaxtree.Constraint)
		 */
		@Override
		public void visit(Constraint n) {
			// Abort visiting
		}

		/**
		 * @see com.github.openasn1.parser.generated.visitor.DepthFirstVisitor#visit(com.github.openasn1.parser.generated.syntaxtree.ExtensionAdditionAlternatives)
		 */
		@Override
		public void visit(ExtensionAdditionAlternatives n) {
			// Abort visiting
		}

		/**
		 * @see com.github.openasn1.parser.generated.visitor.DepthFirstVisitor#visit(com.github.openasn1.parser.generated.syntaxtree.RootAlternativeTypeList)
		 */
		@Override
		public void visit(RootAlternativeTypeList n) {
			// Abort visiting
		}

		/**
		 * @see com.github.openasn1.parser.generated.visitor.DepthFirstVisitor#visit(com.github.openasn1.parser.generated.syntaxtree.AdditionalEnumeration)
		 */
		@Override
		public void visit(AdditionalEnumeration n) {
			// Abort visiting
		}

		@Override
		public void visit(RootEnumeration n) {
			// Abort visiting
		}

		/**
		 * @see com.github.openasn1.parser.generated.visitor.DepthFirstVisitor#visit(com.github.openasn1.parser.generated.syntaxtree.RootComponentTypeList)
		 */
		@Override
		public void visit(RootComponentTypeList n) {
			// Abort visiting
		}

		/**
		 * @see com.github.openasn1.parser.generated.visitor.DepthFirstVisitor#visit(com.github.openasn1.parser.generated.syntaxtree.ExtensionAdditions)
		 */
		@Override
		public void visit(ExtensionAdditions n) {
			// Abort visiting
		}

		/**
		 * @see com.github.openasn1.parser.generated.visitor.DepthFirstVisitor#visit(com.github.openasn1.parser.generated.syntaxtree.DefinedType)
		 */
		@Override
		public void visit(Type n) {
			if (TypeAttributes.isDefinedType(n)) {
				Type t = IdentifierMapHelper.getTypeFromDefinedTypeRecursive(n,
						getNodeInfos());
				t.accept(this);
			} else {
				super.visit(n);
			}
		}

		/**
		 * @return the nodeInfos
		 */
		private ASN1ASTNodeInfos getNodeInfos() {
			return this.nodeInfos;
		}

	}

	public static boolean isBuiltinType(Type n) {
		if (!(n.nodeChoice.choice instanceof NormalConstrainedType)) {
			return false;
		}
		NormalConstrainedType n0 = (NormalConstrainedType) n.nodeChoice.choice;
		if (!(n0.nodeChoice.choice instanceof BuiltinType)) {
			return false;
		}
		return true;
	}

	public static boolean isReferencedType(Type n) {
		if (!(n.nodeChoice.choice instanceof NormalConstrainedType)) {
			return false;
		}
		NormalConstrainedType n0 = (NormalConstrainedType) n.nodeChoice.choice;
		if (!(n0.nodeChoice.choice instanceof ReferencedType)) {
			return false;
		}
		return true;
	}

	public static boolean isTaggedType(Type n) {
		if (!isBuiltinType(n)) {
			return false;
		}
		return (getBuiltintType(n).nodeChoice.choice instanceof TaggedType);
	}

	public static ReferencedType getReferencedType(Type n) {
		if (!isReferencedType(n)) {
			return null;
		}
		NormalConstrainedType n0 = (NormalConstrainedType) n.nodeChoice.choice;
		return (ReferencedType) n0.nodeChoice.choice;
	}

	public static boolean isDefinedType(Type n) {
		if (!isReferencedType(n)) {
			return false;
		}
		return (getReferencedType(n).nodeChoice.choice instanceof DefinedType);
	}

	public static DefinedType getDefinedType(Type n) {
		if (!isDefinedType(n)) {
			return null;
		}
		return (DefinedType) getReferencedType(n).nodeChoice.choice;
	}

	public static BuiltinType getBuiltintType(Type n) {
		if (!isBuiltinType(n)) {
			return null;
		}
		NormalConstrainedType n0 = (NormalConstrainedType) n.nodeChoice.choice;

		return (BuiltinType) n0.nodeChoice.choice;
	}

	public static boolean isChoiceType(Type n) {
		if (!isBuiltinType(n)) {
			return false;
		}
		return (getBuiltintType(n).nodeChoice.choice instanceof ChoiceType);
	}

	public static boolean isSequenceType(Type n) {
		if (!isBuiltinType(n)) {
			return false;
		}
		return (getBuiltintType(n).nodeChoice.choice instanceof SequenceType);
	}

	public static boolean isEnumeratedType(Type n) {
		if (!isBuiltinType(n)) {
			return false;
		}
		return (getBuiltintType(n).nodeChoice.choice instanceof EnumeratedType);
	}

	public static EnumeratedType getEnumeratedType(Type n) {
		if (!isEnumeratedType(n)) {
			return null;
		}
		return ((EnumeratedType) getBuiltintType(n).nodeChoice.choice);
	}

	public static boolean isInSetOrSequenceAndOptional(Type n) {
		if (n.getParent() instanceof NamedType) {
			NamedType nt = (NamedType) n.getParent();
			if (nt.getParent().getParent().getParent() instanceof ComponentType) {
				ComponentType ct = (ComponentType) nt.getParent().getParent()
						.getParent();
				return ComponentTypeAttributes.isOptional(ct);
			}
		}
		return false;
	}

	public static boolean isTypeExtensible(Type n, ASN1ASTNodeInfos nodeInfos) {
		IsTypeExtensibleVisitor vis = new IsTypeExtensibleVisitor(nodeInfos);
		n.accept(vis);
		return vis.isExtensible();
	}

	public static boolean isInExtension(Type n) {
		Node node = n.getParent();
		while ((node != null)) {

			if (node instanceof Type) {
				return false;
			}
			if (node instanceof Enumerations) {
				return false;
			}

			if (node instanceof AlternativeTypeLists) {
				return false;
			}
			if (node instanceof ComponentTypeLists) {
				return false;
			}
			if (node instanceof AdditionalEnumeration) {
				return true;
			}
			if (node instanceof ExtensionAdditionAlternatives) {
				return true;
			}
			if (node instanceof ExtensionAdditions) {
				return true;
			}
			node = node.getParent();
		}
		return false;
	}
}
