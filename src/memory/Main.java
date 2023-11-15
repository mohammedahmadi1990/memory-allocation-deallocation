/**
 * 
 */
package memory;

/**
 * Main driver Class , to test diffrent parts of system
 *
 */
public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		MemoryManager memorymanager = new MemoryManager(500, MemoryManager.Algorithm.firstFit);
		System.out.println("Start Example A.1.2");
		System.out.println(Thread.currentThread().getStackTrace()[1]);
		memorymanager.printMemoryinLine();
		try {
			memorymanager.parse("1,100,200,10");
			memorymanager.printMemoryinLine();
			memorymanager.parse("1,-40,0,0");
			memorymanager.printMemoryinLine();
			memorymanager.parse("1,-40,0,0");
			memorymanager.printMemoryinLine();
			memorymanager.parse("1,80,0,0");
			memorymanager.printMemoryinLine();
			memorymanager.parse("1,80,0,0");
			memorymanager.printMemoryinLine();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			memorymanager.printMemoryinLine();
			memorymanager.parse("1,0,-20,0");
			memorymanager.printMemoryinLine();
			memorymanager.parse("1,0,20,0");
			memorymanager.printMemoryinLine();
			memorymanager.parse("1,0,40,0");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			memorymanager.printMemoryinLine();
			memorymanager.parse("1,0,0,20");
			memorymanager.printMemoryinLine();
			memorymanager.parse("1,0,0,40");
			memorymanager.printMemoryinLine();
			memorymanager.parse("1,0,0,-10");
			memorymanager.printMemoryinLine();
			memorymanager.parse("1,0,0,-140");
			memorymanager.printMemoryinLine();
			memorymanager.parse("1,0,0,40");
			memorymanager.printMemoryinLine();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			memorymanager.printMemoryinLine();
			memorymanager.parse("1,-40,0,0");
			memorymanager.printMemoryinLine();
			memorymanager.parse("2,40");
			memorymanager.printMemoryinLine();
			memorymanager.parse("1,-10");
			memorymanager.printMemoryinLine();
			memorymanager.parse("2,-20");
			memorymanager.printMemoryinLine();
			memorymanager.parse("1,-50");
			memorymanager.printMemoryinLine();
			memorymanager.parse("3,40");
			memorymanager.printMemoryinLine();
			memorymanager.parse("4,160");
			memorymanager.printMemoryinLine();
			memorymanager.parse("5,20,20");
			memorymanager.printMemoryinLine();
			memorymanager.parse("5,20,20");
			memorymanager.printMemoryinLine();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(Thread.currentThread().getStackTrace()[1]);
		System.out.println("End Example A.1.2");
		System.out.println();
		
		System.out.println("Start Example A.2.1");
		System.out.println(Thread.currentThread().getStackTrace()[1]);
		memorymanager = new MemoryManager(500,  MemoryManager.Algorithm.firstFit);
		try {
			memorymanager.parse("1,[100,0],[200,1],10");
			memorymanager.printMemoryinLine();
			memorymanager.parse("1,[-10,0],[-40,1],0");
			memorymanager.printMemoryinLine();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(Thread.currentThread().getStackTrace()[1]);
		System.out.println("End Example A.2.1");
		System.out.println("Start Code A.2.1");
		System.out.println("MemoryManager.java Line 47 to 73");
		System.out.println("End Code A.2.1");
		System.out.println();
		
		System.out.println("Start Example A.2.2");
		System.out.println(Thread.currentThread().getStackTrace()[1]);
		memorymanager = new MemoryManager(500,  MemoryManager.Algorithm.firstFit);
		memorymanager.DisableReadWriteProtection();
		try {
			memorymanager.parse("1,[100,1,2,4],50");
			memorymanager.printMemoryinLine();
			memorymanager.parse("2,[100,1,1,4],30");
			memorymanager.printMemoryinLine();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		memorymanager.EnableReadWriteProtection();
		System.out.println(Thread.currentThread().getStackTrace()[1]);
		System.out.println("End Example A.2.2");
		System.out.println("Start Code A.2.2");
		System.out.println("MemoryManager.java Line 21 to 27");
		System.out.println("MemoryManager.java Line 47 to 73");
		System.out.println("End Code A.2.2");
		System.out.println();
		
		System.out.println("Start Example A.2.3");
		System.out.println(Thread.currentThread().getStackTrace()[1]);
		memorymanager = new MemoryManager(500,  MemoryManager.Algorithm.firstFit);
		try {
			memorymanager.parse("1,[100,0,3,4],[200,1,2,3],10");
			memorymanager.printMemoryinLine();
			memorymanager.parse("1,[-10,0,4],[-40,1,2,3],0");
			memorymanager.printMemoryinLine();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(Thread.currentThread().getStackTrace()[1]);
		System.out.println("End Example A.2.3");
		System.out.println("Start Code A.2.3");
		System.out.println("Segment.java Line 57 to 79");
		System.out.println("MemoryManager.java Line 47 to 73");
		System.out.println("End Code A.2.3");
		System.out.println();
		

		System.out.println("Start Example A.3.1");
		System.out.println(Thread.currentThread().getStackTrace()[1]);
		memorymanager = new MemoryManager(500,  MemoryManager.Algorithm.firstFit);
		memorymanager.setTLBsize(3);
		try {
			memorymanager.parse("1,30,20,10");
			memorymanager.parse("2,30,20,10");
			memorymanager.parse("3,30,20,10");
			memorymanager.parse("4,30,20,10");
			memorymanager.printMemoryinLine();
			memorymanager.accessMemory(1, 1, 0);
			memorymanager.accessMemory(1, 1, 0);
			memorymanager.accessMemory(1, 2, 0);
			memorymanager.accessMemory(1, 3, 0);
			memorymanager.accessMemory(1, 3, 0);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println(Thread.currentThread().getStackTrace()[1]);
		System.out.println("End Example A.3.1");
		System.out.println("Start Code A.3.1");
		System.out.println("MemoryManager.java Line 79 to 103");
		System.out.println("End Code A.3.1");
		System.out.println();
		

		System.out.println("Start Example A.3.2");
		System.out.println(Thread.currentThread().getStackTrace()[1]);
		memorymanager = new MemoryManager(500,  MemoryManager.Algorithm.firstFit);
		try {
			memorymanager.parse("1,100,200,10");
			memorymanager.printMemoryinLine();
			memorymanager.parse("1,-40,0,0");
			memorymanager.printMemoryinLine();
			memorymanager.parse("1,-40,0,0");
			memorymanager.printMemoryinLine();
			memorymanager.compact();
			memorymanager.printMemoryinLine();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(Thread.currentThread().getStackTrace()[1]);
		System.out.println("End Example A.3.2");
		System.out.println("Start Code A.3.2");
		System.out.println("TLB.java Line 1 to 41");
		System.out.println("MemoryManager.java Line 83 to 98");
		System.out.println("End Code A.3.2");

	}

}
