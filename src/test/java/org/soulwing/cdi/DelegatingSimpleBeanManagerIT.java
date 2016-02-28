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
package org.soulwing.cdi;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;

import org.hamcrest.MatcherAssert;
import org.jboss.weld.environment.se.Weld;
import org.jboss.weld.environment.se.WeldContainer;
import org.jboss.weld.literal.AnyLiteral;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.soulwing.cdi.DelegatingSimpleBeanManager;
import org.soulwing.cdi.jndi.AnotherService;
import org.soulwing.cdi.jndi.AnotherServiceAlternative;
import org.soulwing.cdi.jndi.DelegatingBeansXmlClassLoader;
import org.soulwing.cdi.jndi.SomeOtherServiceBean;
import org.soulwing.cdi.jndi.SomeQualifierLiteral;
import org.soulwing.cdi.jndi.SomeService;
import org.soulwing.cdi.jndi.SomeServiceBean;

/**
 * An integration test for {@link DelegatingSimpleBeanManager}
 *
 * @author Carl Harris
 */
public class DelegatingSimpleBeanManagerIT {

  private Weld weld;
  private WeldContainer container;
  private DelegatingSimpleBeanManager beanManager;
  
  @Before
  public void setUp() throws Exception {
    Thread.currentThread().setContextClassLoader(
        new DelegatingBeansXmlClassLoader(
            Thread.currentThread().getContextClassLoader()));
    weld = new Weld();
    container = weld.initialize();
    beanManager = new DelegatingSimpleBeanManager(container.getBeanManager());
  }

  @Test
  public void testGetBean() throws Exception {
    MatcherAssert.assertThat(beanManager.getBean(SomeService.class),
        is(instanceOf(SomeServiceBean.class)));
  }
  
  @Test
  public void testGetQualifiedBean() throws Exception {
    assertThat(beanManager.getBean(
        SomeService.class, SomeQualifierLiteral.INSTANCE),
        is(instanceOf(SomeOtherServiceBean.class)));
  }
  
  @Test
  public void testGetBeans() throws Exception {
    assertThat(
        beanManager.getBeans(SomeService.class, AnyLiteral.INSTANCE).size(),
        is(equalTo(2)));
  }

  @Test
  public void testGetAlternativeBean() throws Exception {
    MatcherAssert.assertThat(beanManager.getBean(AnotherService.class),
        is(instanceOf(AnotherServiceAlternative.class)));
  }
  

  @After
  public void tearDown() throws Exception {
    weld.shutdown();
  }
  
  
}
