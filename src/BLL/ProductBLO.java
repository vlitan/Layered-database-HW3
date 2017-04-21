package BLL;

import dal.DAO;
import models.Product;

public class ProductBLO extends BLO<Product>{
	private static ProductBLO instance;
	
	public static ProductBLO instance(){
		if (instance == null){
			instance = new ProductBLO();
		}
		return (instance);
	}
	
	@Override
	public int insert(Product p){
		if (p == null) return (-1);
		if ((p.quantity >= 0) && (p.price >= 0)){
			return (DAO.instance().insert(p));
		}
		else {
			return (-1);
		}
	}
	
	@Override
	public boolean update(Product p){
		if (p == null) return (false);
		if ((p.quantity >= 0) && (p.price >= 0)){
			return (DAO.instance().update(p));
		}
		else {
			return (false);
		}
	}
}
