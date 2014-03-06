/**
 * 
 */
package com.brimud.tools;

import com.sun.xml.bind.marshaller.NamespacePrefixMapper;

/**
 * @author dan
 * 
 */
public class ZonePrefixMapper extends NamespacePrefixMapper {

	private int prefixIndex = 2;

	@Override
	public String getPreferredPrefix(String namespaceUri, String suggestion,
			boolean required) {
		final String desiredPrefix;
		if ("http://brimud.com/zone".equals(namespaceUri)) {
			desiredPrefix = "";
		} else {
			desiredPrefix = "n" + prefixIndex++;
		}
		return desiredPrefix;
	}
}
