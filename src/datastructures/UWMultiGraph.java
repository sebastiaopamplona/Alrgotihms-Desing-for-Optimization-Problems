package datastructures;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class UWMultiGraph { //Undirected Weighted
	
	private int numV;
	private int numE;
	private Map<Integer, LinkedList<Edge>> edges;
	private List<Edge> allEdges;
	private int[] degree;
	
	
	public UWMultiGraph(int v){
		numV = v;
		numE = 0;
		degree = new int[v*2];
		edges = new HashMap<Integer, LinkedList<Edge>>(v*2); //at most 2 same edges out of 1 vertex
		allEdges = new ArrayList<Edge>(v*2);
	}
	
	public void addEdge(Edge e){
		numE++;
		int hash = getHash(e.getFrom(), e.getTo());
		if(edges.get(hash) == null){
			edges.put(hash, new LinkedList<Edge>());
			edges.get(hash).push(e);
		} else
			edges.get(hash).push(e);
		
		allEdges.add(e);
		degree[e.getFrom()]++;
		degree[e.getTo()]++;
	}
	
	public void addEdge(int v0, int v1, int w, boolean affectDegree){
		numE++;
		int hash = getHash(v0, v1);
		Edge e = new Edge(v0, v1, w);
		if(edges.get(hash) == null){
			edges.put(hash, new LinkedList<Edge>());
			edges.get(hash).push(e);
		} else
			edges.get(hash).push(e);
		
		allEdges.add(e);
		if(affectDegree){
			degree[v0]++;
			degree[v1]++;
		}
	}
	
	public int getWeight(int v0, int v1){
		return (edges.get(getHash(v0, v1)) != null) ? edges.get(getHash(v0, v1)).peek().getWeight() : -1;
	}
	
	public List<Edge> getEdges(int src){
		List<Edge> es = new ArrayList<Edge>(numV-1);
		for(int v = 0; v < src; v++)
			es.addAll(edges.get(getHash(src, v)));
		
		for(int v = src+1; v < numV; v++)
			es.addAll(edges.get(getHash(src, v)));
		
		return es;
	}
	
	public List<Integer> getVertices(){
		List<Integer> vs = new ArrayList<Integer>(numV);
		for(int i = 0; i < numV; i++)
			vs.add(i);
		
		return vs;
	}
	
	private int getHash(int v0, int v1) {
		return Math.min(v0, v1) * numV + Math.max(v0, v1);
	}
	
	public int getNumV() {
		return numV;
	}
	
	public List<Edge> getAllEdges(){
		return allEdges;
	}
	
	public Map<Integer, LinkedList<Edge>> getEdgesMap (){
		return edges;
	}
	
	public int[] getDegree(){
		return degree;
	}
	
	public int getNumE(){
		return numE;
	}
}
