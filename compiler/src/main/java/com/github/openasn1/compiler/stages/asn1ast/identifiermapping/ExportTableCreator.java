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
import com.github.openasn1.parser.attributes.ExportsAttributes;
import com.github.openasn1.parser.attributes.ReferenceAttributes;
import com.github.openasn1.parser.generated.syntaxtree.AssignmentList;
import com.github.openasn1.parser.generated.syntaxtree.Exports;
import com.github.openasn1.parser.generated.syntaxtree.Imports;
import com.github.openasn1.parser.generated.syntaxtree.ModuleBody;
import com.github.openasn1.parser.generated.syntaxtree.ModuleDefinition;
import com.github.openasn1.parser.generated.syntaxtree.Symbol;
import com.github.openasn1.parser.generated.visitor.DepthFirstVisitor;


/**
 * @author Clayton Hoss
 * 
 */
public class ExportTableCreator extends DepthFirstVisitor {

    private static Logger LOGGER = Logger.getLogger("ExportTableCreator");

    private SymbolTable exportTable = new SymbolTable();

    private SymbolTable symtab;

    public ExportTableCreator(SymbolTable symtab) {
        this.symtab = symtab;
    }

    /**
     * @see com.github.openasn1.parser.generated.visitor.DepthFirstVisitor#visit(com.github.openasn1.parser.generated.syntaxtree.ModuleBody)
     */
    @Override
    public void visit(ModuleBody n) {
        if (!n.nodeOptional.present()) {
            LOGGER.debug("Exporting whole Symtab - no export Statement");
            setExportTable(getSymtab());
            return;
        }
        super.visit(n);
    }

    /**
     * @see com.github.openasn1.parser.generated.visitor.DepthFirstVisitor#visit(com.github.openasn1.parser.generated.syntaxtree.ModuleDefinition)
     */
    @Override
    public void visit(ModuleDefinition n) {
        LOGGER.debug("Visiting Module to create ExportTable "
                + n.moduleIdentifier.modulereference.nodeToken.tokenImage);
        super.visit(n);
    }

    /**
     * @see com.github.openasn1.parser.generated.visitor.DepthFirstVisitor#visit(com.github.openasn1.parser.generated.syntaxtree.Symbol)
     */
    @Override
    public void visit(Symbol n) {
        String symbol = ReferenceAttributes.getSymbol(n.reference);
        if (!getSymtab().contains(symbol)) {
            LOGGER.error("Trying to export non declared Symbol " + symbol);
            throw new CompilationStoppingException("Undeclared Symbol Encountered: "
                    + symbol);
        }
        if (ReferenceAttributes.isTypeReference(n.reference)) {
            LOGGER.debug("Inserting Symbol into ExportTable as TypeReference " + symbol);
            getExportTable().insertIntoTypeSymbolTable(symbol,
                    getSymtab().getFromTypeSymbolTable(symbol));
        } else {
            LOGGER.debug("Inserting Symbol into ExportTable as ValueReference " + symbol);
            getExportTable().insertIntoValueSymbolTable(symbol,
                    getSymtab().getFromValueSymbolTable(symbol));
        }
        super.visit(n);
    }

    /**
     * @see com.github.openasn1.parser.generated.visitor.DepthFirstVisitor#visit(com.github.openasn1.parser.generated.syntaxtree.Exports)
     */
    @Override
    public void visit(Exports n) {
        if (ExportsAttributes.isExportsAllStatement(n)) {
            LOGGER.debug("Exporting whole Symtab - Exports ALL Statement");
            setExportTable(getSymtab());
            return;
        }
        super.visit(n);
    }

    /**
     * @return the exportTable
     */
    public SymbolTable getExportTable() {
        return this.exportTable;
    }

    /**
     * @return the symtab
     */
    private SymbolTable getSymtab() {
        return this.symtab;
    }

    /**
     * @param exportTable
     *            the exportTable to set
     */
    private void setExportTable(SymbolTable exportTable) {
        this.exportTable = exportTable;
    }

    /**
     * @see com.github.openasn1.parser.generated.visitor.DepthFirstVisitor#visit(com.github.openasn1.parser.generated.syntaxtree.AssignmentList)
     */
    @Override
    public void visit(AssignmentList n) {
        return;
    }

    /**
     * @see com.github.openasn1.parser.generated.visitor.DepthFirstVisitor#visit(com.github.openasn1.parser.generated.syntaxtree.Imports)
     */
    @Override
    public void visit(Imports n) {
        return;
    }

}
