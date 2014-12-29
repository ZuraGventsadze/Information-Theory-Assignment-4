import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

public class ParityCheck {

	private static final String YES = "YES";
	private static final String NO = "NO";

	private Scanner scanner;
	private PrintWriter writer;

	private int p;

	private int[] toBeDivided;
	private int[] generator;
	private int[] checker;

	private void initStreams(String inFileName, String outFileName) throws FileNotFoundException {
		scanner = new Scanner(new File(inFileName));
		writer = new PrintWriter(new File(outFileName));
	}

	private void processStreams() {
		readVariables();
		processDivision();
		printAnswer();
	}

	private void processDivision() {
		int toBeDividedPos = getFirstNonZeroPosition(toBeDivided);
		int generatorPos = getFirstNonZeroPosition(generator);

		while (toBeDividedPos >= 0 && toBeDividedPos <= generatorPos) {
			int factor = getFactorOfNumber(generator[generatorPos], toBeDivided[toBeDividedPos]);
			int shiftNumber = generatorPos - toBeDividedPos;
			checker[checker.length - 1 - shiftNumber] = factor;

			int[] productArray = shiftArrayToRight(getMultipliedArray(generator, factor),
					generatorPos, shiftNumber);

			toBeDivided = subtractArrays(toBeDivided, productArray);
			toBeDividedPos = getFirstNonZeroPosition(toBeDivided);
		}
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

	private void printAnswer() {
		if (!checkForReminder()) {
			writer.print(NO);
			return;
		}
		writer.println(YES);
		printChecker();
	}

	private void printChecker() {
		for (int i = checker.length - 1; i > 0; i--) {
			writer.print(checker[i] + ((i != 1) ? " " : ""));
		}
	}

	private boolean checkForReminder() {
		for (int n : toBeDivided) {
			if (n != 0) {
				return false;
			}
		}
		return true;
	}

	private void readVariables() {
		p = scanner.nextInt();
		int n = scanner.nextInt();

		toBeDivided = new int[n + 1];
		generator = new int[n + 1];
		checker = new int[n + 1];

		for (int i = 0; i < n; i++) {
			generator[n - i] = scanner.nextInt();
		}
		toBeDivided[0] = 1;
		toBeDivided[toBeDivided.length - 1] = p - 1;
	}

	private void closeStreams() {
		if (scanner != null) {
			scanner.close();
		}
		if (writer != null) {
			writer.close();
		}
	}

	public static void main(String[] args) throws Exception {
		ParityCheck parityCheck = new ParityCheck();
		try {
			parityCheck.initStreams(args[0], args[1]);
			parityCheck.processStreams();
		} finally {
			parityCheck.closeStreams();
		}
	}
}
