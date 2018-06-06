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

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import org.apache.log4j.Logger;
import com.github.openasn1.common.TagValue;
import com.github.openasn1.compiler.astutils.OMAndASN1ASTStorage;
import com.github.openasn1.compiler.codeoutpututils.templaterenderer.Attribute;
import com.github.openasn1.compiler.codeoutpututils.templaterenderer.AttributeHelper;
import com.github.openasn1.compiler.codeoutpututils.templaterenderer.FreemarkerRenderer;
import com.github.openasn1.compiler.omast.EnumASN1Type;
import com.github.openasn1.compiler.omast.ReferencedASN1Type;
import com.github.openasn1.compiler.stages.omast.codeoutput.utils.CanonicalTypeNamingVisitor;
import com.github.openasn1.compiler.stages.omast.codeoutput.utils.InnerComponentNamingStrategy;
import com.github.openasn1.compiler.utils.GlobalConfiguration;
import com.github.openasn1.compiler.utils.reflectivevisitor.ConstraintReflectiveVisitor;

/**
 * @author Clayton Hoss
 *
 */
public class OMCoderTypeInformationRenderer extends FreemarkerRenderer {
	private static Logger LOGGER = Logger.getLogger(OMCoderTypeInformationRenderer.class.getSimpleName());

	private String packageDir;
	private OMAndASN1ASTStorage storage;
	private OMCoderNodeInformation rootNodeInformation;
	
	public OMCoderTypeInformationRenderer(OMAndASN1ASTStorage storage) {
		super(null, GlobalConfiguration.getInstance().getProperty("compiler.templates.path")+ "/omcoder");
		this.storage = storage;
	}
	
	protected OMAndASN1ASTStorage getStorage() {
		return storage;
	}

	private String getPackageDir() {
		return packageDir;
	}

	private void setPackageDir(String packageDir) {
		this.packageDir = packageDir;
	}
	
	private OMCoderNodeInformation getRootNodeInformation() {
		return rootNodeInformation;
	}

	private void setRootNodeInformation(OMCoderNodeInformation rootNodeInformation) {
		this.rootNodeInformation = rootNodeInformation;
	}

	public void renderTypeInformation(OMCoderNodeInformation nodeInformation) {
		String packageDir = GlobalConfiguration.getInstance().getProperty("compiler.codegeneration.output") + "/" + nodeInformation.getModuleName() + "/coder";
		this.setPackageDir(packageDir);
		new File(packageDir).mkdirs();
		
		setRootNodeInformation(nodeInformation);
		
		for (OMCoderNodeInformation child : nodeInformation.getChildren()) {
			renderASN1RootType(child);
		}
	}
	
	public void renderASN1RootType(OMCoderNodeInformation nodeInformation) {
		try {
			setWriter(new FileWriter(getPackageDir() + "/" + nodeInformation.getAsn1Type().getName() + "TypeInformation.java"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		render("preHeader.ftl");
		render("typeinformation/packageDefinition.ftl", new Attribute("package", getRootNodeInformation().getModuleName() + ".coder"));
		render("import.ftl", new Attribute("import", getRootNodeInformation().getModuleName() + ".om.*"));
		
		ArrayList<Map> tags = new ArrayList<Map>();

		for (TagValue tagValue : nodeInformation.getTagList()) {
			tags.add(AttributeHelper.createMap( 
					new Attribute("tagMode", tagValue.getTaggingMode().name()),
					new Attribute("tagClass", tagValue.getTagClass().name()),
					new Attribute("tagIndex", tagValue.getIndex())));
		}
		
		boolean isEnum = nodeInformation.getAsn1Type() instanceof EnumASN1Type;
		Map<String, Integer> enumMap = null;
		if (isEnum) {
			EnumASN1Type enumType = (EnumASN1Type)nodeInformation.getAsn1Type();
			enumMap = enumType.getEnums();
		}
		
		ArrayList<String> components = new ArrayList<String>();
		for (OMCoderNodeInformation childInfo : nodeInformation.getChildren()) {
			components.add(childInfo.getAsn1Type().getName());
		}
		
		render("typeinformation/class.ftl", new Attribute("className", nodeInformation.getAsn1Type().getName()),
											new Attribute("isEnum", isEnum),
											new Attribute("referencedType", getReferencedType(nodeInformation)),
											new Attribute("constraintTree", getConstraintTree(nodeInformation)),
											new Attribute("enumMap", enumMap),
											new Attribute("tags", tags),
											new Attribute("components", components),
											new Attribute("isExtensible", nodeInformation.isExtensible() ? "true" : "false"));
		
		for (OMCoderNodeInformation childInfo : nodeInformation.getChildren()) {
			renderASN1TypeComponent(childInfo, 1);
		}
		
		render("typeinformation/classFooter.ftl");
	}

	private String getReferencedType(OMCoderNodeInformation nodeInformation) {
		String referencedType = null;
		if (nodeInformation.getAsn1Type() instanceof ReferencedASN1Type) {
			ReferencedASN1Type referencedASN1Type = (ReferencedASN1Type)nodeInformation.getAsn1Type();
			referencedType = referencedASN1Type.referencedName;
		}
		return referencedType;
	}

	private String getConstraintTree(OMCoderNodeInformation nodeInformation) {
		ConstraintReflectiveVisitor reflectiveVisitor = new ConstraintReflectiveVisitor();
		String constraintTree = null;
		
		if (nodeInformation.getConstraint() != null) {
			reflectiveVisitor.process(nodeInformation.getConstraint());
			constraintTree = reflectiveVisitor.getOutput();
			LOGGER.debug("  Adding constraint: " + reflectiveVisitor.getOutput());
		}
		
		return constraintTree;
	}
	
	public void renderASN1TypeComponent(OMCoderNodeInformation nodeInformation, int nestingLevel) {
		ArrayList<Map> tags = new ArrayList<Map>();

		for (TagValue tagValue : nodeInformation.getTagList()) {
			tags.add(AttributeHelper.createMap( 
					new Attribute("tagMode", tagValue.getTaggingMode().name()),
					new Attribute("tagClass", tagValue.getTagClass().name()),
					new Attribute("tagIndex", tagValue.getIndex())));
		}

		boolean isEnum = nodeInformation.getAsn1Type() instanceof EnumASN1Type;
		boolean isStatic = getRootNodeInformation().equals(nodeInformation.getParent().getParent());
		String componentName = InnerComponentNamingStrategy.getInstance().getName(nodeInformation.getAsn1Type().getName(), nestingLevel);

		CanonicalTypeNamingVisitor visitor = new CanonicalTypeNamingVisitor();
		nodeInformation.getAsn1Type().accept(visitor);
		
		String canonicalOMClassName = visitor.getCanonicalName(InnerComponentNamingStrategy.getInstance());

		Map<String, Integer> enumMap = null;
		if (isEnum) {
			EnumASN1Type enumType = (EnumASN1Type)nodeInformation.getAsn1Type();
			enumMap = enumType.getEnums();
		}
		
		render("typeinformation/typeComponent.ftl", new Attribute("tags", tags),
													new Attribute("isEnum", isEnum),
													new Attribute("enumMap", enumMap),
													new Attribute("referencedType", getReferencedType(nodeInformation)),
													new Attribute("isStatic", isStatic),
													new Attribute("componentName", componentName),
													new Attribute("constraintTree", getConstraintTree(nodeInformation)),
													new Attribute("canonicalOMClassName", canonicalOMClassName),
													new Attribute("isExtensible", nodeInformation.isExtensible() ? "true" : "false"),
													new Attribute("isExtensionAddition", nodeInformation.isInExtension() ? "true" : "false"),
													new Attribute("isOptional", nodeInformation.isOptional() ? "true" : "false"));

		for (OMCoderNodeInformation childInfo : nodeInformation.getChildren()) {
			renderASN1TypeComponent(childInfo, nestingLevel+1);
		}

		render("typeinformation/typeComponentEnd.ftl", 	new Attribute("componentName", componentName),
														new Attribute("isStatic", isStatic));
	}
}