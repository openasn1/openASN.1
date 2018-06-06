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
package com.github.openasn1.compiler.stages.asn1ast.tagging;

import org.apache.log4j.Logger;
import com.github.openasn1.common.TagClassEnum;
import com.github.openasn1.common.TagValue;
import com.github.openasn1.common.TaggingModeEnum;
import com.github.openasn1.compiler.Exceptions.CompilationStoppingException;
import com.github.openasn1.compiler.Exceptions.FeatureNotSupportedException;
import com.github.openasn1.compiler.astutils.ASN1ASTNodeInfos;
import com.github.openasn1.compiler.astutils.helpers.IdentifierMapHelper;
import com.github.openasn1.compiler.astutils.helpers.LogginHelper;
import com.github.openasn1.compiler.astutils.visitors.GetNextNestedNodeTokenVisitor;
import com.github.openasn1.parser.attributes.ClassNumberAttributes;
import com.github.openasn1.parser.attributes.TagAttributes;
import com.github.openasn1.parser.attributes.TaggedTypeAttributes;
import com.github.openasn1.parser.attributes.TypeAttributes;
import com.github.openasn1.parser.attributes.ValueAttributes;
import com.github.openasn1.parser.attributes.TaggedTypeAttributes.TagVisitor;
import com.github.openasn1.parser.generated.syntaxtree.ClassNumber;
import com.github.openasn1.parser.generated.syntaxtree.ModuleDefinition;
import com.github.openasn1.parser.generated.syntaxtree.Node;
import com.github.openasn1.parser.generated.syntaxtree.NodeToken;
import com.github.openasn1.parser.generated.syntaxtree.Tag;
import com.github.openasn1.parser.generated.syntaxtree.TaggedType;
import com.github.openasn1.parser.generated.syntaxtree.Type;
import com.github.openasn1.parser.generated.syntaxtree.Value;
import com.github.openasn1.parser.generated.visitor.DepthFirstVisitor;


/**
 *
 * This class assings each Type of a TaggedType Notation its Tag and Tagging
 * Environment. The Transformation for the automatic tagging are NOT applied
 * here. It is assumend they were applied prior to invoking this Visitor.
 * <p>
 * This Visitor uses NodeInformationAttributes with the name Tag, and with the
 * Java class TagValue
 *
 * It is assumed, that the AST is already annotated with the "IdentifierMap".
 *
 * @see com.github.openasn1.common.TagValue
 *
 * @author Clayton Hoss
 */
public class TaggingEnvironmentMappingVisitor extends DepthFirstVisitor {

    private static Logger LOGGER = Logger.getLogger("TaggingEnvironmentMappingVisitor");

    private TaggingModeEnum defaultTaggingMode = TaggingModeEnum.EXPLICIT;

    private ASN1ASTNodeInfos nodeinfos;

    private boolean isImplicitTaggingModeAllowed(TaggedType n) {
        Type type = TaggedTypeAttributes.getType(n);

        type = IdentifierMapHelper.getTypeFromDefinedTypeRecursive(type, getNodeinfos());

        if (TypeAttributes.isChoiceType(type)) {
            return false;
        }

        return true;
    }

    public TaggingEnvironmentMappingVisitor(TaggingModeEnum defaultTaggingMode,
            ASN1ASTNodeInfos infos) {
        this.nodeinfos = infos;
        this.defaultTaggingMode = defaultTaggingMode;
    }

    /**
     * @see com.github.openasn1.parser.generated.visitor.DepthFirstVisitor#visit(com.github.openasn1.parser.generated.syntaxtree.TaggedType)
     */
    @Override
    public void visit(TaggedType n) {
        TaggingModeEnum currentTaggingMode;
        TagClassEnum currentTagClass;
        TagVisitor tv = TaggedTypeAttributes.getVisitor(n);
        currentTaggingMode = getTaggingModeFromVisitor(tv, n);
        currentTagClass = getTaggingClassFromTag(tv.getTag());
        int tagNumber = getTagNumberFromTagClassNumber(tv.getTag().classNumber);
        TagValue tagvalue = new TagValue(currentTaggingMode, currentTagClass, tagNumber);
        LOGGER.debug("Adding Tag Info " + tagvalue + " at line "
                + LogginHelper.getCurrentLine(n));
        getNodeinfos().insertInfoIntoNode(n, "Tag", tagvalue);
        Node originalType = n.getParent().getParent().getParent().getParent().getParent().getParent();
        getNodeinfos().insertInfoIntoNode(originalType, "Tag", tagvalue);
        super.visit(n);
    }

    /**
     * @param classNumber
     * @return
     */
    private int getTagNumberFromTagClassNumber(ClassNumber classNumber) {
        if (ClassNumberAttributes.isNumber(classNumber)) {
            return ClassNumberAttributes.getNumber(classNumber);
        }

        if (ClassNumberAttributes.isValueReference(classNumber)) {
            Value val = IdentifierMapHelper.getValueFromDefinedValueRecursive(
                    ClassNumberAttributes.getDefinedValue(classNumber), getNodeinfos());
            Integer i = ValueAttributes.getValueAsInteger(val);
            if (i == null) {
                throw new CompilationStoppingException(
                        "Illegal Referenced Tag Encountered");
            }
            return i;
        }

        if (ClassNumberAttributes.isExternalValueReference(classNumber)) {
            LOGGER.error("Feature: ExternalValueReference is not supported "
                    + ClassNumberAttributes.getSymbol(classNumber));
            throw new FeatureNotSupportedException(
                    "External Value Reference is not Supported");
        }
        // TODO Auto-generated method stub
        return 0;
    }

    /**
     * @param tag
     */
    private TagClassEnum getTaggingClassFromTag(Tag tag) {
        if (TagAttributes.isApplicationClass(tag)) {
            return TagClassEnum.APPLICATION;
        }
        if (TagAttributes.isContextSpecificClass(tag)) {
            return TagClassEnum.CONTEXT_SPECIFIC;
        }
        if (TagAttributes.isUniversalClass(tag)) {
            return TagClassEnum.UNIVERSAL;
        }
        if (TagAttributes.isPrivateClass(tag)) {
            return TagClassEnum.PRIVATE;
        }
        throw new CompilationStoppingException("Invalid Tag Class Encountered");
    }

    /**
     * @param tv
     */
    private TaggingModeEnum getTaggingModeFromVisitor(TagVisitor tv, TaggedType n) {
        if (tv.isTaggingModeManuallySet()) {
            if (tv.isManualyExplicit()) {
                return TaggingModeEnum.EXPLICIT;
            }
            if (tv.isManualyImplicit()) {
                if (!isImplicitTaggingModeAllowed(n)) {
                    GetNextNestedNodeTokenVisitor vis = new GetNextNestedNodeTokenVisitor();
                    n.accept(vis);
                    NodeToken nt = vis.getNodeToken();
                    throw new CompilationStoppingException(
                            "Item cannot be tagged implicit " + nt.beginLine + " "
                                    + nt.beginColumn);
                }
                return TaggingModeEnum.IMPLICIT;
            }
            throw new CompilationStoppingException("Error with Manual set tagging mode");
        }
        if (!isImplicitTaggingModeAllowed(n)) {
            return TaggingModeEnum.EXPLICIT;
        }

        return getDefaultTaggingMode();
    }

    /**
     * @see com.github.openasn1.parser.generated.visitor.DepthFirstVisitor#visit(com.github.openasn1.parser.generated.syntaxtree.ModuleDefinition)
     */
    @Override
    public void visit(ModuleDefinition n) {
        LOGGER.debug("Visiting Module to create TaggingEnvironmentMap "
                + n.moduleIdentifier.modulereference.nodeToken.tokenImage);
        super.visit(n);
    }

    /**
     * @return the defaultTaggingMode
     */
    private TaggingModeEnum getDefaultTaggingMode() {
        return this.defaultTaggingMode;
    }

    /**
     * @return the nodeinfos
     */
    private ASN1ASTNodeInfos getNodeinfos() {
        return this.nodeinfos;
    }
}
