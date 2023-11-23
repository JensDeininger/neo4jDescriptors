# neo4jDescriptors

This Library implements a possible way to write type and refactoring safe User-Defined Procedures and Functions in Neo4j.

The usage of this library is demonstrated in the repository [Neo4j-Live-Type-safe-UDPs](https://github.com/ElenaKohlwey/Neo4j-Live-Type-safe-UDPs). Its functionality was introduced to the public in the Webinar [Type and Refactoring Safety in User-Defined Procedures](https://www.youtube.com/watch?v=18yqaEXOuCU) on November, 23rd 2023.

## Getting Started

In order to get started, you should look at the Procedure Project [Neo4j-Live-Type-safe-UDPs](https://github.com/ElenaKohlwey/Neo4j-Live-Type-safe-UDPs). You have copy-and-paste examples for the usage of the descriptors in this project.

- Clone this repository and build it.
- Create a new UDP Project (cp. [Neo4j Procedure Template](https://github.com/neo4j-examples/neo4j-procedure-template) or [Neo4j-Live-Type-safe-UDPs](https://github.com/ElenaKohlwey/Neo4j-Live-Type-safe-UDPs)) that includes a dependency to this repository.
- Setup the descriptors
  - Write enum types (cp. [ItemKind.java](https://github.com/ElenaKohlwey/Neo4j-Live-Type-safe-UDPs/blob/main/src/main/java/org/ek/nl/descriptors/nodes/ItemKind.java) and [ItemKindProperty.java](https://github.com/ElenaKohlwey/Neo4j-Live-Type-safe-UDPs/blob/main/src/main/java/org/ek/nl/descriptors/nodes/ItemKindProperty.java) in [Neo4j-Live-Type-safe-UDPs](https://github.com/ElenaKohlwey/Neo4j-Live-Type-safe-UDPs))
  - Write Descriptors for desired Nodes and Relationship kinds (folders [nodes](https://github.com/ElenaKohlwey/Neo4j-Live-Type-safe-UDPs/tree/main/src/main/java/org/ek/nl/descriptors/nodes) and [relations](https://github.com/ElenaKohlwey/Neo4j-Live-Type-safe-UDPs/tree/main/src/main/java/org/ek/nl/descriptors/relations) in [Neo4j-Live-Type-safe-UDPs](https://github.com/ElenaKohlwey/Neo4j-Live-Type-safe-UDPs))
  - Set up Label-, RelationshipType-, Node- and Relationship repositories (cp. folder [reps](https://github.com/ElenaKohlwey/Neo4j-Live-Type-safe-UDPs/tree/main/src/main/java/org/ek/nl/descriptors/reps) in [Neo4j-Live-Type-safe-UDPs](https://github.com/ElenaKohlwey/Neo4j-Live-Type-safe-UDPs))
  - Copy paste Validation procedure class and its test class (cp. [Validation Procedure](https://github.com/ElenaKohlwey/Neo4j-Live-Type-safe-UDPs/blob/main/src/main/java/org/ek/nl/Validation.java) and [Validation Test](https://github.com/ElenaKohlwey/Neo4j-Live-Type-safe-UDPs/blob/main/src/test/java/org/ek/nl/ValidityTest.java) in [Neo4j-Live-Type-safe-UDPs](https://github.com/ElenaKohlwey/Neo4j-Live-Type-safe-UDPs))
- Use the descriptors for
  - Fetching nodes
  - Iterating to other nodes
  - Accessing properties
  - Creating nodes
  - Creating relationships
  - Writing properties
  - Validating your graph schema
