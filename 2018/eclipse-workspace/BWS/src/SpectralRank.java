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

}

