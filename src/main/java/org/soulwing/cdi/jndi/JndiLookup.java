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

import javax.naming.NamingException;

/**
 * An object that performs JNDI lookups.
 *
 * @author Carl Harris
 */
interface JndiLookup {

  /**
   * Looks up the given name using the JNDI initial context.
   * @param name the name to look up
   * @return the object bound to {@link name}
   */
  Object lookup(String name) throws NamingException;
  
}
