
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

import org.junit.jupiter.api.Test;

class treeTest {

	@Test
	void test() {
		Map<Integer, String> map = new TreeMap<Integer, String>();
		Map<Integer, String> map2 = new TreeMap<Integer, String>();
		Scanner user = new Scanner(System.in);
		int entries = user.nextInt();
		user.close();
		for(int i = 0; i < entries; i++) {
			map.put(i, Integer.toString(i));
			map2.put(i, Integer.toString(i));
		}
		map.clear();
		for(int i = 0; i < entries; i++) {
			map.put(i, Integer.toString(i));
		}
		map.clear();
		for(int i = 0; i < entries; i++) {
			map.put(i, Integer.toString(i));
		}
		map.clear();
		for(int i = 0; i < entries; i++) {
			map.put(i, Integer.toString(i));
		}
		map.clear();
		for(int i = 0; i < entries; i++) {
			map.put(i, Integer.toString(i));
		}
		map.clear();
		for(int i = 0; i < entries; i++) {
			map.put(i, Integer.toString(i));
		}
		map.clear();
		for(int i = 0; i < entries; i++) {
			map.put(i, Integer.toString(i));
		}
	}

}
