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
package com.github.openasn1.compiler.stages.omast.codeoutput.omcoder.decoder;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Stack;

import org.apache.log4j.Logger;
import com.github.openasn1.compiler.omast.ASN1ImportListFromModule;
import com.github.openasn1.compiler.omast.ASN1Module;
import com.github.openasn1.compiler.omast.ASN1TypeAssignment;
import com.github.openasn1.compiler.omast.AbstractASN1Type;
import com.github.openasn1.compiler.omast.AbstractOMVisitor;
import com.github.openasn1.compiler.omast.AggregatedASN1Type;
import com.github.openasn1.compiler.omast.ComplexASN1Type;
import com.github.openasn1.compiler.omast.ComplexASN1TypeEnum;
import com.github.openasn1.compiler.omast.EnumASN1Type;
import com.github.openasn1.compiler.omast.ReferencedASN1Type;
import com.github.openasn1.compiler.omast.SimpleASN1Type;
import com.github.openasn1.compiler.stages.omast.codeoutput.ModuleImportInformation;
import com.github.openasn1.compiler.utils.GlobalConfiguration;

/**
 * @author Clayton Hoss
 *
 */
public class OMDecoderCodeGeneratingVisitor extends AbstractOMVisitor {
	private static Logger LOGGER = Logger.getLogger("OMCoderCodeGeneratingVisitor");
	private OMDecoderTemplateRenderer renderer;

	/**
	 * holds types on root level. Finally ending up in type assignment
	 */
	private Stack<AbstractASN1Type> childStack = new Stack<AbstractASN1Type>();
	private ArrayList<ModuleImportInformation> imports = new ArrayList<ModuleImportInformation>();
	private String moduleName;

	public OMDecoderCodeGeneratingVisitor(Writer writer) {
		this.renderer = new OMDecoderTemplateRenderer(writer, GlobalConfiguration.getInstance().getProperty("compiler.templates.path")+ "/omcoder");
	}

	public OMDecoderCodeGeneratingVisitor() throws IOException {
		this.renderer = new OMDecoderTemplateRenderer(null, GlobalConfiguration.getInstance().getProperty("compiler.templates.path")+ "/omcoder");
	}

	private boolean isInnerVisitor() {
		return getChildStack().size() > 0;
	}

	/**
	 * @return the renderer
	 */
	protected OMDecoderTemplateRenderer getRenderer() {
		return renderer;
	}

	/**
	 * @param renderer the renderer to set
	 */
	protected void setRenderer(OMDecoderTemplateRenderer renderer) {
		this.renderer = renderer;
	}

	/**
	 * @return the moduleName
	 */
	protected String getModuleName() {
		return this.moduleName;
	}

	/**
	 * @param moduleName the moduleName to set
	 */
	protected void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}

	/**
	 * @return the classInformationStack
	 */
	protected Stack<AbstractASN1Type> getChildStack() {
		return this.childStack;
	}

	private ArrayList<ModuleImportInformation> getImports() {
		return imports;
	}

	@Override
	public void visit(ASN1ImportListFromModule type) {
		for (String importName : type.getImportNames()) {
			getImports().add(new ModuleImportInformation(type.getModuleName(), importName));
		}

		super.visit(type);
	}

	@Override
	public void visit(ASN1Module type) {
		setModuleName(type.getName());

		File outputDir = new File(GlobalConfiguration.getInstance().getProperty("compiler.codegeneration.output") + "/" + getModuleName() + "/coder");
		outputDir.mkdirs();

		super.visit(type);
	}

	/**
	 * @see com.github.openasn1.compiler.omast.AbstractOMVisitor#visit(com.github.openasn1.compiler.omast.ASN1TypeAssignment)
	 */
	@Override
	public void visit(ASN1TypeAssignment type) {
		super.visit(type);

		getChildStack().clear();

		if (getRenderer().getWriter() != null) {
			try {
				getRenderer().getWriter().flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void setOutputFile(String fileName) {
		LOGGER.debug("Generating decoder class '" + fileName + "'");

		String packageDir = GlobalConfiguration.getInstance().getProperty("compiler.codegeneration.output") + "/" + moduleName + "/coder";
		try {
			if (getRenderer().getWriter() != null) {
				getRenderer().getWriter().flush();
			}
			getRenderer().setWriter(new FileWriter(packageDir + "/" + fileName));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @see com.github.openasn1.compiler.omast.AbstractOMVisitor#visit(com.github.openasn1.compiler.omast.ComplexASN1Type)
	 */
	@Override
	public void visit(ComplexASN1Type type) {
		if (! isInnerVisitor()) {
			setOutputFile(type.getName() + "Decoder.java");
			getRenderer().renderFileHeader(getModuleName() + ".coder");
			getRenderer().renderImport(getModuleName() + ".om.*");
			for (ModuleImportInformation importInformation : getImports()) {
				getRenderer().renderImport(importInformation.getModuleName() + ".om." + importInformation.getTypeName());
				getRenderer().renderImport(importInformation.getModuleName() + ".coder." + importInformation.getTypeName() + "Decoder");
			}
		}
		getRenderer().renderClassHeader(type, isInnerVisitor());

		// Render inner decoders
			getChildStack().push(type);
			for (AbstractASN1Type childType : type.getChildren()) {
				childType.accept(this);
			}
			getChildStack().pop();

		getRenderer().renderClassConstructor(type);

		getRenderer().renderDecodeFunctionHeader(type);
		getRenderer().renderDecodeFunctionDecoderList();

		for (AbstractASN1Type childType : type.getChildren()) {
			OMDecoderComponentDecoderCodeGeneratingVisitor childVisitor = new OMDecoderComponentDecoderCodeGeneratingVisitor(getModuleName(), getRenderer().getWriter(), type);
			childType.accept(childVisitor);
		}

		for (AbstractASN1Type childType : type.getChildren()) {
			getRenderer().renderDecodeFunctionDecoderAddToComponentList(childType);
		}

		OMDecoderDecodeCodeGeneratingVisitor childVisitor = new OMDecoderDecodeCodeGeneratingVisitor(getModuleName(), getRenderer().getWriter());
		childVisitor.visit(type);
		
		getRenderer().renderDecodeFunctionOMPrototype(type);

		boolean isChoice = ComplexASN1TypeEnum.CHOICE.equals(type.getAsn1Type());
		
		for (AbstractASN1Type childType : type.getChildren()) {
			if (isChoice)  {
				getRenderer().renderDecodeFunctionDecodeChoice(type, childType);
			} else {
				getRenderer().renderDecodeFunctionDecode(type, childType);
			}
		}

		getRenderer().renderDecodeFunctionFooter();

		getRenderer().renderClassFooter(type);
	}

	@Override
	public void visit(ReferencedASN1Type type) {
		if (isInnerVisitor()) {
			return;
		}

		setOutputFile(type.getName() + "Decoder.java");
		getRenderer().renderReferencedType(getModuleName(), type, getImports());
	}

	/* (non-Javadoc)
	 * @see com.github.openasn1.compiler.omast.AbstractOMVisitor#visit(com.github.openasn1.compiler.omast.AggregatedASN1Type)
	 */
	@Override
	public void visit(AggregatedASN1Type type) {
		if (isInnerVisitor()) {
			super.visit(type);
			return;
		}
		
		if (type.getChild().getName() == null) {
			type.getChild().setName(type.getName());
		}
		
		setOutputFile(type.getName() + "Decoder.java");
		getRenderer().renderFileHeader(getModuleName() + ".coder");
		getRenderer().renderImport(getModuleName() + ".om.*");
		for (ModuleImportInformation importInformation : getImports()) {
			getRenderer().renderImport(importInformation.getModuleName() + ".om." + importInformation.getTypeName());
			getRenderer().renderImport(importInformation.getModuleName() + ".coder." + importInformation.getTypeName() + "Decoder");
		}

		getRenderer().renderClassHeader(type, isInnerVisitor());

		// Render inner decoders
			getChildStack().push(type);
			type.getChild().accept(this);
			getChildStack().pop();
		
		getRenderer().renderClassConstructor(type);

		getRenderer().renderDecodeFunctionHeader(type);

		OMDecoderComponentDecoderCodeGeneratingVisitor childVisitor = new OMDecoderComponentDecoderCodeGeneratingVisitor(getModuleName(), getRenderer().getWriter(), type);
		type.getChild().accept(childVisitor);

		// TypeAggregationVisitor aggregationVisitor = new TypeAggregationVisitor();
		// type.getChild().accept(aggregationVisitor);

//		OMDecoderDecodeCodeGeneratingVisitor visitor = new OMDecoderDecodeCodeGeneratingVisitor(getModuleName(), getRenderer().getWriter());
		// visitor.visit(type);
		
		getRenderer().renderDecodeFunctionOMPrototype(type);

		getRenderer().renderDecodeFunctionAggregatedTypeDecode(type, type.getChild());

		getRenderer().renderDecodeFunctionFooter();

		getRenderer().renderClassFooter(type);
	}
	
	@Override
	public void visit(EnumASN1Type type) {
		if (isInnerVisitor()) {
			return;
		}
		setOutputFile(type.getName() + "Decoder.java");
		getRenderer().renderFileHeader(getModuleName() + ".coder");
		getRenderer().renderImport("java.util.HashMap");

		getRenderer().renderImport(getModuleName() + ".om.*");

		for (ModuleImportInformation importInformation : getImports()) {
			getRenderer().renderImport(importInformation.getModuleName() + ".om." + importInformation.getTypeName());
			getRenderer().renderImport(importInformation.getModuleName() + ".coder." + importInformation.getTypeName() + "Decoder");
		}

		getRenderer().renderEnum(type, isInnerVisitor());
	}

	/**
	 * @see com.github.openasn1.compiler.omast.AbstractOMVisitor#visit(com.github.openasn1.compiler.omast.SimpleASN1Type)
	 */
	@Override
	public void visit(SimpleASN1Type type) {
		if (isInnerVisitor()) {
			return;
		}
		
		setOutputFile(type.getName() + "Decoder.java");
		getRenderer().renderFileHeader(getModuleName() + ".coder");
		getRenderer().renderImport(getModuleName() + ".om.*");

		for (ModuleImportInformation importInformation : getImports()) {
			getRenderer().renderImport(importInformation.getModuleName() + ".om." + importInformation.getTypeName());
			getRenderer().renderImport(importInformation.getModuleName() + ".coder." + importInformation.getTypeName() + "Decoder");
		}

		getRenderer().renderClassHeader(type, isInnerVisitor());
		getRenderer().renderClassConstructor(type);

		getRenderer().renderDecodeFunctionHeader(type);

		OMDecoderDecodeCodeGeneratingVisitor childVisitor = new OMDecoderDecodeCodeGeneratingVisitor(getModuleName(), getRenderer().getWriter());
		childVisitor.visit(type);

		getRenderer().renderDecodeFunctionFooter();

		getRenderer().renderClassFooter(type);
	}
}