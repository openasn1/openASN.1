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
package com.github.openasn1.codec.coder.per;

import static com.github.openasn1.codec.util.ASN1Constants.BITPATTERN_00001111;
import static com.github.openasn1.codec.util.ASN1Constants.BITPATTERN_00111111;
import static com.github.openasn1.codec.util.ASN1Constants.BITPATTERN_01000000;
import static com.github.openasn1.codec.util.ASN1Constants.BITPATTERN_10000000;
import static com.github.openasn1.codec.util.ASN1Constants.SIZE_16K;
import static com.github.openasn1.codec.util.ASN1Constants.SIZE_64K;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import com.github.openasn1.codec.coder.ASN1Decoder;
import com.github.openasn1.codec.coder.TagClass;
import com.github.openasn1.codec.coder.exceptions.DecoderException;
import com.github.openasn1.codec.coder.typecoder.EnumeratedItemTypeInformation;
import com.github.openasn1.codec.coder.typecoder.EnumeratedSortContainer;
import com.github.openasn1.codec.coder.typecoder.TypeDecoder;
import com.github.openasn1.codec.coder.typecoder.TypeDecoderComparator;
import com.github.openasn1.codec.coder.typecoder.TypeInformation;
import com.github.openasn1.codec.constraints.Constraint;
import com.github.openasn1.codec.constraints.character.BMPStringInfo;
import com.github.openasn1.codec.constraints.character.IA5StringInfo;
import com.github.openasn1.codec.constraints.character.NumericStringInfo;
import com.github.openasn1.codec.constraints.character.PrintableStringInfo;
import com.github.openasn1.codec.constraints.character.RestrictedCharacterStringInfo;
import com.github.openasn1.codec.constraints.character.VisibleStringInfo;
import com.github.openasn1.codec.constraints.visitor.PERIntegerSizeConstraintVisitor;
import com.github.openasn1.codec.constraints.visitor.PERRestrictedCharacterStringPermittedAlphabetConstraintVisitor;
import com.github.openasn1.codec.constraints.visitor.PERRestrictedCharacterStringSizeConstraintVisitor;
import com.github.openasn1.codec.util.MathUtil;
import com.github.openasn1.codec.util.bit.BasicBitInputStream;
import com.github.openasn1.codec.util.bit.BitField;
import com.github.openasn1.codec.util.bit.BitInputStream;
import com.github.openasn1.codec.util.bit.DynamicBitField;
import com.github.openasn1.codec.util.bit.StaticBitField;


/**
 * @author Marc Weyland
 *
 */
abstract public class PERDecoder implements ASN1Decoder {
	private BitInputStream _bitInputStream;

	private boolean isOctetAligned = true;
	
	/**
	 * Returns if the Encoder is octet aligned 
	 * 
	 * @return the alignment of this encoder
	 */
	public boolean isOctetAligned() {
		return isOctetAligned;
	}

	/**
	 * Sets the octet alignment of this encoder
	 * 
	 * @param octetAligned true if octet alignment should be kept
	 */
	public void setOctetAligned(boolean octetAligned) {
		this.isOctetAligned = octetAligned;
	}

	public PERDecoder(InputStream inputStream, boolean octetAligned) {
		this._bitInputStream = new BasicBitInputStream(inputStream);
		setOctetAligned(octetAligned);
	}

	protected BitInputStream getBitInputStream() {
		return this._bitInputStream;
	}

	protected void setBitInputStream(BitInputStream inputStream) {
		this._bitInputStream = inputStream;
	}

	public void setInputStream(InputStream inputStream) {
		this._bitInputStream = new BasicBitInputStream(inputStream);
	}
	
	public void finish() throws IOException { }	

	/**
	 * Decoding NULL value.
	 * 
	 * Note: Since there are no bits on the line we don't need decode anything
	 * 
	 * @param typeInformation is the type information parameter
	 * @see com.github.openasn1.codec.coder.ASN1Decoder#decodeNull(com.github.openasn1.codec.coder.typecoder.TypeInformation)
	 * @return Void.class is the class of Void type
	 */
	public Class<Void> decodeNull(TypeInformation typeInformation) throws IOException {
		/**
		 * Nothing to do here
		 */
		
		return Void.class;
	}

	public boolean decodeBoolean(TypeInformation typeInformation) throws IOException {
		return getBitInputStream().readBit() != 0;
	}

	/**
     * Decoding of a non-negative-binary-integer from a fixed size bit-field
     * 
     * Reads the integer from a field which is a fixed number of bits specified
     * by the 'bits' parameter
     * @param bits is the bits integer value
     * @see "X.691-0207 10.3.5"
     * @return decodeNonNegativeBinaryIntegerFromBits as method purpose
     * @throws IOException is the exception
     */
	protected int decodeNonNegativeBinaryIntegerFromBits(int bits) throws IOException {
		/**
         * Note: since we assume java integer encoding on a maximum of 32 bits
         * we read the encoded value into an int. If one needs to read larger
         * values, one can extend the functionality to return Java-"BigInt"s
         * instead. A bit-field which is larger than 32 bits will cause an
         * exception to be thrown.
         */
		if (bits < 1) {
			throw new DecoderException("The amount of bits used to encode the value must be greater than 0");
		}
		if (bits > 32) {
			throw new DecoderException("The amount of bits exceeds the codomain of an java-integer which is 32 bits.");
		}

		StaticBitField bitField = new StaticBitField(bits);

		for (int bitIndex = 0; bitIndex < bits; bitIndex++) {
			int readBit = getBitInputStream().readBit();
			bitField.setBit(bitIndex, readBit);
		}

		int value = bitField.getValue(0, bits - 1);

		return value;
	}

	/**
     * Decoding of a non-negative-binary-integer from a fixed size bit-field
     * 
     * Reads the integer from a field which is a fixed number of octets
     * specified by the 'octets' parameter. Only one or two octets are
     * permittet.
     * 
     * @param octets is integer value
     * @see "X.691-0207 10.3.5"
     * @throws IOException is the exception
     * @return decodeNonNegativeBinaryIntegerFromOctets as method purpose
     */
	protected int decodeNonNegativeBinaryIntegerFromOctets(int octets) throws IOException {
		if ((octets < 1) || (octets > 2)) {
			throw new DecoderException("The amount of octets used to decode the value must be 1 or 2");
		}

		int value = 0;

		if (octets < 2) {
			value = getBitInputStream().readByte();
		} else {
			value |= getBitInputStream().readByte() << 8;
			value |= getBitInputStream().readByte();
		}

		return value;
	}

	/**
     * Decoding a non-negative-binary-integer
     * 
     * Reads the integer from a field which is the minimum number of octets
     * needed to hold it.
     * 
     * @param octets is the value
     * @see "X.691-0207 10.3.6"
     * @throws IOException is the exception
     * @return decodeNonNegativeBinaryInteger as method purpose
     */
	protected int decodeNonNegativeBinaryInteger(int octets) throws IOException {
		/**
         * Note: since we assume java integer encoding on a maximum of 4 octets
         * we read the encoded value into an int. If one needs to read larger
         * values, one can extend the functionality to return Java-"BigInt"s
         * instead. A bit-field which is larger than 4 octets bits will cause an
         * exception to be thrown.
         */
		if (octets < 1) {
			throw new DecoderException("The amount of octets used to encode the value must be greater than 0");
		}
		if (octets > 4) {
			throw new DecoderException("The amount of octets exceeds the codomain of an java-integer which is 4 octets.");
		}

		DynamicBitField bitField = new DynamicBitField();

		for (int i = 0; i < octets; i++) {
			int octet = getBitInputStream().readByte();
			bitField.appendValue(octet, 8);
		}

		int value = bitField.getValue(0, octets * 8 - 1);

		return value;
	}

	/**
     * Decoding of a 2's-complement-binary-integer
     * 
     * A signed integer is read from a field that is the minumum number of
     * octets needed to hold it
     * 
     * @see "X.691-0207 10.4"
     * @param octets is octets
     * @throws IOException is the ecxception
     * @return int value
     */
	protected int decode2sComplementBinaryInteger(int octets) throws IOException {
		/**
         * Note: since we assume java integer encoding on a maximum of 4 octets
         * we read the encoded value into an int. If one needs to read larger
         * values, one can extend the functionality to return Java-"BigInt"s
         * instead. A bit-field which is larger than 4 octets bits will cause an
         * exception to be thrown.
         */
		if (octets < 1) {
			throw new DecoderException("The amount of octets used to encode the value must be greater than 0");
		}
		if (octets > 4) {
			throw new DecoderException("The amount of octets exceeds the codomain of an java-integer which is 4 octets.");
		}

		DynamicBitField bitField = new DynamicBitField();

		for (int i = 0; i < octets; i++) {
			int octet = getBitInputStream().readByte();
			bitField.appendValue(octet, 8);
		}

		int signBit = bitField.getBit(0);
		int positiveValue = bitField.getValue(1, octets * 8 - 1);
		int value = positiveValue - signBit * Math.abs((1 << (8 * octets - 1)));

		return value;
	}

	/**
     * Decoding of a normally small non-negative whole number.
     * 
     * Decoding of a whole number that is expected to be small, but whose size
     * is potentially unlimited due to the presence of an extension marker.
     * (e.g. a choice index)
     * 
     * @see "X.691-0207 10.6"
     * @throws IOException is the exception
     * @return int value
     */
	protected int decodeNormallySmallNonNegativeWholeNumber() throws IOException {
		/**
         * Note: flagBit may be 0 or 1.
         */
		int flagBit = getBitInputStream().readBit();

		int value = 0;

		if (flagBit == 0) {
			/**
             * Note: flagBit is 0 so the value is less than 64. We read the
             * value from a fixed size of 6 bits.
             */
			value = decodeNonNegativeBinaryIntegerFromBits(6);
		} else {
			/**
             * Note: flagBit is 1 so the value is larger or equal than 64.
             */
			value = decodeSemiConstrainedWholeNumber(0);
		}

		return value;
	}

	/**
     * Decoding of a semi-constrained whole number.
     * 
     * The offset from the lower bound (value-lowerBound) is read from the
     * minimum number of octets as a non-negative-binary-integer. The number of
     * octets is first decoded from a length field L
     * 
     * @see "X.691-0207 10.7"
     * @param lowerBound is lowerBound
     * @throws IOException is the exception
     * @return int value
     */
	protected int decodeSemiConstrainedWholeNumber(int lowerBound) throws IOException {
		/**
         * Note: since we assume java integer encoding on a maximum of 32 bits
         * we read the encoded value into an int. If one needs to read larger
         * values, one can extend the functionality to return Java-"BigInt"s
         * instead.
         */
		int octets = decodeLengthField(0);

		int offsetValue = decodeNonNegativeBinaryInteger(octets);

		return offsetValue + lowerBound;
	}

	/**
     * Deoding of an unconstrained whole number (indefinite length case)
     * 
     * The integer type has no lower bound. This results in decoding a
     * 2's-complement-binary-integer. Explicit length encoding is needed. The
     * decoded bit-field is not octet-aligned.
     * 
     * @see "X.691-0207 10.8"
     * @throws IOException is the exception
     * @return int value
     */
	protected int decodeUnconstrainedWholeNumber() throws IOException {
		/**
         * Note: since we assume java integer encoding on a maximum of 32 bits
         * we read the encoded value into an int. If one needs to read larger
         * values, one can extend the functionality to return Java-"BigInt"s
         * instead.
         */
		int octets = decodeLengthField(0);
		int value = decode2sComplementBinaryInteger(octets);

		return value;
	}

	protected void keepOctetAlignment() throws IOException {
		if (getBitInputStream().getPendingBits() > 0) {
			getBitInputStream().skipRemainingBits();
		}
	}

	synchronized protected void decodeOpenTypeField(TypeDecoder selectedAlternativeTypeDecoder) throws IOException {
		BitInputStream oldStream = getBitInputStream();

		finish();

		int completeFieldLength = decodeLengthField(0);

		byte[] inputData = new byte[completeFieldLength];
		for (int i = 0; i < completeFieldLength; i++) {
			inputData[i] = (byte) getBitInputStream().readByte();
		}

		ByteArrayInputStream inputStream = new ByteArrayInputStream(inputData);
		setBitInputStream(new BasicBitInputStream(inputStream));

		selectedAlternativeTypeDecoder.setDecodedValue(selectedAlternativeTypeDecoder.decode(this));

		finish();

		setBitInputStream(oldStream);
	}

	/**
     * Decoding of the octet string type. The decoding is a sequence of eight
     * bits.
     * 
     * @see "X.691-0207 16"
     * @param typeInformation is typeInformation
     * @throws IOException is the exception
     * @return byte[] value
     */
	public byte[] decodeOctetString(TypeInformation typeInformation) throws IOException {
		if (!typeInformation.hasConstraint()) {
			return decodeUnconstrainedOctetString(typeInformation);
		} else {
			return decodeConstrainedOctetString(typeInformation);
		}
	}

	public String decodeVisibleString(TypeInformation typeInformation) throws IOException {
		if (!typeInformation.hasConstraint()) {
			return decodeUnconstrainedRestrictedCharacterString(typeInformation, VisibleStringInfo.getInstance());
		} else {
			return decodeRestrictedCharacterString(typeInformation, VisibleStringInfo.getInstance());
		}
	}

	public String decodePrintableString(TypeInformation typeInformation) throws IOException {
		if (!typeInformation.hasConstraint()) {
			return decodeUnconstrainedRestrictedCharacterString(typeInformation, PrintableStringInfo.getInstance());
		} else {
			return decodeRestrictedCharacterString(typeInformation, PrintableStringInfo.getInstance());
		}
	}

	public String decodeNumericString(TypeInformation typeInformation) throws IOException {
		if (!typeInformation.hasConstraint()) {
			return decodeUnconstrainedRestrictedCharacterString(typeInformation, NumericStringInfo.getInstance());
		} else {
			return decodeRestrictedCharacterString(typeInformation, NumericStringInfo.getInstance());
		}
	}

	public String decodeIA5String(TypeInformation typeInformation) throws IOException {
		if (!typeInformation.hasConstraint()) {
			return decodeUnconstrainedRestrictedCharacterString(typeInformation, IA5StringInfo.getInstance());
		} else {
			return decodeRestrictedCharacterString(typeInformation, IA5StringInfo.getInstance());
		}
	}

	public String decodeBMPString(TypeInformation typeInformation) throws IOException {
		if (!typeInformation.hasConstraint()) {
			return decodeUnconstrainedRestrictedCharacterString(typeInformation, BMPStringInfo.getInstance());
		} else {
			return decodeRestrictedCharacterString(typeInformation, BMPStringInfo.getInstance());
		}
	}
	
	public List<Integer> decodeObjectIdentifier(TypeInformation typeInformation) throws IOException {
		ArrayList<Integer> objectIdentifier = new ArrayList<Integer>();

		/**
         * Read semi constrained length determinant describing amount of used
         * octets.
         * 
         * @see "X.691-0207 23"
         */
		int remainingOctets = decodeLengthField(0);

		/**
         * Keep octet alignment of the bit field in the ALIGNED variant.
         * 
         * @see "X.691-0207 23"
         */
		keepOctetAlignment();

		/**
         * Read the derived subidentifier:
         * 
         * be X the firstObjectIdentifierComponent be Y the
         * secondObjectIdentifierComponent
         * 
         * then the subidentifier will be derived like this: subidentifier =
         * (X*40) + Y
         * 
         * @see "X.690-0207 8.19.4"
         */
		BitField bitField = decodeObjectIdentifierSubIdentifier();
		int firstSubIdentifier = bitField.getValue();
		int firstObjectIdentifierComponent = 0;

		if (firstSubIdentifier < 40) {
			firstObjectIdentifierComponent = 0;
		} else if (firstSubIdentifier < 80) {
			firstObjectIdentifierComponent = 1;
		} else {
			firstObjectIdentifierComponent = 2;
		}

		int secondObjectIdentifierComponent = firstSubIdentifier - firstObjectIdentifierComponent * 40;

		objectIdentifier.add(firstObjectIdentifierComponent);
		objectIdentifier.add(secondObjectIdentifierComponent);

		/**
         * Calculate remaining octets
         */
		remainingOctets -= (int) Math.ceil(bitField.getSize() / 8.0f);

		/**
         * Read BER contents octets of the subsequent subidentifiers if present.
         * 
         * @see "X.690-0207 8.19.5"
         */
		while (remainingOctets > 0) {
			bitField = decodeObjectIdentifierSubIdentifier();
			int subIdentifier = bitField.getValue();
			objectIdentifier.add(subIdentifier);

			/**
             * Recalculate remaining octets
             */
			remainingOctets -= (int) Math.ceil(bitField.getSize() / 8.0f);
		}

		/**
         * TODO: handle fragmentation
         */

		return objectIdentifier;
	}

	private BitField decodeObjectIdentifierSubIdentifier() throws IOException {
		DynamicBitField bitField = new DynamicBitField();

		int leadingBit = 0;

		do {
			int octet = getBitInputStream().readByte();
			bitField.appendValue(octet, 7);

			leadingBit = octet >> 7;
		} while (leadingBit == 1);

		return bitField;
	}

	public String decodeGeneralString(TypeInformation typeInformation) throws IOException {
		throw new RuntimeException("Not yet implemented!");
	}

	private String decodeUnconstrainedRestrictedCharacterString(TypeInformation typeInformation, RestrictedCharacterStringInfo characterStringInfo) throws IOException {
		int neededCharacterBits = (int) Math.ceil(Math.log(characterStringInfo.getAlphabetSize()) / Math.log(2));
		int neededCharacterBitsPowerOf2 = MathUtil.findNextPowerOf2(neededCharacterBits);
		// int largestEncodableIndex = (int)Math.pow(2,
		// neededCharacterBitsPowerOf2) - 1;

		int length = decodeLengthField(0);

		keepOctetAlignment();

		StringBuffer buffer = new StringBuffer();

		for (int i = 0; i < length; i++) {
			int characterIndex = getBitInputStream().readBits(neededCharacterBitsPowerOf2);
			buffer.append(characterStringInfo.getCharacterFromIndex(characterIndex));
		}

		if (length >= SIZE_16K) {
			// Fragmentation
			buffer.append(decodeVisibleString(typeInformation));
		}

		return buffer.toString();
	}

	private String decodeRestrictedCharacterString(TypeInformation typeInformation, RestrictedCharacterStringInfo characterStringInfo) throws IOException {
		Constraint constraint = typeInformation.getConstraint();
		PERRestrictedCharacterStringPermittedAlphabetConstraintVisitor permittedAlphabetVisitor = new PERRestrictedCharacterStringPermittedAlphabetConstraintVisitor(null, characterStringInfo);
		PERRestrictedCharacterStringSizeConstraintVisitor sizeVisitor = new PERRestrictedCharacterStringSizeConstraintVisitor(null);
		constraint.accept(permittedAlphabetVisitor);
		constraint.accept(sizeVisitor);

		/**
         * If the type is extensible then we get a '0' bit if the length is
         * within the extension root. Otherwise we get a '1' bit. The type is
         * encoded as if it were unconstrained.
         * 
         * @see "X.691-0207 27.4"
         */
		if (sizeVisitor.isExtensible()) {
			int extensibleBit = getBitInputStream().readBit();

			if (extensibleBit == 1) {
				return decodeUnconstrainedRestrictedCharacterString(typeInformation, characterStringInfo);
			}
		}

		int neededCharacterBits = (int) Math.ceil(Math.log(permittedAlphabetVisitor.getAlphabetSize()) / Math.log(2));

		int neededCharacterBitsPowerOf2 = MathUtil.findNextPowerOf2(neededCharacterBits);
		int largestEncodableIndex = (int) Math.pow(2, neededCharacterBitsPowerOf2) - 1;

		boolean isAligned = true;
		boolean needLengthEncoding = true;
		if (sizeVisitor.hasConstrainedRootMaximum() && (sizeVisitor.getConstrainedRootMinimum() == sizeVisitor.getConstrainedRootMaximum()) && (sizeVisitor.getConstrainedRootMinimum() <= SIZE_64K)) {
			int maxStringSize = sizeVisitor.getConstrainedRootMaximum();

			if (maxStringSize * neededCharacterBitsPowerOf2 <= 16) {
				isAligned = false;
			}

			needLengthEncoding = false;
		}

		if (permittedAlphabetVisitor.getUpperAlphabetIndex() > largestEncodableIndex) {
			permittedAlphabetVisitor.orderCanonically();
		}
		
		int length = 0;
		
		if (needLengthEncoding) {
			if (sizeVisitor.hasConstrainedRootMaximum()) {
				length = decodeLengthField(sizeVisitor.getConstrainedRootMinimum(), sizeVisitor.getConstrainedRootMaximum());
			} else {
				length = decodeLengthField(sizeVisitor.getConstrainedRootMinimum());
			}
		} else {
			/**
             * No Length encoding so length is known. Minimum size defined by
             * the constraint has to be equal to maximum size. We take minimum
             * size here.
             */
			length = sizeVisitor.getConstrainedRootMinimum();
		}
		
		if (isAligned) {
			keepOctetAlignment();
		}

		StringBuffer buffer = new StringBuffer();

		if ((length >= 0) && (length < SIZE_16K)) {
			for (int i = 0; i < length; i++) {
				int characterIndex = getBitInputStream().readBits(neededCharacterBitsPowerOf2);
				buffer.append(permittedAlphabetVisitor.getCharacterFromIndex(characterIndex));
			}
		} else {
			/**
             * TODO: we can't do it this way because extensions would add wrong
             * extra information in every fragment.
             */
			if (length >= SIZE_16K) {
				// Fragmentation
				buffer.append(decodeVisibleString(typeInformation));
			}
		}

		return buffer.toString();
	}

	public int decodeInteger(TypeInformation typeInformation) throws IOException {
		if (typeInformation.hasConstraint()) {
			return decodeConstrainedInteger(typeInformation);
		} else {
			return decodeUnconstrainedInteger();
		}
	}

	private int decodeConstrainedInteger(TypeInformation typeInformation) throws IOException {
		Constraint constraint = typeInformation.getConstraint();

		/**
         * Evaluate the size constraint
         */
		PERIntegerSizeConstraintVisitor sizeVisitor = new PERIntegerSizeConstraintVisitor(null);
		constraint.accept(sizeVisitor);

		boolean hasConstrainedRootMinimum = sizeVisitor.hasConstrainedRootMinimum();
		boolean hasConstrainedRootMaximum = sizeVisitor.hasConstrainedRootMaximum();
		int constrainedRootMinimum = 0;
		int constrainedRootMaximum = 0;
		boolean isFixedSize = false;
		boolean isUpperBoundSet = false;

		if (hasConstrainedRootMinimum) {
			constrainedRootMinimum = sizeVisitor.getConstrainedRootMinimum();
		}
		if (hasConstrainedRootMaximum) {
			constrainedRootMaximum = sizeVisitor.getConstrainedRootMaximum();
		}

		/**
         * If the type is extensible we read a bit. If it is '0' then the value
         * is within the extension root. Otherwise it is '1'.
         * 
         * @see "X.691-0207 12.1"
         */
		if (sizeVisitor.isExtensible()) {
			boolean isValueInRoot = getBitInputStream().readBit() == 0;

			if (hasConstrainedRootMinimum && hasConstrainedRootMaximum) {
				isUpperBoundSet = (constrainedRootMaximum < SIZE_64K);
				isFixedSize = (constrainedRootMinimum == constrainedRootMaximum);
			} else if ((!hasConstrainedRootMinimum) && hasConstrainedRootMaximum) {
				isUpperBoundSet = (constrainedRootMaximum < SIZE_64K);
			}

			if (!isValueInRoot) {
				return decodeUnconstrainedInteger();
			}
		}

		/**
         * Constraint restricts to single value. No addition to the field list.
         * 
         * @see "X.691-0207 12.2.1"
         */
		if (isFixedSize) {
			return constrainedRootMinimum;
		}

		BitField bitField = null;

		if (hasConstrainedRootMinimum && hasConstrainedRootMaximum) {
			/**
             * Constraint restricts value to a constrained whole number.
             * 
             * @see "X.691-0207 12.2.2"
             */
			return decodeConstrainedWholeNumber(constrainedRootMinimum, constrainedRootMaximum);
		} else if (hasConstrainedRootMinimum && (!hasConstrainedRootMaximum)) {
			/**
             * Constraint restricts value to a semi constrained whole number.
             * 
             * @see "X.691-0207 12.2.3"
             */
			return decodeSemiConstrainedWholeNumber(constrainedRootMinimum);
		} else {
			/**
             * Decode value as unconstrained whole number
             * 
             * @see "X.691-0207 12.2.4"
             */
			return decodeUnconstrainedWholeNumber();
		}
	}

	private int decodeUnconstrainedInteger() throws IOException {
		return decodeUnconstrainedWholeNumber();
		/*
         * int firstOctet = getBitInputStream().readByte();
         * 
         * int usedOctets = firstOctet; int value = 0; // Note: usedOctets must
         * be <=4 for (int i = 0; i < usedOctets; i++) { value |=
         * getBitInputStream().readByte() << ((usedOctets-i-1)*8); }
         * 
         * return value;
         */
	}

	public void decodeSequence(List<TypeDecoder> componentDecoderList, TypeInformation typeInformation) throws IOException {
		// pseudocode
		// if extension marker readBit(1:values of extension additions are
		// present in this encoding|0)
		// foreach optionalOrDefaultComponent (read fragmented lengthencoding if
		// >= 64k):
		// readBit(1:ifPresent|0:ifAbsent)
		// decodeComponents
		// if hasExtensionAdditions
		// readCountOfExtensionAdditions (normally small number)
		// readPresenceBitMapOfThisExtensionAdditions
		// readExtensionAdditionsAsOpenTypeField

		ArrayList<TypeDecoder> rootComponentDecoderList = new ArrayList<TypeDecoder>();
		ArrayList<TypeDecoder> extensionComponentDecoderList = new ArrayList<TypeDecoder>();

		/**
         * Split up component list into root components and extension
         * components.
         */
		for (TypeDecoder typeDecoder : componentDecoderList) {
			if (typeDecoder.getTypeInformation().isExtensionAddition()) {
				extensionComponentDecoderList.add(typeDecoder);
			} else {
				rootComponentDecoderList.add(typeDecoder);
			}
		}

		boolean encodingHasEncodedExtenstionComponents = false;

		if (typeInformation.isExtensible()) {
			if (getBitInputStream().readBit() == 1) {
				encodingHasEncodedExtenstionComponents = true;
			}
		}

		/**
         * Interate over all root component type decoders and write default or
         * optional present bit
         */
		boolean shouldDecodeRootComponent[] = new boolean[rootComponentDecoderList.size()];
		int index = 0;
		for (TypeDecoder typeDecoder : rootComponentDecoderList) {
			if (typeDecoder.getTypeInformation().isOptional() || typeDecoder.getTypeInformation().hasDefaultValue()) {
				shouldDecodeRootComponent[index] = getBitInputStream().readBit() == 1;
			} else {
				shouldDecodeRootComponent[index] = true;
			}
			index++;
		}

		/**
         * Decode components of the extension root
         */
		index = 0;
		for (TypeDecoder<Object> typeDecoder : rootComponentDecoderList) {
			if (shouldDecodeRootComponent[index]) {
				typeDecoder.setDecodedValue(typeDecoder.decode(this));
			} else if (typeDecoder.getTypeInformation().hasDefaultValue()) {
				/**
                 * Component has a default value and was not sent. So we use
                 * this default value.
                 */
				typeDecoder.setDecodedValue(typeDecoder.getTypeInformation().getDefaultValue());
			}

			index++;
		}

		if (!encodingHasEncodedExtenstionComponents) {
			return;
		}

		/**
         * decode length of extension addition bitmap
         */
		int additionBitmapLength = decodeNormallySmallNonNegativeWholeNumber() + 1;
		
		/**
         * Iterate over all addition bitmap bits and read present or absent state
         * of extension additon encoding.
         * 
         * @see "X.680-0207 18.7"
         */
		boolean shouldDecodeAdditionComponent[] = new boolean[additionBitmapLength];
		for (int i = 0; i < additionBitmapLength; i++) {
			shouldDecodeAdditionComponent[i] = getBitInputStream().readBit() == 1;
		}


		BitInputStream oldStream = getBitInputStream();
		finish();

		for (int extensionAdditionIndex = 0; extensionAdditionIndex < additionBitmapLength; extensionAdditionIndex++) {
			if (shouldDecodeAdditionComponent[extensionAdditionIndex]) {
				/**
				 * Note: the encoded addition elements are encoded as an open type
				 */
				int completeFieldLength = decodeLengthField(0);
	
				byte[] inputData = new byte[completeFieldLength];
				for (int i = 0; i < completeFieldLength; i++) {
					inputData[i] = (byte) getBitInputStream().readByte();
				}
	
				ByteArrayInputStream inputStream = new ByteArrayInputStream(inputData);
				setBitInputStream(new BasicBitInputStream(inputStream));
	
				/**
				 * We only can decode values which we know. Values defined in later versions
				 * of the protocol are ignored.
				 */
				if (extensionAdditionIndex < extensionComponentDecoderList.size()) {
					TypeDecoder typeDecoder = extensionComponentDecoderList.get(extensionAdditionIndex);
					typeDecoder.setDecodedValue(typeDecoder.decode(this));
				}
	
				finish();

				setBitInputStream(oldStream);
			} else {
				if (extensionAdditionIndex < extensionComponentDecoderList.size()) {
					TypeDecoder typeDecoder = extensionComponentDecoderList.get(extensionAdditionIndex);

					if (typeDecoder.getTypeInformation().hasDefaultValue()) {
						/**
		                 * Component has a default value and was not sent. So we use
		                 * this default value.
		                 */
						typeDecoder.setDecodedValue(typeDecoder.getTypeInformation().getDefaultValue());
					}
				}
			}
		}	
	}

	/**
     * Decoding of the set type.
     * 
     * @see "X.680-0207 8.6"
     * @see "X.691-0207 20"
     */
	public void decodeSet(List<TypeDecoder> componentDecoderList, TypeInformation typeInformation) throws IOException {
		ArrayList<ArrayList<TypeDecoder>> sortedLists = new ArrayList<ArrayList<TypeDecoder>>();

		/**
         * Iterate over all tag classes in the following order defined by the
         * enum: UNIVERSAL, APPLICATION, context-specific, PRIVATE.
         */
		for (TagClass tagClass : TagClass.values()) {
			ArrayList<TypeDecoder> decoderList = new ArrayList<TypeDecoder>();

			/**
             * Iterate over all type encoders. Visit only root components.
             */
			for (TypeDecoder typeDecoder : componentDecoderList) {
				if (typeDecoder.getTypeInformation().getTagList().size() > 0) {
					/**
                     * The canonical order for tags is based on the outermost
                     * tag of each type. So we use .get(0) to receive the
                     * outermost tag.
                     */
					if (!typeDecoder.getTypeInformation().isExtensionAddition()) {
						if (typeDecoder.getTypeInformation().getTagList().get(0).getTagClass().equals(tagClass)) {
							decoderList.add(typeDecoder);
						}
					}
				}
			}
			Collections.sort(decoderList, TypeDecoderComparator.getInstance());

			sortedLists.add(decoderList);
		}

		ArrayList<TypeDecoder> finalList = new ArrayList<TypeDecoder>();

		for (ArrayList<TypeDecoder> sortedList : sortedLists) {
			for (TypeDecoder decoder : sortedList) {
				finalList.add(decoder);
			}
		}

		/**
         * Iterate over all type encoders. Visit only extension components.
         */
		for (TypeDecoder typeDecoder : componentDecoderList) {
			if (typeDecoder.getTypeInformation().isExtensionAddition()) {
				finalList.add(typeDecoder);
			}
		}

		decodeSequence(finalList, typeInformation);
	}

	public <T> List<T> decodeSequenceOf(TypeDecoder<T> decoderPrototype, TypeInformation typeInformation) throws IOException {
		if (typeInformation.hasConstraint()) {
			return decodeConstrainedSequenceOf(decoderPrototype, typeInformation);
		} else {
			return decodeUnconstrainedSequenceOf(decoderPrototype, typeInformation);
		}
	}

	public <T> List<T> decodeUnconstrainedSequenceOf(TypeDecoder<T> decoderPrototype, TypeInformation typeInformation) throws IOException {
		int components = decodeLengthField(0);

		ArrayList<T> decodedComponentList = new ArrayList<T>(components);

		for (int i = 0; i < components; i++) {
			decodedComponentList.add(decoderPrototype.decode(this));
		}

		if (components >= SIZE_16K) {
			decodedComponentList.addAll(decodeUnconstrainedSequenceOf(decoderPrototype, typeInformation));
		}

		return decodedComponentList;
	}

	public <T> List<T> decodeConstrainedSequenceOf(TypeDecoder<T> decoderPrototype, TypeInformation typeInformation) throws IOException {
		Constraint constraint = typeInformation.getConstraint();

		/**
         * Evaluate the size constraint
         */
		PERIntegerSizeConstraintVisitor sizeVisitor = new PERIntegerSizeConstraintVisitor(null);
		constraint.accept(sizeVisitor);

		boolean hasConstrainedRootMinimum = sizeVisitor.hasConstrainedRootMinimum();
		boolean hasConstrainedRootMaximum = sizeVisitor.hasConstrainedRootMaximum();
		int constrainedRootMinimum = 0;
		int constrainedRootMaximum = 0;
		boolean isFixedSize = false;
		boolean isUpperBoundSet = false;

		if (hasConstrainedRootMinimum) {
			constrainedRootMinimum = sizeVisitor.getConstrainedRootMinimum();
		}
		if (hasConstrainedRootMaximum) {
			constrainedRootMaximum = sizeVisitor.getConstrainedRootMaximum();
		}

		/**
         * If the type is extensible then we read a '0' bit if the component
         * count is within the extension root. Otherwise we read a '1' bit.
         * 
         * @see "X.691-0207 19.4"
         */
		if (sizeVisitor.isExtensible()) {
			boolean isValueInRoot = getBitInputStream().readBit() == 0;

			if (hasConstrainedRootMaximum) {
				isUpperBoundSet = (constrainedRootMaximum < SIZE_64K);
				isFixedSize = (constrainedRootMinimum == constrainedRootMaximum);
			}
			if (!isValueInRoot) {
				return decodeUnconstrainedSequenceOf(decoderPrototype, typeInformation);
			}
		}

		int components = constrainedRootMinimum;

		/**
         * Constraint restricts component count to single value
         * 
         * @see "X.691-0207 19.5"
         */
		if ((!isFixedSize) || (hasConstrainedRootMaximum && (constrainedRootMaximum >= SIZE_64K))) {
			if (hasConstrainedRootMinimum && hasConstrainedRootMaximum && isUpperBoundSet) {
				components = decodeLengthField(constrainedRootMinimum, constrainedRootMaximum);
			} else {
				components = decodeLengthField(0);
			}
		}

		ArrayList<T> decodedComponentList = new ArrayList<T>(components);

		for (int i = 0; i < components; i++) {
			decodedComponentList.add(decoderPrototype.decode(this));
		}

		if (components >= SIZE_16K) {
			// Fragment

			/**
             * TODO: correct fragmentation
             */

			throw new RuntimeException("Not yet implemented!");
		}

		return decodedComponentList;
	}

	public <T> List<T> decodeSetOf(TypeDecoder<T> decoderPrototype, TypeInformation typeInformation) throws IOException {
		/**
         * TODO: set-specific ordering.
         */
		return decodeSequenceOf(decoderPrototype, typeInformation);
	}

	/**
     * Encoding of the choice type
     * 
     * Note: Encoding of choice types are not affected by PER-visible
     * constraints.
     * 
     * TODO: If one component is an untagged CHOICE then the component will
     * adopt the tag of the component with the smallest tag in the
     * "RootAlternativeTypeList" of the CHOICE type.
     * 
     * @see "X.691-0207 22"
     */
	public synchronized void decodeChoice(List<TypeDecoder> componentDecoderList, TypeInformation typeInformation) throws IOException {
		TypeDecoder selectedAlternativeTypeDecoder = null;
		int decodingIndex = 0;

		ArrayList<TypeDecoder> sortedList = new ArrayList<TypeDecoder>();
		ArrayList<TypeDecoder> rootComponentDecoderList = new ArrayList<TypeDecoder>();
		ArrayList<TypeDecoder> extensionAdditionComponentDecoderList = new ArrayList<TypeDecoder>();

		/**
         * Find our selectedAlternativeTypeEncoder and partition the component
         * decoders into root- and extension addition lists.
         */
		for (TypeDecoder typeDecoder : componentDecoderList) {
			if (typeDecoder.getTypeInformation().isExtensionAddition()) {
				extensionAdditionComponentDecoderList.add(typeDecoder);
			} else {
				rootComponentDecoderList.add(typeDecoder);
			}
		}

		boolean extensionAdditionHasBeenEncoded = false;

		/**
         * Read "extension bit" if neccessary and decide if the encoded
         * component is an extension addition.
         * 
         * @see "X.691-0207 22.5"
         */
		if (typeInformation.isExtensible()) {
			extensionAdditionHasBeenEncoded = (getBitInputStream().readBit() == 1);
		}

		boolean shouldDecodeIndex = true;

		/**
         * If the choice has only one alternative in the extension root, there
         * shall be no encoding for the index if that alternative is chosen.
         * 
         * @see "X.691-0207 22.4"
         */
		if (rootComponentDecoderList.size() == 1 && (!extensionAdditionHasBeenEncoded)) {
			shouldDecodeIndex = false;
		}

		int encodingIndex = 0;

		/**
         * Decode the encoding index if present
         */
		if (shouldDecodeIndex) {
			if (!typeInformation.isExtensible() || (!extensionAdditionHasBeenEncoded)) {
				encodingIndex = decodeConstrainedWholeNumber(0, rootComponentDecoderList.size() - 1);
			} else {
				encodingIndex = decodeSemiConstrainedWholeNumber(0);
			}
		}

		if (!extensionAdditionHasBeenEncoded) {
			if (shouldDecodeIndex) {
				/**
                 * Our alternative component is no extension, so we need
                 * canonical ordering.
                 */

				/**
                 * Iterate over all tag classes in the following order defined
                 * by the enum: UNIVERSAL, APPLICATION, context-specific,
                 * PRIVATE.
                 */
				for (TagClass tagClass : TagClass.values()) {
					ArrayList<TypeDecoder> decoderList = new ArrayList<TypeDecoder>();

					/**
                     * Iterate over all type encoders. Visit only root
                     * components.
                     */
					for (TypeDecoder typeDecoder : componentDecoderList) {
						if (typeDecoder.getTypeInformation().getTagList().size() > 0) {
							/**
                             * The canonical order for tags is based on the
                             * outermost tag of each type. So we use .get(0) to
                             * receive the outermost tag.
                             */
							if (!typeDecoder.getTypeInformation().isExtensionAddition()) {
								if (typeDecoder.getTypeInformation().getTagList().get(0).getTagClass().equals(tagClass)) {
									decoderList.add(typeDecoder);
								}
							}
						}
					}
					Collections.sort(decoderList, TypeDecoderComparator.getInstance());

					sortedList.addAll(decoderList);
				}

				selectedAlternativeTypeDecoder = sortedList.get(encodingIndex);
			} else {
				/**
                 * We don't have an encoded index so the root has only one
                 * element. We take it.
                 */
				selectedAlternativeTypeDecoder = rootComponentDecoderList.get(0);
			}
		} else {
			/**
             * Our alternative component is an extension, so we don't need no
             * ordering.
             */

			selectedAlternativeTypeDecoder = extensionAdditionComponentDecoderList.get(encodingIndex);
		}

		/**
         * No "extension bit" present or selected alternative lies within the
         * extension root.
         * 
         * @see "X.691-0207 22.6, 22.7"
         */
		if (!typeInformation.isExtensible() || (!selectedAlternativeTypeDecoder.getTypeInformation().isExtensionAddition())) {
			selectedAlternativeTypeDecoder.setDecodedValue(selectedAlternativeTypeDecoder.decode(this));
			return;
		}

		/**
         * The "extension bit" is present and the selected alternative does not
         * lie within the extension root so we decode the selected alternative
         * as if it were the value of an open type field.
         * 
         * @see "X.691-0207 22.8"
         */
		decodeOpenTypeField(selectedAlternativeTypeDecoder);
	}

	/**
     * Encoding of a bounded above length determinant 
     *  
     * @see "X.691-0207 10.9.3 (ALIGNED -> not fully implemented)"
     * @see "X.691-0207 10.9.4 (UNALIGNED)"
     * @param lowerBound is lowerBound
     * @throws IOException is the exception
     * @return int value
     */
	protected int decodeLengthField(int lowerBound) throws IOException {
		// TODO: handle ALIGNED variant
		
		keepOctetAlignment();

		int length = 0;
		int firstOctet = getBitInputStream().readByte();

		if ((firstOctet & BITPATTERN_10000000) == 0) {
			length = firstOctet;
		} else if ((firstOctet & BITPATTERN_01000000) == 0) {
			// < 16k
			int secondOctet = getBitInputStream().readByte();
			length = ((firstOctet & BITPATTERN_00111111) << 8) | secondOctet;
		} else {
			// >=16k
			// Fragment
			int multiplier = firstOctet & BITPATTERN_00001111;
			length = SIZE_16K * multiplier;
		}

		return length;
	}

	public <T extends Enum> T decodeEnumerated(HashMap<T, EnumeratedItemTypeInformation> enumTypeMap, TypeInformation typeInformation) throws IOException {
		ArrayList<EnumeratedSortContainer> extensionRootContainerList = new ArrayList<EnumeratedSortContainer>();
		ArrayList<EnumeratedSortContainer> extensionAdditionContainerList = new ArrayList<EnumeratedSortContainer>();

		for (Enum enumerated : enumTypeMap.keySet()) {
			EnumeratedItemTypeInformation itemTypeInformation = enumTypeMap.get(enumerated);

			if ((itemTypeInformation != null) && itemTypeInformation.isExtensionAddition()) {
				extensionAdditionContainerList.add(new EnumeratedSortContainer(enumerated, itemTypeInformation.getValue()));
			} else {
				extensionRootContainerList.add(new EnumeratedSortContainer(enumerated, itemTypeInformation.getValue()));
			}
		}

		Collections.sort(extensionRootContainerList);
		/**
         * Extension additions are always defined in ascending order
         * 
         * @see "X.691-0207 13.1"
         */
		// Collections.sort(extensionAdditionContainerList);
		ArrayList<EnumeratedSortContainer> containerList = extensionRootContainerList;

		boolean isExtensionAddition = false;
		if (typeInformation.isExtensible()) {
			isExtensionAddition = getBitInputStream().readBit() == 1;

			if (isExtensionAddition) {
				containerList = extensionAdditionContainerList;
			}
		}

		int index = 0;

		if (isExtensionAddition) {
			/**
             * If an extension marker is present and the value lies not within
             * the extension root list.
             * 
             * @see "X.691-0207 13.3"
             */
			index = decodeSemiConstrainedWholeNumber(0);
		} else {
			/**
             * If no extension marker is present or the value lies within the
             * extension root list.
             * 
             * @see "X.691-0207 13.2"
             */

			index = decodeConstrainedWholeNumber(0, containerList.size() - 1);
		}

		int sortIndex = 0;
		for (EnumeratedSortContainer container : containerList) {
			if (sortIndex == index) {
				return (T) container.getEnumerated();
			}
			sortIndex++;
		}

		throw new RuntimeException("Decoding of enumerated type failed!");
	}

	protected byte[] decodeConstrainedOctetString(TypeInformation typeInformation) throws IOException {
		Constraint constraint = typeInformation.getConstraint();

		/**
         * Evaluate the size constraint
         */
		PERRestrictedCharacterStringSizeConstraintVisitor sizeVisitor = new PERRestrictedCharacterStringSizeConstraintVisitor(null);
		constraint.accept(sizeVisitor);

		boolean hasConstrainedRootMaximum = sizeVisitor.hasConstrainedRootMaximum();
		boolean hasConstrainedRootMinimum = sizeVisitor.hasConstrainedRootMinimum();

		int lowerBound = 0;
		if (hasConstrainedRootMinimum) {
			lowerBound = sizeVisitor.getConstrainedRootMinimum();
		}

		int upperBound = 0;
		if (hasConstrainedRootMaximum) {
			upperBound = sizeVisitor.getConstrainedRootMaximum();
		}

		boolean isWithinTheExtensionRoot = true;

		/**
         * If the type is extensible then we add a '0' bit if the length is
         * within the extension root. Otherwise we add a '1' bit. And encode the
         * type as if the extension marker is not present.
         * 
         * @see "X.691-0207 16.3"
         */
		if (sizeVisitor.isExtensible()) {
			isWithinTheExtensionRoot = getBitInputStream().readBit() == 0;
		}

		if (isWithinTheExtensionRoot && hasConstrainedRootMaximum) {
			/**
             * Octet string is constrained to zero length (ub = 0). No decoding
             * needed.
             * 
             * @see "X.691-0207 16.5"
             */
			if (upperBound == 0) {
				return null;
			}

			/**
             * Octet string is fixed length and the length is smaller or equal
             * to 2. The string will be decoded from "ub" octets.
             * 
             * @see "X.691-0207 16.6"
             */
			if ((lowerBound == upperBound) && (upperBound <= 2)) {
				byte[] data = new byte[upperBound];
				for (int i = 0; i < upperBound; i++) {
					data[i] = (byte) getBitInputStream().readByte();
				}
				return data;
			}

			/**
             * Octet string is fixed length and the length is greater than 2 but
             * smaller than 64k. The string will be decoded from "ub" octets.
             * (octet aligned).
             * 
             * @see "X.691-0207 16.7"
             */
			if ((lowerBound == upperBound) && (upperBound < SIZE_64K)) {
				keepOctetAlignment();

				byte[] data = new byte[upperBound];
				for (int i = 0; i < upperBound; i++) {
					data[i] = (byte) getBitInputStream().readByte();
				}
				return data;
			}
		}

		/**
         * Octet string will be encoded with a length determinant and maybe
         * fragmentation.
         * 
         * @see "X.691-0207 16.8"
         */
		int length = 0;

		if (hasConstrainedRootMaximum) {
			length = decodeLengthField(lowerBound, upperBound);
		} else {
			length = decodeLengthField(lowerBound);
		}

		if (length < SIZE_16K) {
			keepOctetAlignment();

			byte[] data = new byte[length];
			for (int i = 0; i < length; i++) {
				data[i] = (byte) getBitInputStream().readByte();
			}
			return data;
		}

		// Fragmentation
		throw new RuntimeException("Octet String fragmentation is not yet implemented!");
	}

	protected byte[] decodeUnconstrainedOctetString(TypeInformation typeInformation) throws IOException {
		int length = decodeLengthField(0);

		if (length < SIZE_16K) {
			keepOctetAlignment();

			byte[] data = new byte[length];
			for (int i = 0; i < length; i++) {
				data[i] = (byte) getBitInputStream().readByte();
			}

			return data;
		}

		// Fragmentation
		throw new RuntimeException("Octet String fragmentation is not yet implemented!");
	}

	/**
     * Encoding of a "fully" bounded length determinant (UNALIGNED variant)
     * 
     * @see "X.691-0207 10.9.4"
     * @param lowerBound is lowerBound
     * @param upperBound is upperBound
     * @throws IOException is the exception
     * @return int value 
     */
	protected int decodeLengthField(int lowerBound, int upperBound) throws IOException {
		if ((lowerBound == upperBound) && (lowerBound < SIZE_64K)) {
			/**
             * Note: we have no length encoding since the value has a codomain
             * of exactly 1 value. The lower bound is also less than 64K. We
             * return a length of 0 since the sender and receiver know this
             * value.
             */
			return 0;
		}
		
		if (upperBound < SIZE_64K) {
			return decodeConstrainedWholeNumber(lowerBound, upperBound);
			/**
             * Note: bits is a maximum of 16 since our lower bound is >= 0 and
             * our upper bound is 64K. So the value fits easily into an int.
             */
			/* int bits = (int) Math.ceil(Math.log(upperBound - lowerBound + 1) / Math.log(2));

			DynamicBitField bitField = new DynamicBitField();
			for (int i = 0; i < bits; i++) {
				int bit = getBitInputStream().readBit();
				bitField.appendBit(bit);
			}

			int offsetLength = bitField.getValue(0, bits - 1);

			return offsetLength + lowerBound;
			*/
		}

		return decodeLengthField(lowerBound);
	}

	/**
     * Decoding of a constrained whole number (ALIGNED variant)
     * 
     * Decoding on the minimum number of bits necessary for encoding the
     * interval range. There is no length Field L.
     * 
     * @see "X.691-0207 10.5.3"
     * @param lowerBound is lowerBound
     * @param upperBound is upperBound
     * @throws IOException is the exception
     * @return int value 
     */
	protected int decodeConstrainedWholeNumber(int lowerBound, int upperBound) throws IOException {
		int range = upperBound - lowerBound + 1;

		if (range == 1) {
			/**
             * Note: there is no value encoding since it is an empty bit-field.
             * The only possible value is taken either from the lower or from
             * the upper bound. (here: lowerBound).
             */
			return lowerBound;
		}

		/**
         * Note: since we assume java integer encoding on a maximum of 32 bits
         * we read the encoded value into an int. If one needs to read larger
         * values, one can extend the functionality to return Java-"BigInt"s
         * instead.
         */
		int bitsNeccessary = (int) Math.ceil(Math.log(range) / Math.log(2));
		int offsetValue = 0;

		// return offsetValue + lowerBound;

		if (range <= 255) {
			offsetValue = decodeNonNegativeBinaryIntegerFromBits(bitsNeccessary);
		} else if (range == 256) {
			keepOctetAlignment();
			offsetValue = decodeNonNegativeBinaryIntegerFromOctets(1);
		} else if (range <= SIZE_64K) {
			keepOctetAlignment();
			offsetValue = decodeNonNegativeBinaryIntegerFromOctets(2);
		} else {
			/**
             * indefinite length case
             * 
             * @see "X.691-0207 10.5.7.4"
             */
			int length = decodeLengthField(0);
			keepOctetAlignment();
			offsetValue = decodeNonNegativeBinaryIntegerFromBits(length);
		}

		return offsetValue + lowerBound;
	}

}
