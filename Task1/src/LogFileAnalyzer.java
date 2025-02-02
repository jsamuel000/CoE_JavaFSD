import java.io.*;
import java.util.*;

public class LogFileAnalyzer {
	private Set keywords;

	// Constructor to initialize with configurable keywords
	public LogFileAnalyzer(Set keywords) {
		this.keywords = keywords;
	}

	// Analyzes the log file for specific keywords
	public void analyzeLogFile(String inputFile, String outputFile) {
		Map keywordCounts = new HashMap();

		// Initialize counts for all keywords
		for (Iterator it = keywords.iterator(); it.hasNext();) {
			String keyword = (String) it.next();
			keywordCounts.put(keyword, new Integer(0));
		}

		try (BufferedReader reader = new BufferedReader(new FileReader(inputFile));
				BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {

			String line;
			while ((line = reader.readLine()) != null) {
				for (Iterator it = keywords.iterator(); it.hasNext();) {
					String keyword = (String) it.next();
					if (line.contains(keyword)) {
						keywordCounts.put(keyword, new Integer(((Integer) keywordCounts.get(keyword)).intValue() + 1));
						writer.write(line);
						writer.newLine();
						break; // Avoid double-counting if multiple keywords are in one line
					}
				}
			}

			writer.newLine();
			writer.write("--- Keyword Summary ---");
			writer.newLine();

			for (Iterator it = keywordCounts.entrySet().iterator(); it.hasNext();) {
				Map.Entry entry = (Map.Entry) it.next();
				writer.write(entry.getKey() + ": " + entry.getValue());
				writer.newLine();
			}

		} catch (IOException e) {
			System.err.println("Error processing log file: " + e.getMessage());
		}
	}

	public static void main(String[] args) {
		Set keywords = new HashSet();
		keywords.add("ERROR");
		keywords.add("WARNING");
		keywords.add("CRITICAL");

		LogFileAnalyzer analyzer = new LogFileAnalyzer(keywords);

		String inputFile = "logfile.txt";
		String outputFile = "analysis_output.txt";

		analyzer.analyzeLogFile(inputFile, outputFile);

		System.out.println("Log file analysis complete. Results saved to " + outputFile);
	}
}
