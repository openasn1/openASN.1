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

import java.util.Enumeration;

import com.github.openasn1.parser.generated.syntaxtree.*;
import com.github.openasn1.parser.generated.visitor.Visitor;


/**
 * @author Clayton Hoss
 *
 */
public class NoTraversalVisitor implements Visitor {

	public void visit(NodeList n) {
		for (Enumeration<Node> e = n.elements(); e.hasMoreElements();)
			e.nextElement().accept(this);
	}

	public void visit(NodeListOptional n) {
		if (n.present())
			for (Enumeration<Node> e = n.elements(); e.hasMoreElements();)
				e.nextElement().accept(this);
	}

	public void visit(NodeOptional n) {
		if (n.present())
			n.node.accept(this);
	}

	public void visit(NodeSequence n) {
		for (Enumeration<Node> e = n.elements(); e.hasMoreElements();)
			e.nextElement().accept(this);
	}

	public void visit(NodeToken n) {
	}

	public void visit(modulereference n) {

	}

	public void visit(valuereference n) {

	}

	public void visit(xmlasn1typename n) {

	}

	public void visit(NamedValueList n) {

	}

	public void visit(ModuleDefinition n) {

	}

	public void visit(ModuleIdentifier n) {

	}

	public void visit(DefinitiveIdentifier n) {

	}

	public void visit(DefinitiveObjIdComponentList n) {

	}

	public void visit(DefinitiveObjIdComponent n) {

	}

	public void visit(DefinitiveNumberForm n) {

	}

	public void visit(DefinitiveNameAndNumberForm n) {

	}

	public void visit(TagDefault n) {

	}

	public void visit(ExtensionDefault n) {

	}

	public void visit(ModuleBody n) {

	}

	public void visit(Exports n) {

	}

	public void visit(SymbolsExported n) {

	}

	public void visit(Imports n) {

	}

	public void visit(SymbolsImported n) {

	}

	public void visit(SymbolsFromModuleList n) {

	}

	public void visit(SymbolsFromModule n) {

	}

	public void visit(GlobalModuleReference n) {

	}

	public void visit(AssignedIdentifier n) {

	}

	public void visit(SymbolList n) {

	}

	public void visit(Symbol n) {

	}

	public void visit(Reference n) {

	}

	public void visit(AssignmentList n) {

	}

	public void visit(Assignment n) {

	}

	public void visit(DefinedType n) {

	}

	public void visit(ExternalTypeReference n) {

	}

	public void visit(NonParameterizedTypeName n) {

	}

	public void visit(DefinedValue n) {

	}

	public void visit(ExternalValueReference n) {

	}

	public void visit(AbsoluteReference n) {

	}

	public void visit(ItemSpec n) {

	}

	public void visit(ComponentId n) {

	}

	public void visit(TypeAssignment n) {

	}

	public void visit(ValueAssignment n) {

	}

	public void visit(XMLValueAssignment n) {

	}

	public void visit(XMLTypedValue n) {

	}

	public void visit(ValueSetTypeAssignment n) {

	}

	public void visit(ValueSet n) {

	}

	public void visit(Type n) {

	}

	public void visit(NormalConstrainedType n) {

	}

	public void visit(BuiltinType n) {

	}

	public void visit(NamedType n) {

	}

	public void visit(ReferencedType n) {

	}

	public void visit(Value n) {

	}

	public void visit(XMLValue n) {

	}

	public void visit(BuiltinValue n) {

	}

	public void visit(XMLBuiltinValue n) {

	}

	public void visit(ReferencedValue n) {

	}

	public void visit(NamedValue n) {

	}

	public void visit(XMLNamedValue n) {

	}

	public void visit(BooleanType n) {

	}

	public void visit(BooleanValue n) {

	}

	public void visit(XMLBooleanValue n) {

	}

	public void visit(IntegerType n) {

	}

	public void visit(NamedNumberList n) {

	}

	public void visit(NamedNumber n) {

	}

	public void visit(SignedNumber n) {

	}

	public void visit(IntegerValue n) {

	}

	public void visit(XMLIntegerValue n) {

	}

	public void visit(EnumeratedType n) {

	}

	public void visit(Enumerations n) {

	}

	public void visit(RootEnumeration n) {

	}

	public void visit(AdditionalEnumeration n) {

	}

	public void visit(ASNEnumeration n) {

	}

	public void visit(EnumerationItem n) {

	}

	public void visit(EnumeratedValue n) {

	}

	public void visit(XMLEnumeratedValue n) {

	}

	public void visit(RealType n) {

	}

	public void visit(RealValue n) {

	}

	public void visit(NumericRealValue n) {

	}

	public void visit(SpecialRealValue n) {

	}

	public void visit(XMLRealValue n) {

	}

	public void visit(XMLNumericRealValue n) {

	}

	public void visit(XMLSpecialRealValue n) {

	}

	public void visit(BitStringType n) {

	}

	public void visit(NamedBitList n) {

	}

	public void visit(NamedBit n) {

	}

	public void visit(BitStringValue n) {

	}

	public void visit(IdentifierList n) {

	}

	public void visit(XMLBitStringValue n) {

	}

	public void visit(XMLIdentifierList n) {

	}

	public void visit(OctetStringType n) {

	}

	public void visit(OctetStringValue n) {

	}

	public void visit(XMLOctetStringValue n) {

	}

	public void visit(NullType n) {

	}

	public void visit(NullValue n) {

	}

	public void visit(XMLNullValue n) {

	}

	public void visit(SequenceType n) {

	}

	public void visit(ExtensionAndException n) {

	}

	public void visit(OptionalExtensionMarker n) {

	}

	public void visit(ComponentTypeLists n) {

	}

	public void visit(RootComponentTypeList n) {

	}

	public void visit(ExtensionEndMarker n) {

	}

	public void visit(ExtensionAdditions n) {

	}

	public void visit(ExtensionAdditionList n) {

	}

	public void visit(ExtensionAddition n) {

	}

	public void visit(ExtensionAdditionGroup n) {

	}

	public void visit(VersionNumber n) {

	}

	public void visit(ComponentTypeList n) {

	}

	public void visit(ComponentType n) {

	}

	public void visit(SequenceValue n) {

	}

	public void visit(ComponentValueList n) {

	}

	public void visit(XMLSequenceValue n) {

	}

	public void visit(XMLComponentValueList n) {

	}

	public void visit(SequenceOfType n) {

	}

	public void visit(SequenceOfValue n) {

	}

	public void visit(ValueList n) {

	}

	public void visit(XMLSequenceOfValue n) {

	}

	public void visit(XMLValueList n) {

	}

	public void visit(XMLValueOrEmpty n) {

	}

	public void visit(XMLSpaceSeparatedList n) {

	}

	public void visit(XMLDelimitedItemList n) {

	}

	public void visit(XMLDelimitedItem n) {

	}

	public void visit(SetType n) {

	}

	public void visit(SetValue n) {

	}

	public void visit(XMLSetValue n) {

	}

	public void visit(SetOfType n) {

	}

	public void visit(SetOfValue n) {

	}

	public void visit(XMLSetOfValue n) {

	}

	public void visit(ChoiceType n) {

	}

	public void visit(AlternativeTypeLists n) {

	}

	public void visit(RootAlternativeTypeList n) {

	}

	public void visit(ExtensionAdditionAlternatives n) {

	}

	public void visit(ExtensionAdditionAlternativesList n) {

	}

	public void visit(ExtensionAdditionAlternative n) {

	}

	public void visit(ExtensionAdditionAlternativesGroup n) {

	}

	public void visit(AlternativeTypeList n) {

	}

	public void visit(ChoiceValue n) {

	}

	public void visit(XMLChoiceValue n) {

	}

	public void visit(SelectionType n) {

	}

	public void visit(TaggedType n) {

	}

	public void visit(Tag n) {

	}

	public void visit(ClassNumber n) {

	}

	public void visit(TagClass n) {

	}

	public void visit(EmbeddedPDVType n) {

	}

	public void visit(EmbeddedPDVValue n) {

	}

	public void visit(XMLEmbeddedPDVValue n) {

	}

	public void visit(ExternalType n) {

	}

	public void visit(ExternalValue n) {

	}

	public void visit(XMLExternalValue n) {

	}

	public void visit(ObjectIdentifierType n) {

	}

	public void visit(ObjectIdentifierValue n) {

	}

	public void visit(ObjIdComponentsList n) {

	}

	public void visit(ObjIdComponents n) {

	}

	public void visit(NameForm n) {

	}

	public void visit(NumberForm n) {

	}

	public void visit(NameAndNumberForm n) {

	}

	public void visit(XMLObjectIdentifierValue n) {

	}

	public void visit(XMLObjIdComponentList n) {

	}

	public void visit(XMLObjIdComponent n) {

	}

	public void visit(XMLNumberForm n) {

	}

	public void visit(XMLNameAndNumberForm n) {

	}

	public void visit(RelativeOIDType n) {

	}

	public void visit(RelativeOIDValue n) {

	}

	public void visit(RelativeOIDComponentsList n) {

	}

	public void visit(RelativeOIDComponents n) {

	}

	public void visit(XMLRelativeOIDValue n) {

	}

	public void visit(XMLRelativeOIDComponentList n) {

	}

	public void visit(XMLRelativeOIDComponent n) {

	}

	public void visit(CharacterStringType n) {

	}

	public void visit(RestrictedCharacterStringType n) {

	}

	public void visit(RestrictedCharacterStringValue n) {

	}

	public void visit(CharacterStringList n) {

	}

	public void visit(CharSyms n) {

	}

	public void visit(CharsDefn n) {

	}

	public void visit(Quadruple n) {

	}

	public void visit(Group n) {

	}

	public void visit(Plane n) {

	}

	public void visit(Row n) {

	}

	public void visit(Cell n) {

	}

	public void visit(Tuple n) {

	}

	public void visit(TableColumn n) {

	}

	public void visit(TableRow n) {

	}

	public void visit(XMLRestrictedCharacterStringValue n) {

	}

	public void visit(UnrestrictedCharacterStringType n) {

	}

	public void visit(CharacterStringValue n) {

	}

	public void visit(XMLCharacterStringValue n) {

	}

	public void visit(UnrestrictedCharacterStringValue n) {

	}

	public void visit(XMLUnrestrictedCharacterStringValue n) {

	}

	public void visit(UsefulType n) {

	}

	public void visit(TypeWithConstraint n) {

	}

	public void visit(TypeOrNamedType n) {

	}

	public void visit(Constraint n) {

	}

	public void visit(ConstraintSpec n) {

	}

	public void visit(ExceptionSpec n) {

	}

	public void visit(ExceptionIdentification n) {

	}

	public void visit(SubtypeConstraint n) {

	}

	public void visit(ElementSetSpecs n) {

	}

	public void visit(RootElementSetSpec n) {

	}

	public void visit(AdditionalElementSetSpec n) {

	}

	public void visit(ElementSetSpec n) {

	}

	public void visit(Unions n) {

	}

	public void visit(Intersections n) {

	}

	public void visit(IntersectionElements n) {

	}

	public void visit(Elems n) {

	}

	public void visit(Exclusions n) {

	}

	public void visit(UnionMark n) {

	}

	public void visit(IntersectionMark n) {

	}

	public void visit(Elements n) {

	}

	public void visit(SubtypeElements n) {

	}

	public void visit(PatternConstraint n) {

	}

	public void visit(SingleValue n) {

	}

	public void visit(ContainedSubtype n) {

	}

	public void visit(Includes n) {

	}

	public void visit(ValueRange n) {

	}

	public void visit(LowerEndpoint n) {

	}

	public void visit(UpperEndpoint n) {

	}

	public void visit(LowerEndValue n) {

	}

	public void visit(UpperEndValue n) {

	}

	public void visit(SizeConstraint n) {

	}

	public void visit(PermittedAlphabet n) {

	}

	public void visit(TypeConstraint n) {

	}

	public void visit(InnerTypeConstraints n) {

	}

	public void visit(SingleTypeConstraint n) {

	}

	public void visit(MultipleTypeConstraints n) {

	}

	public void visit(FullSpecification n) {

	}

	public void visit(PartialSpecification n) {

	}

	public void visit(TypeConstraints n) {

	}

	public void visit(NamedConstraint n) {

	}

	public void visit(ComponentConstraint n) {

	}

	public void visit(ValueConstraint n) {

	}

	public void visit(PresenceConstraint n) {

	}

}
