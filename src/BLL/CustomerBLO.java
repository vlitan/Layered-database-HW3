package BLL;

import models.Customer;

public class CustomerBLO extends BLO<Customer>{
	private static CustomerBLO instance;
	
	public static CustomerBLO instance(){
		if (instance == null){
			instance = new CustomerBLO();
		}
		return (instance);
	}
}
