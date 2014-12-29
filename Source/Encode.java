import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

public class Encode {

	private Scanner firstScanner;
	private Scanner secondScanner;
	private PrintWriter writer;

	private int p;
	private int[] generator;
	private int[] toBeEncoded;

	private int[][] generatorMatrix;

	private int cycleInterval;
	private int[] encoded;

	private void initStreams(String firstFileName, String secondFileName, String outFileName) throws FileNotFoundException {
		firstScanner = new Scanner(new File(firstFileName));
		secondScanner = new Scanner(new File(secondFileName));
		writer = new PrintWriter(new File(outFileName));
	}

	private void processStreams() {
		readVariables();
		initGeneratorMatrix();
		processEncoding();
		printAnswer();
	}

	private void initGeneratorMatrix() {
		cycleInterval = getCycleInterval();
		generatorMatrix = new int[cycleInterval][generator.length];
		for (int i = 0; i < cycleInterval; i++) {
			generatorMatrix[i] = getShiftedGenerator(i);
		}
	}

	private int[] getShiftedGenerator(int shiftNumber) {
		int[] result = new int[generator.length];
		System.arraycopy(generator, 0, result, shiftNumber, generator.length - cycleInterval + 1);
		return result;
	}

	private void processEncoding() {
		encoded = new int[toBeEncoded.length / cycleInterval * generator.length];
		for (int chunkIndex = 0; chunkIndex < toBeEncoded.length / cycleInterval; chunkIndex++) {
			int[] chunk = new int[cycleInterval];
			System.arraycopy(toBeEncoded, chunkIndex * cycleInterval, chunk, 0, cycleInterval);
			encodeChunk(chunk, chunkIndex);
		}
	}
	
	private void encodeChunk(int[] chunk, int chunkIndex) {
		int[] encodedChunk = getEncodedChunk(chunk);
		//noinspection ManualArrayCopy
		for (int i = 0; i < encodedChunk.length; i++) {
			encoded[chunkIndex * generator.length + i] = encodedChunk[i];
		}
	}
	
	private int[] getEncodedChunk(int[] chunk) {
		int[] result = new int[generator.length];
		for (int i = 0; i < generatorMatrix.length; i++) {
			for (int j = 0; j < generatorMatrix[0].length; j++) {
				result[j] = (result[j] + generatorMatrix[i][j] * chunk[i]) % p;
			}
		}
		return result;
	}

	private int getCycleInterval() {
		for (int i = generator.length - 1; i >= 0; i--) {
			if (generator[i] > 0) {
				return generator.length - i;
			}
		}
		return -1;
	}

	private void printAnswer() {
		writer.println(encoded.length);
		//noinspection ForLoopReplaceableByForEach
		for (int i = 0; i < encoded.length; i++) {
			writer.print(encoded[i] + ((i != encoded.length - 1) ? " " : ""));
		}
	}

	private void readVariables() {
		p = firstScanner.nextInt();
		int n = firstScanner.nextInt();

		generator = new int[n];
		for (int i = 0; i < n; i++) {
			generator[i] = firstScanner.nextInt();
		}
		
		int k = secondScanner.nextInt();
		toBeEncoded = new int[k];
		for (int i = 0; i < k; i++) {
			toBeEncoded[i] = secondScanner.nextInt();
		}
	}

	private void closeStreams() {
		if (firstScanner != null) {
			firstScanner.close();
		}
		if (secondScanner != null) {
			secondScanner.close();
		}
		if (writer != null) {
			writer.close();
		}
	}

	public static void main(String[] args) throws Exception {
		Encode encode = new Encode();
		try {
			encode.initStreams(args[0], args[1], args[2]);
			encode.processStreams();
		} finally {
			encode.closeStreams();
		}
	}
}
