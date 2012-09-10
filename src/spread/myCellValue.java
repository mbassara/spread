package spread;

public class myCellValue {
	private double cost;
	
	public myCellValue(double cost) {
		this.cost = cost;
	}
	
	public double getCost() {
		return cost;
	}

	public void setCost(double cost) {
		this.cost = cost;
	}
	
	@Override
	public String toString(){
		return String.valueOf(cost);
	}
}
