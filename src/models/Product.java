package models;

public class Product {
	public int idProduct;
	public int price;
	public int quantity;
	public String name;
	
	@Override
	public String toString(){
		return ("Product has idProduct: " + idProduct + " and price: " + price + " and quantity: " + quantity + " name: " + name);
	}
	
	public Product(){
		super();
	}
	
	public Product(int price, int quantity, String name) {
		super();
		this.price = price;
		this.quantity = quantity;
		this.name = name;
	}

	public Product(int idProduct, int price, int quantity, String name) {
		super();
		this.idProduct = idProduct;
		this.price = price;
		this.quantity = quantity;
		this.name = name;
	}

}
