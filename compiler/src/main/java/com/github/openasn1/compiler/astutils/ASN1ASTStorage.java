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
package com.github.openasn1.compiler.astutils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import com.github.openasn1.compiler.Exceptions.DuplicateFileInPathException;
import com.github.openasn1.compiler.astutils.valueobjects.ObjectIdentifierValueObject;
import com.github.openasn1.compiler.astutils.visitors.ModuleHeaderInformationExtractionVisitor;
import com.github.openasn1.compiler.interfaces.DeepCopyable;
import com.github.openasn1.parser.generated.syntaxtree.Node;


/**
 * @author Clayton Hoss
 *
 */
public class ASN1ASTStorage implements DeepCopyable {

    private static Logger LOGGER = Logger.getLogger("ASN1ASTStorage");

    private Map<ObjectIdentifierValueObject, Integer> oidToListPosition = new HashMap<ObjectIdentifierValueObject, Integer>();

    private List<Node> asn1AstList = new ArrayList<Node>();

    private Map<String, Integer> fileNameToListPosition = new HashMap<String, Integer>();

    private Map<String, Integer> moduleNameToListPosition = new HashMap<String, Integer>();

    private ASN1ASTNodeInfos astNodeInfos = new ASN1ASTNodeInfos();

    public void addAstToStorage(String filename, Node astroot)
            throws DuplicateFileInPathException {
        getAsn1AstList().add(astroot);
        Integer position = new Integer(getAsn1AstList().indexOf(astroot));

        ModuleHeaderInformationExtractionVisitor vis = new ModuleHeaderInformationExtractionVisitor();
        astroot.accept(vis);
        /* Logger Message */
        LOGGER.debug("Trying to add Node to ASTStorage: " + filename + " "
                + vis.getModuleName() + " " + vis.getModuleOIDValue() + " at Position "
                + position);
        /* End Logging */
        if (getOidToListPosition().containsKey(vis.getModuleOIDValue())
                && getModuleNameToListPosition().containsKey(vis.getModuleName())) {
            DuplicateFileInPathException de = new DuplicateFileInPathException(
                    "Duplicate File found in path");
            de.setFile1(filename);
            throw de;
        }

        getOidToListPosition().put(vis.getModuleOIDValue(), position);
        getFileNameToListPosition().put(filename, position);
        getModuleNameToListPosition().put(vis.getModuleName(), position);
    }

    public Node getAstRootByObjectIdentifier(ObjectIdentifierValueObject oid) {
        Integer i = getOidToListPosition().get(oid);
        return getAsn1AstList().get(i);
    }

    public Node getAstRootByFileName(String name) {
        Integer i = getFileNameToListPosition().get(name);
        return getAsn1AstList().get(i);
    }

    public Node getAstRootByModuleName(String name) {
        Integer i = getModuleNameToListPosition().get(name);
        return getAsn1AstList().get(i);
    }

    public boolean containsASTWithModuleName(String name) {
        return getModuleNameToListPosition().containsKey(name);
    }

    public boolean containsASTWithOID(ObjectIdentifierValueObject oid) {
        return getOidToListPosition().containsKey(oid);
    }

    public boolean containsASTWithFileName(String name) {
        return getFileNameToListPosition().containsKey(name);
    }

    /**
     * @return the fileNameToListPosition
     */
    private Map<String, Integer> getFileNameToListPosition() {
        return this.fileNameToListPosition;
    }

    /**
     * @return the moduleNameToListPosition
     */
    private Map<String, Integer> getModuleNameToListPosition() {
        return this.moduleNameToListPosition;
    }

    /**
     * @return the asn1AstList
     */
    public List<Node> getAsn1AstList() {
        return this.asn1AstList;
    }

    /**
     * @return the oidToListPosition
     */
    private Map<ObjectIdentifierValueObject, Integer> getOidToListPosition() {
        return this.oidToListPosition;
    }

    /**
     * @see com.github.openasn1.compiler.interfaces.DeepCopyable#deepCopy()
     */
    public Object deepCopy() {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * @return the astNodeInfos
     */
    public ASN1ASTNodeInfos getAstNodeInfos() {
        return this.astNodeInfos;
    }

    /**
     * @param n
     * @param key
     * @return
     * @see com.github.openasn1.compiler.astutils.ASN1ASTNodeInfos#deleteInfoFromNode(com.github.openasn1.parser.generated.syntaxtree.Node,
     *      java.lang.String)
     */
    public Object deleteInfoFromNode(Node n, String key) {
        return this.astNodeInfos.deleteInfoFromNode(n, key);
    }

    /**
     * @param n
     * @return
     * @see com.github.openasn1.compiler.astutils.ASN1ASTNodeInfos#getNodeInfoMap(com.github.openasn1.parser.generated.syntaxtree.Node)
     */
    public Map<String, Object> getNodeInfoMap(Node n) {
        return this.astNodeInfos.getNodeInfoMap(n);
    }

    /**
     * @param n
     * @param ident
     * @param o
     * @see com.github.openasn1.compiler.astutils.ASN1ASTNodeInfos#insertInfoIntoNode(com.github.openasn1.parser.generated.syntaxtree.Node,
     *      java.lang.String, java.lang.Object)
     */
    public void insertInfoIntoNode(Node n, String ident, Object o) {
        this.astNodeInfos.insertInfoIntoNode(n, ident, o);
    }

    /**
     * @param n
     * @param key
     * @return
     * @see com.github.openasn1.compiler.astutils.ASN1ASTNodeInfos#returnInfoFromNode(com.github.openasn1.parser.generated.syntaxtree.Node,
     *      java.lang.String)
     */
    public Object returnInfoFromNode(Node n, String key) {
        return this.astNodeInfos.returnInfoFromNode(n, key);
    }

}
