package com.BWS.BWSweb;
import java.util.Arrays;
import java.util.List;



/** This class implements the spectral ranking method.
 * It uses a Markov chain with of the items and edges for
 * their comparisons. The rankings are calculated by randomly
 * walking through the chain until the rankings do not change.
 *
 */
//Make a function to create epsilon edges
public class SpectralRank {
	/**
	//This list contains all the Attributes that are in the Markov chain.
	List<SimpleAttribute> attrList;
	//This map records all the epsilon edges in the Markov chain. 
	//The set of string are Attributes having an epsilon edge going to key.
	Map<String, Set<String>> epsilons = new HashMap<String,Set<String>>();
	//rankValues is the values of the attirbutes after running ASR. They
	//in order of attrList;
	double[] rankValues;
	*/
	
public SpectralRank(List<SimpleAttribute> l) {
	//this.attrList = l;
	//doSpectralRank();
}

public static double[] doSpectralRank(List<SimpleAttribute> attrList) {
	//AttrList contains all the Attributes that are in the Markov chain.
	
	//This map records all the epsilon edges in the Markov chain. 
	//The set of string are Attributes having an epsilon edge going to key.
	
	//rankValues is the values of the Attributes after running ASR. They
	//in order of attrList;
	double[] rankValues;
		
	double[][] m_probability = new double[attrList.size()][attrList.size()];
	rankValues = new double[attrList.size()];
	//Create epsilon edges for attributes that don't have any ins and don't have any outs.
	for(int i = 0; i < attrList.size(); i++) {
		Arrays.fill(m_probability[i], 0.0);
	}
	
	//Make matrix.
	Arrays.fill(rankValues, 1.0 / (double)attrList.size());
	for(int i = 0; i < attrList.size(); i++) {
		for(int j = 0; j < attrList.size(); j++) {
			double aij = attrList.get(i).getCountOf(attrList.get(j));
			double aji = attrList.get(j).getCountOf(attrList.get(i));
			double di = attrList.get(i).getTally();
			
			//Add the epsilon values
			if (aij + aji != 0) {
				aij += 0.01;
				aji += 0.01;
			}
			
			if (aij == 0) {
				m_probability[i][j] = 0;
			} else {
				m_probability[i][j] = (1.0 / di) * (aij / (aij + aji));
			}
		}
	}
	
	//Get the rank values for each attribute (weights).
	double[] updatedValues = new double[attrList.size()];
	while(!Arrays.equals(rankValues, updatedValues)) {
		//Multiply by the array of values for each attribute.
		//(Make a walk).
		for(int j = 0; j < attrList.size(); j++) {
			for(int i = 0; i <attrList.size(); i++) {
				updatedValues[j] += m_probability[i][j] * rankValues[i];
			}
		}
		rankValues = updatedValues;
	}
	return rankValues;
}

/**
public double[] getRankValues() {
	return this.rankValues;
}
*/


//This implementation of the epsilon edges was wrong. Also, the compareTo for
//Simple Attribute was changed from comparing tallies to appearances, so this
//doesn't work.
/**
private static void createEpsilonEdges(List<SimpleAttribute> attrList, 
		Map<String, Set<String>> epsilons) {
	Set<SimpleAttribute> wants_in = new TreeSet<SimpleAttribute>();
	Set<SimpleAttribute> wants_out = new TreeSet<SimpleAttribute>();
	//Get Attributes that have edges going all in or all out.
	for(SimpleAttribute attr : attrList) {
		if(attr.getTally() == attr.getList().size()) {
			wants_in.add(attr);
		} else if(attr.getList().size() == 0) {
			wants_out.add(attr);
		}
	}
	
	//Check for empty sets
	if(wants_in.isEmpty() && wants_out.isEmpty()) return;
	
	//Get the difference in sizes.
	int count = wants_in.size() - wants_out.size();
	
	//Try to get the two sets to have equal sizes.
	for(SimpleAttribute attr : attrList) {
		if (count == 0) break;
		if (!wants_in.contains(attr) && !wants_out.contains(attr)) {
			if(count > 0) {
				wants_out.add(attr);
				count--;
			} else {
				wants_in.add(attr);
				count++;
			}
		}
	}

	Iterator<SimpleAttribute> itr_in = wants_in.iterator();
	Iterator<SimpleAttribute> itr_out = wants_out.iterator();
	//When wants_in's size is greater or equal to wants_out.
	if(count >= 0) {
		while(itr_in.hasNext()) {
			SimpleAttribute in = itr_in.next();
			SimpleAttribute out = itr_out.next();
			//Add the edge from out to in.
			if(epsilons.containsKey(in.getName())) {
				epsilons.get(in.getName()).add(out.getName());
			} else {
				Set<String> out_temp = new HashSet<String>();
				out_temp.add(out.getName());
				epsilons.put(in.getName(), out_temp);
			}
			//Add edge from in to out.
			if(epsilons.containsKey(out.getName())) {
				epsilons.get(out.getName()).add(in.getName());
			} else {
				Set<String> in_temp = new HashSet<String>();
				in_temp.add(in.getName());
				epsilons.put(out.getName(), in_temp);
			}
			if(!itr_out.hasNext()) {
				itr_out = wants_out.iterator();
			}
		}
	//When want_out's size is greater than wants_in.
	} else {
		while(itr_out.hasNext()) {
			SimpleAttribute in = itr_in.next();
			SimpleAttribute out = itr_out.next();
			//Add the edge from out to in.
			if(epsilons.containsKey(in.getName())) {
				epsilons.get(in.getName()).add(out.getName());
			} else {
				Set<String> out_temp = new HashSet<String>();
				out_temp.add(out.getName());
				epsilons.put(in.getName(), out_temp);
			}
			//Add edge from in to out.
			if(epsilons.containsKey(out.getName())) {
				epsilons.get(out.getName()).add(in.getName());
			} else {
				Set<String> in_temp = new HashSet<String>();
				in_temp.add(in.getName());
				epsilons.put(out.getName(), in_temp);
			}
			if(!itr_in.hasNext()) {
				itr_in = wants_in.iterator();
			}
		}
	}
}

*/


}
