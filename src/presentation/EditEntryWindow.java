package presentation;

import java.awt.event.ActionListener;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class EditEntryWindow {
	private final static int WIDTH = 600;
	private final static int HEIGHT = 150;
	private JFrame mainFrame;
	private Action action;
	private ArrayList<JLabel> labels;
	private ArrayList<JTextField> textFields;
	private Class<?> objectType;
	private JButton done;
	private int id;
	private Object editedObject;
	
	public EditEntryWindow(){
		mainFrame = new JFrame();
		this.action = action;
		labels = new ArrayList<JLabel>();
		textFields = new ArrayList<JTextField>();
		done = new JButton("done");
	}
	
	public void setDoneActionListener(ActionListener al){
		done.addActionListener(al);
	}
	
	public void init(Class<?> objectType, Action action){
		this.objectType = objectType;
		this.action = action;
		mainFrame.getContentPane().removeAll();
		labels.clear();
		textFields.clear();
		mainFrame.setBounds(10, 10, WIDTH, HEIGHT);
		mainFrame.setTitle(action.toString() + " " + objectType.getSimpleName());
		mainFrame.setLayout(null);
		int i = 0;
		final int elemWidth = WIDTH / (objectType.getDeclaredFields().length - 1);
		for (Field field : objectType.getDeclaredFields()) {
			if (!field.getName().equals("id" + objectType.getSimpleName())){
				try {
					JLabel label = new JLabel(field.getName());
					label.setBounds(elemWidth * i, HEIGHT / 16, elemWidth, HEIGHT / 4);
					labels.add(label);
					JTextField textField = new JTextField();
					textField.setBounds(elemWidth * i, HEIGHT / 4, elemWidth, HEIGHT / 4);
					textFields.add(textField);
					i++;
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				}
			}
		}
		
		done.setBounds(WIDTH / 4, HEIGHT / 2, WIDTH / 2, HEIGHT / 4 - 10);
		
		for (JLabel label : labels){
			mainFrame.add(label);
		}
		for (JTextField textField : textFields){
			mainFrame.add(textField);
		}
		mainFrame.add(done);
		
		mainFrame.repaint();
		mainFrame.revalidate();
		mainFrame.setVisible(true);
	}
	
	public Class<?> getObjectType() {
		return objectType;
	}

	public void setObjectType(Class<?> objectType) {
		this.objectType = objectType;
	}

	public void init(Object object, Action action){
		init(object.getClass(), action);
		for (Field field : object.getClass().getDeclaredFields()) {
			if (field.getName().equals("id" + object.getClass().getSimpleName())){
				try {
					id = field.getInt(object);
				} catch (IllegalArgumentException | IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		}
		fill(object);
		if (action == Action.DELETE){
			editedObject = object;
			done.doClick();
		}
	}
	
	public Action getAction() {
		return action;
	}

	public void setAction(Action action) {
		this.action = action;
	}

	public void fill(Object object){
		int i = 0;
		for (Field field : object.getClass().getDeclaredFields()) {
			if (!field.getName().equals("id" + object.getClass().getSimpleName())){
				try {
					textFields.get(i).setText(field.get(object).toString());
					i++;
				} catch (IllegalArgumentException | IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	@SuppressWarnings("deprecation")
	public Object getEditedObject(){
		int i = 0;
		if (action == Action.DELETE){
			return editedObject;
		}
		try {
			Object o = Class.forName(objectType.getName()).getConstructor().newInstance();
			for (Field field : objectType.getDeclaredFields()) {
				if (!field.getName().equals("id" + o.getClass().getSimpleName())){
					if (field.getType() == String.class){
						field.set(o, textFields.get(i).getText());
					}
					else{//assume int
						field.set(o, Integer.parseInt(textFields.get(i).getText()));
					}
					i++;
				}
				else{
					field.set(o, id);
				}
			}
			return (o);
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException | ClassNotFoundException  e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return (null);
		}
		
	}
	
	public void close(){
		mainFrame.setVisible(false);
	}

}
