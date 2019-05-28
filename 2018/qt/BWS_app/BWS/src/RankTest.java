package BWS_app

import java.util.LinkedList;
import java.util.List;

import org.junit.jupiter.api.Test;

class RankTest {

	@Test
	void testRun() {
		Rank bws = new Rank();
		bws.setup();
		bws.run();
		bws.run();
		bws.run();
		bws.run();
	}
	
	@Test
	void testRunList() {
		List<String> rank = new LinkedList<String>();
		rank.add("B");
		rank.add("C");
		rank.add("E");
		rank.add("G");
		rank.add("A");
		rank.add("D");
		rank.add("F");
		Rank bws = new Rank();
		bws.setup();
		bws.run(rank);
		bws.run(rank);
		bws.run(rank);
		bws.run(rank);
		bws.run(rank);
	}

}
