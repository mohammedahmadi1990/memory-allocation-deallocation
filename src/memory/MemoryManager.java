package memory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

/**
 * This contains main code to handle memory allocation and deallocation and most
 * important memory access , it contains also logic of processes, TLb, and memory
 * sharing, and authorization on different segments; think of this as the kernel
 * in an operating system.
 *
 */
public class MemoryManager {
	// Definition of Allocation algortihms 
	public static enum Algorithm {
		firstFit
	}
	// all segements of memory
	ArrayList<Segment> segments = new ArrayList<>();;
	// transalte look ahead buffer
	TLB tlb = new TLB(3);
	// total size of physical meory
	private int memorysize = 0;
	// algorothm to use 
	Algorithm algorithm = Algorithm.firstFit;
	// flag to enable and disable read write protection
	private boolean readwriteProtection = true;

	public void setTLBsize(int size) {
		tlb = new TLB(size);
	}
	/**
	 * enable and read write protection
	 */
	public void EnableReadWriteProtection() {
		readwriteProtection = true;
	}
	/**
	 * sdisable read write protection
	 */
	public void DisableReadWriteProtection() {
		readwriteProtection = false;
	}
	/**
	 * 
	 * @param txt command toe execute 
	 * @throws Exception for Out of Memry
	 *                   also for Format and Integer 
	 *                   Parsing erros   
	 */
	void parse(String txt) throws Exception {
		System.out.println(txt);
		// Split by comma outside brackets
		String[] res = txt.split(",(?![^\\(\\[]*[\\]\\)])", 0);
		// convert first element to process number
		int procss = Integer.parseInt(res[0]);
		// i variable to calculate number of previous segments attached to
		// this process
		int i = 0;
		ArrayList<Segment> psegs = new ArrayList<>();
		// iterate trhough memory, fill psegs with previous segments
		// attached to this process
		for (Iterator iterator = segments.iterator(); iterator.hasNext();) {
			Segment segment = (Segment) iterator.next();
			if (segment.getOwner().getId() == procss) {
				i++;
				psegs.add(segment);
			}
			//collect also shared memory
			else if (segment.hasSharedProcess(procss)) {
				i++;
				psegs.add(segment);
			}
		}
		// iterate for all parts of command
		for (int t = 1; t < res.length; t++) {
			// System.out.println(res[t]);
			// init segement size to zero 
			int segmentsize = 0;
			// set as read and write
			int rwflag = 1;
			// share-with-list
			ArrayList<Integer> processes = new ArrayList<>();
			// if the string contains brackets
			if (res[t].contains("[")) {
				//remove brackets from string
				res[t] = res[t].replace("[", "");
				res[t] = res[t].replace("]", "");
				String[] res1 = res[t].split("[,]", 0);
				// first part is segment size
				segmentsize = Integer.parseInt(res1[0]);
				// if there is a read-write flag
				if (res1.length > 1)
					rwflag = Integer.parseInt(res1[1]);
				;
				// if readwriteProtection and there is 
				// a share-with-list set segment to read only 
				if (readwriteProtection && res1.length > 2)
					rwflag = 0;
				// fil share-with-list
				for (int tt = 2; tt < res1.length; tt++) {
					processes.add(Integer.parseInt(res1[tt]));
				}
			} 
			// if the string doe not contains brackets
			else {
				// the whole string is a segmentsize
				segmentsize = Integer.parseInt(res[t]);
			}
			// if the segment to allocate is new
			if (t > i) {
				if (this.algorithm == Algorithm.firstFit)
					addfirstfit(new Process(procss), segmentsize, rwflag, processes);
			} 
			// if the segment is already in memory and this is a modification
			else {
				// if modification to processes or read write flag
				if (procss == psegs.get(t - 1).getOwner().getId())
					psegs.get(t - 1).setProcesses(processes);
				if (procss == psegs.get(t - 1).getOwner().getId())
					psegs.get(t - 1).setRwflag(rwflag);
				// modiy existing segment
				if (this.algorithm == Algorithm.firstFit)
					// if it's not a shared memory
					if(procss==psegs.get(t - 1).getOwner().getId())
					growfirstfit(psegs.get(t - 1), segmentsize);
			}

		}
	}
	/**
	 * Try to access a virtual adress 
	 * using TLB than segments table
	 * @param procss id of process
	 * @param segmentn index of segment for that process starting from 
	 * @param adress virtual adress to access
	 * @return physical adress
	 * @throws Exception if out of memory
	 */
	int accessMemory(int procss, int segmentn, int adress) throws Exception {
		// slow down for second 
		try {
			TimeUnit.SECONDS.sleep(1);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println("process:" + procss + " segment:" + segmentn + " Adress:" + adress);
		// collect segments for current process
		ArrayList<Segment> psegs = new ArrayList<>();
		// variable to collect count of segments for current process
		int i = 0;
		// try to use TLB
		int retadr = tlb.acessAdress(procss, segmentn, adress);
		//TLB success
		if (retadr != -1) {
			System.out.println("TLB HIT");
			return retadr;
		}
		// TLB Failed 
		System.err.println("TLB Fault");
		// try search in segment table
		// iterate in segment table and collect those of current process
		for (Iterator iterator = segments.iterator(); iterator.hasNext();) {
			Segment segment = (Segment) iterator.next();
			if (segment.getOwner().getId() == procss) {
				i++;
				psegs.add(segment);
			}
		}
		// if requested segment number in range
		if (psegs.size() >= segmentn) {
			// refresh TLB with new segment
			tlb.addSegment(procss, segmentn, psegs.get(segmentn - 1).getStartt());
			//return physical adress
			return psegs.get(segmentn - 1).getStartt() + adress;

		}
		// Fail to find segment in All paths
		throw new Exception("Acess Out of Memory");
	}
	/**
	 * allocate or deallocate existing segment
	 * @param seg the segment to modify
	 * @param size new size
	 * @throws Exception out of memory if it's not possible
	 *                   to allocate memory
	 */
	private void growfirstfit(Segment seg, int size) throws Exception {
		// if requested size is zero do nothing
		if (size == 0)
			return;
		// if negative size deallocate
		else if (size < 0) {
			// if new size less or equal  zero remove segment
			if (seg.getSize() + size <= 0)
				segments.remove(seg);
			// else just resize
			else
				seg.setSize(seg.getSize() + size);

		} 
		// if positive size allocate
		else {
			// maximum memory possible to allocate 
			// maximum is total physical memory
			int limit = memorysize;
			// maximum is the start of next segment is memory
			if (segments.indexOf(seg) < segments.size() - 1)
				limit = segments.get(segments.indexOf(seg) + 1).getStartt();
			// if aloocation will not exceeds limit
			if (seg.getStartt() + seg.getSize() + size <= limit)
				//allocate more size
				seg.setSize(size + seg.getSize());
			// no Allocation is possible
			else {
				throw new Exception("Unable to allocate memory");

			}
		}
	}
	/**
	 * Compact momory and remove all holes
	 */
	public void compact() {
		int position = 0;
		Segment seg = null;
		// oterate through all segments
		for (Iterator iterator = segments.iterator(); iterator.hasNext();) {
			Segment segment = (Segment) iterator.next();
			// set the start of segment to previous segment end
			segment.setStartt(position);
			
			// save for next iteration
			seg = segment;
			position += segment.getSize();

		}
	}
	/**
	 * Allocate new memory segment for a process
	 * with FirstFit algorithm
	 * @param process the process who requested for this allocation
	 * @param size  amount of memory to allocate
	 * @param rwflag is it read only or read and write
	 * @param processes share-with-list
	 * @throws Exception out of memory if failed
	 */
	private void addfirstfit(Process process, int size, int rwflag, ArrayList<Integer> processes) throws Exception {
		int position = 0;
		// is there a hole in the middle for this segment
		int middle = 0;
		Segment seg = null;
		// itreate in all segments
		for (Iterator iterator = segments.iterator(); iterator.hasNext();) {
			Segment segment = (Segment) iterator.next();
			// if there is a hole with enough size 
			// go out from iterations
			if ((segment.getStartt() - position) >= size) {
				// confirm there is a hole
				middle = 1;
				break;
			}
			
			//save for next iteration
			seg = segment;
			position = segment.getStartt() + segment.getSize();

		}
		//if there is no hole in the middle
		if (middle == 0) {
			// we are at end of all segments
			// does remaining memory enough ro allocate
			// if not throw exception
			if (position + size > memorysize) {
				throw new Exception("Unable to allocate memory");

			}
		}
		// if a hole in the beginning is enough
		if (segments.size() > 0 && size <= segments.get(0).getStartt()) {
			segments.add(0, new Segment(process, size, rwflag, processes));
		} 
		//if a hole in the middle
		else if (segments.indexOf(seg) < segments.size() - 1) {
			Segment newsg = new Segment(process, seg.getStartt() + seg.getSize(), size, rwflag, processes);
			segments.add(segments.indexOf(seg) + 1, newsg);
		} 
		// if hole at the end
		else {
			segments.add(new Segment(process, position, size, rwflag, processes));
		}

	}
	/**
	 * show all segment with sizes and boundries
	 */
	public void printMemory() {
		int i = 0;
		int position = 0;
		Segment seg = null;
		// iterate through all segments
		for (Iterator iterator = segments.iterator(); iterator.hasNext();) {
			Segment segment = (Segment) iterator.next();
			// if a hole 
			if (segment.getStartt() > position) {
				System.out.println("Addresses [" + ((position + 1 == 1) ? 0 : (position + 1)) + ":"
						+ segment.getStartt() + "] Unused  ");
			}
			// if a segment
			System.out.println("Addresses [" + ((segment.getStartt() + 1 == 1) ? 0 : (segment.getStartt() + 1)) + ":"
					+ (segment.getStartt() + segment.getSize()) + "] Process " + segment.getOwner().getName() + "  ");
			seg = segment;
			position = segment.getStartt() + segment.getSize();
			i++;

		}
		// if a hole at the end
		if (position < memorysize)
			System.out.println(
					"Addresses [" + ((position + 1) == 1 ? 0 : (position + 1)) + ":" + memorysize + "] Unused  ");

	}
	
	/**
	 * show all segment with sizes and boundries
	 */
	public void printMemoryinLine() {
		// slow down for one second
		try {
			TimeUnit.SECONDS.sleep(1);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int i = 0;
		int position = 0;
		Segment seg = null;
		// iterate through all segments
		for (Iterator iterator = segments.iterator(); iterator.hasNext();) {
			Segment segment = (Segment) iterator.next();
			// if a hole
			if (segment.getStartt() > position) {

				System.out.print("[H" + (segment.getStartt() - position) + "]");
			}
			// if process
			System.out.print("[A" + segment.getSize() + ((segment.getRwflag() == 0) ? " r " : "")
					+ segment.printProcesses() + "]");
			seg = segment;
			position = segment.getStartt() + segment.getSize();
			i++;

		}
		// if a hole at the end
		if (position < memorysize)
			System.out.print("[H" + (memorysize - position) + "]");
		System.out.println();
	}

	public MemoryManager(int memorysize, Algorithm algorithm) {
		this.memorysize = memorysize;
	}

}
