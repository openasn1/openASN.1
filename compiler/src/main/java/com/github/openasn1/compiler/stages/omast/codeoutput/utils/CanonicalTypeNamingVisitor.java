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

import com.github.openasn1.compiler.omast.ASN1TypeAssignment;
import com.github.openasn1.compiler.omast.AbstractASN1Type;
import com.github.openasn1.compiler.omast.AggregatedASN1Type;
import com.github.openasn1.compiler.omast.ComplexASN1Type;
import com.github.openasn1.compiler.omast.EnumASN1Type;
import com.github.openasn1.compiler.omast.ReferencedASN1Type;
import com.github.openasn1.compiler.omast.SimpleASN1Type;


public class CanonicalTypeNamingVisitor extends UnspecificOMVisitorImpl {
	private List<AbstractASN1Type> nameContributingTypes = new ArrayList<AbstractASN1Type>();
	private boolean aggregatedTypeIncluded = false;
	
	public CanonicalTypeNamingVisitor() {	
	}
	
	public CanonicalTypeNamingVisitor(boolean includeAggregatedType) {
		this.aggregatedTypeIncluded = includeAggregatedType;
	}
	
	public boolean isAggregatedTypeIncluded() {
		return aggregatedTypeIncluded;
	}

	public void setAggregatedTypeIncluded(boolean includeAggregatedType) {
		this.aggregatedTypeIncluded = includeAggregatedType;
	}

	public List<AbstractASN1Type> getNameContributingTypes() {
		return this.nameContributingTypes;
	}

	public int getNestingDepth() {
		return getNameContributingTypes().size() - 1;
	}
	
	public List<String> getContributingTypeNames(TypeNamingStrategy namingStrategy) {
		List<String> names = new ArrayList<String>();
		
		int depth = 0;

		if (getNameContributingTypes().size() > 0) {
			AbstractASN1Type last = getNameContributingTypes().get(getNameContributingTypes().size()-1);

			for (AbstractASN1Type type : getNameContributingTypes()) {
				if (type != last) {
					names.add(namingStrategy.getName(type.getName(), depth));
					depth++;
				}
			}
			
			names.add(namingStrategy.getName(last.getName(), depth));
		}

		return names;
	}	

	public String getCanonicalName(TypeNamingStrategy namingStrategy) {
		StringBuffer nameBuffer = new StringBuffer();
		int depth = 0;

		if (getNameContributingTypes().size() > 0) {
			AbstractASN1Type last = getNameContributingTypes().get(getNameContributingTypes().size()-1);

			for (AbstractASN1Type type : getNameContributingTypes()) {
				if (type != last) {
					nameBuffer.append(namingStrategy.getName(type.getName(), depth));
					nameBuffer.append(".");
					depth++;
				}
			}

			nameBuffer.append(namingStrategy.getName(last.getName(), depth));
		}

		return nameBuffer.toString();
	}

	@Override
	public void visitUnspecific(AbstractASN1Type type) {
		if (type.getParent() != null) {
			type.getParent().accept(this);
		}
	}

	@Override
	public void visit(ComplexASN1Type type) {
		getNameContributingTypes().add(0, type);

		super.visit(type);
	}

	@Override
	public void visit(EnumASN1Type type) {
		getNameContributingTypes().add(0, type);

		super.visit(type);
	}

	@Override
	public void visit(ReferencedASN1Type type) {
		getNameContributingTypes().add(0, type);

		super.visit(type);
	}

	@Override
	public void visit(SimpleASN1Type type) {
		getNameContributingTypes().add(0, type);

		super.visit(type);
	}

	@Override
	public void visit(AggregatedASN1Type type) {
		if (isAggregatedTypeIncluded() || (type.getParent() instanceof ASN1TypeAssignment)) {
			getNameContributingTypes().add(0, type);
		}
		
		super.visit(type);
	}
	
}