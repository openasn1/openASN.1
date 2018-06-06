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
package com.github.openasn1.compiler.astutils.helpers;

import org.apache.log4j.Logger;
import com.github.openasn1.compiler.Exceptions.FeatureNotSupportedException;
import com.github.openasn1.compiler.astutils.ASN1ASTNodeInfos;
import com.github.openasn1.parser.attributes.DefinedTypeAttributes;
import com.github.openasn1.parser.attributes.DefinedValueAttributes;
import com.github.openasn1.parser.attributes.TypeAttributes;
import com.github.openasn1.parser.attributes.ValueAttributes;
import com.github.openasn1.parser.generated.syntaxtree.DefinedType;
import com.github.openasn1.parser.generated.syntaxtree.DefinedValue;
import com.github.openasn1.parser.generated.syntaxtree.Type;
import com.github.openasn1.parser.generated.syntaxtree.TypeAssignment;
import com.github.openasn1.parser.generated.syntaxtree.Value;
import com.github.openasn1.parser.generated.syntaxtree.ValueAssignment;


/**
 * @author Clayton Hoss
 * 
 */
public class IdentifierMapHelper {

    private static Logger LOGGER = Logger.getLogger("IdentifierMapHelper");

    /**
     * This Method takes a Type and resolves the Identifiers recursively until
     * it reaches a Type which is not a DefinedType.
     * 
     * Note: It does not recursively resolve through Tagged DefinedTypes
     * 
     * @param type
     * @return
     */
    public static Type getTypeFromDefinedTypeRecursive(Type type, ASN1ASTNodeInfos nodeInfos) {
        while (TypeAttributes.isDefinedType(type)) {
            DefinedType dt = TypeAttributes.getDefinedType(type);
            if (DefinedTypeAttributes.isExternalTypeReference(dt)) {
                LOGGER.error("External Type Reference is not Supported");
                throw new FeatureNotSupportedException(
                        "External Type Reference is not Supported");
            }
            TypeAssignment ts = (TypeAssignment) nodeInfos.returnInfoFromNode(dt,
                    "IdentifierMap");

            type = ts.type;
        }
        return type;
    }

    public static Value getValueFromDefinedValueRecursive(Value value,
            ASN1ASTNodeInfos nodeInfos) {
        while (ValueAttributes.isDefinedValue(value)) {
            DefinedValue dv = ValueAttributes.getDefinedValue(value);
            if (DefinedValueAttributes.isExternalValueReference(dv)) {
                LOGGER.error("External Value Reference is not Supported");
                throw new FeatureNotSupportedException(
                        "External Value Reference is not Supported");
            }
            ValueAssignment vs = (ValueAssignment) nodeInfos.returnInfoFromNode(dv,
                    "IdentifierMap");

            value = vs.value;
        }
        return value;
    }

    public static Value getValueFromDefinedValueRecursive(DefinedValue dv,
            ASN1ASTNodeInfos nodeInfos) {
        if (DefinedValueAttributes.isExternalValueReference(dv)) {
            LOGGER.error("External Value Reference is not Supported");
            throw new FeatureNotSupportedException(
                    "External Value Reference is not Supported");
        }
        ValueAssignment vs = (ValueAssignment) nodeInfos.returnInfoFromNode(dv,
                "IdentifierMap");

        Value value = vs.value;

        return getValueFromDefinedValueRecursive(value, nodeInfos);
    }

}
