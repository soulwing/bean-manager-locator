/*
 * File created on Apr 14, 2016
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

import javax.enterprise.util.AnnotationLiteral;

public class SomeQualifierLiteral extends AnnotationLiteral<SomeQualifier> {

  private static final long serialVersionUID = -7550879081741961176L;

  public static final SomeQualifierLiteral INSTANCE = 
      new SomeQualifierLiteral();
  
  private SomeQualifierLiteral() {    
  }
  
}