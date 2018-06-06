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

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import com.github.openasn1.compiler.codeoutpututils.templaterenderer.Attribute;
import com.github.openasn1.compiler.codeoutpututils.templaterenderer.FreemarkerRenderer;
import com.github.openasn1.compiler.omast.AbstractASN1Type;
import com.github.openasn1.compiler.stages.omast.codeoutput.ModuleImportInformation;
import com.github.openasn1.compiler.utils.GlobalConfiguration;


public class OMManagerRenderer extends FreemarkerRenderer {
	public OMManagerRenderer() {
		super(null, GlobalConfiguration.getInstance().getProperty("compiler.templates.path")+ "/ommanager");
	}

	public void renderCodingManager(OMManagerNodeInformation nodeInformation) {
		String packageDir = GlobalConfiguration.getInstance().getProperty("compiler.codegeneration.output") + "/" + nodeInformation.getModuleName() + "/manager";
		new File(packageDir).mkdirs();
		
		try {
			setWriter(new FileWriter(packageDir + "/CodingManager.java"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		render("preHeader.ftl");
		render("packageDefinition.ftl", new Attribute("package", nodeInformation.getModuleName() + ".manager"));

		render("import.ftl", new Attribute("import", nodeInformation.getModuleName() + ".om.*"));
		render("import.ftl", new Attribute("import", nodeInformation.getModuleName() + ".coder.*"));

		for (ModuleImportInformation importInformation : nodeInformation.getImports()) {
			render("import.ftl", new Attribute("import", importInformation.getModuleName() + ".om." + importInformation.getTypeName()));
			render("import.ftl", new Attribute("import", importInformation.getModuleName() + ".coder." + importInformation.getTypeName() + "Encoder"));
			render("import.ftl", new Attribute("import", importInformation.getModuleName() + ".coder." + importInformation.getTypeName() + "Decoder"));
		}
		
		render("classHeader.ftl");

		for (AbstractASN1Type type : nodeInformation.getTopLevelTypes()) {
			render("encodeType.ftl", new Attribute("typeName", type.getName()));
			render("decodeType.ftl", new Attribute("typeName", type.getName()));
		}
		
		render("classFooter.ftl");
	}
}