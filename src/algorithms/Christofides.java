package algorithms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import datastructures.DAPOLinkedList;
import datastructures.DAPONode;
import datastructures.Edge;
import java.util.PriorityQueue;
import java.util.Queue;
import datastructures.UWMultiGraph;
import datastructures.LinkedListArrayMultiGraph;

public class Christofides {

	private UWMultiGraph g;
	private int origin;
	private int numV;
	private int costSolution1;
	private int costSolution2;
	private List<Integer> eulerTour;
	private List<Integer> pathSolution1;
	private List<Integer> pathSolution2;
	
	// For Hierholzer Algorithm
	private Map<Integer, LinkedList<Integer>> toEdges;
	private Map<Integer, Integer> existingEdges;

	public Christofides(UWMultiGraph g, int origin) {
		this.g = g;
		this.origin = origin;
		numV = g.getNumV();
		costSolution1 = 0;
		costSolution2 = 0;
		pathSolution1 = new ArrayList<Integer>(numV + 1);
		pathSolution2 = new ArrayList<Integer>(numV + 1);
		solve();
	}

	public void solve() {
		Prim primAlgorithm = new Prim(g, origin);
		UWMultiGraph umst = primAlgorithm.getUMST();
		List<Integer> oddDegree = getOddDegreeVertices(umst);
		UWMultiGraph inducedSubGraph = calculateInducedSubgraph(oddDegree);
		List<Edge> greedyPerfMatching = calculateGreedyPerfMatching(inducedSubGraph.getNumV(),
				inducedSubGraph.getAllEdges());

		UWMultiGraph eulerianMultiGraph = calculateEulerianMultigraph(umst, greedyPerfMatching);
		String hierholzerPath = hierholzerAlgorithm(eulerianMultiGraph);
		pathSolution1(hierholzerPath);
		costSolution1();
		
		LinkedListArrayMultiGraph lleulerianMultiGraph = new LinkedListArrayMultiGraph(eulerianMultiGraph.getNumV(),
												                eulerianMultiGraph.getAllEdges(),
												                true);
		calculateEulerTourIter(lleulerianMultiGraph, origin);
		calculateChristofides(eulerTour, eulerianMultiGraph.getNumV(), origin);
	}
	
	private void pathSolution1 (String path) {
		boolean [] bool = new boolean [numV];
		String [] pathSplit = path.split(" ");
		for(String s : pathSplit) {
			int nodeValue = Integer.parseInt(s);
			if(!bool[nodeValue]) {
				this.pathSolution1.add(nodeValue);
				bool[nodeValue] = true;
			}
		}
		int root = Integer.parseInt(pathSplit[0]);
		this.pathSolution1.add(root);
	}
	
	private void costSolution1 () {
		int cost = 0;
		for(int i = 0; i < pathSolution1.size()-1; i++) {
			int from = pathSolution1.get(i);
			int to = pathSolution1.get(i+1);
			cost += g.getWeight(from, to);
		}
		this.costSolution1 = cost;
	}

	private String hierholzerAlgorithm(UWMultiGraph eulerianMultiGraph) {
		toEdges = new HashMap<Integer, LinkedList<Integer>>(numV);
		existingEdges = new HashMap<Integer, Integer>(numV*numV);

		addHierholzerVertices(eulerianMultiGraph);
		Queue<Integer> keyQueue = new LinkedList<Integer>();
		Integer root;
		String path;

		for (int i = 0; i < eulerianMultiGraph.getNumV(); i++)
			keyQueue.add(i);

		root = keyQueue.remove();
		path = root.toString();

		while (!keyQueue.isEmpty()) {
			String newPath = hierholzerAlgorithmLoop(root);
			if (!newPath.equals("")) {
				path = concatenatePaths(root.toString(), path, newPath);
			}

			while (!keyQueue.isEmpty() && !toEdges.containsKey(root)) {
				root = keyQueue.remove();
			}
		}
		return path;
	}
	
	private void addHierholzerVertices (UWMultiGraph eulerianMultiGraph) {
		List<Edge> edgesList = eulerianMultiGraph.getAllEdges();

		for(Edge edge : edgesList) {
			Integer from = edge.getFrom();
			Integer to = edge.getTo();
			LinkedList<Integer> toEdgesList = toEdges.get(from);
			LinkedList<Integer> fromEdgesList = toEdges.get(to);
			if(toEdgesList == null) {
				List<Integer> newEdgeList = new LinkedList<Integer>();
				newEdgeList.add(to);
				toEdges.put(from, (LinkedList<Integer>) newEdgeList);
			}
			else {
				toEdgesList.add(to);
			}
			
			if(fromEdgesList == null) {
				List<Integer> newEdgeList = new LinkedList<Integer>();
				newEdgeList.add(from);
				toEdges.put(to, (LinkedList<Integer>) newEdgeList);
			}
			else {
				fromEdgesList.add(from);
			}
			
			int hash = getHash(to, from);
			Integer edgesFromNode = existingEdges.get(hash);
			if(edgesFromNode == null) {
				existingEdges.put(hash, 1);
			}
			else {
				existingEdges.put(hash, edgesFromNode++);
			}
		}
	}

	private String concatenatePaths(String regex, String path, String newPath) {
		String[] tempPath = path.split(regex, 2);
		path = tempPath[0] + newPath + tempPath[1];
		return path;
	}

	private String hierholzerAlgorithmLoop(Integer root) {
		String path = "";
		int current = root;

		while (current != -1 && toEdges.get(current) != null) {
			LinkedList<Integer> list = toEdges.get(current);
			int toEdge = list.removeFirst();

			if (list.size() == 0)
				toEdges.remove(current);

			int hash = getHash(current, toEdge);

			if (existingEdges.get(hash) != null) {
				int hashValue = existingEdges.get(hash);
				if (hashValue == 1)
					existingEdges.remove(hash);
				else {
					existingEdges.put(hash, hashValue--);
				}
				// decrementar edges no caso de ser maior que 1
				if (toEdge == root)
					current = -1;
				else {
					current = toEdge;
					path = path + " " + current;
				}
			}
		}
		if (!path.equals(""))
			path = root + path + " " + root;
		return path;
	}

	private int getHash(int v0, int v1) {
		return Math.min(v0, v1) * numV + Math.max(v0, v1);
	}

	public void calculateChristofides(List<Integer> eulertour, int numV, int root) {
		boolean[] added = new boolean[numV];
		eulertour.remove(0);
		pathSolution2.add(root);
		added[root] = true;

		int lastAdded = root;
		for (Integer v : eulertour) {
			if (!added[v]) {
				pathSolution2.add(v);
				costSolution2 += g.getWeight(lastAdded, v);
				lastAdded = v;
				added[v] = true;
			}
		}

		costSolution2 += g.getWeight(lastAdded, root);
		pathSolution2.add(root);
	}

	public void calculateEulerTourIter (LinkedListArrayMultiGraph llemg, int v){
		DAPOLinkedList etour = new DAPOLinkedList(new DAPONode(0));
		LinkedListArrayMultiGraph llemgCopy = new LinkedListArrayMultiGraph(numV, llemg.allEdges, true);
		boolean[] added = new boolean[numV];
		added[v] = true;
		
		Iterator<Integer> i = llemg.outNodes[v].iterator();
		int lastAdded = v;
		while(i.hasNext()){
			int n = i.next();
			llemg.removeEdge(lastAdded, n, true, false);
			etour.add(n);
			
			added[n] = true;
			lastAdded = n;
			i = llemg.outNodes[n].iterator();
		}
		
		for(int k = 0; k < numV; k++){
			if(!added[k]){
				int neighbour = llemgCopy.getOutNodes(k).removeFirst();
				etour.insertAfter(neighbour, k);
				added[k] = true;
			}
		}
		
		eulerTour = etour.asList(); 
	}

	public UWMultiGraph calculateEulerianMultigraph(UWMultiGraph umst, List<Edge> greedyPerfMatching) {
		UWMultiGraph emg = umst;

		while (!greedyPerfMatching.isEmpty())
			emg.addEdge(greedyPerfMatching.remove(0));

		return emg;
	}

	public List<Edge> calculateGreedyPerfMatching(int numV, List<Edge> edges) {
		List<Edge> gpm = new ArrayList<Edge>(edges.size());
		Queue<Edge> edgesAvailable = new PriorityQueue<Edge>();

		Map<Integer, Boolean> covered = new HashMap<Integer, Boolean>(numV);

		for (Edge e : edges) {
			edgesAvailable.add(e);
			covered.put(e.getFrom(), false);
			covered.put(e.getTo(), false);
		}

		Edge e;
		while (!edgesAvailable.isEmpty()) {
			e = edgesAvailable.remove();
			if (!covered.get(e.getFrom()) && !covered.get(e.getTo())) {
				covered.put(e.getFrom(), true);
				covered.put(e.getTo(), true);
				gpm.add(e);
			}
		}

		return gpm;
	}

	public List<Integer> getOddDegreeVertices(UWMultiGraph g) {
		int[] degree = g.getDegree();
		List<Integer> oddDegree = new ArrayList<Integer>(g.getNumV());

		for (int v = 0; v < numV; v++)
			if (degree[v] % 2 != 0)
				oddDegree.add(v);
		return oddDegree;
	}

	public UWMultiGraph calculateInducedSubgraph(List<Integer> oddDegree) {
		UWMultiGraph isg = new UWMultiGraph(oddDegree.size());
		int v0;
		int v1;

		for (int src = 0; src < oddDegree.size(); src++) {
			v0 = oddDegree.get(src);
			for (int dst = src + 1; dst < oddDegree.size(); dst++) {
				v1 = oddDegree.get(dst);
				isg.addEdge(v0, v1, g.getWeight(v0, v1), false);
			}

		}

		return isg;
	}

	public List<Integer> getPathSolution1() {
		return pathSolution1;
	}

	public int getCostSolution1() {
		return costSolution1;
	}
	
	public List<Integer> getPathSolution2() {
		return pathSolution2;
	}

	public int getCostSolution2() {
		return costSolution2;
	}

}
