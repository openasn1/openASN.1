<#--
    openASN.1 - an open source ASN.1 toolkit for java
 
    Copyright (C) 2007 Clayton Hoss, Marc Weyland
 
    This file is part of openASN.1
 
    openASN.1 is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as 
    published by the Free Software Foundation, either version 3 of 
    the License, or (at your option) any later version.
 
    openASN.1 is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
    GNU Lesser General Public License for more details.
 
    You should have received a copy of the GNU Lesser General Public License
    along with openASN.1. If not, see <http://www.gnu.org/licenses/>. 
-->
public final class ${className}TypeInformation {
	static private TypeInformation typeInformation;
	<#if isEnum>static private HashMap<${className}, EnumeratedItemTypeInformation> enumTypeMap = new HashMap<${className}, EnumeratedItemTypeInformation>();</#if>
	<#if isEnum>public static HashMap<${className}, EnumeratedItemTypeInformation> getEnumTypeMap() { return enumTypeMap; }</#if>
	
	private ${className}TypeInformation() { }
	
	/**
	 * Static class initialisation. 
	 * Fill and set type information
	 */
	static {
    	typeInformation = new TypeInformation();
    	
    	ArrayList<Tag> tagList = new ArrayList<Tag>();
    	<#list tags as tag>
		tagList.add(new Tag(${tag.tagMode}, ${tag.tagClass}, ${tag.tagIndex}));
		<#if referencedType??>
		tagList.addAll(${referencedType}TypeInformation.getTypeInformation().getTagList());
		</#if>		
		</#list>
    	typeInformation.setTagList(tagList);
		<#if isEnum>
		
		${className} enumeration = null;
		<#list enumMap?keys as key>
		enumTypeMap.put(enumeration.${key}, new EnumeratedItemTypeInformation(false, ${enumMap[key]}));
		</#list>
		</#if>
    	
    	typeInformation.setExtensible(${isExtensible});
		<#if constraintTree??>
		
		<#if referencedType??>
		ConstraintSerialisation constraint = new ConstraintSerialisation();
		constraint.addConstraint(${referencedType}TypeInformation.getTypeInformation().getConstraint());
		constraint.addConstraint(${constraintTree});
		typeInformation.setConstraint(constraint);
		<#else>
		typeInformation.setConstraint(${constraintTree});
		</#if>
		<#else>
		<#if referencedType??>
		typeInformation.setConstraint(${referencedType}TypeInformation.getTypeInformation().getConstraint());
		</#if>
		</#if>
	}

