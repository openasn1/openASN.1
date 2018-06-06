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

import java.io.Writer;
import java.util.Stack;

import com.github.openasn1.compiler.codeoutpututils.ASN1ToJavaNameMapper;
import com.github.openasn1.compiler.omast.ASN1TypeAssignment;
import com.github.openasn1.compiler.omast.AbstractASN1Type;
import com.github.openasn1.compiler.omast.AbstractOMVisitor;
import com.github.openasn1.compiler.omast.AggregatedASN1Type;
import com.github.openasn1.compiler.omast.ComplexASN1Type;
import com.github.openasn1.compiler.omast.EnumASN1Type;
import com.github.openasn1.compiler.omast.ReferencedASN1Type;
import com.github.openasn1.compiler.omast.SimpleASN1Type;
import com.github.openasn1.compiler.stages.omast.codeoutput.omcoder.OMCoderASNTypeIdentificationVisitor;
import com.github.openasn1.compiler.utils.GlobalConfiguration;

/**
 * @author Clayton Hoss
 *
 */
public class OMDecoderComponentDecoderCodeGeneratingVisitor extends AbstractOMVisitor {
	private OMDecoderTemplateRenderer renderer;
	private AbstractASN1Type parentType;

	/**
	 * holds types on root level. Finally ending up in type assignment
	 */
	private Stack<AbstractASN1Type> childStack = new Stack<AbstractASN1Type>();
	private String moduleName;

	public OMDecoderComponentDecoderCodeGeneratingVisitor(String moduleName, Writer writer, AbstractASN1Type parentType) {
		this.moduleName = moduleName;
		this.renderer = new OMDecoderTemplateRenderer(writer, GlobalConfiguration.getInstance().getProperty("compiler.templates.path")+ "/omcoder");
		this.parentType = parentType;
	}

	private AbstractASN1Type getParentType() {
		return parentType;
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

	/**
	 * @see com.github.openasn1.compiler.omast.AbstractOMVisitor#visit(com.github.openasn1.compiler.omast.ASN1TypeAssignment)
	 */
	@Override
	public void visit(ASN1TypeAssignment type) {
		super.visit(type);

		getChildStack().clear();
	}

	/**
	 * @see com.github.openasn1.compiler.omast.AbstractOMVisitor#visit(com.github.openasn1.compiler.omast.ComplexASN1Type)
	 */
	@Override
	public void visit(ComplexASN1Type type) {
		if (getChildStack().isEmpty()) {
			if (getParentType() == null) {
				getRenderer().renderDecodeFunctionDecoderListAddStart(ASN1ToJavaNameMapper.getMappedJavaName(type), type.getName());
				getRenderer().renderDecodeFunctionDecoderListAdd(type, type.getName(), ASN1ToJavaNameMapper.getMappedJavaName(type), type.getName());
			} else {
				getRenderer().renderDecodeFunctionDecoderInnerComplexTypeListAddStart(type, ASN1ToJavaNameMapper.getMappedJavaName(type), type.getName());
				getRenderer().renderDecodeFunctionDecoderInnerComplexTypeListAdd(type, type.getName(), ASN1ToJavaNameMapper.getMappedJavaName(type), type.getName());
			}
			getRenderer().renderDecodeFunctionDecoderListAddEnd();
		} else {
			getRenderer().renderDecodeFunctionDecoderInnerComplexTypeListAddPrototype(type, type.getName(), ASN1ToJavaNameMapper.getMappedJavaName(type), type.getName());
		}
	}

	/**
	 * @see com.github.openasn1.compiler.omast.AbstractOMVisitor#visit(com.github.openasn1.compiler.omast.ReferencedASN1Type)
	 */
	@Override
	public void visit(ReferencedASN1Type type) {
		if (getChildStack().isEmpty()) {
			getRenderer().renderDecodeFunctionDecoderListAddStart(type.getReferencedName(), type.getName());
			getRenderer().renderDecodeFunctionDecoderListAdd(type, type.getReferencedName(), type.getReferencedName(), type.getName());
			getRenderer().renderDecodeFunctionDecoderListAddEnd();
		} else {
			getRenderer().renderDecodeFunctionDecoderListAddPrototype(type, type.getReferencedName(), type.getReferencedName(), type.getName());
		}
	}

	/* (non-Javadoc)
	 * @see com.github.openasn1.compiler.omast.AbstractOMVisitor#visit(com.github.openasn1.compiler.omast.AggregatedASN1Type)
	 */
	@Override
	public void visit(AggregatedASN1Type type) {
		if (type.getChild().getName() == null) {
			type.getChild().setName(type.getName());
		}

		OMCoderASNTypeIdentificationVisitor typeNameVisitor = new OMCoderASNTypeIdentificationVisitor();
		typeNameVisitor.visit(type);

		if (getChildStack().isEmpty()) {
			getRenderer().renderDecodeFunctionDecoderListAddAggDef(ASN1ToJavaNameMapper.getMappedJavaName(type), typeNameVisitor.getDecoratedTypeName(), type.getName());
		}

		getRenderer().renderDecodeFunctionDecoderListAddAggStart(type, typeNameVisitor.getDecoratedTypeName());
		
		getChildStack().push(type);
		super.visit(type);
		getChildStack().pop();
		
		if (getChildStack().isEmpty()) {
			getRenderer().renderDecodeFunctionDecoderListAddAggEnd(type, getParentType().getName());
			getRenderer().renderDecodeFunctionDecoderListAddEnd();
		} else {
			getRenderer().renderDecodeFunctionDecoderListAddAggEndPrototype(type, getParentType().getName());
		}
	}

	/**
	 * @see com.github.openasn1.compiler.omast.AbstractOMVisitor#visit(com.github.openasn1.compiler.omast.SimpleASN1Type)
	 */
	@Override
	public void visit(SimpleASN1Type type) {
		if (getChildStack().isEmpty()) {
			getRenderer().renderDecodeFunctionDecoderListAddStart(ASN1ToJavaNameMapper.getMappedJavaName(type), type.getName());
			getRenderer().renderDecodeFunctionDecoderListAdd(type, type.getName(), ASN1ToJavaNameMapper.getMappedJavaName(type), type.getName());
			getRenderer().renderDecodeFunctionDecoderListAddEnd();
		} else {
			getRenderer().renderDecodeFunctionDecoderListAddPrototype(type, type.getName(), ASN1ToJavaNameMapper.getMappedJavaName(type), type.getName());
		}
	}

	@Override
	public void visit(EnumASN1Type type) {
		if (getChildStack().isEmpty()) {
			getRenderer().renderDecodeFunctionDecoderEnumTypeListAddStart(type, ASN1ToJavaNameMapper.getMappedJavaName(type), type.getName());
			getRenderer().renderDecodeFunctionDecoderEnumTypeListAdd(type, type.getName(), ASN1ToJavaNameMapper.getMappedJavaName(type), type.getName());
			getRenderer().renderDecodeFunctionDecoderListAddEnd();
		} else {
			getRenderer().renderDecodeFunctionDecoderListAddPrototype(type, type.getName(), ASN1ToJavaNameMapper.getMappedJavaName(type), type.getName());
		}
	}
}