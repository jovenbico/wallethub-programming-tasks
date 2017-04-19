import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

public class TopPhrases {

	public static void main(String[] args) {
		// TODO - set absolute path of a file
		String fileAbsolutePath = "/home/joven/Development/workspaces/wallet-hub/wallethub-tasks/tests/TopPhrases.txt";
		// TODO - set desire limit
		int threshold = 10;
		String lineSeparator = "\\|";

		TopPhrases topPhrases = new TopPhrases(fileAbsolutePath, lineSeparator, threshold);
		System.out.println(">> Simple Counter <<");
		topPhrases.process();
		topPhrases.output();

		System.out.println(">> Lossy Counter <<");
		topPhrases.lossyCounterProcess();
		topPhrases.output();
	}

	private String fileAbsolutePath;
	private String lineSeparator;
	private int limit;
	private Map<String, Integer> heap;
	private Map<String, Integer> top;

	public TopPhrases(String fileAbsolutePath, String lineSeparator, int threshold) {
		this.fileAbsolutePath = fileAbsolutePath;
		this.lineSeparator = lineSeparator;
		this.limit = threshold;
		this.heap = new HashMap<>();
	}

	private void process() {

		FileInputScanner fileInputScanner = new FileInputScanner(fileAbsolutePath, lineSeparator);

		try {

			heap = new PhraseCounter(fileInputScanner).getOutput();

		} finally {
			fileInputScanner.close();
		}

	}

	private void lossyCounterProcess() {

		FileInputScanner fileInputScanner = new FileInputScanner(fileAbsolutePath, lineSeparator);

		try {

			heap = new LossyCounter(fileInputScanner, 0.2D, 0.1D, 500).getOutput();

		} finally {
			fileInputScanner.close();
		}

	}

	private void output() {
		// top phrases base on count and limit
		top = heap.entrySet().stream().sorted(Map.Entry.comparingByValue(Comparator.reverseOrder())).limit(limit)
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (k, v) -> k, LinkedHashMap::new));

		top.forEach((key, value) -> {
			System.out.println(value + " " + key);
		});

	}

	interface FileReader {
		boolean hasNextLine();

		String[] nextLine();

		void close();
	}

	class FileInputScanner implements FileReader {

		private String lineSeparator;
		private FileInputStream inputStream;
		private Scanner scanner;

		public FileInputScanner(String path, String lineSeparator) {
			try {

				this.lineSeparator = lineSeparator;
				this.inputStream = new FileInputStream(path);
				this.scanner = new Scanner(inputStream);

			} catch (FileNotFoundException e) {
				System.err.println(e.getMessage());
			}
		}

		@Override
		public boolean hasNextLine() {
			return scanner.hasNextLine();
		}

		@Override
		public String[] nextLine() {
			return scanner.nextLine().split(lineSeparator);
		}

		@Override
		public void close() {

			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
				}
			}
			if (scanner != null) {
				scanner.close();
			}

		}

	}

	abstract class PhraseFrequecy {
		protected FileReader fileReader;

		public PhraseFrequecy(FileReader fileReader) {
			this.fileReader = fileReader;
		}

		public abstract Map<String, Integer> getOutput();
	}

	class PhraseCounter extends PhraseFrequecy {

		public PhraseCounter(FileReader fileReader) {
			super(fileReader);
		}

		@Override
		public Map<String, Integer> getOutput() {
			Map<String, Integer> phraseCount = new HashMap<>();

			while (fileReader.hasNextLine()) {
				Arrays.stream(fileReader.nextLine()).forEach(phrase -> {
					phraseCount.merge(phrase.trim(), 1, Integer::sum);
				});
			}

			return phraseCount;
		}
	}

	class LossyCounter extends PhraseFrequecy {

		private double frequency;
		private double error;
		private int batchSize;

		public LossyCounter(FileReader fileReader, double frequency, double error, int batchSize) {
			super(fileReader);
			this.frequency = frequency;
			this.error = error * frequency;
			this.batchSize = batchSize;
		}

		@Override
		public Map<String, Integer> getOutput() {
			Map<String, Integer> phraseCount = new HashMap<>();
			int totalPhrases = 0;

			while (fileReader.hasNextLine()) {
				totalPhrases += 1;

				// increment frequency
				Arrays.stream(fileReader.nextLine()).forEach(phrase -> {
					phraseCount.merge(phrase.trim(), 1, Integer::sum);
				});

				// decrement all frequency-1
				if ((totalPhrases % batchSize) == 0) {
					phraseCount.replaceAll((k, v) -> v - 1);
					phraseCount.entrySet().removeIf(e -> e.getValue().equals(0));
				}
			}

			double frequencyThreshold = ((frequency * totalPhrases) - (error * totalPhrases));
			phraseCount.entrySet().removeIf(e -> e.getValue() <= frequencyThreshold);

			return phraseCount;
		}

	}

}
