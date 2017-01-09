package ddl;

import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.WindowEvent;
import java.util.List;
import java.util.Vector;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;

import business.Attribute;
import useful.ResponseData;

public class ModifyTableGUI extends CreateTableGUI {
	
	private JComboBox<String> comboChoiceTable;
	
	private boolean bug = false;
	
	//private int numberOfItemComboChoiceTableTemp = 0;
	

	public ModifyTableGUI(DDLController control) {
		super(control);
		this.setTitle("Modifier une table");
		this.changeComponents();
		
		
	}

	/**
	 * Cette méthode va changer quelques composants de
	 * la classe hérité pour créer la vue
	 * de modifications
	 */
	private void changeComponents() {
		this.tableNameField.setVisible(false);
		
		
		initComboBoxChoice();
		
		this.createTableButton.setText("Modifier");
	}

	/**
	 * Initialise la nouvelle comboBox
	 * Possédant la liste des tables
	 */
	private void initComboBoxChoice() {
		
		Rectangle coords = this.tableNameField.getBounds();
		ResponseData<String> tables = this.control.getTables();
		Vector<String> vectTables = new Vector<String>(tables.getCollection());
		DefaultComboBoxModel<String> boxModel = new DefaultComboBoxModel<String>(vectTables);
		
		if (this.comboChoiceTable == null)
			this.comboChoiceTable = new JComboBox<String>();

		
		this.comboChoiceTable.setModel(boxModel);
		this.comboChoiceTable.setBounds(coords);
		this.add(this.comboChoiceTable);
		this.comboChoiceTable.addActionListener(this);

	}


	


	/**
	 * Cette méthode va initialiser toutes les valeurs 
	 * selon la table entrée en Paramètre dans le comboBox
	 */
	private void setValues() {
		//TODO
//		this.resetView();
		
		this.setViewModify(
				this.control.getAttributes(this.comboChoiceTable.getSelectedItem().toString()), 
				this.comboChoiceTable.getSelectedItem().toString());
		
	}
	
	
	public void setViewModify(List<Attribute> attributes, String tableName) {	
		this.resetView();
		for (Attribute a : attributes){
//			System.out.println(a.toString());
			this.models.addAttribute(a);
		}
		
	}
	
	@Override
	protected void createTableButtonAction(){
		System.out.println("createTableButtonAction");
		this.control.modifyTable(this.getTable());
	}
	
	@Override
	public void windowActivated(WindowEvent e)
	{
		acualiseComboBox();
	}

	private void acualiseComboBox() {
		int index = this.comboChoiceTable.getSelectedIndex();
		this.initComboBoxChoice();
		try{
			this.comboChoiceTable.setSelectedIndex(index);
		}
		catch(IllegalArgumentException e){}
	}
	

	@Override
	public void actionPerformed(ActionEvent e){
		super.actionPerformed(e);
		
		if (e.getSource() == this.comboChoiceTable && e.getModifiers() == 16 ){
			
			if (e.getSource() == this.comboChoiceTable){
				String tableSelected = this.comboChoiceTable.getSelectedItem().toString();
				bug = !bug;
				if (!bug){
					
					this.setValues();
					this.tableNameField.setText(this.comboChoiceTable.getSelectedItem().toString());
//					System.out.println(this.tableNameField.getText());
				}
			}

				
			//TODO la résolution de se problème s'effectur en surchargant la classe JComboBox à priori...
			//cette solution sera utilisé dans un premier temps pour palier à cette erreur
		}
	}


}
