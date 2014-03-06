/**
 * 
 */
package com.brimud.tools;

import com.sun.xml.bind.marshaller.NamespacePrefixMapper;

/**
 * @author dan
 * 
 */
public class WorldPrefixMapper extends NamespacePrefixMapper {
  private int prefixIndex = 2;

  @Override
  public String getPreferredPrefix(String namespaceUri, String suggestion, boolean required) {
    final String desiredPrefix;
    if ("http://brimud.com/world".equals(namespaceUri)) {
      desiredPrefix = "";
    } else if ("http://brimud.com/zone".equals(namespaceUri)) {
      desiredPrefix = "zone";
    } else if ("http://www.w3.org/2001/XInclude".equals(namespaceUri)) {
      desiredPrefix = "xi";
    } else {
      desiredPrefix = "n" + prefixIndex++;
    }
    return desiredPrefix;
  }
}
