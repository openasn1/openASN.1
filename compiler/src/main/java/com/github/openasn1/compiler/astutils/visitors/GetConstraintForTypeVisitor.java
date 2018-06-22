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

import com.github.openasn1.codec.constraints.Constraint;
import com.github.openasn1.codec.constraints.ConstraintExtension;
import com.github.openasn1.codec.constraints.ConstraintIntersection;
import com.github.openasn1.codec.constraints.ConstraintUnion;
import com.github.openasn1.codec.constraints.subtype.IntegerValueRangeConstraint;
import com.github.openasn1.codec.constraints.subtype.PermittedAlphabetConstraint;
import com.github.openasn1.codec.constraints.subtype.RestrictedCharacterValueRangeConstraint;
import com.github.openasn1.codec.constraints.subtype.SingleValueConstraint;
import com.github.openasn1.codec.constraints.subtype.ValueRangeConstraint;
import com.github.openasn1.codec.constraints.subtype.ValueRangeConstraint.Extrema;
import com.github.openasn1.compiler.astutils.ASN1ASTNodeInfos;
import com.github.openasn1.parser.generated.syntaxtree.BuiltinType;
import com.github.openasn1.parser.generated.syntaxtree.BuiltinValue;
import com.github.openasn1.parser.generated.syntaxtree.CharacterStringType;
import com.github.openasn1.parser.generated.syntaxtree.CharacterStringValue;
import com.github.openasn1.parser.generated.syntaxtree.ContainedSubtype;
import com.github.openasn1.parser.generated.syntaxtree.ElementSetSpecs;
import com.github.openasn1.parser.generated.syntaxtree.Elements;
import com.github.openasn1.parser.generated.syntaxtree.InnerTypeConstraints;
import com.github.openasn1.parser.generated.syntaxtree.IntegerType;
import com.github.openasn1.parser.generated.syntaxtree.IntegerValue;
import com.github.openasn1.parser.generated.syntaxtree.IntersectionElements;
import com.github.openasn1.parser.generated.syntaxtree.Intersections;
import com.github.openasn1.parser.generated.syntaxtree.LowerEndValue;
import com.github.openasn1.parser.generated.syntaxtree.LowerEndpoint;
import com.github.openasn1.parser.generated.syntaxtree.NodeToken;
import com.github.openasn1.parser.generated.syntaxtree.PatternConstraint;
import com.github.openasn1.parser.generated.syntaxtree.PermittedAlphabet;
import com.github.openasn1.parser.generated.syntaxtree.ReferencedType;
import com.github.openasn1.parser.generated.syntaxtree.RestrictedCharacterStringType;
import com.github.openasn1.parser.generated.syntaxtree.RestrictedCharacterStringValue;
import com.github.openasn1.parser.generated.syntaxtree.SingleValue;
import com.github.openasn1.parser.generated.syntaxtree.SizeConstraint;
import com.github.openasn1.parser.generated.syntaxtree.TaggedType;
import com.github.openasn1.parser.generated.syntaxtree.TypeConstraint;
import com.github.openasn1.parser.generated.syntaxtree.TypeOrNamedType;
import com.github.openasn1.parser.generated.syntaxtree.Unions;
import com.github.openasn1.parser.generated.syntaxtree.UpperEndValue;
import com.github.openasn1.parser.generated.syntaxtree.UpperEndpoint;
import com.github.openasn1.parser.generated.syntaxtree.Value;
import com.github.openasn1.parser.generated.syntaxtree.ValueRange;
import com.github.openasn1.parser.generated.visitor.GJVoidDepthFirst;

/**
 * @author Clayton Hoss
 *
 */
public class GetConstraintForTypeVisitor extends GJVoidDepthFirst<Constraint> {

	private ASN1ASTNodeInfos infos;

	private Constraint constraint;
	private Class containingType;

	/**
	 * @param infos is infos
	 */
	public GetConstraintForTypeVisitor(ASN1ASTNodeInfos infos) {
		super();
		this.infos = infos;
	}

	/**
	 * @param infos is infos
	 * @param containingType is containingType
	 */
	public GetConstraintForTypeVisitor(ASN1ASTNodeInfos infos, Class containingType) {
		super();
		this.infos = infos;
		this.containingType = containingType;
	}

	public Class getContainingType() {
		return containingType;
	}

	private void setConstraint(Constraint constraint) {
		this.constraint = constraint;
	}

	@Override
	public void visit(BuiltinType n, Constraint argu) {
		if (n.nodeChoice.choice instanceof IntegerType) {
			this.containingType = IntegerType.class;
		}
		if (n.nodeChoice.choice instanceof CharacterStringType) {
			CharacterStringType characterType = (CharacterStringType)n.nodeChoice.choice;

			if (characterType.nodeChoice.choice instanceof RestrictedCharacterStringType) {
				this.containingType = RestrictedCharacterStringType.class;
			}
		}

		if (n.nodeChoice.choice instanceof TaggedType) {
			n.nodeChoice.choice.accept(this, argu);
		} else {
			// LOGGER.debug("Constraint for BuiltinType: " + n.nodeChoice.choice);
		}
	}

	@Override
	public void visit(ReferencedType n, Constraint argu) {
		// LOGGER.debug("Constraint for ReferencedType: " + n.toString());
	}

	@Override
	public void visit(TypeOrNamedType n, Constraint argu) {
		// LOGGER.debug("Constraint for TypeOrNamedType: " + n.toString());
	}

	private class IntersectionsFromUnionsVisitor extends NoTraversalVisitor {
		private List<Intersections> intersections = new ArrayList<Intersections>();

		@Override
		public void visit(Intersections n) {
			getIntersections().add(n);
		}

		@Override
		public void visit(Unions n) {
		      n.intersections.accept(this);
		      n.nodeListOptional.accept(this);
		}

		public List<Intersections> getIntersections() {
			return intersections;
		}
	}

	private class ElementsFromIntersectionVisitor extends NoTraversalVisitor {
		private List<Elements> elements = new ArrayList<Elements>();

		@Override
		public void visit(Elements n) {
			getElements().add(n);
		}

		@Override
		public void visit(Intersections n) {
		      n.intersectionElements.accept(this);
		      n.nodeListOptional.accept(this);
		}

		@Override
		public void visit(IntersectionElements n) {
			n.elements.accept(this);
		    n.nodeOptional.accept(this);
		}

		public List<Elements> getElements() {
			return elements;
		}
	}

	private List<Elements> getElementsFromIntersections(Intersections n) {
		ElementsFromIntersectionVisitor elementsVisior = new ElementsFromIntersectionVisitor();
		n.accept(elementsVisior);

		return elementsVisior.getElements();
	}

	private List<Intersections> getIntersectionsFromUnions(Unions n) {
		IntersectionsFromUnionsVisitor intersectionsVisior = new IntersectionsFromUnionsVisitor();
		n.accept(intersectionsVisior);

		return intersectionsVisior.getIntersections();
	}

	private Constraint getConstraintFromElements(Elements elements) {
		GetConstraintForTypeVisitor elementsVisitor =  new GetConstraintForTypeVisitor(null, getContainingType());
		elements.accept(elementsVisitor, null);

		return elementsVisitor.getConstraint();
	}

	private Constraint getConstraintFromIntersections(Intersections intersections) {
		GetConstraintForTypeVisitor intersectionsVisitor =  new GetConstraintForTypeVisitor(null, getContainingType());
		intersections.accept(intersectionsVisitor, null);

		return intersectionsVisitor.getConstraint();
	}



	@Override
	public void visit(ElementSetSpecs n, Constraint argu) {
		if (n.nodeOptional.present()) {
			// LOGGER.debug("Adding constraint: Extension");

			// we got an extension marker but not neccessarily an additional element
			GetConstraintForTypeVisitor rootConstraintVisitor = new GetConstraintForTypeVisitor(null, getContainingType());
			n.rootElementSetSpec.accept(rootConstraintVisitor, argu);

			GetConstraintForTypeVisitor additionConstraintVisitor = new GetConstraintForTypeVisitor(null, getContainingType());
			n.nodeOptional.accept(additionConstraintVisitor, argu);

			ConstraintExtension constraintExtension = new ConstraintExtension(rootConstraintVisitor.getConstraint(), additionConstraintVisitor.getConstraint());

			this.constraint = constraintExtension;
		} else {
			n.rootElementSetSpec.accept(this, argu);
		}
	}

	@Override
	public void visit(Intersections n, Constraint argu) {
		ConstraintIntersection constraintIntersection = new ConstraintIntersection();

		int addedElements = 0;
		for (Elements elements : getElementsFromIntersections(n)) {
			Constraint elementConstraint = getConstraintFromElements(elements);
			if (elementConstraint != null) {
				constraintIntersection.addConstraint(elementConstraint);
				addedElements++;
			}
		}

		if (addedElements == 1) {
			// LOGGER.debug("Adding constraint: ## pseudo Intersection");
			// We only have one intersection element, so we don't need the intersection
			this.constraint = constraintIntersection.getList().get(0);
		} else if (addedElements > 1) {
			// LOGGER.debug("Adding constraint: Intersection (" + addedElements + " elements)");
			this.constraint = constraintIntersection;
		}
	}

	@Override
	public void visit(Unions n, Constraint argu) {
		// do we really have a union?
		if (n.nodeListOptional.present()) {
			ConstraintUnion constraintUnion = new ConstraintUnion();

			// LOGGER.debug("Adding constraint: Union");

			for (Intersections intersections : getIntersectionsFromUnions(n)) {
				Constraint intersectionsConstraint = getConstraintFromIntersections(intersections);
				if (intersectionsConstraint != null) {
					constraintUnion.addConstraint(intersectionsConstraint);
				}
			}

			this.constraint = constraintUnion;

			return;
		}

		super.visit(n, argu);
	}

	@Override
	public void visit(ContainedSubtype n, Constraint argu) {
		// LOGGER.debug("Adding constraint: ContainedSubtype");

		super.visit(n, argu);
	}

	@Override
	public void visit(InnerTypeConstraints n, Constraint argu) {
		// LOGGER.debug("Adding constraint: InnerTypeConstraints");

		super.visit(n, argu);
	}

	@Override
	public void visit(PatternConstraint n, Constraint argu) {
		// LOGGER.debug("Adding constraint: PatternConstraint");

		super.visit(n, argu);
	}

	@Override
	public void visit(PermittedAlphabet n, Constraint argu) {
		// LOGGER.debug("Adding constraint: PermittedAlphabet");

		GetConstraintForTypeVisitor alphabetVisitor = new GetConstraintForTypeVisitor(null, getContainingType());
		n.constraint.accept(alphabetVisitor, null);

		Constraint permittedAlphabetConstraint = new PermittedAlphabetConstraint(alphabetVisitor.getConstraint());

		this.setConstraint(permittedAlphabetConstraint);
	}

	@Override
	public void visit(SingleValue n, Constraint argu) {
		// LOGGER.debug("Adding constraint: SingleValue");

		ValueVisitor valueVisitor = new ValueVisitor();
		n.value.accept(valueVisitor);

		if (valueVisitor.getValue() instanceof Integer) {
			// LOGGER.debug("Adding constraint: --- is Integer (" + (Integer)valueVisitor.getValue() + ")");
			setConstraint(new SingleValueConstraint<Integer>((Integer)valueVisitor.getValue()));
		} else if (valueVisitor.getValue() instanceof String) {
			// LOGGER.debug("Adding constraint: --- is String (" + valueVisitor.getValue() + ")");
			setConstraint(new SingleValueConstraint<String>((String)valueVisitor.getValue()));
		}
	}

	private class ValueVisitor extends NoTraversalVisitor {
		private Object value;

		public Object getValue() {
			return value;
		}

		private void setValue(Object value) {
			this.value = value;
		}

		@Override
		public void visit(Value n) {
			n.nodeChoice.choice.accept(this);
		}

		@Override
		public void visit(BuiltinValue n) {
			n.nodeChoice.choice.accept(this);
		}

		@Override
		public void visit(CharacterStringValue n) {
			n.restrictedCharacterStringValue.accept(this);
		}

		@Override
		public void visit(RestrictedCharacterStringValue n) {
			if (n.nodeChoice.choice instanceof NodeToken) {
				NodeToken token = (NodeToken)n.nodeChoice.choice;

				setValue(token.tokenImage.substring(1, token.tokenImage.length()-1));
			}
		}

		@Override
		public void visit(IntegerValue n) {
			try {
				setValue(Integer.parseInt(n.signedNumber.nodeToken.tokenImage));
			} catch (Exception e) {
				setValue(Integer.MAX_VALUE);
			}
		}
	}

	@Override
	public void visit(SizeConstraint n, Constraint argu) {
		// LOGGER.debug("Adding constraint: SizeConstraint");

		GetConstraintForTypeVisitor sizeVisitor = new GetConstraintForTypeVisitor(null, IntegerType.class);
		n.constraint.accept(sizeVisitor, null);

		Constraint sizeConstraint = new com.github.openasn1.codec.constraints.subtype.SizeConstraint(sizeVisitor.getConstraint());

		this.setConstraint(sizeConstraint);
	}

	@Override
	public void visit(TypeConstraint n, Constraint argu) {
		// LOGGER.debug("Adding constraint: TypeConstraint");

		super.visit(n, argu);
	}

	private class ValueRangeEndpointVisitor extends NoTraversalVisitor {
		private boolean isEndpointExcluded = false;
		private Object endpointValue;

		public Object getEndpointValue() {
			return endpointValue;
		}

		private void setEndpointValue(Object endpointValue) {
			this.endpointValue = endpointValue;
		}

		public boolean isExtremum() {
			return Extrema.MINIMUM.equals(getEndpointValue()) || Extrema.MAXIMUM.equals(getEndpointValue());
		}

		public boolean isEndpointExcluded() {
			return isEndpointExcluded;
		}

		private void setEndpointExcluded(boolean isEndpointExcluded) {
			this.isEndpointExcluded = isEndpointExcluded;
		}

		@Override
		public void visit(LowerEndpoint n) {
			// LOGGER.debug("Adding constraint: (LowerEndpoint)");

			setEndpointExcluded(n.nodeOptional.present());

			n.lowerEndValue.accept(this);
		}

		@Override
		public void visit(UpperEndpoint n) {
			// LOGGER.debug("Adding constraint: (UpperEndpoint)");

			setEndpointExcluded(n.nodeOptional.present());

			n.upperEndValue.accept(this);
		}

		@Override
		public void visit(LowerEndValue n) {
			n.nodeChoice.choice.accept(this);
		}

		@Override
		public void visit(UpperEndValue n) {
			n.nodeChoice.choice.accept(this);
		}

		@Override
		public void visit(NodeToken n) {
			if ("MIN".equalsIgnoreCase(n.tokenImage)) {
				// LOGGER.debug("Adding constraint: --- MIN extremum");
				setEndpointValue(Extrema.MINIMUM);
			} else if ("MAX".equalsIgnoreCase(n.tokenImage)) {
				// LOGGER.debug("Adding constraint: --- MAX extremum");
				setEndpointValue(Extrema.MAXIMUM);
			}
		}

		@Override
		public void visit(Value n) {
			n.nodeChoice.choice.accept(this);
		}

		@Override
		public void visit(BuiltinValue n) {
			n.nodeChoice.choice.accept(this);
		}

		@Override
		public void visit(CharacterStringValue n) {
			n.restrictedCharacterStringValue.accept(this);
		}

		@Override
		public void visit(RestrictedCharacterStringValue n) {
			if (n.nodeChoice.choice instanceof NodeToken) {
				NodeToken token = (NodeToken)n.nodeChoice.choice;

				// LOGGER.debug("Adding constraint: --- endpoint String (" + token.tokenImage.substring(1, 2) + ")");
				setEndpointValue(token.tokenImage.substring(1, 2));
			}
		}

		@Override
		public void visit(IntegerValue n) {
			// LOGGER.debug("Adding constraint: --- endpoint Integer (" + Integer.parseInt(n.signedNumber.nodeToken.tokenImage) + ")");
			try {
				setEndpointValue(Integer.parseInt(n.signedNumber.nodeToken.tokenImage));
			} catch (Exception e) {
				setEndpointValue(Integer.MAX_VALUE);
			}
		}
	}

	@Override
	public void visit(ValueRange n, Constraint argu) {
		// LOGGER.debug("Adding constraint: ValueRange");

		ValueRangeEndpointVisitor lowerEndpointVisitor =  new ValueRangeEndpointVisitor();
		n.lowerEndpoint.accept(lowerEndpointVisitor);

		ValueRangeEndpointVisitor upperEndpointVisitor =  new ValueRangeEndpointVisitor();
		n.upperEndpoint.accept(upperEndpointVisitor);

		boolean isLowerEndpointExcluded = lowerEndpointVisitor.isEndpointExcluded();
		boolean isUpperEndpointExcluded = upperEndpointVisitor.isEndpointExcluded();
		boolean isLowerEndpointExtremum = lowerEndpointVisitor.isExtremum();
		boolean isUpperEndpointExtremum = upperEndpointVisitor.isExtremum();

		ValueRangeConstraint valueRangeConstraint = null;

		if (isLowerEndpointExtremum && isUpperEndpointExtremum) {
			if (IntegerType.class.equals(getContainingType())) {
				valueRangeConstraint = new IntegerValueRangeConstraint((Extrema)lowerEndpointVisitor.getEndpointValue(), (Extrema)upperEndpointVisitor.getEndpointValue(), isLowerEndpointExcluded, isUpperEndpointExcluded);
			} else if(RestrictedCharacterStringType.class.equals(getContainingType())) {
				valueRangeConstraint = new RestrictedCharacterValueRangeConstraint((Extrema)lowerEndpointVisitor.getEndpointValue(), (Extrema)upperEndpointVisitor.getEndpointValue(), isLowerEndpointExcluded, isUpperEndpointExcluded);
			}
		} else if (isLowerEndpointExtremum && ! isUpperEndpointExtremum) {
			if (IntegerType.class.equals(getContainingType())) {
				valueRangeConstraint = new IntegerValueRangeConstraint((Extrema)lowerEndpointVisitor.getEndpointValue(), (Integer)upperEndpointVisitor.getEndpointValue(), isLowerEndpointExcluded, isUpperEndpointExcluded);
			} else if(RestrictedCharacterStringType.class.equals(getContainingType())) {
				valueRangeConstraint = new RestrictedCharacterValueRangeConstraint((Extrema)lowerEndpointVisitor.getEndpointValue(), ((String)upperEndpointVisitor.getEndpointValue()).charAt(0), isLowerEndpointExcluded, isUpperEndpointExcluded);
			}
		} else if (! isLowerEndpointExtremum && isUpperEndpointExtremum) {
			if (IntegerType.class.equals(getContainingType())) {
				valueRangeConstraint = new IntegerValueRangeConstraint((Integer)lowerEndpointVisitor.getEndpointValue(), (Extrema)upperEndpointVisitor.getEndpointValue(), isLowerEndpointExcluded, isUpperEndpointExcluded);
			} else if(RestrictedCharacterStringType.class.equals(getContainingType())) {
				valueRangeConstraint = new RestrictedCharacterValueRangeConstraint(((String)lowerEndpointVisitor.getEndpointValue()).charAt(0), (Extrema)upperEndpointVisitor.getEndpointValue(), isLowerEndpointExcluded, isUpperEndpointExcluded);
			}
		} else {
			if (IntegerType.class.equals(getContainingType())) {
				valueRangeConstraint = new IntegerValueRangeConstraint((Integer)lowerEndpointVisitor.getEndpointValue(), (Integer)upperEndpointVisitor.getEndpointValue(), isLowerEndpointExcluded, isUpperEndpointExcluded);
			} else if(RestrictedCharacterStringType.class.equals(getContainingType())) {
				valueRangeConstraint = new RestrictedCharacterValueRangeConstraint(((String)lowerEndpointVisitor.getEndpointValue()).charAt(0), ((String)upperEndpointVisitor.getEndpointValue()).charAt(0), isLowerEndpointExcluded, isUpperEndpointExcluded);
			}
		}

		this.setConstraint(valueRangeConstraint);
	}

	/**
	 * @return the constraint
	 */
	public Constraint getConstraint() {
		return this.constraint;
	}

	/**
	 * @return the infos
	 */
	protected ASN1ASTNodeInfos getInfos() {
		return this.infos;
	}
}