
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

class SpectralRankTest {
	
	@Test
	void SpectralRankDoTest() {
		List<SimpleAttribute> attrList;
		
		SimpleAttribute attrA = new SimpleAttribute("A", "QualityA");
		SimpleAttribute attrB = new SimpleAttribute("B", "QualityB");
		SimpleAttribute attrC = new SimpleAttribute("C", "QualityC");
		SimpleAttribute attrD = new SimpleAttribute("D", "QualityD");
		SimpleAttribute attrE = new SimpleAttribute("E", "QualityE");
		SimpleAttribute attrF = new SimpleAttribute("F", "QualityF");
		SimpleAttribute attrG = new SimpleAttribute("G", "QualityG");
		
		//Comparisons: A>B, A>C, A>D
		//B>D, C>D, D>E, D>F, D>G, E>G, F>G,
		//A>D, A>E, A>F, D>F, E>F,
		//B>C, B>D, B>G, C>D, G>D
		//In this example, the person chose D greater than G and 
		//G>D in another question, which is completely possible for the
		//person to do.
		
		attrB.addToList(attrA); attrC.addToList(attrA); attrD.addToList(attrA);
		attrD.addToList(attrB); attrD.addToList(attrC); attrE.addToList(attrD);
		attrF.addToList(attrD); attrG.addToList(attrD); attrG.addToList(attrE);
		attrG.addToList(attrF); attrD.addToList(attrA); attrE.addToList(attrA);
		attrF.addToList(attrA); attrF.addToList(attrD); attrF.addToList(attrE); 
		attrC.addToList(attrB); attrD.addToList(attrB); attrG.addToList(attrB); 
		attrD.addToList(attrC); attrD.addToList(attrG);
		
		attrList = new ArrayList<SimpleAttribute>();
		attrList.add(attrA);
		attrList.add(attrB);
		attrList.add(attrC);
		attrList.add(attrD);
		attrList.add(attrE);
		attrList.add(attrF);
		attrList.add(attrG);
		
		//SpectralRank spectral = new SpectralRank(attrList);
		double[] rankValues = SpectralRank.doSpectralRank(attrList);
		//Print the rankings
		for(int i = 0; i < attrList.size(); i++) {
			System.out.println(attrList.get(i).getName() + ": "  + rankValues[i] * 200.0);
		}
		System.out.println();
	}

	@Test
	void SpectralRankDoTest2() {
		System.out.println("ASR Test 2:");
		List<SimpleAttribute> attrList;
		
		//items: A, B, C, D, E, F, G
		SimpleAttribute attrA = new SimpleAttribute("A", "QualityA");
		SimpleAttribute attrB = new SimpleAttribute("B", "QualityB");
		SimpleAttribute attrC = new SimpleAttribute("C", "QualityC");
		SimpleAttribute attrD = new SimpleAttribute("D", "QualityD");
		SimpleAttribute attrE = new SimpleAttribute("E", "QualityE");
		SimpleAttribute attrF = new SimpleAttribute("F", "QualityF");
		SimpleAttribute attrG = new SimpleAttribute("G", "QualityG");
		
		//A>G, A>E, G>E
		//E>C, E>F, C>F
		//A>F, A>D, F>D
		//B>E, B>D, E>D
		//C>D, C>G, D>G
		//B>F, B>G, F>G
		//A>B, A>C, B>C
		attrG.addToList(attrA); attrE.addToList(attrA); attrE.addToList(attrG);
		attrC.addToList(attrE); attrF.addToList(attrE); attrF.addToList(attrC);
		attrF.addToList(attrA); attrD.addToList(attrA); attrD.addToList(attrF);
		attrE.addToList(attrB); attrD.addToList(attrB); attrD.addToList(attrE);
		attrD.addToList(attrC); attrG.addToList(attrC); attrG.addToList(attrD);
		attrF.addToList(attrB); attrG.addToList(attrB); attrG.addToList(attrF);
		attrB.addToList(attrA); attrC.addToList(attrA); attrC.addToList(attrB);
		
		attrList = new ArrayList<SimpleAttribute>();
		attrList.add(attrA);
		attrList.add(attrB);
		attrList.add(attrC);
		attrList.add(attrD);
		attrList.add(attrE);
		attrList.add(attrF);
		attrList.add(attrG);
		//SpectralRank spectral = new SpectralRank(attrList);
		double[] rankValues = SpectralRank.doSpectralRank(attrList);
		//Print the rankings
		for(int i = 0; i < attrList.size(); i++) {
			System.out.println(attrList.get(i).getName() + ": "  + rankValues[i] * 200.0);
		}
	}
}
