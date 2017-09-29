// Priority Queue using generic type implementation using heap 
// Handling priority of Events and Servers
public class PriorityEventQueue<ElementType extends Comparable<ElementType>> {
	ElementType[] arr;
	int lastIndex;

	// public static void main(String[] args) {
	// {
	// // empty creation
	// EventQueue1<String> pq = new EventQueue1<String>();
	// System.out.println(pq.toString());
	// }
	// {
	// // creation with 1 element
	// EventQueue1<String> pq = new EventQueue1<String>();
	// pq.enqueue("a");
	//
	// System.out.println(pq.toString());
	// }
	//
	// {
	// EventQueue1<String> pq = new EventQueue1<String>();
	//
	// pq.enqueue("a");
	// assert (pq.toString().substring(0, 1) == "a");
	// pq.enqueue("b");
	// assert (pq.toString().substring(0, 1) == "b");
	// pq.enqueue("a");
	// assert (pq.toString().substring(0, 1) == "a");
	// pq.dequeue();
	// System.out.println(pq.toString());
	// pq.enqueue("d");
	// System.out.println(pq.toString());
	// pq.enqueue("e");
	// pq.dequeue();
	// System.out.println(pq.toString());
	// pq.enqueue("a");
	// System.out.println(pq.toString());
	// }
	// }

	public PriorityEventQueue() {
		arr = (ElementType[]) new Comparable[20000];
		lastIndex = -1;
	}

	// Inserting an element in the queue
	public void enqueue(ElementType elementType) {
		if (lastIndex == arr.length - 1)
			resize(2 * lastIndex + 1);
		arr[++lastIndex] = elementType;
		topDown(lastIndex);
	}

	// Removing an element from Queue
	public ElementType dequeue() {
		if (isEmpty())
			return null;
		ElementType t = arr[0];

		exch(0, lastIndex);
		arr[lastIndex] = null;
		lastIndex--;
		if (lastIndex != -1)
			bottomUp(0);

		// resize this array
		// if (lastIndex == (arr.length - 1) / 4)
		// resize((arr.length - 1) / 2 + 1);
		return t;
	}

	// helper methods toString
	public String toString() {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i <= lastIndex; i++)
			sb.append(arr[i].toString() + " ");
		return sb.toString();
	}

	//Checking for empty queue
	public boolean isEmpty() {
		return lastIndex == -1;
	}

	// Resize the priority queue
	private void resize(int maxSize) {
		ElementType[] copy = (ElementType[]) new Comparable[maxSize];
		for (int i = 1; i <= lastIndex; i++)
			copy[i] = arr[i];
		arr = copy;
	}

	
	//Top Down Approach
	private void topDown(int k) {
		while (k > 0 && less((k - 1) / 2, k)) {
			exch(k / 2, k);
			k = k / 2;
		}
	}

	// Bottom Up Approach
	private void bottomUp(int k) {
		while (2 * k + 1 <= lastIndex) {
			int j = 2 * k + 1;
			if (j < lastIndex && less(j, j + 1))
				j = j + 1;
			if (less(j, k))
				break;
			exch(k, j);
			k = j;
		}
	}

	// Comparing the elements
	private boolean less(int i, int j) {
		if (arr[i].compareTo(arr[j]) < 0)
			return true;
		return false;
	}

	// Swap the elements
	private void exch(int i, int j) {
		ElementType temp = arr[i];
		arr[i] = arr[j];
		arr[j] = temp;
	}
}