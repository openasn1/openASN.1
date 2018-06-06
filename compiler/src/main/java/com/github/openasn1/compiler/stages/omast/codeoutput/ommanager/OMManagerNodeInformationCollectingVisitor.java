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
package com.github.openasn1.compiler.stages.omast.codeoutput.ommanager;

import com.github.openasn1.compiler.omast.ASN1ImportListFromModule;
import com.github.openasn1.compiler.omast.ASN1Module;
import com.github.openasn1.compiler.omast.ASN1TypeAssignment;
import com.github.openasn1.compiler.omast.AbstractOMVisitor;
import com.github.openasn1.compiler.omast.AggregatedASN1Type;
import com.github.openasn1.compiler.omast.ComplexASN1Type;
import com.github.openasn1.compiler.omast.ReferencedASN1Type;
import com.github.openasn1.compiler.omast.SimpleASN1Type;
import com.github.openasn1.compiler.stages.omast.codeoutput.ModuleImportInformation;


public class OMManagerNodeInformationCollectingVisitor extends AbstractOMVisitor {
	private OMManagerNodeInformation nodeInformation = new OMManagerNodeInformation();

	public OMManagerNodeInformation getNodeInformation() {
		return nodeInformation;
	}

	@Override
	public void visit(ASN1ImportListFromModule type) {
		for (String importName : type.getImportNames()) {
			getNodeInformation().getImports().add(new ModuleImportInformation(type.getModuleName(), importName));
		}
		super.visit(type);
	}

	@Override
	public void visit(ASN1Module type) {
		getNodeInformation().setModuleName(type.getName());
		super.visit(type);
	}

	/**
	 * @see com.github.openasn1.compiler.omast.AbstractOMVisitor#visit(com.github.openasn1.compiler.omast.ASN1TypeAssignment)
	 */
	@Override
	public void visit(ASN1TypeAssignment type) {
		super.visit(type);
	}

	/**
	 * @see com.github.openasn1.compiler.omast.AbstractOMVisitor#visit(com.github.openasn1.compiler.omast.ComplexASN1Type)
	 */
	@Override
	public void visit(ComplexASN1Type type) {
		getNodeInformation().getTopLevelTypes().add(type);
	}

	@Override
	public void visit(ReferencedASN1Type type) {
		getNodeInformation().getTopLevelTypes().add(type);
	}

	@Override
	public void visit(AggregatedASN1Type type) {
		getNodeInformation().getTopLevelTypes().add(type);
	}

	@Override
	public void visit(SimpleASN1Type type) {
		getNodeInformation().getTopLevelTypes().add(type);
	}
}