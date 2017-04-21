package BLL;

import javax.swing.JOptionPane;

import models.Orderf;
import models.OrderfHasProduct;
import models.Product;

public class OrderfBLO extends BLO<Orderf>{
	private static OrderfBLO instance;
	
	public static OrderfBLO instance(){
		if (instance == null){
			instance = new OrderfBLO();
		}
		return (instance);
	}

	public boolean addProduct(Orderf o, Product p){
		if ((o == null) || (p == null)) return false;
		
		//checkQuantity
		if (p.quantity == 0){
			JOptionPane.showMessageDialog(null, p.name + " is out of stock!", "Warehouse error" , JOptionPane.INFORMATION_MESSAGE);
			System.out.println("[OrderfBLO] " + p.name + " is out of stock!");
			return (false);
		}
		//decrease quantity
		p.quantity--;
		ProductBLO.instance().update(p);
		//add to total price
		o.price += p.price;
		//update order
		update(o);
		//create relationship
		if (OrderfHasProductBLO.instance().insert(new OrderfHasProduct(p.idProduct, o.idOrderf)) != -1){
			return true;
		}
		else{
			System.out.println("[OrderfBLO] an error occured while inserting the relationship");
			return false;
		}
	}
}
