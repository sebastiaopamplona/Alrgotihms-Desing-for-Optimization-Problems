package algorithms;

import java.util.PriorityQueue;
import java.util.Queue;

import datastructures.UWMultiGraph;
import datastructures.Edge;
import datastructures.LinkedListArrayMultiGraph;

public class Prim {

	private LinkedListArrayMultiGraph dmst;
	private UWMultiGraph umst;
	
	public Prim (UWMultiGraph g, int origin) {
		int numNodes = g.getNumV();
		dmst = new LinkedListArrayMultiGraph(numNodes);
		umst = new UWMultiGraph(numNodes);
		generateMST(g, origin, numNodes);
	}
	
	private void generateMST (UWMultiGraph g, int origin, int numNodes) {
		boolean[] added = new boolean[g.getNumV()];
		int mstAdded = 0;
		
		added[origin] = true;
		mstAdded++;
		Queue<Edge> edgesAvailable = new PriorityQueue<Edge>();

		int lastAdded = origin;
		while( mstAdded < numNodes ) {
			for(Edge edge : g.getEdges(lastAdded)) {
				if( !added[edge.getTo()] )
					edgesAvailable.add(edge);
			}

			Edge cheapestEdge = edgesAvailable.remove(); //O(1)
			while( added[cheapestEdge.getTo()] )
				cheapestEdge = edgesAvailable.remove();
			
			added[cheapestEdge.getTo()] = true;
			mstAdded++;
			dmst.addEdge(cheapestEdge.getFrom(), cheapestEdge.getTo());
			umst.addEdge(cheapestEdge.getFrom(), cheapestEdge.getTo(), cheapestEdge.getWeight(), true);
			lastAdded = cheapestEdge.getTo();
		}
	}
	
	public LinkedListArrayMultiGraph getDMST() {
		return dmst;
	}
	
	public UWMultiGraph getUMST() {
		return umst;
	}
}
