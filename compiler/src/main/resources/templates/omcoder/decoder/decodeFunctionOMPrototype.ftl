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
<#if enclosingClassName!="">
		<#assign index = 0>
		<#list enclosingClassNames as name>
			<#assign index = index + 1>
			<#if index == 1>
				<#assign cumulatedName = name>
				<#assign instanceCreator = name>				
		${cumulatedName} enclosingInstance${index} = new ${name}();
			<#else>
				<#assign cumulatedName = cumulatedName + "." + name>			
				<#assign instanceCreator = "enclosingInstance" + (index-1)>				
		${cumulatedName} enclosingInstance${index} = ${instanceCreator}.new ${name}();
			</#if>
		</#list>
		${canonicalOMClassName} decodedValue = enclosingInstance${index}.new ${className}();
<#else>
		${canonicalOMClassName} decodedValue = new ${className}();
</#if>
