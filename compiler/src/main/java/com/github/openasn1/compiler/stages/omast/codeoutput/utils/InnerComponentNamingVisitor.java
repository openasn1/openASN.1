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
import com.github.openasn1.compiler.omast.ComplexASN1Type;
import com.github.openasn1.compiler.omast.EnumASN1Type;
import com.github.openasn1.compiler.omast.ReferencedASN1Type;
import com.github.openasn1.compiler.omast.SimpleASN1Type;
import com.github.openasn1.compiler.utils.StringUtils;

/**
 * @author Clayton Hoss
 *
 */
public class InnerComponentNamingVisitor extends UnspecificOMVisitorImpl {
	private List<AbstractASN1Type> nameContributingTypes = new ArrayList<AbstractASN1Type>();
	
	private List<AbstractASN1Type> getNameContributingTypes() {
		return nameContributingTypes;
	}
	
	public String getComponentName() {
		return StringUtils.repeat("_", getNameContributingTypes().size() - 2) + getNameContributingTypes().get(getNameContributingTypes().size()-1).getName();
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
		super.visit(type);
	}
}