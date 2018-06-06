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

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import com.github.openasn1.compiler.Exceptions.CompilationStoppingException;
import com.github.openasn1.parser.generated.syntaxtree.TypeAssignment;
import com.github.openasn1.parser.generated.syntaxtree.ValueAssignment;


/**
 * @author Clayton Hoss
 * 
 */
public class SymbolTable {

    private static Logger LOGGER = Logger.getLogger("SymbolTable");

    private Map<String, TypeAssignment> typeSymbolTable = new HashMap<String, TypeAssignment>();

    private Map<String, ValueAssignment> valueSymbolTable = new HashMap<String, ValueAssignment>();

    public void insertIntoTypeSymbolTable(String symbol, TypeAssignment type) {
        getTypeSymbolTable().put(symbol, type);
    }

    public void insertIntoValueSymbolTable(String symbol, ValueAssignment value) {
        getValueSymbolTable().put(symbol, value);
    }

    public TypeAssignment getFromTypeSymbolTable(String symbol) {
        return getTypeSymbolTable().get(symbol);
    }

    public ValueAssignment getFromValueSymbolTable(String symbol) {
        return getValueSymbolTable().get(symbol);
    }

    public boolean contains(String key) {
        return (getTypeSymbolTable().containsKey(key) || getValueSymbolTable()
                .containsKey(key));
    }

    /**
     * This Method adds the Symbols from another SymbolTable into this Object.
     * The Symbols need to be disjunct. Otherwise the method will throw an
     * Exception, that an Symbol is beeing redefined.
     * 
     * @param otherSymtab
     *            the other SymbolTable Object whose Symbols are added to this
     *            table
     */
    public void addDisjunctSymbolTables(SymbolTable otherSymtab) {
        for (String symbol : otherSymtab.getTypeSymbolTable().keySet()) {
            if (getTypeSymbolTable().containsKey(symbol)) {
                LOGGER.error("Trying to Redefine Symbol: " + symbol);
                throw new CompilationStoppingException("Redefined Symbol: " + symbol);
            }
            insertIntoTypeSymbolTable(symbol, otherSymtab.getFromTypeSymbolTable(symbol));
        }

        for (String symbol : otherSymtab.getValueSymbolTable().keySet()) {
            if (getValueSymbolTable().containsKey(symbol)) {
                LOGGER.error("Trying to Redefine Symbol: " + symbol);
                throw new CompilationStoppingException("Redefined Symbol: " + symbol);
            }
            insertIntoValueSymbolTable(symbol, otherSymtab
                    .getFromValueSymbolTable(symbol));
        }
    }

    /**
     * @return the typeSymbolTable
     */
    private Map<String, TypeAssignment> getTypeSymbolTable() {
        return this.typeSymbolTable;
    }

    /**
     * @return the valueSymbolTable
     */
    private Map<String, ValueAssignment> getValueSymbolTable() {
        return this.valueSymbolTable;
    }

}
