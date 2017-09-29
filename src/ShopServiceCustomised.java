// Class to implement the shop Service Simulation
import java.io.File;
import java.util.Scanner;

public class ShopServiceCustomised {
	private static int noOfServers; // no of serving servers
	static Server[] servers; // Server array
	private double currentTime = 0;// get the current time of server

	// Variable to print the statistics
	private int numberOfCustomerServed;
	private double lastCustomerServiceFinishTime;
	private int totalCustomerQueueLength;
	private int customerQueueMaxLength;
	private Customer[] customers2 = new Customer[100000];// maintaing customer
															// array
	/* fcfs queue of Customers */
	FifoQueue<Customer> customerFIFOQueue = new FifoQueue<>(20000);
	/* Priority queue for events */
	PriorityEventQueue<Event> eventQueue = new PriorityEventQueue();
	/* Priority Queue for getting the Idle servers */
	PriorityEventQueue<Server> idleServers = new PriorityEventQueue();//

	// Function for starting the simulation
	public void startShop() {
		readFile();
		int i = 0;
		// Read 1st CustomerArrival event from file and add it to the priority
		// queue
		if (i <= numberOfCustomerServed) {
			Customer nextCustomer = customers2[i++];
			eventQueue.enqueue(new Event(EventType.ARRIVAL, nextCustomer.arrivalTime, nextCustomer.customerId, -1));
		}
		// While event priority queue not empty
		while (!eventQueue.isEmpty()) {
			// Get next event from event priority queue
			Event nextEvent = eventQueue.dequeue();
			switch (nextEvent.eventType) {
			// If Event = CustomerArrival
			case ARRIVAL:
				/*
				 * Add customer to customer FIFO queue Read next CustomerArrival
				 * event from file If not EOF add Event to event priority queue
				 */
				Customer customer = customers2[nextEvent.customerIndex];
				currentTime = nextEvent.timeStamp;
				customerFIFOQueue.enqueue(customer);
				if (i < numberOfCustomerServed) {
					Customer nextCustomer = customers2[i++];
					eventQueue.enqueue(
							new Event(EventType.ARRIVAL, nextCustomer.arrivalTime, nextCustomer.customerId, -1));
				}
				if (customerFIFOQueue.size() > customerQueueMaxLength)
					customerQueueMaxLength = customerFIFOQueue.size();
				totalCustomerQueueLength += customerFIFOQueue.size() - 1;
				break;
			/*
			 * Else // must be a CustomerCompletePayment event Set server[Event]
			 * to idle and sort it End if
			 */
			case FINISH:
				currentTime = nextEvent.timeStamp;
				ProcessFinishedServer(nextEvent);
				break;
			}
			/*
			 * If customer FIFO not empty and idle server available Get Next
			 * Customer from FIFO Find fastest idle server... set server’s idle
			 * flag to busy... calculate server’s finish time and do its
			 * stats... add CustomerCompletePayment event to priority queue end
			 * if
			 */
			while (!customerFIFOQueue.isQueueEmpty() && !idleServers.isEmpty()) {
				Customer customer = customerFIFOQueue.dequeue();
				totalCustomerQueueLength += customerFIFOQueue.size();
				Server server = idleServers.dequeue();
				customer.allocate(server, currentTime); // order is important
														// here call
				// allocate before server
				server.serve(customer);
				// add finish event to event queue
				eventQueue.enqueue(
						new Event(EventType.FINISH, customer.completionTime, customer.customerId, server.serverId));
			}
		}
		for (int j = 0; j < noOfServers; j++) {
			servers[j].idleTime += lastCustomerServiceFinishTime - servers[j].serviceFinishTimeStamp;
		}

	}

	// get finish time
	private void ProcessFinishedServer(Event nextEvent) {
		Server server = servers[nextEvent.serverId];
		idleServers.enqueue(server);
		// server.currentCustomer = null;
		lastCustomerServiceFinishTime = nextEvent.timeStamp;
	}

	// Read file from the console and seprate the nuumber of server
	// arrival Time , Service Time
	public void readFile() {
		Scanner file = null;
		String filename;
		try {
			Scanner readFilename = new Scanner(System.in);
			System.out.println("Enter file name");
			filename = readFilename.next();
			file = new Scanner(new File(filename));
			noOfServers = Integer.valueOf(file.nextLine().trim());
			// System.out.println("Servers is " + noOfServers);
			servers = new Server[noOfServers]; // create the servers

			for (int i = 0; i < noOfServers; i++) {
				double efficiency = Double.valueOf(file.nextLine().trim());
				servers[i] = new Server(i, efficiency);
				idleServers.enqueue(servers[i]);
			}
			int customerId = 0;
			while (file.hasNextLine()) {
				// Get arrival time and Finish Time
				String customerData = file.nextLine().trim();
				String[] customerEvent = customerData.split("\\s+", 2);
				double arrivalTime = Double.valueOf(customerEvent[0]);
				double serviceTime = Double.valueOf(customerEvent[1]);
				customers2[customerId] = new Customer(customerId, arrivalTime, serviceTime);
				customerId++;
			}
			numberOfCustomerServed = customerId;
		} catch (Exception e) {
		}
	}

	// Print all the stats
	public void printStatistics() {
		System.out.println("1. The number of customers served: \t" + numberOfCustomerServed);
		System.out.println("-------------------------------------------------------------------------");
		System.out.println(
				"2. The time at which the last customer completed service: \t" + this.lastCustomerServiceFinishTime);
		System.out.println("-------------------------------------------------------------------------");
		System.out.println("3. The greatest length reached by the queue: \t" + customerQueueMaxLength);
		System.out.println("-------------------------------------------------------------------------");
		System.out.println("4. The average length of the queue:  \t"
				+ ((double) totalCustomerQueueLength) / (2 * numberOfCustomerServed));
		System.out.println("-------------------------------------------------------------------------");
		double totalWaitTime = 0;
		for (int i = 0; i < numberOfCustomerServed; i++) {
			totalWaitTime += customers2[i].waitingTime;
		}
		System.out.println(
				"5. The average time spent by a customer in the queue: \t" + totalWaitTime / numberOfCustomerServed);
		System.out.println("-------------------------------------------------------------------------");
		System.out.println("6 The number of customers serverd by server \n");
		System.out.printf("%10s  %10s   %15s   %1s %n", "Checkout", "Priority", "CustomersServed     ", "IdleTime");
		for (int i = 0; i < servers.length; i++) {
			System.out.printf("%10s  %10s   %15s   %1s %n", servers[i].serverId, servers[i].efficiency,
					servers[i].customerCount, servers[i].idleTime);
		}
	}

	// main function to start simulation and print the stats
	public static void main(String[] args) {
		ShopServiceCustomised s = new ShopServiceCustomised();
		s.startShop();
		s.printStatistics();
	}
}
