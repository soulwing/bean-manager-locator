/*
 * File created on Feb 28, 2016
 *
 * Copyright (c) 2016 Carl Harris, Jr
 * and others as noted
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.soulwing.cdi.jndi;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.Vector;

/**
 * A {@link ClassLoader} that ensures that only one beans.xml file appears to
 * be present.
 *
 * @author Carl Harris
 */
public class DelegatingBeansXmlClassLoader extends ClassLoader {

  private static final String BEANS_XML = "META-INF/beans.xml";
  
  private final ClassLoader delegate;

  /**
   * Constructs a new instance.
   * @param delegate
   */
  public DelegatingBeansXmlClassLoader(ClassLoader delegate) {
    this.delegate = delegate;
  }

  public int hashCode() {
    return delegate.hashCode();
  }

  public boolean equals(Object obj) {
    return delegate.equals(obj);
  }

  public String toString() {
    return delegate.toString();
  }

  public Class<?> loadClass(String name) throws ClassNotFoundException {
    return delegate.loadClass(name);
  }

  public URL getResource(String name) {
    return delegate.getResource(name);
  }

  public Enumeration<URL> getResources(String name) throws IOException {
    if (BEANS_XML.equals(name)) {
      Vector<URL> v = new Vector<URL>();
      v.add(getResource(BEANS_XML));
      return v.elements();
    }
    return delegate.getResources(name);
  }

  public InputStream getResourceAsStream(String name) {
    return delegate.getResourceAsStream(name);
  }

  public void setDefaultAssertionStatus(boolean enabled) {
    delegate.setDefaultAssertionStatus(enabled);
  }

  public void setPackageAssertionStatus(String packageName, boolean enabled) {
    delegate.setPackageAssertionStatus(packageName, enabled);
  }

  public void setClassAssertionStatus(String className, boolean enabled) {
    delegate.setClassAssertionStatus(className, enabled);
  }

  public void clearAssertionStatus() {
    delegate.clearAssertionStatus();
  }
  
}
