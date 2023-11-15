package memory;

/**
 * 
 * Represent one process in the system, 
 * contains id and name of the process.
 *
 */
public class Process {
	public Process(int id) {
		this.id=id;
		this.name="P"+id;
	}
	/**
	 * unique identifier of Process in the system
	 */
	private int id;
	/**
	 * a name to show for presentation of the process 
	 */
	private String name;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
