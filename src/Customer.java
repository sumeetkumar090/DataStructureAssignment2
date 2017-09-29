
// Customer class containing details of customer
public class Customer {
	int customerId;
	double arrivalTime;
	double completionTime;
	double allocationTime;
	double serviceTime;
	double waitingTime;

	Customer(int customerId, double arrivalTime, double serviceTime) {
		this.customerId = customerId;
		this.arrivalTime = arrivalTime;
		this.serviceTime = serviceTime;
	}

	public void allocate(Server server, double currentTime) {
		this.allocationTime = currentTime;
		this.completionTime = currentTime + this.serviceTime * server.efficiency;
		this.waitingTime = this.allocationTime - this.arrivalTime;
	}
}
