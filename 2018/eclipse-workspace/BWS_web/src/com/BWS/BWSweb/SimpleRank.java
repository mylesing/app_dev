package com.BWS.BWSweb;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;

/**
 * The BWS Ranking process. It takes four SimpleAttributes taking
 * into account their priorities to be asked and asks the
 * question. It then ranks and assigns new priorities to
 * the SimpleAttributes.
 */
public class SimpleRank {
	/**
	 * HashMap containing the complete list of SimpleAttributes to be ranked.
	 * Corresponding Map of priorities. Priority is determined by the total 
	 * number of comparisons an SimpleAttribute has with other SimpleAttributes.
	 * Smaller numbers have greater priority.
	 */
	// complete list of simple attributes to be ranked 
	private Map<String, SimpleAttribute> attrAll;
	
	// corresponding list of priorities 
	// priority determined by the total number of comparisons an attribute has w other attributes
	// less comparisons = greater priority
	private Set<SimpleAttribute> priorities;
	
	private Set<SimpleAttribute> lastQuestion;
	private Set<String> removed;
	private Set<String> middle;
	private Set<String> best;
	private HashSet<Set<String>> toAsk;
	private boolean ran;
	private int maxAppearance;
	private int maxTally;
	private int itemsPerQuestion;
	
	public SimpleRank(List<SimpleAttribute> attrList) {
		//If there are less than 7 Attributes throw error.
		if (attrList.size() < 7) {
			throw new IllegalArgumentException("Attribute list does not "
					+ "contain enough Attributes.");
		}
		
		//Initialize global variables
		this.attrAll = new HashMap<String, SimpleAttribute>();
		this.priorities = new TreeSet<SimpleAttribute>();
		this.lastQuestion = new TreeSet<SimpleAttribute>();
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
		this.toAsk = new HashSet<Set<String>>();
		//The maximum number of times an Attribute can appear in a question.
		//The default is 3.
		this.maxAppearance = 3;
		this.itemsPerQuestion = 3;
		
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
		makeQuestions(0);
		for (SimpleAttribute attr : attrList) {
			attr.clear();
		}
		priorities.clear();
		priorities.addAll(attrList);
	}
	
	public SimpleRank(List<SimpleAttribute> attrList, int maxAppearance, int maxQuestions) {
		//If there are less than four Attributes throw error.
		if (attrList.size() < 4) {
			throw new IllegalArgumentException("Attribute list does not "
					+ "contain at least four Attributes.");
		}
		
		//Initialize global variables
		this.attrAll = new HashMap<String, SimpleAttribute>();
		this.priorities = new TreeSet<SimpleAttribute>();
		this.lastQuestion = new TreeSet<SimpleAttribute>();
		this.ran = false;
		this.removed = new HashSet<String>();
		this.middle = new HashSet<String>();
		this.best = new HashSet<String>();
		this.toAsk = new HashSet<Set<String>>();
		this.maxAppearance = maxAppearance;
		this.itemsPerQuestion = 3;
		
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
		makeQuestions(maxQuestions);
		for (SimpleAttribute attr : attrList) {
			attr.clear();
		}
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
		
		for (Set<String> question : toAsk) {
			//Choose the four highest priority SimpleAttributes to ask a question
			//If there are no options to give, the questions are done.
			if (question.isEmpty()) {
				user.close();
				return 1;
			}
			
			//Ask question and receive input
			System.out.println("Options: " + Arrays.toString(question.toArray()));
			System.out.println("What is best?");
			String most = user.next();
			if(!question.contains(most)) {
				user.close();
				throw new IllegalArgumentException("That is not one of the options.");
			}
			System.out.print("What is worst?");
			String least = user.next();
			if(!question.contains(least)) {
				user.close();
				throw new IllegalArgumentException("That is not one of the options.");
			} else if(least.equals(most)) {
				user.close();
				throw new IllegalArgumentException("Worst cannot be chosen same as best.");
			}
			
			
			//Update Lists
			update(most, least, question);
		}
		user.close();
		return 0;
	}
	
	public int runDynamic() {
		this.ran = true;
		//If an SimpleAttribute has comparisons with all the other SimpleAttributes,
		//it is ranked and done.
		//If they all have comparisons to each other, all rankings are complete
		//and BWS process is done.
		Scanner user = new Scanner(System.in);
		int count = (int) Math.ceil((double)(attrAll.size() * maxAppearance) / itemsPerQuestion);
		while (count > 0) {
			Set<String> question = makeQuestionsDynamic();
			//Choose the four highest priority SimpleAttributes to ask a question
			//If there are no options to give, the questions are done.
			if (question.isEmpty()) {
				user.close();
				return 0;
			}
			
			//Ask question and receive input
			System.out.println("Options: " + Arrays.toString(question.toArray()));
			System.out.println("What is best?");
			String most = user.next();
			if(!question.contains(most)) {
				user.close();
				throw new IllegalArgumentException("That is not one of the options.");
			}
			System.out.print("What is worst?");
			String least = user.next();
			if(!question.contains(least)) {
				user.close();
				throw new IllegalArgumentException("That is not one of the options.");
			} else if(least.equals(most)) {
				user.close();
				throw new IllegalArgumentException("Worst cannot be chosen same as best.");
			}
			
			
			//Update Lists
			update(most, least, question);
			count--;
		}
		user.close();
		return 0;
	}
	
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
	private void makeQuestions(int num) {
		// map rankings<Integer, Set> : attributes are mapped to priority values in sets
		// while (rankings.size < numAttributes)
		// 1. find the first set of attributes in the map of size > 1
		// 2. if set has 4+ attributes, pick first 4 and add them to the set of questions
		// 		otherwise, make a question out of all of the sets
		// 3. as user to identify best and worst attributes
		// 		if best, +1 the priority and add it to the relevant set in map
		//		if worst, -1 the priority and add it the the relevant set in map
		// 4. add attributes that rank lower than best to best's attrList
		//		also add worst to the middle attr's lists
		// 5. update map and ask next question
		
//		Map<Integer, Set<SimpleAttribute>> rankings = new HashMap<Integer, Set<SimpleAttribute>>();
//		Set<SimpleAttribute> attr_set = priorities;
//		rankings.put(0, attr_set);
//		
//		while (rankings.size() < priorities.size()) {
//			 Iterator it = rankings.entrySet().iterator();
//			 while (it.hasNext()) {
//				 Map.Entry pair = (Map.Entry)it.next();
//				 Set<SimpleAttribute> curr_set = (Set<SimpleAttribute>)pair.getValue();
//				 if (curr_set.size() > 1) {
//					 Set<SimpleAttribute> question = new TreeSet<SimpleAttribute>();
//					 if (curr_set.size() <= 4) {
//						 question.addAll(curr_set);
//					 } else {
//						 int ct = 0;
//						 Iterator set_it = curr_set.iterator();
//						    while (set_it.hasNext() && ct < 4) {
//						        question.add((SimpleAttribute) set_it.next());
//						        ct++;
//						    }
//						 
//					 }
//				 } 
//			     
//			 }
//		}
//		
		
		// let there be 
		
		
		//Find max number of options per question
		int max;
		if (num == 0) {
			if (attrAll.size() < 9) {
				max = 3;
			} else if (attrAll.size() < 11) {
				max = 4;
			} else {
				max = 5;
			}
		} else {
			max = num;
		}
		//Make the questions
		//List<SimpleAttribute> list = new LinkedList<SimpleAttribute>(attrAll.values());
		//Collections.shuffle(list);
		//Iterator<SimpleAttribute> iter = list.iterator();
		boolean first = true;
		LinkedList<Set<String>> tempStore = new LinkedList<Set<String>>();
		while (true) {
			// iterate through priorities
			SimpleAttribute start = priorities.iterator().next();
			// tempStore starts off empty
			// we go through each set in tempStore and add it to the sort set
			// the set is sorted in reverse order (since less comparisons = less priority)
			for (Set<String> q : tempStore) {
				TreeSet<SimpleAttribute> sort = new TreeSet<SimpleAttribute>(Collections.reverseOrder());
				// 
				for (String attr : q) {
					sort.add(attrAll.get(attr));
				}
				
				for (SimpleAttribute attr : sort) {
					if (attr.getAppearance() < maxAppearance) {
						start = attr;
						break;
					}
				}
			}
			// for testing
			System.out.println(start.getName());
			
			//Make a question
			if ((start.getAppearance() > 0 || first)) {
				// for testing purposes
				System.out.println(start.getName());
				for (SimpleAttribute attr : priorities) {
					System.out.println(attr.getName() + ": "  + attr.getAppearance());
				}
				
				first = false;
				//Add the first option to the question
				int count = 1;
				Set<String> question = new HashSet<String>();
				question.add(start.getName());
				//Add more Attributes that haven't met each other to the question.
				OUTER:
				for (SimpleAttribute attr : priorities) {
					System.out.println(attr.getName());
					//Check if it is a viable option
					boolean canOption = true;
					for (String option : question) {
						if (attr.getName().equals(option)
								|| attr.getAppearance() >= maxAppearance) {
							canOption = false;
						} else {
							for (Set<String> q : toAsk) {
								if (q.contains(option) && q.contains(attr.getName())) {
									System.out.println("11111111");
									canOption = false;
								}
							}
						}
					}
					//Add the Attribute to the question.
					if (canOption) {
						question.add(attr.getName());
						count++;
						if (count == max) {
							toAsk.add(question);
							tempStore.addFirst(question);
							//Update with arbitrary best/worst choices to temporarily
							//update lists for making the next questions.
							update(start.getName(), attr.getName(), question);
							start = attrAll.get(start.getName());
							break OUTER;
						}
					}
				}
				//toAsk.add(question);
			}
			
			if(toAsk.size() >= (attrAll.size()*maxAppearance/max) - 3) {
				return;
			}
		}
	}
	
	private Set<String> makeQuestionsDynamic() {
		//Find max number of options per question
		int max;
		int cap;
		if (attrAll.size() < 9) {
			max = 3;
		} else if (attrAll.size() < 11) {
			max = 4;
		} else {
			max = 5;
		}
		cap = max - 1;
		//Set max number of attributes it can be compared to. 
		//By giving the maxAppearance they are telling us how many
		//comparisons with other attributes they want to make.
		//Compute the average number of attributes each attribute
		//should be compared too.
		System.out.println("maxTally:" + maxTally);
		this.maxTally = (int)((double)maxAppearance * (2.0 * ((double)max 
				- 1.0) + ((double)max - 2.0) * 2.0) / (double)max);
		//Make the questions---
		//Iterate through the list
		Iterator<SimpleAttribute> iter = priorities.iterator();
		System.out.println();
		for (SimpleAttribute attr : priorities) {
			System.out.print(attr.getName());
		}
		System.out.println();
		while (iter.hasNext()) {
			SimpleAttribute start = iter.next();
			//Make a question
			//Don't use attribute if it has made more comparisons with other
			//attributes than the maximum times it can
			if (start.getTally() <= maxTally - cap) {
				//Use the attribute since it has made fewer comparisons with other
				//attributes than the maximum times it can and put it in the question.
				Set<String> question = new HashSet<String>();
				question.add(start.getName());
				//Add more Attributes that haven't met each other to the question.
				for (SimpleAttribute attr : priorities) {
					System.out.println(start.getName() + attr.getName() + attr.getWorst());
					//Check if it is a viable option
					boolean canOption = true;
					for (String option : question) {
						if (attrAll.get(option).getList().contains(attr.getName()) 
								|| (attr.getList().contains(option))
								|| attr.getName().equals(option)
								|| attr.getTally() > maxTally - cap) {
							canOption = false;
						}
					}
					//Add the Attribute to the question.
					if (canOption) {
						System.out.println("		" + start.getName() + attr.getName());
						question.add(attr.getName());
						System.out.println(max + " " + question.size());
						if (question.size() == max) {
							for(String option : question) attrAll.get(option).incrementAppearance();
							return question;
						}
					}
				}
			}
			if(!iter.hasNext() && max > 1) {
				max--;
				cap--;
				iter = priorities.iterator();
			}
		}
		return new HashSet<String>();
	}
	
	/**
	 * Add attributes to the respective lists. An Attribute's list contains
	 * Attributes that are ranked higher than it.
	 * Update the priorities list.
	 */
	
	private void update(String most, String least, Set<String> question) {
		updateLists(most, least, question);
		
		//Update priorities list.
		priorities.clear();
		priorities.addAll(attrAll.values());
		
		//Update done.
		return;
	}
	
	private void updateLists(String most, String least, Set<String> question) {
		//Find middle Attributes
		String middle1 = "";
		String middle2 = "";
		String middle3 = "";
		for (String o : question) {
			if (!o.equals(most) && !o.equals(least)) {
				if (!middle1.isEmpty() && middle2.isEmpty()) middle2 = o;
				if (middle1.isEmpty()) middle1 = o;
			}
		}
		
		//Update best and worst attributes
		attrAll.get(most).setBest();
		attrAll.get(least).setWorst();
				
		//Update Lists
		attrAll.get(least).addToList(attrAll.get(most));
		if (!middle2.equals("")) attrAll.get(middle2).addToList(attrAll.get(most));
		if (!middle1.equals("")) attrAll.get(middle1).addToList(attrAll.get(most));
		if (!middle1.equals("")) attrAll.get(least).addToList(attrAll.get(middle1));
		if (!middle2.equals("")) attrAll.get(least).addToList(attrAll.get(middle2));
		if (!middle3.equals("")) attrAll.get(middle3).addToList(attrAll.get(most));
		if (!middle3.equals("")) attrAll.get(least).addToList(attrAll.get(middle3));
		
		//Increment Appearances
		attrAll.get(most).incrementAppearance();
		attrAll.get(least).incrementAppearance();
		if (!middle1.equals("")) attrAll.get(middle1).incrementAppearance();
		if (!middle2.equals("")) attrAll.get(middle2).incrementAppearance();
		if (!middle3.equals("")) attrAll.get(middle3).incrementAppearance();
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
	
	public HashSet<Set<String>> getToAsk() {
		return toAsk;
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
