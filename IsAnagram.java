import java.util.*;
public class IsAnagram {

	static boolean IsAnag(String a, String b)
	{
		int numberofnull = 0;
		HashMap<Character, Integer> hmap = new HashMap<Character, Integer>();
		if (a.length() == b.length()){
			
			// Insert a to hmap.
			for (int i = 0; i < a.length(); i++){
				if(!hmap.containsKey(a.charAt(i)))
					hmap.put(a.charAt(i), 1);
				else{
					hmap.put(a.charAt(i), hmap.get(a.charAt(i)) + 1);
				}
			}
			
			// Compare a with b.
		//	System.out.println("Before a compared with b");
		//	System.out.println(hmap);
			for (int i = 0; i < a.length(); i++){
				if(hmap.containsKey(b.charAt(i))){
					hmap.put(b.charAt(i), hmap.get(b.charAt(i)) - 1);
				}
				else{
					return false;
				}
			}
		//	System.out.println("After a compared with b");
		//	System.out.println(hmap);
			
			// Check if all values are 0, 
			// and count the number of 0 to match with the length of a.
			for (int i = 0; i < a.length(); i++){
				if(hmap.get(a.charAt(i)) == 0)
					numberofnull++;
				else
					return false;
			}
			if(numberofnull == a.length())
				return true;
			else
				return false;
		}
		return false;
	}
	public static void main(String[] args) {
		
		String a = "apple";
		String b = "lpape";
		if(IsAnag(a, b))
			System.out.println("a and b are the same!");
		else
			System.out.println("a and b are different!");
	}
}