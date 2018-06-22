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
package com.github.openasn1.compiler.Exceptions;

/**
 * @author Clayton Hoss
 * 
 */
public class DuplicateFileInPathException extends CompilationStoppingException {

	private String file1;

	private String file2;

	private static final long serialVersionUID = -2305033284211063901L;

	/**
	 * 
	 */
	public DuplicateFileInPathException() {
		super();
	}

	/**
	 * @param message is message
	 * @param cause is cause
	 */
	public DuplicateFileInPathException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param message is message
	 */
	public DuplicateFileInPathException(String message) {
		super(message);
	}

	/**
	 * @param cause is cause
	 */
	public DuplicateFileInPathException(Throwable cause) {
		super(cause);
	}

	/**
	 * @return the file1
	 */
	public String getFile1() {
		return this.file1;
	}

	/**
	 * @param file1 the file1 to set
	 */
	public void setFile1(String file1) {
		this.file1 = file1;
	}

	/**
	 * @return the file2
	 */
	public String getFile2() {
		return this.file2;
	}

	/**
	 * @param file2 the file2 to set
	 */
	public void setFile2(String file2) {
		this.file2 = file2;
	}

	/* (non-Javadoc)
	 * @see java.lang.Throwable#toString()
	 */
	@Override
	public String toString() {
		return super.toString() + " " + getFile1();
	}
	

}
