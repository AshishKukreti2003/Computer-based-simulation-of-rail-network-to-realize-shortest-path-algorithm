import java.util.*;
import java.io.*;

public class Graph_M {
	public class Vertex {
		HashMap<String, Integer> nbrs = new HashMap<>();
	}

	static HashMap<String, Vertex> vtces;

	public Graph_M() {
		vtces = new HashMap<>();
	}

	public int numVetex() {
		return this.vtces.size();
	}

	public boolean containsVertex(String vname) {
		return this.vtces.containsKey(vname);
	}

	public void addVertex(String vname) {
		Vertex vtx = new Vertex();
		vtces.put(vname, vtx);
	}

	public void removeVertex(String vname) {
		Vertex vtx = vtces.get(vname);
		ArrayList<String> keys = new ArrayList<>(vtx.nbrs.keySet());

		for (String key : keys) {
			Vertex nbrVtx = vtces.get(key);
			nbrVtx.nbrs.remove(vname);
		}

		vtces.remove(vname);
	}

	public int numEdges() {
		ArrayList<String> keys = new ArrayList<>(vtces.keySet());
		int count = 0;

		for (String key : keys) {
			Vertex vtx = vtces.get(key);
			count = count + vtx.nbrs.size();
		}

		return count / 2;
	}

	public boolean containsEdge(String vname1, String vname2) {
		Vertex vtx1 = vtces.get(vname1);
		Vertex vtx2 = vtces.get(vname2);

		if (vtx1 == null || vtx2 == null || !vtx1.nbrs.containsKey(vname2)) {
			return false;
		}

		return true;
	}

	public void addEdge(String vname1, String vname2, int value) {
		Vertex vtx1 = vtces.get(vname1);
		Vertex vtx2 = vtces.get(vname2);

		if (vtx1 == null || vtx2 == null || vtx1.nbrs.containsKey(vname2)) {
			return;
		}

		vtx1.nbrs.put(vname2, value);
		vtx2.nbrs.put(vname1, value);
	}

	public void removeEdge(String vname1, String vname2) {
		Vertex vtx1 = vtces.get(vname1);
		Vertex vtx2 = vtces.get(vname2);

		// check if the vertices given or the edge between these vertices exist or not
		if (vtx1 == null || vtx2 == null || !vtx1.nbrs.containsKey(vname2)) {
			return;
		}

		vtx1.nbrs.remove(vname2);
		vtx2.nbrs.remove(vname1);
	}

	public void display_Map() {
		System.out.println("\t Bangalore g Map");
		System.out.println("\t------------------");
		System.out.println("----------------------------------------------------\n");
		ArrayList<String> keys = new ArrayList<>(vtces.keySet());

		for (String key : keys) {
			String str = key + " =>\n";
			Vertex vtx = vtces.get(key);
			ArrayList<String> vtxnbrs = new ArrayList<>(vtx.nbrs.keySet());

			for (String nbr : vtxnbrs) {
				str = str + "\t" + nbr + "\t";
				if (nbr.length() < 16)
					str = str + "\t";
				if (nbr.length() < 8)
					str = str + "\t";
				str = str + vtx.nbrs.get(nbr) + "\n";
			}
			System.out.println(str);
		}
		System.out.println("\t------------------");
		System.out.println("---------------------------------------------------\n");

	}

	public void display_Stations() {
		System.out.println("\n***********************************************************************\n");
		ArrayList<String> keys = new ArrayList<>(vtces.keySet());
		int i = 1;
		for (String key : keys) {
			System.out.println(i + ". " + key);
			i++;
		}
		System.out.println("\n***********************************************************************\n");
	}

	/////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public boolean hasPath(String vname1, String vname2, HashMap<String, Boolean> processed) {
		// DIR EDGE
		if (containsEdge(vname1, vname2)) {
			return true;
		}

		// MARK AS DONE
		processed.put(vname1, true);

		Vertex vtx = vtces.get(vname1);
		ArrayList<String> nbrs = new ArrayList<>(vtx.nbrs.keySet());

		// TRAVERSE THE NBRS OF THE VERTEX
		for (String nbr : nbrs) {

			if (!processed.containsKey(nbr))
				if (hasPath(nbr, vname2, processed))
					return true;
		}

		return false;
	}

	private class DijkstraPair implements Comparable<DijkstraPair> {
		String vname;
		String psf;
		int cost;

		 
		@Override
		public int compareTo(DijkstraPair o) {
			return o.cost - this.cost;
		}
	}

	public int dijkstra(String src, String des, boolean nan) {
		int val = 0;
		HashMap<String, DijkstraPair> map = new HashMap<>();
		PriorityQueue<DijkstraPair> heap = new PriorityQueue<>(Comparator.comparingInt(pair -> pair.cost));
	
		for (String key : vtces.keySet()) {
			DijkstraPair np = new DijkstraPair();
			np.vname = key;
			np.cost = Integer.MAX_VALUE;
	
			if (key.equals(src)) {
				np.cost = 0;
				np.psf = key;
			}
	
			heap.add(np);
			map.put(key, np);
		}
	
		while (!heap.isEmpty()) {
			DijkstraPair rp = heap.poll();
	
			if (rp.vname.equals(des)) {
				val = rp.cost;
				break;
			}
	
			map.remove(rp.vname);
	
			Vertex v = vtces.get(rp.vname);
			for (Map.Entry<String, Integer> entry : v.nbrs.entrySet()) {
				String nbr = entry.getKey();
				int edgeCost = entry.getValue();
				if (map.containsKey(nbr)) {
					DijkstraPair gp = map.get(nbr);
					int nc;
					if (nan) {
						nc = rp.cost + 120 + 40 * edgeCost;
					} else {
						nc = rp.cost + edgeCost;
					}
					if (nc < gp.cost) {
						gp.psf = rp.psf + nbr;
						gp.cost = nc;
						heap.remove(gp);
						heap.add(gp);
					}
				}
			}
		}
		return val;
	}
	

	private class Pair {
		String vname;
		String psf;
		int min_dis;
		int min_time;
	}

	public String Get_Minimum_Distance(String src, String dst) {
		int min = Integer.MAX_VALUE;
		// int time = 0;
		String ans = "";
		HashMap<String, Boolean> processed = new HashMap<>();
		LinkedList<Pair> stack = new LinkedList<>();

		// create a new pair
		Pair sp = new Pair();
		sp.vname = src;
		sp.psf = src + "  ";
		sp.min_dis = 0;
		sp.min_time = 0;

		// put the new pair in stack
		stack.addFirst(sp);

		// while stack is not empty keep on doing the work
		while (!stack.isEmpty()) {
			// remove a pair from stack
			Pair rp = stack.removeFirst();

			if (processed.containsKey(rp.vname)) {
				continue;
			}

			// processed put
			processed.put(rp.vname, true);

			// if there exists a direct edge b/w removed pair and destination vertex
			if (rp.vname.equals(dst)) {
				int temp = rp.min_dis;
				if (temp < min) {
					ans = rp.psf;
					min = temp;
				}
				continue;
			}

			Vertex rpvtx = vtces.get(rp.vname);
			ArrayList<String> nbrs = new ArrayList<>(rpvtx.nbrs.keySet());

			for (String nbr : nbrs) {
				// process only unprocessed nbrs
				if (!processed.containsKey(nbr)) {

					// make a new pair of nbr and put in queue
					Pair np = new Pair();
					np.vname = nbr;
					np.psf = rp.psf + nbr + "  ";
					np.min_dis = rp.min_dis + rpvtx.nbrs.get(nbr);
					// np.min_time = rp.min_time + 120 + 40*rpvtx.nbrs.get(nbr);
					stack.addFirst(np);
				}
			}
		}
		ans = ans + Integer.toString(min);
		return ans;
	}

	public String Get_Minimum_Time(String src, String dst) {
		int min = Integer.MAX_VALUE;
		String ans = "";
		HashMap<String, Boolean> processed = new HashMap<>();
		LinkedList<Pair> stack = new LinkedList<>();

		// create a new pair
		Pair sp = new Pair();
		sp.vname = src;
		sp.psf = src + "  ";
		sp.min_dis = 0;
		sp.min_time = 0;

		// put the new pair in queue
		stack.addFirst(sp);

		// while queue is not empty keep on doing the work
		while (!stack.isEmpty()) {

			// remove a pair from queue
			Pair rp = stack.removeFirst();

			if (processed.containsKey(rp.vname)) {
				continue;
			}

			// processed put
			processed.put(rp.vname, true);

			// if there exists a direct edge b/w removed pair and destination vertex
			if (rp.vname.equals(dst)) {
				int temp = rp.min_time;
				if (temp < min) {
					ans = rp.psf;
					min = temp;
				}
				continue;
			}

			Vertex rpvtx = vtces.get(rp.vname);
			ArrayList<String> nbrs = new ArrayList<>(rpvtx.nbrs.keySet());

			for (String nbr : nbrs) {
				// process only unprocessed nbrs
				if (!processed.containsKey(nbr)) {

					// make a new pair of nbr and put in queue
					Pair np = new Pair();
					np.vname = nbr;
					np.psf = rp.psf + nbr + "  ";
					// np.min_dis = rp.min_dis + rpvtx.nbrs.get(nbr);
					np.min_time = rp.min_time + 120 + 40 * rpvtx.nbrs.get(nbr);
					stack.addFirst(np);
				}
			}
		}
		Double minutes = Math.ceil((double) min / 60);
		ans = ans + Double.toString(minutes);
		return ans;
	}

	public ArrayList<String> get_Interchanges(String str) {
		ArrayList<String> arr = new ArrayList<>();
		String res[] = str.split("  ");
		arr.add(res[0]);
		int count = 0;
		for (int i = 1; i < res.length - 1; i++) {
			int index = res[i].indexOf('~');
			String s = res[i].substring(index + 1);

			if (s.length() == 2) {
				String prev = res[i - 1].substring(res[i - 1].indexOf('~') + 1);
				String next = res[i + 1].substring(res[i + 1].indexOf('~') + 1);

				if (prev.equals(next)) {
					arr.add(res[i]);
				} else {
					arr.add(res[i] + " ==> " + res[i + 1]);
					i++;
					count++;
				}
			} else {
				arr.add(res[i]);
			}
		}
		arr.add(Integer.toString(count));
		arr.add(res[res.length - 1]);
		return arr;
	}

	public static void Create_g_Map(Graph_M g) {
		g.addVertex("Namma Metro Station");
        g.addVertex("MG Road Station");
        g.addVertex("Silk Board Station");
        g.addVertex("KR Puram Station");
        g.addVertex("Jayanagar Station");
        g.addVertex("Lakshmi Nagar Station");
        g.addVertex("Peenya Industry Station");
        g.addVertex("Sarjapur Road Station");
        g.addVertex("Kengeri Station");
        g.addVertex("Hosahalli Station");
		
		g.addVertex("Yelagirinagar Station");
		g.addVertex("Mahadevapura Station");
		g.addVertex("Hosakote Station");
		g.addVertex("Attibele Station");

        g.addEdge("Namma Metro Station", "MG Road Station", 10);
        g.addEdge("Namma Metro Station", "Jayanagar Station", 5);
        g.addEdge("MG Road Station", "Jayanagar Station", 10);
        g.addEdge("MG Road Station", "Lakshmi Nagar Station", 20);
        g.addEdge("Jayanagar Station", "Lakshmi Nagar Station", 10);
        g.addEdge("Jayanagar Station", "Peenya Industry Station", 20);
        g.addEdge("Lakshmi Nagar Station", "Peenya Industry Station", 10);
        g.addEdge("Lakshmi Nagar Station", "Silk Board Station", 30);
        g.addEdge("Peenya Industry Station", "Silk Board Station", 20);
        g.addEdge("Silk Board Station", "KR Puram Station", 20);
        g.addEdge("KR Puram Station", "Hosahalli Station", 20);
        g.addEdge("Hosahalli Station", "Sarjapur Road Station", 20);
        g.addEdge("Sarjapur Road Station", "Kengeri Station", 20);
        g.addEdge("Namma Metro Station", "Peenya Industry Station", 15); 
        g.addEdge("Peenya Industry Station", "KR Puram Station", 35);
        g.addEdge("Jayanagar Station", "Kengeri Station", 40);
        g.addEdge("Hosahalli Station", "Silk Board Station", 45);

		
        g.addEdge("Kengeri Station", "Yelagirinagar Station", 20);
        g.addEdge("Yelagirinagar Station", "Mahadevapura Station", 20);
		g.addEdge("Mahadevapura Station", "Kengeri Station", 40);
        g.addEdge("Mahadevapura Station", "Hosakote Station", 20);
        g.addEdge("Hosakote Station", "Attibele Station", 20);
	}

	public static String[] printCodelist() {
		System.out.println("List of station along with their codes:\n");
		ArrayList<String> keys = new ArrayList<>(vtces.keySet());
		String codes[] = new String[keys.size()];
		for (int i = 0; i < keys.size(); i++) {
			String temp = "";
			StringTokenizer stname = new StringTokenizer(keys.get(i));
			while (stname.hasMoreTokens()) {
				String part = stname.nextToken();
				char c = part.charAt(0);
				while (c > 47 && c < 58) {
					temp += c;
					c = part.charAt(temp.length());
				}
				if ((c < 48 || c > 57) && c < 123)
					temp += c;
			}
			if (temp.length() < 2)
				temp += Character.toUpperCase(temp.charAt(1));
			codes[i] = temp;
			System.out.print((i + 1) + ". " + keys.get(i) + "\t");
			if (keys.get(i).length() < 22)
				System.out.print("\t");
			if (keys.get(i).length() < 14)
				System.out.print("\t");
			if (keys.get(i).length() < 6)
				System.out.print("\t");
			System.out.println(temp);
		}
		return codes;
	}

	public String getRoute(String src, String dst) {
		HashMap<String, Boolean> processed = new HashMap<>();
		LinkedList<Pair> queue = new LinkedList<>();
		StringBuilder route = new StringBuilder();
	
		Pair sp = new Pair();
		sp.vname = src;
		sp.psf = src;
		queue.addLast(sp);
	
		while (!queue.isEmpty()) {
			Pair rp = queue.removeFirst();
	
			if (processed.containsKey(rp.vname)) {
				continue;
			}
	
			processed.put(rp.vname, true);
	
			if (rp.vname.equals(dst)) {
				route.append(rp.psf);
				break;
			}
	
			Vertex rpvtx = vtces.get(rp.vname);
			ArrayList<String> nbrs = new ArrayList<>(rpvtx.nbrs.keySet());
	
			for (String nbr : nbrs) {
				if (!processed.containsKey(nbr)) {
					Pair np = new Pair();
					np.vname = nbr;
					np.psf = rp.psf + " -> " + nbr;
					queue.addLast(np);
				}
			}
		}
		return route.toString();
	}
	
	

	public static void main(String[] args) throws IOException {
		Graph_M g = new Graph_M();
		Create_g_Map(g);
	
		System.out.println("\n\t\t\t****WELCOME TO THE g APP*****");
	
		BufferedReader inp = new BufferedReader(new InputStreamReader(System.in));
		// STARTING SWITCH CASE
		while (true) {
			System.out.println("\t\t\t\t~~LIST OF ACTIONS~~\n\n");
			System.out.println("1. LIST ALL THE STATIONS IN THE MAP");
			System.out.println("2. SHOW THE g MAP");
			System.out.println("3. GET SHORTEST DISTANCE FROM A 'SOURCE' STATION TO 'DESTINATION' STATION");
			System.out.println("4: GET SHORTEST PATH (DISTANCE WISE) TO REACH FROM A 'SOURCE' STATION TO 'DESTINATION' STATION");
			System.out.println("5. EXIT THE MENU");
			System.out.print("\nENTER YOUR CHOICE FROM THE ABOVE LIST (1 to 5) : ");
			int choice = -1;
			try {
				choice = Integer.parseInt(inp.readLine());
			} catch (Exception e) {
				// default will handle
			}
			System.out.print("\n***********************************************************\n");
			if (choice == 5) {
				System.exit(0);
			}
			switch (choice) {
				case 1:
					g.display_Stations();
					break;
	
				case 2:
					g.display_Map();
					break;
	
				case 3:
					ArrayList<String> keys = new ArrayList<>(vtces.keySet());
					String codes[] = printCodelist();
					System.out.println(
							"\n1. TO ENTER SERIAL NO. OF STATIONS\n2. TO ENTER CODE OF STATIONS\n3. TO ENTER NAME OF STATIONS\n");
					System.out.println("ENTER YOUR CHOICE:");
					int ch = Integer.parseInt(inp.readLine());
					int j;
	
					String st1 = "", st2 = "";
					System.out.println("ENTER THE SOURCE AND DESTINATION STATIONS");
					if (ch == 1) {
						st1 = keys.get(Integer.parseInt(inp.readLine()) - 1);
						st2 = keys.get(Integer.parseInt(inp.readLine()) - 1);
					} else if (ch == 2) {
						String a, b;
						a = (inp.readLine()).toUpperCase();
						for (j = 0; j < keys.size(); j++)
							if (a.equals(codes[j]))
								break;
						st1 = keys.get(j);
						b = (inp.readLine()).toUpperCase();
						for (j = 0; j < keys.size(); j++)
							if (b.equals(codes[j]))
								break;
						st2 = keys.get(j);
					} else if (ch == 3) {
						st1 = inp.readLine();
						st2 = inp.readLine();
					} else {
						System.out.println("Invalid choice");
						System.exit(0);
					}
	
					HashMap<String, Boolean> processed = new HashMap<>();
					if (!g.containsVertex(st1) || !g.containsVertex(st2) || !g.hasPath(st1, st2, processed))
						System.out.println("THE INPUTS ARE INVALID");
					else
						System.out.println("SHORTEST DISTANCE FROM " + st1 + " TO " + st2 + " IS "
								+ g.dijkstra(st1, st2, false) + "KM\n");
					break;
	
				case 4:
					System.out.println("Enter the source station:");
					String source = inp.readLine().trim();
					System.out.println("Enter the destination station:");
					String destination = inp.readLine().trim();
	
					if (!g.containsVertex(source) || !g.containsVertex(destination)) {
						System.out.println("Invalid input. One or both of the stations do not exist in the map.");
					} else if (!g.hasPath(source, destination, new HashMap<>())) {
						System.out.println("No path exists between the source and destination stations.");
					} else {
						System.out.println("Shortest path from " + source + " to " + destination + " (Distance wise):");
						System.out.println(g.getRoute(source, destination));
					}
					break;
	
				default: // If switch expression does not match with any case,
						 // default statements are executed by the program.
						 // No break is needed in the default case
					System.out.println("Please enter a valid option! ");
					System.out.println("The options you can choose are from 1 to 5. ");
			}
		}
	}
	
}
