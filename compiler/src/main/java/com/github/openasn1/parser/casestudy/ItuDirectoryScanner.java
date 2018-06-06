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
package com.github.openasn1.parser.casestudy;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Clayton Hoss
 * 
 */
public class ItuDirectoryScanner extends DirectoryScanner {

	/**
	 * 
	 */
	public ItuDirectoryScanner() {
		super();
	}

	/**
	 * @param basepath
	 */
	public ItuDirectoryScanner(String basepath) {
		super(basepath);
	}

	/**
	 * @see com.github.openasn1.parser.casestudy.DirectoryScanner#modifyFileList(java.io.File[])
	 */
	@Override
	protected File[] modifyFileList(File[] files) {
		List<File> pkgs = new ArrayList<File>();
		List<String> pkgsnames = new ArrayList<String>();
		for (File f : files) {
			if (f.getAbsolutePath().endsWith("pkg.asn.zip")) {
				pkgs.add(f);
				pkgsnames.add(f.getAbsolutePath());
			}
		}
		for (File f : files) {
			String currentfilename = f.getAbsolutePath();
			if (currentfilename.endsWith(".asn.zip")
					&& (!pkgsnames.contains(currentfilename))) {
				if (!pkgsnames.contains(currentfilename.replace(".asn.zip",
						"pkg.asn.zip"))) {
					pkgs.add(f);
				}
			}
		}

		return pkgs.toArray(new File[0]);
	}
}
