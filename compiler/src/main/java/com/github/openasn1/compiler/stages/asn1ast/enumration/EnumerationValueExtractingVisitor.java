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
package com.github.openasn1.compiler.stages.asn1ast.enumration;

import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;
import com.github.openasn1.compiler.astutils.ASN1ASTNodeInfos;
import com.github.openasn1.parser.attributes.EnumerationItemAttributes;
import com.github.openasn1.parser.attributes.NamedNumberAttributes;
import com.github.openasn1.parser.generated.syntaxtree.AdditionalEnumeration;
import com.github.openasn1.parser.generated.syntaxtree.EnumeratedType;
import com.github.openasn1.parser.generated.syntaxtree.EnumerationItem;
import com.github.openasn1.parser.generated.syntaxtree.NamedNumber;
import com.github.openasn1.parser.generated.syntaxtree.RootEnumeration;
import com.github.openasn1.parser.generated.visitor.DepthFirstVisitor;


/**
 * Implements X680 Topic 19
 *
 * @author Clayton Hoss
 *
 */
public class EnumerationValueExtractingVisitor extends DepthFirstVisitor {

	private static Logger LOGGER = Logger
			.getLogger("EnumerationValueExtractingVisitor");

	private enum Stage {
		RootEnumNamedNumberEnums, RootEnumIdentifierEnums, AddionalEnum
	}

	private ASN1ASTNodeInfos nodeinfos;

	private Set<Integer> definedNumbers = new HashSet<Integer>();

	private int nextAutomaticNumber = 0;

	private Stage currentStage;

	private boolean firstAdditionalItem = true;

	public EnumerationValueExtractingVisitor(ASN1ASTNodeInfos nodeinfos) {
		this.nodeinfos = nodeinfos;
	}

	@Override
	public void visit(AdditionalEnumeration n) {
		setCurrentStage(Stage.AddionalEnum);
		super.visit(n);
	}

	@Override
	public void visit(EnumeratedType n) {
		// Initialize all variables
		setNextAutomaticNumber(0);
		setCurrentStage(Stage.RootEnumNamedNumberEnums);
		getDefinedNumbers().clear();
		super.visit(n);
	}

	@Override
	public void visit(EnumerationItem n) {
		if (getCurrentStage() == Stage.RootEnumNamedNumberEnums) {
			if (EnumerationItemAttributes.isNamedNumber(n)) {
				NamedNumber number = EnumerationItemAttributes
						.getNamedNumber(n);
				Integer val = NamedNumberAttributes.getValue(number);
				getNodeinfos().insertInfoIntoNode(n, "EnumValue", val);
				LOGGER.debug("Adding NamedNumber in RootEnumeration: "
						+ EnumerationItemAttributes.getName(n) + " " + val);
				getDefinedNumbers().add(val);
			}
		}

		if (getCurrentStage() == Stage.RootEnumIdentifierEnums) {
			if (EnumerationItemAttributes.isIdentifier(n)) {
				int number = getNextAutomaticNumber();
				while (getDefinedNumbers().contains(number)) {
					number++;
				}
				getNodeinfos().insertInfoIntoNode(n, "EnumValue", number);
				LOGGER.debug("Adding Identifier in RootEnumeration: "
						+ EnumerationItemAttributes.getName(n) + " " + number);
				setNextAutomaticNumber(number + 1);
			}
		}

		if (getCurrentStage() == Stage.AddionalEnum) {
			if (EnumerationItemAttributes.isNamedNumber(n)) {
				NamedNumber number = EnumerationItemAttributes
						.getNamedNumber(n);
				Integer val = NamedNumberAttributes.getValue(number);
				getNodeinfos().insertInfoIntoNode(n, "EnumValue", val);
				LOGGER.debug("Adding NamedNumber in AdditionalEnumeration: "
						+ EnumerationItemAttributes.getName(n) + " " + val);
				getDefinedNumbers().add(val);
				if (!isFirstAdditionalItem()) {
					setNextAutomaticNumber(val + 1);
				}
			}

			if (EnumerationItemAttributes.isIdentifier(n)) {
				setFirstAdditionalItem(false);
				int number = getNextAutomaticNumber();
				while (getDefinedNumbers().contains(number)) {
					number++;
				}
				getNodeinfos().insertInfoIntoNode(n, "EnumValue", number);
				LOGGER.debug("Adding Identifier in AdditionalEnumeration: "
						+ EnumerationItemAttributes.getName(n) + " " + number);
				setNextAutomaticNumber(number + 1);
			}
		}

		super.visit(n);
	}

	@Override
	public void visit(RootEnumeration n) {
		setCurrentStage(Stage.RootEnumNamedNumberEnums);
		// Process Root Enumeration for all NamedNumbers
		super.visit(n);
		// Process Root Enumeration for all non-NamedNumbers^
		setCurrentStage(Stage.RootEnumIdentifierEnums);
		super.visit(n);
	}

	/**
	 * @return the definedNumbers
	 */
	private Set<Integer> getDefinedNumbers() {
		return this.definedNumbers;
	}

	/**
	 * @return the nextAutomaticNumber
	 */
	private int getNextAutomaticNumber() {
		return this.nextAutomaticNumber;
	}

	/**
	 * @return the nodeinfos
	 */
	private ASN1ASTNodeInfos getNodeinfos() {
		return this.nodeinfos;
	}

	/**
	 * @param nextAutomaticNumber
	 *            the nextAutomaticNumber to set
	 */
	private void setNextAutomaticNumber(int nextAutomaticNumber) {
		this.nextAutomaticNumber = nextAutomaticNumber;
	}

	/**
	 * @return the currentStage
	 */
	private Stage getCurrentStage() {
		return this.currentStage;
	}

	/**
	 * @param currentStage
	 *            the currentStage to set
	 */
	private void setCurrentStage(Stage currentStage) {
		this.currentStage = currentStage;
	}

	/**
	 * @return the firstAdditionalItem
	 */
	private boolean isFirstAdditionalItem() {
		return this.firstAdditionalItem;
	}

	/**
	 * @param firstAdditionalItem
	 *            the firstAdditionalItem to set
	 */
	private void setFirstAdditionalItem(boolean firstAdditionalItem) {
		this.firstAdditionalItem = firstAdditionalItem;
	}

}
