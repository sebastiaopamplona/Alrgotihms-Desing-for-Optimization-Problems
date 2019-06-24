package algorithms;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import datastructures.LinkedListArrayMultiGraph;
import datastructures.UWMultiGraph;

public class GreedyMST {

	private static final int ORIGIN = 0;
	private UWMultiGraph g;
	private int numNodes;
	private LinkedListArrayMultiGraph gmst;
	private List<Integer> path;
	
	public GreedyMST(UWMultiGraph g){
		this.g = g;
		numNodes = g.getNumV();
		gmst = new LinkedListArrayMultiGraph(numNodes);
		path = new ArrayList<Integer>(numNodes + 1); 
		solve();
	}
	
	public void solve() {
		Prim primAlgorithm = new Prim(g, ORIGIN);
		gmst = primAlgorithm.getDMST();
		calculatePermutation(ORIGIN);
		path.add(ORIGIN);
	}
	
	public void calculatePermutation(int v){
		path.add(v);
		Iterator<Integer> i = gmst.outNodes[v].listIterator();
		while (i.hasNext()) {
			int n = i.next();
			calculatePermutation(n);
		}
	}
	
	public LinkedListArrayMultiGraph getMST () {
		return gmst;
	}
	
	public List<Integer> getPath(){
		return path;
	}
	
	public int calculateCost(){
		int c = 0;
		for(int i = 0; i < numNodes; i++)
			c += g.getWeight(path.get(i), path.get(i+1));
		
		return c;
	}
	
}
