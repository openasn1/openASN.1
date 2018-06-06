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
package com.github.openasn1.compiler.stages.omast.codeoutput.omcoder;

import com.github.openasn1.compiler.codeoutpututils.ASN1ToJavaNameMapper;
import com.github.openasn1.compiler.omast.AbstractOMVisitor;
import com.github.openasn1.compiler.omast.AggregatedASN1Type;
import com.github.openasn1.compiler.omast.ComplexASN1Type;
import com.github.openasn1.compiler.omast.EnumASN1Type;
import com.github.openasn1.compiler.omast.ReferencedASN1Type;
import com.github.openasn1.compiler.omast.SimpleASN1Type;
import com.github.openasn1.compiler.stages.omast.codeoutput.utils.CanonicalTypeNamingVisitor;
import com.github.openasn1.compiler.stages.omast.codeoutput.utils.InnerComponentNamingStrategy;

/**
 * @author Clayton Hoss
 *
 */
public class OMCoderASNTypeIdentificationVisitor extends AbstractOMVisitor {
	private StringBuffer typeNameBuffer;
	private String typeName;
	private int aggregationDepth = 0; 

	public OMCoderASNTypeIdentificationVisitor() {
		this.typeNameBuffer = new StringBuffer();
	}
	
	private StringBuffer getTypeNameBuffer() {
		return typeNameBuffer;
	}
	
	public String getDecoratedTypeName() {
		return getTypeNameBuffer().toString();
	}

	/**
	 * @see com.github.openasn1.compiler.omast.AbstractOMVisitor#visit(com.github.openasn1.compiler.omast.ComplexASN1Type)
	 */
	@Override
	public void visit(ComplexASN1Type type) {
		CanonicalTypeNamingVisitor visitor = new CanonicalTypeNamingVisitor();
		type.accept(visitor);
		
		setTypeName(visitor.getCanonicalName(InnerComponentNamingStrategy.getInstance()));
		getTypeNameBuffer().append(getTypeName());
	}
	
	@Override
	public void visit(EnumASN1Type type) {
		CanonicalTypeNamingVisitor visitor = new CanonicalTypeNamingVisitor();
		type.accept(visitor);
		
		setTypeName(visitor.getCanonicalName(InnerComponentNamingStrategy.getInstance()));
		getTypeNameBuffer().append(getTypeName());
	}

	/**
	 * @see com.github.openasn1.compiler.omast.AbstractOMVisitor#visit(com.github.openasn1.compiler.omast.ReferencedASN1Type)
	 */
	@Override
	public void visit(ReferencedASN1Type type) {
		setTypeName(type.getReferencedName());
		getTypeNameBuffer().append(getTypeName());
	}


	/* (non-Javadoc)
	 * @see com.github.openasn1.compiler.omast.AbstractOMVisitor#visit(com.github.openasn1.compiler.omast.AggregatedASN1Type)
	 */
	@Override
	public void visit(AggregatedASN1Type type) {
		aggregationDepth++;

		if (aggregationDepth > 1) {
			getTypeNameBuffer().append("List<");
		}
		super.visit(type);
		if (aggregationDepth > 1) {
			getTypeNameBuffer().append(">");
		}
		
		aggregationDepth--;
	}

	/**
	 * @see com.github.openasn1.compiler.omast.AbstractOMVisitor#visit(com.github.openasn1.compiler.omast.SimpleASN1Type)
	 */
	@Override
	public void visit(SimpleASN1Type type) {
		setTypeName(ASN1ToJavaNameMapper.getMappedJavaType(type));
		getTypeNameBuffer().append(getTypeName());
	}

	public String getTypeName() {
		return typeName;
	}

	private void setTypeName(String undecoratedTypeName) {
		this.typeName = undecoratedTypeName;
	}
}