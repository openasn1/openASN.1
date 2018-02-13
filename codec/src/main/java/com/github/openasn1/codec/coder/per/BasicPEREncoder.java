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
 *   along with openASN.1. If not, see <http://www.gnu.com.github.licenses/>.
 * 
 */
package com.github.openasn1.codec.coder.per;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.github.openasn1.codec.coder.TagClass;
import com.github.openasn1.codec.coder.typecoder.TypeEncoder;
import com.github.openasn1.codec.coder.typecoder.TypeEncoderComparator;
import com.github.openasn1.codec.coder.typecoder.TypeInformation;
import com.github.openasn1.codec.util.bit.BitOutputStream;


/**
 * @author Marc Weyland
 *
 */
public class BasicPEREncoder extends PEREncoder {
	public BasicPEREncoder(OutputStream outputStream, boolean octetAligned) {
		super(outputStream, octetAligned);
	}
	
	public BasicPEREncoder(BitOutputStream bitOutputStream, boolean octetAligned) {
		super(bitOutputStream, octetAligned);
	}
	
	/**
	 * Encoding of the set type.
	 * 
	 * Components of the extension root are ordered according to the
	 * canonical order. (Note: there might be two parts if two extension
	 * markers are present). If one component is an untagged CHOICE then the
	 * component will adopt the tag of the component with the smallest tag in the
	 * "RootAlternativeTypeList" of the CHOICE type.
	 * After ordering the set is encoded as a sequence type. 
	 * The set elements that occur in the "ExtensionAdditionList" shall be 
	 * encoded as though they were components of a sequence type.
	 * 
	 * TODO: If one component is an untagged CHOICE then the component will adopt 
	 * 		 the tag of the component with the smallest tag in the "RootAlternativeTypeList" 
	 * 		 of the CHOICE type.
	 * 
	 * @see "X.680-0207 8.6"
	 * @see "X.691-0207 20"
	 */
	public void encodeAsSet(List<TypeEncoder> componentEncoderList, TypeInformation typeInformation) throws IOException {
		ArrayList<ArrayList<TypeEncoder>> sortedLists = new ArrayList<ArrayList<TypeEncoder>>();
		
		/**
		 * Iterate over all tag classes in the following order defined by the enum:
		 * UNIVERSAL, APPLICATION, context-specific, PRIVATE.
		 */
		for (TagClass tagClass : TagClass.values()) {
			ArrayList<TypeEncoder> encoderList = new ArrayList<TypeEncoder>();
			
			/**
			 * Iterate over all type encoders. Visit only root components.
			 */
			for (TypeEncoder typeEncoder : componentEncoderList) {
				if (typeEncoder.getTypeInformation().getTagList().size() > 0) {
					/**
					 * The canonical order for tags is based on the outermost tag of each type.
					 * So we use .get(0) to receive the outermost tag.
					 */
					if (! typeEncoder.getTypeInformation().isExtensionAddition()) {
						if (typeEncoder.getTypeInformation().getTagList().get(0).getTagClass().equals(tagClass)) {
							encoderList.add(typeEncoder);
						}
					}
				}
			}
			Collections.sort(encoderList, TypeEncoderComparator.getInstance());
			
			sortedLists.add(encoderList);
		}
		
		ArrayList<TypeEncoder> finalList = new ArrayList<TypeEncoder>();
		
		for (ArrayList<TypeEncoder> sortedList : sortedLists) {
			for (TypeEncoder encoder : sortedList) {
				finalList.add(encoder);
			}
		}

		/**
		 * Iterate over all type encoders. Visit only extension components.
		 */
		for (TypeEncoder typeEncoder : componentEncoderList) {
			if (typeEncoder.getTypeInformation().isExtensionAddition()) {
				finalList.add(typeEncoder);
			}
		}

		encodeAsSequence(finalList, typeInformation);
	}
	
	/**
	 * Encoding of the set-of type.
	 * 
	 * Note: For BASIC-PER the set-of shall be encoded as if it had been declared a sequence-of type.
	 * 
	 * @see "X.691-0207 21"
	 */
	public <T> void encodeAsSetOf(TypeEncoder<T> encoderPrototype, List<T> elements, TypeInformation typeInformation) throws IOException {
		encodeAsSequenceOf(encoderPrototype, elements, typeInformation);
	}	
}