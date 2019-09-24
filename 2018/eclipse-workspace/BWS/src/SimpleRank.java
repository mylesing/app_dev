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
	private Map<String, SimpleAttribute> attrAll;
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
	private void makeQuestions(int num) {
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
			SimpleAttribute start = priorities.iterator().next();
			for (Set<String> q : tempStore) {
				TreeSet<SimpleAttribute> sort = new TreeSet<SimpleAttribute>(Collections.reverseOrder());
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
			System.out.println(start.getName());
			//Make a question
			
			// if the appearance of the "start" attribute is greater than 0 or if we're on the first attribute
			// add first option to question
			// go through the list of attributes in priorities 
			// if the attribute does not share a name and / or has less appearance than maxAppearance OR is already in the list of questions that need to be asked
			// 				-> it is not a viable option
			// otherwise, it is a viable option 
			// if the attribute is a viable option, add it to te question and increment count
			// if we've maxed out the number of attributes we can ask about in this question, 
			// we add it to the list of questions we have to ask from in the future
			if ((start.getAppearance() > 0 || first)) {
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
