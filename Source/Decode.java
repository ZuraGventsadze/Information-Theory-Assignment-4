import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Decode {

	private Scanner firstScanner;
	private Scanner secondScanner;
	private PrintWriter writer;

	private int p;
	private int[] generator;
	private int[] toBeDecoded;

	private int generatorPos;

	@SuppressWarnings("FieldCanBeLocal")
	private List<Integer> decoded;

	private void initStreams(String firstFileName, String secondFileName, String outFileName) throws FileNotFoundException {
		firstScanner = new Scanner(new File(firstFileName));
		secondScanner = new Scanner(new File(secondFileName));
		writer = new PrintWriter(new File(outFileName));
	}

	private void processStreams() {
		readVariables();
		processDecoding();
		printAnswer();
	}

	private void processDecoding() {
		generator = getReversedArray(generator);
		generatorPos = getFirstNonZeroPosition(generator);
		decoded = new ArrayList<Integer>();

		for (int chunkIndex = 0; chunkIndex < toBeDecoded.length / generator.length; chunkIndex++) {
			int[] chunk = new int[generator.length];
			System.arraycopy(toBeDecoded, chunkIndex * generator.length, chunk, 0, generator.length);
			decodeChunk(chunk);
		}
	}

	private void decodeChunk(int[] chunk) {
		int[] decodedChunk = getDecodedChunk(chunk);
		//noinspection ManualArrayCopy
		for (int i = 0; i < generatorPos + 1; i++) {
			decoded.add(decodedChunk[i]);
		}
	}

	private int[] getDecodedChunk(int[] chunk) {
		return getReversedArray(processDivision(getReversedArray(chunk)));
	}

	private int[] processDivision(int[] toBeDivided) {
		int[] checker = new int[toBeDivided.length];
		int toBeDividedPos = getFirstNonZeroPosition(toBeDivided);

		while (toBeDividedPos >= 0 && toBeDividedPos <= generatorPos) {
			int factor = getFactorOfNumber(generator[generatorPos], toBeDivided[toBeDividedPos]);
			int shiftNumber = generatorPos - toBeDividedPos;
			checker[checker.length - 1 - shiftNumber] = factor;

			int[] productArray = shiftArrayToRight(getMultipliedArray(generator, factor),
					generatorPos, shiftNumber);

			toBeDivided = subtractArrays(toBeDivided, productArray);
			toBeDividedPos = getFirstNonZeroPosition(toBeDivided);
		}
		return checker;
	}

	private int[] subtractArrays(int[] arrayA, int[] arrayB) {
		int[] result = new int[arrayA.length];
		for (int i = 0; i < result.length; i++) {
			result[i] = (p + arrayA[i] - arrayB[i]) % p;
		}
		return result;
	}

	private int[] shiftArrayToRight(int[] array, int startPos, int shiftNumber) {
		int[] result = new int[array.length];
		//noinspection ManualArrayCopy
		for (int i = startPos; i < array.length; i++) {
			result[i - shiftNumber] = array[i];
		}
		return result;
	}

	private int[] getMultipliedArray(int[] array, int factor) {
		int[] result = new int[array.length];
		for (int i = 0; i < array.length; i++) {
			result[i] = (array[i] * factor) % p;
		}
		return result;
	}

	private int getFactorOfNumber(int number, int product) {
		for (int i = 0; i < p; i++) {
			if ((number * i) % p == product) {
				return i;
			}
		}
		return -1;
	}

	private int getFirstNonZeroPosition(int[] array) {
		for (int i = 0; i < array.length; i++) {
			if (array[i] > 0) {
				return i;
			}
		}
		return -1;
	}

	private int[] getReversedArray(int[] array) {
		int[] result = new int[array.length];
		for (int i = 0; i < array.length; i++) {
			result[i] = array[array.length - 1 - i];
		}
		return result;
	}

	private void printAnswer() {
		writer.println(decoded.size());
		//noinspection ForLoopReplaceableByForEach
		for (int i = 0; i < decoded.size(); i++) {
			writer.print(decoded.get(i) + ((i != decoded.size() - 1) ? " " : ""));
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
		toBeDecoded = new int[k];
		for (int i = 0; i < k; i++) {
			toBeDecoded[i] = secondScanner.nextInt();
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
		Decode decode = new Decode();
		try {
			decode.initStreams(args[0], args[1], args[2]);
			decode.processStreams();
		} finally {
			decode.closeStreams();
		}
	}
}
