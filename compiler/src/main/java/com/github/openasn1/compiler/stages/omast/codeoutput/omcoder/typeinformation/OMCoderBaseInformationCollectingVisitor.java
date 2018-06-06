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

import com.github.openasn1.compiler.omast.ASN1ImportListFromModule;
import com.github.openasn1.compiler.omast.ASN1Module;
import com.github.openasn1.compiler.omast.AbstractOMVisitor;
import com.github.openasn1.compiler.stages.omast.codeoutput.ModuleImportInformation;

/**
 * @author Clayton Hoss
 *
 */
public class OMCoderBaseInformationCollectingVisitor extends AbstractOMVisitor {
	private OMCoderNodeInformation nodeInformation;
	
	public OMCoderBaseInformationCollectingVisitor(OMCoderNodeInformation nodeInformation) {
		this.nodeInformation = nodeInformation;
	}
	
	public OMCoderNodeInformation getNodeInformation() {
		return nodeInformation;
	}

	@Override
	public void visit(ASN1ImportListFromModule type) {
		for (String importName : type.getImportNames()) {
			getNodeInformation().getImports().add(new ModuleImportInformation(type.getModuleName(), importName));
		}
	}

	@Override
	public void visit(ASN1Module type) {
		getNodeInformation().setModuleName(type.getName());
		super.visit(type);
	}
}