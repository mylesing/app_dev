package BWS_app

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * The BWS Ranking process. It takes four Attributes taking
 * into account their priorities to be asked and asks the
 * question. It then ranks and assigns new priorities to
 * the Attributes.
 */
public class Rank {
	/**
	 * HashMap containing the complete list of Attributes to be ranked.
	 * Corresponding Map of priorities. Priority is determined by the total 
	 * number of comparisons an Attribute has with other Attributes.
	 * Smaller numbers have greater priority.
	 */
	private Map<String, Attribute> attrAll = new HashMap<>();
	private Set<Attribute> priorities = new TreeSet<Attribute>();
	
	//Creating the Attributes.
	//Update this process with sets of Attributes that can be accessed
	//and a UI that can create a set of Attributes.
	Attribute attrA = new Attribute("A", "QualityA");
	Attribute attrB = new Attribute("B", "QualityB");
	Attribute attrC = new Attribute("C", "QualityC");
	Attribute attrD = new Attribute("D", "QualityD");
	Attribute attrE = new Attribute("E", "QualityE");
	Attribute attrF = new Attribute("F", "QualityF");
	Attribute attrG = new Attribute("G", "QualityG");
	
	public void setup() {
		//Adding the Attributes to the Attributes map.
		attrAll.put("A", attrA);
		attrAll.put("B", attrB);
		attrAll.put("C", attrC);
		attrAll.put("D", attrD);
		attrAll.put("E", attrE);
		attrAll.put("F", attrF);
		attrAll.put("G", attrG);
		//Adding priorities to priorities TreeSet
		priorities.add(attrA);
		priorities.add(attrB);
		priorities.add(attrC);
		priorities.add(attrD);
		priorities.add(attrE);
		priorities.add(attrF);
		priorities.add(attrG);	
	}
	
	/**
	 * Choose four Attributes based on priority.
	 * Best and worst will be picked here until UI is made.
	 * Update lists and tallies.
	 * Repeat until complete ranking is achieved (if they all have
	 * comparisons to each other).
	 */
	public int run() {
		//If an Attribute has comparisons with all the other Attributes,
		//it is ranked and done.
		//If they all have comparisons to each other, all rankings are complete
		//and BWS process is done.
		Iterator<Attribute> iterator = attrAll.values().iterator();
		while(iterator.hasNext()) {
			Attribute attr = iterator.next();
			//Return 1 for completing ranking.
			//If there is only one attribute that doesn't have all comparisons,
			//the chooseFour function will throw the error. Though, it isn't
			//likely.
			if (attr.getTotal() >= attrAll.size() - 1 && !iterator.hasNext()) 
				return 1;
			if (attr.getTotal() >= attrAll.size() - 1 && iterator.hasNext()) 
				continue;
			break;
		}
					
		//Choose the four highest priority Attributes to ask a question
		String[] options = new String[4];
		options = chooseFour(options);
		
		//Ask question and receive input
		String most = options[1];
		String least = options[0];
		
		//Update Lists
		updateLists(most, least, options[2], options[3]);
		
		//Print in order of largest tally
		Collection<Attribute> attrs = attrAll.values();
		Iterator<Attribute> iter = attrs.iterator();
		while(iter.hasNext()) {
			Attribute attribute = iter.next();
			System.out.println(attribute.getName() + ": " + attribute.getList().size());
		}
		
		//Return 0 for having run successfully
		return 0;
	}
	
	public int run(List<String> rank) {
		//If an Attribute has comparisons with all the other Attributes,
		//it is ranked and done.
		//If they all have comparisons to each other, all rankings are complete
		//and BWS process is done.
		Iterator<Attribute> iterator = attrAll.values().iterator();
		while(iterator.hasNext()) {
			Attribute attr = iterator.next();
			//Return 1 for completing ranking.
			//If there is only one attribute that doesn't have all comparisons,
			//the chooseFour function will throw the error. Though, it isn't
			//likely.
			if (attr.getTotal() >= attrAll.size() - 1 && !iterator.hasNext()) 
				return 1;
			if (attr.getTotal() >= attrAll.size() - 1 && iterator.hasNext()) 
				continue;
			break;
		}
					
		//Choose the four highest priority Attributes to ask a question
		String[] options = new String[4];
		options = chooseFour(options);
		
		//Ask question and receive input. Then update lists.
		String[] rankedOptions = new String[4];
		Arrays.fill(rankedOptions, "");
		int count = 0;
		for (String ranked : rank) {
			if (ranked.equals(options[0])) {
				rankedOptions[count] = options[0];
				count++;
			} else if (ranked.equals(options[1])) {
				rankedOptions[count] = options[1];
				count++;
			} else if (ranked.equals(options[2])) {
				rankedOptions[count] = options[2];
				count++;
			} else if (ranked.equals(options[3])) {
				rankedOptions[count] = options[3];
				count++;
			}
			if(count >= 4) break;
		}
		String most = rankedOptions[0];
		System.out.println("most: " +  most);
		if(rankedOptions[2].equals("")) {
			String least = rankedOptions[1];
			updateLists(most, least, rankedOptions[2], rankedOptions[3]);
		} else if(rankedOptions[3].equals("")) {
			String least = rankedOptions[2];
			updateLists(most, least, rankedOptions[1], rankedOptions[3]);
		} else {
			String least = rankedOptions[3];
			updateLists(most, least, rankedOptions[1], rankedOptions[2]);
		}
		
		//Print in order of largest tally
		Collection<Attribute> attrs = attrAll.values();
		Iterator<Attribute> iter = attrs.iterator();
		while(iter.hasNext()) {
			Attribute attribute = iter.next();
			System.out.println(attribute.getName() + ": " + attribute.getList().size());
		}
		
		//Return 0 for having run successfully
		return 0;
	}
	
	/**
	 * Current Priority method
	 * Choose one with lowest number of comparisons with other Attributes.
	 * If the only one that can be added is maxed out in comparisons, add blank.
	 * If there is only one option then send error.
	 * 
	 * Possible Priority method
	 * Does not have a comparison total of attrAll.size() - 1.
	 * Go in order of Attributes with longest lists and pick one that isn't in 
	 * current and target's lists until you have four picked out.
	 * Add blanks if needed.
	 * If there is only one option then send error.
	 */
	private String[] chooseFour(String[] options) {
		int optionsCount = 0;
		//Fill with blanks in case four Attributes cannot be chosen.
		Arrays.fill(options, "");
		
		//Get a list of the Attributes sorted by fewest comparisons.
		//Set<Map.Entry<Integer, String>> attrByPrior = priorities.entrySet();
		
		//Iterate through list for options that still need to make comparisons 
		//and add them to the options list.
		Iterator<Attribute> iter = priorities.iterator();
		while (iter.hasNext() && (optionsCount < 4)) {
			Attribute current = iter.next();
			if(current.getTotal() >= attrAll.size() - 1) {
				continue;
			}
			options[optionsCount] = current.getName();
			System.out.println(current.getName());
			optionsCount++;
		}
		
		//Throw an error if there is one or zero options, otherwise return options.
		if (optionsCount <= 1)
			throw new java.lang.Error("Not enough options to pose question.");
		return options;
	}
	
	/**
	 * Get a list of Attributes lower than middle1 and middle2, and least.
	 * Add most and most's lists to middle1 and middle2, and the ones lower 
	 * than middle1 and middle2 but not in least's lowest. The contains 
	 * function in Attribute's add function will take care of repeats.
	 * Combine middle1's and middle2's list and middle1 and 
	 * middle2. Add them to least and the ones lower than least.
	 */
	private void updateLists(String most, String least, String middle1, 
			String middle2) {
		//Get a list of Attributes lower than middle1 and middle2, and least.
		Set<String> l_middle;
		if (middle1.equals("")) {
			l_middle = null;
		} else if (middle2.equals("")) {
			l_middle = getLowerAttributes(new String[]{middle1});
		} else {
			l_middle = getLowerAttributes(new String[]{middle1, middle2});
		}
		Set<String> l_least = getLowerAttributes(new String[]{least});
		//Create a set of Attributes that will be added to other Attributes.
		Set<String> listToAdd = new HashSet<String>();
		listToAdd.addAll(attrAll.get(most).getList());
		listToAdd.add(most);
		//Add most and most's lists to middle1 and middle2
		for (String attr_name : listToAdd) {
			Attribute attr = attrAll.get(attr_name);
			if (middle1 != "") {
				attrAll.get(middle1).addToList(attr);
			}
			if (middle2 != "") {
				attrAll.get(middle2).addToList(attr);
			}
			//Add listToAdd to the ones lower than middle1 and middle2 but not in 
			//least's lowest
			if (!(l_middle == null)) {
				for (String l_middle_attr : l_middle) {
					if (!l_least.contains(l_middle_attr))
						attrAll.get(l_middle_attr).addToList(attr);
				}
			}
		}
		//Combine middle1's and middle2's lists and middle1 and middle2.
		if (middle1 != "") {
			listToAdd.addAll(attrAll.get(middle1).getList());
			listToAdd.add(middle1);
		}
		if (middle2 != "") {
			listToAdd.addAll(attrAll.get(middle2).getList());
			listToAdd.add(middle2);
		}
		//Add listToAdd to least and the ones lower than least.
		for (String attr_name : listToAdd) {
			Attribute attr = attrAll.get(attr_name);
			attrAll.get(least).addToList(attr);
			for (String l_least_attr : l_least) {
				attrAll.get(l_least_attr).addToList(attr);
			}
		}
		//Update priorities list.
		priorities.clear();
		priorities.addAll(attrAll.values());
		//Update done.
		return;
	}
	
	/**
	 * Return a HashSet of Attributes that have lower ranks than the given 
	 * Attributes.
	 */
	private Set<String> getLowerAttributes(String[] attrs) {
		Set<String> l_attrs = new HashSet<String>();
		//Iterate through the Attributes and find ones that contain attr in
		//their lists of Attributes that have higher ranks.
		for (Attribute attr : attrAll.values()) {
			if (attr.containsAtLeastOne(attrs)) l_attrs.add(attr.getName());
		}
		return l_attrs;
	}
	
}
