import java.util.*;

public class AnagramFinder {
	public static List<Integer> findAnagrams(String s, String p) {
		List<Integer> result = new ArrayList<>();
		if (s == null || p == null || s.length() < p.length()) {
			return result;
		}

		// Frequency map for characters in p
		int[] pCount = new int[26];
		int[] sCount = new int[26];

		for (char ch : p.toCharArray()) {
			pCount[ch - 'a']++;
		}

		int pLength = p.length();

		for (int i = 0; i < s.length(); i++) {
			sCount[s.charAt(i) - 'a']++;

			// Maintain the window size equal to p's length
			if (i >= pLength) {
				sCount[s.charAt(i - pLength) - 'a']--;
			}

			// Compare window with p's frequency map
			if (Arrays.equals(pCount, sCount)) {
				result.add(i - pLength + 1);
			}
		}

		return result;
	}

	public static void main(String[] args) {
		String s = "cbaebabacd";
		String p = "abc";
		List<Integer> indices = findAnagrams(s, p);
		System.out.println(indices); // Output: [0, 6]
	}
}
