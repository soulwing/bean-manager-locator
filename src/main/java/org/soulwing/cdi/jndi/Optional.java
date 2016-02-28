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

/**
 * A container object which may or may not contain a non-null value. 
 *
 * @author Carl Harris
 */
class Optional<T> {

  private final T value;

  /**
   * Constructs a new instance.
   * @param value
   */
  public Optional(T value) {
    this.value = value;
  }
  
  /**
   * Gets the optional value.
   * @return value or {@code null} if not present
   */
  public T get() {
    return value;
  }
  
  /**
   * Tests whether there is a value present.
   * @return {@code true} if a value is present
   */
  public boolean isPresent() {
    return value != null;
  }
  
}
