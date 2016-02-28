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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.Matchers.sameInstance;
import static org.jmock.Expectations.returnValue;
import static org.jmock.Expectations.throwException;

import javax.enterprise.inject.spi.BeanManager;

import org.jmock.Expectations;
import org.jmock.api.Action;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.soulwing.cdi.BeanManagerLocator;
import org.soulwing.cdi.NoBeanManagerException;

/**
 * Unit tests for {@link JndiBeanManagerLocator}.
 *
 * @author Carl Harris
 */
public class JndiBeanManagerLocatorTest {

  @Rule
  public final JUnitRuleMockery context = new JUnitRuleMockery();
  
  @Mock
  private JndiLookup jndiLookup;
  
  @Mock
  private BeanManager beanManager;
    
  private BeanManagerLocator locator;
  
  @Before
  public void setUp() throws Exception {
    locator = new JndiBeanManagerLocator(jndiLookup);
  }
  
  @Test
  public void testGetBeanManagerWithStandardLookup() throws Exception {
    context.checking(jndiLookupExpectations(JndiBeanManagerLocator.BEAN_MANAGER, 
        returnValue(beanManager)));
    assertThat(locator.getBeanManager(), is(sameInstance(beanManager)));
  }
  
  @Test
  public void testGetBeanManagerWithAlternateLookup() throws Exception {
    context.checking(jndiLookupExpectations(JndiBeanManagerLocator.BEAN_MANAGER, 
        returnValue(null)));
    context.checking(jndiLookupExpectations(JndiBeanManagerLocator.ALT_BEAN_MANAGER, 
        returnValue(beanManager)));
    assertThat(locator.getBeanManager(), is(sameInstance(beanManager)));
  }
  
  @Test(expected = NoBeanManagerException.class)
  public void getGetBeanManagerWhenNotFound() throws Exception {
    context.checking(jndiLookupExpectations(JndiBeanManagerLocator.BEAN_MANAGER, 
        throwException(new NoBeanManagerException())));
    locator.getBeanManager();
  }
  
  @Test
  public void testGetSimpleBeanManager() throws Exception {
    context.checking(jndiLookupExpectations(JndiBeanManagerLocator.BEAN_MANAGER, 
        returnValue(beanManager)));
    assertThat(locator.getSimpleBeanManager().getDelegate(), 
        is(sameInstance(beanManager)));
    
  }
  
  private Expectations jndiLookupExpectations(final String name,
      final Action outcome) 
      throws Exception {
    return new Expectations() {
      {
        oneOf(jndiLookup).lookup(with(name));
        will(outcome);
      }
    };
  }

  @Test
  public void testGetInstance() throws Exception {
    final JndiBeanManagerLocator locator = JndiBeanManagerLocator.getInstance();
    assertThat(locator, is(not(nullValue())));
    assertThat(JndiBeanManagerLocator.getInstance(), is(sameInstance(locator)));
  }

}
