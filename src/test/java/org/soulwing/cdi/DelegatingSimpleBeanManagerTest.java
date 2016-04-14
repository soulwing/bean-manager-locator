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
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.sameInstance;
import static org.jmock.Expectations.returnValue;
import static org.jmock.Expectations.throwException;

import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.AmbiguousResolutionException;
import javax.enterprise.inject.UnsatisfiedResolutionException;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;

import org.jmock.Expectations;
import org.jmock.api.Action;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

/**
 * Unit tests of {@link DelegatingSimpleBeanManager}.
 *
 * @author Carl Harris
 */
public class DelegatingSimpleBeanManagerTest {

  private static final Annotation[] QUALIFIERS = new Annotation[0];

  private static final Object REF1 = new Object();
  
  private static final Object REF2 = new Object();
  
  private static final Set<Object> REFS = new HashSet<>();
  
  static {
    REFS.add(REF1);
    REFS.add(REF2);
  }
  
  @Rule
  public final JUnitRuleMockery context = new JUnitRuleMockery();
  
  @Mock
  private BeanManager delegate;
  
  @Mock
  private CreationalContext<?> creationalContext;
  
  @Mock
  private Bean<?> bean1;
  
  @Mock
  private Bean<?> bean2;
  
  private Set<Bean<?>> beans = new HashSet<>();
  
  private DelegatingSimpleBeanManager beanManager;
  
  @Before
  public void setUp() throws Exception {
    beanManager = new DelegatingSimpleBeanManager(delegate);
    beans.add(bean1);
    beans.add(bean2);
  }

  @Test
  public void testGetBean() throws Exception {
    context.checking(getBeansExpectations(Object.class, QUALIFIERS, 
        returnValue(Collections.singleton(bean1))));
    context.checking(beanExpectations(false));
    context.checking(resolveExpectations(Collections.singleton(bean1),
        returnValue(bean1)));
    context.checking(getReferenceExpectations(Object.class, bean1, 
        returnValue(REF1)));
    
    assertThat(beanManager.getBean(Object.class, QUALIFIERS), 
        is(sameInstance(REF1)));
  }
  
  @Test(expected = UnsatisfiedResolutionException.class)
  public void testGetBeanWhenNoneFound() throws Exception {
    context.checking(getBeansExpectations(Object.class, QUALIFIERS, 
        returnValue(Collections.emptySet())));    
    context.checking(beanExpectations(false));
    beanManager.getBean(Object.class, QUALIFIERS);
  }
  
  @Test(expected = RuntimeException.class)
  public void testGetBeanWhenMoreThanOneFound() throws Exception {
    context.checking(getBeansExpectations(Object.class, QUALIFIERS, 
        returnValue(beans)));
    context.checking(beanExpectations(false));
    context.checking(resolveExpectations(beans, 
        throwException(new AmbiguousResolutionException())));
    
    beanManager.getBean(Object.class, QUALIFIERS);
  }

  @Test
  public void testGetBeanWhenAlternativeFound() throws Exception {
    context.checking(getBeansExpectations(Object.class, QUALIFIERS, 
        returnValue(beans)));
    context.checking(beanExpectations(true));
    context.checking(resolveExpectations(Collections.singleton(bean2), 
        returnValue(bean2)));
    context.checking(getReferenceExpectations(Object.class, bean2, 
        returnValue(REF2)));

    assertThat(beanManager.getBean(Object.class, QUALIFIERS), 
        is(sameInstance(REF2)));
  }

  @Test
  public void testGetNamedBean() throws Exception {
    context.checking(new Expectations() {
      {
        oneOf(delegate).getBeans(DelegatingSimpleBeanManagerIT.BEAN_NAME);
        will(returnValue(Collections.singleton(bean1)));
      }
    });

    context.checking(beanExpectations(false));
    context.checking(resolveExpectations(Collections.singleton(bean1),
          returnValue(bean1)));
    context.checking(getReferenceExpectations(Object.class, bean1,
        returnValue(REF1)));

    assertThat(beanManager.getBean(DelegatingSimpleBeanManagerIT.BEAN_NAME, Object.class),
        is(sameInstance(REF1)));
  }

  @Test(expected = AmbiguousResolutionException.class)
  public void testGetNamedBeanWhenMoreThanOneFound() throws Exception {
    context.checking(new Expectations() {
      {
        oneOf(delegate).getBeans(DelegatingSimpleBeanManagerIT.BEAN_NAME);
        will(returnValue(beans));
      }
    });

    context.checking(beanExpectations(false));
    context.checking(resolveExpectations(beans,
        throwException(new AmbiguousResolutionException())));

    assertThat(beanManager.getBean(DelegatingSimpleBeanManagerIT.BEAN_NAME, Object.class),
        is(sameInstance(REF1)));
  }

  @Test
  public void testGetNamedBeanWhenAlternativeFound() throws Exception {
    context.checking(new Expectations() {
      {
        oneOf(delegate).getBeans(DelegatingSimpleBeanManagerIT.BEAN_NAME);
        will(returnValue(beans));
      }
    });
    context.checking(beanExpectations(true));
    context.checking(resolveExpectations(Collections.singleton(bean2),
        returnValue(bean2)));
    context.checking(getReferenceExpectations(Object.class, bean2,
        returnValue(REF2)));

    assertThat(beanManager.getBean(DelegatingSimpleBeanManagerIT.BEAN_NAME, Object.class),
        is(sameInstance(REF2)));
  }

  @Test
  public void testGetBeans() throws Exception {
    context.checking(getBeansExpectations(Object.class, QUALIFIERS, 
        returnValue(beans)));
    context.checking(getReferenceExpectations(Object.class, bean1, 
        returnValue(REF1)));
    context.checking(getReferenceExpectations(Object.class, bean2, 
        returnValue(REF2)));
    
    assertThat(beanManager.getBeans(Object.class, QUALIFIERS), 
        is(equalTo((REFS))));
  }
  
  private Expectations getBeansExpectations(final Class<?> type,
      final Annotation[] qualifiers, final Action outcome) throws Exception {
    return new Expectations() {
      {
        oneOf(delegate).getBeans(type, qualifiers);
        will(outcome);
      }
    };
  }

  @SuppressWarnings("unchecked")
  private Expectations resolveExpectations(final Set<? extends Bean<?>> beans,
      final Action outcome) {
    return new Expectations() {
      {
        oneOf(delegate).resolve((Set<Bean<?>>) beans);
        will(outcome);
      }
    };
  }
  
  private Expectations beanExpectations(final boolean bean2Alternative) 
      throws Exception {
    return new Expectations() {
      {
        allowing(bean1).isAlternative();
        will(returnValue(false));
        allowing(bean2).isAlternative();
        will(returnValue(bean2Alternative));
      }
    };
  }
  
  private Expectations getReferenceExpectations(final Class<?> type,
      final Bean<?> bean, final Action outcome) throws Exception {
    return new Expectations() {
      {
        oneOf(delegate).createCreationalContext(bean);
        will(returnValue(creationalContext));
        oneOf(delegate).getReference(bean, type, creationalContext);
        will(outcome);
      }
    };
  }
  


}
