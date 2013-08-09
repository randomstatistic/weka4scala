weka4scala
==========

Scala wrapper for making it easier to programmatically interact with the Weka machine learning framework.

Specifically:
* Hide some of the incestious interdependancies between Attribute, Instance, and Instances
* Deal with names, not array positions and doubles
* Some utility for loading data from a file that's not in ARFF format

Potential Cons:
* Some limitations on Attribute types and nominal values (need to be represented as Strings)
* Adding a wrapper never helps performance, so although this wrapper doesn't seem too offensive, 
(mostly extra GC, I'd expect) if you have serious performance concerns, you should probably 
just use the java API directly.
