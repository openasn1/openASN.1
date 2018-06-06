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
package com.github.openasn1.compiler.stages.asn1ast.RestrictionChecking;

import com.github.openasn1.compiler.astutils.ASN1ASTNodeInfos;
import com.github.openasn1.compiler.astutils.helpers.IdentifierMapHelper;
import com.github.openasn1.parser.attributes.ComponentTypeAttributes;
import com.github.openasn1.parser.attributes.DefinedValueAttributes;
import com.github.openasn1.parser.attributes.EnumeratedTypeAttributes;
import com.github.openasn1.parser.attributes.TypeAttributes;
import com.github.openasn1.parser.attributes.ValueAssignmentAttributes;
import com.github.openasn1.parser.attributes.ValueAttributes;
import com.github.openasn1.parser.generated.syntaxtree.BuiltinValue;
import com.github.openasn1.parser.generated.syntaxtree.ComponentType;
import com.github.openasn1.parser.generated.syntaxtree.EnumeratedType;
import com.github.openasn1.parser.generated.syntaxtree.EnumeratedValue;
import com.github.openasn1.parser.generated.syntaxtree.Node;
import com.github.openasn1.parser.generated.syntaxtree.NodeChoice;
import com.github.openasn1.parser.generated.syntaxtree.NodeToken;
import com.github.openasn1.parser.generated.syntaxtree.Type;
import com.github.openasn1.parser.generated.syntaxtree.Value;
import com.github.openasn1.parser.generated.syntaxtree.ValueAssignment;

/**
 * This Class provides Fixup functionality for the identifier Mapping, so that
 * Enums no longer pose a problem. It takes a Value Object on a AST and checks
 * if it is a DefinedValue with a valuerefence subclass and replaces the
 * DefinedValue with a EnumerationValue if the Type it refers to is an
 * Enumeration. (That contains this value)
 *
 * Only Values in ValueAssignment and Default statements are fixed. This class
 * requires precense of an annotated AST for ALL Type Information.
 *
 * @author Clayton Hoss
 *
 */
public class EnumerationFixer {

    private ASN1ASTNodeInfos nodeInfos;

    private Value valueToFix;

    private EnumeratedType enumType;

    public EnumerationFixer(ASN1ASTNodeInfos infos, Value n) {
        this.nodeInfos = infos;
        this.valueToFix = n;
    }

    public boolean checkIfTypeNeedsFixing() {
        if (!checkIfTypeIsValidEnumType()) {
            return false;
        }
        // Checks if valueReference is a Symbol in the EnumType
        NodeToken valref = DefinedValueAttributes.getValueReferenceToken((ValueAttributes
                .getDefinedValue(getValueToFix())));
        if (!(EnumeratedTypeAttributes.getEnumerationStrings(getEnumType())
                .contains(valref.tokenImage))) {
            return false;
        }
        return true;
    }

    private boolean checkIfTypeIsValidEnumType() {
        if (getValueToFix() == null) {
            return false;
        }
        // Check if it as a valuereference
        if (!(ValueAttributes.isDefinedValue(getValueToFix()))) {
            return false;
        }
        if (DefinedValueAttributes.isExternalValueReference(ValueAttributes
                .getDefinedValue(getValueToFix()))) {
            return false;
        }

        // is it in ValueAssignment
        if (ValueAssignmentAttributes.isValueAssignment(getValueToFix().getParent())) {
            ValueAssignment va = ValueAssignmentAttributes
                    .getValueAssignment(getValueToFix().getParent());
            Type t = IdentifierMapHelper.getTypeFromDefinedTypeRecursive(va.type,
                    getNodeInfos());
            if (TypeAttributes.isEnumeratedType(t)) {
                setEnumType(TypeAttributes.getEnumeratedType(t));
                return true;
            }
            return false;
        }
        // is in Default Branch of Sequence?
        Node possibleComponentType = getValueToFix().getParent().getParent().getParent()
                .getParent().getParent().getParent();
        if (ComponentTypeAttributes.isComponentType(possibleComponentType)) {
            ComponentType ct = (ComponentType) possibleComponentType;
            if (!(ComponentTypeAttributes.isNamedType(ct))) {
                return false;
            }
            if (!(ComponentTypeAttributes.hasDefaultValue(ct))) {
                return false;
            }
            Type t = ComponentTypeAttributes
                    .getType((ComponentType) possibleComponentType);
            t = IdentifierMapHelper.getTypeFromDefinedTypeRecursive(t, getNodeInfos());
            if (TypeAttributes.isEnumeratedType(t)) {
                setEnumType(TypeAttributes.getEnumeratedType(t));
                return true;
            }
        }
        return false;
    }

    public boolean checkAndFixType() {
        if (!checkIfTypeNeedsFixing()) {
            return false;
        }
        fixType();
        return true;
    }

    /**
     *
     */
    public void fixType() {
        NodeToken valref = DefinedValueAttributes.getValueReferenceToken((ValueAttributes
                .getDefinedValue(getValueToFix())));
        // Change the AST
        BuiltinValue bv = new BuiltinValue(new NodeChoice(new EnumeratedValue(valref)));
        getValueToFix().nodeChoice.choice = bv;
        bv.setParent(getValueToFix());
    }

    /**
     * @return the nodeInfos
     */
    private ASN1ASTNodeInfos getNodeInfos() {
        return this.nodeInfos;
    }

    /**
     * @return the valueToFix
     */
    private Value getValueToFix() {
        return this.valueToFix;
    }

    /**
     * @return the enumType
     */
    private EnumeratedType getEnumType() {
        return this.enumType;
    }

    /**
     * @param enumType
     *            the enumType to set
     */
    private void setEnumType(EnumeratedType enumType) {
        this.enumType = enumType;
    }
}
