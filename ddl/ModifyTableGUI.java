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
import business.Table;
import useful.ResponseData;

public class ModifyTableGUI extends CreateTableGUI {
	
	private JComboBox<String> comboChoiceTable;
	
	private boolean bug = false;
	
	private Table tableSource;
	

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
//		this.tableNameField.setVisible(false);
		
		
		initComboBoxChoice();
		
		this.createTableButton.setText("Modifier");
	}

	
	/**
	 * Initialise la nouvelle comboBox
	 * Possédant la liste des tables
	 */
	private void initComboBoxChoice() 
	{
		/*
		 * TODO : faire deux méthodes : une pour créer l'objet et l'autre pour gérer son contenu. 
		 */
		Rectangle coords = this.tableNameField.getBounds(); //Ok !
		ResponseData<String> tables = this.control.getTables();
		//TODO : this.talk(tables) pour afficher un message de succès ou d'échec.
		Vector<String> vectTables = new Vector<String>(tables.getCollection());//TODO useless
		DefaultComboBoxModel<String> boxModel = new DefaultComboBoxModel<String>(vectTables); //TODO : useless
		
		if (this.comboChoiceTable == null)
			this.comboChoiceTable = new JComboBox<String>();

		
		this.comboChoiceTable.setModel(boxModel); 
		/*
		 * TODO : peut être instancié en une seule ligne :
		 * this.comboChoiceTable = new JComboBox(tables.getCollection().toArray());
		 */
		this.comboChoiceTable.setBounds(new Rectangle(coords.x,coords.y,coords.width,coords.height));
		this.add(this.comboChoiceTable);

		coords.x+=coords.width;
		this.tableNameField.setBounds(coords);
		
		this.comboChoiceTable.addActionListener(this); //TODO : ItemListener pour les listes déroulantes

	}


	/**
	 * Cette méthode va initialiser toutes les valeurs TODO : on sait que c'est une méthode
	 * selon la table entrée en Paramètre dans le comboBox //TODO : Paramètre n'est pas un Dieu :p
	 */
	private void setValues() {
		this.setViewModify(
				this.control.getAttributes(this.comboChoiceTable.getSelectedItem().toString()), 
				this.comboChoiceTable.getSelectedItem().toString());
		
	}
	
	
	public void setViewModify(List<Attribute> attributes, String tableName) {	
		this.resetView();
		for (Attribute a : attributes){
			this.models.addAttribute(a);
		}
	}
	
	
	@Override
	protected void createTableButtonAction(){
		this.control.modifyTable(this.getTable(),this.tableSource);
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
			/*
			 * TODO : tu veux rester sur la même ligne même après mise à jour?
			 * L'index peut changer si on ajoute un item à la liste.
			 * Il faut chercher par correspondance entre chaînes de caractères.
			 */
			this.comboChoiceTable.setSelectedIndex(index);
		}
		catch(IllegalArgumentException e){}
	}
	

	@Override
	public void actionPerformed(ActionEvent e){
		super.actionPerformed(e);
		//TODO : Object o = e.getSource();
		if (e.getSource() == this.comboChoiceTable 
				&& e.getModifiers() == 16 ) //TODO : useless si ItemListener
		{
			
			if (e.getSource() == this.comboChoiceTable){
				String tableSelected = this.comboChoiceTable.getSelectedItem().toString();
				
				bug = !bug; //TODO : à expliquer parce que là ça veut juste dire "une fois sur deux"
				if (!bug){
					this.setValues();
					this.tableNameField.setText(this.comboChoiceTable.getSelectedItem().toString());
					this.tableSource = this.getTable();
				}
			}
			//TODO : la résolution de se problème s'effectur en surchargant la classe JComboBox à priori...
			//TODO : ce serait étonnant ! 
			//cette solution sera utilisé dans un premier temps pour palier à cette erreur
			//TODO : quel problème ?
		}
	}


}
