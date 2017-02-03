package ddl;

import gui.BasicGUI;
import gui.ListeningGUI;

import java.awt.BorderLayout;
import java.awt.event.*;
import java.util.List;
import java.awt.Font;

import javax.swing.*;

import business.Attribute;
import business.Table;
import useful.FieldsKeyAdapter;
import useful.ResponseData;
import useful.MaxLengthTextDocument;

/**
 * @author Adrian
 *
 */
@SuppressWarnings("serial")
/**
 * IHM pour créer des tables dans la base de données.
 * 
 * @author MAURY Adrian
 */
public class CreateTableGUI
extends CreateModifyProperties
implements ActionListener, ItemListener
{
	// ==========================VARIABLES========================

	

	

	
	/**
	 * Constructeur commun pour l'ihm de création de table.
	 * @param cm : objet ConnectionManager obtenu lors de la connexion.
	 */
	public CreateTableGUI(DDLController control)
	{
		super();
		this.control = control;
		this.handleComponents();
		this.setProperties(WindowConstants.DISPOSE_ON_CLOSE);
		this.initFields();
	}

	/**
	 * Instancie, positionne et dimensionne les différents composants de $this.
	 */
	private void handleComponents()
	{
		this.tableLabel = new JLabel("Table :");
		this.tableLabel.setFont(new Font(FONT, Font.BOLD, 18));
		this.bindAndAdd(this.tableLabel);

		this.tableNameLabel= new JLabel("Nom de la table :");
		this.bindAndAdd(this.tableNameLabel,7,true);

		this.tableNameField = new JTextField();
		this.bindAndAdd(this.tableNameField,10,false);

		this.increaseTop(10);
		this.attributeLabel = new JLabel("Attribut :");
		this.attributeLabel.setFont(new Font(FONT, Font.BOLD, 18));
		this.bindAndAdd(this.attributeLabel);

		this.attributeNameField = new JTextField();
		this.bindAndAdd(this.attributeNameField,10,true);
		this.types = this.control.getAttributeTypes();
		this.attributeTypeComboBox = new JComboBox<String>((String[])types);
		this.attributeTypeComboBox.addActionListener(this);
		this.bindAndAdd(this.attributeTypeComboBox,10,true);

		this.attributeSizeField = new JTextField();
		this.bindAndAdd(this.attributeSizeField,21,true);

		this.notNullCheckBox = new JCheckBox("NOT NULL");
		this.bindAndAdd(this.notNullCheckBox,9,true);

		this.uniqueCheckBox = new JCheckBox("UNIQUE");
		this.bindAndAdd(this.uniqueCheckBox,11,true);

		this.primaryKeyCheckBox = new JCheckBox("PRIMARY KEY");
		this.primaryKeyCheckBox.addItemListener(this);
		this.bindAndAdd(this.primaryKeyCheckBox,7,true);

		this.foreignKeyCheckBox = new JCheckBox("FOREIGN KEY");
		this.foreignKeyCheckBox.addItemListener(this);
		this.bindAndAdd(this.foreignKeyCheckBox,7,true);

		this.foreignKeyTableComboBoxModel = new DefaultComboBoxModel<String>();
		this.foreignKeyTableComboBoxModel.addElement("Nom Table");
		this.fkTableNameComboBox = new JComboBox<String>(foreignKeyTableComboBoxModel);
		this.fkTableNameComboBox.addActionListener(this);
		this.fkTableNameComboBox.setEnabled(false);
		this.bindAndAdd(this.fkTableNameComboBox,10,true);

		this.foreignKeyAttributeComboBoxModel = new DefaultComboBoxModel<String>();
		this.foreignKeyAttributeComboBoxModel.addElement("Nom Attribut");
		this.fkAtrributeNameComboBox = new JComboBox<String>s(foreignKeyAttributeComboBoxModel);
		this.fkAtrributeNameComboBox.addActionListener(this);
		this.fkAtrributeNameComboBox.setEnabled(false);
		this.bindAndAdd(this.fkAtrributeNameComboBox,10,false);
		this.increaseTop(10);

		this.attributeButton = new JButton("Ajouter l'attribut") ;
		this.attributeButton.addActionListener(this);
		this.bindAndAdd(this.attributeButton,6,true);

		this.confirmUpdateAttributeButton = new JButton("Modifier l'attribut") ;
		this.confirmUpdateAttributeButton.addActionListener(this);
		this.bindAndAdd(this.confirmUpdateAttributeButton,6,true);

		this.cancelUpdateAttributeButton = new JButton("Annuler les modifications") ;
		this.cancelUpdateAttributeButton.addActionListener(this);
		this.bindAndAdd(this.cancelUpdateAttributeButton,4,false);
		this.setVisibleEnabledUpdateButtons(false);
		this.increaseTop(10);

		this.panelAttributes = new JPanel(new BorderLayout());
		this.models = new AttributesAbstractTableModel();
		this.table = new JTable(this.models){
			public boolean isCellEditable(int row, int col) {
				return false;
			}
		};
		this.table.getSelectionModel().addListSelectionListener(new ControlTableResult(this));
		this.scrollPane = new JScrollPane(this.table);
		this.table.setFillsViewportHeight(true);
		this.table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.scrollPane.setVisible(true);
		this.panelAttributes.add(this.scrollPane);
		this.bindAndAdd(this.panelAttributes,200);
		this.increaseTop(10);

		this.deleteAttributeButton = new JButton("Supprimer attribut") ;
		this.deleteAttributeButton.addActionListener(this);
		this.bindAndAdd(this.deleteAttributeButton,5,true);

		this.updateAttributeButton = new JButton("Mofifier attribut") ;
		this.updateAttributeButton.addActionListener(this);
		this.bindAndAdd(this.updateAttributeButton,6,true);
		this.increaseLeft(60);

		this.downPositionAttributeButton = new JButton("DOWN") ;
		this.downPositionAttributeButton.addActionListener(this);
		this.bindAndAdd(this.downPositionAttributeButton,10,true);

		this.upPositionAttributeButton = new JButton("UP") ;
		this.upPositionAttributeButton.addActionListener(this);
		this.bindAndAdd(this.upPositionAttributeButton,10,true);
		this.increaseLeft(180);

		this.resetButton = new JButton("Reset") ;
		this.resetButton.addActionListener(this);
		this.bindAndAdd(this.resetButton,10,false);
		this.increaseTop(10);

		this.createTableButton = new JButton("Créer la Table") ;
		this.createTableButton.addActionListener(this);
		this.bindAndAdd(this.createTableButton,6,false);

		this.setEnableButtonUpdateDeleteUpDown(false);

	}

	/**
	 * Gère les caractéristiques des Fields.
	 */
	private void initFields()
	{
		this.intKey = new FieldsKeyAdapter("int");
		this.stringKey = new FieldsKeyAdapter("String");
		this.attributeSizeField.addKeyListener(this.intKey);
		this.tableNameField.addKeyListener(this.stringKey);
		this.attributeNameField.addKeyListener(this.stringKey);	
		this.tableNameField.setDocument(fieldInputSize(30));
		this.attributeNameField.setDocument(fieldInputSize(29));
		this.attributeSizeField.setDocument(fieldInputSize(3));
		this.attributeNameField.setText("nomAttribut");
		this.attributeSizeField.setText("Taille");
	}

	/**
	 * Réinitialise les champs de saisie des attributs.
	 */
	public void clearAttribute()
	{
		this.attributeNameField.setText("");
		this.attributeSizeField.setText("");
		this.notNullCheckBox.setSelected(false);
		this.uniqueCheckBox.setSelected(false);
		this.foreignKeyCheckBox.setSelected(false);
		this.primaryKeyCheckBox.setSelected(false);
		this.attributeTypeComboBox.setSelectedIndex(0);
	}

	/**
	 * Réinitialise tous les champs de la vue.
	 */
	public void resetView()
	{
		this.setAttributesValues("nomAttribut", "VARCHAR2", "Taille", false, false, false, false, "nomTable", "nomAttribut");
		this.tableNameField.setText("");
		this.talk("");
		this.setEnableButtonUpdateDeleteUpDown(false);;
		this.models.removeAll();
	}

	/**
	 * Affiche les arguments passés en paramètres dans la table.
	 * 
	 * @param attributes
	 * @param string
	 */
	public void setView(List<Attribute> attributes, String tableName) {	
		this.resetView();
		for (Attribute a : attributes){
//			System.out.println(a.toString());
			this.addAttributeToTable(a);
		}
		this.setTableName(tableName);
	}

	/**
	 * Modifie l'état des boutons de modifications du tableau 
	 * en fonction du boolean .
	 * 
	 * @param b : un boolean
	 */
	public void setEnableButtonUpdateDeleteUpDown(boolean b){
		this.deleteAttributeButton.setEnabled(b);
		this.updateAttributeButton.setEnabled(b);
		this.upPositionAttributeButton.setEnabled(b);
		this.downPositionAttributeButton.setEnabled(b);
	}

	/**
	 * Ajoute l'attribut passé en paramètre à la table 
	 * contenant les attributs.
	 * 
	 * @param attribute : un objet Attribute
	 */
	protected void addAttributeToTable(Attribute attribute){
		if (this.isValidateAttribute(attribute)){
			this.models.addAttribute(attribute);
			this.talk(succesAttribute +"Attribut ajouté.");
			this.clearAttribute();
		}
		else{
			System.out.println("Attribut non valide");
		}
	}
	

	/**
	 * Document permettant de limiter le nombre de caractères maximum saisis.
	 * Retourne un Document pouvant être passé en paramètre 
	 * de la fonction Component.setDocument(Document).
	 * 
	 * @param size : un entier int
	 * @return MaxLengthTextDocument
	 */
	private MaxLengthTextDocument fieldInputSize(int size)
	{
		MaxLengthTextDocument maxLengthNameAtt = new MaxLengthTextDocument();
		maxLengthNameAtt.setMaxChars(size);
		return maxLengthNameAtt;
	}

	/**
	 *  Retourne vrai si tous les champs de l'attributs sont 
	 *  renseignés,
	 *  faux sinon.
	 * @return boolean
	 */
	private boolean isCompleteAttribute()
	{
		if(("").equals(this.attributeNameField.getText()) 
				|| ("").equals(this.attributeSizeField.getText()) 
				|| (this.foreignKeyCheckBox.isSelected() && (this.foreignKeyAttributeComboBoxModel.getSize()==0 
				|| this.foreignKeyTableComboBoxModel.getSize()==0)))
		{
			this.talk(errorAttribute + "Tous les champs Attributs doivent être renseignés.");
			return false;
		}
		else
		{
			return true;
		}
	}

	/**
	 * Retourne vrai si la table est complète, c'est à dire si elle
	 * contient des attributs et un nom de table,
	 * faux sinon.
	 * 
	 * @return boolean
	 */
	private boolean isCompleteTable()
	{
		if(this.models.getRowCount()==0){
			this.talk(errorAttribute+"Il n'y a pas d'Attribut");
			return false;
		} else if ("".equals(this.tableNameField.getText())) {
			this.talk(errorAttribute+"Il manque le nom de la Table");
			return false;
		} else {
			return true;
		}
	}

	/**
	 * Retourne vrai Si l'attribut a est un attribut Valide. C'est a dire 
	 * Si les champs attributs sont complets,
	 * Si la taille renseignées est correcte en fonction du type,
	 * Si il n'y a pas de doublon au niveau du nom de l'attribut.
	 * @param a
	 * @return un boolean
	 */
	private boolean isValidateAttribute(Attribute a){
			if(!(a.checkSizeAttributes()>=0)){
				this.talk(this.errorAttribute +a.attributeSizeError(a.checkSizeAttributes()));
				return false;
			}else if(this.models.isDuplicateAttributeName(a) && !(this.updateState)){
				this.talk(errorAttribute +"Un attribut existant a déja le même nom.");
				return false;
		}
		return true;
	}

	/**
	 * Modifie le nom de la Table dans le champs de saisie.
	 * 
	 * @param tableName : une chaine de caractère
	 */
	private void setTableName(String tableName) {
		this.tableNameField.setText(tableName);

	}

	/**
	 * Si bool est true Désactives tous les champs de saisies, les boutons et le tableau 
	 * qui ne sont pas utilies pour la modification d'un attribut,
	 * sinon les actives.
	 * 
	 * @param bool
	 */
	private void setDisableAllExceptAttribute(boolean bool){
		this.tableNameField.setEnabled(bool);
		this.attributeButton.setEnabled(bool);
		this.panelAttributes.setEnabled(bool);
		this.setEnableButtonUpdateDeleteUpDown(bool);
		this.upPositionAttributeButton.setEnabled(bool);
		this.downPositionAttributeButton.setEnabled(bool);
		this.resetButton.setEnabled(bool);
		this.createTableButton.setEnabled(bool);
	}
	/**
	 * Definit l'état des checkBox Unique et NotNull.
	 * 
	 * @param bool
	 */
	private void setEnabledSelectedUniqueNotNullCheckBox(boolean bool) {
		if(!bool){
			this.notNullCheckBox.setSelected(false);
			this.notNullCheckBox.setEnabled(false);
			this.uniqueCheckBox.setSelected(false);
			this.uniqueCheckBox.setEnabled(false);
		}else{
			this.notNullCheckBox.setEnabled(true);
			this.uniqueCheckBox.setEnabled(true);
		}
	}

	/**
	 * Definit l'état du champs de saisie de la taille d'un Attribut.
	 * 
	 * @param b
	 */
	private void setEnabledSizeField(boolean b){
		if(b){
			this.attributeSizeField.setEnabled(b);
			this.attributeSizeField.setText("Taille");
		}else{
			this.attributeSizeField.setEnabled(b);
			this.attributeSizeField.setText("1");
		}
	}

	/**
	 * Rend visible les boutons de modification d'un attribut.
	 * @param bool
	 */
	private void setVisibleEnabledUpdateButtons(boolean bool){
		this.confirmUpdateAttributeButton.setVisible(bool);
		this.confirmUpdateAttributeButton.setEnabled(bool);
		this.cancelUpdateAttributeButton.setVisible(bool);
		this.cancelUpdateAttributeButton.setEnabled(bool);
	}


	/**
	 * Modfifie tous les champs d'un attribut.
	 * 
	 * @param name : une chaine String
	 * @param type : une chaine String
	 * @param size : une chaine String
	 * @param notNull : un boolean
	 * @param unique : un boolean
	 * @param pk : un boolean
	 * @param fk : un boolean
	 * @param fkTable : une chaine String
	 * @param fkAttribute : une chaine String
	 */
	private void setAttributesValues(String name, String type, String size, boolean notNull, 
			boolean unique, boolean pk, boolean fk, 
			String fkTable, String fkAttribute)
	{
		this.attributeNameField.setText(name);
		this.attributeTypeComboBox.setSelectedIndex(getIndexAttributeTypeComboBox(type));
		this.attributeSizeField.setText(size);
		this.notNullCheckBox.setSelected(notNull);
		this.uniqueCheckBox.setSelected(unique);
		this.primaryKeyCheckBox.setSelected(pk);
		this.foreignKeyCheckBox.setSelected(fk);
		this.foreignKeyTableComboBoxModel.setSelectedItem(fkTable);
		this.foreignKeyAttributeComboBoxModel.setSelectedItem(fkAttribute);
	}

	/**
	 * Retourne l'index de String dans la ComboBox des types
	 * d'un attribut. 
	 * 0 si VARCHAR2, 1 si NUMBER, 2 si DATE, 3 si CHAR.
	 * 
	 * @param type : une chaine String corespondant au type d'un attribut
	 * @return int
	 */
	private int getIndexAttributeTypeComboBox(String type)
	{
		if("VARCHAR2".equals(type)){
			return 0;
		}else if("NUMBER".equals(type)){
			return 1;
		}else if("DATE".equals(type)){
			return 2;
		}else{
			return 3;
		}
	}

	/**
	 * Initialise les tables dans la ComboBox des tables
	 * lorsque la clé étrangère est selectionnée si bool est vrai,
	 * sinon désative les ComboBox des Clés etrangères.
	 * 
	 * @param bool : un boolean
	 */
	private void initComboBoxFkTableAttributte(boolean bool) {
		if (bool){
			this.res = control.getTables();
			this.foreignKeyTableComboBoxModel.removeAllElements();
			for (String s : res.getCollection()) {
				this.foreignKeyTableComboBoxModel.addElement(s);
			}
		}
		this.fkTableNameComboBox.setEnabled(bool);
		this.fkAtrributeNameComboBox.setEnabled(bool);
	}


	/**
	 * Définit le nom de l'attribut dans la comboBox des clé étrangères
	 * en fonction du nom de la table passé en paramètre.
	 * 
	 * @param table
	 */
	private void initComboBoxFkAttributeName(String table){
		this.foreignKeyAttributeComboBoxModel.removeAllElements();
		ResponseData<String> result = control.getPrimaryKey(table);
		//TODO : Il faut controler qu'il n'y ait pas eu d'erreur ! 
		//TODO : le for each boucle sur un null si erreur !
		for (String s : result.getCollection()) {
			this.foreignKeyAttributeComboBoxModel.addElement(s);
		}
	}

	/**
	 * Détermine ce qu'il se passe lors d'une action sur
	 * le bouton "UP" ou le bouton "DOWN".
	 */
	private void positionAttributButtonAction(String direction){
		if(this.table.getSelectedRow()!=-1){
			int rowIndex = this.table.getSelectedRow();
			if("UP".equals(direction) && rowIndex!=0){
				this.models.changeAttributePosition(direction, rowIndex);
				this.table.setRowSelectionInterval(rowIndex-1, rowIndex-1);
			}
			if("DOWN".equals(direction) && rowIndex!=this.table.getRowCount()-1){
				this.models.changeAttributePosition(direction, rowIndex);
				this.table.setRowSelectionInterval(rowIndex+1, rowIndex+1);
			}
		}
	}

	/**
	 * Détermine ce qu'il se passe lors de la sélection d'une DATE
	 * dans la ComboBox du type de l'attribut.
	 */
	private void selectSizeDateComboBoxAction() {
		Object selected = this.attributeTypeComboBox.getSelectedItem();
		if("DATE".equals(selected.toString())){
			setEnabledSizeField(false);
		}else{
			setEnabledSizeField(true);
		}
	}

	/**
	 * Détermine ce qu'il se passe lors de la sélection d'une Table
	 * dans la ComboBox de sélection des tables pour une clé étrangère.
	 */
	private void selectForeignKeyTableComboBoxAction() {
		Object selected = this.fkTableNameComboBox.getSelectedItem();
		if(!(selected==null)){
			if(!("Nom Attribut".equals(selected.toString()))){
				this.initComboBoxFkAttributeName(selected.toString());
			}
		}
	}

	/**
	 * Détermine ce qu'il se passe lors d'une action sur
	 * le bouton "Ajouter l'attribut".
	 */
	private void addAttributeButtonAction()
	{
		if(isCompleteAttribute()){
		Attribute a = this.models.createAttribute(
				attributeNameField.getText(),
				(String)attributeTypeComboBox.getSelectedItem(), 
				attributeSizeField.getText(), 
				this.notNullCheckBox.isSelected(), 
				this.uniqueCheckBox.isSelected(),
				this.primaryKeyCheckBox.isSelected(),
				this.foreignKeyCheckBox.isSelected(),
				this.foreignKeyTableComboBoxModel.getSelectedItem().toString(),
				this.foreignKeyAttributeComboBoxModel.getSelectedItem().toString());
		if(isValidateAttribute(a)){
			this.addAttributeToTable(a);
		}
		}
	}

	/**
	 * Détermine ce qu'il se passe lors d'une action sur
	 * le bouton "Supprimer l'attribut".
	 */
	private void deleteAttributeButtonAction()
	{
		this.models.removeAttributes(this.table.getSelectedRow());
		this.talk(succesAttribute+"Attribut supprimé");
		this.setEnableButtonUpdateDeleteUpDown(false);
	}

	/**
	 * Détermine ce qu'il se passe lors d'une action sur
	 * le bouton "Modifier l'attribut".
	 */
	private void updateAttributeButtonAction()
	{
		this.talk("");
		int rowIndex = this.table.getSelectedRow();
		Attribute a = this.models.getAttributeAt(rowIndex);
		this.setAttributesValues(a.name, a.type, Integer.toString(a.size), a.notNull, a.unique, a.primaryKey, a.foreignKey, a.fkTable,a.fkAttribute);
		this.updateState=true;
		this.setDisableAllExceptAttribute(false);
		this.setVisibleEnabledUpdateButtons(true);
	}

	/**
	 * Détermine ce qu'il se passe lors d'une action sur
	 * le bouton "Modifier".
	 */
	private void confirmUpdateAttributeButtonAction(){
		if(isCompleteAttribute()){
		Attribute a = this.models.createAttribute(this.attributeNameField.getText(),
				(String)this.attributeTypeComboBox.getSelectedItem(), 
				this.attributeSizeField.getText(), 
				this.notNullCheckBox.isSelected(), 
				this.uniqueCheckBox.isSelected(),
				this.primaryKeyCheckBox.isSelected(),
				this.foreignKeyCheckBox.isSelected(),
				this.foreignKeyTableComboBoxModel.getSelectedItem().toString(),
				this.foreignKeyAttributeComboBoxModel.getSelectedItem().toString());
		if(isValidateAttribute(a)){
			this.models.setAttributeValueAt(this.table.getSelectedRow(),a);
			this.talk(succesAttribute+"Attribut Modifé.");
			this.updateState=false;
			this.clearAttribute();
			this.setDisableAllExceptAttribute(true);
			this.setVisibleEnabledUpdateButtons(false);
		}
		}
	}

	/**
	 * Détermine ce qu'il se passe lors d'une action sur
	 * le bouton "Annuler les modifications".
	 */

	private void cancelUpdateAttributeButtonAction(){
		this.clearAttribute();
		this.setDisableAllExceptAttribute(true);
		this.setVisibleEnabledUpdateButtons(false);

	}
	@Override
	public void actionPerformed(ActionEvent e) 
	{
		Object o = e.getSource();
		if (o == this.attributeButton) {
			this.addAttributeButtonAction();
		}
		if (o == this.createTableButton) {
			if(this.isCompleteTable()){
				createTableButtonAction();
			}
		}
		if (o == this.deleteAttributeButton) {
			this.deleteAttributeButtonAction();
		}
		if (o == this.updateAttributeButton) {
			this.updateAttributeButtonAction();
		}
		if (o == this.resetButton) {
			this.resetView();
		}
		if (o == this.upPositionAttributeButton) {
			this.positionAttributButtonAction("UP");
		}
		if (o == this.downPositionAttributeButton) {
			this.positionAttributButtonAction("DOWN");
		}
		if (o == this.attributeTypeComboBox) {
			selectSizeDateComboBoxAction();
		}
		if (o == this.fkTableNameComboBox) {
			selectForeignKeyTableComboBoxAction();
		}
		if (o== this.confirmUpdateAttributeButton){
			this.confirmUpdateAttributeButtonAction();
		}
		if (o== this.cancelUpdateAttributeButton){
			this.cancelUpdateAttributeButtonAction();
		}
	}

	protected void createTableButtonAction() {
		this.control.createTable(this.getTable());
	}

	/**
	 * Retourne un objet de type Table qui contient tous
	 * les éléments de la vue
	 * @return
	 */
	protected Table getTable() {
		return new Table(
				this.tableNameField.getText(),
				this.models.getAttributes());
	}

	@Override
	public void itemStateChanged(ItemEvent item)
	{
		Object obj = item.getItem();
		int status = item.getStateChange();
		if(obj==this.primaryKeyCheckBox){
			if (status == ItemEvent.SELECTED){
				this.setEnabledSelectedUniqueNotNullCheckBox(false);
			}else if(status == ItemEvent.DESELECTED){
				this.setEnabledSelectedUniqueNotNullCheckBox(true);
			}
		}
		if(obj==this.foreignKeyCheckBox){
			if (status == ItemEvent.SELECTED){
				this.initComboBoxFkTableAttributte(true);
			}else if(status == ItemEvent.DESELECTED){
				this.initComboBoxFkTableAttributte(false);
			}
		}
	}


	@Override
	public boolean isComplete() {
		// TODO Auto-generated method stub
		return false;
	}

}


