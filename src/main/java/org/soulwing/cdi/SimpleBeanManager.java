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
import java.util.Set;

import javax.enterprise.inject.AmbiguousResolutionException;
import javax.enterprise.inject.UnsatisfiedResolutionException;
import javax.enterprise.inject.spi.BeanManager;

/**
 * A simple bean manager.
 * <p>
 * This interface provides simplified access to the most common needs that
 * application code has from the bean manager; i.e. getting references to
 * bean instances.
 *
 * @author Carl Harris
 */
public interface SimpleBeanManager {

  /**
   * Gets the underlying {@link BeanManager}.
   * @return bean manager
   */
  BeanManager getDelegate();
  
  /**
   * Gets a reference to a bean of the given type.
   * @param type the type of bean desired
   * @param qualifiers qualifiers to constrain candidate beans
   * @return bean instance
   * @throws UnsatisfiedResolutionException if there is no qualifying bean
   *    of the given type
   * @throws AmbiguousResolutionException unless if there is more than one
   *    qualifying bean of the given type 
   */
  <T> T getBean(Class<T> type, Annotation... qualifiers);
  
  /**
   * Gets a set of references to beans of the given type.
   * @param type the type of beans desired
   * @param qualifiers qualifiers to constrain candidate beans
   * @return set of bean instances (which may be empty)
   */
  <T> Set<T> getBeans(Class<T> type, Annotation... qualifiers);

  /**
   * Gets a reference to a bean with the given name.
   * @param name name of the desired bean
   * @param type the type of beans desired
   * @return bean instance
   * @throws UnsatisfiedResolutionException if there is no qualifying bean
   *    of the given type
   * @throws AmbiguousResolutionException unless if there is more than one
   *    qualifying bean of the given type
   */
  <T> T getBean(String name, Class<T> type);

}
