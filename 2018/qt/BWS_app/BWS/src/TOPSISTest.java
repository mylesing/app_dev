package BWS_app

import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 *Testing TOPSIS class
 */
class TOPSISTest {
	double[][] m_decision;
	double[] weights;
	TOPSIS topsis;
	
	@Test
	protected void testTOPSISIrregularColumns() {
		m_decision = new double[4][7];
		m_decision[1] = new double[8];
		weights = new double[7];
		Assertions.assertThrows(IllegalArgumentException.class,
				() -> new TOPSIS(m_decision, weights));
	}
	
	@Test
	protected void testTOPSISMismatchWeightsAndMatrix() {
		m_decision = new double[4][7];
		weights = new double[8];
		Assertions.assertThrows(IllegalArgumentException.class,
				() -> new TOPSIS(m_decision, weights));
	}
	
	@Test
	protected void testTOPSISNull() {
		m_decision = new double[4][7];
		weights = new double[7];
		Assertions.assertThrows(NullPointerException.class,
				() -> new TOPSIS(null, weights));
		Assertions.assertThrows(NullPointerException.class,
				() -> new TOPSIS(m_decision, null));
	}
	
	@Test
	public void testTOPSIS() {
		m_decision = new double[4][7];
		weights = new double[7];

		topsis = new TOPSIS(m_decision, weights);
	}
	
	@Test
	public void testGetWeights() {
		m_decision = new double[4][7];
		weights = new double[] {0.2, 0.1, 0.1, 0.1, 0.2, 0.3, 0.3};
		topsis = new TOPSIS(m_decision, weights);
		
		assertTrue(Arrays.equals(new double[] {0.2, 0.1, 0.1, 0.1, 0.2, 0.3, 0.3}, topsis.getWeights()));
	}
	
	@Test
	public void testGetDecisionMatrix() {
		m_decision = new double[4][6];
		m_decision[0] = new double[] {2.0, 1500, 20000, 5.5, 5, 9};
		m_decision[1] = new double[] {2.5, 2700, 18000, 6.5, 3, 5};
		m_decision[2] = new double[] {1.8, 2000, 21000, 4.5, 7, 7};
		m_decision[3] = new double[] {2.2, 1800, 20000, 5, 5, 5};
		weights = new double[] {0.2, 0.1, 0.1, 0.1, 0.2, 0.3};
		topsis = new TOPSIS(m_decision, weights);
		
		double[][] m = {new double[] {2.0, 1500, 20000, 5.5, 5, 9}, 
			new double[] {2.5, 2700, 18000, 6.5, 3, 5}, 
			new double[] {1.8, 2000, 21000, 4.5, 7, 7},
			new double[] {2.2, 1800, 20000, 5, 5, 5}};
		assertTrue(Arrays.deepEquals(m, topsis.getDecisionMatrix()));
		}
	
	@Test
	public void testGetRankings() {
		m_decision = new double[4][6];
		m_decision[0] = new double[] {2.0, 1500, 20000, 5.5, 5, 9};
		m_decision[1] = new double[] {2.5, 2700, 18000, 6.5, 3, 5};
		m_decision[2] = new double[] {1.8, 2000, 21000, 4.5, 7, 7};
		m_decision[3] = new double[] {2.2, 1800, 20000, 5, 5, 5};
		weights = new double[] {0.2, 0.1, 0.1, 0.1, 0.2, 0.3};
		topsis = new TOPSIS(m_decision, weights);
		
		Map<Double, Integer> r = new TreeMap<Double, Integer>(Collections.reverseOrder());
		r.put(0.6432770005532701, 0);
		r.put(0.2870664830977208, 1);
		r.put(0.5971556333009952, 2);
		r.put(0.3015667905557404, 3);
		for(Map.Entry<Double, Integer> entry : topsis.getRankings().entrySet()) {
			System.out.println("Closeness: " + entry.getKey() + "   Item: " + entry.getValue());
		}
		assertTrue(r.equals(topsis.getRankings()));
	}
	
	@Test
	public void testSetWeights() {
		m_decision = new double[4][6];
		weights = new double[] {0.2, 0.1, 0.1, 0.1, 0.2, 0.3};
		topsis = new TOPSIS(m_decision, weights);
		
		topsis.setWeights(new double[] {0.3, 0.1, 0.1, 0.1, 0.3, 0.3});
		assertTrue(Arrays.equals(new double[] {0.3, 0.1, 0.1, 0.1, 0.3, 0.3}, topsis.getWeights()));
	}
	
	@Test
	public void testSetWeightsNull() {
		m_decision = new double[4][7];
		weights = new double[7];
		topsis = new TOPSIS(m_decision, weights);
		
		Assertions.assertThrows(NullPointerException.class, () -> topsis.setWeights(null));
	}
	
	@Test
	public void testSetDecisionMatrix() {
		m_decision = new double[4][6];
		weights = new double[] {0.2, 0.1, 0.1, 0.1, 0.2, 0.3};
		topsis = new TOPSIS(m_decision, weights);
		
		topsis.setDecisionMatrix(new double[][] {new double[] {2.0, 1500, 20000, 5.5, 5, 9}, 			
			new double[] {2.5, 2700, 18000, 6.5, 3, 5}, 
			new double[] {1.8, 2000, 21000, 4.5, 7, 7},
			new double[] {2.2, 1800, 20000, 5, 5, 5}});
		m_decision[0] = new double[] {2.0, 1500, 20000, 5.5, 5, 9};
		m_decision[1] = new double[] {2.5, 2700, 18000, 6.5, 3, 5};
		m_decision[2] = new double[] {1.8, 2000, 21000, 4.5, 7, 7};
		m_decision[3] = new double[] {2.2, 1800, 20000, 5, 5, 5};
		assertTrue(Arrays.deepEquals(m_decision, topsis.getDecisionMatrix()));
	}
	
	@Test
	protected void testSetDecisionMatrixIrregularColumns() {
		m_decision = new double[4][7];
		weights = new double[7];
		topsis = new TOPSIS(m_decision, weights);
		
		m_decision[1] = new double[8];
		Assertions.assertThrows(IllegalArgumentException.class,
				() -> topsis.setDecisionMatrix(m_decision));
	}
	
	@Test
	protected void testSetDecisionMatrixMismatchWeightsAndMatrix() {
		m_decision = new double[4][7];
		weights = new double[7];
		topsis = new TOPSIS(m_decision, weights);
		m_decision = new double [4][8];
		Assertions.assertThrows(IllegalArgumentException.class,
				() -> topsis.setDecisionMatrix(m_decision));
	}
	
	@Test
	protected void testSetDecisionMatrixNull() {
		m_decision = new double[4][7];
		weights = new double[7];
		Assertions.assertThrows(NullPointerException.class,
				() -> topsis.setDecisionMatrix(null));
	}
	
	
	
	

}
