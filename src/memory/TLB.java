package memory;

/**
 * translation look aside buffers r ,  a class that gives better performance for the system
 */
public class TLB {
	// process column
	private int process[];
	// segmenet column
	private int segment[];
	// start(Physical adress)	
	private int start[];
	// current position 
	private int pos=0;
	//size of TLB
	private int size=0;
	TLB(int size)
	{
		process=new int [size];
		segment=new int [size];
		start=new int [size];
		this.size=size;
	}
	/**
	 * get maximum positition to search to
	 */
	private int maxx() {
		if(pos<size) return pos; else return size;
	}
	/**
	 * Try to access memory segment using TLB
	 * @param pro process id
	 * @param segment index of segment starting from 1
	 * @param adress virtual adress to search for
	 * @return physical adress or -1 if failed 
	 */
	public int acessAdress(int pro,int segment,int adress)
	{
		// search in all TLB
		for (int i = 0; i < maxx(); i++) {
			if(this.process[i]==pro
					&&
					this.segment[i]==segment
			)
				// found
				return this.start[i]+adress;
			
		}
		// failed
		return -1;
	}
	// add a segment to TLB 
	void addSegment(int pro,int segment,int start)
	{
		// FIFO algorithm using reaminder of devision by size
		int c=pos%size;
		// fill row
		this.process[c]=pro;
		this.segment[c]=segment;
		this.start[c]=start;
		pos++;
	}
	

}
