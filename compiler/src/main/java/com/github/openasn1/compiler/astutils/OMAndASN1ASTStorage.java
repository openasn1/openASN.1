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
import java.util.Set;

import com.github.openasn1.compiler.Exceptions.CompilationStoppingException;
import com.github.openasn1.compiler.Exceptions.DuplicateFileInPathException;
import com.github.openasn1.compiler.astutils.valueobjects.ObjectIdentifierValueObject;
import com.github.openasn1.compiler.interfaces.DeepCopyable;
import com.github.openasn1.compiler.omast.AbstractASN1Type;
import com.github.openasn1.parser.generated.syntaxtree.Node;


/**
 * @author Clayton Hoss
 *
 */
public class OMAndASN1ASTStorage implements DeepCopyable {

	private ASN1ASTStorage storage = null;

	private OMASTNodeInfos nodeinfos = new OMASTNodeInfos();

	private List<AbstractASN1Type> omAstList = new ArrayList<AbstractASN1Type>();

	private Map<String, Integer> moduleNameToListPosition = new HashMap<String, Integer>();

	// private static Logger LOGGER = Logger.getLogger("JavaAndASN1ASTStorage");

	private OMASTNodeInfos OMastNodeInfos = new OMASTNodeInfos();

	/**
	 * @param storage is asn1 storage
	 */
	public OMAndASN1ASTStorage(ASN1ASTStorage storage) {
		super();
		this.storage = storage;
	}

	/**
	 * @see com.github.openasn1.compiler.interfaces.DeepCopyable#deepCopy()
	 */
	public Object deepCopy() {
		// TODO Auto-generated method stub
		return null;
	}

	public void addOMAstToStorage(AbstractASN1Type astroot, String moduleName) {
		getOmAstList().add(astroot);
		Integer position = new Integer(getOmAstList().indexOf(astroot));
		if (getModuleNameToListPosition().containsKey(moduleName)) {
			throw new CompilationStoppingException(
					"Trying to add duplicate OM Ast");
		}
		getModuleNameToListPosition().put(moduleName, position);

	}

	public AbstractASN1Type getOMRootByModuleName(String name) {
		Integer i = getModuleNameToListPosition().get(name);
		return getOmAstList().get(i);
	}

	/**
	 * @return the oMastNodeInfos
	 */
	public OMASTNodeInfos getOMastNodeInfos() {
		return this.OMastNodeInfos;
	}

	/**
	 * @param filename is filename
	 * @param astroot is root node
	 * @throws DuplicateFileInPathException is exception
	 */
	public void addAstToStorage(String filename, Node astroot)
			throws DuplicateFileInPathException {
		this.storage.addAstToStorage(filename, astroot);
	}

	/**
	 * @param name is name
	 * @return boolean value
	 */
	public boolean containsASTWithFileName(String name) {
		return this.storage.containsASTWithFileName(name);
	}

	/**
	 * @param name is name
	 * @return boolean value
	 */
	public boolean containsASTWithModuleName(String name) {
		return this.storage.containsASTWithModuleName(name);
	}

	/**
	 * @param oid is oid
	 * @return boolean value
	 */
	public boolean containsASTWithOID(ObjectIdentifierValueObject oid) {
		return this.storage.containsASTWithOID(oid);
	}

	/**
	 * @param n is node
	 * @param key is key
	 * @return generic object
	 */
	public Object deleteInfoFromNode(Node n, String key) {
		return this.storage.deleteInfoFromNode(n, key);
	}

	/**
	 * @return list of nodes
	 */
	public List<Node> getAsn1AstList() {
		return this.storage.getAsn1AstList();
	}

	/**
	 * @return ASN1ASTNodeInfos
	 */
	public ASN1ASTNodeInfos getAstNodeInfos() {
		return this.storage.getAstNodeInfos();
	}

	/**
	 * @param name is name 
	 * @return node is node
	 */
	public Node getAstRootByFileName(String name) {
		return this.storage.getAstRootByFileName(name);
	}

	/**
	 * @param name is name
	 * @return node is node
	 */
	public Node getAstRootByModuleName(String name) {
		return this.storage.getAstRootByModuleName(name);
	}

	/**
	 * @param oid is oid
	 * @return node is node
	 */
	public Node getAstRootByObjectIdentifier(ObjectIdentifierValueObject oid) {
		return this.storage.getAstRootByObjectIdentifier(oid);
	}

	/**
	 * @param n is node
	 * @return map is map
	 */
	public Map<String, Object> getNodeInfoMap(Node n) {
		return this.storage.getNodeInfoMap(n);
	}

	/**
	 * @param n is node
	 * @param ident is ident
	 * @param o is object
	 */
	public void insertInfoIntoNode(Node n, String ident, Object o) {
		this.storage.insertInfoIntoNode(n, ident, o);
	}

	/**
	 * @param n is node
	 * @param key is key
	 * @return object is object
	 */
	public Object returnInfoFromNode(Node n, String key) {
		return this.storage.returnInfoFromNode(n, key);
	}

	/**
	 * @param n is node
	 * @param key is key
	 * @return object is object
	 */
	public Object deleteInfoFromNode(AbstractASN1Type n, String key) {
		return this.nodeinfos.deleteInfoFromNode(n, key);
	}

	/**
	 * @param n AbstractASN1Type
	 * @return map is map
	 */
	public Map<String, Object> getNodeInfoMap(AbstractASN1Type n) {
		return this.nodeinfos.getNodeInfoMap(n);
	}

	/**
	 * @param n is AbstractASN1Type
	 * @param ident is ident
	 * @param o is object
	 */
	public void insertInfoIntoNode(AbstractASN1Type n, String ident, Object o) {
		this.nodeinfos.insertInfoIntoNode(n, ident, o);
	}

	/**
	 * @param n AbstractASN1Type
	 * @param key is key
	 * @return is object
	 */
	public Object returnInfoFromNode(AbstractASN1Type n, String key) {
		return this.nodeinfos.returnInfoFromNode(n, key);
	}

	/**
	 * @return the moduleNameToListPosition
	 */
	private Map<String, Integer> getModuleNameToListPosition() {
		return this.moduleNameToListPosition;
	}

	/**
	 * @return the moduleNameToList
	 */
	public Set<String> getModuleNameList() {
		return this.moduleNameToListPosition.keySet();
	}

	/**
	 * @return the omAstList
	 */
	public List<AbstractASN1Type> getOmAstList() {
		return this.omAstList;
	}

}
