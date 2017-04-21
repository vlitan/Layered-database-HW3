package BLL;

import models.OrderfHasProduct;

public class OrderfHasProductBLO extends BLO<OrderfHasProduct>{
	private static OrderfHasProductBLO instance;
	
	public static OrderfHasProductBLO instance(){
		if (instance == null){
			instance = new OrderfHasProductBLO();
		}
		return (instance);
	}
}
