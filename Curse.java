import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class Curse {
	static int N, M, A;
	int[][] board;

	public int[][] readInput() {
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader("curse.in"));

			String line = reader.readLine();
			String[] lineSplit = line.split(" ");
			N = Integer.parseInt(lineSplit[0]);
			M = Integer.parseInt(lineSplit[1]);
			A = Integer.parseInt(lineSplit[2]);
			board = new int[A][N];
			for (int i = 0; i < A; i++) {
				String[] splittedRow = reader.readLine().split(" ");
				for (int j = 0; j < N; j++) {
					board[i][j] = Integer.parseInt(splittedRow[j]);
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		return board;
	}

	public ArrayList<Integer> getResult() {
		Curse curse = new Curse();
		int[][] matrix = curse.readInput();
		HashMap<Integer, ArrayList<Integer>> map = new HashMap<>();
		for (int i = 1; i <= M; i++) {
			map.put(i, new ArrayList<Integer>());
		}
		int[] betterCars = new int[M + 1];
		betterCars[0] = -1;

		for (int i = 0; i < A - 1; i++) {
			for (int j = 0; j < N; j++) {
				if (matrix[i][j] != matrix[i + 1][j]) {
					map.get(matrix[i][j]).add(matrix[i + 1][j]);
					betterCars[matrix[i + 1][j]]++;
					break;
				}
			}
		}
		ArrayList<Integer> result = new ArrayList<>();
		for (int i = 1; i <= M; i++) {
			if (betterCars[i] == 0) {
				betterCars[i] = -1;
				if (!result.contains(i)) {
					result.add(i);
					for (int j : map.get(i)) {
						betterCars[j]--;

					}
				}
				i = 0;
			}
		}
		return result;
	}

	public static void main(String[] args) throws IOException {
		Curse curse = new Curse();
		ArrayList<Integer> result = curse.getResult();
		FileWriter writer = new FileWriter("curse.out");
		BufferedWriter buffer = new BufferedWriter(writer);
		for (int i : result) {
			buffer.write(i + " ");

		}
		buffer.close();
	}
}




