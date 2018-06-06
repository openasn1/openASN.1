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
package com.github.openasn1.compiler.codeoutpututils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Clayton Hoss
 *
 */
public class ClassInformationHolder {
	private String name;

	private String packagename;

	private String extendsClass = "Object";

	private List<ClassInformationHolder> subClasses = new ArrayList<ClassInformationHolder>();

	private Set<String> imports = new HashSet<String>();

	private Map<String, String> members = new LinkedHashMap<String, String>();

	private Map<String, String> getterCode = new LinkedHashMap<String, String>();

	private Map<String, String> setterCode = new LinkedHashMap<String, String>();

	private List<String> customMethods = new ArrayList<String>();

	private boolean isEnum = false;

	public ClassInformationHolder(String classname, String packagename) {
		this.name = classname;
		this.packagename = packagename;
	}


	public void addMemberVariable(String name, String type) {
		addMemberVariable(name, type, null);
	}

	public void addMemberVariable(String name, String type, String importForType) {
		getMembers().put(name, type);

		// One could map Imports for the default types here. e.g. Date
		if (importForType != null && !importForType.trim().equals("")) {
			addImport(importForType);
		}
	}

	public void addSubClass(ClassInformationHolder theClass) {
		getSubClasses().add(theClass);
		getImports().addAll(theClass.getImports());
	}

	/**
	 * This Code is for adding custom code to the setter, which is used before
	 * the assignment. The local Variable name must be the same as the name of
	 * the member variable.
	 *
	 * @param member
	 * @param codeSnippet
	 */
	public void addCustomSetterCode(String member, String codeSnippet) {
		if (!getSetterCode().containsKey(member)) {
			getSetterCode().put(member, "");
		}
		String code = getSetterCode().get(member);
		code += codeSnippet;
		getSetterCode().put(member, code);
	}

	/**
	 * This Code is for adding custom code to the setter, which is used before
	 * the return statement. The member Variable should be accessed via this.
	 *
	 * @param member
	 * @param codeSnippet
	 */

	public void addCustomGetterCode(String member, String codeSnippet) {
		if (!getGetterCode().containsKey(member)) {
			getGetterCode().put(member, "");
		}
		String code = getGetterCode().get(member);
		code += codeSnippet;
		getGetterCode().put(member, code);
	}

	public void addCustomMethod(String method) {
		getCustomMethods().add(method);
	}

	public void addImport(String myImport) {
		getImports().add(myImport);
	}

	/**
	 * @return the getterCode
	 */
	Map<String, String> getGetterCode() {
		return this.getterCode;
	}

	/**
	 * @return the members
	 */
	Map<String, String> getMembers() {
		return this.members;
	}

	/**
	 * @return the setterCode
	 */
	Map<String, String> getSetterCode() {
		return this.setterCode;
	}

	/**
	 * @return the imports
	 */
	Set<String> getImports() {
		return this.imports;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * @return the packagename
	 */
	public String getPackagename() {
		return this.packagename;
	}

	/**
	 * @return the subClasses
	 */
	List<ClassInformationHolder> getSubClasses() {
		return this.subClasses;
	}


	/**
	 * @return the extendsClass
	 */
	public String getExtendsClass() {
		return this.extendsClass;
	}


	/**
	 * @param extendsClass the extendsClass to set
	 */
	public void setExtendsClass(String extendsClass) {
		this.extendsClass = extendsClass;
	}


	/**
	 * @return the customMethods
	 */
	List<String> getCustomMethods() {
		return this.customMethods;
	}


	/**
	 * @return the isEnum
	 */
	public boolean isEnum() {
		return this.isEnum;
	}


	/**
	 * @param isEnum the isEnum to set
	 */
	public void setEnum(boolean isEnum) {
		this.isEnum = isEnum;
	}

}
