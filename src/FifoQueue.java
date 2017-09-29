// Genenric implementation of First Come First Server Circular Queue
import java.util.Arrays;
import javax.management.RuntimeErrorException;

public class FifoQueue<ElementType> {
	// Array <ElementType> queueArr;
	private ElementType[] queueArr;
	int front = 0;
	int rear = 0;
	boolean status = false;
	int currentSize;

	public FifoQueue(int queueSize) {
		queueArr = (ElementType[]) new Object[queueSize];
	}

	/**
	 * Enqueue elements to rear.
	 */
	public void enqueue(ElementType item) {

		// PrintQueue();
		// System.out.println("Adding " + item);
		if (isQueueFull()) {
			// System.out.println("Overflow ! Unable to add element");
		} else {
			queueArr[rear] = item;
			rear = (rear + 1) % queueArr.length;
			currentSize++;
			// System.out.println("added " + item);

			// PrintQueue();
		}
	}

	/**
	 * Dequeue element from Front.
	 */
	public ElementType dequeue() {
		// PrintQueue();
		ElementType element = null;
		if (isQueueEmpty()) {
			throw new RuntimeErrorException(null);
		} else {
			element = queueArr[front];
			queueArr[front] = null;
			front = (front + 1) % queueArr.length;
			currentSize--;
		}
		// System.out.println("Removed element: " + element);
		// PrintQueue();
		return element;

	}

	private void PrintQueue() {
		System.out.println("Queue: " + this);

	}

	/**
	 * Check if queue is full.
	 */
	public boolean isQueueFull() {
		return (currentSize == queueArr.length);
	}

	/**
	 * Check if Queue is empty.
	 */
	public boolean isQueueEmpty() {
		return (currentSize == 0);
	}

	@Override
	public String toString() {
		return "[" + Arrays.toString(queueArr) + "](Front,Rear) (" + Integer.valueOf(front) + ","
				+ Integer.valueOf(rear) + ")";
	}

	public static void main(String a[]) {
		FifoQueue<String> circularQueue = new FifoQueue<>(2);

		System.out.print("1) Overflow");
		circularQueue.PrintQueue();
		circularQueue.enqueue("a");
		circularQueue.enqueue("b");
		circularQueue.enqueue("b");
		circularQueue.PrintQueue();

		System.out.println(circularQueue.dequeue() + " ");
		System.out.println(circularQueue.dequeue() + " ");
		System.out.println(circularQueue.dequeue() + " ");
		System.out.println(circularQueue.dequeue() + " ");
		circularQueue.enqueue("e");
		System.out.println("After enqueueing circular queue");
		System.out.println(circularQueue);
		System.out.println(circularQueue.dequeue() + " ");
		System.out.println(circularQueue.dequeue() + " ");

	}

	public int size() {
		return currentSize;

	}
}