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
package com.github.openasn1.compiler.codeoutpututils;

import java.util.HashMap;

import org.apache.log4j.Logger;
import com.github.openasn1.compiler.omast.AggregatedASN1Type;
import com.github.openasn1.compiler.omast.AggregatedASN1TypeEnum;
import com.github.openasn1.compiler.omast.ComplexASN1Type;
import com.github.openasn1.compiler.omast.ComplexASN1TypeEnum;
import com.github.openasn1.compiler.omast.EnumASN1Type;
import com.github.openasn1.compiler.omast.ReferencedASN1Type;
import com.github.openasn1.compiler.omast.SimpleASN1Type;
import com.github.openasn1.compiler.omast.SimpleASN1TypeEnum;


public final class ASN1ToJavaNameMapper {
	private static Logger LOGGER = Logger.getLogger(ASN1ToJavaNameMapper.class.getSimpleName());

	private static HashMap<SimpleASN1TypeEnum, String> simpleTypeNameMapping = new HashMap<SimpleASN1TypeEnum, String>();
	private static HashMap<ComplexASN1TypeEnum, String> complexTypeNameMapping = new HashMap<ComplexASN1TypeEnum, String>();
	private static HashMap<AggregatedASN1TypeEnum, String> aggregatedTypeNameMapping = new HashMap<AggregatedASN1TypeEnum, String>();

	private static HashMap<SimpleASN1TypeEnum, String> simpleTypeMapping = new HashMap<SimpleASN1TypeEnum, String>();

	static {
		simpleTypeNameMapping.put(SimpleASN1TypeEnum.INTEGER, "Integer");
		simpleTypeNameMapping.put(SimpleASN1TypeEnum.BMPString, "BMPString");
		simpleTypeNameMapping.put(SimpleASN1TypeEnum.GeneralString, "GeneralString");
		simpleTypeNameMapping.put(SimpleASN1TypeEnum.UTF8String, "UTF8String");
		simpleTypeNameMapping.put(SimpleASN1TypeEnum.VisibleString, "VisibleString");
		simpleTypeNameMapping.put(SimpleASN1TypeEnum.IA5String, "IA5String");
		simpleTypeNameMapping.put(SimpleASN1TypeEnum.NumericString, "NumericString");
		simpleTypeNameMapping.put(SimpleASN1TypeEnum.PrintableString, "PrintableString");
		simpleTypeNameMapping.put(SimpleASN1TypeEnum.OBJECTIDENTIFIER, "ObjectIdentifier");
		simpleTypeNameMapping.put(SimpleASN1TypeEnum.NULL, "NullType");
		simpleTypeNameMapping.put(SimpleASN1TypeEnum.BOOLEAN, "Boolean");
		simpleTypeNameMapping.put(SimpleASN1TypeEnum.OCTETSTRING, "OctetString");

		complexTypeNameMapping.put(ComplexASN1TypeEnum.SET, "Set");
		complexTypeNameMapping.put(ComplexASN1TypeEnum.CHOICE, "Choice");
		complexTypeNameMapping.put(ComplexASN1TypeEnum.SEQUENCE, "Sequence");

		aggregatedTypeNameMapping.put(AggregatedASN1TypeEnum.SEQUENCEOF, "SequenceOf");
		aggregatedTypeNameMapping.put(AggregatedASN1TypeEnum.SETOF, "SetOf");

		simpleTypeMapping.put(SimpleASN1TypeEnum.INTEGER, "Integer");
		simpleTypeMapping.put(SimpleASN1TypeEnum.BMPString, "String");
		simpleTypeMapping.put(SimpleASN1TypeEnum.GeneralString, "String");
		simpleTypeMapping.put(SimpleASN1TypeEnum.UTF8String, "String");
		simpleTypeMapping.put(SimpleASN1TypeEnum.VisibleString, "String");
		simpleTypeMapping.put(SimpleASN1TypeEnum.IA5String, "String");
		simpleTypeMapping.put(SimpleASN1TypeEnum.NumericString, "String");
		simpleTypeMapping.put(SimpleASN1TypeEnum.PrintableString, "String");
		simpleTypeMapping.put(SimpleASN1TypeEnum.OBJECTIDENTIFIER, "List<Integer>");
		simpleTypeMapping.put(SimpleASN1TypeEnum.NULL, "Class<Void>");
		simpleTypeMapping.put(SimpleASN1TypeEnum.BOOLEAN, "Boolean");
		simpleTypeMapping.put(SimpleASN1TypeEnum.OCTETSTRING, "byte[]");
	}

	public static String getMappedJavaType(SimpleASN1Type simpleType) {
		if (! simpleTypeMapping.containsKey(simpleType.getAsn1Type())) {
			LOGGER.error("simpleType with name '" + simpleType.getName() + "' of type '" + simpleType.getAsn1Type().name() + "' cannot be mapped to java type!");
		}
		return simpleTypeMapping.get(simpleType.getAsn1Type());
	}

	public static String getMappedJavaName(SimpleASN1Type simpleType) {
		if (! simpleTypeMapping.containsKey(simpleType.getAsn1Type())) {
			LOGGER.error("simpleType with name '" + simpleType.getName() + "' of type '" + simpleType.getAsn1Type().name() + "' cannot be mapped to java name!");
		}
		return simpleTypeNameMapping.get(simpleType.getAsn1Type());
	}

	public static String getMappedJavaName(EnumASN1Type enumType) {
		return "Enumerated";
	}

	public static String getMappedJavaName(ComplexASN1Type complexType) {
		return complexTypeNameMapping.get(complexType.getAsn1Type());
	}

	public static String getMappedJavaName(AggregatedASN1Type aggregatedType) {
		return aggregatedTypeNameMapping.get(aggregatedType.getAsn1Type());
	}

	public static String getMappedJavaName(ReferencedASN1Type referencedType) {
		return referencedType.getReferencedName();
	}
}