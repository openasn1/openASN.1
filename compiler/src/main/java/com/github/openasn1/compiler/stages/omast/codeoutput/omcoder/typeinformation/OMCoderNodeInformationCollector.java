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

import org.apache.log4j.Logger;
import com.github.openasn1.common.TagValue;
import com.github.openasn1.compiler.astutils.OMAndASN1ASTStorage;
import com.github.openasn1.compiler.astutils.visitors.GetConstraintForTypeVisitor;
import com.github.openasn1.compiler.astutils.visitors.GetTagListForTypeVisitor;
import com.github.openasn1.compiler.omast.AbstractASN1Type;
import com.github.openasn1.compiler.omast.AbstractOMVisitor;
import com.github.openasn1.parser.attributes.TypeAttributes;
import com.github.openasn1.parser.generated.syntaxtree.Type;

/**
 * @author Clayton Hoss
 *
 */
public class OMCoderNodeInformationCollector extends AbstractOMVisitor {
	private static Logger LOGGER = Logger.getLogger(OMCoderNodeInformationCollector.class.getSimpleName());

	private OMCoderNodeInformation nodeInformation = new OMCoderNodeInformation();

	private OMAndASN1ASTStorage storage;

	public OMCoderNodeInformationCollector(OMAndASN1ASTStorage storage) {
		this.storage = storage;
	}

	private OMAndASN1ASTStorage getStorage() {
		return this.storage;
	}

	public OMCoderNodeInformation getNodeInformation() {
		return this.nodeInformation;
	}

	public void collectNodeInformation(AbstractASN1Type type) {
		getNodeInformation().setAsn1Type(type);

		OMCoderBaseInformationCollectingVisitor baseInfoVisitor = new OMCoderBaseInformationCollectingVisitor(getNodeInformation());
		type.accept(baseInfoVisitor);

		OMCoderTypeChildrenCollectingVisitor childrenVisitor = new OMCoderTypeChildrenCollectingVisitor(getNodeInformation());
		type.accept(childrenVisitor);

		collectInfo(getNodeInformation());
	}

	private void collectInfo(OMCoderNodeInformation nodeInformation) {
		for (OMCoderNodeInformation childInfo : nodeInformation.getChildren()) {
			collectNodeTags(childInfo);
			collectNodeConstraint(childInfo);
			collectNodeExtensibility(childInfo);
			collectNodeIsInExtension(childInfo);
			collectNodeOptionability(childInfo);

			collectInfo(childInfo);
		}
	}

	private void collectNodeTags(OMCoderNodeInformation childInfo) {
		GetTagListForTypeVisitor tagListVisitor = new GetTagListForTypeVisitor(getStorage().getAstNodeInfos());
		childInfo.getAsn1Type().getCorrespondingASN1Node().accept(tagListVisitor);

		for (TagValue tagValue : tagListVisitor.getTaglist()) {
			childInfo.getTagList().add(tagValue);
		}
	}

	private void collectNodeConstraint(OMCoderNodeInformation childInfo) {
		LOGGER.debug("Processing Constraint for: " + childInfo.getAsn1Type().getName());
		GetConstraintForTypeVisitor constraintVisitor = new GetConstraintForTypeVisitor(getStorage().getAstNodeInfos());
		childInfo.getAsn1Type().getCorrespondingASN1Node().accept(constraintVisitor, null);

		childInfo.setConstraint(constraintVisitor.getConstraint());
	}

	private void collectNodeExtensibility(OMCoderNodeInformation childInfo) {
		Type type = (Type) childInfo.getAsn1Type().getCorrespondingASN1Node();
		boolean isExtensible = TypeAttributes.isTypeExtensible(type, getStorage().getAstNodeInfos());
		childInfo.setExtensible(isExtensible);
	}

	private void collectNodeIsInExtension(OMCoderNodeInformation childInfo) {
		Type type = (Type) childInfo.getAsn1Type().getCorrespondingASN1Node();
		boolean isInExtension = TypeAttributes.isInExtension(type);
		childInfo.setInExtension(isInExtension);
	}

	private void collectNodeOptionability(OMCoderNodeInformation childInfo) {
		Type type = (Type) childInfo.getAsn1Type().getCorrespondingASN1Node();
		boolean isOptional = TypeAttributes.isInSetOrSequenceAndOptional(type);
		childInfo.setOptional(isOptional);
	}
}