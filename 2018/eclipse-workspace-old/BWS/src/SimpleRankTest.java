import java.util.LinkedList;


import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.Test;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;


class SimpleRankTest {
	
	

	@Test
	void SimpleRankRunTest() throws IOException, InvalidFormatException {
		SimpleAttribute attrA = new SimpleAttribute("1", "QualityA");
		SimpleAttribute attrB = new SimpleAttribute("2", "QualityB");
		SimpleAttribute attrC = new SimpleAttribute("3", "QualityC");
		SimpleAttribute attrD = new SimpleAttribute("4", "QualityD");
		SimpleAttribute attrE = new SimpleAttribute("5", "QualityE");
		SimpleAttribute attrF = new SimpleAttribute("6", "QualityF");
		SimpleAttribute attrG = new SimpleAttribute("7", "QualityG");
		SimpleAttribute attrH = new SimpleAttribute("8", "QualityG");
		SimpleAttribute attrI = new SimpleAttribute("9", "QualityG");
		SimpleAttribute attrJ = new SimpleAttribute("10", "QualityG");
		SimpleAttribute attrK = new SimpleAttribute("11", "QualityG");
		SimpleAttribute attrL = new SimpleAttribute("12", "QualityG");
		SimpleAttribute attrM = new SimpleAttribute("13", "QualityG");
		SimpleAttribute attrN = new SimpleAttribute("14", "QualityG");
		SimpleAttribute attrO = new SimpleAttribute("15", "QualityG");
		SimpleAttribute attrP = new SimpleAttribute("16", "QualityG");
		List<SimpleAttribute> attrList = new LinkedList<SimpleAttribute>();
		attrList.add(attrA);
		attrList.add(attrB);
		attrList.add(attrC);
		attrList.add(attrD);
		attrList.add(attrE);
		attrList.add(attrF);
		attrList.add(attrG);
		attrList.add(attrH);
		attrList.add(attrI);
		attrList.add(attrJ);
		attrList.add(attrK);
		attrList.add(attrL);
		attrList.add(attrM);
		attrList.add(attrN);
		attrList.add(attrO);
		attrList.add(attrP);
		double[] rankValues = new double[attrList.size()];
		SimpleRank rank = new SimpleRank(attrList, 3, 4);
		if (rank.run() == 0) {
			rankValues = SpectralRank.doSpectralRank(attrList);
			for(int i = 0; i < attrList.size(); i++) {
				System.out.println(attrList.get(i).getName() + ": "  + rankValues[i] * 200.0 + " " + attrList.get(i).getTally() + " " + attrList.get(i).getAppearance());
				System.out.println(attrList.get(i).getList());
			}
		}
		/**
		double[][] m_decision = new double[4][9];
		m_decision[0] = new double[] {2.0, 1500, 20000, 5.5, 5, 9, 0.1};
		m_decision[1] = new double[] {2.5, 2700, 18000, 8.5, 3, 5, 0.2};
		m_decision[2] = new double[] {1.8, 2000, 21000, 4.5, 7, 7, 0.4};
		m_decision[3] = new double[] {2.2, 1800, 20000, 7.5, 5, 5, 0.3};
		TOPSIS topsis = new TOPSIS(m_decision, rankValues);
		for(Map.Entry<Double, Integer> entry : topsis.getRankings().entrySet()) {
			System.out.println("Closeness: " + entry.getKey() + "   Item: " + entry.getValue());
		}
		**/
	}
	
	//@Test
	void SimpleRankDataTest() throws IOException, InvalidFormatException {
		String path = "D:/work 2018/data files for testing";
		String dataFile = "/MaxDiff Data FINAL 9.5.18 - RAW DIRECT EXPORT ALL CASES.xlsx";
		String casesFile = "/PfCiB_final_MXD_Design 9.25.18.xlsx";
		Workbook databook = WorkbookFactory.create(new File(path + dataFile));
		Workbook casebook = WorkbookFactory.create(new File(path + casesFile));
		Sheet datasheet = databook.getSheetAt(0);
		Sheet casesheet = casebook.getSheetAt(0);
		//Data formatter to convert cells to strings
		DataFormatter dataformatter = new DataFormatter();
		//Iterator<Row> row = datasheet.rowIterator();
		datasheet.forEach(row -> {
			if (!(dataformatter.formatCellValue(row.getCell(0)).equals("sys_RespNum"))) {
				//Attributes
				SimpleAttribute attr1 = new SimpleAttribute("1", "Quality");SimpleAttribute attr2 = new SimpleAttribute("2", "Quality");SimpleAttribute attr3 = new SimpleAttribute("3", "Quality");
				SimpleAttribute attr4 = new SimpleAttribute("4", "Quality");SimpleAttribute attr5 = new SimpleAttribute("5", "Quality");SimpleAttribute attr6 = new SimpleAttribute("6", "Quality");
				SimpleAttribute attr7 = new SimpleAttribute("7", "Quality");SimpleAttribute attr8 = new SimpleAttribute("8", "Quality");SimpleAttribute attr9 = new SimpleAttribute("9", "Quality");
				SimpleAttribute attr10 = new SimpleAttribute("10", "Quality");SimpleAttribute attr11 = new SimpleAttribute("11", "Quality");SimpleAttribute attr12 = new SimpleAttribute("12", "Quality");
				SimpleAttribute attr13 = new SimpleAttribute("13", "Quality");SimpleAttribute attr14 = new SimpleAttribute("14", "Quality");SimpleAttribute attr15 = new SimpleAttribute("15", "Quality");SimpleAttribute attr16 = new SimpleAttribute("16", "Quality");
				HashMap<String, SimpleAttribute> attrAll = new HashMap<String, SimpleAttribute>();
				attrAll.put("1", attr1);attrAll.put("2", attr2);attrAll.put("3", attr3);attrAll.put("4", attr4);attrAll.put("5", attr5);attrAll.put("6", attr6);attrAll.put("7", attr7);
				attrAll.put("8", attr8);attrAll.put("9", attr9);attrAll.put("10", attr10);attrAll.put("11", attr11);attrAll.put("12", attr12);attrAll.put("13", attr13);attrAll.put("14", attr14);
				attrAll.put("15", attr15);attrAll.put("16", attr16);
				boolean complete = true;
				int ver = 1;
				while (!(dataformatter.formatCellValue(casesheet.getRow(ver).getCell(0)).equals(dataformatter.formatCellValue(row.getCell(44))))) {
					ver++;
				}
				for (int i = 20; i < 44; i+=2) {
					String bcell = dataformatter.formatCellValue(row.getCell(i));
					String wcell = dataformatter.formatCellValue(row.getCell(i+1));
					if (bcell.isEmpty() || wcell.isEmpty()) {
						complete = false;
						break;
					}
					String middle1 = "";
					String middle2 = "";
					Set<String> options = new HashSet<String>();
					for (int j = 2; j <= 5; j++) options.add(dataformatter.formatCellValue(casesheet.getRow(ver).getCell(j)));
					for (String o : options) {
						if (!o.equals(bcell) && !o.equals(wcell)) {
							if (!middle1.isEmpty() && middle2.isEmpty()) middle2 = o;
							if (middle1.isEmpty()) middle1 = o;
							if (!(middle1.isEmpty() || middle2.isEmpty())) break;
						}
					}
					attrAll.get(wcell).addToList(attrAll.get(bcell));
					attrAll.get(wcell).addToList(attrAll.get(middle2));
					attrAll.get(wcell).addToList(attrAll.get(middle1));
					attrAll.get(middle1).addToList(attrAll.get(bcell));
					attrAll.get(middle2).addToList(attrAll.get(bcell));
					ver++;
				}
				if (complete) {
					ArrayList<SimpleAttribute> attrList = new ArrayList<SimpleAttribute>();
					attrList.add(attr1);attrList.add(attr2);attrList.add(attr3);attrList.add(attr4);attrList.add(attr5);attrList.add(attr6);attrList.add(attr7);
					attrList.add(attr8);attrList.add(attr9);attrList.add(attr10);attrList.add(attr11);attrList.add(attr12);attrList.add(attr13);attrList.add(attr14);
					attrList.add(attr15);attrList.add(attr16);
					double[] rankValues = SpectralRank.doSpectralRank(attrList);
					//Print Spectral Ranking
					double sum = 0;
					for(int i = 0; i < attrList.size(); i++) {
						sum += rankValues[i];
					}
					for(int i = 0; i < attrList.size(); i++) {
						//System.out.println(attrList.get(i).getName() + ": "  + rankValues[i] * 200.0);
						row.createCell(i + 65).setCellValue(rankValues[i] / sum * 100.0);
					}
				}
			}
		});
	    FileOutputStream outFile =new FileOutputStream(new File(path + "/Contraceptive_Data_with_Scores.xlsx"));
	    databook.write(outFile);		
		databook.close();
		casebook.close();
	}

	@Test
	void SimpleRankVersionTest() throws IOException, InvalidFormatException {
		SimpleAttribute attrA = new SimpleAttribute("1", "QualityA");
		SimpleAttribute attrB = new SimpleAttribute("2", "QualityB");
		SimpleAttribute attrC = new SimpleAttribute("3", "QualityC");
		SimpleAttribute attrD = new SimpleAttribute("4", "QualityD");
		SimpleAttribute attrE = new SimpleAttribute("5", "QualityE");
		SimpleAttribute attrF = new SimpleAttribute("6", "QualityF");
		SimpleAttribute attrG = new SimpleAttribute("7", "QualityG");
		SimpleAttribute attrH = new SimpleAttribute("8", "QualityG");
		SimpleAttribute attrI = new SimpleAttribute("9", "QualityG");
		SimpleAttribute attrJ = new SimpleAttribute("10", "QualityG");
		SimpleAttribute attrK = new SimpleAttribute("11", "QualityG");
		SimpleAttribute attrL = new SimpleAttribute("12", "QualityG");
		SimpleAttribute attrM = new SimpleAttribute("13", "QualityG");
		SimpleAttribute attrN = new SimpleAttribute("14", "QualityG");
		SimpleAttribute attrO = new SimpleAttribute("15", "QualityG");
		SimpleAttribute attrP = new SimpleAttribute("16", "QualityG");
		List<SimpleAttribute> attrList = new LinkedList<SimpleAttribute>();
		attrList.add(attrA);
		attrList.add(attrB);
		attrList.add(attrC);
		attrList.add(attrD);
		attrList.add(attrE);
		attrList.add(attrF);
		attrList.add(attrG);
		attrList.add(attrH);
		attrList.add(attrI);
		attrList.add(attrJ);
		attrList.add(attrK);
		attrList.add(attrL);
		attrList.add(attrM);
		attrList.add(attrN);
		attrList.add(attrO);
		attrList.add(attrP);
		
		String path = "D:/work 2018/data files for testing";
		String casesFile = "/PfCiB_final_MXD_Design 9.25.18.xlsx";
		Workbook casebook = WorkbookFactory.create(new File(path + casesFile));
		Sheet casesheet = casebook.getSheetAt(0);
		//Data formatter to convert cells to strings
		DataFormatter dataformatter = new DataFormatter();
		//Iterator<Row> row = datasheet.rowIterator();
		HashSet<HashSet<Set<String>>> versions = new HashSet<HashSet<Set<String>>>();
		for(int i = 1; i <= 300; i++) {
			HashSet<Set<String>> toAsk = new HashSet<Set<String>>();
			for(int j = 0; j < 12;  j++) {
				Row row = casesheet.getRow(i+j);
				if (!dataformatter.formatCellValue(row.getCell(0)).equals(String.valueOf(i))) {
					i++;
				}
				Set<String> question = new HashSet<String>();
				question.add(dataformatter.formatCellValue(row.getCell(2)));
				question.add(dataformatter.formatCellValue(row.getCell(3)));
				question.add(dataformatter.formatCellValue(row.getCell(4)));
				question.add(dataformatter.formatCellValue(row.getCell(5)));
				toAsk.add(question);
			}
			versions.add(toAsk);
		}
		versions.forEach(k->System.out.println(k.toString()));
		System.out.println();
		boolean matched = false;
		for(int i = 0; i < 1; i ++) {
			SimpleRank rank = new SimpleRank(attrList, 3, 4);
			HashSet<Set<String>> toAsk = rank.getToAsk();
			System.out.println(toAsk.toString());
			for (HashSet<Set<String>> a : versions) {
				if (toAsk.equals(a)) {
					matched = true;
					break;
				}
			}
		}
		System.out.println();
		System.out.println(matched);
		casebook.close();
		
	}
	
	/**
	 * SimpleAttribute attrA = new SimpleAttribute("A", "QualityA");
		SimpleAttribute attrB = new SimpleAttribute("B", "QualityB");
		SimpleAttribute attrC = new SimpleAttribute("C", "QualityC");
		SimpleAttribute attrD = new SimpleAttribute("D", "QualityD");
		SimpleAttribute attrE = new SimpleAttribute("E", "QualityE");
		SimpleAttribute attrF = new SimpleAttribute("F", "QualityF");
		SimpleAttribute attrG = new SimpleAttribute("G", "QualityG");
		SimpleAttribute attrH = new SimpleAttribute("H", "QualityG");
		SimpleAttribute attrI = new SimpleAttribute("I", "QualityG");
		SimpleAttribute attrJ = new SimpleAttribute("J", "QualityG");
		SimpleAttribute attrK = new SimpleAttribute("K", "QualityG");
		SimpleAttribute attrL = new SimpleAttribute("L", "QualityG");
		SimpleAttribute attrM = new SimpleAttribute("M", "QualityG");
		SimpleAttribute attrN = new SimpleAttribute("N", "QualityG");
		SimpleAttribute attrO = new SimpleAttribute("O", "QualityG");
		SimpleAttribute attrP = new SimpleAttribute("P", "QualityG");
		List<SimpleAttribute> attrList = new LinkedList<SimpleAttribute>();
		attrList.add(attrA);
		attrList.add(attrB);
		attrList.add(attrC);
		attrList.add(attrD);
		attrList.add(attrE);
		attrList.add(attrF);
		attrList.add(attrG);
		attrList.add(attrH);
		attrList.add(attrI);
		attrList.add(attrJ);
		attrList.add(attrK);
		attrList.add(attrL);
		attrList.add(attrM);
		attrList.add(attrN);
		attrList.add(attrO);
		attrList.add(attrP);
	 */
}
