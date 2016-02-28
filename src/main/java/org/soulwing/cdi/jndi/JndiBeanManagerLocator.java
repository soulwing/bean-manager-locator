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

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.enterprise.inject.spi.BeanManager;
import javax.naming.NameNotFoundException;
import javax.naming.NamingException;

import org.soulwing.cdi.BeanManagerLocator;
import org.soulwing.cdi.DelegatingSimpleBeanManager;
import org.soulwing.cdi.NoBeanManagerException;
import org.soulwing.cdi.SimpleBeanManager;

/**
 * A singleton JNDI-based {@link BeanManagerLocator}.
 *
 * @author Carl Harris
 */
public class JndiBeanManagerLocator implements BeanManagerLocator {

  public static final String BEAN_MANAGER = "java:comp/BeanManager";

  public static final String ALT_BEAN_MANAGER = "java:comp/env/BeanManager";

  private final Lock lookupLock = new ReentrantLock();

  private final JndiLookup jndiLookup;

  private volatile Optional<BeanManager> beanManagerHolder;

  private static final Lock factoryMethodLock = new ReentrantLock();

  private static volatile JndiBeanManagerLocator instance;

  /**
   * Constructs a new instance.
   */
  private JndiBeanManagerLocator() {
    this(new InitialContextJndiLookup());
  }
  
  /**
   * Constructs a new instance.
   * @param jndiLookup lookup that will be used to locate the bean manager
   */
  protected JndiBeanManagerLocator(JndiLookup jndiLookup) {
    this.jndiLookup = jndiLookup;
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public BeanManager getBeanManager() {
    if (beanManagerHolder == null) {
      lookupLock.lock();
      try {
        if (beanManagerHolder == null) {
          beanManagerHolder = new Optional<>(locateBeanManager());
        }
      }
      finally {
        lookupLock.unlock();
      }
    }
    if (!beanManagerHolder.isPresent()) {
      throw new NoBeanManagerException();
    }
    return beanManagerHolder.get();
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public SimpleBeanManager getSimpleBeanManager() {
    return new DelegatingSimpleBeanManager(getBeanManager());
  }
  
  /**
   * Attempts to locate the bean manager using the standard and alternate
   * JNDI names for it. 
   * @return bean manager or {@code null} if not found
   */
  private BeanManager locateBeanManager() {
    BeanManager beanManager = lookupBeanManager(BEAN_MANAGER);
    if (beanManager == null) {
      beanManager = lookupBeanManager(ALT_BEAN_MANAGER);
    }
    return beanManager;
  }
  
  /**
   * Attempts to locate the bean manager using the given JNDI name.
   * @param name the name to lookup
   * @return bean manager or {@code null} if not found
   */
  private BeanManager lookupBeanManager(String name) {
    try {
      return (BeanManager) jndiLookup.lookup(name);
    }
    catch (NameNotFoundException ex) {
      return null;
    }
    catch (NamingException ex) {
      throw new RuntimeException(ex);
    }
  }

  /**
   * Gets a reference to a shared static instance.
   * @return shared bean manager locator
   */
  public static JndiBeanManagerLocator getInstance() {
    if (instance == null) {
      factoryMethodLock.lock();
      try {
        instance = new JndiBeanManagerLocator();
      }
      finally {
        factoryMethodLock.unlock();
      }
    }
    return instance;
  }

}
