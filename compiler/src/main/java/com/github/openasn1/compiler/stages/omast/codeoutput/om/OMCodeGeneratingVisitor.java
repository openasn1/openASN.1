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
package com.github.openasn1.compiler.stages.omast.codeoutput.om;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

import org.apache.log4j.Logger;
import com.github.openasn1.compiler.codeoutpututils.ClassInformationHolder;
import com.github.openasn1.compiler.omast.ASN1ImportListFromModule;
import com.github.openasn1.compiler.omast.ASN1Module;
import com.github.openasn1.compiler.omast.ASN1TypeAssignment;
import com.github.openasn1.compiler.omast.ASN1TypeAssignmentList;
import com.github.openasn1.compiler.omast.AbstractOMVisitor;
import com.github.openasn1.compiler.omast.AggregatedASN1Type;
import com.github.openasn1.compiler.omast.ComplexASN1Type;
import com.github.openasn1.compiler.omast.EnumASN1Type;
import com.github.openasn1.compiler.omast.ReferencedASN1Type;
import com.github.openasn1.compiler.omast.SimpleASN1Type;
import com.github.openasn1.compiler.omast.SimpleASN1TypeEnum;
import com.github.openasn1.compiler.stages.omast.codeoutput.utils.CanonicalTypeNamingVisitor;
import com.github.openasn1.compiler.stages.omast.codeoutput.utils.InnerComponentNamingStrategy;


public class OMCodeGeneratingVisitor extends AbstractOMVisitor {
	private static Logger LOGGER = Logger
			.getLogger("OMCoderCodeGeneratingVisitor");

	private static HashMap<String, String> typeMapping = new HashMap<String, String>();

	private static String SUBPACKAGENAME = ".om";

	static {
		typeMapping.put(SimpleASN1TypeEnum.INTEGER.name(), "Integer");
		typeMapping.put(SimpleASN1TypeEnum.BOOLEAN.name(), "Boolean");
		typeMapping.put(SimpleASN1TypeEnum.BMPString.name(), "String");
		typeMapping.put(SimpleASN1TypeEnum.GeneralString.name(), "String");
		typeMapping.put(SimpleASN1TypeEnum.UTF8String.name(), "String");
		typeMapping.put(SimpleASN1TypeEnum.VisibleString.name(), "String");
		typeMapping.put(SimpleASN1TypeEnum.IA5String.name(), "String");
		typeMapping.put(SimpleASN1TypeEnum.NumericString.name(), "String");
		typeMapping.put(SimpleASN1TypeEnum.PrintableString.name(), "String");
		typeMapping.put(SimpleASN1TypeEnum.OBJECTIDENTIFIER.name(),
				"List<Integer>");
		typeMapping.put(SimpleASN1TypeEnum.OCTETSTRING.name(), "byte[]");
		typeMapping.put(SimpleASN1TypeEnum.NULL.name(), "Class<Void>");
	}

	/**
	 * holds types on root level. Finally ending up in type assignment
	 */
	private Stack<ClassInformationHolder> classInformationStack = new Stack<ClassInformationHolder>();

	private int stackArrayCounter = 0;

	private String moduleName;

	private List<String> importList = new ArrayList<String>();

	private List<ClassInformationHolder> generatedClassInformationList = new ArrayList<ClassInformationHolder>();

	public OMCodeGeneratingVisitor() {
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.github.openasn1.compiler.omast.AbstractOMVisitor#visit(com.github.openasn1.compiler.omast.ASN1ImportListFromModule)
	 */
	@Override
	public void visit(ASN1ImportListFromModule type) {
		String name = "";
		if (!SUBPACKAGENAME.equals("")) {
			name = SUBPACKAGENAME + ".";
		}

		for (String s : type.getImportNames()) {
			getImportList().add(type.getModuleName() + name + s);
		}

		super.visit(type);
	}

	/**
	 * @see com.github.openasn1.compiler.omast.AbstractOMVisitor#visit(com.github.openasn1.compiler.omast.ASN1Module)
	 */
	@Override
	public void visit(ASN1Module type) {
		setModuleName(type.getName());
		super.visit(type);
	}

	private String getMappedSimpleType(String name) {
		if (getStackArrayCounter() > 0) {
			decrementStackArrayCounter();
			return "List<" + getMappedSimpleType(name) + ">";
		}
		return typeMapping.get(name);
	}

	private String getMappedType(String name) {
		if (getStackArrayCounter() > 0) {
			decrementStackArrayCounter();
			return "List<" + getMappedType(name) + ">";
		}
		return name;
	}

	/**
	 * @return the moduleName
	 */
	protected String getModuleName() {
		return this.moduleName;
	}

	/**
	 * @return the typeMapping
	 */
	protected static HashMap<String, String> getTypeMapping() {
		return typeMapping;
	}

	/**
	 * @param moduleName
	 *            the moduleName to set
	 */
	protected void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}

	/**
	 * @return the classInformationStack
	 */
	protected Stack<ClassInformationHolder> getTypeStack() {
		return this.classInformationStack;
	}

	/**
	 * @return the stackArrayCounter
	 */
	protected int getStackArrayCounter() {
		return this.stackArrayCounter;
	}

	/**
	 * @param stackArrayCounter
	 *            the stackArrayCounter to set
	 */
	protected void setStackArrayCounter(int stackArrayCounter) {
		this.stackArrayCounter = stackArrayCounter;
	}

	/**
	 * @see com.github.openasn1.compiler.omast.AbstractOMVisitor#visit(com.github.openasn1.compiler.omast.ASN1TypeAssignment)
	 */
	@Override
	public void visit(ASN1TypeAssignment type) {
		super.visit(type);

		getGeneratedClassInformationList().add(getTypeStack().peek());
		getTypeStack().clear();
		resetStackArrayCounter();
	}

	private void resetStackArrayCounter() {
		setStackArrayCounter(0);
	}

	private void incrementStackArrayCounter() {
		setStackArrayCounter(getStackArrayCounter() + 1);
	}

	private void decrementStackArrayCounter() {
		setStackArrayCounter(getStackArrayCounter() - 1);
	}

	/**
	 * @see com.github.openasn1.compiler.omast.AbstractOMVisitor#visit(com.github.openasn1.compiler.omast.ASN1TypeAssignmentList)
	 */
	@Override
	public void visit(ASN1TypeAssignmentList type) {
		super.visit(type);
	}

	/**
	 * @see com.github.openasn1.compiler.omast.AbstractOMVisitor#visit(com.github.openasn1.compiler.omast.ComplexASN1Type)
	 */
	@Override
	public void visit(ComplexASN1Type type) {
		ClassInformationHolder classInformation = null;

		CanonicalTypeNamingVisitor visitor = new CanonicalTypeNamingVisitor();
		type.accept(visitor);
		String className = InnerComponentNamingStrategy.getInstance().getName(
				type.getName(), visitor.getNestingDepth());
		

		String javaType = getMappedType(className);

		if (getTypeStack().isEmpty()) {
			classInformation = createNewClassInformationHolder(javaType);
			LOGGER.debug("generating ComplexType '" + javaType + "'");
			getTypeStack().push(classInformation);
			super.visit(type);

		} else {
			classInformation = createNewClassInformationHolder(className);

			LOGGER.debug("  generating contained ComplexType '"
					+ type.getName() + "'");

			getTypeStack().push(classInformation);
			super.visit(type);
			getTypeStack().pop();
			getTypeStack().peek().addSubClass(classInformation);
			getTypeStack().peek().addMemberVariable(type.getName(), javaType);
		}
	}

	/**
	 * @see com.github.openasn1.compiler.omast.AbstractOMVisitor#visit(com.github.openasn1.compiler.omast.ReferencedASN1Type)
	 */
	@Override
	public void visit(ReferencedASN1Type type) {
		ClassInformationHolder classInformation = null;

		String javaType = getMappedType(type.getReferencedName());

		if (getTypeStack().isEmpty()) {
			classInformation = createNewClassInformationHolder(type.getName());
			/*
			 * .addCustomMethod( " public " + type.getName() + "(" + javaType + "
			 * value) {\n" + " super(value);\n" + " };\n\n" );
			 */

			classInformation.setExtendsClass(javaType);
			LOGGER.debug("generating ReferencedType '" + type.getName()
					+ "' extending Java type '" + javaType + "'");
			getTypeStack().push(classInformation);
		} else {
			classInformation = getTypeStack().peek();
			classInformation.addMemberVariable(type.getName(), javaType);
			LOGGER.debug("  generating contained ReferencedType '"
					+ type.getName() + "' as Java type '" + javaType + "'");
		}

		super.visit(type);
	}

	/**
	 * @param type
	 * @return
	 */
	private ClassInformationHolder createNewClassInformationHolder(String name) {
		ClassInformationHolder ci = new ClassInformationHolder(name,
				getModuleName() + SUBPACKAGENAME);
		for (String s : getImportList()) {
			ci.addImport(s);
		}
		return ci;

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.github.openasn1.compiler.omast.AbstractOMVisitor#visit(com.github.openasn1.compiler.omast.AggregatedASN1Type)
	 */
	@Override
	public void visit(AggregatedASN1Type type) {
		if (getTypeStack().isEmpty()) {
			ClassInformationHolder c = createNewClassInformationHolder(type
					.getName());
			getTypeStack().push(c);
			incrementStackArrayCounter();
			if (type.getChild().getName() == null) {
				type.getChild().setName("value");
			}
		} else {
			incrementStackArrayCounter();
			if (type.getChild().getName() == null) {
				type.getChild().setName(type.getName());
			}
		}
		super.visit(type);
	}

	/**
	 * @see com.github.openasn1.compiler.omast.AbstractOMVisitor#visit(com.github.openasn1.compiler.omast.SimpleASN1Type)
	 */
	@Override
	public void visit(SimpleASN1Type type) {
		ClassInformationHolder classInformation = null;

		String mappedType = getMappedSimpleType(type.getAsn1Type().name());

		if (getTypeStack().isEmpty()) {
			classInformation = createNewClassInformationHolder(type.getName());
			classInformation.addCustomMethod("	public " + type.getName() + "("
					+ mappedType + " value) {\n" + "		this.set"
					+ type.getName() + "(value);\n" + "	};\n\n");

			LOGGER.debug("generating SimpleType '" + type.getName()
					+ "' with containing ASN.1 type '"
					+ type.getAsn1Type().name() + "' as Java type '"
					+ mappedType + "'");
			getTypeStack().push(classInformation);
		} else {
			classInformation = getTypeStack().peek();
			LOGGER.debug("  generating contained SimpleType '" + type.getName()
					+ "' with containing ASN.1 type '"
					+ type.getAsn1Type().name() + "' as Java type '"
					+ mappedType + "'");
		}

		classInformation.addMemberVariable(type.getName(), mappedType);

		super.visit(type);
	}

	/**
	 * @see com.github.openasn1.compiler.omast.AbstractOMVisitor#visit(com.github.openasn1.compiler.omast.EnumASN1Type)
	 */
	@Override
	public void visit(EnumASN1Type type) {
		CanonicalTypeNamingVisitor visitor = new CanonicalTypeNamingVisitor();
		type.accept(visitor);
		String className = InnerComponentNamingStrategy.getInstance().getName(
				type.getName(), visitor.getNestingDepth());

		ClassInformationHolder classInformation = createNewClassInformationHolder(className);
		classInformation.setEnum(true);
		String javaType = getMappedType(className);

		for (String s : type.getEnums().keySet()) {
			classInformation.addMemberVariable(s, getMappedEnumItemType());
		}

		if (getTypeStack().isEmpty()) {
			LOGGER.debug("generating EnumType '" + javaType + "'");
			getTypeStack().push(classInformation);
		} else {
			LOGGER.debug("  generating contained EnumType '" + className + "'");
			getTypeStack().peek().addSubClass(classInformation);
			getTypeStack().peek().addMemberVariable(type.getName(), javaType);
		}

		super.visit(type);
	}

	/**
	 * @return
	 */
	private String getMappedEnumItemType() {
		return null;
	}

	/**
	 * @return the generatedClassInformationList
	 */
	public List<ClassInformationHolder> getGeneratedClassInformationList() {
		return this.generatedClassInformationList;
	}

	/**
	 * @return the importList
	 */
	protected List<String> getImportList() {
		return this.importList;
	}
}