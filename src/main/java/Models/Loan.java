package Models;

public class Loan  {
    protected double amount;
    protected double douwnPayment;
    protected double interestRate;
    protected int duration;
    protected String frequency;
    private Customer customer;
    private Vehicle vehicle;

    // Parameterless constructor (added)
    public Loan() {
        this(0.0, 0.0, 0.0, 0, "", null, null);
    }


    public Loan(double amount, double douwnPayment, double interestRate, int duration, String frequency, Customer customer, Vehicle vehicle) {
        this.amount = amount;
        this.douwnPayment = douwnPayment;
        this.interestRate = interestRate;
        this.duration = duration;
        this.frequency = frequency;
        this.customer = customer;
        this.vehicle = vehicle;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getDouwnPayment() {
        return douwnPayment;
    }

    public void setDouwnPayment(double douwnPayment) {
        this.douwnPayment = douwnPayment;
    }

    public double getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(double interestRate) {
        this.interestRate = interestRate;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    @Override
    public String toString() {
        return customer.getName() +
                ", " + vehicle.getType() +
                ", " + interestRate +
                '%';
    }
}
