package memory;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * This the basic block of memory , it has a variable length size ,
 *  and the start and end of segment is saved here as real and a calculated variable.
 *  other attribute is also attached to segment
 */
public class Segment {
	// start of segment
	private int startt=0;
	// size of segment
	private int size;
	// PRocess who created this Segment
	private Process owner;
	// read and write protection flag
	private int rwflag=0;
	// share-with-list
	private ArrayList<Integer>processes;
	
	public int getRwflag() {
		return rwflag;
	}
	public void setRwflag(int rwflag) {
		this.rwflag = rwflag;
	}
	public ArrayList<Integer> getProcesses() {
		return processes;
	}
	public void setProcesses(ArrayList<Integer> processes) {
		this.processes = processes;
	}
	public Process getOwner() {
		return owner;
	}
	public void setOwner(Process owner) {
		this.owner = owner;
	}
	public Segment(Process owner,int segmentsize, int rwflag, ArrayList<Integer> processes) {
		this.size=segmentsize;
		this.owner = owner;
		this.rwflag=rwflag;
		this.processes=processes;
		
	}
	public Segment(Process owner,int startt, int segmentsize, int rwflag, ArrayList<Integer> processes) {
		this.size=segmentsize;
		this.owner = owner;
		this.startt=startt;
		this.rwflag=rwflag;
		this.processes=processes;
	}
	public int getStartt() {
		return startt;
	}
	public void setStartt(int startt) {
		this.startt = startt;
	}
	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}
	// format sharz-with-list in a string
	public String printProcesses()
	{
		String ret="";
		// iterate through shar-with-list
		for (Iterator iterator = processes.iterator(); iterator.hasNext();) {
			Integer pro = (Integer) iterator.next();
			ret+= pro;
			if(processes.indexOf(pro)<processes.size()-1)ret=ret+",";
			
		}
		if (ret=="") return ret;
		return "<"+ret+">";

		
	}
	/**
	 * test if a process is in the share-with-list of this segment
	 * @param procss id o process to search for 
	 * @return
	 */
	public boolean hasSharedProcess(int procss) {
		for (Iterator iterator = processes.iterator(); iterator.hasNext();) {
			Integer pro = (Integer) iterator.next();
			if(pro==procss)return true;
		}
		
		return false;
	}
	
	
}
