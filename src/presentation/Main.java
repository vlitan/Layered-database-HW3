package presentation;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.lang.reflect.Field;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import BLL.BLO;
import BLL.BLOManager;
import BLL.OrderfBLO;
import dal.ConnectionFactory;
import dal.DAO;
import models.Customer;
import models.Orderf;
import models.Product;

public class Main {

	static Orderf currentOrder = null;
	
	public static void main(String[] args) {
		System.out.println("[Main] app started");
		EditEntryWindow eew = new EditEntryWindow();
		GUI gui = new GUI();
		
		ActionListener modifierAL = new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				JButton button = (JButton)e.getSource();
				Action action = Action.valueOf(button.getText());
				if (action == Action.INSERT){
					eew.init(gui.getSelectedClass(), Action.INSERT);
				}
				else{
					eew.init(gui.getSelectedObject(), action);
				}
			}};
			
		eew.setDoneActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				try{
					BLOManager.instance().getBLO(gui.getSelectedClass()).applyAction(eew.getAction(), eew.getEditedObject());
					gui.loadInTable(BLOManager.instance().getBLO(gui.getSelectedClass()).selectAll());
				}
				catch (Exception e1){
					System.out.println("[Main] An error ocurred.");
					e1.printStackTrace();
				}
				eew.close();
			}
			
		});
		gui.setComboBoxItemListener(new ItemListener(){
			@Override
			public void itemStateChanged(ItemEvent arg0) {
				gui.loadInTable(BLOManager.instance().getBLO(gui.getSelectedClass()).selectAll());
			}
		});
		gui.setGenerateReportsActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				ArrayList<Object> orders = DAO.instance().selectAll(Orderf.class);
				for (Object o : orders){
					createReport((Orderf)o);
				}
			}
		});
		gui.setCreateOrderActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (currentOrder == null){
					if (gui.getSelectedClass() != Customer.class){
						System.out.println("[Main] please select the customer table");
					}
					else if ((gui.getSelectedObject() == null) || (gui.getSelectedObject().getClass() != Customer.class)){
						System.out.println("[Main] please select a customer from the table");
					}
					else{
						currentOrder = new Orderf();
						currentOrder.idCustomer = ((Customer)gui.getSelectedObject()).idCustomer;
						currentOrder.idOrderf = OrderfBLO.instance().insert(currentOrder);
						System.out.println("[Main] selected customer is " + gui.getSelectedObject().toString());
						gui.setSelectedClass(Product.class);
					}
				}
				else{
					if (gui.getSelectedClass() != Product.class){
						System.out.println("[Main] please select the product table");
					}
					else if ((gui.getSelectedObject() == null) || (gui.getSelectedObject().getClass() != Product.class)){
						System.out.println("[Main] please select a product to add to the order");
					}
					else if (!gui.getSelectionEnded()){
						System.out.println("[Main] I try to insert in order " + currentOrder.idOrderf + " of customer " + currentOrder.idCustomer + " the product " + ((Product)gui.getSelectedObject()).idProduct);
						OrderfBLO.instance().addProduct(currentOrder, (Product)gui.getSelectedObject());
						gui.loadInTable(BLOManager.instance().getBLO(gui.getSelectedClass()).selectAll());
					}
					else{
						currentOrder = null;
						gui.setSelectioEnded(false);
					}
				}
			}
		});
		gui.setCreateNewActionListener(modifierAL);
		gui.setUpdateSelectedActionListener(modifierAL);
		gui.setDeleteSelectedListener(modifierAL);
		gui.init();
	}
	
	public static void createReport(Orderf order){
		//TODO create list of products
		Writer writer;
		try {
			writer = new BufferedWriter(new OutputStreamWriter( new FileOutputStream("Order" + order.idOrderf + ".txt"), "utf-8"));
			ArrayList<Product> products = DAO.instance().getProductsOfOrder(order);
			writer.write("Bill of order " + order.idOrderf + "\n");
			for (Product p : products){
				writer.write(p.name + " costs " + p.price + "\n");
			}
			writer.write("total: " + order.price + "\n");
			writer.write("observations " + order.observations);
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

	}


}
