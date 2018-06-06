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

import com.github.openasn1.compiler.omast.AbstractASN1Type;
import com.github.openasn1.compiler.omast.AbstractOMVisitor;
import com.github.openasn1.compiler.omast.AggregatedASN1Type;
import com.github.openasn1.compiler.omast.ComplexASN1Type;
import com.github.openasn1.compiler.omast.EnumASN1Type;
import com.github.openasn1.compiler.omast.ReferencedASN1Type;
import com.github.openasn1.compiler.omast.SimpleASN1Type;

/**
 * @author Clayton Hoss
 *
 */
public class OMCoderTypeChildrenCollectingVisitor extends AbstractOMVisitor {
	private OMCoderNodeInformation nodeInformation;
	
	public OMCoderTypeChildrenCollectingVisitor(OMCoderNodeInformation nodeInformation) {
		this.nodeInformation = nodeInformation;
	}
	
	public OMCoderNodeInformation getNodeInformation() {
		return nodeInformation;
	}

	/**
	 * @see com.github.openasn1.compiler.omast.AbstractOMVisitor#visit(com.github.openasn1.compiler.omast.ComplexASN1Type)
	 */
	@Override
	public void visit(ComplexASN1Type type) {
		OMCoderNodeInformation childInfo = new OMCoderNodeInformation();
		childInfo.setAsn1Type(type);
		childInfo.setParent(getNodeInformation());
		getNodeInformation().getChildren().add(childInfo);
		
		for (AbstractASN1Type childType : type.getChildren()) {
			OMCoderTypeChildrenCollectingVisitor childVisitor = new OMCoderTypeChildrenCollectingVisitor(childInfo);
			childType.accept(childVisitor);
		}
	}
	
	@Override
	public void visit(AggregatedASN1Type type) {
		OMCoderNodeInformation childInfo = new OMCoderNodeInformation();
		childInfo.setAsn1Type(type);
		childInfo.setParent(getNodeInformation());
		getNodeInformation().getChildren().add(childInfo);
		
		if (type.getChild() != null) {
			OMCoderTypeChildrenCollectingVisitor childVisitor = new OMCoderTypeChildrenCollectingVisitor(childInfo);
			type.getChild().accept(childVisitor);
		}
	}

	@Override
	public void visit(EnumASN1Type type) {
		OMCoderNodeInformation childInfo = new OMCoderNodeInformation();
		childInfo.setAsn1Type(type);
		childInfo.setParent(getNodeInformation());
		getNodeInformation().getChildren().add(childInfo);
	}

	@Override
	public void visit(ReferencedASN1Type type) {
		OMCoderNodeInformation childInfo = new OMCoderNodeInformation();
		childInfo.setAsn1Type(type);
		childInfo.setParent(getNodeInformation());
		getNodeInformation().getChildren().add(childInfo);
	}

	@Override
	public void visit(SimpleASN1Type type) {
		OMCoderNodeInformation childInfo = new OMCoderNodeInformation();
		childInfo.setAsn1Type(type);
		childInfo.setParent(getNodeInformation());
		getNodeInformation().getChildren().add(childInfo);
	}
}