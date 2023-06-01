import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class MyHashMap<K, V> implements DefaultMap<K, V> {
	public static final double DEFAULT_LOAD_FACTOR = 0.75;
	public static final int DEFAULT_INITIAL_CAPACITY = 16;
	public static final String ILLEGAL_ARG_CAPACITY = "Initial Capacity must be non-negative";
	public static final String ILLEGAL_ARG_LOAD_FACTOR = "Load Factor must be positive";
	public static final String ILLEGAL_ARG_NULL_KEY = "Keys must be non-null";
	
	private double loadFactor;
	private int capacity;
	private int size;

	// Use this instance variable for Separate Chaining conflict resolution
	private List<HashMapEntry<K, V>>[] buckets;  
	
	// Use this instance variable for Linear Probing
	//private HashMapEntry<K, V>[] entries; 	

	public MyHashMap() {
		this(DEFAULT_INITIAL_CAPACITY, DEFAULT_LOAD_FACTOR);
	}
	
	/**
	 * 
	 * @param initialCapacity the initial capacity of this MyHashMap
	 * @param loadFactor the load factor for rehashing this MyHashMap
	 * @throws IllegalArgumentException if initialCapacity is negative or loadFactor not
	 * positive
	 */
	@SuppressWarnings("unchecked")
	public MyHashMap(int initialCapacity, double loadFactor) throws IllegalArgumentException {
		this.loadFactor = loadFactor;
		this.capacity = initialCapacity;
		this.size = 0;
		
		if (initialCapacity < 0) {
			throw new IllegalArgumentException ("Initial capacity is a negative value");
		} else if (loadFactor <= 0) {
			throw new IllegalArgumentException("Load Factor is not a positive value");
		}
		// if you use Separate Chaining
		buckets = (List<HashMapEntry<K, V>>[]) new List<?>[capacity];
		for (int i = 0; i < capacity; i ++) {
			List<HashMapEntry<K,V>> bucket = new LinkedList<HashMapEntry<K,V>>();
			buckets[i] = bucket;
		}
		// if you use Linear Probing
		// entries = (HashMapEntry<K, V>[]) new HashMapEntry<?, ?>[initialCapacity];
	}

	@Override
	public boolean put(K key, V value) throws IllegalArgumentException {
		if (key == null) {
			throw new IllegalArgumentException("Key value was null");
		}
		if (containsKey(key)) {
			return false;
		}
		List<HashMapEntry<K,V>> bucket;
		int index = getIndex(key);
		if (buckets[index] != null){
			bucket = buckets[index];
		} else {
			bucket  = new LinkedList<HashMapEntry<K,V>>();
		}
		HashMapEntry<K, V> entry = new HashMapEntry<K, V>(key, value);
		bucket.add(entry);
		buckets[index] = bucket;
		this.size++;
		if (size > loadFactor * capacity) {
			doubleCapacity();
		}
		return true;
	}

	@Override
	public boolean replace(K key, V newValue) throws IllegalArgumentException {
		if (key == null) {
			throw new IllegalArgumentException("Key value was null");
		}
		boolean exists = containsKey(key);
		int index = getIndex(key);
		List<HashMapEntry<K,V>> bucket = buckets[index];
		HashMapEntry<K, V> entry = new HashMapEntry<K, V>(key, newValue);
		if (!exists) {
			return false;
		}
		bucket.set(0, entry);
		return true;
	}

	@Override
	public boolean remove(K key) throws IllegalArgumentException {
		if (key == null) {
			throw new IllegalArgumentException("Key value was null");
		}
		int index = getIndex(key);
		try {
			List<HashMapEntry<K,V>> bucket = buckets[index];
			for (HashMapEntry<K,V> entry: bucket) {
				if (entry.getKey().equals(key)) {
					bucket.remove(entry);
					this.size--;
					return true;
				}
			}
		} catch (Exception e) {
			return false;
		}
		return false;
	}

	@Override
	public void set(K key, V value) throws IllegalArgumentException {
		if (key == null) {
			throw new IllegalArgumentException("Key value was null");
		}
		boolean exists = containsKey(key);
		/**int index = getIndex(key);
		List<HashMapEntry<K,V>> bucket = buckets[index];
		if (bucket.isEmpty() || bucket == null) { **/
		if (exists) {
			replace(key, value);
		}
		put(key, value);
		
	}

	@Override
	public V get(K key) throws IllegalArgumentException {
		if (key == null) {
			throw new IllegalArgumentException("Key value was null");
		}
		int index = getIndex(key);
		List<HashMapEntry<K,V>> bucket = buckets[index];
		if (bucket != null) {
			for (HashMapEntry<K,V> entry: bucket) {
				if (entry.getKey().equals(key)) {
					return entry.value;
				}
			}
		}
		return null;
	}

	public List<HashMapEntry<K,V>> getAll(K key) throws IllegalArgumentException {
		if (key == null) {
			throw new IllegalArgumentException("Key value was null");
		}
		int index = getIndex(key);
		List<HashMapEntry<K,V>> bucket = buckets[index];
		if (bucket != null) {
			return bucket;
		}
		return null;
	}

	@Override
	public int size() {
		return size;
	}

	@Override
	public boolean isEmpty() {
		for (List<HashMapEntry<K,V>> bucket: buckets) {
			if (bucket != null && bucket.isEmpty() == false) {
				return false;
			}
		}
		return true;
	}

	@Override
	public boolean containsKey(K key) throws IllegalArgumentException {
		if (key == null) {
			throw new IllegalArgumentException("Key value was null");
		}
		int index = getIndex(key);
		List<HashMapEntry<K,V>> bucket = buckets[index];
		try {
			for (HashMapEntry<K,V> entry: bucket) {
				if (entry.getKey().equals(key)) {
					return true;
				}
			}
		} catch (Exception e) {
			return false;
		}	
		return false;
	}

	@Override
	public List<K> keys() {
		List<K> keyList = new LinkedList<K>();
		try{
			for (List<HashMapEntry<K,V>> bucket: buckets) {
				if (bucket != null && !bucket.isEmpty()) {
					//for (int i = 0; i < bucket.size(); i++) {
						keyList.add(bucket.get(0).getKey());
					//}
				}
			}
		} catch(Exception e) {
			return keyList;
		}
		keyList.removeAll(Collections.singleton(null));
		return keyList;
	}

	private int getIndex(K key) {
		int index = (Objects.hashCode(key)) % capacity;
		System.out.println(index);
		if (index < 0) {
			return index * -1;
		}
		return index;
	}

	private void doubleCapacity() {
		capacity *= 2;
		List<HashMapEntry<K, V>>[] hashData = buckets.clone();
		buckets = (List<HashMapEntry<K, V>>[]) new List<?>[capacity];
		for (int i = 0; i < capacity; i ++) {
			List<HashMapEntry<K,V>> bucket = new LinkedList<HashMapEntry<K,V>>();
			buckets[i] = bucket;
		}
		size = 0;
		for (int i = 0; i < hashData.length; i++) {
			if (hashData[i] != null) {
				for (HashMapEntry<K,V> entry: hashData[i]) {
					if (entry != null) {
						put(entry.getKey(), entry.getValue());
					}
				}
			}
		}
	}

	public void printMap() {
		for (int i = 0; i < buckets.length; i++) {
			System.out.println("Bucket: " + i);
			for (int j = 0; j < buckets[i].size(); j++) {
				System.out.println(buckets[i].get(j));
			}
		}
	}
	
	private static class HashMapEntry<K, V> implements DefaultMap.Entry<K, V> {
		
		K key;
		V value;
		
		private HashMapEntry(K key, V value) {
			this.key = key;
			this.value = value;
		}

		@Override
		public K getKey() {
			return key;
		}

		@Override
		public V getValue() {
			return value;
		}
		
		@Override
		public void setValue(V value) {
			this.value = value;
		}
	}
}
