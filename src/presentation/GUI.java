package presentation;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.ScrollPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemListener;
import java.lang.reflect.Field;
import java.sql.Date;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;

import models.Customer;
import models.Orderf;
import models.Product;

public class GUI {
	private final static int WIDTH = 600;
	private final static int HEIGHT = 400;
	private final static int DIV_RATIO = 4;
	private JFrame mainFrame; 
	private JPanel buttonsPanel;
	private JPanel dataPanel;
	private JScrollPane scrollPane;
	private JButton createOrderButton;
	private JComboBox<Class<?>>  tableSelector;
	private JButton generateReports;
	private JButton createNew; //we will rise!!1!
	private JButton	updateSelected;
	private Object selectedObject;
	private JButton deleteSelected;
	private JButton endSelection;
	private boolean selectionEnded = false;
	//TODO current state and selected value;
	
	public GUI(){
		endSelection = new JButton("End order");
		deleteSelected = new JButton("DELETE");
		createOrderButton = new JButton("Select");
		tableSelector = new JComboBox<Class<?>>();
		mainFrame = new JFrame("ironic title");
		dataPanel = new JPanel();
		buttonsPanel = new JPanel();
		scrollPane = new JScrollPane();
		generateReports = new JButton("Generate reports");
		createNew = new JButton("INSERT");
		updateSelected = new JButton("UPDATE");
		endSelection.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				selectionEnded = true;
			}
			
		});

	}
	
	public boolean getSelectionEnded(){
		return selectionEnded;
	}
	
	public void setSelectioEnded(boolean val){
		selectionEnded = val;
	}
	
	public boolean setSelectedClass(Class<?> cls){
		tableSelector.setSelectedItem(cls);
		return (true);
	}
	
	public Object getSelectedObject(){
		return (selectedObject);
	}
	
	public void setComboBoxItemListener(ItemListener itemListener){
		tableSelector.addItemListener(itemListener);
	}
	
	public void setDeleteSelectedListener(ActionListener al){
		deleteSelected.addActionListener(al);
	}
	
	public boolean loadInTable(ArrayList<Object> objects){
		if (objects == null) return (false);
		if (objects.size() == 0) {
			final JTable table = new JTable();
			table.setBounds(0, 0, dataPanel.getWidth(), dataPanel.getHeight());
			table.setPreferredScrollableViewportSize(new Dimension(500, 70));
	        table.setFillsViewportHeight(true);
	        scrollPane.setViewportView(table);
			System.out.println("[GUI] no data to be displayed");
			return (false);
		}
		String[] columnNames = new String[objects.get(0).getClass().getDeclaredFields().length];
		String[][] rowsData = new String[objects.size()][objects.get(0).getClass().getDeclaredFields().length];
		int i = 0;

		for (Field field : objects.get(0).getClass().getDeclaredFields()) {
			columnNames[i++] = field.getName();
			System.out.print(field.getName() + "\t");
		}
		System.out.println();
		i = 0;
		for (Object o : objects){
			int j = 0;
			for (Field field : objects.get(0).getClass().getDeclaredFields()) {
				try {
					if (field.getType() == Date.class){
						System.out.println("[GUI] TODO convert date type");//TODO
					}
					else{
						rowsData[i][j++] = field.get(o).toString();
					}
					
					System.out.print(field.get(o) + "\t\t");
				} catch (IllegalArgumentException | IllegalAccessException e) {
					e.printStackTrace();
				}
			}
			i++;
			System.out.println();
		}
		final JTable table = new JTable(rowsData, columnNames);
		table.setBounds(0, 0, dataPanel.getWidth(), dataPanel.getHeight());
		table.setPreferredScrollableViewportSize(new Dimension(500, 70));
        table.setFillsViewportHeight(true);
        //TODO add selected action listener
        table.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
			@Override
			public void valueChanged(ListSelectionEvent e) {
				selectedObject = objects.get(table.getSelectionModel().getMaxSelectionIndex());
			}
		});
        scrollPane.setViewportView(table);
		return (true);
	}
	
	
	public Class<?> getSelectedClass(){
		return (Class<?>) (tableSelector.getSelectedItem());
	}
	
	public void setCreateOrderActionListener(ActionListener a){
		createOrderButton.addActionListener(a);
	}
	
	public void setGenerateReportsActionListener(ActionListener a){
		generateReports.addActionListener(a);
	}
	
	public void setCreateNewActionListener(ActionListener a){
		createNew.addActionListener(a);
	}
	
	public void setUpdateSelectedActionListener(ActionListener a){
		updateSelected.addActionListener(a);
	}


	
	public void init(){
		mainFrame.setBounds(0, 0, WIDTH, HEIGHT);
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.setLayout(null);

		buttonsPanel.setBackground(Color.BLUE);
		buttonsPanel.setLayout(null);
		buttonsPanel.setBounds(0, 0, mainFrame.getWidth(), mainFrame.getHeight() / DIV_RATIO);
		dataPanel.setBackground(Color.black);
		dataPanel.setLayout(null);
		dataPanel.setBounds(0, buttonsPanel.getHeight(), mainFrame.getWidth(), mainFrame.getHeight() - buttonsPanel.getHeight());
		
		tableSelector.addItem(models.Product.class);
		tableSelector.addItem(models.Customer.class);
		tableSelector.addItem(models.Orderf.class);
		tableSelector.addItem(models.OrderfHasProduct.class);
		tableSelector.setBounds(120, 10, 200, 25);
		
		generateReports.setBounds(350, 10, 200, 25);
		createOrderButton.setBounds(10, 10, 100, 25);
		createNew.setBounds(10, 50, 100, 25);
		deleteSelected.setBounds(260, 50, 100, 25);
		scrollPane.setBounds(0, 0, dataPanel.getWidth(), dataPanel.getHeight());
		updateSelected.setBounds(130, 50, 100, 25);
		endSelection.setBounds(390, 50, 100, 25);	
		
		buttonsPanel.add(endSelection);
		buttonsPanel.add(deleteSelected);
		buttonsPanel.add(createOrderButton);
		buttonsPanel.add(tableSelector);
		buttonsPanel.add(generateReports);
		buttonsPanel.add(createNew);
		buttonsPanel.add(updateSelected);
		dataPanel.add(scrollPane);
		
		mainFrame.add(buttonsPanel);
		mainFrame.add(dataPanel);

		mainFrame.setVisible(true);
	}
	

}
