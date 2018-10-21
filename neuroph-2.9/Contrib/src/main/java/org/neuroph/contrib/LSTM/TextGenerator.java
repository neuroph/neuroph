package org.neuroph.contrib.LSTM;

import java.io.*;
import java.util.*;
import java.util.Map.Entry;
public class TextGenerator {

    /**
     * @param args when used with three arguments, the first represents the k-order of the Markov objects.
     * The second represents the number of characters to print out. The third represents the file to be read.
     * 
     * When used with two arguments, the first represents the k-order of the Markov objects, and
     * the second represents the file to be read. The generated text will be the same number of characters
     * as the original file.
     */
    public static void main(String[] args) {
	int k = 0;
	int M = 0;
	String file = "";
	StringBuilder text = new StringBuilder();
//	if (args.length == 3) {
//	    k = Integer.parseInt(args[0]);
//	    M = Integer.parseInt(args[1]);
//	    file = args[2];
//	}
//	else if (args.length == 2) {
//	    k = Integer.parseInt(args[0]);
//	    file = args[1];
//	}
//	else {
//	    System.out.println("\n"+"Usage: java TextGenerator k M file");
//	    System.out.println("where k is the markov order, M is the number");
//	    System.out.println("of characters to be printed, and file is the");
//	    System.out.println("name of the file to print from. M may be left out."+"\n");
//	    System.exit(1);
//	}

        k=7;M=1000;
        file="G:/bkp/$AVG/baseDir/Imports/Sprites/txts/ygirla.txt";
	FileReader reader = null;
	try {
	    reader = new FileReader(file);
	} catch (FileNotFoundException e) {
	    System.out.println("File not found.");
	    e.printStackTrace();
	}

	MyHashMap<String, Markov> hash = new MyHashMap<String, Markov>();

	Character next = null;
	try {
	    next = (char) reader.read();
	} catch (IOException e1) {
	    System.out.println("IOException in stepping through the file");
	    e1.printStackTrace();
	}

	StringBuilder origFileBuffer = new StringBuilder();
	while (Character.isDefined(next)) {
	    Character.toString(next);
	    origFileBuffer.append(next);
	    try {
		next = (char) reader.read();
	    } catch (IOException e) {
		System.out.println("IOException in stepping through the file");
		e.printStackTrace();
	    }

	}
	String origFile = origFileBuffer.toString();
	String firstSub = origFile.substring(0, k);
	for (int i=0; i<origFile.length()-k; i++) {
	    String sub = origFile.substring(i,i+k);
	    Character suffix = origFile.charAt(i+k);

	    if (hash.containsKey(sub)) {
		Markov marvin = hash.get(sub);
		marvin.add(suffix);
		hash.put(sub, marvin);
	    }
	    else {
		Markov marvin = new Markov(sub, suffix);
		hash.put(sub, marvin);
	    }
	}
	if (M == 0)
	    M = origFile.length();
	for (int i=k; i<M; i++) {
	    if (i==k) {
		text.append(firstSub);
		if (text.length() > k)
		    i=text.length();
	    }
	    String sub = text.substring((i-k),(i));
	    Markov tmp = hash.get(sub);
	    if (tmp!=null) {
		Character nextChar = tmp.random();
		text.append(nextChar);
	    }
	    else {
		i = k-1;
	    }
	}
//	if (hash.size() < 100) {
//	    Iterator<String> keys = hash.keys();
//	    while (keys.hasNext()) {
//		String hashKey = keys.next();
//		Markov hashValue = hash.get(hashKey);
//		System.out.print(hashValue.count()+">>>>>>>"+hashKey+":");
//		for (Entry<Character, Integer> entry : hashValue.getMap().entrySet()) {
//		    char suffix = entry.getKey();
//		    int frequencyCount = entry.getValue();
//		    System.out.print(">>>>>"+frequencyCount+" "+suffix);
//		}
//		System.out.println();
//	    }
//	}
	System.out.println(text.toString().substring(0, Math.min(M, text.length())));
    }
}