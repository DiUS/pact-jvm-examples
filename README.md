pact-example
============

Example projects for https://github.com/DiUS/pact-jvm built for playframework 2.2.1

animal-consumer
===============

Observe _link to come_

This test runs during the normal "sbt test" (or "play test") phase, but will output a file into target/pacts/

The file is intended to then be published to a provider project


animal-provider
===============

Observe :
*  The pact in _link to come_
*  The configuration in _link to come_

To verify that this provider meets the pact:

1.  Start the server with:

    sbt run

2.  In a separate terminal run the pacts with:

    sbt verifyPacts

