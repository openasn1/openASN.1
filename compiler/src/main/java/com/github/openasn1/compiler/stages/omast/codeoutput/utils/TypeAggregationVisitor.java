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
package com.github.openasn1.compiler.stages.omast.codeoutput.utils;

import java.util.ArrayList;
import java.util.List;

import com.github.openasn1.compiler.omast.AbstractASN1Type;
import com.github.openasn1.compiler.omast.AggregatedASN1Type;

/**
 * @author Clayton Hoss
 *
 */
public class TypeAggregationVisitor extends UnspecificOMVisitorImpl {
	private TypeNamingStrategy typeNamingStrategy = new AggregationNamingStrategy();
	private List<AggregatedASN1Type> enclosingAggregations = new ArrayList<AggregatedASN1Type>();
	private AbstractASN1Type enclosedType;
	
	public TypeNamingStrategy getTypeNamingStrategy() {
		return typeNamingStrategy;
	}

	public void setTypeNamingStrategy(TypeNamingStrategy typeNamingStrategy) {
		this.typeNamingStrategy = typeNamingStrategy;
	}

	private List<AggregatedASN1Type> getEnclosingAggregations() {
		return enclosingAggregations;
	}
	
	public int getAggregationDepth() {
		return getEnclosingAggregations().size();
	}

	public AbstractASN1Type getEnclosedType() {
		return enclosedType;
	}
	
	private void setEnclosedType(AbstractASN1Type enclosedType) {
		this.enclosedType = enclosedType;
	}

	public String getEnclosedTypeName() {
		return getTypeNamingStrategy().getName(getEnclosedType().getName(), getAggregationDepth());
	}

	@Override
	public void visitUnspecific(AbstractASN1Type type) {
		AbstractASN1Type parent = type.getParent();
		
		setEnclosedType(type);
		
		while (parent instanceof AggregatedASN1Type) {
			getEnclosingAggregations().add((AggregatedASN1Type)parent);
			parent = parent.getParent();
		}
	}

	@Override
	public void visit(AggregatedASN1Type type) {
		if (type.getChild() != null) {
			type.getChild().accept(this);
		}
	}
}