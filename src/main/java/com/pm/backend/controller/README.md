# Backend Controller Classes

This is the documentation for all controllers living in this application.

Background on `package com.pm.backend.controller`:
We will use controllers to handle incoming requests, and serve up responses. Think of these classes as a "binding" between when the request first comes in and when the response is then returned.

When developing backend services, it is usually good to practice versioning. Thus, no controller classes directly live in the package `com.pm.backend.controller`. Rather, each version of the API lives in it's own package (ie, `com.pm.backend.controller.v1` - our starting point).

Any notes, design decisions/justifications, etc. regarding controllers should be committed into this README below when significant code changes are made (going in descending order from the time of the change):

### (Repo Setup) Wrote a skeleton `UserController` to test Spring Boot Setup
- Note that currently, there is no functionality with this controller code. This is being done for a couple of reasons:
1. Give others who are less familiar with Spring Boot services a chance to see how request mapping is done
2. We have not finalized our design for this codebase yet, but we wanted to get a head start on setting up the infrastructure for the application.
