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
package com.github.openasn1.compiler.stages.omast.codeoutput.omcoder.decoder;

import java.io.Writer;
import java.util.List;

import com.github.openasn1.compiler.codeoutpututils.ASN1ToJavaNameMapper;
import com.github.openasn1.compiler.codeoutpututils.templaterenderer.Attribute;
import com.github.openasn1.compiler.codeoutpututils.templaterenderer.FreemarkerRenderer;
import com.github.openasn1.compiler.omast.AbstractASN1Type;
import com.github.openasn1.compiler.omast.AggregatedASN1Type;
import com.github.openasn1.compiler.omast.ComplexASN1Type;
import com.github.openasn1.compiler.omast.ReferencedASN1Type;
import com.github.openasn1.compiler.omast.SimpleASN1Type;
import com.github.openasn1.compiler.stages.omast.codeoutput.ModuleImportInformation;
import com.github.openasn1.compiler.stages.omast.codeoutput.utils.CanonicalTypeNamingVisitor;
import com.github.openasn1.compiler.stages.omast.codeoutput.utils.ChainedNamingStrategy;
import com.github.openasn1.compiler.stages.omast.codeoutput.utils.InnerComponentNamingStrategy;
import com.github.openasn1.compiler.stages.omast.codeoutput.utils.PostfixNamingStrategy;


/**
 * @author Clayton Hoss
 *
 */
public class OMDecoderTemplateRenderer extends FreemarkerRenderer {
	public OMDecoderTemplateRenderer(Writer writer, String templateRepositoryPath) {
		super(writer, templateRepositoryPath);
	}

	public void renderDecodeFunctionHeader(AbstractASN1Type theType) {
		CanonicalTypeNamingVisitor visitor = new CanonicalTypeNamingVisitor();
		theType.accept(visitor);
		
		String canonicalOMClassName = visitor.getCanonicalName(InnerComponentNamingStrategy.getInstance());
		
		render("decoder/decodeFunctionHeader.ftl", 
				new Attribute("canonicalOMClassName", canonicalOMClassName));
	}

	public void renderDecodeFunctionOMPrototype(AbstractASN1Type theType) {
		CanonicalTypeNamingVisitor visitor = new CanonicalTypeNamingVisitor();
		theType.accept(visitor);
		
		String canonicalOMClassName = visitor.getCanonicalName(InnerComponentNamingStrategy.getInstance());
		String className = InnerComponentNamingStrategy.getInstance().getName(theType.getName(), visitor.getNestingDepth());

		visitor.getNameContributingTypes().remove(visitor.getNameContributingTypes().size()-1);
		String enclosingClassName = visitor.getCanonicalName(InnerComponentNamingStrategy.getInstance());
		List<String> enclosingClassNames = visitor.getContributingTypeNames(InnerComponentNamingStrategy.getInstance());
		
		render("decoder/decodeFunctionOMPrototype.ftl",
				new Attribute("enclosingClassNames", enclosingClassNames),
				new Attribute("enclosingClassName", enclosingClassName),
				new Attribute("className", className),
				new Attribute("canonicalOMClassName", canonicalOMClassName));
	}

	public void renderDecodeFunctionFooter() {
		render("decoder/decodeFunctionFooter.ftl");
	}

	public void renderReferencedType(String moduleName, ReferencedASN1Type type, List<ModuleImportInformation> imports) {
		render("preHeader.ftl");
		render("decoder/decoderPackageDef.ftl",
				new Attribute("package", moduleName + ".coder"));

		renderImport("java.lang.reflect.Field");
		
		renderImport(moduleName + ".om.*");
		for (ModuleImportInformation importInformation : imports) {
			renderImport(importInformation.getModuleName() + ".om." + importInformation.getTypeName());
			renderImport(importInformation.getModuleName() + ".coder." + importInformation.getTypeName() + "Decoder");
		}
		
		render("decoder/decoderReferencedType.ftl",
				new Attribute("className", type.getName() + "Decoder"),
				new Attribute("omClassName", type.getName()),
				new Attribute("superOMClassName", type.getReferencedName()),
				new Attribute("superClassName", type.getReferencedName() + "Decoder"));
	}

	public void renderFileHeader(String packageName) {
		render("preHeader.ftl");
		render("decoder/decoderPackageDef.ftl",
				new Attribute("package", packageName));
	}

	public void renderImport(String fileImport) {
		render("import.ftl",
				new Attribute("import", fileImport));
	}

	public void renderDecodeFunctionDecoderList() {
		render("decoder/decodeFunctionDecoderList.ftl");
	}

	public void renderDecodeFunctionDecoderListAddAggStart(AggregatedASN1Type type, String name) {
		render("decoder/decodeFunctionDecoderListAddAggStart.ftl",
				new Attribute("aggregatedType", ASN1ToJavaNameMapper.getMappedJavaName(type)),
				new Attribute("componentTypeName", name));
	}
	
	public void renderDecodeFunctionDecoderAddToComponentList(AbstractASN1Type componentType) {
		render("decoder/decodeFunctionDecoderAddToComponentList.ftl",
				new Attribute("componentName", componentType.getName()));
	}

	public void renderDecodeFunctionDecode(AbstractASN1Type parentType, AbstractASN1Type childType) {
		render("decoder/decodeFunctionDecode.ftl",
				new Attribute("parentClassName", parentType.getName()),
				new Attribute("componentName", childType.getName()));
	}
	
	public void renderDecodeFunctionAggregatedTypeDecode(AggregatedASN1Type parentType, AbstractASN1Type childType) {
		render("decoder/decodeFunctionAggregatedTypeDecode.ftl",
				new Attribute("aggregationType", ASN1ToJavaNameMapper.getMappedJavaName(parentType)),
				new Attribute("parentClassName", parentType.getName()),
				new Attribute("componentName", childType.getName()));
	}	

	public void renderDecodeFunctionDecodeChoice(AbstractASN1Type parentType, AbstractASN1Type childType) {
		render("decoder/decodeFunctionDecodeChoice.ftl",
				new Attribute("parentClassName", parentType.getName()),
				new Attribute("componentName", childType.getName()));
	}

	public void renderDecodeFunctionDecoderListAddAggEnd(AbstractASN1Type componentType, String className) {
		CanonicalTypeNamingVisitor visitor = new CanonicalTypeNamingVisitor(true);
		componentType.accept(visitor);
		
		String typeInformationClass = visitor.getCanonicalName(new ChainedNamingStrategy(new PostfixNamingStrategy("TypeInformation"), InnerComponentNamingStrategy.getInstance()));

		render("decoder/decodeFunctionDecoderListAddAggEnd.ftl",
				new Attribute("typeInformationClass", typeInformationClass),
				new Attribute("className", className),
				new Attribute("componentName", componentType.getName()));
	}

	public void renderDecodeFunctionDecoderListAddAggEndPrototype(AbstractASN1Type componentType, String className) {
		CanonicalTypeNamingVisitor visitor = new CanonicalTypeNamingVisitor(true);
		componentType.accept(visitor);
		
		String typeInformationClass = visitor.getCanonicalName(new ChainedNamingStrategy(new PostfixNamingStrategy("TypeInformation"), InnerComponentNamingStrategy.getInstance()));

		render("decoder/decodeFunctionDecoderListAddAggEndPrototype.ftl",
				new Attribute("typeInformationClass", typeInformationClass),
				new Attribute("className", className),
				new Attribute("componentName", componentType.getName()));
	}

	public void renderDecodeFunctionDecoderListAddAggDef(String componentDecoderName, String componentTypeName, String componentName) {
		render("decoder/decodeFunctionDecoderListAddAggDef.ftl",
				new Attribute("componentDecoderName", componentDecoderName + "Decoder"),
				new Attribute("componentTypeName", componentTypeName),
				new Attribute("componentName", componentName));
	}

	public void renderDecodeFunctionDecoderListAddStart(String componentDecoderName, String componentName) {
		render("decoder/decodeFunctionDecoderListAddStart.ftl",
				new Attribute("componentDecoderName", componentDecoderName + "Decoder"),
				new Attribute("componentName", componentName));
	}

	public void renderDecodeFunctionDecoderInnerComplexTypeListAddStart(ComplexASN1Type type, String componentDecoderName, String componentName) {
		CanonicalTypeNamingVisitor visitor = new CanonicalTypeNamingVisitor();
		type.accept(visitor);

		// String typeInformationClass = visitor.getCanonicalName(new ChainedNamingStrategy(new PostfixNamingStrategy("TypeInformation"), InnerComponentNamingStrategy.getInstance()));
		String decoderClass = visitor.getCanonicalName(new ChainedNamingStrategy(new PostfixNamingStrategy("Decoder"), InnerComponentNamingStrategy.getInstance()));

		render("decoder/decodeFunctionDecoderListAddStart.ftl",
				new Attribute("componentDecoderName", decoderClass),
				new Attribute("componentName", componentName));
	}

	public void renderDecodeFunctionDecoderEnumTypeListAddStart(AbstractASN1Type type, String componentDecoderName, String componentName) {
		CanonicalTypeNamingVisitor visitor = new CanonicalTypeNamingVisitor();
		type.accept(visitor);

		String canonicalClassName = visitor.getCanonicalName(InnerComponentNamingStrategy.getInstance());
		
		render("decoder/decodeFunctionDecoderEnumTypeListAddStart.ftl",
				new Attribute("canonicalClassName", canonicalClassName),
				new Attribute("componentDecoderName", componentDecoderName + "Decoder"),
				new Attribute("componentName", componentName));
	}

	public void renderDecodeFunctionDecoderListAddEnd() {
		render("decoder/decodeFunctionDecoderListAddEnd.ftl");
	}

	public void renderDecodeFunctionDecoderComplexType(ComplexASN1Type theType) {
		render("decoder/decodeFunctionDecoderComplexType.ftl", new Attribute("complexType", ASN1ToJavaNameMapper.getMappedJavaName(theType)));
	}

	public void renderDecodeFunctionDecoderSimpleType(SimpleASN1Type theType) {
		render("decoder/decodeFunctionDecoderSimpleType.ftl", 
				new Attribute("simpleType", ASN1ToJavaNameMapper.getMappedJavaName(theType)),
				new Attribute("omClassName", theType.getName()));
	}

	public void renderDecodeFunctionDecoderAggregatedType(AggregatedASN1Type theType) {
		render("decoder/decodeFunctionDecoderAggregatedType.ftl", 
				new Attribute("aggregatedType", ASN1ToJavaNameMapper.getMappedJavaName(theType)),
				new Attribute("omClassName", theType.getName()));
	}

	public void renderDecodeFunctionDecoderListAddPrototype(AbstractASN1Type type, String className, String componentDecoderName, String componentName) {
		CanonicalTypeNamingVisitor visitor = new CanonicalTypeNamingVisitor(true);
		type.accept(visitor);
		
		String typeInformationClass = visitor.getCanonicalName(new ChainedNamingStrategy(new PostfixNamingStrategy("TypeInformation"), InnerComponentNamingStrategy.getInstance()));

		render("decoder/decodeFunctionDecoderListAddPrototype.ftl",
				new Attribute("typeInformationClass", typeInformationClass),
				new Attribute("componentDecoderName", componentDecoderName + "Decoder"),
				new Attribute("componentName", componentName),
				new Attribute("className", className));
	}

	public void renderDecodeFunctionDecoderInnerComplexTypeListAddPrototype(AbstractASN1Type type, String className, String componentDecoderName, String componentName) {
		CanonicalTypeNamingVisitor visitor = new CanonicalTypeNamingVisitor(true);
		type.accept(visitor);
		
		String typeInformationClass = visitor.getCanonicalName(new ChainedNamingStrategy(new PostfixNamingStrategy("TypeInformation"), InnerComponentNamingStrategy.getInstance()));

		visitor = new CanonicalTypeNamingVisitor();
		type.accept(visitor);

		String decoderClass = visitor.getCanonicalName(new ChainedNamingStrategy(new PostfixNamingStrategy("Decoder"), InnerComponentNamingStrategy.getInstance()));
		if (decoderClass.length() == 0) {
			decoderClass = type.getName() + "Decoder";
		}
		
		render("decoder/decodeFunctionDecoderInnerComplexTypeListAddPrototype.ftl",
				new Attribute("typeInformationClass", typeInformationClass),
				new Attribute("componentDecoderName", decoderClass),
				new Attribute("componentName", componentName),
				new Attribute("className", className));
	}

	public void renderDecodeFunctionDecoderListAdd(AbstractASN1Type type, String className, String componentDecoderName, String componentName) {
		CanonicalTypeNamingVisitor visitor = new CanonicalTypeNamingVisitor(true);
		type.accept(visitor);
		
		String typeInformationClass = visitor.getCanonicalName(new ChainedNamingStrategy(new PostfixNamingStrategy("TypeInformation"), InnerComponentNamingStrategy.getInstance()));
		
		render("decoder/decodeFunctionDecoderListAdd.ftl",
				new Attribute("typeInformationClass", typeInformationClass),
				new Attribute("componentDecoderName", componentDecoderName + "Decoder"),
				new Attribute("componentName", componentName),
				new Attribute("className", className));
	}

	public void renderDecodeFunctionDecoderInnerComplexTypeListAdd(AbstractASN1Type type, String className, String componentDecoderName, String componentName) {
		CanonicalTypeNamingVisitor visitor = new CanonicalTypeNamingVisitor(true);
		type.accept(visitor);
		
		String typeInformationClass = visitor.getCanonicalName(new ChainedNamingStrategy(new PostfixNamingStrategy("TypeInformation"), InnerComponentNamingStrategy.getInstance()));

		visitor = new CanonicalTypeNamingVisitor();
		type.accept(visitor);
		String decoderClass = visitor.getCanonicalName(new ChainedNamingStrategy(new PostfixNamingStrategy("Decoder"), InnerComponentNamingStrategy.getInstance()));
		
		render("decoder/decodeFunctionDecoderListAdd.ftl",
				new Attribute("typeInformationClass", typeInformationClass),
				new Attribute("componentDecoderName", decoderClass),
				new Attribute("componentName", componentName),
				new Attribute("className", className));
	}

	public void renderDecodeFunctionDecoderEnumTypeListAdd(AbstractASN1Type type, String className, String componentDecoderName, String componentName) {
		CanonicalTypeNamingVisitor visitor = new CanonicalTypeNamingVisitor(true);
		type.accept(visitor);
		
		String typeInformationClass = visitor.getCanonicalName(new ChainedNamingStrategy(new PostfixNamingStrategy("TypeInformation"), InnerComponentNamingStrategy.getInstance()));

		visitor = new CanonicalTypeNamingVisitor();
		type.accept(visitor);
		String canonicalClassName = visitor.getCanonicalName(InnerComponentNamingStrategy.getInstance());
		
		render("decoder/decodeFunctionDecoderEnumTypeListAdd.ftl",
				new Attribute("typeInformationClass", typeInformationClass),
				new Attribute("componentDecoderName", componentDecoderName + "Decoder"),
				new Attribute("componentName", componentName),
				new Attribute("canonicalClassName", canonicalClassName),
				new Attribute("className", className));
	}

	public void renderEnum(AbstractASN1Type theType, boolean isInnerClass) {
		CanonicalTypeNamingVisitor visitor = new CanonicalTypeNamingVisitor();
		theType.accept(visitor);
		
		String omClassName = InnerComponentNamingStrategy.getInstance().getName(theType.getName(), visitor.getNestingDepth());
		String canonicalOMClassName = visitor.getCanonicalName(InnerComponentNamingStrategy.getInstance());

		render("decoder/decoderEnum.ftl",
				new Attribute("visibility", isInnerClass ? "private" : "public"),
				new Attribute("className", omClassName + "Decoder"),
				new Attribute("canonicalOMClassName", canonicalOMClassName));
	}
	
	public void renderClassHeader(AbstractASN1Type theType, boolean isInnerClass) {
		CanonicalTypeNamingVisitor visitor = new CanonicalTypeNamingVisitor();
		theType.accept(visitor);
		
		String omClassName = InnerComponentNamingStrategy.getInstance().getName(theType.getName(), visitor.getNestingDepth());
		String canonicalOMClassName = visitor.getCanonicalName(InnerComponentNamingStrategy.getInstance());

		render("decoder/decoderClassHeader.ftl",
				new Attribute("visibility", isInnerClass ? "private" : "public"),
				new Attribute("className", omClassName + "Decoder"),
				new Attribute("canonicalOMClassName", canonicalOMClassName));
	}

	public void renderClassConstructor(AbstractASN1Type theType) {
		CanonicalTypeNamingVisitor visitor = new CanonicalTypeNamingVisitor();
		theType.accept(visitor);

		String omClassName = InnerComponentNamingStrategy.getInstance().getName(theType.getName(), visitor.getNestingDepth());
		
		render("decoder/decoderClassConstructor.ftl",
				new Attribute("className", omClassName + "Decoder"));
	}

	public void renderClassFooter(AbstractASN1Type theType) {
		render("encoder/encoderClassFooter.ftl");
	}
}