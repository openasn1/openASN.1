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
package com.github.openasn1.parser.utils;

import java.io.File;

/**
 * @author Clayton Hoss
 * 
 */
public class FileUtils {
	/**
	 * Deletes a directory recursivly
	 * 
	 * Shamelessly stolen from
	 * http://forum.java.sun.com/thread.jspa?threadID=563148&amp;messageID=2772354
	 * 
	 * @param dir is dir
	 * @return boolean value
	 */
	public static boolean deleteRecursive(File dir) {
		if (!dir.exists()) {
			return true;
		}
		boolean res = true;
		if (dir.isDirectory()) {
			File[] files = dir.listFiles();
			for (int i = 0; i < files.length; i++) {
				res &= FileUtils.deleteRecursive(files[i]);
			}
			res = dir.delete();// Delete dir itself
		} else {
			res = dir.delete();
		}
		return res;
	}

}
