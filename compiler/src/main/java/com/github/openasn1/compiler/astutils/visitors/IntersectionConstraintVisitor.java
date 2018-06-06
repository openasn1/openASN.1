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
package com.github.openasn1.compiler.astutils.visitors;

import java.util.ArrayList;
import java.util.List;

import com.github.openasn1.compiler.astutils.ASN1ASTNodeInfos;
import com.github.openasn1.parser.generated.syntaxtree.BuiltinType;
import com.github.openasn1.parser.generated.syntaxtree.ContainedSubtype;
import com.github.openasn1.parser.generated.syntaxtree.InnerTypeConstraints;
import com.github.openasn1.parser.generated.syntaxtree.PatternConstraint;
import com.github.openasn1.parser.generated.syntaxtree.PermittedAlphabet;
import com.github.openasn1.parser.generated.syntaxtree.ReferencedType;
import com.github.openasn1.parser.generated.syntaxtree.SingleValue;
import com.github.openasn1.parser.generated.syntaxtree.SizeConstraint;
import com.github.openasn1.parser.generated.syntaxtree.TypeConstraint;
import com.github.openasn1.parser.generated.syntaxtree.TypeOrNamedType;
import com.github.openasn1.parser.generated.syntaxtree.ValueRange;
import com.github.openasn1.parser.generated.visitor.GJVoidDepthFirst;

import com.github.openasn1.codec.constraints.Constraint;
import com.github.openasn1.codec.constraints.subtype.PermittedAlphabetConstraint;

/**
 * @author Clayton Hoss
 *
 */
public class IntersectionConstraintVisitor extends GJVoidDepthFirst<Constraint> {
	private List<Constraint> intersections = new ArrayList<Constraint>();

	public List<Constraint> getIntersections() {
		return intersections;
	}

	/**
	 * @param infos
	 */
	public IntersectionConstraintVisitor(ASN1ASTNodeInfos infos) {
		super();
	}
	
	@Override
	public void visit(BuiltinType n, Constraint argu) {
		
	}

	@Override
	public void visit(ReferencedType n, Constraint argu) {
		
	}

	@Override
	public void visit(TypeOrNamedType n, Constraint argu) {
		
	}
	
	
	@Override
	public void visit(ContainedSubtype n, Constraint argu) {
		super.visit(n, argu);
	}

	@Override
	public void visit(InnerTypeConstraints n, Constraint argu) {
		super.visit(n, argu);
	}

	@Override
	public void visit(PatternConstraint n, Constraint argu) {
		super.visit(n, argu);
	}

	@Override
	public void visit(PermittedAlphabet n, Constraint argu) {
		GetConstraintForTypeVisitor innerVisitor = new GetConstraintForTypeVisitor(null);
		n.accept(innerVisitor, null);
		
		PermittedAlphabetConstraint constraint = new PermittedAlphabetConstraint(innerVisitor.getConstraint());
		
		getIntersections().add(constraint);
	}

	@Override
	public void visit(SingleValue n, Constraint argu) {
		super.visit(n, argu);
	}

	@Override
	public void visit(SizeConstraint n, Constraint argu) {
		super.visit(n, argu);
	}

	@Override
	public void visit(TypeConstraint n, Constraint argu) {
		super.visit(n, argu);
	}

	@Override
	public void visit(ValueRange n, Constraint argu) {
		super.visit(n, argu);
	}
}