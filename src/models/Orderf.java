package models;

import java.sql.Date;

public class Orderf {
	public Orderf() {
		super();
	}

	public int idOrderf;
	public int price;
	public String observations;
	public int idCustomer;
	
	public Orderf(int price, Date date, String observations) {
		super();
		this.price = price;
		this.observations = observations;
	}

	public Orderf(int idOrder, int price, Date date, String observations) {
		super();
		this.idOrderf = idOrder;
		this.price = price;
		this.observations = observations;
	}
	
	@Override
	public String toString(){
		return ("Order has id: " + idOrderf + " and price: " + price + " and observations: " + observations);
	}
}
