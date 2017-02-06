package gui.ddl;



import java.awt.BorderLayout;
import java.awt.event.*;
import java.awt.Font;
import java.util.LinkedHashSet;

import javax.swing.*;

import controller.DDLController;
import ddl.AttributesAbstractTableModel;
import ddl.ControlTableResult;
import ddl.CreateModifyProperties;
import ddl.I_Attribute;
import ddl.I_Table;
import ddl.TableModel;
import useful.FieldsKeyAdapter;
import useful.MaxLengthTextDocument;
import useful.Response;

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
	public void setEnableButtonUpdateDeleteUpDown(boolean enabled)
	{
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
		else if (o == this.createTableButton) {
			if(this.isComplete()){
				createTableButtonAction();
			}
		}
		else if (o == this.deleteAttributeButton) {
			this.deleteAttributeButtonAction();
		}
		else if (o == this.updateAttributeButton) {
			this.updateAttributeButtonAction();
		}
		else if (o == this.resetButton) {
			this.resetView();
		}
		else if (o == this.upPositionAttributeButton) {
			this.positionAttributButtonAction("UP");
		}
		else if (o == this.downPositionAttributeButton) {
			this.positionAttributButtonAction("DOWN");
		}
		else if (o == this.attributeTypeComboBox) {
			this.selectSizeDateComboBoxAction();
		}
		else if (o== this.confirmUpdateAttributeButton){
			this.confirmUpdateAttributeButtonAction();
		}
		else if (o== this.cancelUpdateAttributeButton){
			this.cancelUpdateAttributeButtonAction();
		}
	}


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


	@Override
	public boolean isComplete()
	{
		String msg;
		boolean hasAttribute = this.models.getRowCount()!=0;
		boolean hasName =  !"".equals(this.tableNameField.getText());
		boolean okay =  hasAttribute &&  hasName ;
		
		if(! okay){
			if (!hasAttribute)
				msg = "Il n'y a pas d'attribut";
			else 
				msg = "Il manque le nom de la table.";
		} 
		else msg = "";
		
		this.talk(new Response(okay, msg));
		return okay;
	}


	/**
	 * Instancie, dimensionne et positionne les composants pour 
	 * sélectionner une table.
	 */
	protected void handleTableInputs()
	{
		this.tableNameField = new JTextField();
		this.bindAndAdd(this.tableNameField, 6, false);
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
	 * Ajoute l'attribut passé en paramètre à la table 
	 * contenant les attributs.
	 * 
	 * @param attribute : null interdit.
	 */
	protected void addAttributeToTable(I_Attribute attribute){
		this.models.addAttribute(attribute);
		this.talk(new Response(true,"Attribut ajouté."));
		this.clearAttribute();
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
	private void setAttributesValues (String attribute, int type, String size, boolean notNull, boolean pk)
	{
		this.attributeNameField.setText(attribute);
		this.attributeTypeComboBox.setSelectedIndex(type);
		this.attributeSizeField.setText(size);
		this.notNullCheckBox.setSelected(notNull);
		this.primaryKeyCheckBox.setSelected(pk);;
	}

	/**
	 * Instancie, positionne et dimensionne les différents composants de l'IHM.
	 */
	private void handleComponents()
	{
		this.handleTableLabels();
		this.handleTableInputs();
		this.handleAttributesComponent();
		this.handleRestComponent();
	}

	
	/**
	 * Instancie, positionne et dimensionne les composants s'occupant
	 * de la table ciblée.
	 */
	private void handleTableLabels()
	{
		JLabel table = new JLabel("Table :");
		table.setFont(new Font(FONT, Font.BOLD, 18));
		this.bindAndAdd(table);
	
		JLabel tableName = new JLabel("Nom de la table :");
		this.bindAndAdd(tableName, 6, true);
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
		this.models = new AttributesAbstractTableModel(this);
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


	/**
	 * Affiche un message pour indiquer si $attribute est acceptable.
	 * 
	 * @param attribute : null interdit.
	 * @return vrai si et seulement si $a est un attribut dont :<br/> 
	 * -les champs attributs sont complets,<br/> 
	 * -la taille renseignée est correcte en fonction du type,<br/> 
	 * -le nom est unique dans la table.
	 */
	private boolean isValidateAttribute(I_Attribute attribute)
	{
		String msg;
		
		boolean result;
		boolean doublon = this.models.isDuplicateAttributeName(attribute);

		if(! attribute.checkSize()){
			msg = attribute.sizeErrorMsg();
			result = false;
		}
		else if(!doublon) {
			msg = "";
			result = true;
		} 
		else {
			String msgDoublon = "Un attribut existant a déja le même nom.";
			if(!this.updateState) {
				msg = msgDoublon;
				result = false;
			} 
			else {
				int rowIndex = this.table.getSelectedRow();
				I_Attribute test = this.models.getAttributeAt(rowIndex);
				result = attribute.equals(test);
				msg = result ? "" : msgDoublon;
			}
		}
		
		this.talk(new Response(result, msg));
		return result;
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
		this.table.setEnabled(enable);
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
		this.cancelUpdateAttributeButton.setVisible(visible);
	}


	/**
	 * Détermine ce qu'il se passe lors d'une action sur
	 * le bouton "UP" ou le bouton "DOWN".
	 */
	private void positionAttributButtonAction(String direction)
	{
		boolean existRow = this.table.getSelectedRow()!=-1;
		
		if(existRow){
			int rowIndex = this.table.getSelectedRow();
			boolean firstRow = (rowIndex==0);
			boolean lastRow = rowIndex==(this.table.getRowCount()-1);
			
			if("UP".equals(direction) && !firstRow){
				this.models.changeAttributePosition(direction, rowIndex);
				this.table.setRowSelectionInterval(rowIndex-1, rowIndex-1);
			}
			else if("DOWN".equals(direction) && !lastRow){
				this.models.changeAttributePosition(direction, rowIndex);
				this.table.setRowSelectionInterval(rowIndex+1, rowIndex+1);
			}
		}
	}


	/**
	 * Détermine ce qu'il se passe lors de la sélection d'une DATE
	 * dans la ComboBox du type de l'attribut.
	 */
	private void selectSizeDateComboBoxAction() 
	{
		Object selected = this.attributeTypeComboBox.getSelectedItem();
		enableSizeField(!"DATE".equals(selected.toString()));
	}

	
	/**
	 * Détermine ce qu'il se passe lors d'une action sur
	 * le bouton "Ajouter l'attribut".
	 */
	private void addAttributeButtonAction()
	{
		if(isCompleteAttribute()){
			String name = attributeNameField.getText();
			String type = (String)attributeTypeComboBox.getSelectedItem();
			int size = Integer.parseInt(attributeSizeField.getText());
			boolean notNull = this.notNullCheckBox.isSelected();
			boolean primaryKey = this.primaryKeyCheckBox.isSelected();
			I_Attribute attribute = this.control.getAttributeModel
					(name, type, size, notNull, primaryKey);
			if(this.isValidateAttribute(attribute))
				this.addAttributeToTable(attribute);
		}
	}

	
	/**
	 * Détermine ce qu'il se passe lors d'une action sur
	 * le bouton "Supprimer l'attribut".
	 */
	private void deleteAttributeButtonAction()
	{
		this.models.removeAttributes(this.table.getSelectedRow());
		this.talk(new Response(true,"Attribut supprimé"));
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
	 * Affiche un message pour indiquer si toutes les informations
	 * nécessaire pour créer un attribut sont renseignées dans les 
	 * formulaires de saisie.
	 * 
	 * @return vrai ssi tous les champs de l'attributs sont renseignés,
	 * faux sinon.
	 */
	private boolean isCompleteAttribute()
	{
		String name = this.attributeNameField.getText();
		String size = this.attributeSizeField.getText();

		if("".equals(name) || "".equals(size)) {
			this.talk(new Response
					(false, "Les champs de nom et de taille d'attribut doivent être renseignés."));
			return false;
		} 
		else
			return true;
	}

	/**
	 * Détermine ce qu'il se passe lors d'une action sur
	 * le bouton "Modifier".
	 */

	private void confirmUpdateAttributeButtonAction(){
		if(isCompleteAttribute()){
			String name = attributeNameField.getText();
			String type = (String)attributeTypeComboBox.getSelectedItem();
			int size = Integer.parseInt(attributeSizeField.getText());
			boolean notNull = this.notNullCheckBox.isSelected();
			boolean primaryKey = this.primaryKeyCheckBox.isSelected();
			I_Attribute attribute = this.control.getAttributeModel(name,type,size,notNull,primaryKey);
			if(isValidateAttribute(attribute)){
				this.models.setAttributeValueAt(this.table.getSelectedRow(),attribute);
				this.talk(new Response(true,"Attribut modifé."));
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
		this.updateState=false;
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
		I_Table table = new TableModel(this.tableNameField.getText());
		LinkedHashSet<I_Attribute> attributes = this.models.getAttributes();
		for(I_Attribute attribute : attributes){
			table.addAttribute(attribute);
		}
		System.out.println(table);
		this.control.createTable(table);
		
	}
}