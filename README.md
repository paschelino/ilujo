Ilujo is intended to become a Java web developer's toolkit
==========================================================

Note
----
This toolkit is in an early stage of development and provides no release yet. In case you're interested to help, you're
welcome ;-)

How to build this project
=========================
Currently this project requires a dependency, which itself is in an early stage of development:
https://github.com/bechte/junit-hierarchicalcontextrunner
Thus, in order to build, some things are required on your machine:
* JDK 1.7: http://www.oracle.com/technetwork/java/javase/downloads/index.html
* Gradle 1.8: http://www.gradle.org/installation (hint for Mac OS X users: brew install gradle)
* Maven 3.0.5: http://maven.apache.org/guides/getting-started/maven-in-five-minutes.html (hint for Max OS X users: brew install maven)

Then you need to install bechte's junit-hierarchicalcontextrunner:
* choose a place, where to clone his git repository: cd <YOUR_DIRECTORY>
* clone the repository: git clone git@github.com:bechte/junit-hierarchicalcontextrunner.git
* change into the cloned repos: cd junit-hierarchicalcontextrunner
* build and install it into your local maven repository: mvn install

If everything went fine, clone the ilujo project and try to execute some build tasks:
* clone the branch you need: git clone <THE_REQUIRED_ILUJO_BRANCH>
* change into the cloned repos: cd ilujo
* execute some task: gradle test

How to contribute
=================
There are always things to do:
* Taste the current API: An essential thing to provide a useful library is the availability of feedback...
* Develop a feature: See the TODOs below. In case you're interested in one of these, don't hesitate and go for it.
  Please try to contact me in any case in order to discuss a little bit, how the things you develop could be. I'm very
  open to good ideas, but still I have a direction in mind, where this library should evolve to.

Note
----
This library is developed with TDD. No other option. Untested code will never be accepted. Please forgive me the
strictness of these words. If you've never done TDD before, then this is your chance to learn it. ;-) No client's
pressure, no manager threatening you. It's up to you...

TODO
====

path operations
---------------
* merge
* intersections
* subtractions
* subpath
* split
* remove

interoperability
-------------------------------
* implement serializable

static factory dsl
------------------
Here I have a builder pattern in mind with static methods. E.g. (includes draft api for not yet existing features):

```java

    Path servicePath = path("/projects");
    UnknownClassOfUserProject someProject = ...;
    UnknownClassOfUserDocument document = ...;
    RichUrl documentUrl = http(
            host(
                hostName("example.org"),
                port(1234)
            ),
            path(
                servicePath,
                path(
                    pathAtom(/* String */ someProject.getDocumentsPath()),
                    pathAtom(/* String */ document.getPath())
                )
            )
        );
    
    RichUrl node1Url = http(
            subDomain("node1"),
            noProtocolUrl("example.org:2435/publicdocs")
        );
    
    RichUrl node2Url = http(
            subDomain("node2"),
            noProtocolUrl("example.org:2435/publicdocs")
        );
    
    httpService.copy(documentUrl, node1Url, node2Url);

```

An incomplete list of static factory methods: 
* path with string
* path with atoms
* path with paths
* decide if other combinations are required
* atom with string
* atom with atom
* atom list with atoms
* ...

IDEAS
=====
* mapTo
* mapFrom
* mapToAndFrom
* path mapping observer
* interface toPath for Paths and PathAtoms
* transform with transformation rule
* consider path multiplications as kind
  of linear algebra operations in order to provide routing matrixes

URL encoding
------------
* interface URLEncodable
* decide between eager and lazy encoding
