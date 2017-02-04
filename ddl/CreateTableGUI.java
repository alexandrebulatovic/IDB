package ddl;

import java.awt.BorderLayout;
import java.awt.event.*;
import java.awt.Font;

import javax.swing.*;

import business.Attribute;
import useful.FieldsKeyAdapter;


import useful.MaxLengthTextDocument;

@SuppressWarnings("serial")
/**
 * IHM pour créer des tables dans la base de données.
 * 
 * @author MAURY Adrian
 */
public class CreateTableGUI
extends CreateModifyProperties
implements ItemListener
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
	 * Active les boutons de suppression, de modification et de 
	 * déplacement des attributs si et seulement si $enabled est vrai, 
	 * sinon les désactive.
	 * 
	 * @param enabled : vrai si et seulement si les boutons doivent être activés,
	 * faux sinon.
	 */
	public void setEnableButtonUpdateDeleteUpDown(boolean enabled){
		this.deleteAttributeButton.setEnabled(enabled);
		this.updateAttributeButton.setEnabled(enabled);
		this.upPositionAttributeButton.setEnabled(enabled);
		this.downPositionAttributeButton.setEnabled(enabled);
	}


	@Override
	public void actionPerformed(ActionEvent e) 
	{
		Object o = e.getSource();
		if (o == this.attributeButton) {
				this.addAttributeButtonAction();
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
			this.selectSizeDateComboBoxAction();
		}
		if (o== this.confirmUpdateAttributeButton){
			this.confirmUpdateAttributeButtonAction();
		}
		if (o== this.cancelUpdateAttributeButton){
			this.cancelUpdateAttributeButtonAction();
		}
	}


	//	/**
	//	 * TODO
	//	 * @return un objet de type Table qui contient tous
	//	 * les éléments de la vue
	//	 */
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

		if(obj == this.primaryKeyCheckBox){
			boolean enabled = (status == ItemEvent.DESELECTED);
			this.enableSelectNotNullCheckBox(enabled);
		}
	}


	/**
	 * Réinitialise toutes les boites de saisie de la vue.<br/>
	 * Réactive toutes les cases à cocher.<br/>
	 * Efface les messages.<br/>
	 * Vide la collection d'attributs.
	 */
	protected void resetView()
	{
		this.talk("");
		this.tableNameField.setText("");
		this.clearAttribute();
		this.setEnableButtonUpdateDeleteUpDown(false);;
		this.models.removeAll();
	}


	/**
	 * Réinitialise les champs de saisie des attributs.<br/>
	 * Décoche les cases à cocher.<br/>
	 * Replace les listes déroulantes sur le premier item.<br/>
	 */
	private void clearAttribute()
	{
		this.setAttributesValues("nomAttribut", 0, "", false, false);
	}


	/**
	 * Modifie l'état des différents composants liés à la saisie d'un attribut.
	 * 
	 * @param attribute : texte à écrire dans la boite de saisie du nom de l'attribut, null interdit.
	 * @param type : indice de la liste déroulante des types, 0 <= type < 4.
	 * @param size : texte à écrire dans la boite de saisie de la taille de l'attribut, 
	 *  null interdit, chaine vide autorisée.
	 * @param notNull : vrai si et seulement si la case à cocher NOT NULL 
	 *  doit être sélectionnée, faux sinon.
	 * @param pk : vrai si et seulement si la case à cocher PRIMARY KEY 
	 *  doit être sélectionnée, faux sinon.
	 */
	private void setAttributesValues
	(String attribute, int type, String size, boolean notNull, boolean pk)
	{
		this.attributeNameField.setText(attribute);
		this.attributeTypeComboBox.setSelectedIndex(type);
		this.attributeSizeField.setText(size);
		this.notNullCheckBox.setSelected(notNull);
		this.primaryKeyCheckBox.setSelected(pk);;
	}


	/**
	 * Ajoute l'attribut passé en paramètre à la table 
	 * contenant les attributs.
	 * 
	 * @param attribute : null interdit.
	 */
	protected void addAttributeToTable(String name, int type, String size, boolean notNull, boolean primaryKey){
		if (this.isValidateAttribute(name,type,size,notNull,primaryKey)){
			this.models.addAttribute(name,(String)this.attributeTypeComboBox.getSelectedItem(),Integer.parseInt(size),notNull,primaryKey);
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
		JLabel table = new JLabel("Table :");
		table.setFont(new Font(FONT, Font.BOLD, 18));
		this.bindAndAdd(table);

		JLabel tableName = new JLabel("Nom de la table :");
		this.bindAndAdd(tableName, 6, true);
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
		JLabel attributeLabel = new JLabel("Attribut :");
		attributeLabel.setFont(new Font(FONT, Font.BOLD, 18));
		this.bindAndAdd(attributeLabel);

		int b = 6;
		this.attributeNameField = new JTextField();
		this.bindAndAdd(this.attributeNameField, b,true);

		this.types = this.control.getAttributeTypes();
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
		this.setVisibleUpdateButtons(false);
		this.increaseTop(10);

		this.panelAttributes = new JPanel(new BorderLayout());
		this.models = new AttributesAbstractTableModel();
		this.table = new JTable(this.models){
			public boolean isCellEditable(int row, int col) {
				return false;
			}
		};
		this.table.getSelectionModel().addListSelectionListener
		(new ControlTableResult(this));
		JScrollPane jscp = new JScrollPane(this.table);
		this.table.setFillsViewportHeight(true);
		this.table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		jscp.setVisible(true);
		this.panelAttributes.add(jscp);
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


	@Override
	public boolean isComplete()
	{
		if(this.models.getRowCount()==0){
			this.talk(ERROR_ATTRIBUTE+"Il n'y a pas d'attribut");
			return false;
		} 
		else if ("".equals(this.tableNameField.getText())) {
			this.talk(ERROR_ATTRIBUTE+"Il manque le nom de la Table");
			return false;
		} 
		else {
			return true;
		}
	}

	/**
	 * @param at : null interdit.
	 * @return vrai si et seulement si $a est un attribut dont :<br/> 
	 * -les champs attributs sont complets,<br/> 
	 * -la taille renseignée est correcte en fonction du type,<br/> 
	 * -le nom est unique dans la table.
	 */
	private boolean isValidateAttribute(String name, int type, String size, boolean notNull, boolean pk)
	{
		int checkSize = this.models.checkSizeAttributes(type,Integer.parseInt(size));
		if(!(checkSize>=0)){
			this.talk(ERROR_ATTRIBUTE +models.attributeSizeError(checkSize));
			return false;
		}
		else if(this.models.isDuplicateAttributeName(name) && !(this.updateState)){
			this.talk(ERROR_ATTRIBUTE +"Un attribut existant a déja le même nom.");
			return false;
		}
		else return true;
	}


	/**
	 * Modifie le nom de la Table dans le champs de saisie.
	 * 
	 * @param tableName : une chaine de caractère
	 */
	private void setTableName(String tableName) 
	{
		this.tableNameField.setText(tableName);
	}


	/**
	 * Active ou désactive tous les composants qui n'interviennent pas
	 * dans la définition d'un attribut.
	 * 
	 * @param enable : vrai si et seulement si les composants doivent être activés,
	 * faux sinon.
	 */
	private void disableAllExceptAttribute(boolean enable)
	{
		this.tableNameField.setEnabled(enable);
		this.attributeButton.setEnabled(enable);
		this.panelAttributes.setEnabled(enable);
		this.setEnableButtonUpdateDeleteUpDown(enable);
		this.upPositionAttributeButton.setEnabled(enable);
		this.downPositionAttributeButton.setEnabled(enable);
		this.resetButton.setEnabled(enable);
		this.createTableButton.setEnabled(enable);
	}


	/**
	 * Definit si la case à cocher NOT NULL est active et sélectionnée.
	 * 
	 * @param state :  vrai si et seulement si la case à cocher doit être active,
	 * faux si elle doit être inactive et non sélectionnée.
	 */
	private void enableSelectNotNullCheckBox(boolean state) 
	{
		if(!state) this.notNullCheckBox.setSelected(false);
		this.notNullCheckBox.setEnabled(state);
	}


	/**
	 * Definit l'état du champs de saisie de la taille d'un attribut.<br/>
	 * Une taille de 1 est écrite si la boite de saisie est désactivée.
	 * 
	 * @param enabled : vrai si et seulement si la boite de saisie doit être activée,
	 * faux sinon.
	 */
	private void enableSizeField(boolean enabled)
	{
		this.attributeSizeField.setEnabled(enabled);
		if(!enabled) this.attributeSizeField.setText("1");
	}


	/**
	 * Rend visible ou non les boutons de modification d'un attribut.
	 * @param visible : vrai si et seulement si les boutons doivent être visible,
	 * faux sinon.
	 */
	private void setVisibleUpdateButtons(boolean visible)
	{
		this.confirmUpdateAttributeButton.setVisible(visible);
		//		this.confirmUpdateAttributeButton.setEnabled(bool);
		this.cancelUpdateAttributeButton.setVisible(visible);
		//		this.cancelUpdateAttributeButton.setEnabled(bool);
	}


	/**
	 * Détermine ce qu'il se passe lors d'une action sur
	 * le bouton "UP" ou le bouton "DOWN".
	 */
	private void positionAttributButtonAction(String direction)
	{
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
			enableSizeField(false);
		}else{
			enableSizeField(true);
		}
	}

	/**
	 * Détermine ce qu'il se passe lors d'une action sur
	 * le bouton "Ajouter l'attribut".
	 * TODO
	 */

	private void addAttributeButtonAction()
	{
		if(isCompleteAttribute()){
			String name = attributeNameField.getText();
			int type = attributeTypeComboBox.getSelectedIndex();
			String size = attributeSizeField.getText();
			boolean notNull = this.notNullCheckBox.isSelected();
			boolean primaryKey = this.primaryKeyCheckBox.isSelected();
			if(isValidateAttribute(name,type,size,notNull,primaryKey)){
				this.addAttributeToTable(name,type,size,notNull,primaryKey);
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
		this.talk(SUCCES_ATTRIBUTE+"Attribut supprimé");
		this.setEnableButtonUpdateDeleteUpDown(false);
	}


		/**
		 *	TODO
		 * Détermine ce qu'il se passe lors d'une action sur
		 * le bouton "Modifier l'attribut".
		 */
		private void updateAttributeButtonAction()
		{
			this.talk("");
			int rowIndex = this.table.getSelectedRow();
			String name = (String) this.models.getValueAt(rowIndex, 0);
			String type = (String) this.models.getValueAt(rowIndex, 1);
			int size = (int) this.models.getValueAt(rowIndex, 2);
			boolean notNull = (boolean) this.models.getValueAt(rowIndex, 3);
			boolean primaryKey = (boolean) this.models.getValueAt(rowIndex, 4);
					
			this.setAttributesValues(name,this.getAttributeSizeComboBoxIndex(type),Integer.toString(size),notNull,primaryKey);
			this.updateState=true;
			this.disableAllExceptAttribute(false);
			this.setVisibleUpdateButtons(true);
		}


	/**
	 * @return vrai ssi tous les champs de l'attributs sont renseignés,
	 * faux sinon.
	 */
	private boolean isCompleteAttribute()
	{
		String name = this.attributeNameField.getText();
		String size = this.attributeSizeField.getText();

		if("".equals(name) || "".equals(size)) {
			this.talk(ERROR_ATTRIBUTE + 
					"Tous les champs Attributs doivent être renseignés.");
			return false;
		} 
		else
			return true;
	}

		/**
		 * Détermine ce qu'il se passe lors d'une action sur
		 * le bouton "Modifier".
		 * TODO
		 */
	
		private void confirmUpdateAttributeButtonAction(){
			if(isCompleteAttribute()){
				String name = attributeNameField.getText();
				int type = attributeTypeComboBox.getSelectedIndex();
				String size = attributeSizeField.getText();
				boolean notNull = this.notNullCheckBox.isSelected();
				boolean primaryKey = this.primaryKeyCheckBox.isSelected();
						if(isValidateAttribute(name,type,size,notNull,primaryKey)){
							this.models.setAttributeValueAt(this.table.getSelectedRow(),name, attributeTypeComboBox.getItemAt(type),Integer.parseInt(size),notNull,primaryKey);
							this.talk(SUCCES_ATTRIBUTE+"Attribut Modifé.");
							this.updateState=false;
							this.clearAttribute();
							this.disableAllExceptAttribute(true);
							this.setVisibleUpdateButtons(false);
						}
			}
		}


	/**
	 * Détermine ce qu'il se passe lors d'une action sur
	 * le bouton "Annuler les modifications".
	 */

	private void cancelUpdateAttributeButtonAction(){
		this.clearAttribute();
		this.disableAllExceptAttribute(true);
		this.setVisibleUpdateButtons(false);

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

	private int getAttributeSizeComboBoxIndex(String type){
		int index = 0;
		for(String string : types){
			if(type.equals(string))
				return index;
			index++;
		}
		return -1;
	}

	protected void createTableButtonAction() 
	{
		/**
		 * TODO : envoyer appeller le controleur
		 * afficher la Response retournée.
		 */
	}
}