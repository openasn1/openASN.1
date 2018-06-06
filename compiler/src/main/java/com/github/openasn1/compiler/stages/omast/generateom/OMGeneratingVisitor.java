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
package com.github.openasn1.compiler.stages.omast.generateom;

import java.util.Collections;
import java.util.Stack;

import org.apache.log4j.Logger;
import com.github.openasn1.compiler.Exceptions.CompilationStoppingException;
import com.github.openasn1.compiler.astutils.ASN1ASTNodeInfos;
import com.github.openasn1.compiler.astutils.helpers.LogginHelper;
import com.github.openasn1.compiler.omast.ASN1ImportListFromModule;
import com.github.openasn1.compiler.omast.ASN1Module;
import com.github.openasn1.compiler.omast.ASN1TypeAssignment;
import com.github.openasn1.compiler.omast.ASN1TypeAssignmentList;
import com.github.openasn1.compiler.omast.AbstractASN1Type;
import com.github.openasn1.compiler.omast.AggregatedASN1Type;
import com.github.openasn1.compiler.omast.AggregatedASN1TypeEnum;
import com.github.openasn1.compiler.omast.ComplexASN1Type;
import com.github.openasn1.compiler.omast.ComplexASN1TypeEnum;
import com.github.openasn1.compiler.omast.EnumASN1Type;
import com.github.openasn1.compiler.omast.ReferencedASN1Type;
import com.github.openasn1.compiler.omast.SimpleASN1Type;
import com.github.openasn1.compiler.omast.SimpleASN1TypeEnum;
import com.github.openasn1.parser.attributes.DefinedTypeAttributes;
import com.github.openasn1.parser.attributes.EnumerationItemAttributes;
import com.github.openasn1.parser.attributes.ReferenceAttributes;
import com.github.openasn1.parser.attributes.TypeWithConstraintAttributes;
import com.github.openasn1.parser.generated.ASN1ParserConstants;
import com.github.openasn1.parser.generated.syntaxtree.AssignmentList;
import com.github.openasn1.parser.generated.syntaxtree.BooleanType;
import com.github.openasn1.parser.generated.syntaxtree.ChoiceType;
import com.github.openasn1.parser.generated.syntaxtree.DefinedType;
import com.github.openasn1.parser.generated.syntaxtree.EnumeratedType;
import com.github.openasn1.parser.generated.syntaxtree.EnumerationItem;
import com.github.openasn1.parser.generated.syntaxtree.IntegerType;
import com.github.openasn1.parser.generated.syntaxtree.ModuleDefinition;
import com.github.openasn1.parser.generated.syntaxtree.ModuleIdentifier;
import com.github.openasn1.parser.generated.syntaxtree.NamedType;
import com.github.openasn1.parser.generated.syntaxtree.NodeToken;
import com.github.openasn1.parser.generated.syntaxtree.NullType;
import com.github.openasn1.parser.generated.syntaxtree.ObjectIdentifierType;
import com.github.openasn1.parser.generated.syntaxtree.OctetStringType;
import com.github.openasn1.parser.generated.syntaxtree.RestrictedCharacterStringType;
import com.github.openasn1.parser.generated.syntaxtree.SequenceOfType;
import com.github.openasn1.parser.generated.syntaxtree.SequenceType;
import com.github.openasn1.parser.generated.syntaxtree.SetOfType;
import com.github.openasn1.parser.generated.syntaxtree.SetType;
import com.github.openasn1.parser.generated.syntaxtree.Symbol;
import com.github.openasn1.parser.generated.syntaxtree.SymbolsFromModule;
import com.github.openasn1.parser.generated.syntaxtree.Type;
import com.github.openasn1.parser.generated.syntaxtree.TypeAssignment;
import com.github.openasn1.parser.generated.syntaxtree.TypeWithConstraint;
import com.github.openasn1.parser.generated.syntaxtree.ValueAssignment;
import com.github.openasn1.parser.generated.visitor.DepthFirstVisitor;


/**
 * @author Clayton Hoss
 *
 */
public class OMGeneratingVisitor extends DepthFirstVisitor {

	private static Logger LOGGER = Logger.getLogger("OMGeneratingVisitor");

	private boolean isRetPrimitive; // needs to be set just before EXITING the

	private boolean inline = false;

	private Stack<AbstractASN1Type> returnStack = new Stack<AbstractASN1Type>();

	private ASN1ASTNodeInfos nodeInfos;

	public OMGeneratingVisitor(ASN1ASTNodeInfos nodeinfos) {
		this.nodeInfos = nodeinfos;
	}

	@Override
	public void visit(TypeAssignment n) {
		Object guard = getReturnStack().peek();
		super.visit(n);
		if (getReturnStack().peek() == guard) {
			throw new CompilationStoppingException(
					"No type node added to the stack, most likely encountered unsupported type "
							+ LogginHelper.getCurrentLine(n));
		}
		ASN1TypeAssignment temp = new ASN1TypeAssignment(
				getReturnStack().pop(), isRetPrimitive());
		temp.setName(n.nodeToken.tokenImage);
		temp.getAsn1type().setName(n.nodeToken.tokenImage);
		temp.setCorrespondingASN1Node(n);
		getReturnStack().push(temp);
	}

	/**
	 * @see com.github.openasn1.parser.generated.visitor.DepthFirstVisitor#visit(com.github.openasn1.parser.generated.syntaxtree.DefinedType)
	 */
	@Override
	public void visit(DefinedType n) {
		if (isInline()) {
			LOGGER.debug("Following a defined Type at line "
					+ LogginHelper.getCurrentLine(n));
			TypeAssignment ts = (TypeAssignment) getNodeInfos()
					.returnInfoFromNode(n, "IdentifierMap");
			Type type = ts.type;

			super.visit(type);
		} else {
			LOGGER.debug("Added ASN1ReferencedType for Type at line "
					+ LogginHelper.getCurrentLine(n));
			ReferencedASN1Type ref = new ReferencedASN1Type();
			ref.setCorrespondingASN1Node(n);
			ref.setReferencedName(DefinedTypeAttributes.getSymbol(n));
			getReturnStack().add(ref);
		}
	}

	/**
	 * @see com.github.openasn1.parser.generated.visitor.DepthFirstVisitor#visit(com.github.openasn1.parser.generated.syntaxtree.SequenceType)
	 */
	@Override
	public void visit(SequenceType n) {
		Object guard = getReturnStack().peek();
		super.visit(n);
		// Empty Sequences are allowed!
		// if (getReturnStack().peek() == guard) {
		// throw new CompilationStoppingException(
		// "No type node added to the stack, most likely empty sequence "
		// + LogginHelper.getCurrentLine(n));
		// }
		ComplexASN1Type ct = new ComplexASN1Type();
		ct.setCorrespondingASN1Node(n);
		ct.setAsn1Type(ComplexASN1TypeEnum.SEQUENCE);

		while (getReturnStack().peek() != guard) {
			AbstractASN1Type listitem = getReturnStack().pop();
			ct.addChild(listitem);
			LOGGER.debug("Adding Child to the Sequence");
		}
		Collections.reverse(ct.getChildren());
		getReturnStack().push(ct);
		setRetPrimitive(false);
		LOGGER.debug("Adding a Sequence the stack "
				+ LogginHelper.getCurrentLine(n));

	}

	/**
	 * @see com.github.openasn1.parser.generated.visitor.DepthFirstVisitor#visit(com.github.openasn1.parser.generated.syntaxtree.NamedType)
	 */
	@Override
	public void visit(NamedType n) {
		super.visit(n);
		String name = n.nodeToken.tokenImage;
		getReturnStack().peek().setName(name);
	}

	/**
	 * @see com.github.openasn1.parser.generated.visitor.DepthFirstVisitor#visit(com.github.openasn1.parser.generated.syntaxtree.SequenceOfType)
	 */
	@Override
	public void visit(SequenceOfType n) {
		super.visit(n);
		AggregatedASN1Type type = new AggregatedASN1Type();
		type.setAsn1Type(AggregatedASN1TypeEnum.SEQUENCEOF);
		AbstractASN1Type child = getReturnStack().pop();
		type.setCorrespondingASN1Node(n);
		type.setChild(child);
		getReturnStack().push(type);
	}

	/**
	 * @see com.github.openasn1.parser.generated.visitor.DepthFirstVisitor#visit(com.github.openasn1.parser.generated.syntaxtree.SetOfType)
	 */
	@Override
	public void visit(SetOfType n) {
		super.visit(n);
		AggregatedASN1Type type = new AggregatedASN1Type();
		type.setAsn1Type(AggregatedASN1TypeEnum.SETOF);
		AbstractASN1Type child = getReturnStack().pop();
		type.setChild(child);
		getReturnStack().push(type);
	}

	/**
	 * @see com.github.openasn1.parser.generated.visitor.DepthFirstVisitor#visit(com.github.openasn1.parser.generated.syntaxtree.TypeWithConstraint)
	 */
	@Override
	public void visit(TypeWithConstraint n) {
		super.visit(n);
		AggregatedASN1Type type = new AggregatedASN1Type();
		if (TypeWithConstraintAttributes.isSequenceOf(n)) {
			type.setAsn1Type(AggregatedASN1TypeEnum.SEQUENCEOF);
		}

		if (TypeWithConstraintAttributes.isSetOf(n)) {
			type.setAsn1Type(AggregatedASN1TypeEnum.SETOF);
		}

		AbstractASN1Type child = getReturnStack().pop();
		type.setChild(child);
		getReturnStack().push(type);
	}

	/**
	 * @see com.github.openasn1.parser.generated.visitor.DepthFirstVisitor#visit(com.github.openasn1.parser.generated.syntaxtree.Type)
	 */
	@Override
	public void visit(Type n) {
		super.visit(n);
		getReturnStack().peek().setCorrespondingASN1Node(n);
		LOGGER.debug("Setting Corressponding ASN.1 Node");
	}

	/**
	 * @see com.github.openasn1.parser.generated.visitor.DepthFirstVisitor#visit(com.github.openasn1.parser.generated.syntaxtree.SetType)
	 */
	@Override
	public void visit(SetType n) {
		Object guard = getReturnStack().peek();
		super.visit(n);
		if (getReturnStack().peek() == guard) {
			throw new CompilationStoppingException(
					"No type node added to the stack, most likely empty set "
							+ LogginHelper.getCurrentLine(n));
		}
		ComplexASN1Type ct = new ComplexASN1Type();
		ct.setCorrespondingASN1Node(n);
		ct.setAsn1Type(ComplexASN1TypeEnum.SET);
		while (getReturnStack().peek() != guard) {
			AbstractASN1Type listitem = getReturnStack().pop();
			ct.addChild(listitem);
			LOGGER.debug("Adding Child to the Set");
		}
		Collections.reverse(ct.getChildren());
		getReturnStack().push(ct);
		setRetPrimitive(false);
		LOGGER
				.debug("Adding a Set the stack "
						+ LogginHelper.getCurrentLine(n));
	}

	/**
	 *
	 * @see com.github.openasn1.parser.generated.visitor.DepthFirstVisitor#visit(com.github.openasn1.parser.generated.syntaxtree.RestrictedCharacterStringType)
	 */
	@Override
	public void visit(RestrictedCharacterStringType n) {
		SimpleASN1Type temp = new SimpleASN1Type();
		temp.setCorrespondingASN1Node(n);
		setRetPrimitive(true);
		NodeToken token = (NodeToken) n.nodeChoice.choice;
		boolean supportedType = false;

		if (token.kind == ASN1ParserConstants.VISIBLESTRING_TKN) {
			supportedType = true;
			temp.setAsn1Type(SimpleASN1TypeEnum.VisibleString);
		}

		if (token.kind == ASN1ParserConstants.UTF8STRING_TKN) {
			supportedType = true;
			temp.setAsn1Type(SimpleASN1TypeEnum.UTF8String);
		}

		if (token.kind == ASN1ParserConstants.GENERALSTRING_TKN) {
			supportedType = true;
			temp.setAsn1Type(SimpleASN1TypeEnum.GeneralString);
		}

		if (token.kind == ASN1ParserConstants.BMPSTRING_TKN) {
			supportedType = true;
			temp.setAsn1Type(SimpleASN1TypeEnum.BMPString);
		}

		if (token.kind == ASN1ParserConstants.IA5STRING_TKN) {
			supportedType = true;
			temp.setAsn1Type(SimpleASN1TypeEnum.IA5String);
		}

		if (token.kind == ASN1ParserConstants.NUMERICSTRING_TKN) {
			supportedType = true;
			temp.setAsn1Type(SimpleASN1TypeEnum.NumericString);
		}

		if (token.kind == ASN1ParserConstants.PRINTABLESTRING_TKN) {
			supportedType = true;
			temp.setAsn1Type(SimpleASN1TypeEnum.PrintableString);
		}

		if (!supportedType) {
			throw new CompilationStoppingException("Unsuported String type '"
					+ token.kind + "' encountered");
		}

		getReturnStack().push(temp);

		LOGGER.debug("Added ASN1 Simple Type for Type at line "
				+ LogginHelper.getCurrentLine(n));
	}

	/**
	 * @see com.github.openasn1.parser.generated.visitor.DepthFirstVisitor#visit(com.github.openasn1.parser.generated.syntaxtree.IntegerType)
	 */
	@Override
	public void visit(IntegerType n) {
		SimpleASN1Type temp = new SimpleASN1Type();
		temp.setCorrespondingASN1Node(n);
		setRetPrimitive(true);
		temp.setAsn1Type(SimpleASN1TypeEnum.INTEGER);
		getReturnStack().push(temp);
	}

	/**
	 * @see com.github.openasn1.parser.generated.visitor.DepthFirstVisitor#visit(com.github.openasn1.parser.generated.syntaxtree.AssignmentList)
	 */
	@Override
	public void visit(AssignmentList n) {
		AbstractASN1Type marker = getReturnStack().peek();
		super.visit(n);
		ASN1TypeAssignmentList list = new ASN1TypeAssignmentList();
		while (getReturnStack().peek() != marker) {
			AbstractASN1Type listitem = getReturnStack().pop();
			list.add((ASN1TypeAssignment) listitem);
		}
		getReturnStack().push(list);
	}

	/**
	 * @return the isRetPrimitive
	 */
	private boolean isRetPrimitive() {
		return this.isRetPrimitive;
	}

	/**
	 * @param isRetPrimitive
	 *            the isRetPrimitive to set
	 */
	private void setRetPrimitive(boolean isRetPrimitive) {
		this.isRetPrimitive = isRetPrimitive;
	}

	/**
	 * @return the returnStack
	 */
	private Stack<AbstractASN1Type> getReturnStack() {
		return this.returnStack;
	}

	public AbstractASN1Type getOMAstRoot() {
		LOGGER.debug("Size of stack when returning OM AST root: "
				+ getReturnStack().size());
		return getReturnStack().peek();
	}

	/**
	 * @return the nodeInfos
	 */
	private ASN1ASTNodeInfos getNodeInfos() {
		return this.nodeInfos;
	}

	/**
	 * @param inline
	 *            the inline to set
	 */
	public void setInline(boolean inline) {
		this.inline = inline;
	}

	/**
	 * @return the inline
	 */
	private boolean isInline() {
		return this.inline;
	}

	/**
	 * @see com.github.openasn1.parser.generated.visitor.DepthFirstVisitor#visit(com.github.openasn1.parser.generated.syntaxtree.EnumeratedType)
	 */
	@Override
	public void visit(EnumeratedType n) {
		EnumASN1Type enumer = new EnumASN1Type();
		getReturnStack().push(enumer);
		super.visit(n);
	}

	/**
	 * @see com.github.openasn1.parser.generated.visitor.DepthFirstVisitor#visit(com.github.openasn1.parser.generated.syntaxtree.EnumerationItem)
	 */
	@Override
	public void visit(EnumerationItem n) {
		String name = EnumerationItemAttributes.getName(n);
		Integer value = (Integer) getNodeInfos().returnInfoFromNode(n, "EnumValue");
		((EnumASN1Type) (getReturnStack().peek())).addEnum(name, value);
		super.visit(n);
	}

	/**
	 * @see com.github.openasn1.parser.generated.visitor.DepthFirstVisitor#visit(com.github.openasn1.parser.generated.syntaxtree.ObjectIdentifierType)
	 */
	@Override
	public void visit(ObjectIdentifierType n) {
		SimpleASN1Type temp = new SimpleASN1Type();
		temp.setCorrespondingASN1Node(n);
		setRetPrimitive(true);
		temp.setAsn1Type(SimpleASN1TypeEnum.OBJECTIDENTIFIER);
		getReturnStack().push(temp);
		super.visit(n);
	}

	/**
	 * @see com.github.openasn1.parser.generated.visitor.DepthFirstVisitor#visit(com.github.openasn1.parser.generated.syntaxtree.NullType)
	 */
	@Override
	public void visit(NullType n) {
		SimpleASN1Type temp = new SimpleASN1Type();
		temp.setCorrespondingASN1Node(n);
		setRetPrimitive(true);
		temp.setAsn1Type(SimpleASN1TypeEnum.NULL);
		getReturnStack().push(temp);
	}

	/**
	 * @see com.github.openasn1.parser.generated.visitor.DepthFirstVisitor#visit(com.github.openasn1.parser.generated.syntaxtree.OctetStringType)
	 */
	@Override
	public void visit(OctetStringType n) {
		SimpleASN1Type temp = new SimpleASN1Type();
		temp.setCorrespondingASN1Node(n);
		setRetPrimitive(true);
		temp.setAsn1Type(SimpleASN1TypeEnum.OCTETSTRING);
		getReturnStack().push(temp);
	}

	/**
	 * @see com.github.openasn1.parser.generated.visitor.DepthFirstVisitor#visit(com.github.openasn1.parser.generated.syntaxtree.BooleanType)
	 */
	@Override
	public void visit(BooleanType n) {
		SimpleASN1Type temp = new SimpleASN1Type();
		temp.setCorrespondingASN1Node(n);
		setRetPrimitive(true);
		temp.setAsn1Type(SimpleASN1TypeEnum.BOOLEAN);
		getReturnStack().push(temp);
	}

	/**
	 * @see com.github.openasn1.parser.generated.visitor.DepthFirstVisitor#visit(com.github.openasn1.parser.generated.syntaxtree.ModuleIdentifier)
	 */
	@Override
	public void visit(ModuleIdentifier n) {
		ASN1Module module = (ASN1Module) getReturnStack().peek();
		module.setName(n.modulereference.nodeToken.tokenImage);

		super.visit(n);
	}

	/**
	 * @see com.github.openasn1.parser.generated.visitor.DepthFirstVisitor#visit(com.github.openasn1.parser.generated.syntaxtree.SymbolsFromModule)
	 */
	@Override
	public void visit(SymbolsFromModule n) {
		ASN1ImportListFromModule imports = new ASN1ImportListFromModule();
		imports.setCorrespondingASN1Node(n);
		String moduleName = n.globalModuleReference.modulereference.nodeToken.tokenImage;
		imports.setModuleName(moduleName);
		getReturnStack().push(imports);
		super.visit(n);
	}

	/**
	 * @see com.github.openasn1.parser.generated.visitor.DepthFirstVisitor#visit(com.github.openasn1.parser.generated.syntaxtree.Symbol)
	 */
	@Override
	public void visit(Symbol n) {
		String ref = ReferenceAttributes.getSymbol(n.reference);

		if (ReferenceAttributes.isTypeReference(n.reference)) {
			if (getReturnStack().peek().getClass().equals(
					ASN1ImportListFromModule.class)) {
				((ASN1ImportListFromModule) getReturnStack().peek())
						.getImportNames().add(ref);
			}
		} else {
			// TODO let it function with ValueReferences, too
		}
		super.visit(n);
	}

	/**
	 * @see com.github.openasn1.parser.generated.visitor.DepthFirstVisitor#visit(com.github.openasn1.parser.generated.syntaxtree.ModuleDefinition)
	 */
	@Override
	public void visit(ModuleDefinition n) {
		ASN1Module module = new ASN1Module();
		getReturnStack().push(module);
		module.setCorrespondingASN1Node(n);
		super.visit(n);
		module.setTypeAssingmentList((ASN1TypeAssignmentList) getReturnStack()
				.pop());
		while (getReturnStack().peek() != module) {
			module.add((ASN1ImportListFromModule) getReturnStack().pop());
		}
	}

	/**
	 * @see com.github.openasn1.parser.generated.visitor.DepthFirstVisitor#visit(com.github.openasn1.parser.generated.syntaxtree.ChoiceType)
	 */
	@Override
	public void visit(ChoiceType n) {

		Object guard = getReturnStack().peek();
		super.visit(n);
		if (getReturnStack().peek() == guard) {
			throw new CompilationStoppingException(
					"No type node added to the stack, most likely empty Choice "
							+ LogginHelper.getCurrentLine(n));
		}
		ComplexASN1Type ct = new ComplexASN1Type();
		ct.setCorrespondingASN1Node(n);
		ct.setAsn1Type(ComplexASN1TypeEnum.CHOICE);

		while (getReturnStack().peek() != guard) {
			AbstractASN1Type listitem = getReturnStack().pop();
			ct.addChild(listitem);
			LOGGER.debug("Adding Child to the Choice");
		}
		Collections.reverse(ct.getChildren());
		getReturnStack().push(ct);
		setRetPrimitive(false);
		LOGGER.debug("Adding a Choice the stack "
				+ LogginHelper.getCurrentLine(n));

	}

	/**
	 * @see com.github.openasn1.parser.generated.visitor.DepthFirstVisitor#visit(com.github.openasn1.parser.generated.syntaxtree.ValueAssignment)
	 */
	@Override
	public void visit(ValueAssignment n) {
		// ignore Value Assignemtns for now
		return;
	}

}
