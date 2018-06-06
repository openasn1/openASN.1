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
package com.github.openasn1.compiler.stages.omast.codeoutput.omcoder.encoder;

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
public class OMEncoderTemplateRenderer extends FreemarkerRenderer {
	public OMEncoderTemplateRenderer(Writer writer, String templateRepositoryPath) {
		super(writer, templateRepositoryPath);
	}

	public void renderEncodeFunctionHeader() {
		render("encoder/encodeFunctionHeader.ftl");
	}

	public void renderEncodeFunctionFooter() {
		render("encoder/encodeFunctionFooter.ftl");
	}

	public void renderReferencedType(String moduleName, ReferencedASN1Type type, List<ModuleImportInformation> imports) {
		render("preHeader.ftl");
		render("encoder/encoderPackageDef.ftl",
				new Attribute("package", moduleName + ".coder"));

		renderImport(moduleName + ".om.*");
		for (ModuleImportInformation importInformation : imports) {
			renderImport(importInformation.getModuleName() + ".om." + importInformation.getTypeName());
			renderImport(importInformation.getModuleName() + ".coder." + importInformation.getTypeName() + "Encoder");
		}

		render("encoder/encoderReferencedType.ftl",
				new Attribute("className", type.getName() + "Encoder"),
				new Attribute("omClassName", type.getName()),
				new Attribute("superOMClassName", type.getReferencedName()),
				new Attribute("superClassName", type.getReferencedName() + "Encoder"));
	}

	public void renderFileHeader(String packageName) {
		render("preHeader.ftl");
		render("encoder/encoderPackageDef.ftl",
				new Attribute("package", packageName));
	}

	public void renderImport(String fileImport) {
		render("import.ftl",
				new Attribute("import", fileImport));
	}

	public void renderEncodeFunctionEncoderList() {
		render("encoder/encodeFunctionEncoderList.ftl");
	}

	public void renderEncodeFunctionEncoderListAddAggStart(AggregatedASN1Type type, String name) {
		CanonicalTypeNamingVisitor visitor = new CanonicalTypeNamingVisitor(true);
		type.accept(visitor);

		String typeInformationClass = visitor.getCanonicalName(new ChainedNamingStrategy(new PostfixNamingStrategy("TypeInformation"), InnerComponentNamingStrategy.getInstance()));
		if (typeInformationClass.length() == 0) {
			typeInformationClass = type.getName() + "TypeInformation";
		}
		
		render("encoder/encodeFunctionEncoderListAddAggStart.ftl",
				new Attribute("aggregatedType", ASN1ToJavaNameMapper.getMappedJavaName(type)),
				new Attribute("typeInformationClass", typeInformationClass),				
				new Attribute("componentTypeName", name));
	}

	public void renderEncodeFunctionEncoderListAddAggEnd(AbstractASN1Type componentType, String className) {
		CanonicalTypeNamingVisitor visitor = new CanonicalTypeNamingVisitor(true);
		componentType.accept(visitor);
		String typeInformationClass = visitor.getCanonicalName(new ChainedNamingStrategy(new PostfixNamingStrategy("TypeInformation"), InnerComponentNamingStrategy.getInstance()));
		
		if (typeInformationClass.length() == 0) {
			typeInformationClass = componentType.getName() + "TypeInformation";
		}

		render("encoder/encodeFunctionEncoderListAddAggEnd.ftl",
				new Attribute("className", className),
				new Attribute("typeInformationClass", typeInformationClass),				
				new Attribute("componentName", componentType.getName()));
	}

	public void renderEncodeFunctionEncoderListAddAggEndPrototype(AbstractASN1Type componentType, String className) {
		CanonicalTypeNamingVisitor visitor = new CanonicalTypeNamingVisitor(true);
		componentType.accept(visitor);
		String typeInformationClass = visitor.getCanonicalName(new ChainedNamingStrategy(new PostfixNamingStrategy("TypeInformation"), InnerComponentNamingStrategy.getInstance()));
		if (typeInformationClass.length() == 0) {
			typeInformationClass = componentType.getName() + "TypeInformation";
		}
		
		render("encoder/encodeFunctionEncoderListAddAggEndPrototype.ftl",
				new Attribute("className", className),
				new Attribute("typeInformationClass", typeInformationClass),				
				new Attribute("componentName", componentType.getName()));
	}

	public void renderEncodeFunctionEncoderListAddStart() {
		render("encoder/encodeFunctionEncoderListAddStart.ftl");
	}

	public void renderEncodeFunctionEncoderListAddEnd() {
		render("encoder/encodeFunctionEncoderListAddEnd.ftl");
	}

	public void renderEncodeFunctionEncoderComplexType(ComplexASN1Type theType) {
		render("encoder/encodeFunctionEncoderComplexType.ftl", new Attribute("complexType", ASN1ToJavaNameMapper.getMappedJavaName(theType)));
	}

	public void renderEncodeFunctionEncoderSimpleType(SimpleASN1Type theType) {
		render("encoder/encodeFunctionEncoderSimpleType.ftl", 
				new Attribute("simpleType", ASN1ToJavaNameMapper.getMappedJavaName(theType)),
				new Attribute("componentName", theType.getName()));
	}
	
	public void renderEncodeFunctionEncoderAggregatedTypeStart(AggregatedASN1Type theType) {
		render("encoder/encodeFunctionEncoderAggregatedTypeStart.ftl", 
				new Attribute("aggregatedType", ASN1ToJavaNameMapper.getMappedJavaName(theType)),
				new Attribute("omClassName", theType.getName()),
				new Attribute("componentName", theType.getName()));
	}

	public void renderEncodeFunctionEncoderAggregatedTypeEnd(AggregatedASN1Type theType) {
		render("encoder/encodeFunctionEncoderAggregatedTypeEnd.ftl", 
				new Attribute("aggregatedType", ASN1ToJavaNameMapper.getMappedJavaName(theType)),
				new Attribute("omClassName", theType.getName()),
				new Attribute("componentName", theType.getName()));
	}	

	public void renderEncodeFunctionEncoderListAddPrototype(AbstractASN1Type type, String className, String componentEncoderName, String componentName) {
		CanonicalTypeNamingVisitor visitor = new CanonicalTypeNamingVisitor(true);
		type.accept(visitor);

		String typeInformationClass = visitor.getCanonicalName(new ChainedNamingStrategy(new PostfixNamingStrategy("TypeInformation"), InnerComponentNamingStrategy.getInstance()));
		if (typeInformationClass.length() == 0) {
			typeInformationClass = type.getName() + "TypeInformation";
		}

		render("encoder/encodeFunctionEncoderListAddPrototype.ftl",
				new Attribute("componentEncoderName", componentEncoderName + "Encoder"),
				new Attribute("componentName", componentName),
				new Attribute("typeInformationClass", typeInformationClass),				
				new Attribute("className", className));
	}
	
	public void renderEncodeFunctionEncoderEnumTypeListAddPrototype(AbstractASN1Type type, String className, String componentEncoderName, String componentName) {
		CanonicalTypeNamingVisitor visitor = new CanonicalTypeNamingVisitor(true);
		type.accept(visitor);

		String typeInformationClass = visitor.getCanonicalName(new ChainedNamingStrategy(new PostfixNamingStrategy("TypeInformation"), InnerComponentNamingStrategy.getInstance()));
		if (typeInformationClass.length() == 0) {
			typeInformationClass = type.getName() + "TypeInformation";
		}

		visitor = new CanonicalTypeNamingVisitor();
		type.accept(visitor);
		String innerComponentName = visitor.getCanonicalName(InnerComponentNamingStrategy.getInstance());

		render("encoder/encodeFunctionEncoderEnumTypeListAddPrototype.ftl",
				new Attribute("componentEncoderName", componentEncoderName + "Encoder"),
				new Attribute("innerComponentName", innerComponentName),
				new Attribute("typeInformationClass", typeInformationClass));
	}

	public void renderEncodeFunctionEncoderListAdd(AbstractASN1Type type, String className, String componentEncoderName, String componentName) {
		CanonicalTypeNamingVisitor visitor = new CanonicalTypeNamingVisitor(true);
		type.accept(visitor);

		String typeInformationClass = visitor.getCanonicalName(new ChainedNamingStrategy(new PostfixNamingStrategy("TypeInformation"), InnerComponentNamingStrategy.getInstance()));
		if (typeInformationClass.length() == 0) {
			typeInformationClass = type.getName() + "TypeInformation";
		}
		
		render("encoder/encodeFunctionEncoderListAdd.ftl",
				new Attribute("componentEncoderName", componentEncoderName + "Encoder"),
				new Attribute("componentName", componentName),
				new Attribute("typeInformationClass", typeInformationClass),
				new Attribute("className", className));
	}

	public void renderEncodeFunctionEncoderInnerComplexTypeListAdd(ComplexASN1Type type, String className, String componentEncoderName, String componentName) {
		CanonicalTypeNamingVisitor visitor = new CanonicalTypeNamingVisitor(true);
		type.accept(visitor);

		String typeInformationClass = visitor.getCanonicalName(new ChainedNamingStrategy(new PostfixNamingStrategy("TypeInformation"), InnerComponentNamingStrategy.getInstance()));
		if (typeInformationClass.length() == 0) {
			typeInformationClass = type.getName() + "TypeInformation";
		}

		visitor = new CanonicalTypeNamingVisitor();
		type.accept(visitor);

		String encoderClass = visitor.getCanonicalName(new ChainedNamingStrategy(new PostfixNamingStrategy("Encoder"), InnerComponentNamingStrategy.getInstance()));
		if (encoderClass.length() == 0) {
			encoderClass = type.getName() + "Encoder";
		}
		
		render("encoder/encodeFunctionEncoderInnerComplexTypeListAdd.ftl",
				new Attribute("componentEncoderName", encoderClass),
				new Attribute("componentName", componentName),
				new Attribute("typeInformationClass", typeInformationClass),
				new Attribute("className", className));
	}
	
	public void renderEncodeFunctionEncoderInnerComplexTypeListAddPrototype(ComplexASN1Type type, String className, String componentEncoderName, String componentName) {
		CanonicalTypeNamingVisitor visitor = new CanonicalTypeNamingVisitor(true);
		type.accept(visitor);

		String typeInformationClass = visitor.getCanonicalName(new ChainedNamingStrategy(new PostfixNamingStrategy("TypeInformation"), InnerComponentNamingStrategy.getInstance()));
		if (typeInformationClass.length() == 0) {
			typeInformationClass = type.getName() + "TypeInformation";
		}

		visitor = new CanonicalTypeNamingVisitor();
		type.accept(visitor);

		String encoderClass = visitor.getCanonicalName(new ChainedNamingStrategy(new PostfixNamingStrategy("Encoder"), InnerComponentNamingStrategy.getInstance()));
		if (encoderClass.length() == 0) {
			encoderClass = type.getName() + "Encoder";
		}
		
		render("encoder/encodeFunctionEncoderInnerComplexTypeListAddPrototype.ftl",
				new Attribute("componentEncoderName", encoderClass),
				new Attribute("componentName", componentName),
				new Attribute("typeInformationClass", typeInformationClass),
				new Attribute("className", className));
	}

	public void renderEncodeFunctionEncoderEnumTypeListAdd(AbstractASN1Type type, String className, String componentEncoderName, String componentName) {
		CanonicalTypeNamingVisitor visitor = new CanonicalTypeNamingVisitor(true);
		type.accept(visitor);

		String typeInformationClass = visitor.getCanonicalName(new ChainedNamingStrategy(new PostfixNamingStrategy("TypeInformation"), InnerComponentNamingStrategy.getInstance()));
		if (typeInformationClass.length() == 0) {
			typeInformationClass = type.getName() + "TypeInformation";
		}
		String innerComponentName = InnerComponentNamingStrategy.getInstance().getName(componentName, visitor.getNestingDepth());

		render("encoder/encodeFunctionEncoderEnumTypeListAdd.ftl",
				new Attribute("componentEncoderName", componentEncoderName + "Encoder"),
				new Attribute("innerComponentName", innerComponentName),
				new Attribute("componentName", componentName),
				new Attribute("typeInformationClass", typeInformationClass),				
				new Attribute("className", className));
	}
	
	public void renderEnum(AbstractASN1Type theType, boolean isInnerClass) {
		CanonicalTypeNamingVisitor visitor = new CanonicalTypeNamingVisitor();
		theType.accept(visitor);
		
		String omClassName = InnerComponentNamingStrategy.getInstance().getName(theType.getName(), visitor.getNestingDepth());
		String canonicalOMClassName = visitor.getCanonicalName(InnerComponentNamingStrategy.getInstance());
		if (canonicalOMClassName.length() == 0) {
			canonicalOMClassName = theType.getName();
		}
		
		render("encoder/encoderEnum.ftl",
				new Attribute("visibility", isInnerClass ? "private" : "public"),
				new Attribute("className", omClassName + "Encoder"),
				new Attribute("canonicalOMClassName", canonicalOMClassName));
	}	

	public void renderClassHeader(AbstractASN1Type theType, boolean isInnerClass) {
		CanonicalTypeNamingVisitor visitor = new CanonicalTypeNamingVisitor();
		theType.accept(visitor);
		
		String omClassName = InnerComponentNamingStrategy.getInstance().getName(theType.getName(), visitor.getNestingDepth());
		String canonicalOMClassName = visitor.getCanonicalName(InnerComponentNamingStrategy.getInstance());

		// For aggregated type
		if (canonicalOMClassName.length() == 0) {
			canonicalOMClassName = omClassName;
		}

		render("encoder/encoderClassHeader.ftl",
				new Attribute("visibility", isInnerClass ? "private" : "public"),
				new Attribute("className", omClassName + "Encoder"),
				new Attribute("canonicalOMClassName", canonicalOMClassName));
	}

	public void renderClassConstructor(AbstractASN1Type theType) {
		CanonicalTypeNamingVisitor visitor = new CanonicalTypeNamingVisitor();
		theType.accept(visitor);

		String omClassName = InnerComponentNamingStrategy.getInstance().getName(theType.getName(), visitor.getNestingDepth());
		String canonicalOMClassName = visitor.getCanonicalName(InnerComponentNamingStrategy.getInstance());
		// For aggregated type
		if (canonicalOMClassName.length() == 0) {
			canonicalOMClassName = omClassName;
		}
		
		render("encoder/encoderClassConstructor.ftl",
				new Attribute("className", omClassName + "Encoder"),
				new Attribute("canonicalOMClassName", canonicalOMClassName));
	}

	public void renderClassFooter(AbstractASN1Type theType) {
		render("encoder/encoderClassFooter.ftl");
	}
}