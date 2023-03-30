package de.quizgamemaker.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

import de.quizgamemaker.data.Program;

public class IDCalculator {
	private static ArrayList<Integer> idList = new ArrayList<Integer>();
	/**
	 * Calculate the lowest available id.
	 * @return id number
	 */
	public static int calculateId() {
		Collections.sort(idList, (c1, c2) -> c2 + c1);
		/*if(Program.DEBUG) {
			Iterator<Integer> it2 = idList.iterator();
			System.out.println("--------\nidList content:");
			while (it2.hasNext()) {
				System.out.println(it2.next());
			}
			System.out.println("--------");
		}*/
		int id = -1;
		int id2 = 0;
		Iterator<Integer> it = idList.iterator();
		while (it.hasNext()) {
			id2 = it.next();
			if(id2 != (1+id)) {
				idList.add(1+id);
				Collections.sort(idList);
				if(Program.DEBUG) System.out.println("=> Calculated new ID: " + (1+id) + "\n");
				return 1+id;
			}
			id++;
		}
		idList.add(1+id);
		if(Program.DEBUG) System.out.println("=> Calculated new ID: " + (1+id) + "\n");
		return 1+id;
	}
	
	/**
	 * Free the specified id so that the id can be used again
	 * @param id
	 */
	public static void freeId(int id) {
		Iterator<Integer> it = idList.iterator();
		while (it.hasNext()) {
			if(it.next() == id) {
				it.remove();
			}
		}
	}
	
	/**
	 * Clear the id list. Dangerous! Should only be used for loading a (new) project and testing
	 */
	public static void clearIdList() {
		idList = new ArrayList<Integer>();
		if(Program.DEBUG) System.out.println("ID List cleared...");
	}
	
	/**
	 * registers a ID in the id list.
	 */
	public static void registerID (int id) {
		for(int x : idList) {
			if (x == id) {
				throw new NoSuchFieldError("Error: ID number " + id + " is already registered in idList!");
			}
		}
		idList.add(id);
		if(Program.DEBUG) System.out.println("Registered new ID: " + id);
		Collections.sort(idList);
	}
}