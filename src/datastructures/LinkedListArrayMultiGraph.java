package datastructures;

import java.util.LinkedList;
import java.util.List;

public class LinkedListArrayMultiGraph {

	public LinkedList<Integer>[] outNodes;
	public List<Edge> allEdges;
	private int numV;
	private int numE;
	public int[] degree;
	
	@SuppressWarnings("unchecked")
	public LinkedListArrayMultiGraph(int numV, List<Edge> edges, boolean undirected){
		this.numV = numV;
		this.allEdges = edges;
		numE = edges.size();
		degree = new int[numV];
		
		outNodes = new LinkedList[numV];
		for(int i = 0; i < numV; i++)
			outNodes[i] = new LinkedList<>();
	
		for(Edge e : edges){
			outNodes[e.getFrom()].add(e.getTo());
			if(undirected)
				outNodes[e.getTo()].add(e.getFrom());
		}
	}
	
	@SuppressWarnings("unchecked")
	public LinkedListArrayMultiGraph(int numV){
		this.numV = numV;
		degree = new int[numV];
		
		outNodes = new LinkedList[numV];
		for(int i = 0; i < numV; i++)
			outNodes[i] = new LinkedList<>();
		
	}
	
	public void addEdge(int src, int dst){
		outNodes[src].add(dst);
		numE++;
		degree[src]++;
		degree[dst]++;
	}
	
	public void removeEdge(int src, int dst, boolean undirected, boolean affectDegree){
		outNodes[src].remove(new Integer(dst));
		if(undirected)
			outNodes[dst].remove(new Integer(src));
		
		numE--;
		if(affectDegree){
			degree[src]--;
			degree[dst]--;
		}
	}
	
	public LinkedList<Integer> getOutNodes(int src){
		return outNodes[src];
	}
	
	public int[] getDegree(){
		return degree;
	}
	
	public int getNumV(){
		return numV;
	}
	
	public int getNumE(){
		return numE;
	}
}
