import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/** 
 * This the object class for the attribute options that will be
 * ranked with the BWS method. It stores the attribute's name, 
 * description, list of attributes currently ranked higher, and
 * a tally of how many attributes this attribute is compared to.
 */


public class SimpleAttribute implements Comparable<SimpleAttribute>{
	
	/** The name and description for this Attribute.
	 *  name: Attributes can have a long description. This gives it a
	 *  	  shorter name.
	 *  description: A description is how the Attribute appears in the 
	 *  			 question (i.e. "Runny nose").
	 */
	private String name;
	private String description;
	
	/** The list and tally for this Attribute.
	 *  list: A list containing the names of Attributes ranked higher.
	 *  tally: The count of how many Attributes this Attribute is
	 *  	   compared to.
	 *  appearance: The number of times this Attribute appears in
	 *  			a question.
	 *  edges: The number of edges this Attirbute has in the Markov
	 *  	   chain.
	 */
	private List<String> list;
	private int tally;
	private int appearance;
	private int edges;
	
	/**
	 * Constructors
	 */
	public SimpleAttribute(String name, String description) {
		//Test for empty or null inputs
		if (name.equals("")) {
			throw new RuntimeException("Attribute name is empty.");
		} else if (name.equals(null)) {
			throw new NullPointerException("Attribute name is null.");
		} else if (description.equals("")) {
			throw new RuntimeException("Attribute description is empty.");
		} else if (description.equals(null)) {
			throw new NullPointerException("Attribute description is null.");
		}
		
		this.name = name;
		this.description = description;
		this.list = new LinkedList<String>();
		this.tally = 0;
		this.edges = 0;
	}
	
	public SimpleAttribute(String name, String description, List<String> list, 
			int tally, int edges) {
		//Test for empty or null inputs
		if (name.equals("")) {
			throw new RuntimeException("Attribute name is empty.");
		} else if (name.equals(null)) {
			throw new NullPointerException("Attribute name is null.");
		} else if (description.equals("")) {
			throw new RuntimeException("Attribute description is empty.");
		} else if (description.equals(null)) {
			throw new NullPointerException("Attribute description is null.");
		} else if (list == null) {
			throw new NullPointerException("List input is null.");
		}
		
		this.name = name;
		this.description = description;
		this.list = new LinkedList<String>(list);
		this.tally = tally;
		this.edges = edges;
	}
	

	/** Methods
	 *  -There is no set name method because list is dependent on 
	 *  the name of attribute being added to list.
	 *  -There is no remove from list method because additions to 
	 *  list should be final and the result of a true comparison.
	 *  -There is no decrement tally for the same reason as above.
	 */
	
	/**
	 * Get a copy of the name of the Attribute.
	 */
	public String getName() {
		String nameCopy = this.name;
		return nameCopy;
	}
	
	/**
	 * Get a copy of the description of the Attribute.
	 */
	public String getDescription() {
		String descriptionCopy = this.description;
		return descriptionCopy;
	}
	
	/**
	 * Change the description of this Attribute.
	 */
	public void setDescription(String description) {
		if (description.equals("")) {
			throw new RuntimeException("Attribute description is empty.");
		} else if (description.equals(null)) {
			throw new NullPointerException("Attribute description is null.");
		}
		this.description = description;
	}
	
	/**
	 * Add the name of the Attribute that is ranked higher. Increment 
	 * the tallies.
	 */
	public void addToList(SimpleAttribute attr) {
		if (attr.equals(null)) {
			throw new NullPointerException("Attribute to add to list is null.");
		}
		if (!list.contains(attr.getName()) 
				&& !(attr.getList().contains(this.getName())) 
				&& !attr.equals(this)) {
			attr.incrementTally();
			this.incrementTally();
		}
		list.add(attr.name);
		this.incrementEdges();
		attr.incrementEdges();
	
	}
	
	/**
	 * Increment appearance whenever it is asked in a question.
	 */
	public void incrementAppearance() {
		this.appearance++;
	}
	
	/**
	 * Increment the edges attacched to this Attribute.
	 */
	public void incrementEdges() {
		this.edges++;
	}
	
	/**
	 * Return the appearance count.
	 */
	public int getAppearance() {
		return this.appearance;
	}
	
	/**
	 * Get the number of edges connected to this Attribute.
	 */
	public int getEdges() {
		return this.edges;
	}
	
	/**
	 * Return the number of times attr is in this Attribute's 
	 * list.
	 */
	
	public int getCountOf(SimpleAttribute attr) {
		int count = 0;
		Iterator<String> iter = list.iterator();
		while(iter.hasNext()) {
			if (iter.next().equals(attr.getName())) count++;
		}
		return count;
	}
	
	/**
	 * Increment the tally by 1.
	 */
	private void incrementTally() {
		tally++;
	}
	
	
	/**
	 * Return the tally.
	 */
	public int getTally() {
		return tally;
	}
	
	/**
	 * Overwrite compareTo function
	 */
	public int compareTo(SimpleAttribute attr) {
		Integer total = new Integer(getTally());
		if (total.compareTo(attr.getTally()) == 0) {
			if ((new Random()).nextBoolean()) return 1;
			return -1;
		} else {
			return total.compareTo(attr.getTally());
		}
	}
	/**
	 * Get the total number of comparisons this Attribute has with
	 * other Attributes.
	 */
	public int getTotal() {
		return tally + list.size();
	}
	
	/**
	 * Return true if list contains at least one of the attributes
	 * in the given array. Otherwise return false.
	 */
	
	public boolean containsAtLeastOne(String[] attrs) {
		if (attrs.length == 0) {
			throw new RuntimeException("String array is empty.");
		} else if (attrs.equals(null)) {
			throw new NullPointerException("String array is null.");
		}
		for (String attr : attrs) {
			if (list.contains(attr)) return true;
		}
		return false;
	}
	
	public boolean containsAtLeastOne(String attr) {
		if (attr.equals(null)) {
			throw new NullPointerException("String array is null.");
		}
		if (list.contains(attr)) return true;
		return false;
	}
	
	/**
	 * Return a copy of list.
	 */
	public List<String> getList() {
		return new LinkedList<String>(list);
	}
	
}
