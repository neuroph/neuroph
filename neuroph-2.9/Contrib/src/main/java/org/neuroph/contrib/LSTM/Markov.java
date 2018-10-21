package org.neuroph.contrib.LSTM;

import java.util.Map.Entry;
import java.util.*;

/**
 * Represents a k-character substring. Can give a pseudo-random suffix character based on probability.
 * @author Daniel Friedman (Fall 2011)
 *
 */
public class Markov {
    String substring;
    int count = 0;
    TreeMap<Character, Integer> map;
    /**
     * Constructs a Markov object from a given substring.
     * @param substring the given substring.
     */
    public Markov(String substring) {
	this.substring = substring;
	map = new TreeMap<Character, Integer>();
	add();
    }
    /**
     * Constructs a Markov object from a given substring and suffix character. Suffix characters are stored in a TreeMap.
     * @param substring the specified substring.
     * @param suffix the specified suffix.
     */
    public Markov(String substring, Character suffix) {
	this.substring = substring;
	map = new TreeMap<Character, Integer>();
	add(suffix);
    }
    /**
     * Increments the count of number of times the substring appears in a text.
     */
    public void add() {
	count++;
    }
    /**
     * Adds a suffix character to the TreeMap.
     * @param c the suffix character to be added.
     */
    public void add(char c) {
	add();
	if (map.containsKey(c)) {
	    int frequency = map.get(c);
	    map.put(c, frequency+1);
	}
	else
	    map.put(c, 1);
    }
    /**
     * Gives the frequency count of a suffix character; that is, the number of times the specified suffix follows the substring in a text.
     * @param c the specified suffix.
     * @return the frequency count.
     */
    public int getFrequencyCount(char c) {
	if (!map.containsKey(c)) {
	    return -1;
	}
	else {
	    return map.get(c);
	}
    }
    /**
     * Gives a percentage of frequency count / number of total suffixes.
     * @param c
     * @return
     */
    public double getCharFrequency(char c) {
	if (getFrequencyCount(c) == -1)
	    return -1;
	else
	    return ((double) getFrequencyCount(c))/((double)count);
    }
    /**
     * Finds whether or not the given suffix is in the TreeMap.
     * @param c the given suffix.
     * @return True if the suffix exists in the TreeMap, false otherwise.
     */
    public boolean containsChar(char c) {
	if (!map.containsKey(c)) return false;
	else return true;
    }
    /**
     * Gives the number of times this substring occurs in a text.
     * @return said number of times.
     */
    public int count() {
	return count;
    }
    /**
     * Gives the TreeMap.
     * @return the TreeMap.
     */
    public TreeMap<Character, Integer> getMap() {
	return map;
    }
    /**
     * Using probability, returns a pseudo-random character to follow the substring.
     * Character possibilities are added to an ArrayList (duplicates allowed), and a random number
     * from 0 to the last index in the ArrayList is picked. Since more common suffixes
     * occupy more indices in the ArrayList, the probability of getting a more common suffix
     * is greater than the probability of getting a less common suffix.
     * @return the pseudo-random suffix.
     */
    public char random() {
	Character ret = null;
	Set<Entry<Character, Integer>> s = map.entrySet();
	Iterator<Entry<Character, Integer>> it = s.iterator();
	ArrayList<Character> suffixes = new ArrayList<Character>();

	while (it.hasNext()) {
	    Entry<Character, Integer> tmp = it.next();
	    for (int i=0; i<tmp.getValue(); i++) {
		suffixes.add(tmp.getKey());
	    }
	}

	Random rand = new Random();
	int retIndex = rand.nextInt(suffixes.size());
	ret = suffixes.get(retIndex);
	return ret;
    }
    /**
     * Gives a String representation of the Markov object.
     * @return said String representation.
     */
    public String toString() {
	String ret = "Substring: "+substring+", Count: "+count;
	ret += "\n"+"Suffixes and frequency counts: ";

	for (Entry<Character, Integer> entry : map.entrySet()) {
	    char key = entry.getKey();
	    int value = entry.getValue();
	    ret += "\n"+"Suffix: "+key+", frequency count: "+value;
	}
	return ret;
    }
}
