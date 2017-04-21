package models;

public class OrderfHasProduct {
	public int idProduct;
	public int idOrder;
	public int idOrderfHasProduct;
	
	public OrderfHasProduct() {
		super();
	}

	@Override
	public String toString(){
		return ("OrderProduct has idProduct: " + idProduct + " and idOrder: " + idOrder + " and idOrderProduct: " + idOrderfHasProduct);
	}

	public OrderfHasProduct(int idProduct, int idOrder, int idOrderProduct) {
		super();
		this.idProduct = idProduct;
		this.idOrder = idOrder;
		this.idOrderfHasProduct = idOrderProduct;
	}

	public OrderfHasProduct(int idProduct, int idOrder) {
		super();
		this.idProduct = idProduct;
		this.idOrder = idOrder;
	}
}
