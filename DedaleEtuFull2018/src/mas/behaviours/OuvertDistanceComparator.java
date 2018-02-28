package mas.behaviours;

import java.util.Comparator;

import env.Couple;

public class OuvertDistanceComparator implements Comparator<Couple<String, Integer>> {

	public int compare(Couple<String, Integer> c1, Couple<String, Integer> c2) {

		Integer i1 = (Integer)(c1.getRight());
		Integer i2 = (Integer)(c2.getRight());
		return i1.compareTo(i2);
	}	
}
