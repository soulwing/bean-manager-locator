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

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Set;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.UnsatisfiedResolutionException;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;

import org.soulwing.cdi.SimpleBeanManager;

/**
 * A {@link SimpleBeanManager} that delegates to a {@link BeanManager}.
 *
 * @author Carl Harris
 */
public class DelegatingSimpleBeanManager implements SimpleBeanManager {

  private final BeanManager delegate;
    
  /**
   * Constructs a new instance.
   * @param delegate the bean manager delegate
   */
  public DelegatingSimpleBeanManager(BeanManager delegate) {
    this.delegate = delegate;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public BeanManager getDelegate() {
    return delegate;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public <T> T getBean(Class<T> type, Annotation... qualifiers) {
    Set<Bean<?>> beans = delegate.getBeans(type, qualifiers);
    if (beans.isEmpty()) {
      throw new UnsatisfiedResolutionException("no qualifying beans of type "
          + type.getName());
    }
    Bean<?> bean = delegate.resolve(hasAlternative(beans) ? 
        alternatives(beans) : beans);
    return getReference(bean, type);
  }

  private boolean hasAlternative(Set<Bean<?>> beans) {
    for (Bean<?> bean : beans) {
      if (bean.isAlternative()) return true;
    }
    return false;
  }
  
  private Set<Bean<?>> alternatives(Set<Bean<?>> beans) {
    Set<Bean<?>> alternatives = new HashSet<>();
    for (Bean<?> bean : beans) {
      if (!bean.isAlternative()) continue;
      alternatives.add(bean);
    }
    return alternatives;
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public <T> Set<T> getBeans(Class<T> type, Annotation... qualifiers) {
    Set<Bean<?>> beans = delegate.getBeans(type, qualifiers); 
    Set<T> references = new HashSet<>();
    for (Bean<?> bean : beans) {
      references.add(getReference(bean, type));
    }
    return references;
  }

  /**
   * Gets a reference to a given bean.
   * @param bean the subject bean
   * @param type the target type
   * @return reference to the bean
   */
  @SuppressWarnings("unchecked")
  private <T> T getReference(Bean<?> bean, Class<T> type) {
    CreationalContext<?> cc = delegate.createCreationalContext(bean);
    return (T) delegate.getReference(bean, type, cc);
  }

}
