import java.util.*;

public class IsAnagram {
	
	static boolean IsAnag(String a, String b) {		
		if (a.length() == b.length()) {
			Map<Character, Integer> cmap = CharMap(a);
				
			// For debugging.
			// System.out.println("Before a compared with b");
			// System.out.println(cmap);
			// If a char (key) of the string b exists in the cmap, 
			// it will subtract 1 from the corresponding value.
			for (int i = 0; i < b.length(); i++) {
				if (cmap.containsKey(b.charAt(i))) {
					cmap.put(b.charAt(i), cmap.get(b.charAt(i)) - 1);
				} else { 
					return false;
				}
			}
			
			// For debugging.
			// System.out.println("After a compared with b");
			// System.out.println(cmap);			
			// Checking if all values in cmap.values() are 0.
			// If all values are 0, 
			// it will mean the string b is an anagram of the string a.
			for (Integer i : cmap.values()) {
				if (i != 0) {
					return false;
				}
			}		
			return true;
		}
		return false;
	}
	
	// Making a HashMap to record the number of every character of a string.
	static Map<Character, Integer> CharMap(String str) {
		Map<Character, Integer> hmap = new HashMap<Character, Integer>();
		for (int i = 0; i < str.length(); i++) {
			if (!hmap.containsKey(str.charAt(i))) {
				hmap.put(str.charAt(i), 1);
			} else {
				hmap.put(str.charAt(i), hmap.get(str.charAt(i)) + 1);
			}
		}		
		return hmap;
	} 
	
	public static void main(String[] args) {
		String a = "cinema";
		String b = "iceman";
		if(IsAnag(a, b)) {
			System.out.println(b + " is an anagram of the " + a + ".");
		} else {
			System.out.println(b + " is not an anagram of the " + a + ".");
		}
	}
}