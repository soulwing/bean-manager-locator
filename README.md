bean-manager-locator
====================

[![Build Status](https://travis-ci.org/soulwing/bean-manager-locator.svg?branch=master)](https://travis-ci.org/soulwing/bean-manager-locator)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/org.soulwing/bean-manager-locator/badge.svg)](http://search.maven.org/#search%7Cga%7C1%7Cg%3Aorg.soulwing%20a%3Abean-manager-locator*)

A JNDI service locator for the CDI `BeanManager`.

Sometimes, you want to use some CDI beans in classes that are dynamically 
created by another framework, and aren't subject to dependency injection.
For this reason, in a Java EE container, a reference to an application's 
`BeanManager` is stored as a JNDI reference named `java:comp/BeanManager`.

Performing JNDI lookups is always a bit of chore. This small library provides a 
convenient and efficient way to get a reference to the `BeanManager` without 
having to directly handle the required JNDI lookups in your code.


##### Tomcat Compatible

Tomcat does not allow the global namespace to be manipulated by a deployed
application. If you're using Weld for CDI support in an application that runs
in Tomcat, the JNDI name for the bean manager is `java:comp/env/BeanManager`.
This library knows how to find it there, too, and there's absolutely no 
additional configuration required!  


Maven Dependency
----------------

This small library is distributed via Maven Central. If you're using Maven to 
configure your build system, you can simply include this dependency.

```
<dependency>
  <groupId>org.soulwing</groupId>
  <artifactId>bean-manager-locator</artifactId>
  <version>1.0.0</version>
</dependency>
```

Using `JndiBeanManagerLocator`
------------------------------

The `JndiBeanManagerLocator` is fully thread safe so it can be shared across
any number of objects.  It has a `getInstance` method that is used to obtain
a reference to the singleton locator instance.

```
BeanManager beanManager = JndiBeanManagerLocator.getInstance().getBeanManager();
```

The locator implements the `BeanManagerLocator` interface. You can mock this
interface as needed to test code that utilizes the locator, without having to
run your code in a container that has an actual `BeanManager` instance available
as a JNDI reference.


Using `SimpleBeanManager`
-------------------------

The CDI `BeanManager` has a somewhat complicated API. Usually, when you want to
lookup the bean manager, it's because you just want to get a reference to some 
beans.  This library includes a `SimpleBeanManager` interface that makes getting
a reference to a bean a much simpler task.

To get a reference to a singleton bean that implements `MyService`:

```
SimpleBeanManager beanManager = JndiBeanManagerLocator.getInstance()
    .getSimpleBeanManager();

MyService = beanManager.getBean(MyService.class);
```

Those less commonly needed, you can also get a set of references to all beans 
of a given type, and you can specify qualifier annotation classes to be more
selective. For example, to get all beans of type `EncryptorService` that 
have the `AES` qualifier:

```
SimpleBeanManager beanManager = JndiBeanManagerLocator.getInstance()
    .getSimpleBeanManager();

MyService = beanManager.getBean(MyService.class);

Set<EncryptorService> encryptorServices = beanManager.getBeans(
    EncryptorService.class, AES.class);
```

