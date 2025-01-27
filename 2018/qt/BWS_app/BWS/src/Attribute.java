package BWS_app

import java.util.LinkedList;
import java.util.List;

/** 
 * This the object class for the attribute options that will be
 * ranked with the BWS method. It stores the attribute's name, 
 * description, list of attributes currently ranked higher, and
 * a tally of how many attributes it is ranked higher than.
 */

public class Attribute implements Comparable<Attribute>{
	
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
	 *  	   ranked higher than.
	 */
	private List<String> list;
	private int tally;
	
	/**
	 * Constructors
	 */
	public Attribute(String name, String description) {
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
	}
	
	public Attribute(String name, String description, List<String> list, 
			int tally) {
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
		this.list = list;
		this.tally = tally;
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
	 * Add the name of the Attribute that is ranked higher if it
	 * doesn't already exist in the list or if this one exists in
	 * the other Attributes list.
	 */
	public void addToList(Attribute attr) {
		if (attr.equals(null)) {
			throw new NullPointerException("Attribute to add to list is null.");
		}
		if (!list.contains(attr.getName()) 
				&& !(attr.getList().contains(this.getName())) 
				&& !attr.equals(this)) {
			list.add(attr.name);
			attr.incrementTally();
		}
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
	public int compareTo(Attribute attr) {
		Integer total = new Integer(getTotal());
		if (total.compareTo(attr.getTotal()) == 0) {
			return name.compareTo(attr.getName());
		} else {
			return total.compareTo(attr.getTotal());
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
	
	/**
	 * Return a copy of list.
	 */
	public List<String> getList() {
		return new LinkedList<String>(list);
	}
	
}
