package gui.ddl;

import gui.abstrct.AbstractBasicGUI;
import useful.Response;
import useful.ResponseData;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.table.DefaultTableModel;
import controller.DDLController;


@SuppressWarnings("serial")
public class ConstraintsGUI extends AbstractBasicGUI{

	private static String[] constraintsTypes = new String[]{"UNIQUE", "FOREIGN KEY"};

	/** Controleur lié à l'IHM. */
	private DDLController control;

	/** Liste déroulantes contenant les types de contraintes.*/
	private JComboBox<String> typeConstraintComboBox;

	/** Liste déroulantes contenant les tables disponibles.*/
	private JComboBox<String> tableNameComboBox;

	/** Liste déroulantes contenant les tables disponibles.*/
	private JComboBox<String> fkTableNameComboBox;

	/** Tableau contenant le nom attributs sources. */
	private JTable namesAttributeSourceTable;

	/** Tableau contenant lle nom attributs référencés. */
	private JTable namesAttributeReferenceTable;

	/** Tableau contenant les contraintes de la table source. */
	private JTable constraintsTable;

	/** Bouton pour envoyer la requête de création d'une contrainte.*/
	private JButton addButton;

	/** Bouton pour envoyer la requête de suppression d'une contrainte.*/
	private JButton deleteButton;

	/** Model pour la Table des attributs Source.*/
	private DefaultTableModel modelSource;

	/**  Model pour la Table des attributs Référencés.*/
	private DefaultTableModel modelFk;

	/** Model pour la Table des contraintes.*/
	private DefaultTableModel constraintsListModel;

	/** ScrollPane pour la Table des attributs Référencés*/
	private JScrollPane fkAttributesNameScrollPane;

	/** Label pour les tables référencées.*/
	private JLabel fkTableLabel;

	/**
	 * Constructeur commun.
	 * 
	 * @param control : null interdit.
	 */
	public ConstraintsGUI(DDLController ddlController) {
		super("Contraintes",null, 600, 600, 25);
		this.control = ddlController;
		this.setProperties(WindowConstants.DISPOSE_ON_CLOSE);
		this.handleComponents();
		this.enableFkComponents(false);
		this.fillComponents();
		this.addListener();
	}


	@Override
	public void actionPerformed(ActionEvent e) {
		Object o = e.getSource();
		if (o == this.typeConstraintComboBox) {
			this.selectTypeConstraintComboBoxAction();
		}
		if (o == this.tableNameComboBox) {
			this.fillTableAttribute(this.tableNameComboBox,this.modelSource);
			this.fillTableConstraints();
		}
		if (o == this.fkTableNameComboBox) {
			this.fillTableAttribute(this.fkTableNameComboBox,this.modelFk);
		}
		if (o == this.addButton) {
			this.addConstraintButtonAction();
		}
		if (o == this.deleteButton) {
			this.dropConstraintButtonAction();
		}
	}


	private void dropConstraintButtonAction() {
		if(this.constraintsTable.getSelectedRowCount() != 0){
			String constraint = this.constraintsListModel.getValueAt(this.constraintsTable.getSelectedRow(),0).toString();
			String tableSourceName = this.constraintsListModel.getValueAt(this.constraintsTable.getSelectedRow(),2).toString();
			String attribute = this.constraintsListModel.getValueAt(this.constraintsTable.getSelectedRow(),3).toString();
			Response response = this.control.removeConstraint(tableSourceName,attribute,constraint);
			this.talk(response);
			if(response.hasSuccess()){
				this.fillTableConstraints();
			}
		}else{
			this.talk(new Response(false,"Il faut selectionner la contrainte à supprimer."));
		}
	}



	private void addConstraintButtonAction() {
		if("FOREIGN KEY".equals(this.typeConstraintComboBox.getSelectedItem().toString())){
			if(isValidSelectedAttributesCount()){
				String tableSourceName = this.tableNameComboBox.getSelectedItem().toString();
				System.out.println(tableSourceName);
				int selected[] = this.namesAttributeSourceTable.getSelectedRows();
				String attributesSourcesNames[] = new String[selected.length];
				int c = 0;
				for(int i : selected){
					String attribut = (String) this.modelSource.getValueAt(i, 0);
					attributesSourcesNames[c] = attribut;
					System.out.println(attribut);
					c++;
				}

				String tableDestinationName = this.fkTableNameComboBox.getSelectedItem().toString();
				System.out.println(tableDestinationName);
				selected = this.namesAttributeReferenceTable.getSelectedColumns();
				String attributesDestinationsNames[] = new String[selected.length];
				c = 0;
				for(int i : selected){
					String attribut = (String) this.modelFk.getValueAt(i, 0);
					attributesDestinationsNames[c] = attribut;
					System.out.println(attribut);
					c++;
				}
				Response response = this.control.addForeignKey(tableSourceName,attributesSourcesNames,tableDestinationName,attributesDestinationsNames);
				this.talk(response);
				if(response.hasSuccess()){
					this.fillTableConstraints();
				}
			}
		}
		if("UNIQUE".equals(this.typeConstraintComboBox.getSelectedItem().toString())){
			if(this.namesAttributeSourceTable.getSelectedRowCount() != 0){
				String tableSourceName = this.tableNameComboBox.getSelectedItem().toString();
				System.out.println(tableSourceName);
				int selected[] = this.namesAttributeSourceTable.getSelectedRows();
				String attributesSourcesNames[] = new String[selected.length];
				int c = 0;
				for(int i : selected){
					String attribut = (String) this.modelSource.getValueAt(i, 0);
					attributesSourcesNames[c] = attribut;
					System.out.println(attribut);
					c++;
				}
				Response response = this.control.addUnique(tableSourceName,attributesSourcesNames);
				this.talk(response);
				if(response.hasSuccess()){
					this.fillTableConstraints();
				}
			}else{
				this.talk(new Response(false,"Il faut selectionner au moins 1 attribut pour créer une contrainte."));
			}
		}
	}



	@Override
	public boolean isComplete() {
		// TODO Auto-generated method stub
		return false;
	}


	/**
	 * Instancie, positionne et dimensionne les différents composants de l'IHM.
	 */
	private void handleComponents(){
		String [] header={"attributs","type"};
		String [] header2={"Nom de la contrainte","Type","Table cible","Attributs","Table référencée","Attributs"};
		this.modelSource = new DefaultTableModel(header,0);
		this.modelFk = new DefaultTableModel(header,0);
		this.constraintsListModel = new DefaultTableModel(header2,0);

		this.bindAndAdd(new JLabel("Type de contrainte"),3,true);

		this.typeConstraintComboBox = new JComboBox<String>(constraintsTypes);
		this.typeConstraintComboBox.addActionListener(this);
		this.bindAndAdd(this.typeConstraintComboBox,3,false);

		this.bindAndAdd(new JLabel("Table : "),8,true);

		this.tableNameComboBox = new JComboBox<String>();

		this.bindAndAdd(this.tableNameComboBox,6,true);

		this.increaseLeft(120);

		this.fkTableLabel = new JLabel("Table référencée : ");
		this.bindAndAdd(fkTableLabel ,5,true);

		this.fkTableNameComboBox = new JComboBox<String>();
		this.bindAndAdd(this.fkTableNameComboBox,6,false);

		this.namesAttributeSourceTable = new JTable(modelSource){
			public boolean isCellEditable(int row, int col) {
				return false;
			}
		};
		JScrollPane attributesNameScrollPane = new JScrollPane(namesAttributeSourceTable, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		this.bindAndAdd(attributesNameScrollPane,3,true);
		attributesNameScrollPane.setSize(new Dimension(220,100));
		attributesNameScrollPane.setVisible(true);

		this.increaseLeft(100);

		this.namesAttributeReferenceTable = new JTable(modelFk){
			public boolean isCellEditable(int row, int col) {
				return false;
			}
		};
		this.namesAttributeReferenceTable.getTableHeader().setReorderingAllowed(false);
		this.fkAttributesNameScrollPane =new JScrollPane(namesAttributeReferenceTable, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		this.bindAndAdd(fkAttributesNameScrollPane,3,false);
		fkAttributesNameScrollPane.setSize(new Dimension(220,100));
		fkAttributesNameScrollPane.setVisible(true);

		this.increaseTop(90);

		this.addButton = new JButton("Ajouter la contrainte");
		this.bindAndAdd(this.addButton);

		this.increaseTop(15);

		this.bindAndAdd(new JSeparator(SwingConstants.HORIZONTAL));

		this.bindAndAdd(new JLabel("Liste des contraintes sur la table source : "));

		this.constraintsTable = new JTable(this.constraintsListModel){
			public boolean isCellEditable(int row, int col) {
				return false;
			}
		};
		JScrollPane constraintsListScrollPane = new JScrollPane(constraintsTable, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

		constraintsTable.getColumnModel().getColumn(0).setPreferredWidth(120);
		constraintsTable.getColumnModel().getColumn(4).setPreferredWidth(100);
		this.constraintsTable.setFillsViewportHeight(true);
		this.bindAndAdd(constraintsListScrollPane,150);
		constraintsListScrollPane.setVisible(true);

		this.deleteButton = new JButton("Supprimer la contrainte");
		this.bindAndAdd(this.deleteButton);
	}

	/**
	 * Ajoute des ActionListener aux ComboBox des Tables
	 */
	private void addListener() {
		this.tableNameComboBox.addActionListener(this);
		this.fkTableNameComboBox.addActionListener(this);
		this.addButton.addActionListener(this);
		this.deleteButton.addActionListener(this);
	}

	/**
	 * Remplit les listes déroulantes avec le nom des 
	 * tables de données disponibles pour l'utilisateur
	 * Remplit les tables aves le nom et le type des attributs
	 * Affiche le nombre de tables récupérées.
	 */
	private void fillComponents() {
		this.fillComboBox();
		this.fillTableAttribute(this.tableNameComboBox,this.modelSource);
		this.fillTableAttribute(this.fkTableNameComboBox,this.modelFk);
		this.fillTableConstraints();
	}

	/**
	 * Remplit la liste déroulante avec le nom des 
	 * tables de données disponibles pour l'utilisateur.
	 * Affiche le nombre de tables récupérées.
	 */
	private void fillComboBox()
	{
		String msg;
		ResponseData<String> 
		response = this.control.getTables();
		msg = response.getMessage();
		this.tableNameComboBox.removeAllItems();
		this.fkTableNameComboBox.removeAllItems();
		if (response.hasSuccess()) {
			msg += " : " + response.getCollection().size();
			for (String s : response.getCollection()) {
				this.tableNameComboBox.addItem(s);
				this.fkTableNameComboBox.addItem(s);
			}
		}
		this.talk(msg);
	}

	/**
	 * Remplit les tables aves le nom et le type des attributs
	 */
	private void fillTableAttribute(JComboBox<String> comboBox, DefaultTableModel model)
	{
		ResponseData<String[]> 
		response = this.control.getAttributes(comboBox.getSelectedItem().toString());
		while(model.getRowCount() != 0){
			model.removeRow(0);
		}
		if (response.hasSuccess()) {
			for (String[] s : response.getCollection()) {
				Vector<String> v = new Vector<String>();
				v.addElement(s[0]);
				v.addElement(s[1]+" ("+s[2]+")");
				model.addRow(v);
			}
		}
	}

	private void fillTableConstraints()
	{
		ResponseData<String[]> 
		response = this.control.getPrimaryFromForeign(this.tableNameComboBox.getSelectedItem().toString());
		while(this.constraintsListModel.getRowCount() != 0){
			constraintsListModel.removeRow(0);
		}
		if (response.hasSuccess()) {
			for (String[] s : response.getCollection()) {
				Vector<String> v = new Vector<String>();
				v.addElement(s[5]);
				v.addElement("FOREIGN KEY");
				v.addElement(s[3]);
				v.addElement(s[4]);
				v.addElement(s[0]);
				v.addElement(s[1]);

				this.constraintsListModel.addRow(v);
			}
		}
		ResponseData<String[]> rep = this.control.getUniqueAttribute(this.tableNameComboBox.getSelectedItem().toString());
		if (rep.hasSuccess()) {
			int i=0;

			// [0] : nom index
			// [1] : nom attribut
			for (String[] s : rep.getCollection()){

				if (i != 0){
					Vector<String> v = new Vector<String>();
					v.addElement(s[0]);
					v.addElement("UNIQUE");
					v.addElement(this.tableNameComboBox.getSelectedItem().toString());
					v.addElement(s[1]);
					this.constraintsListModel.addRow(v);
				}
				i++;
			}
		}
	}

	/**
	 * Détermine ce qu'il se passe lors de la sélection d'une FOREIGN KEY
	 * dans la ComboBox du type de la contrainte.
	 */
	private void selectTypeConstraintComboBoxAction() {
		Object selected = this.typeConstraintComboBox.getSelectedItem();
		enableFkComponents("FOREIGN KEY".equals(selected.toString()));

	}


	private boolean isValidSelectedAttributesCount(){
		boolean b = false;
		int nbSelectedRowsSource = this.namesAttributeSourceTable.getSelectedRows().length;
		int nbSelectedRowsDest = this.namesAttributeReferenceTable.getSelectedRows().length;
		if(nbSelectedRowsSource == 0 && nbSelectedRowsDest == 0){
			this.talk(new Response(false,"Aucun attributs selectionnés"));
			b =  false;
		}else if (nbSelectedRowsSource != nbSelectedRowsDest){
			this.talk(new Response(false,"Le nombre d'attributs sélectionnés doit être le même pour les deux tables."));
			b = false;
		}else{
			b = true;
		}
		return b;
	}

	/**
	 * Definit si l'on affiche la partie Foreign Key de la vue.
	 * 
	 * @param state :  vrai si et seulement si la partie Foreign Key de la vue doit etre visible,
	 * faux si elle doit être invisible.
	 */
	private void enableFkComponents(boolean b) {
		this.fkTableLabel.setVisible(b);
		this.fkTableNameComboBox.setVisible(b);
		this.fkAttributesNameScrollPane.setVisible(b);
		this.namesAttributeReferenceTable.setVisible(b);
	}
}