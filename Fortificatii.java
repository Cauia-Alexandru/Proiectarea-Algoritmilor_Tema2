import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.PriorityQueue;
import java.util.Scanner;

public class Fortificatii {
	static int noduri, muchii, nrOraseBarbare;
	static long fortificatii;
	static int source = 1;
	static ArrayList<Integer> oraseBarb;
	public static final int NMAX = 100005;
	public static final long INF = Long.MAX_VALUE;

	public class Pair implements Comparable<Pair> {
		public int destination;
		public long cost;

		public Pair(int destination, long cost) {
			this.destination = destination;
			this.cost = cost;
		}

		public int compareTo(Pair rhs) {
			return Long.compare(cost, rhs.cost);
		}
	}

	@SuppressWarnings("unchecked")
	ArrayList<Pair>[] adj = new ArrayList[NMAX];

	public ArrayList<Pair>[] readInput() {
		try {
			Scanner sc = new Scanner(new BufferedReader(new FileReader(
					"fortificatii.in")));
			noduri = sc.nextInt();
			muchii = sc.nextInt();
			fortificatii = sc.nextInt();
			nrOraseBarbare = sc.nextInt();
			oraseBarb = new ArrayList<>();
			for (int i = 0; i < nrOraseBarbare; i++) {
				oraseBarb.add(sc.nextInt());
			}

			for (int i = 1; i <= noduri; i++) {
				adj[i] = new ArrayList<>();
			}
			for (int i = 1; i <= muchii; i++) {
				int x, y, w;
				x = sc.nextInt();
				y = sc.nextInt();
				w = sc.nextInt();
				adj[x].add(new Pair(y, w));
				adj[y].add(new Pair(x, w));
			}
			sc.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return adj;
	}


	public long dijkstra() {
		ArrayList<Long> d = new ArrayList<>();
		ArrayList<Pair>[] adj = readInput();

		for (int node = 0; node <= noduri; node++) {
			d.add(INF);
		}

		PriorityQueue<Pair> pq = new PriorityQueue<>();
		d.set(source, (long) 0);
		pq.add(new Pair(source, 0));

		while (!pq.isEmpty()) {
			long cost = pq.peek().cost;
			int node = pq.poll().destination;
			if (cost > d.get(node)) {
				continue;
			}

			for (var e : adj[node]) {
				//nu claculez drumul minim si pentru muchiile
				//care duc spre oras barbar
				if (oraseBarb.contains(e.destination)) {
					continue;
				}

				int neigh = e.destination;
				long w = e.cost;

				if (d.get(node) + w < d.get(neigh)) {
					d.set(neigh, d.get(node) + w);
					pq.add(new Pair(neigh, d.get(neigh)));
				}
			}
		}
		ArrayList<Long> res = new ArrayList<>();

		for (Integer i : oraseBarb) {
			for (Pair x : adj[i]) {
				if (!oraseBarb.contains(x.destination) && d.get(x.destination) != INF) {
					res.add(d.get(x.destination) + x.cost);
				}
			}
		}

		//repartizez fortificatiile
		Collections.sort(res);
		long dif, k, fRamase;
		while (fortificatii > 0) {
			for (int i = 0; i < res.size() - 1; i++) {
				if (fortificatii == 0) {
					break;
				}
				if (res.get(i) != res.get(i + 1)) {
					dif = res.get(i + 1) - res.get(i);
					k = dif * (i + 1);
					if (fortificatii - k >= 0) {
						for (int j = 0; j <= i; j++) {
							res.set(j, res.get(j) + dif);
						}
						fortificatii -= k;
					} else {
						fRamase = fortificatii / (i + 1);
						for (int j = 0; j <= i; j++) {
							res.set(j, res.get(j) + fRamase);
							fortificatii = 0;
							break;
						}
					}
				} else {
					continue;
				}

			}
			long impartEgal = fortificatii / res.size();
			int m = 0;
			res.set(m, res.get(m) + impartEgal);
			fortificatii = 0;
		}
		return res.get(0);
	}

	public static void main(String[] args) throws IOException {
		Fortificatii fort = new Fortificatii();
		long res = fort.dijkstra();

		FileWriter writer = new FileWriter("fortificatii.out");
		BufferedWriter buffer = new BufferedWriter(writer);
		buffer.write("" + res);
		buffer.close();

	}
}
