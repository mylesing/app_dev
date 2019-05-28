package com.BWS.BWSweb;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class BWSweb
 */
@WebServlet("/BWSweb")
public class BWSweb extends HttpServlet {
	private static final long serialVersionUID = 1L;
	public static final String HTML_START="<html><body>";
	public static final String HTML_END="</body></html>";
	List<SimpleAttribute> attrList = new LinkedList<SimpleAttribute>();
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public BWSweb() {
        super();
        // TODO Auto-generated constructor stub
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
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		PrintWriter out = response.getWriter();
		Date date = new Date();
		SimpleRank rank = new SimpleRank(attrList, 3, 4);
		HashSet<Set<String>> toAsk = rank.getToAsk();
		
		for (Set<String> question : toAsk) {
			Iterator<String> itr = question.iterator();
			out.println(HTML_START + "<h2>" + itr.next() + "</h2><br/><h3>Date="+date +"</h3>"+HTML_END);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
