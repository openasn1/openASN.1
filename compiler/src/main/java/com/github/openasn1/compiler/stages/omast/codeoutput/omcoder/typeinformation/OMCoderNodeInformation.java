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
package com.github.openasn1.compiler.stages.omast.codeoutput.omcoder.typeinformation;

import java.util.ArrayList;
import java.util.List;

import com.github.openasn1.common.TagValue;
import com.github.openasn1.compiler.omast.AbstractASN1Type;
import com.github.openasn1.compiler.stages.omast.codeoutput.ModuleImportInformation;

import com.github.openasn1.codec.constraints.Constraint;

public class OMCoderNodeInformation {
	private AbstractASN1Type asn1Type;
	private OMCoderNodeInformation parent;
	private ArrayList<OMCoderNodeInformation> children = new ArrayList<OMCoderNodeInformation>();
	private ArrayList<ModuleImportInformation> imports = new ArrayList<ModuleImportInformation>();
	private ArrayList<TagValue> tagList = new ArrayList<TagValue>();
	private Constraint constraint = null;
	private String moduleName;
	private boolean isExtensible;
	private boolean isInExtension;
	private boolean isOptional;
	
	public Constraint getConstraint() {
		return constraint;
	}

	public boolean hasConstraint() {
		return getConstraint() != null;
	}

	public void setConstraint(Constraint constraint) {
		this.constraint = constraint;
	}

	public ArrayList<OMCoderNodeInformation> getChildren() {
		return children;
	}

	public void setChildren(ArrayList<OMCoderNodeInformation> children) {
		this.children = children;
	}

	public OMCoderNodeInformation getParent() {
		return parent;
	}

	public void setParent(OMCoderNodeInformation parent) {
		this.parent = parent;
	}

	public AbstractASN1Type getAsn1Type() {
		return asn1Type;
	}

	public void setAsn1Type(AbstractASN1Type asn1Type) {
		this.asn1Type = asn1Type;
	}
	
	public ArrayList<TagValue> getTagList() {
		return tagList;
	}

	public void setTagList(ArrayList<TagValue> tagList) {
		this.tagList = tagList;
	}

	/**
	 * @return the moduleName
	 */
	public String getModuleName() {
		return this.moduleName;
	}

	/**
	 * @param moduleName the moduleName to set
	 */
	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}

	public List<ModuleImportInformation> getImports() {
		return imports;
	}
	
	public boolean isInExtension() {
		return isInExtension;
	}

	public void setInExtension(boolean isInExtension) {
		this.isInExtension = isInExtension;
	}

	public boolean isExtensible() {
		return isExtensible;
	}

	public void setExtensible(boolean isExtensible) {
		this.isExtensible = isExtensible;
	}

	public boolean isOptional() {
		return isOptional;
	}

	public void setOptional(boolean isOptional) {
		this.isOptional = isOptional;
	}
	
	
}
