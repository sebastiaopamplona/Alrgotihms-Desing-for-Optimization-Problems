package datastructures;

import java.util.ArrayList;
import java.util.List;

public class DAPOLinkedList {

	public DAPONode head;
	public DAPONode tail;
	public int size;
	
	public DAPOLinkedList(DAPONode head){
		this.head = head;
		tail = null;
		size = 1;
	}
	
	public void insertAfter(int v0, int v1){
		
		DAPONode curr = this.head;
		DAPONode newNode = new DAPONode(v1);
		
		while(curr.v != v0)
			curr = curr.next;
			
		DAPONode newNodeNext = curr.next; // 7
		
		curr.setNext(newNode); // 3 -> 4
		newNode.setNext(newNodeNext); // 4 -> 7
		
		//TODO: tratar da tail
		
		size++;
	}
	
	public void add(int v1){
		if(size > 1){
			DAPONode newNode = new DAPONode(v1);
			tail.setNext(newNode);
			tail = newNode;
			size++;
		} else {
			tail = new DAPONode(v1);
			head.setNext(tail);
			size++;
		}
	}
	
	public List<Integer> asList(){
		List<Integer> l = new ArrayList<Integer>(size);
		
		DAPONode curr = head;
		l.add(curr.v);
		while(curr.next != null){
			l.add(curr.next.v);
			curr = curr.next;
		}
		
		return l;
	}
	
	public void print(){
		DAPONode curr = head;
		System.out.print(curr.v + " ");
		while(curr.next != null){
			System.out.print(curr.next.v + " ");
			curr = curr.next;
		}
		System.out.println();
	}
	
}
