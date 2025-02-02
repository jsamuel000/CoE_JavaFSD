class StringProcessor {

	public String reverseString(String str) {
		return new StringBuilder(str).reverse().toString();
	}

	public int countOccurrences(String text, String sub) {
		return text.split(sub, -1).length - 1;
	}

	public String splitAndCapitalize(String str) {
		StringBuilder result = new StringBuilder();
		for (String word : str.split(" ")) {
			if (!word.isEmpty()) {
				result.append(Character.toUpperCase(word.charAt(0))).append(word.substring(1)).append(" ");
			}
		}
		return result.toString().trim();
	}

	public static void main(String[] args) {
		StringProcessor processor = new StringProcessor();

		String testStr = "hello world example";
		System.out.println("Reversed: " + processor.reverseString(testStr));

		String text = "Java is fun. Java is powerful. Java is everywhere.";
		System.out.println("Occurrences of 'Java': " + processor.countOccurrences(text, "Java"));

		System.out.println("Capitalized: " + processor.splitAndCapitalize(testStr));
	}
}
