package BWS_app

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
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
public class SimpleMatrixRank {
	/**
	 * HashMap containing the complete list of SimpleAttributes to be ranked.
	 * Corresponding Map of ordering. Priority is determined by the total 
	 * number of comparisons an SimpleAttribute has with other SimpleAttributes.
	 * Smaller numbers have greater priority.
	 */
	private Map<String, SimpleAttribute> attrAll;
	private List<SimpleAttribute> ordering;
	private Set<SimpleAttribute> priorities;
	private boolean ran;
	private int maxAppearance;
	boolean[][] allPairs;
	
	public SimpleMatrixRank(List<SimpleAttribute> attrList) {
		//If there are less than four Attributes throw error.
		if (attrList.size() < 4) {
			throw new IllegalArgumentException("Attribute list does not "
					+ "contain at least four Attributes.");
		}
		
		//Initialize global variables
		this.attrAll = new HashMap<String, SimpleAttribute>();
		this.priorities = new TreeSet<SimpleAttribute>();
		this.ordering = new ArrayList<SimpleAttribute>();
		this.ran = false;
		this.maxAppearance = 3;
		
		//Adding the SimpleAttributes to the SimpleAttributes map.
		for (SimpleAttribute attr : attrList) {
			attrAll.put(attr.getName(), attr);
		}
		
		//Adding Attributes to priorities TreeSet
		priorities.addAll(attrList);
		
		//Adding Attributes to ordering list.
		ordering.addAll(attrList);
		makeRelationshipMatrix();
	}
	
	public SimpleMatrixRank(List<SimpleAttribute> attrList, int maxAppearance) {
		//If there are less than four Attributes throw error.
		if (attrList.size() < 4) {
			throw new IllegalArgumentException("Attribute list does not "
					+ "contain at least four Attributes.");
		}
		
		//Initialize global variables
		this.attrAll = new HashMap<String, SimpleAttribute>();
		this.priorities = new TreeSet<SimpleAttribute>();
		this.ordering = new ArrayList<SimpleAttribute>();
		this.ran = false;
		this.maxAppearance = maxAppearance;
		
		//Adding the SimpleAttributes to the SimpleAttributes map.
		for (SimpleAttribute attr : attrList) {
			attrAll.put(attr.getName(), attr);
		}
		
		//Adding Attributes to priorities TreeSet
		priorities.addAll(attrList);
		
		//Adding Attributes to ordering list.
		ordering.addAll(attrList);
		makeRelationshipMatrix();
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
		for(int i = 0; i <= Math.ceil((float)(attrAll.size() * 3)/4.0f) + 1; i++) {
			Iterator<SimpleAttribute> iterator = attrAll.values().iterator();
			while(iterator.hasNext()) {
				SimpleAttribute attr = iterator.next();
				//Return 0 for completing ranking and having run successfully.
				if ((attr.getAppearance() >= maxAppearance || (attr.getTally()
						>= (ordering.size() - 1))) && !iterator.hasNext()) {
					user.close();
					return 0;
				}
				if ((attr.getAppearance() >= maxAppearance || (attr.getTally()
						>= (ordering.size() - 1))) && iterator.hasNext()) 
					continue;
				break;
			}
	
			//Choose the four highest priority SimpleAttributes to ask a question
			String[] options = new String[4];
			options = chooseFour(options);
			
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
		int optionsCount = 0;
		//Fill with blanks in case four SimpleAttributes cannot be chosen.
		Arrays.fill(options, "");
		
		//Get a list of the SimpleAttributes sorted by fewest comparisons.
		//Set<Map.Entry<Integer, String>> attrByPrior = ordering.entrySet();
		
		//Iterate through list for options that still need to make comparisons 
		//and add them to the options list.
		
		//Get the first 1 or two pairs
		outer:
		for (SimpleAttribute attr : priorities) {
			for (SimpleAttribute attrB : priorities) {
				if (!allPairs[ordering.indexOf(attr)][ordering.indexOf(attrB)]) {
					if (!(attr.getAppearance() >= maxAppearance
							||attrB.getAppearance() >= maxAppearance
							|| attr.getName().equals(options[0]) 
							|| attr.getName().equals(options[1]) 
							|| attr.getName().equals(options[2]))
							&& optionsCount < 3) {
						options[optionsCount] = attr.getName();
						optionsCount++;
						if (!(attrB.getName().equals(options[0]) 
								|| attrB.getName().equals(options[1]) 
								|| attrB.getName().equals(options[2]))
								&& optionsCount < 4) {
							options[optionsCount] = attrB.getName();
							optionsCount++;
						}
					}
					else if (attr.getAppearance() < maxAppearance
							&& attrB.getAppearance() < maxAppearance
							&& optionsCount < 4
							&& !(attrB.getName().equals(options[0]) 
								|| attrB.getName().equals(options[1]) 
								|| attrB.getName().equals(options[2]))) {
						options[optionsCount] = attrB.getName();
						optionsCount++;
					}
				}
				if (optionsCount > 3) break outer;
			}
		}
		//Get ones where one of them has not reached max appearance.
		outer2:
			for (SimpleAttribute attr : priorities) {
				for (SimpleAttribute attrB : priorities) {
					if (!allPairs[ordering.indexOf(attr)][ordering.indexOf(attrB)] 
							|| !allPairs[ordering.indexOf(attrB)][ordering.indexOf(attr)]) {
						if (!(attr.getAppearance() >= maxAppearance
								|| attr.getName().equals(options[0]) 
								|| attr.getName().equals(options[1]) 
								|| attr.getName().equals(options[2]))
								&& optionsCount <= 2) {
							options[optionsCount] = attr.getName();
							optionsCount++;
							if (!(attrB.getName().equals(options[0]) 
									|| attrB.getName().equals(options[1]) 
									|| attrB.getName().equals(options[2]))
									&& optionsCount < 4) {
								options[optionsCount] = attrB.getName();
								optionsCount++;
							}
						}
					}
					if (optionsCount > 3) break outer2;
				}
			}
		/**
		//If there are no options, then there is a letter that has not appeared
		//the maximum amount of times, but all it's relationships are true.
		for(int i = 0; i < ordering.size(); i++) {
			System.out.println(ordering.get(i).getName() + ": " + ordering.get(i).getAppearance() + "tally: " + ordering.get(i).getTally());
		}
		*/
		for(int i = 0; i < ordering.size(); i++) {
			System.out.println(Arrays.toString(allPairs[i]));
		}
				
		//If there is only 1 option, get its pair that has over the max appearances.
		if(optionsCount == 1) {
			for (int i = 0; i < ordering.size(); i++) {
				if (!allPairs[i][ordering.indexOf(attrAll.get(options[0]))]
						|| !allPairs[ordering.indexOf(attrAll.get(options[0]))][i]) {
					if (!(ordering.get(i).getName().equals(options[0]))) {
						options[optionsCount] = ordering.get(i).getName();
						optionsCount++;
						break;
					}
				}
			}
		}

		//Throw an error if there is one or zero options, otherwise return options.
		if (optionsCount <= 1)
			throw new java.lang.Error("Not enough options to pose question: " + optionsCount);
		return options;
	}
	
	private String[] chooseFour(String[] options, List<String> toAsk) {
		return options;
	}
	
	/**
	 * Make a matrix where the rows and columns are the Attributes. 
	 * The matrix has every possible relationship between two Attributes.
	 * Mark true for relationship that have already been considered.
	 * A relationship between an Attribute and itself is true because
	 * we already know the relationship for that.
	 * allPairs[i][j] is the same as allPairs[j][i] so mark true for
	 * one of them randomly to help randomize the matrix.
	 */
	public void makeRelationshipMatrix() {
		allPairs = new boolean[ordering.size()][ordering.size()];
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
		//Add Attributes to lists and update number of times appeared.
		attrAll.get(least).addToList(attrAll.get(most));
		allPairs[ordering.indexOf(attrAll.get(least))][ordering.indexOf(attrAll.get(most))] = true;
		allPairs[ordering.indexOf(attrAll.get(most))][ordering.indexOf(attrAll.get(least))] = true;
		if(!middle1.equals("")) {
			attrAll.get(middle1).addToList(attrAll.get(most));
			attrAll.get(least).addToList(attrAll.get(middle1));
			attrAll.get(middle1).incrementAppearance();
			allPairs[ordering.indexOf(attrAll.get(middle1))][ordering.indexOf(attrAll.get(least))] = true;
			allPairs[ordering.indexOf(attrAll.get(least))][ordering.indexOf(attrAll.get(middle1))] = true;
			allPairs[ordering.indexOf(attrAll.get(middle1))][ordering.indexOf(attrAll.get(most))] = true;
			allPairs[ordering.indexOf(attrAll.get(most))][ordering.indexOf(attrAll.get(middle1))] = true;
			if(!middle2.equals("")) {
				attrAll.get(middle2).addToList(attrAll.get(most));
				attrAll.get(least).addToList(attrAll.get(middle2));
				attrAll.get(middle2).incrementAppearance();
				allPairs[ordering.indexOf(attrAll.get(middle2))][ordering.indexOf(attrAll.get(least))] = true;
				allPairs[ordering.indexOf(attrAll.get(least))][ordering.indexOf(attrAll.get(middle2))] = true;
				allPairs[ordering.indexOf(attrAll.get(middle2))][ordering.indexOf(attrAll.get(most))] = true;
				allPairs[ordering.indexOf(attrAll.get(most))][ordering.indexOf(attrAll.get(middle2))] = true;
			}
		}
		attrAll.get(least).incrementAppearance();
		attrAll.get(most).incrementAppearance();
		
		//Update priorities list.
		priorities.clear();
		priorities.addAll(ordering);

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
