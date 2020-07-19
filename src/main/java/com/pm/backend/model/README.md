#Backend Model Classes

This is the documentation for the versioning of the data modeling schema.

No model classes directly live in the package `com.pm.backend.model`. Rather, each version of the API lives in it's own package (ie, `com.pm.backend.model.v1`).
Once the initial features are complete, and some real data is colelcted, we can better optimize the schema.

Any notes, design decisions/justifications, etc. regarding controllers should be committed into this README below when significant code changes are made (going in descending order from the time of the change):



### Initial commit (v1)
* Because this application will be read-heavy, we can get away with having some duplicated data to speed up queries. 
* ####Assumptions about data usage / queries
  *  A user will probably not change their username frequently if at all
  * Most of the queries will be done by group, rather than user. Rather than checking one user's stats, they will most likely be checking out the stats in the group, to see how they are doing in comparison to other users. 