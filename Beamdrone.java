import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.PriorityQueue;

public class Beamdrone {

	public static  final int INF = (int) 1e9;
	public static int dimX, dimY, startX, startY, endX, endY;
	public static char [][] maze = null;


	class MyStructure implements Comparable<MyStructure> {
		int x;			//cordonatele celulei din matrice
		int y;
		int prevX;		//coordonatele celulei anterioare
		int prevY;
		int direction;  //1 pe verticala, 0 pe orizontala
		int cost;

		public MyStructure(int x, int y, int prevX, int prevY, int direction, int cost) {
			this.x = x;
			this.y = y;
			this.prevX = prevX;
			this.prevY = prevY;
			this.direction = direction;
			this.cost = cost;
		}

		@Override
		public int compareTo(MyStructure o) {
			return Integer.compare(this.cost, o.cost);
		}
	}

	static boolean isInsideGrid(int x, int y, int rows, int cols) {
		return (x >= 0 && x < rows && y >= 0 && y < cols);
	}

	public char[][] readInput() {
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader("beamdrone.in"));
			String[] line = br.readLine().split(" ");
			dimX = Integer.parseInt(line[0]);
			dimY = Integer.parseInt(line[1]);
			line = br.readLine().split(" ");

			startX = Integer.parseInt(line[0]);
			startY = Integer.parseInt(line[1]);
			endX = Integer.parseInt(line[2]);
			endY = Integer.parseInt(line[3]);

			maze = new char[dimX][dimY];


			for (int i = 0; i < dimX; i++) {
				String reading = br.readLine();
				for (int j = 0; j < dimY; j++) {
					maze[i][j] = reading.charAt(j);
				}
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return maze;
	}

	private int[][] dijkstra() {
		int[][] results = new int[dimX][dimY];

		for (int i = 0; i < dimX; i++) {
			for (int j = 0; j < dimY; j++) {
				results[i][j] = INF;
			}
		}

		PriorityQueue<MyStructure> pq = new PriorityQueue<>();

		results[startX][startY] = 0;
		// ma ocup de cei 4 vecini initiali
		if (isInsideGrid(startX - 1, startY, dimX, dimY)
				&& maze[startX - 1][startY] != 'W') { // nord
			results[startX - 1][startY] = 0;
			pq.add(new MyStructure(startX - 1, startY, startX, startY,1, 0));
		}
		if (isInsideGrid(startX + 1, startY, dimX, dimY)
				&& maze[startX + 1][startY] != 'W') { // sud
			results[startX + 1][startY] = 0;
			pq.add(new MyStructure(startX + 1, startY, startX, startY, 1, 0));
		}
		if (isInsideGrid(startX, startY - 1, dimX, dimY)
				&& maze[startX][startY - 1] != 'W') { // vest
			results[startX][startY - 1] = 0;
			pq.add(new MyStructure(startX, startY - 1, startX, startY, 0, 0));
		}
		if (isInsideGrid(startX, startY + 1, dimX, dimY)
				&& maze[startX][startY + 1] != 'W') { // est
			results[startX][startY + 1] = 0;
			pq.add(new MyStructure(startX, startY + 1, startX, startY, 0, 0));
		}

		while (!pq.isEmpty()) {
			MyStructure currentTp = pq.poll();

			if (currentTp.cost > results[currentTp.x][currentTp.y]) {
				continue;
			}

			// verificare vecin nordic
			if (currentTp.x != currentTp.prevX + 1) { // nu am venit de acolo
				if (isInsideGrid(currentTp.x - 1, currentTp.y, dimX, dimY)
						&& maze[currentTp.x - 1][currentTp.y] != 'W') {
					// celula valida
					int addedValue = 0;
					if (currentTp.direction != 1) {
						addedValue = 1;
					}

					if (results[currentTp.x][currentTp.y] + addedValue
							<= results[currentTp.x - 1][currentTp.y]) {
						// daca gasesc drum mai bun fac update si bag in coada
						results[currentTp.x - 1][currentTp.y]
								= results[currentTp.x][currentTp.y] + addedValue;
						pq.add(new MyStructure(currentTp.x - 1, currentTp.y, currentTp.x,
								currentTp.y, 1, results[currentTp.x - 1][currentTp.y]));
					}
				}
			}

			// verific vecin sudic
			if (currentTp.x != currentTp.prevX - 1) { // nu am venit de acolo
				if (isInsideGrid(currentTp.x + 1, currentTp.y, dimX, dimY)
						&& maze[currentTp.x + 1][currentTp.y] != 'W') {
					// celula valida
					int addedValue = 0;
					if (currentTp.direction != 1) {
						addedValue = 1;
					}

					if (results[currentTp.x][currentTp.y] + addedValue
							<= results[currentTp.x + 1][currentTp.y]) {
						// daca gasesc drum mai bun fac update si bag in coada
						results[currentTp.x + 1][currentTp.y]
								= results[currentTp.x][currentTp.y] + addedValue;
						pq.add(new MyStructure(currentTp.x + 1, currentTp.y, currentTp.x,
								currentTp.y, 1, results[currentTp.x + 1][currentTp.y]));
					}
				}
			}

			// verific vecin vestic
			if (currentTp.y != currentTp.prevY + 1) { // nu am venit de acolo
				if (isInsideGrid(currentTp.x, currentTp.y - 1, dimX, dimY)
						&& maze[currentTp.x][currentTp.y - 1] != 'W') {
					// celula valida
					int addedValue = 0;
					if (currentTp.direction != 0) {
						addedValue = 1;
					}

					if (results[currentTp.x][currentTp.y] + addedValue
							<= results[currentTp.x][currentTp.y - 1]) {
						// daca gasesc drum mai bun fac update si bag in coada
						results[currentTp.x][currentTp.y - 1]
								= results[currentTp.x][currentTp.y] + addedValue;
						pq.add(new MyStructure(currentTp.x, currentTp.y - 1, currentTp.x,
								currentTp.y, 0, results[currentTp.x][currentTp.y - 1]));
					}
				}
			}

			// verific vecin estic
			if (currentTp.y != currentTp.prevY - 1) { // nu am venit de acolo
				if (isInsideGrid(currentTp.x, currentTp.y + 1, dimX, dimY)
						&& maze[currentTp.x][currentTp.y + 1] != 'W') {
					// celula valida
					int addedValue = 0;
					if (currentTp.direction != 0) {
						addedValue = 1;
					}

					if (results[currentTp.x][currentTp.y] + addedValue
							<= results[currentTp.x][currentTp.y + 1]) {
						// daca gasesc drum mai bun fac update si bag in coada
						results[currentTp.x][currentTp.y + 1]
								= results[currentTp.x][currentTp.y] + addedValue;
						pq.add(new MyStructure(currentTp.x, currentTp.y + 1, currentTp.x,
								currentTp.y, 0, results[currentTp.x][currentTp.y + 1]));
					}
				}
			}
		}

		return results;
	}



	public static void main(String[] args) throws IOException {
		BufferedWriter bw = new BufferedWriter(new FileWriter("beamdrone.out"));
		Beamdrone bd = new Beamdrone();
		char[][] maze = bd.readInput();
		int[][] res = bd.dijkstra();

		bw.write("" + res[endX][endY]);

		bw.close();
	}
}
