# memory-allocation-deallocation


![image](https://github.com/mohammedahmadi1990/memory-allocation-deallocation/assets/72644777/f6fb8fc4-4487-4bd8-8c68-eaad0530ca79)

Expectations
Main File
Design 
 
The design is based on performance and efficiency, the memory management module will make sure to link different part of the system 
Main class => Main.java
This is the main class that contains driver code, it contains all examples of functionality of the system, form allocation , deallocation to read-write and sharing of segments. 
For every testcase Main class prepare initial state of the system before running the test, eact test suite is isolated from others, some settings and configurations are necessary for special cases like disabling read and write protection, or setting TLB size
Part of tests are surrounded by try and catch , every exception is printed on screen , log library  like log4j are not used by our project.
Classes
MemoryManager.java
This contains main code to handle memory allocation and deallocation and most important memory access , it contains also logic of processes, TLb, and memory sharing, and authorization on different segments; think of this as the kernel in an operating system.
Segments variable Represent the segments table of physical memory, as a sequence of memory blocks (Class Segment), in a list, the structure we chose for performance reasons is ArrayList , restrictions on boundaries of blocks are also controlled for each element in those segments , no block will overflow to the next one.
We created a parse function , to split each command to memory, due to complexity for splitting string with comma inside and outside brackets, we do this in two steps, first we split by comma out of brackets, than we parse again and split by comma inside brackets.
We created one system that supports all required features, we used default values to handle one of the features or combination of them, however it’s not always possible without controlling configuration by final user, for example it’s not possible to show sharing , without showing read write protection, because according to requirements a read write protection is set on all sharing, for that we added a function DisableReadWriteProtection.
Memory allocation is done by adding segment to segments list , we represent just active memory , not empty memory or holes , this is the similar behavior used in most operating system, holes are just calculated variables, and presented when printing, they don’t have memory segment in our table.
Memory deallocation is implemented by resizing segments, change the size of one segment , will created the hole automatically, as a hole id defined as the difference   between end of previous node and start of next one.
If dealocation results in a size equal or less than zero, we remove that segment directly, no exception will thrown for dealocating more than the size of the segment.
More attention is given to cases where there is a hole at the start or the end of segments, or the special cases where exception is used for out of memory.
The Algorithm used is FirstFit, our design enable to implements more algorithms later like Worstfit, or Best Fit. An enum structure is used to define them.
A TLB translation look aside buffers, is used with a configurable fixed length size, we use a simple 3 columns process,segment and physical address, we make it the structure as simple arrays with FIFO replacement using remainder of devision of cumulative position by  the size of TLB.
MemorySize is saved with constructor, and used later as the maximum memory available of processes.
Our design is based on segment of memory , not on processes, we didn’t implement any Processor priority algorithm like Round Robin,  we just use identifier of processes as integers in most parts.

An AccessMemory function is added to test functionality of reading or writing to memory from processes , no physical memory is present in the prject as this is just a simulation, so we only test possibility of accessing memory , we retrun physical address rather than doing real I/O operations.
The access we use TLB if available, if not it will do a complete search in the segment table.
Segment.java
This the basic block of memory , it has a variable length size , and the start and end of segment is saved here as real or calculated variable.
Rather than saving end of segment, we save the size the block, this results in easier deallocation where  we just change the size of segment.
 Owner process is saved in variable separated from shared-with-list, this choice is based on linux groups where owner of objects is different from others who have authorization for access.
Shared-with-list as saved as a list of processes , it’s possible to have emptuy list, or replace them at every step.
Read and write protection is implemented in this Class via rwflag variable, we make it rather than Boolean which gives more flexibility later to add more modes like authorization for running code in that segment similar to +x.
A printProcesses function is added to quickly get a formatted string contains all processes in share-with-list.
To test if a process is in the shared-with-list we added hasSharedProcess ()wheich return a boolean value with true if search found the process in that list.

TLB.java
translation look aside buffers r ,  a class that gives better performance for the system, because cpu code needs many accesses to the same memory to run simple mov or add,  TLB will prevent this intensive access and deep search  in the segment table. It does not look useful for few segments like our example, but in real life situations we have about half million segments in a basic web server.
We use three  integer arrays(Process,Segement,Start) to create a TLB like this
Position	Process	Segment	Start(physical Adress)
1	1	2	200
2	2	1	50
3	1	1	400

Primary key for this table is (Process,Segment) .
A function to access physical address from virtual address is added with name accessAdress(); that function returns physical address if it’s possible (TLB Hit), or -1 if this segment is not in Table , this will be considered as TLB Fault..
TLB will be filled with a FIFO algorithm. A better design will require use of LRU or Optimal replacement algorithm
We implemented FIFO by saving cumulative position of the TLB , we do use the remainder of devision by size of TLB as a cursor to add and replace at the specified position every step.
Process.java
Represent one process in the system, contains id a  unique identifier of Process in the system and a name to show for presentation of the process . those variables are private , a public getter and setter will allow access or changing them.
No Process management was implemented , we concentrate on memory management, processed in most part are referenced by their id only
Implementation of some important features
Read and write protection 
A flag is added to Segment to represent access rights, another flag is added to MemoryManager to disable that features, this was necessary to enable Memory sharing without read write protection, a public function to enable or disable this option is available. 
On printing read and write protection is represented by a letter near the segment if it’s read and write [A100] , and if it’s read only [A100  r ]
Memory Sharing 
A list is added to each segment to represent which processes is allowed  to access that segment , this is printed to screen as a list inside the segment 
[A100  <3,4,5>]  means that segement is shared with process 3 and 4 and 5.
Compact
Compact was easier because of implementation of our segments list because there is no holes in our list, we could iterate on segments list , and make the start of each node , as the end of previous node a pseudo code will be like this
Position=0
For segment in segments
     Segment.start=Position
     Position += segment.size
UML Diagram
 

Examples
Start Example A.1.2
memory.Main.main(Main.java:18)
memory.Main.main(Main.java:86)
End Example A.1.2

Start Example A.2.1
memory.Main.main(Main.java:91)
memory.Main.main(Main.java:102)
End Example A.2.1
Start Code A.2.1
MemoryManager.java Line 47 to 73
End Code A.2.1

Start Example A.2.2
memory.Main.main(Main.java:110)
memory.Main.main(Main.java:123)
End Example A.2.2
Start Code A.2.2
MemoryManager.java Line 21 to 27
MemoryManager.java Line 47 to 73
End Code A.2.2

Start Example A.2.3
memory.Main.main(Main.java:132)
memory.Main.main(Main.java:143)
End Example A.2.3
Start Code A.2.3
Segment.java Line 57 to 79
MemoryManager.java Line 47 to 73
End Code A.2.3

Start Example A.3.1
memory.Main.main(Main.java:153)
memory.Main.main(Main.java:171)
End Example A.3.1
Start Code A.3.1
MemoryManager.java Line 79 to 103
End Code A.3.1

Start Example A.3.2
memory.Main.main(Main.java:180)
memory.Main.main(Main.java:195)
End Example A.3.2
Start Code A.3.2
TLB.java Line 1 to 41
MemoryManager.java Line 83 to 98
End Code A.3.2

