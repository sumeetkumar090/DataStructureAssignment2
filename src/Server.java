// Class to implement Servers with serve function and compareTo
public class Server implements Comparable<Server> {

	int serverId;
	double efficiency;
	int customerCount = 0;
	double idleTime = 0;
	double serviceStartTimeStamp = 0;
	double serviceFinishTimeStamp = 0;
	Customer currentCustomer = new Customer(-1, 0, 0);

	// public void serve(Customer customer) {
	// currentCustomer = customer;
	// serviceStartTimeStamp = (this.serviceFinishTimeStamp >
	// customer.arrivalTime ? this.serviceFinishTimeStamp :
	// customer.arrivalTime);
	// idleTime += serviceStartTimeStamp - serviceFinishTimeStamp;
	// serviceFinishTimeStamp = serviceStartTimeStamp + efficiency *
	// customer.serviceTime;
	// customerCount++;
	// }
	public void serve(Customer customer) {
		currentCustomer = customer;
		serviceStartTimeStamp = customer.allocationTime;
		idleTime += serviceStartTimeStamp - serviceFinishTimeStamp;
		serviceFinishTimeStamp = customer.completionTime;
		customerCount++;
	}

	public Server(int serverId, double efficiency) {
		this.serverId = serverId;
		this.efficiency = efficiency;
	}

	@Override
	public int compareTo(Server o) {
		if (this.efficiency > o.efficiency)
			return -1;
		else if (this.efficiency == o.efficiency)
			return 0;
		else
			return 1;
	}

}
