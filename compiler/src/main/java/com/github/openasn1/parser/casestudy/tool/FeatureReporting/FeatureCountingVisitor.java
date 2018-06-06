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
package com.github.openasn1.parser.casestudy.tool.FeatureReporting;

import com.github.openasn1.parser.attributes.ComponentTypeAttributes;
import com.github.openasn1.parser.attributes.RestrictedCharacterStringTypeAttributes;
import com.github.openasn1.parser.attributes.TagDefaultAttributes;
import com.github.openasn1.parser.attributes.TaggedTypeAttributes;
import com.github.openasn1.parser.generated.syntaxtree.AbsoluteReference;
import com.github.openasn1.parser.generated.syntaxtree.Assignment;
import com.github.openasn1.parser.generated.syntaxtree.BitStringType;
import com.github.openasn1.parser.generated.syntaxtree.BooleanType;
import com.github.openasn1.parser.generated.syntaxtree.CharacterStringType;
import com.github.openasn1.parser.generated.syntaxtree.ChoiceType;
import com.github.openasn1.parser.generated.syntaxtree.ChoiceValue;
import com.github.openasn1.parser.generated.syntaxtree.ComponentType;
import com.github.openasn1.parser.generated.syntaxtree.Constraint;
import com.github.openasn1.parser.generated.syntaxtree.ContainedSubtype;
import com.github.openasn1.parser.generated.syntaxtree.EmbeddedPDVType;
import com.github.openasn1.parser.generated.syntaxtree.EmbeddedPDVValue;
import com.github.openasn1.parser.generated.syntaxtree.EnumeratedType;
import com.github.openasn1.parser.generated.syntaxtree.ExceptionSpec;
import com.github.openasn1.parser.generated.syntaxtree.ExtensionAndException;
import com.github.openasn1.parser.generated.syntaxtree.ExtensionDefault;
import com.github.openasn1.parser.generated.syntaxtree.ExternalType;
import com.github.openasn1.parser.generated.syntaxtree.ExternalTypeReference;
import com.github.openasn1.parser.generated.syntaxtree.ExternalValue;
import com.github.openasn1.parser.generated.syntaxtree.ExternalValueReference;
import com.github.openasn1.parser.generated.syntaxtree.Imports;
import com.github.openasn1.parser.generated.syntaxtree.InnerTypeConstraints;
import com.github.openasn1.parser.generated.syntaxtree.IntegerType;
import com.github.openasn1.parser.generated.syntaxtree.ModuleDefinition;
import com.github.openasn1.parser.generated.syntaxtree.NullType;
import com.github.openasn1.parser.generated.syntaxtree.ObjectIdentifierType;
import com.github.openasn1.parser.generated.syntaxtree.OctetStringType;
import com.github.openasn1.parser.generated.syntaxtree.PatternConstraint;
import com.github.openasn1.parser.generated.syntaxtree.PermittedAlphabet;
import com.github.openasn1.parser.generated.syntaxtree.PresenceConstraint;
import com.github.openasn1.parser.generated.syntaxtree.RealType;
import com.github.openasn1.parser.generated.syntaxtree.RelativeOIDType;
import com.github.openasn1.parser.generated.syntaxtree.RestrictedCharacterStringType;
import com.github.openasn1.parser.generated.syntaxtree.SelectionType;
import com.github.openasn1.parser.generated.syntaxtree.SequenceOfType;
import com.github.openasn1.parser.generated.syntaxtree.SequenceOfValue;
import com.github.openasn1.parser.generated.syntaxtree.SequenceType;
import com.github.openasn1.parser.generated.syntaxtree.SequenceValue;
import com.github.openasn1.parser.generated.syntaxtree.SetOfType;
import com.github.openasn1.parser.generated.syntaxtree.SetOfValue;
import com.github.openasn1.parser.generated.syntaxtree.SetType;
import com.github.openasn1.parser.generated.syntaxtree.SetValue;
import com.github.openasn1.parser.generated.syntaxtree.SingleValue;
import com.github.openasn1.parser.generated.syntaxtree.SizeConstraint;
import com.github.openasn1.parser.generated.syntaxtree.Tag;
import com.github.openasn1.parser.generated.syntaxtree.TagDefault;
import com.github.openasn1.parser.generated.syntaxtree.TaggedType;
import com.github.openasn1.parser.generated.syntaxtree.TypeConstraint;
import com.github.openasn1.parser.generated.syntaxtree.UnrestrictedCharacterStringType;
import com.github.openasn1.parser.generated.syntaxtree.UnrestrictedCharacterStringValue;
import com.github.openasn1.parser.generated.syntaxtree.UsefulType;
import com.github.openasn1.parser.generated.syntaxtree.ValueAssignment;
import com.github.openasn1.parser.generated.syntaxtree.ValueRange;
import com.github.openasn1.parser.generated.syntaxtree.ValueSetTypeAssignment;
import com.github.openasn1.parser.generated.syntaxtree.XMLValue;
import com.github.openasn1.parser.generated.visitor.DepthFirstVisitor;

/**
 * @author Clayton Hoss
 * 
 */
public class FeatureCountingVisitor extends DepthFirstVisitor {

    /**
     * @see com.github.openasn1.parser.generated.visitor.DepthFirstVisitor#visit(com.github.openasn1.parser.generated.syntaxtree.ValueSetTypeAssignment)
     */
    @Override
    public void visit(ValueSetTypeAssignment n) {
        getFeatureList().incFeatureCount(FeatureList.ValueSetTypeAssignment);
        super.visit(n);
    }

    /**
     * @see com.github.openasn1.parser.generated.visitor.DepthFirstVisitor#visit(com.github.openasn1.parser.generated.syntaxtree.ExternalTypeReference)
     */
    @Override
    public void visit(ExternalTypeReference n) {
        getFeatureList().incFeatureCount(FeatureList.ExternalTypeReference);
        super.visit(n);
    }

    /**
     * @see com.github.openasn1.parser.generated.visitor.DepthFirstVisitor#visit(com.github.openasn1.parser.generated.syntaxtree.ExternalValueReference)
     */
    @Override
    public void visit(ExternalValueReference n) {
        getFeatureList().incFeatureCount(FeatureList.ExternalValueReference);
        super.visit(n);
    }

    /**
     * 
     * @see com.github.openasn1.parser.generated.visitor.DepthFirstVisitor#visit(com.github.openasn1.parser.generated.syntaxtree.PresenceConstraint)
     */
    @Override
    public void visit(PresenceConstraint n) {
        getFeatureList().incFeatureCount(FeatureList.PresenceContraint);
        super.visit(n);
    }

    private FeatureListHandler featureList = new FeatureListHandler();

    private String moduleName;

    /**
     * @see com.github.openasn1.parser.generated.visitor.DepthFirstVisitor#visit(com.github.openasn1.parser.generated.syntaxtree.ValueAssignment)
     */
    @Override
    public void visit(ValueAssignment n) {
        getFeatureList().incFeatureCount(FeatureList.ValueAssignment);
        super.visit(n);
    }

    /**
     * @see com.github.openasn1.parser.generated.visitor.DepthFirstVisitor#visit(com.github.openasn1.parser.generated.syntaxtree.XMLValue)
     */
    @Override
    public void visit(XMLValue n) {
        getFeatureList().incFeatureCount(FeatureList.XMLValue);
        super.visit(n);
    }

    /**
     * @see com.github.openasn1.parser.generated.visitor.DepthFirstVisitor#visit(com.github.openasn1.parser.generated.syntaxtree.ExceptionSpec)
     */
    @Override
    public void visit(ExceptionSpec n) {
        getFeatureList().incFeatureCount(FeatureList.Exception);
        super.visit(n);
    }

    /**
     * @see com.github.openasn1.parser.generated.visitor.DepthFirstVisitor#visit(com.github.openasn1.parser.generated.syntaxtree.ExtensionAndException)
     */
    @Override
    public void visit(ExtensionAndException n) {
        getFeatureList().incFeatureCount(FeatureList.Extension);
        super.visit(n);
    }

    /**
     * @see com.github.openasn1.parser.generated.visitor.DepthFirstVisitor#visit(com.github.openasn1.parser.generated.syntaxtree.TagDefault)
     */
    @Override
    public void visit(TagDefault n) {
        getFeatureList().incFeatureCount(FeatureList.ModuleTaggingMode);
        if (TagDefaultAttributes.isImplicitTaggingMode(n)) {
            getFeatureList().incFeatureCount(FeatureList.ImplicitMode);
        }
        if (TagDefaultAttributes.isExplicitTaggingMode(n)) {
            getFeatureList().incFeatureCount(FeatureList.ExplicitMode);
        }
        if (TagDefaultAttributes.isAutomaticTaggingMode(n)) {
            getFeatureList().incFeatureCount(FeatureList.AutomaticMode);
        }
        super.visit(n);
    }

    /**
     * 
     * @see com.github.openasn1.parser.generated.visitor.DepthFirstVisitor#visit(com.github.openasn1.parser.generated.syntaxtree.ExtensionDefault)
     */
    @Override
    public void visit(ExtensionDefault n) {
        getFeatureList().incFeatureCount(FeatureList.ImpliedExtensibility);
        super.visit(n);
    }

    /**
     * @see com.github.openasn1.parser.generated.visitor.DepthFirstVisitor#visit(com.github.openasn1.parser.generated.syntaxtree.ModuleDefinition)
     */
    @Override
    public void visit(ModuleDefinition n) {
        super.visit(n);
        setModuleName(n.moduleIdentifier.modulereference.nodeToken.tokenImage);
        if (n.moduleIdentifier.nodeOptional.present()) {
            getFeatureList().incFeatureCount(FeatureList.ModuleHeaderOID);
        }
    }

    /**
     * @see com.github.openasn1.parser.generated.visitor.DepthFirstVisitor#visit(com.github.openasn1.parser.generated.syntaxtree.SelectionType)
     */
    @Override
    public void visit(SelectionType n) {
        getFeatureList().incFeatureCount(FeatureList.SelectionType);
        super.visit(n);
    }

    /**
     * @see com.github.openasn1.parser.generated.visitor.DepthFirstVisitor#visit(com.github.openasn1.parser.generated.syntaxtree.Imports)
     */
    @Override
    public void visit(Imports n) {
        getFeatureList().incFeatureCount(FeatureList.Imports);
        super.visit(n);
    }

    /**
     * @see com.github.openasn1.parser.generated.visitor.DepthFirstVisitor#visit(com.github.openasn1.parser.generated.syntaxtree.Constraint)
     */
    @Override
    public void visit(Constraint n) {
        getFeatureList().incFeatureCount(FeatureList.Constraint);
        super.visit(n);
    }

    /**
     * @see com.github.openasn1.parser.generated.visitor.DepthFirstVisitor#visit(com.github.openasn1.parser.generated.syntaxtree.Tag)
     */
    @Override
    public void visit(Tag n) {
        getFeatureList().incFeatureCount(FeatureList.Tag);
        super.visit(n);
    }

    /**
     * @see com.github.openasn1.parser.generated.visitor.DepthFirstVisitor#visit(com.github.openasn1.parser.generated.syntaxtree.TaggedType)
     */
    @Override
    public void visit(TaggedType n) {
        if (TaggedTypeAttributes.isManualExplicitTag(n)) {
            getFeatureList().incFeatureCount(FeatureList.ExplicitTag);
        }
        if (TaggedTypeAttributes.isManualImplicitTag(n)) {
            getFeatureList().incFeatureCount(FeatureList.ImplicitTag);
        }
        super.visit(n);
    }

    /**
     * @see com.github.openasn1.parser.generated.visitor.DepthFirstVisitor#visit(com.github.openasn1.parser.generated.syntaxtree.AbsoluteReference)
     */
    @Override
    public void visit(AbsoluteReference n) {
        getFeatureList().incFeatureCount(FeatureList.AbsoluteReference);
        super.visit(n);
    }

    /**
     * @return the featureMatrix
     */
    public FeatureListHandler getFeatureList() {
        return this.featureList;
    }

    /**
     * @return the moduleName
     */
    public String getModuleName() {
        return this.moduleName;
    }

    /**
     * @param moduleName
     *            the moduleName to set
     */
    private void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.github.openasn1.parser.generated.visitor.DepthFirstVisitor#visit(com.github.openasn1.parser.generated.syntaxtree.ContainedSubtype)
     */
    @Override
    public void visit(ContainedSubtype n) {
        getFeatureList().incFeatureCount(FeatureList.ContainedSubtypeConstraint);
        super.visit(n);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.github.openasn1.parser.generated.visitor.DepthFirstVisitor#visit(com.github.openasn1.parser.generated.syntaxtree.InnerTypeConstraints)
     */
    @Override
    public void visit(InnerTypeConstraints n) {
        getFeatureList().incFeatureCount(FeatureList.InnerTypeConstraints);
        super.visit(n);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.github.openasn1.parser.generated.visitor.DepthFirstVisitor#visit(com.github.openasn1.parser.generated.syntaxtree.PatternConstraint)
     */
    @Override
    public void visit(PatternConstraint n) {
        getFeatureList().incFeatureCount(FeatureList.PatternConstraint);
        super.visit(n);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.github.openasn1.parser.generated.visitor.DepthFirstVisitor#visit(com.github.openasn1.parser.generated.syntaxtree.PermittedAlphabet)
     */
    @Override
    public void visit(PermittedAlphabet n) {
        getFeatureList().incFeatureCount(FeatureList.PermittedAlphabetConstraint);
        super.visit(n);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.github.openasn1.parser.generated.visitor.DepthFirstVisitor#visit(com.github.openasn1.parser.generated.syntaxtree.SingleValue)
     */
    @Override
    public void visit(SingleValue n) {
        getFeatureList().incFeatureCount(FeatureList.SingleValueContraint);
        super.visit(n);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.github.openasn1.parser.generated.visitor.DepthFirstVisitor#visit(com.github.openasn1.parser.generated.syntaxtree.SizeConstraint)
     */
    @Override
    public void visit(SizeConstraint n) {
        getFeatureList().incFeatureCount(FeatureList.SizeConstraint);
        super.visit(n);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.github.openasn1.parser.generated.visitor.DepthFirstVisitor#visit(com.github.openasn1.parser.generated.syntaxtree.TypeConstraint)
     */
    @Override
    public void visit(TypeConstraint n) {
        getFeatureList().incFeatureCount(FeatureList.TypeConstraint);
        super.visit(n);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.github.openasn1.parser.generated.visitor.DepthFirstVisitor#visit(com.github.openasn1.parser.generated.syntaxtree.ValueRange)
     */
    @Override
    public void visit(ValueRange n) {
        getFeatureList().incFeatureCount(FeatureList.ValueRangeConstraint);
        super.visit(n);
    }

    /**
     * @see com.github.openasn1.parser.generated.visitor.DepthFirstVisitor#visit(com.github.openasn1.parser.generated.syntaxtree.BitStringType)
     */
    @Override
    public void visit(BitStringType n) {
        getFeatureList().incFeatureCount(FeatureList.BitStringType);
        super.visit(n);
    }

    /**
     * @see com.github.openasn1.parser.generated.visitor.DepthFirstVisitor#visit(com.github.openasn1.parser.generated.syntaxtree.BooleanType)
     */
    @Override
    public void visit(BooleanType n) {
        getFeatureList().incFeatureCount(FeatureList.BooleanType);
        super.visit(n);
    }

    /**
     * @see com.github.openasn1.parser.generated.visitor.DepthFirstVisitor#visit(com.github.openasn1.parser.generated.syntaxtree.CharacterStringType)
     */
    @Override
    public void visit(CharacterStringType n) {
        getFeatureList().incFeatureCount(FeatureList.CharacterStringType);
        super.visit(n);
    }

    /**
     * @see com.github.openasn1.parser.generated.visitor.DepthFirstVisitor#visit(com.github.openasn1.parser.generated.syntaxtree.ChoiceType)
     */
    @Override
    public void visit(ChoiceType n) {
        getFeatureList().incFeatureCount(FeatureList.ChoiceType);
        super.visit(n);
    }

    /**
     * @see com.github.openasn1.parser.generated.visitor.DepthFirstVisitor#visit(com.github.openasn1.parser.generated.syntaxtree.EmbeddedPDVType)
     */
    @Override
    public void visit(EmbeddedPDVType n) {
        getFeatureList().incFeatureCount(FeatureList.EmbeddedPDVType);
        super.visit(n);
    }

    /**
     * @see com.github.openasn1.parser.generated.visitor.DepthFirstVisitor#visit(com.github.openasn1.parser.generated.syntaxtree.EnumeratedType)
     */
    @Override
    public void visit(EnumeratedType n) {
        getFeatureList().incFeatureCount(FeatureList.EnumeratedType);
        super.visit(n);
    }

    /**
     * @see com.github.openasn1.parser.generated.visitor.DepthFirstVisitor#visit(com.github.openasn1.parser.generated.syntaxtree.ExternalType)
     */
    @Override
    public void visit(ExternalType n) {
        getFeatureList().incFeatureCount(FeatureList.ExternalType);
        super.visit(n);
    }

    /**
     * @see com.github.openasn1.parser.generated.visitor.DepthFirstVisitor#visit(com.github.openasn1.parser.generated.syntaxtree.IntegerType)
     */
    @Override
    public void visit(IntegerType n) {
        getFeatureList().incFeatureCount(FeatureList.IntegerType);
        super.visit(n);
    }

    /**
     * @see com.github.openasn1.parser.generated.visitor.DepthFirstVisitor#visit(com.github.openasn1.parser.generated.syntaxtree.NullType)
     */
    @Override
    public void visit(NullType n) {
        getFeatureList().incFeatureCount(FeatureList.NullType);
        super.visit(n);
    }

    /**
     * @see com.github.openasn1.parser.generated.visitor.DepthFirstVisitor#visit(com.github.openasn1.parser.generated.syntaxtree.ObjectIdentifierType)
     */
    @Override
    public void visit(ObjectIdentifierType n) {
        getFeatureList().incFeatureCount(FeatureList.ObjectIdentifyerType);
        super.visit(n);
    }

    /**
     * @see com.github.openasn1.parser.generated.visitor.DepthFirstVisitor#visit(com.github.openasn1.parser.generated.syntaxtree.OctetStringType)
     */
    @Override
    public void visit(OctetStringType n) {
        getFeatureList().incFeatureCount(FeatureList.OctetStringType);
        super.visit(n);
    }

    /**
     * @see com.github.openasn1.parser.generated.visitor.DepthFirstVisitor#visit(com.github.openasn1.parser.generated.syntaxtree.RealType)
     */
    @Override
    public void visit(RealType n) {
        getFeatureList().incFeatureCount(FeatureList.RealType);
        super.visit(n);
    }

    /**
     * @see com.github.openasn1.parser.generated.visitor.DepthFirstVisitor#visit(com.github.openasn1.parser.generated.syntaxtree.RelativeOIDType)
     */
    @Override
    public void visit(RelativeOIDType n) {
        getFeatureList().incFeatureCount(FeatureList.RelativeOIDType);
        super.visit(n);
    }

    /**
     * @see com.github.openasn1.parser.generated.visitor.DepthFirstVisitor#visit(com.github.openasn1.parser.generated.syntaxtree.RestrictedCharacterStringType)
     */
    @Override
    public void visit(RestrictedCharacterStringType n) {
        getFeatureList().incFeatureCount(FeatureList.RestrictedCharacterStringType);

        if (RestrictedCharacterStringTypeAttributes.isBMPString(n)) {
            getFeatureList().incFeatureCount(FeatureList.BMPString);
        }
        if (RestrictedCharacterStringTypeAttributes.isGeneralString(n)) {
            getFeatureList().incFeatureCount(FeatureList.GeneralString);
        }
        if (RestrictedCharacterStringTypeAttributes.isGraphicString(n)) {
            getFeatureList().incFeatureCount(FeatureList.GraphicString);
        }
        if (RestrictedCharacterStringTypeAttributes.isIA5String(n)) {
            getFeatureList().incFeatureCount(FeatureList.IA5String);
        }
        if (RestrictedCharacterStringTypeAttributes.isIso64String(n)) {
            getFeatureList().incFeatureCount(FeatureList.Iso64String);
        }
        if (RestrictedCharacterStringTypeAttributes.isNumericString(n)) {
            getFeatureList().incFeatureCount(FeatureList.NumericString);
        }
        if (RestrictedCharacterStringTypeAttributes.isPrintableString(n)) {
            getFeatureList().incFeatureCount(FeatureList.PrintableString);
        }
        if (RestrictedCharacterStringTypeAttributes.isT61String(n)) {
            getFeatureList().incFeatureCount(FeatureList.T61String);
        }
        if (RestrictedCharacterStringTypeAttributes.isTeletexString(n)) {
            getFeatureList().incFeatureCount(FeatureList.TeletexString);
        }
        if (RestrictedCharacterStringTypeAttributes.isUniversalString(n)) {
            getFeatureList().incFeatureCount(FeatureList.UniversalString);
        }
        if (RestrictedCharacterStringTypeAttributes.isUTF8String(n)) {
            getFeatureList().incFeatureCount(FeatureList.UTF8String);
        }
        if (RestrictedCharacterStringTypeAttributes.isVideotexString(n)) {
            getFeatureList().incFeatureCount(FeatureList.VideotexString);
        }
        if (RestrictedCharacterStringTypeAttributes.isVisibleString(n)) {
            getFeatureList().incFeatureCount(FeatureList.VisibleString);
        }

        super.visit(n);
    }

    /**
     * @see com.github.openasn1.parser.generated.visitor.DepthFirstVisitor#visit(com.github.openasn1.parser.generated.syntaxtree.SequenceOfType)
     */
    @Override
    public void visit(SequenceOfType n) {
        getFeatureList().incFeatureCount(FeatureList.SequenceOfType);
        super.visit(n);
    }

    /**
     * @see com.github.openasn1.parser.generated.visitor.DepthFirstVisitor#visit(com.github.openasn1.parser.generated.syntaxtree.SequenceType)
     */
    @Override
    public void visit(SequenceType n) {
        getFeatureList().incFeatureCount(FeatureList.SequenceType);
        super.visit(n);
    }

    /**
     * @see com.github.openasn1.parser.generated.visitor.DepthFirstVisitor#visit(com.github.openasn1.parser.generated.syntaxtree.SetOfType)
     */
    @Override
    public void visit(SetOfType n) {
        getFeatureList().incFeatureCount(FeatureList.SetOfType);
        super.visit(n);
    }

    /**
     * @see com.github.openasn1.parser.generated.visitor.DepthFirstVisitor#visit(com.github.openasn1.parser.generated.syntaxtree.SetType)
     */
    @Override
    public void visit(SetType n) {
        getFeatureList().incFeatureCount(FeatureList.SetType);
        super.visit(n);
    }

    /**
     * @see com.github.openasn1.parser.generated.visitor.DepthFirstVisitor#visit(com.github.openasn1.parser.generated.syntaxtree.UnrestrictedCharacterStringType)
     */
    @Override
    public void visit(UnrestrictedCharacterStringType n) {
        getFeatureList().incFeatureCount(FeatureList.UnrestrictedCharacterStringType);
        super.visit(n);
    }

    /**
     * @see com.github.openasn1.parser.generated.visitor.DepthFirstVisitor#visit(com.github.openasn1.parser.generated.syntaxtree.ComponentType)
     */
    @Override
    public void visit(ComponentType n) {
        if (ComponentTypeAttributes.hasDefaultValue(n)) {
            getFeatureList().incFeatureCount(FeatureList.Default);
        }
        if (ComponentTypeAttributes.isOptional(n)) {
            getFeatureList().incFeatureCount(FeatureList.Optional);
        }
        if(ComponentTypeAttributes.isComponentsOf(n)) {
            getFeatureList().incFeatureCount(FeatureList.ComponentsOfType);
        }
        super.visit(n);
    }

    /**
     * @see com.github.openasn1.parser.generated.visitor.DepthFirstVisitor#visit(com.github.openasn1.parser.generated.syntaxtree.UsefulType)
     */
    @Override
    public void visit(UsefulType n) {
        getFeatureList().incFeatureCount(FeatureList.UsefulType);
        super.visit(n);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.github.openasn1.parser.generated.visitor.DepthFirstVisitor#visit(com.github.openasn1.parser.generated.syntaxtree.ChoiceValue)
     */
    @Override
    public void visit(ChoiceValue n) {
        getFeatureList().incFeatureCount(FeatureList.ComplexValueAssignment);
        super.visit(n);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.github.openasn1.parser.generated.visitor.DepthFirstVisitor#visit(com.github.openasn1.parser.generated.syntaxtree.SequenceOfValue)
     */
    @Override
    public void visit(SequenceOfValue n) {
        getFeatureList().incFeatureCount(FeatureList.ComplexValueAssignment);
        super.visit(n);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.github.openasn1.parser.generated.visitor.DepthFirstVisitor#visit(com.github.openasn1.parser.generated.syntaxtree.SequenceValue)
     */
    @Override
    public void visit(SequenceValue n) {
        getFeatureList().incFeatureCount(FeatureList.ComplexValueAssignment);
        super.visit(n);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.github.openasn1.parser.generated.visitor.DepthFirstVisitor#visit(com.github.openasn1.parser.generated.syntaxtree.SetOfValue)
     */
    @Override
    public void visit(SetOfValue n) {
        getFeatureList().incFeatureCount(FeatureList.ComplexValueAssignment);
        super.visit(n);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.github.openasn1.parser.generated.visitor.DepthFirstVisitor#visit(com.github.openasn1.parser.generated.syntaxtree.SetValue)
     */
    @Override
    public void visit(SetValue n) {
        getFeatureList().incFeatureCount(FeatureList.ComplexValueAssignment);
        super.visit(n);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.github.openasn1.parser.generated.visitor.DepthFirstVisitor#visit(com.github.openasn1.parser.generated.syntaxtree.EmbeddedPDVValue)
     */
    @Override
    public void visit(EmbeddedPDVValue n) {
        getFeatureList().incFeatureCount(FeatureList.ComplexValueAssignment);
        super.visit(n);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.github.openasn1.parser.generated.visitor.DepthFirstVisitor#visit(com.github.openasn1.parser.generated.syntaxtree.ExternalValue)
     */
    @Override
    public void visit(ExternalValue n) {
        getFeatureList().incFeatureCount(FeatureList.ComplexValueAssignment);
        super.visit(n);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.github.openasn1.parser.generated.visitor.DepthFirstVisitor#visit(com.github.openasn1.parser.generated.syntaxtree.UnrestrictedCharacterStringValue)
     */
    @Override
    public void visit(UnrestrictedCharacterStringValue n) {
        getFeatureList().incFeatureCount(FeatureList.ComplexValueAssignment);
        super.visit(n);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.github.openasn1.parser.generated.visitor.DepthFirstVisitor#visit(com.github.openasn1.parser.generated.syntaxtree.Assignment)
     */
    @Override
    public void visit(Assignment n) {
        getFeatureList().incFeatureCount(FeatureList.Assignments);
        super.visit(n);
    }

}