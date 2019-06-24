import utils.Utils;

import java.util.List;
import java.util.Scanner;

import algorithms.Christofides;
import algorithms.GreedyMST;
import datastructures.LinkedListArrayMultiGraph;
import datastructures.UWMultiGraph;

public class Testing {

	public static LinkedListArrayMultiGraph gmst;

	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);

		int numNodes = in.nextInt();
		int n = 0;
		int numEdges = Utils.getNumEdges(numNodes);
		UWMultiGraph g = new UWMultiGraph(numNodes);
		
		int[][] matrix = new int[numNodes][numNodes];
		int v = numNodes;
		in.nextLine();
		while(v > 0){
			String[] line = in.nextLine().split("\\s+");
			for(int i = n; i < numNodes; i++){
				matrix[n][i] = Integer.parseInt(line[i]);
			}
			v--;
			n++;
		}
		
		for(int i = 0; i < numNodes; i++)
			for(int j = i+1; j < numNodes; j++)
				g.addEdge(i, j, matrix[i][j], true);
		
		//////////////////////////////////////////////////////
		
		GreedyMST greedymst = new GreedyMST(g);
		List<Integer> path = greedymst.getPath();
		System.out.println("[GREEDY MST]");
		System.out.print("Path: ");
		for(Integer node : path)
			System.out.print(node + " ");
		
		System.out.println();
		System.out.println("Cost: " + greedymst.calculateCost());
		
		System.out.println();
		
		//////////////////////////////////////////////////////
		
		Christofides christofides = new Christofides(g, 0); 
		path = christofides.getPathSolution1();
		System.out.println("[CHRISTOFIDES SOLUTION 1]");
		System.out.print("Path: ");
		for(Integer node : path)
			System.out.print(node + " ");
		
		System.out.println();
		System.out.println("Cost: " + christofides.getCostSolution1());
		
		System.out.println();
		
		//////////////////////////////////////////////////////
		
		path = christofides.getPathSolution2();
		System.out.println("[CHRISTOFIDES SOLUTION 2]");
		System.out.print("Path: ");
		for(Integer node : path)
			System.out.print(node + " ");
		
		System.out.println();
		System.out.println("Cost: " + christofides.getCostSolution2());
		
		//////////////////////////////////////////////////////
		
		in.close();
	}

}
