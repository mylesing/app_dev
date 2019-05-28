import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;

/**
 * The BWS Ranking process. It takes four SimpleAttributes taking
 * into account their priorities to be asked and asks the
 * question. It then ranks and assigns new priorities to
 * the SimpleAttributes.
 */
public class SimpleRank2 {
	/**
	 * HashMap containing the complete list of SimpleAttributes to be ranked.
	 * Corresponding Map of priorities. Priority is determined by the total 
	 * number of comparisons an SimpleAttribute has with other SimpleAttributes.
	 * Smaller numbers have greater priority.
	 */
	private Map<String, SimpleAttribute> attrAll;
	private Set<SimpleAttribute> priorities;
	private Set<String> removed;
	private Set<String> middle;
	private Set<String> best;
	private LinkedList<Set<String>> toAsk;
	private boolean ran;
	private int maxAppearance;
	
	public SimpleRank2(List<SimpleAttribute> attrList) {
		//If there are less than four Attributes throw error.
		if (attrList.size() < 4) {
			throw new IllegalArgumentException("Attribute list does not "
					+ "contain at least four Attributes.");
		}
		
		//Initialize global variables
		this.attrAll = new HashMap<String, SimpleAttribute>();
		this.priorities = new TreeSet<SimpleAttribute>();
		this.ran = false;
		//Attributes are chosen as best, worst, or the two not chosen as best or
		//worst. 
		//We work with groups of Attributes at a time--> Question further the 
		//best Attributes as one group, and then question further the middle
		//Attributes as one group. Worst Attributes are no longer questioned
		//so we only further question Attribute the patient cares about.
		//
		//removed: collection of worst Attributes
		//
		//middle: the Attributes not chosen as best or worst for the current
		// group we are working with. Once this group has been questioned,
		// the Attributes in this HashSet will be stored in the LinkedList (toAsk).
		// This way we can further question Attributes chosen as best in the group
		// currently being worked on, and question the other Attributes that
		// patient is less interested in later.
		//
		//best: the Attributes chosen as best in the group being worked on
		//
		//toAsk: A LinkedList that contains groups of Attributes that need to
		// be questioned. They are in order of groups that the patient is
		//most interested in.
		this.removed = new HashSet<String>();
		this.middle = new HashSet<String>();
		this.best = new HashSet<String>();
		this.toAsk = new LinkedList<Set<String>>();
		//The maximum number of times an Attribute can appear in a question.
		//The default is 3.
		this.maxAppearance = 3;
		
		//Adding the SimpleAttributes to the SimpleAttributes map.
		for (SimpleAttribute attr : attrList) {
			//Check if there are Attributes with the same name, which we use to ID Attributes.
			if (attrAll.containsKey(attr.getName())) {
				throw new IllegalArgumentException("Attributes with same name given.");
			}
			attrAll.put(attr.getName(), attr);
		}
		
		//Adding Attributes to priorities TreeSet
		priorities.addAll(attrList);
	}
	
	public SimpleRank2(List<SimpleAttribute> attrList, int maxAppearance) {
		//If there are less than four Attributes throw error.
		if (attrList.size() < 4) {
			throw new IllegalArgumentException("Attribute list does not "
					+ "contain at least four Attributes.");
		}
		
		//Initialize global variables
		this.attrAll = new HashMap<String, SimpleAttribute>();
		this.priorities = new TreeSet<SimpleAttribute>();
		this.ran = false;
		this.removed = new HashSet<String>();
		this.middle = new HashSet<String>();
		this.best = new HashSet<String>();
		this.toAsk = new LinkedList<Set<String>>();
		this.maxAppearance = maxAppearance;
		
		//Adding the SimpleAttributes to the SimpleAttributes map.
		for (SimpleAttribute attr : attrList) {
			//Check if there are Attributes with the same name, which we use to ID Attributes.
			if (attrAll.containsKey(attr.getName())) {
				throw new IllegalArgumentException("Attributes with same name given.");
			}
			attrAll.put(attr.getName(), attr);
		}
		
		//Adding Attributes to priorities TreeSet
		priorities.addAll(attrList);
	}
	
	/**
	 * Choose four SimpleAttributes based on priority.
	 * Best and worst will be picked here until UI is made.
	 * Update lists and tallies.
	 * Repeat until complete ranking is achieved (if they all have
	 * comparisons to each other).
	 */
	public int run() {
		this.ran = true;
		//If an SimpleAttribute has comparisons with all the other SimpleAttributes,
		//it is ranked and done.
		//If they all have comparisons to each other, all rankings are complete
		//and BWS process is done.
		Scanner user = new Scanner(System.in);
		for(int i = 0; i <= Math.ceil((float)(attrAll.size() * maxAppearance)/4.0f) + 1; i++) {
			Iterator<SimpleAttribute> iterator = attrAll.values().iterator();
			while(iterator.hasNext()) {
				SimpleAttribute attr = iterator.next();
				//Return 0 for completing ranking and having run successfully.
				if (attr.getAppearance() >= maxAppearance && !iterator.hasNext()) {
					user.close();
					return 0;
				}
				if (attr.getAppearance() >= maxAppearance && iterator.hasNext()) 
					continue;
				break;
			}
	
			//Choose the four highest priority SimpleAttributes to ask a question
			String[] options = new String[4];
			options = chooseFour(options);
			//If there are no options to give, the questions are done.
			if (options.length == 0) {
				user.close();
				return 0;
			}
			
			//Ask question and receive input
			System.out.println("Options: " + Arrays.toString(options));
			System.out.println("What is best?");
			String most = user.next();
			if(!(most.equals(options[0]) || most.equals(options[1]) 
					|| most.equals(options[2]) || most.equals(options[3]))) {
				user.close();
				throw new IllegalArgumentException("That is not one of the options.");
			}
			System.out.print("What is worst?");
			String least = user.next();
			if(!(least.equals(options[0]) || least.equals(options[1]) 
					|| least.equals(options[2]) || least.equals(options[3]))) {
				user.close();
				throw new IllegalArgumentException("That is not one of the options.");
			} else if(least.equals(most)) {
				user.close();
				throw new IllegalArgumentException("Worst cannot be chosen same as best.");
			}
			
			//Find middle Attributes
			String[] middle = new String[] {"", ""};
			int count = 0;
			for (String o : options) {
				if (!o.equals(most) && !o.equals(least)) {
					middle[count] = o;
					count++;
				}
				if (count > 1) {
					break;
				}
			}
			System.out.println(Arrays.toString(middle));
			
			//Update Lists
			updateLists(most, least, middle[0], middle[1]);
		}
		user.close();
		return 1;
	}
	
	/** Commented out until for now until it needs to be used.
	public int run(List<String> rank) {
		//If an SimpleAttribute has comparisons with all the other SimpleAttributes,
		//it is ranked and done.
		//If they all have comparisons to each other, all rankings are complete
		//and BWS process is done.
		Iterator<SimpleAttribute> iterator = attrAll.values().iterator();
		while(iterator.hasNext()) {
			SimpleAttribute attr = iterator.next();
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
					
		//Choose the four highest priority SimpleAttributes to ask a question
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
		Collection<SimpleAttribute> attrs = attrAll.values();
		Iterator<SimpleAttribute> iter = attrs.iterator();
		while(iter.hasNext()) {
			SimpleAttribute attribute = iter.next();
			System.out.println(attribute.getName() + ": " + attribute.getList().size());
		}
		
		//Return 0 for having run successfully
		return 0;
	}
	
	
		//Fill with  options that have more than 3 appearances if max options
		//have not been chosen.
		iter = priorities.iterator();
		while (iter.hasNext() && (optionsCount < max)) {
			SimpleAttribute current = iter.next();
			if(current.getName().equals(options[0]) 
					|| current.getName().equals(options[1]) 
					|| current.getName().equals(options[2])) {
				continue;
			}
			options[optionsCount] = current.getName();
			optionsCount++;
		}
		
		
		
	 * Make a matrix where the rows and columns are the Attributes. 
	 * The matrix has every possible relationship between two Attributes.
	 * Mark true for relationship that have already been considered.
	 * A relationship between an Attribute and itself is true because
	 * we already know the relationship for that.
	 * allPairs[i][j] is the same as allPairs[j][i] so mark true for
	 * one of them randomly to help randomize the matrix.
	 * 
	public void makeRelationshipMatrix() {
		allPairs = new boolean[priorities.size()][priorities.size()];
		Random rand = new Random();
		for (boolean[] row: allPairs)
			Arrays.fill(row, false);
		for (int i = 0; i < allPairs[0].length; i++) {
			for (int j = 0; j < allPairs[0].length; j++) {
				if (i == j) allPairs[i][j] = true;
				if (allPairs[j][i] || allPairs[i][j]) continue;
				allPairs[i][j] = rand.nextBoolean();
				allPairs[j][i] = !allPairs[i][j];
			}
		}
		for (boolean[] row: allPairs)
			System.out.println(Arrays.toString(row));
	}
	
	*/
	
	
	/**
	 * Current Priority method
	 * Choose one with lowest number of comparisons with other SimpleAttributes.
	 * If the only one that can be added is maxed out in comparisons, add blank.
	 * If there is only one option then send error.
	 * 
	 * Possible Priority method
	 * Does not have a comparison total of attrAll.size() - 1.
	 * Go in order of SimpleAttributes with longest lists and pick one that isn't in 
	 * current and target's lists until you have four picked out.
	 * Add blanks if needed.
	 * If there is only one option then send error.
	 */
	private String[] chooseFour(String[] options) {
		//If priorities set is empty, start asking questions with the best 
		//attributes
		System.out.println("sdfsdfas" + best.toString());
		
		//If there is only one Attribute to question, it cannot be questioned
		//to itself and has been questioned with the Attributes it needs to.
		//The questions are complete.
		for(SimpleAttribute simp : priorities) {
			System.out.println("verver" + simp.getName());
		}
		
		if (priorities.isEmpty()) {
			if (best.size() <= 1) {
				removed.addAll(best);
				if (!toAsk.isEmpty()) {
					Set<String> setToAdd = toAsk.removeFirst();
					for (String toAdd : setToAdd) {
						priorities.add(attrAll.get(toAdd));
					}
				}
			} else {
				for (String toAdd : best) {
					priorities.add(attrAll.get(toAdd));
				}
			}
			
			//Clear best and middle lists to keep track of the new bests and middles
			//Store the previous middles to ask in questions them later.
			best.clear();
			if (middle.size() >= 2) {
				toAsk.addFirst(new HashSet<String>(middle));
				System.out.println("midtransfer: " + middle.toString());
			}
			middle.clear();
		}
		
		if (removed.size() >= attrAll.size() - 1) return new String[]{};
		
		//Start choosing Attributes from priorities set to put in the question
		int optionsCount = 0;
		
		//Find whether there should be at most four or three Attributes in a question.
		int max;
		if (priorities.size() % 4 == 0) {
			max = 4;
		} else {
			max = 3;
		}
		
		//Fill with blanks in case four SimpleAttributes cannot be chosen.
		Arrays.fill(options, "");
		
		//Get a list of the SimpleAttributes sorted by fewest comparisons.
		//Set<Map.Entry<Integer, String>> attrByPrior = priorities.entrySet();
		
		//Iterate through list for options that still need to make comparisons 
		//and add them to the options list.
		Iterator<SimpleAttribute> iter = priorities.iterator();
		while (iter.hasNext() && (optionsCount < max)) {
			SimpleAttribute current = iter.next();
			System.out.println(current.getName() + ": " + current.getTally());
			if(current.getAppearance() >= maxAppearance) {
				removed.add(current.getName());
				continue;
			}
			options[optionsCount] = current.getName();
			optionsCount++;
		}
		
		//Check if all of there are no options.Also, there might be a case where all
		//all the attributes in priorities has appeared the max amount of times, so
		//clear priorities.
		if (optionsCount == 0) {
			for (SimpleAttribute maxed: priorities) {
				removed.add(maxed.getName());
			}
			priorities.clear();
			chooseFour(options);
		}
		
		//Throw an error if there is one or zero options, otherwise return options.
		if (optionsCount <= 1)
			throw new java.lang.Error("Not enough options to pose question.");
		return options;
	}
	
	/**
	 * Get a list of SimpleAttributes lower than middle1 and middle2, and least.
	 * Add most and most's lists to middle1 and middle2, and the ones lower 
	 * than middle1 and middle2 but not in least's lowest. The contains 
	 * function in SimpleAttribute's add function will take care of repeats.
	 * Combine middle1's and middle2's list and middle1 and 
	 * middle2. Add them to least and the ones lower than least.
	 */
	
	private void updateLists(String most, String least, String middle1, 
			String middle2) {
		attrAll.get(least).addToList(attrAll.get(most));
		if (!middle2.equals("")) attrAll.get(middle2).addToList(attrAll.get(most));
		if (!middle1.equals("")) attrAll.get(middle1).addToList(attrAll.get(most));
		if (!middle1.equals("")) attrAll.get(least).addToList(attrAll.get(middle1));
		if (!middle2.equals("")) attrAll.get(least).addToList(attrAll.get(middle2));
		
		//Update number of times appeared.
		attrAll.get(least).incrementAppearance();
		if (!middle2.equals("")) attrAll.get(middle2).incrementAppearance();
		if (!middle1.equals("")) attrAll.get(middle1).incrementAppearance();
		attrAll.get(most).incrementAppearance();
		
		//Update priorities list.
		removed.add(least);
		if (!middle1.equals("")) middle.add(middle1);
		if (!middle2.equals("")) middle.add(middle2);
		best.add(most);
		priorities.clear();
		priorities.addAll(attrAll.values());
		for (String toRemove : removed) {
			priorities.remove(attrAll.get(toRemove));
		}
		for (String toRemove : middle) {
			priorities.remove(attrAll.get(toRemove));
		}
		for (String toRemove : best) {
			priorities.remove(attrAll.get(toRemove));
		}
		for(SimpleAttribute simp : priorities) {
			System.out.println("before:" + simp.getName());
		}
		
		Iterator<Set<String>> iter = toAsk.iterator();
		while(iter.hasNext()) {
			System.out.println("here");
			for (String toRemove : iter.next()) {
				System.out.println(toRemove);
				priorities.remove(attrAll.get(toRemove));
			}
		}
		for(SimpleAttribute simp : priorities) {
			System.out.println("after" + simp.getName());
		}
		

		
		//Update done.
		return;
	}
	
public LinkedList<SimpleAttribute> getFinalAttributeList() {
	//Check if SimpleRank was run.
	if (ran == false) {
		throw new NullPointerException("The Attributes in this list "
				+ "have not yet been run.");
	} else {
		//Return the list
		return new LinkedList<SimpleAttribute>(attrAll.values());
	}
}
	
	/**
	 * Return a HashSet of SimpleAttributes that have lower ranks than the given 
	 * SimpleAttributes.
	 */
	/**
	private Set<String> getLowerSimpleAttributes(String[] attrs) {
		Set<String> l_attrs = new HashSet<String>();
		//Iterate through the SimpleAttributes and find ones that contain attr in
		//their lists of SimpleAttributes that have higher ranks.
		for (SimpleAttribute attr : attrAll.values()) {
			if (attr.containsAtLeastOne(attrs)) l_attrs.add(attr.getName());
		}
		return l_attrs;
	}
	*/
	
}