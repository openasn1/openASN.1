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

import java.util.Properties;

import org.apache.log4j.Logger;
import com.github.openasn1.compiler.astutils.ASN1ASTStorage;
import com.github.openasn1.compiler.interfaces.ASN1ASTModifier;
import com.github.openasn1.parser.generated.syntaxtree.Node;


/**
 * @author Clayton Hoss
 *
 */
public class IdentifierMapping implements ASN1ASTModifier {

    static Logger LOGGER = Logger.getLogger("IdentifierMapping");

    /**
     * @see com.github.openasn1.compiler.stages.generics.ASTModifier#init(java.util.Properties)
     */
    public boolean init(Properties prop) {
        return true;
    }

    /**
     * @see com.github.openasn1.compiler.stages.generics.ASTModifier#modifyAST(java.lang.Object)
     */
    public ASN1ASTStorage modifyAST(ASN1ASTStorage storage) {
        for (Node ast : storage.getAsn1AstList()) {
            SymbolTableCreator sc = new SymbolTableCreator();
            ast.accept(sc);
            SymbolTable symtab = sc.getSymtab();
            ExportTableCreator ec = new ExportTableCreator(symtab);
            ast.accept(ec);
            SymbolTable exportTab = ec.getExportTable();
            storage.insertInfoIntoNode(ast, "SymbolTable", symtab);
            storage.insertInfoIntoNode(ast, "ExportTable", exportTab);
        }

        for (Node ast : storage.getAsn1AstList()) {
            ImportTableCreator ic = new ImportTableCreator(storage);
            ast.accept(ic);
            SymbolTable importTable = ic.getImportTable();
            SymbolTable st = (SymbolTable) storage.returnInfoFromNode(ast, "SymbolTable");
            st.addDisjunctSymbolTables(importTable);
        }

        for (Node ast : storage.getAsn1AstList()) {
            SymbolTable st = (SymbolTable) storage.returnInfoFromNode(ast, "SymbolTable");
            IdentifierMapperVisitor im = new IdentifierMapperVisitor(st, storage.getAstNodeInfos(),IdentifierMapperVisitor.Mode.TypeMapping);
            ast.accept(im);
        }

        for (Node ast : storage.getAsn1AstList()) {
            SymbolTable st = (SymbolTable) storage.returnInfoFromNode(ast, "SymbolTable");
            IdentifierMapperVisitor im = new IdentifierMapperVisitor(st, storage.getAstNodeInfos(),IdentifierMapperVisitor.Mode.ValueMapping);
            ast.accept(im);
        }
        return storage;
    }

}
