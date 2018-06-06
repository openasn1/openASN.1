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

/**
 * @author Clayton Hoss
 * 
 */
public enum FeatureList { 
	/** Complexity Features **/
	Files,
	ParsedFiles,
	Assignments,
	
	/** interesting ASN.1 Language Features **/
	ValueAssignment,
	ComplexValueAssignment,
	ValueSetTypeAssignment,
	XMLValue, 
	ModuleHeaderOID,
	ModuleTaggingMode, 
	ImplicitMode,
	ExplicitMode,
	AutomaticMode,
	ImpliedExtensibility, 
	Imports, 
	AbsoluteReference, 
	Extension, 
	Exception, 
	SelectionType,
        ComponentsOfType,
	Tag, 
	ImplicitTag,
	ExplicitTag,
	Constraint,
	
	/** ASN.1 Constraints **/ 
	ValueRangeConstraint, 
	ContainedSubtypeConstraint, 
	PermittedAlphabetConstraint, 
	SizeConstraint, 
	TypeConstraint, 
	InnerTypeConstraints, 
	SingleValueContraint, 
	PatternConstraint,
	PresenceContraint,
        ExternalTypeReference,
        ExternalValueReference,

	/** ASN.1 Data-Types **/
	Default,
	Optional,
	BooleanType,
	NullType,
	IntegerType,
	RealType,
	EnumeratedType,
	ObjectIdentifyerType,
	RelativeOIDType,
	ChoiceType,
	SequenceOfType,
	SequenceType,
	SetOfType,
	SetType,
	EmbeddedPDVType,
	ExternalType,
	UsefulType,
		
	/** ASN.1 String-Data-Types **/	
	BitStringType,
	OctetStringType,
	CharacterStringType,
	UnrestrictedCharacterStringType,
	RestrictedCharacterStringType,
	BMPString,
	GeneralString,
	GraphicString,
	IA5String,
	Iso64String,
	NumericString,
	PrintableString,
	TeletexString,
	T61String,
	UniversalString,
	UTF8String,
	VideotexString,
	VisibleString, 
	
	/** Manually checked features **/
	InformationObjectClass,
	InformationObject,

	UserConstraintsConstrainedBy,
	ContentsConstraintEncodedBy,
	ContentsConstraintContaining,
	ContentsConstraint,

	Parameterization,
	ClassTypeIdentifyer,
}
