package org.neuroph.contrib.LSTM;

import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Scanner;
/**
 * Similar to FrequencyCounter, but also counts the suffixes for each k-length substring in the text.
 * @author Daniel Friedman
 *
 */

public class SuffixCounter {
    public static void main(String[] args) {
	String text;
	int k = 0;
	if (args.length == 1) {
	    k = Integer.parseInt(args[0]);
	}
	System.out.print("Enter string: ");
	Scanner s = new Scanner(System.in);
	text = s.nextLine();
	MyHashMap<String, Markov> hash = new MyHashMap<String, Markov>();
	int distinct = 0;

	for (int i=0; i<=text.length()-k; i++) {
	    String sub = text.substring(i,i+k);
	    Character suffix = null;
	    if (((i+k) <= (text.length()-1)))
		suffix = text.charAt(i+k);

	    if (hash.containsKey(sub)) {
		Markov temp = hash.get(sub);

		if (!(suffix == null)) {
		    temp.add(suffix);
		    hash.put(sub, temp);
		}
	    }
	    else {
		Markov m;
		if (!(suffix == null)) {
		    m = new Markov(sub, suffix);
		    hash.put(sub, m);
		    distinct++;
		}
	    }
	}
	System.out.println(distinct+" distinct keys");
	Iterator<String> keys = hash.keys();
	while (keys.hasNext()) {
	    String hashKey = keys.next();
	    Markov hashValue = hash.get(hashKey);
	    System.out.print(hashValue.count()+" "+hashKey+":");
	    for (Entry<Character, Integer> entry : hashValue.getMap().entrySet()) {
		char suffix = entry.getKey();
		int frequencyCount = entry.getValue();
		System.out.print(" "+frequencyCount+" "+suffix);
	    }
	    System.out.println();
	}
    }
}
