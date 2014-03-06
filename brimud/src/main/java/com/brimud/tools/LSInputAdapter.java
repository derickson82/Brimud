/**
 * 
 */
package com.brimud.tools;

import java.io.InputStream;
import java.io.Reader;

import org.w3c.dom.ls.LSInput;

class LSInputAdapter implements LSInput {

  @Override
  public String getBaseURI() {
    return null;
  }

  @Override
  public InputStream getByteStream() {
    return null;
  }

  @Override
  public boolean getCertifiedText() {
    return false;
  }

  @Override
  public Reader getCharacterStream() {
    return null;
  }

  @Override
  public String getEncoding() {
    return null;
  }

  @Override
  public String getPublicId() {
    return null;
  }

  @Override
  public String getStringData() {
    return null;
  }

  @Override
  public String getSystemId() {
    return null;
  }

  @Override
  public void setBaseURI(String baseURI) {

  }

  @Override
  public void setByteStream(InputStream byteStream) {

  }

  @Override
  public void setCertifiedText(boolean certifiedText) {

  }

  @Override
  public void setCharacterStream(Reader characterStream) {

  }

  @Override
  public void setEncoding(String encoding) {

  }

  @Override
  public void setPublicId(String publicId) {

  }

  @Override
  public void setStringData(String stringData) {

  }

  @Override
  public void setSystemId(String systemId) {

  }

}