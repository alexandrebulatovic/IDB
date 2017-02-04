package ddl;

import java.awt.BorderLayout;
import java.awt.event.*;
import java.util.List;
import java.awt.Font;

import javax.swing.*;

import business.Attribute;
import useful.FieldsKeyAdapter;
import useful.Response;

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

	/**
	 * Constructeur commun.
	 * 
	 * @param control : null interdit.
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
	 * TODO : appellée nulle part
	 * Affiche les arguments passés en paramètres dans la table.
	 * 
	 * @param attributes
	 * @param string
	 */
	public void setView(List<Attribute> attributes, String tableName) {	
		this.resetView();
		for (Attribute a : attributes){
			this.addAttributeToTable(a);
		}
		this.setTableName(tableName);
	}

	
	/**
	 * Active les boutons de suppression, de modification et de 
	 * déplacement des attributs si et seulement si $b est vrai, 
	 * sinon les désactive.
	 * 
	 * @param b 
	 */
	public void setEnableButtonUpdateDeleteUpDown(boolean b){
		this.deleteAttributeButton.setEnabled(b);
		this.updateAttributeButton.setEnabled(b);
		this.upPositionAttributeButton.setEnabled(b);
		this.downPositionAttributeButton.setEnabled(b);
	}

	
	@Override
	public void actionPerformed(ActionEvent e) 
	{
		Object o = e.getSource();
		if (o == this.attributeButton) {
			//	this.addAttributeButtonAction();
		}
		if (o == this.createTableButton) {
			if(this.isComplete()){
				createTableButtonAction();
			}
		}
		if (o == this.deleteAttributeButton) {
			this.deleteAttributeButtonAction();
		}
		if (o == this.updateAttributeButton) {
			//	this.updateAttributeButtonAction();
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
		if (o== this.confirmUpdateAttributeButton){
			//TODO this.confirmUpdateAttributeButtonAction();
		}
		if (o== this.cancelUpdateAttributeButton){
			this.cancelUpdateAttributeButtonAction();
		}
	}


	/**
	 * Retourne un objet de type Table qui contient tous
	 * les éléments de la vue
	 * @return
	 */
	//TODO
	//	protected Table getTable() {
	//		return new Table(
	//				this.tableNameField.getText(),
	//				this.models.getAttributes());
	//	}
	
	@Override
	public void itemStateChanged(ItemEvent item)
	{
		Object obj = item.getItem();
		int status = item.getStateChange();
		if(obj==this.primaryKeyCheckBox){
			if (status == ItemEvent.SELECTED){
				this.setEnabledSelectedNotNullCheckBox(false);
			}else if(status == ItemEvent.DESELECTED){
				this.setEnabledSelectedNotNullCheckBox(true);
			}
		}
	}


	/**
	 * Réinitialise tous les champs de la vue.
	 */
	protected void resetView()
	{
		this.setAttributesValues("nomAttribut", false, false);
		this.tableNameField.setText("");
		this.talk("");
		this.setEnableButtonUpdateDeleteUpDown(false);;
		this.models.removeAll();
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
			this.talk(SUCCES_ATTRIBUTE +"Attribut ajouté.");
			this.clearAttribute();
		}
		else{
			this.talk("Attribut non valide");
		}
	}


	/**
	 * Instancie, positionne et dimensionne les composants s'occupant
	 * de la table ciblée.
	 */
	protected void handleTableComponent()
	{
		this.tableLabel = new JLabel("Table :");
		this.tableLabel.setFont(new Font(FONT, Font.BOLD, 18));
		this.bindAndAdd(this.tableLabel);

		this.tableNameLabel= new JLabel("Nom de la table :");
		this.bindAndAdd(this.tableNameLabel,7,true);
	}


	/**
	 * Instancie, positionne et dimensionne les différents composants de l'IHM.
	 */
	private void handleComponents()
	{
		this.handleTableComponent();
		this.tableNameField = new JTextField();
		this.bindAndAdd(this.tableNameField, 6, false);
		this.handleAttributesComponent();
		this.handleRestComponent();
	}


	/**
	 * Instancie, dimensionne et positionne les composants s'occupant
	 * d'ajouter un nouvel attribut.
	 */
	private void handleAttributesComponent()
	{
		this.increaseTop(10);
		this.attributeLabel = new JLabel("Attribut :");
		this.attributeLabel.setFont(new Font(FONT, Font.BOLD, 18));
		this.bindAndAdd(this.attributeLabel);

		int b = 6;
		this.attributeNameField = new JTextField();
		this.bindAndAdd(this.attributeNameField, b,true);

		String [] types = this.control.getAttributeTypes();
		this.attributeTypeComboBox = new JComboBox<String>(types);
		this.attributeTypeComboBox.addActionListener(this);
		this.bindAndAdd(this.attributeTypeComboBox, b,true);

		this.attributeSizeField = new JTextField();
		this.attributeSizeField.setToolTipText("Taille de l'attribut.");
		this.bindAndAdd(this.attributeSizeField, b, false);

		this.notNullCheckBox = new JCheckBox("NOT NULL");
		this.bindAndAdd(this.notNullCheckBox,b,true);

		this.primaryKeyCheckBox = new JCheckBox("PRIMARY KEY");
		this.primaryKeyCheckBox.addItemListener(this);
		this.bindAndAdd(this.primaryKeyCheckBox,b,true);


		this.attributeButton = new JButton("Ajouter l'attribut") ;
		this.attributeButton.addActionListener(this);
		this.bindAndAdd(this.attributeButton,b,true);
	}

	
	/**
	 * Instancie, dimensionne et positionne beaucoup de composants.
	 */
	private void handleRestComponent()
	{
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
		KeyAdapter intKey = new FieldsKeyAdapter("int");
		this.attributeSizeField.addKeyListener(intKey);
		this.attributeSizeField.setDocument(fieldInputSize(3));

		KeyAdapter stringKey = new FieldsKeyAdapter("String");
		this.tableNameField.addKeyListener(stringKey);
		this.tableNameField.setDocument(fieldInputSize(30));

		this.attributeNameField.addKeyListener(stringKey);	
		this.attributeNameField.setDocument(fieldInputSize(29));
		this.attributeNameField.setText("nomAttribut");
	}

	
	/**
	 * Réinitialise les champs de saisie des attributs.<br/>
	 * Décoche les cases à cocher.<br/>
	 * Replace les listes déroulantes sur le premier item.<br/>
	 */
	private void clearAttribute()
	{
		this.attributeNameField.setText("");
		this.attributeSizeField.setText("");
		this.notNullCheckBox.setSelected(false);
		this.primaryKeyCheckBox.setSelected(false);
		this.attributeTypeComboBox.setSelectedIndex(0);
	}
	

	/**
	 * Retourne vrai si la table est complète, c'est à dire si elle
	 * contient des attributs et un nom de table,
	 * faux sinon.
	 * 
	 * @return boolean
	 */
	@Override
	public boolean isComplete()
	{
		if(this.models.getRowCount()==0){
			this.talk(ERROR_ATTRIBUTE+"Il n'y a pas d'Attribut");
			return false;
		} else if ("".equals(this.tableNameField.getText())) {
			this.talk(ERROR_ATTRIBUTE+"Il manque le nom de la Table");
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
			this.talk(ERROR_ATTRIBUTE +a.attributeSizeError(a.checkSizeAttributes()));
			return false;
		}else if(this.models.isDuplicateAttributeName(a) && !(this.updateState)){
			this.talk(ERROR_ATTRIBUTE +"Un attribut existant a déja le même nom.");
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
	private void setEnabledSelectedNotNullCheckBox(boolean bool) {
		if(!bool){
			this.notNullCheckBox.setSelected(false);
			this.notNullCheckBox.setEnabled(false);
		}else{
			this.notNullCheckBox.setEnabled(true);
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
	 * TODO : chaine d'obsolescence getIndexAttributeTypeComboBox()
	 * @param attribute : nom de l'attribut, null interdit.
	 * @param notNull : vrai si et seulement si $attribute ne peut pas être null, faux sinon.
	 * @param pk : vrai si et seulement si $attribute est membre de la clée primaire, faux sinon.
	 */
	private void setAttributesValues(String attribute, boolean notNull, boolean pk)
	{
		this.attributeNameField.setText(attribute);
		this.attributeTypeComboBox.setSelectedIndex(0);
		this.notNullCheckBox.setSelected(notNull);
		this.primaryKeyCheckBox.setSelected(pk);;
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
	 * Détermine ce qu'il se passe lors d'une action sur
	 * le bouton "Ajouter l'attribut".
	 */
	//TODO
	//	private void addAttributeButtonAction()
	//	{
	//		if(isCompleteAttribute()){
	//		Attribute a = this.models.createAttribute(
	//				attributeNameField.getText(),
	//				(String)attributeTypeComboBox.getSelectedItem(), 
	//				attributeSizeField.getText(), 
	//				this.notNullCheckBox.isSelected(), 
	//				this.uniqueCheckBox.isSelected(),
	//				this.primaryKeyCheckBox.isSelected(),
	//				this.foreignKeyCheckBox.isSelected(),
	//				this.foreignKeyTableComboBoxModel.getSelectedItem().toString(),
	//				this.foreignKeyAttributeComboBoxModel.getSelectedItem().toString());
	//		if(isValidateAttribute(a)){
	//			this.addAttributeToTable(a);
	//		}
	//		}
	//	}

	/**
	 * Détermine ce qu'il se passe lors d'une action sur
	 * le bouton "Supprimer l'attribut".
	 */
	private void deleteAttributeButtonAction()
	{
		this.models.removeAttributes(this.table.getSelectedRow());
		this.talk(SUCCES_ATTRIBUTE+"Attribut supprimé");
		this.setEnableButtonUpdateDeleteUpDown(false);
	}

	//TODO
	//	/**
	//	 * Détermine ce qu'il se passe lors d'une action sur
	//	 * le bouton "Modifier l'attribut".
	//	 */
	//	private void updateAttributeButtonAction()
	//	{
	//		this.talk("");
	//		int rowIndex = this.table.getSelectedRow();
	//		Attribute a = this.models.getAttributeAt(rowIndex);
	//		this.setAttributesValues(a.name, a.type, Integer.toString(a.size), a.notNull, a.unique, a.primaryKey, a.foreignKey, a.fkTable,a.fkAttribute);
	//		this.updateState=true;
	//		this.setDisableAllExceptAttribute(false);
	//		this.setVisibleEnabledUpdateButtons(true);
	//	}

	/**
	 * Détermine ce qu'il se passe lors d'une action sur
	 * le bouton "Modifier".
	 */
	//TODO
	//	private void confirmUpdateAttributeButtonAction(){
	//		if(isCompleteAttribute()){
	//		Attribute a = this.models.createAttribute(this.attributeNameField.getText(),
	//				(String)this.attributeTypeComboBox.getSelectedItem(), 
	//				this.attributeSizeField.getText(), 
	//				this.notNullCheckBox.isSelected(), 
	//				this.uniqueCheckBox.isSelected(),
	//				this.primaryKeyCheckBox.isSelected(),
	//				this.foreignKeyCheckBox.isSelected(),
	//				this.foreignKeyTableComboBoxModel.getSelectedItem().toString(),
	//				this.foreignKeyAttributeComboBoxModel.getSelectedItem().toString());
	//		if(isValidateAttribute(a)){
	//			this.models.setAttributeValueAt(this.table.getSelectedRow(),a);
	//			this.talk(SUCCES_ATTRIBUTE+"Attribut Modifé.");
	//			this.updateState=false;
	//			this.clearAttribute();
	//			this.setDisableAllExceptAttribute(true);
	//			this.setVisibleEnabledUpdateButtons(false);
	//		}
	//		}
	//	}

	/**
	 * Détermine ce qu'il se passe lors d'une action sur
	 * le bouton "Annuler les modifications".
	 */

	private void cancelUpdateAttributeButtonAction(){
		this.clearAttribute();
		this.setDisableAllExceptAttribute(true);
		this.setVisibleEnabledUpdateButtons(false);

	}
	/**
	 * Document permettant de limiter le nombre de caractères maximum saisis.
	 * Retourne un Document pouvant être passé en paramètre 
	 * de la fonction Component.setDocument(Document).
	 * 
	 * @param size : un entier int
	 * @return MaxLengthTextDocument
	 */
	private static MaxLengthTextDocument fieldInputSize(int size)
	{
		MaxLengthTextDocument maxLengthNameAtt = new MaxLengthTextDocument();
		maxLengthNameAtt.setMaxChars(size);
		return maxLengthNameAtt;
	}


	protected void createTableButtonAction() 
	{
		/**
		 * TODO : envoyer appeller le controleur
		 * afficher la Response retournée.
		 */
	}

}


