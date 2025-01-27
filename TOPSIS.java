package com.BWS.BWSweb;
import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

/**
 * This is the class for running TOPSIS.
 * It sorts items based on their attributes and a set weights.
 * It stores an unchangeable decision matrix that contains
 * the items and their values for each attribute. It takes in
 * a set of weights (a person's preferences that we get from BWS)
 * and orders the items with those weights applied. The class
 * allows for setting the weights and getting the rankings of
 * the items. The ranking returns the items in order of 
 * demonstrating the highest amounts of the most preferred 
 * attributes. 
 */
public class TOPSIS {
	//The decision matrix that contains the values for each item's 
	//attributes.
	double[][]  m_decision;
	//The number of rows in the decision matrix is the number of items.
	int items_length;
	//The number of columns in the decision matrix is the number of attributes.
	int attr_length;
	//The weights are the person's preferences for each attribute.
	double[] weights;
	//The ranking of the items in m_decision. The Integer is the index
	//of the item in weights, and the Double is the closeness value
	//to the ideal solution (the rank is determined by highest
	//closeness value).
	Map<Double, Integer> rankings;

	public TOPSIS(double[][]  d, double[] w) {
		//Check that the decision matrix has the same number of columns for
		//each row.
		for(int i = 0; i < d.length; i++) {
			if(d[i].length != d[0].length) 
				throw new IllegalArgumentException("Matrix given is does not "
						+ "have equal number of columns for each row.");
		}
		
		//Check that the number of weights matches the number of columns.
		if(w.length != d[0].length)
			throw new IllegalArgumentException("Length of weights array does "
					+ "not match number of columns in decision matrix.");
		
		//Check for null inputs.
		if(d.equals(null)) {
			throw new NullPointerException("Given matrix is null.");
		} else if(w.equals(null)) {
			throw new NullPointerException("Given weights is null");
		}
		
		//Set the matrix and weights.
		this.m_decision = d;
		this.weights = w;
		//Calculate how many attributes and how many items are in m_decision.
		//Matrices are row major.
		items_length = m_decision.length;
		attr_length = m_decision[0].length;
		
		//Initialize rankings
		rankings = new TreeMap<Double, Integer>(Collections.reverseOrder());
		
		//Call doTOPSIS to get the orderings.
		doTOPSIS();
	}
	
	private void doTOPSIS() {
		//Construct the normalized decision matrix.
		double[][]  m_normalized = new double[items_length][attr_length];
		for(int j = 0; j < attr_length; j++) {
			//Get the sum of each value squared for every column and then get
			//its square root.
			double rootSumSquare = 0;
			for(int i = 0; i < items_length; i++) {
				rootSumSquare += m_decision[i][j] * m_decision[i][j];
			}
			rootSumSquare = Math.sqrt(rootSumSquare);
			//Divide each item in the column by the rootSumSquare.
			for(int i = 0; i < items_length; i++) {
				if(rootSumSquare == 0) {
					m_normalized[i][j] = 0.0;
				} else {
					m_normalized[i][j] = m_decision[i][j] / rootSumSquare;
				}
			}
		}
		
		//Construct the weighted normalized matrix
		double[][] m_weighted = new double[items_length][attr_length];
		for(int i = 0; i < items_length; i++) {
			for(int j = 0; j < attr_length; j++) {
				m_weighted[i][j] = m_normalized[i][j] * weights[j];
			}
		}
				
		//Determine the ideal solution which is the largest value in each column
		//(different from the final ideal ordering of items).
		//Also determine the negative-ideal solution which is the smallest value 
		//in each column
		double[] ideal_solution = new double[attr_length];
		double[] neg_solution = new double[attr_length];
		for(int j = 0; j < attr_length; j++) {
			double max = m_weighted[0][j];
			double min = m_weighted[0][j];
			for(int i = 1; i <items_length; i++) {
				if(m_weighted[i][j] > max) max = m_weighted[i][j];
				if(m_weighted[i][j] < min) min = m_weighted[i][j];
			}
			ideal_solution[j] = max;
			neg_solution[j] = min;
		}

		//Calculate the separation measures
		double[] pos_separation = new double[items_length];
		double[] neg_separation = new double[items_length];
		
		for(int i = 0; i < items_length; i++) {
			double rootSepSum = 0.0;
			double rootNegSepSum = 0.0;
			for(int j = 0; j < attr_length; j++) {
				rootSepSum += Math.pow(m_weighted[i][j] 
						- ideal_solution[j], 2.0);
				rootNegSepSum += Math.pow(m_weighted[i][j] 
						- neg_solution[j], 2.0);
			}
			pos_separation[i] = Math.sqrt(rootSepSum);
			neg_separation[i] = Math.sqrt(rootNegSepSum);
		}
		
		//Calculate relative closeness to the ideal solution
		//(Close to ideal solution, far from negative-ideal solution).
		double[] pos_close = new double[items_length];
		for(int i = 0; i < items_length; i++) {
			if(neg_separation[i] == 0) {
				pos_close[i] = 0;
			} else {
				pos_close[i] = neg_separation[i] / (pos_separation[i] 
						+ neg_separation[i]);
			}
		}
		
		//Rank the preference order.
		rankings.clear();
		for(int i = 0; i < items_length; i++) {
			rankings.put(pos_close[i], i);
		}
		
		//Orderings done.
	}
	
	/**
	 * Set the new weights. When setWeights is called, 
	 * the ranking is cleared and recalculated;
	 */
	public void setWeights(double[] w) {
		//Check for null input.
		if(w.equals(null)) {
			throw new NullPointerException("Given weights is null");
		}
				
		this.weights = w;
		rankings.clear();
		
		
		//Calculate new rankings with TOPSIS.
		doTOPSIS();
	}
	
	/**
	 * Set a new decision matrix. All the variables except 
	 * weights are cleared and recalculated.
	 */
	public void setDecisionMatrix(double[][] m) {
		//Check that the decision matrix has the same number of columns for
		//each row.
		for(int i = 0; i < m.length; i++) {
			if(m[i].length != m[0].length) 
				throw new IllegalArgumentException("Matrix given is does not "
						+ "have equal number of columns for each row.");
		}
		//Check that the number of weights matches the number of columns.
		if(weights.length != m[0].length)
			throw new IllegalArgumentException("Number of columns in given "
					+ "matrix does not match length of weights array.");
		
		//Check for null input.
		if(m.equals(null)) {
			throw new NullPointerException("Given matrix is null.");
		}
		
		//Set new matrix.
		this.m_decision = m;
		
		//Calculate how many attributes and how many items are in m_decision.
		//Matrices are row major.
		items_length = m_decision.length;
		attr_length = m_decision[0].length;
		
		//Initialize ideal ranking
		rankings.clear();
		
		//Call doTOPSIS to get the orderings.
		doTOPSIS();
	}
	
	/**
	 * Return the weights.
	 */
	public double[] getWeights() {
		return this.weights;
	}
	
	/**
	 *Return the decision matrix.
	 */
	public double[][] getDecisionMatrix() {
		return this.m_decision;
	}
	
	/**
	 * Return the ranked items.
	 */
	public Map<Double, Integer> getRankings() {
		return this.rankings;
	}
}
