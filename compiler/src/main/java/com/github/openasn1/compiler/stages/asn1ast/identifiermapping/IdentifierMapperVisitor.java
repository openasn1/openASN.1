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
package com.github.openasn1.compiler.stages.asn1ast.identifiermapping;

import org.apache.log4j.Logger;
import com.github.openasn1.compiler.Exceptions.CompilationStoppingException;
import com.github.openasn1.compiler.Exceptions.FeatureNotSupportedException;
import com.github.openasn1.compiler.astutils.ASN1ASTNodeInfos;
import com.github.openasn1.compiler.astutils.helpers.LogginHelper;
import com.github.openasn1.compiler.stages.asn1ast.RestrictionChecking.EnumerationFixer;
import com.github.openasn1.parser.attributes.DefinedValueAttributes;
import com.github.openasn1.parser.attributes.NodeTokenAttribues;
import com.github.openasn1.parser.attributes.ValueAttributes;
import com.github.openasn1.parser.generated.syntaxtree.DefinedType;
import com.github.openasn1.parser.generated.syntaxtree.DefinedValue;
import com.github.openasn1.parser.generated.syntaxtree.ExternalTypeReference;
import com.github.openasn1.parser.generated.syntaxtree.ExternalValueReference;
import com.github.openasn1.parser.generated.syntaxtree.ModuleDefinition;
import com.github.openasn1.parser.generated.syntaxtree.NodeToken;
import com.github.openasn1.parser.generated.syntaxtree.TypeAssignment;
import com.github.openasn1.parser.generated.syntaxtree.Value;
import com.github.openasn1.parser.generated.syntaxtree.ValueAssignment;
import com.github.openasn1.parser.generated.syntaxtree.valuereference;
import com.github.openasn1.parser.generated.visitor.DepthFirstVisitor;


/**
 * Maps the used Identifiers (Type and Value References to the assignments they
 * were defined in. The Production "Reference" is only used in the Import and
 * Export Statements and therefore don't need to be mapped
 *
 * @author Clayton Hoss
 *
 */
public class IdentifierMapperVisitor extends DepthFirstVisitor {

    public static enum Mode {
        TypeMapping(false, true), ValueMapping(true, false),
        TypeAndValueMapping(true, true);

        private final boolean doValueMapping;

        private final boolean doTypeMapping;

        private Mode(final boolean doValueMapping, final boolean doTypeMapping) {
            this.doValueMapping = doValueMapping;
            this.doTypeMapping = doTypeMapping;
        }

        /**
         * @return the doTypeMapping
         */
        public boolean doTypeMapping() {
            return this.doTypeMapping;
        }

        /**
         * @return the doValueMapping
         */
        public boolean doValueMapping() {
            return this.doValueMapping;
        }

    }

    private SymbolTable symtab;

    private ASN1ASTNodeInfos nodeinfos;

    private Mode mode;

    private static Logger LOGGER = Logger.getLogger("IdentifierMapper");

    private class IdentifierMapperHelper extends DepthFirstVisitor {

        /**
         * @see com.github.openasn1.parser.generated.visitor.DepthFirstVisitor#visit(com.github.openasn1.parser.generated.syntaxtree.ExternalTypeReference)
         */
        @Override
        public void visit(ExternalTypeReference n) {
            throw new FeatureNotSupportedException(
                    "External Type Reference not Supported");
        }

        /**
         * @see com.github.openasn1.parser.generated.visitor.DepthFirstVisitor#visit(com.github.openasn1.parser.generated.syntaxtree.ExternalValueReference)
         */
        @Override
        public void visit(ExternalValueReference n) {
            throw new FeatureNotSupportedException(
                    "External Value Reference not Supported");
        }

        /**
         * @see com.github.openasn1.parser.generated.visitor.DepthFirstVisitor#visit(com.github.openasn1.parser.generated.syntaxtree.valuereference)
         */
        @SuppressWarnings("synthetic-access")
        @Override
        public void visit(valuereference n) {
            String symbol = n.nodeToken.tokenImage;
            LOGGER.debug("Mapping Valuereference Symbol " + symbol);
            if (!getSymtab().contains(symbol)) {
                LOGGER.error("Trying to map an undefined (value) symbol " + symbol);
                throw new CompilationStoppingException("Undefined Symbol Encountered "
                        + symbol + " at line " + LogginHelper.getCurrentLine(n));
            }
            ValueAssignment va = getSymtab().getFromValueSymbolTable(symbol);
            getNodeinfos().insertInfoIntoNode(n.getParent().getParent(), "IdentifierMap",
                    va);
        }

        /**
         * @see com.github.openasn1.parser.generated.visitor.DepthFirstVisitor#visit(com.github.openasn1.parser.generated.syntaxtree.NodeToken)
         */
        @SuppressWarnings("synthetic-access")
        @Override
        public void visit(NodeToken n) {
            if (!NodeTokenAttribues.isTypeReference(n)) {
                return;
            }
            String symbol = n.tokenImage;
            LOGGER.debug("Mapping Typereference Symbol " + symbol);
            if (!getSymtab().contains(symbol)) {
                LOGGER.error("Trying to map an undefined (type) symbol " + symbol);
                throw new CompilationStoppingException("Undefined Symbol Encountered "
                        + symbol);
            }
            TypeAssignment ta = getSymtab().getFromTypeSymbolTable(symbol);
            getNodeinfos().insertInfoIntoNode(n.getParent().getParent(), "IdentifierMap",
                    ta);
        }

    }

    public IdentifierMapperVisitor(SymbolTable st, ASN1ASTNodeInfos infos, Mode mode) {
        this.symtab = st;
        this.nodeinfos = infos;
        this.mode = mode;
    }

    @Override
    public void visit(ModuleDefinition n) {
        LOGGER.debug("Visiting Module to create IdentfierMap "
                + n.moduleIdentifier.modulereference.nodeToken.tokenImage);
        super.visit(n);
    }

    @SuppressWarnings("synthetic-access")
    @Override
    public void visit(DefinedType n) {
        if (!getMode().doTypeMapping()) {
            return;
        }
        // acutal Mapping is done in the Visitor!!
        // visitor is not side effect free!
        n.accept(new IdentifierMapperHelper());
        super.visit(n);
    }


    @SuppressWarnings("synthetic-access")
    @Override
    public void visit(DefinedValue n) {
        if (!getMode().doValueMapping()) {
            return;
        }
        // acutal Mapping is done in the Visitor!!
        // visitor is not side effect free!
        n.accept(new IdentifierMapperHelper());
        super.visit(n);
    }

    @Override
    public void visit(Value n) {
        if (!getMode().doValueMapping()) {
            return;
        }
        // the Enum fixup HERE
        EnumerationFixer ef = new EnumerationFixer(getNodeinfos(), n);
        if (ef.checkIfTypeNeedsFixing()) {
            LOGGER.debug("Using enumfixup for Value "
                    + DefinedValueAttributes.getValueReferenceToken((ValueAttributes
                            .getDefinedValue(n))) + " at line "
                    + LogginHelper.getCurrentLine(n));
            ef.fixType();
            return;
        }

         super.visit(n);
    }

    /**
     * @return the nodeinfos
     */
    private ASN1ASTNodeInfos getNodeinfos() {
        return this.nodeinfos;
    }

    /**
     * @return the symtab
     */
    private SymbolTable getSymtab() {
        return this.symtab;
    }

    /**
     * @return the mode
     */
    private Mode getMode() {
        return this.mode;
    }

}
