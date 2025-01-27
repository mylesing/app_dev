package BWS_app

import static org.junit.jupiter.api.Assertions.*;
import java.util.LinkedList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * This is the test class for the Attribute class.
 * Side note:
 * In this test class, the lists and tallies do not make sense in 
 * terms of determining a ranked list of the four Attributes 
 * created here. Random lists and tallies are made in the testing.
 */
class AttributeTest {
	private Attribute attrA;
	private Attribute attrB;
	private Attribute attrC;
	private Attribute attrD;
	
	
	@Test
	protected void testAttributeStringStringEmptyNameException() {
		//empty name
		Assertions.assertThrows(RuntimeException.class, () -> new 
				Attribute("", "QualityA"), "Attribute name is empty.");
	}
	
	@Test
	protected void testAttributeStringStringNullNameException() {
		//null name
		Assertions.assertThrows(NullPointerException.class, () -> new 
				Attribute(null, "QualityB"), "Attribute name is null.");
	}
	
	@Test
	protected void testAttributeStringStringEmptyDescriptionException() {
		//empty description
		Assertions.assertThrows(RuntimeException.class, () -> new 
				Attribute("", "QualityA"), "Attribute description is empty.");
	}
	
	@Test
	protected void testAttributeStringStringNullDescriptionException() {
		//null description
		Assertions.assertThrows(NullPointerException.class, () -> new 
				Attribute("B", null), "Attribute description is null.");
	}

	@Test
	protected void testAttributeStringStringListOfStringIntEmptyNameException() {
		List<String> list = new LinkedList<String>();
		list.add("A");
		int tally = 3;
		
		//empty name
		Assertions.assertThrows(RuntimeException.class, () -> new 
				Attribute("", "QualityA", list, tally), "Attribute name is empty.");
	}
	
	@Test
	protected void testAttributeStringStringListOfStringIntNullNameException() {
		List<String> list = new LinkedList<String>();
		list.add("A");
		int tally = 3;
		
		//null name
		Assertions.assertThrows(NullPointerException.class, () -> new 
				Attribute(null, "QualityB", list, tally), "Attribute name is null.");
	}
	
	@Test
	protected void testAttributeStringStringListOfStringIntEmptyDescriptionException() {
		List<String> list = new LinkedList<String>();
		list.add("A");
		int tally = 3;

		//empty description
		Assertions.assertThrows(RuntimeException.class, () -> new 
				Attribute("", "QualityA", list, tally), "Attribute description "
						+ "is empty.");
	}
	
	@Test
	protected void testAttributeStringStringListOfStringIntNullDescriptionException() {
		List<String> list = new LinkedList<String>();
		list.add("A");
		int tally = 3;
		
		//null description
		Assertions.assertThrows(NullPointerException.class, () -> new 
				Attribute("B", null, list, tally), "Attribute description "
						+ "is null.");
	}
	
	@Test
	protected void testAttributeStringStringListOfStringIntNullListException() {
		int tally = 3;
		
		//null list
		Assertions.assertThrows(NullPointerException.class, () -> new 
				Attribute("C", "QualityC", null, tally), "List input is null.");
	}
	
	@Test
	public void testAttributeStringString() {
		attrA = new Attribute("A", "QualityA");
		attrB = new Attribute("B", "QualityB");
		attrC = new Attribute("C", "QualityC");
		//other constructor
		List<String> list = new LinkedList<String>();
		list.add("A");
		int tally = 3;
		attrD = new Attribute("D", "QualityD", list, tally);
	}

	@Test
	public void testGetName() {
		attrA = new Attribute("A", "QualityA");
		attrB = new Attribute("B", "QualityB");
		attrC = new Attribute("C", "QualityC");
		//other constructor
		List<String> list = new LinkedList<String>();
		list.add("A");
		int tally = 3;
		attrD = new Attribute("D", "QualityD", list, tally);
		
		assertEquals("A", attrA.getName());
		assertEquals("B", attrB.getName());
		assertEquals("C", attrC.getName());
		assertEquals("D", attrD.getName());
	}

	@Test
	public void testGetDescription() {
		attrA = new Attribute("A", "QualityA");
		attrB = new Attribute("B", "QualityB");
		attrC = new Attribute("C", "QualityC");
		//other constructor
		List<String> list = new LinkedList<String>();
		list.add("A");
		int tally = 3;
		attrD = new Attribute("D", "QualityD", list, tally);
		
		assertEquals("QualityA", attrA.getDescription());
		assertEquals("QualityB", attrB.getDescription());
		assertEquals("QualityC", attrC.getDescription());
		assertEquals("QualityD", attrD.getDescription());
	}

	@Test
	public void testSetDescription() {
		attrA = new Attribute("A", "QualityA");
		attrB = new Attribute("B", "QualityB");
		attrC = new Attribute("C", "QualityC");
		//other constructor
		List<String> list = new LinkedList<String>();
		list.add("A");
		int tally = 3;
		attrD = new Attribute("D", "QualityD", list, tally);
		
		attrA.setDescription("Description A");
		attrB.setDescription("Description B");
		attrC.setDescription("Description C");
		attrD.setDescription("Description D");
		
		assertEquals("Description A", attrA.getDescription());
		assertEquals("Description B", attrB.getDescription());
		assertEquals("Description C", attrC.getDescription());
		assertEquals("Description D", attrD.getDescription());
	}
	
	@Test
	public void testSetDescriptionEmptyDescriptionException() {
		Assertions.assertThrows(RuntimeException.class, ()-> attrD.setDescription(""),
				"Attribute description is empty.");
	}
	
	@Test
	public void testSetDescriptionNullDescriptionException() {
		Assertions.assertThrows(NullPointerException.class, ()-> attrD.setDescription(null),
				"Attribute description is null.");
	}

	@Test
	public void testGetList() {
		List<String> testList = new LinkedList<String>();
		testList.add("B");
		attrA = new Attribute("A", "Description A", testList, 0);
		assertEquals(testList, attrA.getList());
	}
	
	@Test
	public void testAddToList() {
		attrB = new Attribute("B", "QualityB");
		attrC = new Attribute("C", "QualityC");

		List<String> testList = new LinkedList<String>();
		testList.add("C");
		attrB.addToList(attrC);
		assertEquals(testList, attrB.getList());
	}
	
	@Test
	public void testAddToListNullException() {
		Assertions.assertThrows(NullPointerException.class, ()-> attrA.addToList(null),
				"Attribute to add to list is null.");
	}

	@Test
	public void testGetTotal() {
		attrA = new Attribute("A", "QualityA");
		attrB = new Attribute("B", "QualityB");
		attrC = new Attribute("C", "QualityC");
		//other constructor
		List<String> list = new LinkedList<String>();
		list.add("A");
		int tally = 3;
		attrD = new Attribute("D", "QualityD", list, tally);
		
		attrA.addToList(attrB);
		attrB.addToList(attrC);
		attrD.addToList(attrB);
		
		assertEquals(1, attrA.getTotal());
		assertEquals(3, attrB.getTotal());
		assertEquals(1, attrC.getTotal());
		assertEquals(5, attrD.getTotal());
	}

	@Test
	public void testContainsAtLeastOne() {
		attrA = new Attribute("A", "QualityA");
		attrB = new Attribute("B", "QualityB");
		attrC = new Attribute("C", "QualityC");
		//other constructor
		List<String> list = new LinkedList<String>();
		list.add("A");
		int tally = 3;
		attrD = new Attribute("D", "QualityD", list, tally);
		
		attrA.addToList(attrB);
		attrB.addToList(attrC);
		
		assertTrue(attrA.containsAtLeastOne(new String[] {"D", "B"}));
		assertFalse(attrB.containsAtLeastOne(new String[] {"D", "A"}));
	}
	
	@Test
	public void testContainsAtLeastOneEmptyArrayException() {
		Assertions.assertThrows(RuntimeException.class, ()-> attrA.containsAtLeastOne(new String[] {}),
				"String array is empty");
	}
	
	@Test
	public void testContainsAtLeastOneNullArrayException() {
		Assertions.assertThrows(NullPointerException.class, ()-> attrA.containsAtLeastOne(null),
				"String array is null");
	}

}
