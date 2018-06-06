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
package com.github.openasn1.compiler.stages.omast.codeoutput.om;

import java.util.Properties;

import org.apache.log4j.Logger;
import com.github.openasn1.compiler.astutils.OMAndASN1ASTStorage;
import com.github.openasn1.compiler.codeoutpututils.ClassInformationHolder;
import com.github.openasn1.compiler.codeoutpututils.ClassOutWriterToOM;
import com.github.openasn1.compiler.interfaces.OMAndASN1ASTModifier;
import com.github.openasn1.compiler.omast.AbstractASN1Type;
import com.github.openasn1.compiler.utils.GlobalConfiguration;


/**
 * @author Clayton Hoss
 *
 */
public class OMCodeGenerator implements OMAndASN1ASTModifier {
	private static Logger LOGGER = Logger.getLogger("ASN1Compiler");

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
		try {
			freemarker.log.Logger.selectLoggerLibrary(freemarker.log.Logger.LIBRARY_NONE);
		} catch (ClassNotFoundException e1) { }

		for (String name : storage.getModuleNameList()) {
			AbstractASN1Type type = storage.getOMRootByModuleName(name);
			OMCodeGeneratingVisitor visitor = new OMCodeGeneratingVisitor();
			type.accept(visitor);

			LOGGER.info("Generating class files for module " + name);
			for (ClassInformationHolder ci : visitor
					.getGeneratedClassInformationList()) {
				ClassOutWriterToOM writer = new ClassOutWriterToOM(
						GlobalConfiguration.getInstance().getProperty(
								"compiler.codegeneration.output"));
				writer.writeClass(ci);
			}
		}

		// TODO Auto-generated method stub
		return storage;
	}

}
