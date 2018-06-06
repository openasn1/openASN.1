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
package com.github.openasn1.compiler.stages.omast.codeoutput.omcoder.encoder;

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
public class OMEncoderComponentEncoderCodeGeneratingVisitor extends AbstractOMVisitor {
	private OMEncoderTemplateRenderer renderer;
	private AbstractASN1Type parentType;

	/**
	 * holds types on root level. Finally ending up in type assignment
	 */
	private Stack<AbstractASN1Type> aggregationStack = new Stack<AbstractASN1Type>();
	private String moduleName;

	public OMEncoderComponentEncoderCodeGeneratingVisitor(String moduleName, Writer writer, AbstractASN1Type parentType) {
		this.moduleName = moduleName;
		this.renderer = new OMEncoderTemplateRenderer(writer, GlobalConfiguration.getInstance().getProperty("compiler.templates.path")+ "/omcoder");
		this.parentType = parentType;
	}

	private AbstractASN1Type getParentType() {
		return parentType;
	}

	/**
	 * @return the renderer
	 */
	protected OMEncoderTemplateRenderer getRenderer() {
		return renderer;
	}

	/**
	 * @param renderer the renderer to set
	 */
	protected void setRenderer(OMEncoderTemplateRenderer renderer) {
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
	protected Stack<AbstractASN1Type> getAggregationStack() {
		return this.aggregationStack;
	}

	/**
	 * @see com.github.openasn1.compiler.omast.AbstractOMVisitor#visit(com.github.openasn1.compiler.omast.ASN1TypeAssignment)
	 */
	@Override
	public void visit(ASN1TypeAssignment type) {
		super.visit(type);

		getAggregationStack().clear();
	}

	/**
	 * @see com.github.openasn1.compiler.omast.AbstractOMVisitor#visit(com.github.openasn1.compiler.omast.ComplexASN1Type)
	 */
	@Override
	public void visit(ComplexASN1Type type) {
		if (getAggregationStack().isEmpty()) {
			getRenderer().renderEncodeFunctionEncoderListAddStart();
			if (getParentType() == null) {
				getRenderer().renderEncodeFunctionEncoderListAdd(type, type.getName(), ASN1ToJavaNameMapper.getMappedJavaName(type), type.getName());
			} else {
				getRenderer().renderEncodeFunctionEncoderInnerComplexTypeListAdd(type, type.getName(), ASN1ToJavaNameMapper.getMappedJavaName(type), type.getName());
			}
			getRenderer().renderEncodeFunctionEncoderListAddEnd();
		} else {
			getRenderer().renderEncodeFunctionEncoderInnerComplexTypeListAddPrototype(type, type.getName(), ASN1ToJavaNameMapper.getMappedJavaName(type), type.getName());
		}
	}

	/**
	 * @see com.github.openasn1.compiler.omast.AbstractOMVisitor#visit(com.github.openasn1.compiler.omast.ReferencedASN1Type)
	 */
	@Override
	public void visit(ReferencedASN1Type type) {
		if (getAggregationStack().isEmpty()) {
			getRenderer().renderEncodeFunctionEncoderListAddStart();
			getRenderer().renderEncodeFunctionEncoderListAdd(type, type.getReferencedName(), type.getReferencedName(), type.getName());
			getRenderer().renderEncodeFunctionEncoderListAddEnd();
		} else {
			getRenderer().renderEncodeFunctionEncoderListAddPrototype(type, type.getReferencedName(), type.getReferencedName(), type.getName());
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

		if (getAggregationStack().isEmpty()) {
			getRenderer().renderEncodeFunctionEncoderListAddStart();
		}

		OMCoderASNTypeIdentificationVisitor typeNameVisitor = new OMCoderASNTypeIdentificationVisitor();
		typeNameVisitor.visit(type);

		getRenderer().renderEncodeFunctionEncoderListAddAggStart(type, typeNameVisitor.getDecoratedTypeName());
		
		getAggregationStack().push(type);
		super.visit(type);
		getAggregationStack().pop();

		if (getAggregationStack().isEmpty()) {
			getRenderer().renderEncodeFunctionEncoderListAddAggEnd(type, getParentType().getName());
			getRenderer().renderEncodeFunctionEncoderListAddEnd();
		} else {
			getRenderer().renderEncodeFunctionEncoderListAddAggEndPrototype(type, getParentType().getName());
		}
	}
	
	@Override
	public void visit(EnumASN1Type type) {
		if (getAggregationStack().isEmpty()) {
			getRenderer().renderEncodeFunctionEncoderListAddStart();
			getRenderer().renderEncodeFunctionEncoderEnumTypeListAdd(type, getParentType().getName(), ASN1ToJavaNameMapper.getMappedJavaName(type), type.getName());
			getRenderer().renderEncodeFunctionEncoderListAddEnd();
		} else {
			getRenderer().renderEncodeFunctionEncoderEnumTypeListAddPrototype(type, getParentType().getName(), ASN1ToJavaNameMapper.getMappedJavaName(type), type.getName());
		}
	}

	/**
	 * @see com.github.openasn1.compiler.omast.AbstractOMVisitor#visit(com.github.openasn1.compiler.omast.SimpleASN1Type)
	 */
	@Override
	public void visit(SimpleASN1Type type) {
		if (getAggregationStack().isEmpty()) {
			getRenderer().renderEncodeFunctionEncoderListAddStart();
			getRenderer().renderEncodeFunctionEncoderListAdd(type, type.getName(), ASN1ToJavaNameMapper.getMappedJavaName(type), type.getName());
			getRenderer().renderEncodeFunctionEncoderListAddEnd();
		} else {
			getRenderer().renderEncodeFunctionEncoderListAddPrototype(type, type.getName(), ASN1ToJavaNameMapper.getMappedJavaName(type), type.getName());
		}
	}
}