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
	public static class ${componentName}TypeInformation {
		private static TypeInformation typeInformation = new TypeInformation();
		<#if isEnum>private static HashMap<${canonicalOMClassName}, EnumeratedItemTypeInformation> enumTypeMap = new HashMap<${canonicalOMClassName}, EnumeratedItemTypeInformation>();</#if>
		public static TypeInformation getTypeInformation() { return typeInformation; }
		<#if isEnum>public static HashMap<${canonicalOMClassName}, EnumeratedItemTypeInformation> getEnumTypeMap() { return enumTypeMap; }</#if>
		
		static {
		<#list tags as tag>
			getTypeInformation().setTagList(new ArrayList<Tag>());
			getTypeInformation().getTagList().add(new Tag(${tag.tagMode}, ${tag.tagClass}, ${tag.tagIndex}));
			<#if referencedType??>
			getTypeInformation().getTagList().addAll(${referencedType}TypeInformation.getTypeInformation().getTagList());
			</#if>
		</#list>
			getTypeInformation().setExtensible(${isExtensible});
			<#if isExtensionAddition=="true">
			getTypeInformation().setExtensionAddition(${isExtensionAddition});
			</#if>
			getTypeInformation().setOptional(${isOptional});
			<#if constraintTree??>
			
			<#if referencedType??>
			ConstraintSerialisation constraint = new ConstraintSerialisation();
			constraint.addConstraint(${referencedType}TypeInformation.getTypeInformation().getConstraint());
			constraint.addConstraint(${constraintTree});
			getTypeInformation().setConstraint(constraint);
			<#else>
			getTypeInformation().setConstraint(${constraintTree});
			</#if>
			<#else>
			<#if referencedType??>
			getTypeInformation().setConstraint(${referencedType}TypeInformation.getTypeInformation().getConstraint());
			</#if>
			</#if>
			<#if isEnum>
			
			${canonicalOMClassName} enumeration = null;
			<#list enumMap?keys as key>
			enumTypeMap.put(enumeration.${key}, new EnumeratedItemTypeInformation(false, ${enumMap[key]}));
			</#list>
			</#if>
		}
