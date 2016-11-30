package create;

import interf.IDBFrame;

import java.awt.BorderLayout;
import java.awt.ItemSelectable;
import java.awt.event.*;
import java.awt.Font;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;

public class CreateTableView
extends JFrame 
implements ActionListener, ItemListener {
	private static final String FONT = null;
	/**
	 * Controleur lié à l'IHM.
	 */
	private CreateTableController control;
	
	private Table t;
	
	private final int elementHeight = 20;

	private final int margin = 20;

	private int height = 600;

	private int width = 900;

	private String errorAttribute = "ERREUR : ";
	
	private String succesAttribute = "SUCCES : ";

	private AttributesAbstractTableModel [] models;


	private JTable table;

	private JTable [] tables;


	private JScrollPane scrollPane;

	private JScrollPane [] scrollPanes;


	private JTextField tableNameField;

	private JTextField attributeNameField;

	private JTextField attributeSizeField;
	
	private JTextField fkTableNameField;
	
	private JTextField fkAttributeNameField;

	private JTextField [] fields;

	private final int fieldNumber = 5;


	private JLabel tableNameLabel;

	private JLabel tableLabel;

	private JLabel attributeLabel;

	private JLabel [] labels;
	
	private JLabel errorAttributesLabel;

	private final int labelNumber = 4;

	private JButton resetButton;
	
	private JButton attributeButton;

	private JButton createTableButton;
	
	private JButton updateAttributeButton;
	
	private JButton deleteAttributeButton;

	private JButton buttons[];

	private final int buttonNumber = 5;


	private JPanel panelAttributes;

	private JPanel [] panels;

	private final int panelNumber = 1;


	private JComboBox attributeTypeComboBox;

	private JComboBox tableFKComboBox;

	private JComboBox attributeFKComboBox;

	private JComboBox [] comboBox;

	private final int comboBoxNumber = 1;


	private JCheckBox notNullCheck;

	private JCheckBox uniqueCheck;

	private JCheckBox pkCheck;

	private JCheckBox fkCheck;

	private JCheckBox [] checkBoxs;

	private final int checkBoxNumber = 4;



	private Object[] types = new Object[]{"VARCHAR", "NUMBER", "DATE", "CHAR"};
	
	private Object[] tablesFK = new Object[]{"Clients", "Produits", "Commande"};
	
	private Object[] attributesFK = new Object[]{"code", "nom"};


	public CreateTableView(CreateTableController cm)
	{
		super("Création de table");
		this.control = cm;
		this.setLayout(null);
		this.handlePanels();
		this.handleComboBox();
		this.handleFields();
		this.handleLabels();
		this.handleButtons();
		this.handleCheckBox();
		this.setProperties();
	}


	// ==========================BUTTONS========================
	private void createButtons()
	{
		this.buttons = new JButton [this.buttonNumber];
		this.buttons[0] = this.attributeButton = new JButton("Ajouter l'attribut") ;
		this.buttons[1] = this.createTableButton = new JButton("Créer la Table") ;
		this.buttons[2] = this.deleteAttributeButton = new JButton("Supprimer attribut") ;
		this.buttons[3] = this.updateAttributeButton = new JButton("Mofifier attribut") ;
		this.buttons[4] = this.resetButton = new JButton("Reset") ;

	}


	private void bindButtons()
	{
		this.attributeButton.setBounds((int)(1.35*this.margin), (int)(0.37*height), 165, (int)(1.5*this.elementHeight));
		this.createTableButton.setBounds((int)(1.35*this.margin), (int)(0.87*height), 150, (int)(1.5*this.elementHeight));
		this.deleteAttributeButton.setBounds((int)(1.35*this.margin), (int)(0.80*height), 150, (int)(1.5*this.elementHeight));
		this.updateAttributeButton.setBounds((int)(1.35*this.margin)+ 160, (int)(0.80*height), 150, (int)(1.5*this.elementHeight));
		this.resetButton.setBounds(800, (int)(0.80*height), 80, (int)(1.5*this.elementHeight));
		this.deleteAttributeButton.setEnabled(false);
		this.updateAttributeButton.setEnabled(false);
		
	}


	private void addButtons()
	{
		for (JButton jb : this.buttons) {
			jb.addActionListener(this);
			this.add(jb);
		}
	}

	private void handleButtons()
	{
		this.createButtons();
		this.bindButtons();
		this.addButtons();
	}



	// ==========================FIELDS========================
	private void createFields()
	{	
		this.fields = new JTextField [this.fieldNumber];
		this.fields[0] = this.attributeNameField = new JTextField();
		this.fields[1] = this.attributeSizeField = new JTextField();
		this.fields[2] = this.tableNameField = new JTextField();
		this.fields[3] = this.fkTableNameField = new JTextField();
		this.fields[4] = this.fkAttributeNameField = new JTextField();
	}

	java.awt.event.KeyAdapter intKey = new java.awt.event.KeyAdapter() {
        public void keyTyped(KeyEvent evt) {
            char c = evt.getKeyChar();
            if (c >= '0' && c <= '9') {
            } else {
                evt.consume();
            }
        }
    };
    
    java.awt.event.KeyAdapter stringKey = new java.awt.event.KeyAdapter() {
        public void keyTyped(KeyEvent evt) {
            char c = evt.getKeyChar();
            if ((c >= 'A' && c <= 'Z')||(c >= 'a' && c <= 'z')||(c == '_')||(c >= '0' && c <= '9')) {
            } else {
                evt.consume();
            }
        }
    };

	private void bindFields()
	{
		this.tableNameField.setBounds((int)((1.35*this.margin)+140), (int)(0.09*height), 100, (int)(1.5*this.elementHeight));	
		this.attributeNameField.setBounds((int)(1.35*this.margin), (int)(0.28*height), 100, (int)(1.5*this.elementHeight));	
		this.attributeSizeField.setBounds((int)((1.35*this.margin)+210), (int)(0.28*height), 50, (int)(1.5*this.elementHeight));	
		this.fkTableNameField.setBounds((int)((1.35*this.margin)+675), (int)(0.28*height), 75, (int)(1.5*this.elementHeight));	
		this.fkAttributeNameField.setBounds((int)((1.35*this.margin)+765), (int)(0.28*height), 75, (int)(1.5*this.elementHeight));
		this.attributeSizeField.addKeyListener(intKey);
		this.tableNameField.addKeyListener(stringKey);
		this.attributeNameField.addKeyListener(stringKey);
		this.fkTableNameField.addKeyListener(stringKey);
		this.fkAttributeNameField.addKeyListener(stringKey);
		MaxLengthTextDocument maxLengthNameTable = new MaxLengthTextDocument();
		maxLengthNameTable.setMaxChars(30);
		this.tableNameField.setDocument(maxLengthNameTable);
		
		MaxLengthTextDocument maxLengthNameAtt = new MaxLengthTextDocument();
		maxLengthNameAtt.setMaxChars(64);
		this.attributeNameField.setDocument(maxLengthNameAtt);
		this.attributeNameField.setText("nomAttribut");
		
		
		MaxLengthTextDocument maxLengthSize = new MaxLengthTextDocument();
		maxLengthSize.setMaxChars(6);
		this.attributeSizeField.setDocument(maxLengthSize);
		this.attributeSizeField.setText("Taille");
		this.fkTableNameField.setText("nomTable");
		this.fkAttributeNameField.setText("nomAttribut");
		this.fkTableNameField.setEnabled(false);
		this.fkAttributeNameField.setEnabled(false);	
	}


	private void addFields()
	{
		for (JTextField jtf : this.fields) {
			this.add(jtf);
		}
	}


	private void handleFields()
	{
		this.createFields();
		this.bindFields();
		this.addFields();
	}

	// ==========================LABEL========================
	private void createLabels()
	{
		this.labels = new JLabel [this.labelNumber];
		this.labels[0] = this.tableLabel = new JLabel("Table :");
		this.labels[1] = this.attributeLabel = new JLabel("Attribut :");
		this.labels[2] = this.tableNameLabel= new JLabel("Nom de la table :");
		this.labels[3] = this.errorAttributesLabel= new JLabel("");
	}


	private void bindLabels()
	{
		this.tableLabel.setBounds(this.margin, (int)(0.03*height), 100, (int)(1.5*this.elementHeight));
		this.tableLabel.setFont(new Font(FONT, Font.BOLD, 18));
		this.attributeLabel.setBounds(this.margin, (int)(0.20*height), 100, (int)(1.5*this.elementHeight));
		this.attributeLabel.setFont(new Font(FONT, Font.BOLD, 18));
		this.tableNameLabel.setBounds((int)(1.35*this.margin), (int)(0.09*height), 140, (int)(1.5*this.elementHeight));
		this.tableNameLabel.setFont(new Font(FONT, Font.CENTER_BASELINE, 14));
		this.errorAttributesLabel.setBounds(300,(int)(0.37*height), 600, (int)(1.5*this.elementHeight));
		this.errorAttributesLabel.setFont(new Font(FONT, Font.CENTER_BASELINE, 14));
	}


	private void addLabels()
	{
		for (JLabel jl : this.labels) {
			this.add(jl);
		}
	}

	private void handleLabels()
	{
		this.createLabels();
		this.bindLabels();
		this.addLabels();
	}

	// ==========================PANELS========================
	private void createPanels()
	{
		this.panels = new JPanel[this.panelNumber];
		this.panels[0] = this.panelAttributes = new JPanel(new BorderLayout());
		this.tables = new JTable[1];
		this.models = new AttributesAbstractTableModel[1];
		this.models[0] = new AttributesAbstractTableModel();
		this.tables[0] = new JTable(this.models[0]){
			public boolean isCellEditable(int row, int col) {
				return false;
			}
		};
		this.tables[0].getSelectionModel().addListSelectionListener(new ControlTableResult(this));
		this.scrollPanes = new JScrollPane[1];
		this.scrollPanes[0] = new JScrollPane(this.tables[0]);
	}


	private void bindPanels()
	{
		this.panels[0].setBounds(0, (int)(0.45*height), width-5,  (int)(0.5*height)-100);
		this.tables[0].setFillsViewportHeight(true);
		this.tables[0].setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.scrollPanes[0].setVisible(true);
		this.panels[0].add( this.scrollPanes[0]);
	}


	private void addPanels()
	{
		for (JPanel jl : this.panels) {
			this.add(jl);
		}
	}

	private void handlePanels()
	{
		this.createPanels();
		this.bindPanels();
		this.addPanels();
	}

	// ==========================COMBOBOX========================
	private void createComboBox()
	{
		this.comboBox = new JComboBox [this.comboBoxNumber];
		this.comboBox[0] = this.attributeTypeComboBox = new JComboBox(types);
	}


	private void bindComboBox()
	{
		this.attributeTypeComboBox.setBounds(
				(int)((1.35*this.margin)+110), 
				(int)(0.28*height), 
				85, 
				(int)(1.5*this.elementHeight));
	}


	private void addComboBox()
	{
		for (JComboBox jc : this.comboBox) {
			this.add(jc);
		}
	}


	private void handleComboBox()
	{
		this.createComboBox();
		this.bindComboBox();
		this.addComboBox();
	}

	// ==========================CHECKBOX========================

	private void createCheckBox()
	{
		this.checkBoxs = new JCheckBox [this.checkBoxNumber];
		this.checkBoxs[0] = this.notNullCheck = new JCheckBox("NOT NULL");
		this.checkBoxs[1] = this.uniqueCheck = new JCheckBox("UNIQUE");
		this.checkBoxs[2] = this.pkCheck = new JCheckBox("PRIMARY KEY");
		this.checkBoxs[3] = this.fkCheck = new JCheckBox("FOREIGN KEY");
	}


	private void bindCheckBox()
	{
		this.notNullCheck.setBounds((int)((1.35*this.margin)+265), (int)(0.28*height), 100, (int)(1.5*this.elementHeight));	
		this.uniqueCheck.setBounds((int)((1.35*this.margin)+365), (int)(0.28*height), 80, (int)(1.5*this.elementHeight));	
		this.pkCheck.setBounds((int)((1.35*this.margin)+442), (int)(0.28*height), 115, (int)(1.5*this.elementHeight));	
		this.pkCheck.addItemListener(this);
		this.fkCheck.setBounds((int)((1.35*this.margin)+555), (int)(0.28*height), 115, (int)(1.5*this.elementHeight));	
		this.fkCheck.addItemListener(this);
	}


	private void addCheckBox()
	{
		for (JCheckBox jl : this.checkBoxs) {
			this.add(jl);
		}
	}

	private void handleCheckBox()
	{
		this.createCheckBox();
		this.bindCheckBox();
		this.addCheckBox();
	}

	private void setProperties()
	{
		this.setSize(width, height);
		this.setLocationRelativeTo(null); 
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  
		this.setVisible(true);    
		this.setResizable(false);
	}
	
	private void clearAttribute(){
		this.attributeNameField.setText("");
		this.attributeSizeField.setText("");
		this.notNullCheck.setSelected(false);
		this.uniqueCheck.setSelected(false);
		this.fkCheck.setSelected(false);
		this.pkCheck.setSelected(false);
	}
	
	public void resetView(){
		this.setValues("nomAttribut", "VARCHAR", "Taille", false, false, false, false, "nomTable", "nomAttribut");
		this.tableNameField.setText("");
		this.models[0].removeAll();
	}

	public boolean isInteger(String i){
		try {
		    Integer.parseInt(i);
		    return true;
		} catch (NumberFormatException nfe) {
		    return false;
		}
	}
	
	public boolean isCompleteAttribute(){
		if(this.attributeNameField.getText().equals((String)"") || this.attributeSizeField.getText().equals((String)"")){
			return false;
		}else{
			return true;
		}
	}
	
	public boolean isCompleteTable(){
		if(this.models[0].getRowCount()==0 || this.tableNameField.getText().equals("")){
			this.talk(errorAttribute+"Il n'y a pas d'Attribut OU Il manque le nom de la Table");
			return false;
		}else{
			return true;
		}
	}
	
	public boolean isGoodSizeValue(int size){
		if(size<=4000 && size>0){
			return true;
		}else{
			return false;
		}
	}
	
	public boolean isGoodSize(String size){
		/*
		 *TODO : Non, dans le cas des NUMBERS, la taille peut être décimale.
		 *par exemple, 10,2 veut dire "dix chiffres avant la virgule et deux après"
		 */
		if(isInteger(size)){
			if(isGoodSizeValue(Integer.parseInt(size))){
				return true;				
			}else{
				this.talk(errorAttribute + "La taille de l'attribut doit être un entier compris entre 0 et 4000.");
				return false;
			}
		}else{
			this.talk(errorAttribute + "La taille de l'attribut doit être un entier.");
			return false;
		}
	}
	
	public Boolean isValidateAttributes(){
		if(isCompleteAttribute()){
			if(isGoodSize(attributeSizeField.getText())){
				return true;
			}else{
				return false;
			}
		}else{
			this.talk(errorAttribute + "Les Champs nomAttribut et/ou Taille ne sont pas renseigné(s).");
			return false;
		}
	}
	
	public void setEnableButtonUpdateDelete(boolean b){
		this.deleteAttributeButton.setEnabled(b);
		this.updateAttributeButton.setEnabled(b);
	}
	
	public void talk(String message){
		this.errorAttributesLabel.setText("");
		this.errorAttributesLabel.setText(message);
	}
	
	public void setValues(String name, String type, String size, boolean notNull, 
			boolean unique, boolean pk, boolean fk, 
			String fkTable, String fkAttribute){
		this.attributeNameField.setText(name);
		this.attributeTypeComboBox.setSelectedIndex(getIndexAttributeTypeComboBox(type));
		this.attributeSizeField.setText(size);
		this.notNullCheck.setSelected(notNull);
		this.uniqueCheck.setSelected(unique);
		this.pkCheck.setSelected(pk);
		this.fkCheck.setSelected(fk);
		this.fkTableNameField.setText(fkTable);
		this.fkAttributeNameField.setText(fkAttribute);
	}
	
	public int getIndexAttributeTypeComboBox(String type){
		if(type.equals("VARCHAR")){
			return 0;
		}else if(type.equals("NUMBER")){
			return 1;
		}else if(type.equals("DATE")){
			return 2;
		}else{
			return 3;
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == this.attributeButton) {
			this.addAttributeButtonAction();
		}
		if (e.getSource() == this.createTableButton) {
			if(this.isCompleteTable()){
			this.control.createTable(new Table(
					this.models[0].getAttributes(),
					this.tableNameField.getText()));
			}
		}
		if (e.getSource() == this.deleteAttributeButton) {
			this.deleteAttributeButtonAction();
		}
		if (e.getSource() == this.updateAttributeButton) {
			updateAttributeButtonAction();
		}
		if (e.getSource() == this.resetButton) {
			this.resetView();
		}
	}
	
	public void itemStateChanged(ItemEvent item) {
		Object obj = item.getItem();
		int status = item.getStateChange();
		if((JCheckBox)obj==this.pkCheck){
			if (status == ItemEvent.SELECTED){
				this.notNullCheck.setSelected(false);
				this.notNullCheck.setEnabled(false);
				this.uniqueCheck.setSelected(false);
				this.uniqueCheck.setEnabled(false);
			}else if(status == ItemEvent.DESELECTED){
				this.notNullCheck.setSelected(false);
				this.notNullCheck.setEnabled(true);
				this.uniqueCheck.setSelected(false);
				this.uniqueCheck.setEnabled(true);
			}
		}
		if((JCheckBox)obj==this.fkCheck){
			if (status == ItemEvent.SELECTED){
				this.fkTableNameField.setEnabled(true);
				this.fkAttributeNameField.setEnabled(true);
			}else if(status == ItemEvent.DESELECTED){
				this.fkTableNameField.setEnabled(false);
				this.fkAttributeNameField.setEnabled(false);
				this.fkTableNameField.setText("nomTable");
				this.fkAttributeNameField.setText("nomAttribut");
			}
			}
	}
	
	private void updateAttributeButtonAction(){
		this.talk("");
		int rowIndex = this.tables[0].getSelectedRow();
		Attribute a = this.models[0].getAttributeAt(rowIndex);
		this.setValues(a.getName(), a.getType(), Integer.toString(a.getSize()), a.isNotNull(), a.isUnique(), a.isPrimaryKey(), a.isForeignKey(), a.getFkTable(), a.getFkAttribute());
		this.models[0].removeAttributes(this.tables[0].getSelectedRow());
		this.setEnableButtonUpdateDelete(false);
	}
	
	
	
	private void deleteAttributeButtonAction(){
		this.models[0].removeAttributes(this.tables[0].getSelectedRow());
		this.talk(succesAttribute+"Attribut supprimé");
		this.setEnableButtonUpdateDelete(false);
	}
	
	private void addAttributeButtonAction()
	{
		if(isValidateAttributes()){
				if(fkCheck.isSelected()){
					int i = this.models[0].addAttribute(new Attribute(attributeNameField.getText(),(String)attributeTypeComboBox.getSelectedItem(), Integer.parseInt(attributeSizeField.getText()), notNullCheck.isSelected(), uniqueCheck.isSelected(),pkCheck.isSelected(),fkCheck.isSelected(),fkTableNameField.getText(),fkAttributeNameField.getText()));
					if( i == 0){
						this.talk(errorAttribute +"Un attribut existant a déja le même nom.");
					}else{
						this.talk(succesAttribute +"Attribut ajouté.");
						this.clearAttribute();
					}
				}else{
					int i = this.models[0].addAttribute(new Attribute(attributeNameField.getText(),(String)attributeTypeComboBox.getSelectedItem(), Integer.parseInt(attributeSizeField.getText()), notNullCheck.isSelected(), uniqueCheck.isSelected(),pkCheck.isSelected(),fkCheck.isSelected(),"N/A","N/A"));	
					if( i == 0){
						this.talk(errorAttribute +"Un attribut existant a déja le même nom.");
					}else{
						this.talk(succesAttribute +"Attribut ajouté.");
						this.clearAttribute();
					}
				}
		}		
		
	}
	
}

