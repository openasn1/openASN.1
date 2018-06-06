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
package com.github.openasn1.compiler.stages.asn1ast.componentsoftransformation;

import org.apache.log4j.Logger;
import com.github.openasn1.compiler.Exceptions.CompilationStoppingException;
import com.github.openasn1.compiler.Exceptions.FeatureNotSupportedException;
import com.github.openasn1.compiler.astutils.ASN1ASTNodeInfos;
import com.github.openasn1.compiler.astutils.helpers.IdentifierMapHelper;
import com.github.openasn1.compiler.astutils.helpers.LogginHelper;
import com.github.openasn1.parser.attributes.ComponentTypeAttributes;
import com.github.openasn1.parser.attributes.TypeAttributes;
import com.github.openasn1.parser.generated.syntaxtree.ComponentType;
import com.github.openasn1.parser.generated.syntaxtree.Type;
import com.github.openasn1.parser.generated.visitor.DepthFirstVisitor;


/**
 * This visitor is ment to do the Components-Of transformation in sequences.
 * There is some started arbitary code here, which SHOULD check if the
 * conditions for starting a Components-Of transformation are met. The actual
 * transformation is not implemented yet. It requires rearranging and copying
 * parts of the AST. Since only a few ASN.1 Specifications use the Components-Of
 * notation, it is left out for future implementations.<br>
 * Note1: The Components-Of transformation is ment to be done AFTER deciding
 * whether sequences should be automatically tagged, but BEFORE actually tagging
 * them automatically. <br>
 * Note2: The Components-Of transformation can add already tagged types to a
 * sequence which is flagged for automatic tagging.
 *
 * @author Clayton Hoss
 *
 */
public class ComponentsOfTransformingVisitor extends DepthFirstVisitor {

    private static Logger LOGGER = Logger.getLogger("ComponentsOfTransformingVisitor");

    private ASN1ASTNodeInfos nodeInfos;

    public ComponentsOfTransformingVisitor(ASN1ASTNodeInfos nodeInfos) {
        this.nodeInfos = nodeInfos;
    }

    /**
     * @see com.github.openasn1.parser.generated.visitor.DepthFirstVisitor#visit(com.github.openasn1.parser.generated.syntaxtree.SequenceType)
     */
    @Override
    public void visit(ComponentType n) {
        if (ComponentTypeAttributes.isComponentsOf(n)) {
            Type type = ComponentTypeAttributes.getType(n);
            if (!(TypeAttributes.isDefinedType(type))) {
                LOGGER.error("No Typereference in Components of Notation at line "
                        + LogginHelper.getCurrentLine(n));
                throw new CompilationStoppingException(
                        "Not Referencing another Type in Components of Notation");
            }
            Type sequenceType = IdentifierMapHelper.getTypeFromDefinedTypeRecursive(type,
                    getNodeInfos());

            if (!(TypeAttributes.isSequenceType(sequenceType))) {
                LOGGER.error("Using Components of Notation on non Sequence Type at line "
                        + LogginHelper.getCurrentLine(n));
                throw new CompilationStoppingException(
                        "Using Components of Notation on non Sequence Type");
            }
            //TODO the components of Transformation HERE
            throw new FeatureNotSupportedException(
                    "Components of Transformation is not supported");
        }
        super.visit(n);
    }

    /**
     * @return the nodeInfos
     */
    private ASN1ASTNodeInfos getNodeInfos() {
        return this.nodeInfos;
    }

}
