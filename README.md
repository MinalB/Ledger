# Task

Create a Ledger system with Nodes and subscriptions

#Time Limit

1-1.5 hours


================================================================

#### How to execute

1. Build the application with maven for dependency resolution

3. Execute the Test class LedgerOperationsTest.java


#### Functionality
1. The Ledger would have transactions that map with nodes.

2. The Nodes should subscribe and unsubscribe to any existing nodes

3. When a Node subscribes to another nodes, all the transactions of the current node and nodes in subscription list should be published

4. Ability to fetch transactions in ordered way, in the order they were added or in reverse order


#### Further enhancements (that can be incorporated in future)

1. The models can be moved to database, this will make it easier for referential integrity.

2. REST controllers for access of the ledger details from various parts of application

3. Concurrency and Parallelism (If access and data increases)

4. Specific Error handling with custom exceptions

5..and of course some User Interface :)
