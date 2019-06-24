package datastructures;

public class Edge implements Comparable<Edge> {

	public Integer from;
	public Integer to;
	public int weight;

	public Edge(Integer from, Integer to, int weight) {
		this.from = from;
		this.to = to;
		this.weight = weight;
	}

	public Integer getFrom() {
		return from;
	}

	public Integer getTo() {
		return to;
	}

	public int getWeight() {
		return weight;
	}

	@Override
	public int compareTo(Edge e) {
		if (this.weight < e.weight)
			return -1;
		if (this.weight > e.weight)
			return 1;

		final int from = this.from.compareTo(e.from);
		if (from != 0)
			return from;

		final int to = this.to.compareTo(e.to);
		if (to != 0)
			return to;

		return 0;
	}

}
