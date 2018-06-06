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

import java.util.Properties;

import org.apache.log4j.Logger;
import com.github.openasn1.compiler.astutils.OMAndASN1ASTStorage;
import com.github.openasn1.compiler.interfaces.OMAndASN1ASTModifier;
import com.github.openasn1.compiler.omast.AbstractASN1Type;


/**
 * @author Clayton Hoss
 *
 */
public class OMManagerCodeGenerator implements OMAndASN1ASTModifier {
	private static Logger LOGGER = Logger.getLogger(OMManagerCodeGenerator.class.getSimpleName());
	
	/**
	 * @see com.github.openasn1.compiler.stages.generics.ASTModifier#init(java.util.Properties)
	 */
	public boolean init(Properties prop) {
		return true;
	}

	/**
	 * @see com.github.openasn1.compiler.stages.generics.ASTModifier#modifyAST(java.lang.Object)
	 */
	public OMAndASN1ASTStorage modifyAST(OMAndASN1ASTStorage storage) {

		for (String name : storage.getModuleNameList()) {
			LOGGER.debug("Generating CodingManager class for module '" + name + "'");

			AbstractASN1Type type = storage.getOMRootByModuleName(name);

			OMManagerNodeInformationCollectingVisitor visitor = new OMManagerNodeInformationCollectingVisitor();
			type.accept(visitor);
			
			OMManagerRenderer renderer = new OMManagerRenderer();
			renderer.renderCodingManager(visitor.getNodeInformation());
		}

		return storage;
	}

}
