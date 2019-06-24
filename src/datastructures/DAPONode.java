package datastructures;

public class DAPONode {

	public int v;
	public DAPONode next;
	
	public DAPONode(int v) {
		this.v = v;
		next = null;
	}
	
	public void setNext(DAPONode next){
		this.next = next;
	}
}