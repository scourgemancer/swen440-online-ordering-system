# SWEN-440:  Project 2
Fall 2018

## Online Ordering System

### Overview:

The system consists of three main components:  a command line order 
entry module, a central processing module and a data storage module. 
 
 - The order entry module handles all communication with the user
 - The data storage module handles all communication with the underlying 
   file system.  
 - The central processing system coordinates the activities across 
   the system taking requests from the order entry module and
   interacting with the data storage layer to fulfill the request.
   
## Architecture
A key component of understanding and extending the **Online Ordering System** 
is its architecture.  Management expects each team to fully document
the architecture and how they plan to extend it.  Components to consider in
developing the architecture documentation are the key drivers, the
_Architectually Significant Requirements_ the team is addressing, the
architecture itself, and how the chosen architecture addresses the
drivers.  
   
## Development
The **Online Ordering System** was developed using the Java language.  The
developers used IntelliJ IDEA, atom, and vsCode as their principle
development tools.  Development work was done in both Ubuntu and Windows 10.
However, the customer has explicitly asked that the final
product run on Windows 10.

### Coding Style
The management team _fired_ the last set of architects and developers because they could
not agree on architectural direction or coding styles and conventions.  This
inability has led to a slightly brittle system that is at risk of falling behind its
primary competitor, that South American company, Amazon.   

The management team strongly believes that consistency in coding will yield a better result
and a better final grade.

### Dependencies
 - Java 8 : Refactoring work done over the life of the ordering system has
 resulted in dependencies on newer versions of Java.  Currently, streams
 and lambda expressions are used.  
 
    The customer is open to upgrading to newer Java versions if the architecture
    team identifies functionality that would enhance functionality of the
    system or readability of the resulting code.
 - Lombok Library: Lombok annotations are used in places to auto-generate getters and
 setters.  
 - JUnit Library:  Included but never used.
 - MySql: will need to install mysql commmandline to create the server

### Building
The development team choose Maven as their build tool.  Maven 3.1.0 and 3.3.9
have both been successfully used.  


With Maven installed, 
	run mvn package from the root install directory


## Running the Online Ordering System
	type run.sh or start.sh in command prompt from the root install directory

