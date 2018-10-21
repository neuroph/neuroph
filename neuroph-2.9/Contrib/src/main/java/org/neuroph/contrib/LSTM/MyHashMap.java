package org.neuroph.contrib.LSTM;

import java.util.*;
/**
 * A Hash table implementation. Uses an array of LinkedList<MyEntry> as backing storage.
 * @author Daniel Friedman
 *
 * @param <K> generic type for Keys.
 * @param <V> generic type for Values.
 */

public class MyHashMap<K, V> {
    /**
     * Represents a key, value pairing.
     * @author Daniel Friedman
     *
     */
    public class MyEntry {
	protected K key;
	protected V value;
	@Override
	/**
	 * Gives the hashcode of a MyEntry object.
	 * @return the hashcode of the MyEntry's key.
	 */
	public int hashCode() {
	    return key.hashCode();
	}
	/**
	 * Tests whether this object is equivalent to a specified other.
	 * @param obj the other object.
	 * @return true if the keys are equivalent.
	 */
	@SuppressWarnings("unchecked")
	@Override
	public boolean equals(Object obj) {
	    boolean ret = false;
	    if (obj == null) ret = false;
	    else if (this == null) ret = false;
	    MyEntry other = (MyEntry) obj;
	    if (key.equals(other.key))
		ret = true;
	    return ret;
	}
	/**
	 * Constructs a MyEntry object with given key and value.
	 * @param key the key to be stored.
	 * @param value the value to be stored.
	 */
	public MyEntry(K key, V value) {
	    this.key = key;
	    this.value = value;
	}
	/**
	 * Gives a String representation of this object.
	 * @return the String representation.
	 */
	public String toString() {
	    return "("+key+", "+value+")";
	}
    }
    
    private LinkedList<MyEntry> table[];
    private int size;
    private float loadFactor;
    private ArrayList<Integer> primes = new ArrayList<Integer>();
    /**
     * Constructs a new MyHashMap object.
     * @param capacity the size of the array.
     * @param loadFactor the specified load factor. The array will resize when the load factor is reached.
     */
    @SuppressWarnings("unchecked")
    public MyHashMap(int capacity, float loadFactor) {
	table = (LinkedList<MyEntry> []) new LinkedList[capacity];
	this.loadFactor = loadFactor;

	for (int i=0; i<table.length; i++) {
	    table[i] = new LinkedList<MyEntry>();
	}
    }
    /**
     * Constructs a MyHashMap with capacity 11 and load factor 0.75.
     */
    public MyHashMap() {
	this(11, (float) 0.75);
	primes.add(11);
	primes.add(23);
	primes.add(47);
	primes.add(97);
	primes.add(197);
	primes.add(397);
	primes.add(797);
	primes.add(1597);
	primes.add(3203);
	primes.add(6421);
	primes.add(12853);
	primes.add(25717);
	primes.add(51437);
	primes.add(102877);
	primes.add(205759);
	primes.add(411527);
	primes.add(823117);
	primes.add(1646237);
	primes.add(3292489);
	primes.add(6584983);
	primes.add(13169977);
	primes.add(26339969);
	primes.add(52679969);
	primes.add(105359939);
	primes.add(210719881);
	primes.add(421439783);
	primes.add(842879579);
	primes.add(1685759167);
    }
    /**
     * Gives the number of MyEntries in the array.
     * @return number of MyEntry objects in the array.
     */
    public int size() {
	return size;
    }
    /**
     * Tests whether or not the array is empty.
     * @return true if empty, false otherwise.
     */
    public boolean isEmpty() {
	if (size == 0) return true;
	else return false;
    }
    /**
     * Removes all objects from the array.
     */
    public void clear() {
	for (int i=0; i<table.length; i++) {
	    table[i].clear();
	}
    }
    /**
     * Gives a String representation of the array.
     * @return said String representation.
     */
    public String toString() {
	String ret = "";
	for (int i=0; i<table.length; i++) {
	    ret+=("Bucket "+i+": "+table[i]+"\n");
	}
	return ret;
    }
    /**
     * Adds a key, value mapping to the array. If the key already is contained, the value will be updated.
     * @param key the key to be added.
     * @param value the value to be added.
     * @return the previous value if the key already was contained, null otherwise.
     */
    public V put(K key, V value) {
	MyEntry entry = new MyEntry(key, value);
	int hashCode = Math.abs(key.hashCode());
	int mapping = hashCode%(table.length);
	LinkedList<MyEntry> bucket = table[mapping];
	V ret = null;

	if (bucket.contains(entry)) {
	    int index = bucket.indexOf(entry);
	    ret = bucket.get(index).value;
	    bucket.set(index, entry);
	}
	else
	    bucket.add(entry);
	size++;
	if (((double)size)/((double)table.length) >= loadFactor)
	    resize();
	return ret;
    }
    /**
     * Gives the value associated with a specified key.
     * @param key the specified key.
     * @return the value associated. Null if the key is not found.
     */
    public V get(K key) {
	V ret = null;
	int hashCode = Math.abs(key.hashCode());
	int mapping = hashCode%(table.length);
	LinkedList<MyEntry> bucket = table[mapping];

	for (int i=0; i<bucket.size(); i++) {
	    MyEntry cur = bucket.get(i);
	    if (cur.key.equals(key)) {
		ret = cur.value;
	    }
	}
	return ret;
    }
    /**
     * Removes a specified object from the array.
     * @param key the key corresponding to the MyEntry to be removed.
     * @return the value associated with the removed key.
     */
    public V remove(K key) {
	V ret = null;
	int hashCode = Math.abs(key.hashCode());
	int mapping = hashCode%(table.length);
	LinkedList<MyEntry> bucket = table[mapping];

	for (int i=0; i<bucket.size(); i++) {
	    MyEntry cur = bucket.get(i);
	    if (cur.key.equals(key)) {
		ret = cur.value;
		bucket.remove(cur);
	    }
	}
	size--;
	return ret;
    }
    /**
     * Finds whether or not the array contains a given key.
     * @param key the key to be tested.
     * @return true if found, false if not.
     */
    public boolean containsKey(K key) {
	boolean ret = false;
	int hashCode = Math.abs(key.hashCode());
	int mapping = hashCode%(table.length);
	LinkedList<MyEntry> bucket = table[mapping];
	MyEntry test = new MyEntry(key, null);

	if (bucket.contains(test)) {
	    ret = true;
	}
	return ret;
    }
    /**
     * Finds whether or not the array contains a given value.
     * @param value the value to be tested.
     * @return true if found, false if not.
     */
    public boolean containsValue(V value) {
	boolean ret = false;
	for (int i=0; i<table.length; i++) {
	    LinkedList<MyEntry> bucket = table[i];
	    for (int j=0; j<bucket.size(); j++) {
		MyEntry entry = bucket.get(j);
		if (entry.value.equals(value))
		    ret = true;
	    }
	}
	return ret;
    }
    /**
     * Iterates over the MyEntry objects in the array, looking at their keys.
     * @return a keys iterator.
     */
    public Iterator<K> keys() {
	return new Iterator<K>() {
	    int bucketIndex = 0;
	    LinkedList<MyEntry> bucket = table[bucketIndex];
	    Iterator<MyEntry> listIt = bucket.iterator();
	    MyEntry cur;
	    /**
	     * Finds whether or not there is a next MyEntry object in the array.
	     * @return
	     */
	    @Override
	    public boolean hasNext() {
		if (listIt.hasNext())
		    return true;
		else if (bucketIndex == (table.length-1)) {
		    return false;
		}
		else {
		    for (int i=bucketIndex+1; i<table.length; i++) {
			if (!table[i].isEmpty())
			    return true;
		    }
		}
		return false;
	    }
	    /**
	     * Gives the key associated with the next MyEntry object in the array. Throws a NoSuchElementException if the array has been completely iterated.
	     * @return the key associated with the next MyEntry object.
	     */
	    @Override
	    public K next() {
		K ret = null;
		if (listIt.hasNext()) {
		    cur = listIt.next();
		    ret = cur.key;
		}
		else if (hasNext()) {
		    for (int i=bucketIndex+1; i<table.length; i++) {
			if (!table[i].isEmpty()) {
			    bucketIndex = i;
			    bucket = table[bucketIndex];
			    listIt = bucket.iterator();
			    cur = listIt.next();
			    ret = cur.key;
			    break;
			}
		    }
		}
		else throw new NoSuchElementException("All buckets iterated.");
		return ret;
	    }
	    /**
	     * Removes the current MyEntry.
	     */
	    @Override
	    public void remove() {
		bucket.remove(cur);
	    }

	};
    }
    /**
     * Resizes the array to the next largest prime number that is at least double the current array size.
     */
    public void resize() {
	MyHashMap<K,V> tmp = new MyHashMap<K,V>(helper(table.length), (float) 0.75);
	Iterator<K> it = keys();
	
	while (it.hasNext()) {
	    K key = it.next();
	    V value = get(key);
	    tmp.put(key, value);
	}
	this.table = tmp.table;
	this.size = tmp.size;
	this.loadFactor = tmp.loadFactor;
    }
    /**
     * Gives the next largest prime number that is at least double the current array size.
     * @param i
     * @return
     */
    public int helper(int i) {
	int ret = -1;
	for (int j=0; j<primes.size(); j++) {
	    if (primes.get(j) > i) {
		ret = primes.get(j);
		break;
	    }
	}
	return ret;
    }
    /**
     * Tests the MyHashMap class and its methods.
     * 
     */
    public static void main(String[] args) {
	// test1:
	/* MyHashMap<String, Integer> testMap = new MyHashMap<String, Integer>();

	System.out.println("HashMap size: "+testMap.size());
	System.out.println("HashMap capacity: "+testMap.table.length);
	System.out.println("HashMap Load Factor: "+testMap.loadFactor);
	System.out.println("HashMap isEmpty? "+testMap.isEmpty());
	System.out.println("HashMap toString: "+testMap); */
	
	// test2:
	MyHashMap<String, Integer> testMap = new MyHashMap<String, Integer>();
	
	for (int i=0; i<100; i++) {
	    testMap.put(""+i, i);
	    System.out.println("Hashtable:");
	    System.out.println(testMap);
	}
	
	for (int i=0; i<100; i++) {
	    String key = ""+i;
	    System.out.println("Removing key "+key+":");
	    testMap.remove(key);
	    System.out.println(testMap);
	}
    }
}
