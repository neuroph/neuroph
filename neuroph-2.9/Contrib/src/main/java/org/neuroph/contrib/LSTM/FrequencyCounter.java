package org.neuroph.contrib.LSTM;

import java.util.*;
/**
 * Counts the frequencies of k-length substrings of input text.
 * @author Daniel Friedman
 *
 */
public class FrequencyCounter {
    public static void main(String[] args) {
	String text;
	int k = 0;
	if (args.length == 1) {
	    k = Integer.parseInt(args[0]);
	}
	System.out.print("Enter string: ");
	Scanner s = new Scanner(System.in);
	text = s.nextLine();
	MyHashMap<String, Integer> hash = new MyHashMap<String, Integer>();
	int distinct = 0;
	
	for (int i=0; i<=text.length()-k; i++) {
	    String sub = text.substring(i,i+k);
	    Markov m = new Markov(sub);
	    m.add();
	    if (hash.containsKey(sub)) {
		Integer count = hash.get(sub);
		hash.put(sub, count+1);
	    }
	    else {
		hash.put(sub, m.count);
		distinct++;
	    }
	}
	
	System.out.println(distinct+" distinct keys");
	Iterator<String> keys = hash.keys();
	while (keys.hasNext()) {
	    String key = keys.next();
	    Integer value = hash.get(key);
	    System.out.println(value+" "+key);
	}
    }
}
