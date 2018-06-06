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
import com.github.openasn1.compiler.astutils.ASN1ASTStorage;
import com.github.openasn1.parser.attributes.ReferenceAttributes;
import com.github.openasn1.parser.generated.syntaxtree.AssignmentList;
import com.github.openasn1.parser.generated.syntaxtree.Exports;
import com.github.openasn1.parser.generated.syntaxtree.ModuleDefinition;
import com.github.openasn1.parser.generated.syntaxtree.Node;
import com.github.openasn1.parser.generated.syntaxtree.Symbol;
import com.github.openasn1.parser.generated.syntaxtree.SymbolsFromModule;
import com.github.openasn1.parser.generated.visitor.DepthFirstVisitor;


/**
 * @author Clayton Hoss
 * 
 */
public class ImportTableCreator extends DepthFirstVisitor {

    private static Logger LOGGER = Logger.getLogger("ImportTableCreator");

    private ASN1ASTStorage storage;

    private SymbolTable importTable = new SymbolTable();

    private SymbolTable currentExportTable;

    public ImportTableCreator(ASN1ASTStorage storage) {
        this.storage = storage;
    }

    /**
     * @see com.github.openasn1.parser.generated.visitor.DepthFirstVisitor#visit(com.github.openasn1.parser.generated.syntaxtree.ModuleDefinition)
     */
    @Override
    public void visit(ModuleDefinition n) {
        LOGGER.debug("Visiting Module to create ImportTable "
                + n.moduleIdentifier.modulereference.nodeToken.tokenImage);
        super.visit(n);
    }

    /**
     * @see com.github.openasn1.parser.generated.visitor.DepthFirstVisitor#visit(com.github.openasn1.parser.generated.syntaxtree.SymbolsFromModule)
     */
    @Override
    public void visit(SymbolsFromModule n) {
        // n.globalModuleReference;
        // get ExportTable from Module
        // without assignedIdentifier (featureNotSupppoertedException)
        // TODO make it work with OID Values, too
        String moduleName = n.globalModuleReference.modulereference.nodeToken.tokenImage;

        LOGGER.debug("Loading ExportTable from Module " + moduleName);
        Node ast = getASTRootFromModuleName(moduleName);
        setCurrentExportTable((SymbolTable) getStorage().returnInfoFromNode(ast,
                "ExportTable"));
        super.visit(n);
        setCurrentExportTable(null);
    }

    private Node getASTRootFromModuleName(String moduleName) {
        if (!getStorage().containsASTWithModuleName(moduleName)) {
            LOGGER.error("Module not found in Compilation Environment with name "
                    + moduleName);
            throw new CompilationStoppingException("Module not found " + moduleName);
        }
        Node ast = getStorage().getAstRootByModuleName(moduleName);
        return ast;
    }

    /**
     * @see com.github.openasn1.parser.generated.visitor.DepthFirstVisitor#visit(com.github.openasn1.parser.generated.syntaxtree.Symbol)
     */
    @Override
    public void visit(Symbol n) {
        String ref = ReferenceAttributes.getSymbol(n.reference);
        if (!getCurrentExportTable().contains(ref)) {
            LOGGER.error("Trying to Import nonexisting Symbol " + ref);
            throw new CompilationStoppingException("Trying to Import nonexisting Symbol "
                    + ref);
        }

        if (ReferenceAttributes.isTypeReference(n.reference)) {
            LOGGER.debug("Inserting Symbol into ImportTable as TypeReference " + ref);
            getImportTable().insertIntoTypeSymbolTable(ref,
                    getCurrentExportTable().getFromTypeSymbolTable(ref));
        } else {
            LOGGER.debug("Inserting Symbol into ImportTable as ValueReference " + ref);
            getImportTable().insertIntoValueSymbolTable(ref,
                    getCurrentExportTable().getFromValueSymbolTable(ref));
        }
        super.visit(n);
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
    public void visit(Exports n) {
        return;
    }

    /**
     * @return the importTable
     */
    public SymbolTable getImportTable() {
        return this.importTable;
    }

    /**
     * @return the currentExportTable
     */
    private SymbolTable getCurrentExportTable() {
        return this.currentExportTable;
    }

    /**
     * @param currentExportTable
     *            the currentExportTable to set
     */
    private void setCurrentExportTable(SymbolTable currentExportTable) {
        this.currentExportTable = currentExportTable;
    }

    /**
     * @return the storage
     */
    private ASN1ASTStorage getStorage() {
        return this.storage;
    }
}
