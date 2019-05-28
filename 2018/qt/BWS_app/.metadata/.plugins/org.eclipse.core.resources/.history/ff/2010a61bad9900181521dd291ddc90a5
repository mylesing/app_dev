import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

class SimpleRankTest {

	@Test
	void SimpleRankRunTest() {
		double[] rankValues;
		SimpleAttribute attrA = new SimpleAttribute("A", "QualityA");
		SimpleAttribute attrB = new SimpleAttribute("B", "QualityB");
		SimpleAttribute attrC = new SimpleAttribute("C", "QualityC");
		SimpleAttribute attrD = new SimpleAttribute("D", "QualityD");
		SimpleAttribute attrE = new SimpleAttribute("E", "QualityE");
		SimpleAttribute attrF = new SimpleAttribute("F", "QualityF");
		SimpleAttribute attrG = new SimpleAttribute("G", "QualityG");
		List<SimpleAttribute> attrList = new LinkedList<SimpleAttribute>();
		attrList.add(attrA);
		attrList.add(attrB);
		attrList.add(attrC);
		attrList.add(attrD);
		attrList.add(attrE);
		attrList.add(attrF);
		attrList.add(attrG);
		rankValues = new double[attrList.size()];
		SimpleRank rank = new SimpleRank(attrList, 3);
		if (rank.run() == 0) {
			rankValues = SpectralRank.doSpectralRank(attrList);
			for(int i = 0; i < attrList.size(); i++) {
				System.out.println(attrList.get(i).getName() + ": "  + rankValues[i] * 200.0);
			}
		}
		double[][] m_decision = new double[4][7];
		m_decision[0] = new double[] {2.0, 1500, 20000, 5.5, 5, 9, 0.1};
		m_decision[1] = new double[] {2.5, 2700, 18000, 8.5, 3, 5, 0.2};
		m_decision[2] = new double[] {1.8, 2000, 21000, 4.5, 7, 7, 0.4};
		m_decision[3] = new double[] {2.2, 1800, 20000, 7.5, 5, 5, 0.3};
		TOPSIS topsis = new TOPSIS(m_decision, rankValues);
		for(Map.Entry<Double, Integer> entry : topsis.getRankings().entrySet()) {
			System.out.println("Closeness: " + entry.getKey() + "   Item: " + entry.getValue());
		}
	}

}
