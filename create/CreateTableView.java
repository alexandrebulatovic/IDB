package create;

import java.awt.BorderLayout;
import java.awt.event.*;
import java.awt.Font;

import javax.swing.*;

public class CreateTableView
extends JFrame 
implements ActionListener, ItemListener {
	// ==========================VARIABLES========================
	private static final String FONT = null;
	
	/**
	 * Controleur lié à l'IHM.
	 */
	private CreateTableController control;
	
	/**
	 * Hauteur des éléments.
	 */
	private final int elementHeight = 20;

	/**
	 * Marge à gauche des éléments.
	 */
	private final int margin = 20;

	/**
	 * Hauteur de l'IHM.
	 */
	private int height = 600;

	/**
	 * Largeur de l'IHM.
	 */
	private int width = 900;

	/**
	 * Préfixe des messages d'erreurs.
	 */
	private String errorAttribute = "ERREUR : ";
	
	/**
	 * Préfixe des messages de succès.
	 */
	private String succesAttribute = "SUCCES : ";
	
	/**
	 * 
	 */
	private Object[] types = new Object[]{"VARCHAR", "NUMBER", "DATE", "CHAR"};

	/**
	 * Contient tous les models.
	 */
	private AttributesAbstractTableModel [] models;

	/**
	 * Tableau contenant les attributs.
	 */
	private JTable table;

	/**
	 * Contient tous les Tableaux.
	 */
	private JTable [] tables;

	/**
	 * ScrollPane du Tableau.
	 */
	private JScrollPane scrollPane;

	/**
	 * Contient tous les scrollPanes.
	 */
	private JScrollPane [] scrollPanes;
	
	// ==========================FIELDS========================
	/**
	 * Boite de saisie du nom de la table.
	 */
	private JTextField tableNameField;

	/**
	 * Boite de saisie du nom de l'attribut.
	 */
	private JTextField attributeNameField;

	/**
	 * Boite de saisie de la taille de l'attribut.
	 */
	private JTextField attributeSizeField;
	
	/**
	 * Boite de saisie du nom de la table pour la clé etrangère. 
	 */
	private JTextField fkTableNameField;
	
	/**
	 * Boite de saisie du nom de l'attribut pour la clé etrangère. 
	 */
	private JTextField fkAttributeNameField;

	/**
	 * Contient toutes les boîtes de saisie.
	 */
	private JTextField [] fields;

	/**
	 * Nombre de boîtes de saisie.
	 */
	private final int fieldNumber = 5;
	
	// ==========================LABELS========================
	/**
	 * Etiquette pour le nom de la Table.
	 */
	private JLabel tableNameLabel;

	/**
	 * Etiquette pour le titre Table.
	 */
	private JLabel tableLabel;

	/**
	 * Etiquette pour le titre Attribut.
	 */
	private JLabel attributeLabel;

	/**
	 * Etiquette pour les Messages d'erreurs.
	 */
	private JLabel errorAttributesLabel;
	
	/**
	 * Contient toutes les étiquettes
	 */
	private JLabel [] labels;
	
	/**
	 * Nombre d'étiquettes.
	 */
	private final int labelNumber = 4;
	
	
	// ==========================BUTTONS========================
	/**
	 * Bouton 'Reset'.
	 */
	private JButton resetButton;
	
	/**
	 * Bouton 'Ajouter l'attribut'.
	 */
	private JButton attributeButton;

	/**
	 * Bouton 'Créer la table'.
	 */
	private JButton createTableButton;
	
	/**
	 * Bouton 'Modifier attribut'.
	 */
	private JButton updateAttributeButton;
	
	/**
	 * Bouton 'Supprimer attribut'.
	 */
	private JButton deleteAttributeButton;

	/**
	 * Contient tous les boutons.
	 */
	private JButton buttons[];

	/**
	 * Nombre de boutons.
	 */
	private final int buttonNumber = 5;
	
	
	// ==========================PANELS========================
	/**
	 * Panel Contenant le tableau.
	 */
	private JPanel panelAttributes;

	/**
	 * Contient tous les panels.
	 */
	private JPanel [] panels;

	/**
	 * Nombre de panels.
	 */
	private final int panelNumber = 1;
	
	
	// ==========================COMBOBOX========================
	/**
	 * ComboBox du choix du type de l'attribut.
	 */
	private JComboBox attributeTypeComboBox;

	/**
	 * Contient toutes les ComboBoxs.
	 */
	private JComboBox [] comboBox;

	/**
	 * Nombre de comboBox.
	 */
	private final int comboBoxNumber = 1;
	
	
	// ==========================CHECKBOXS========================
	/**
	 * 
	 */
	private JCheckBox notNullCheck;

	/**
	 * 
	 */
	private JCheckBox uniqueCheck;

	/**
	 * 
	 */
	private JCheckBox pkCheck;

	/**
	 * 
	 */
	private JCheckBox fkCheck;

	/**
	 * 
	 */
	private JCheckBox [] checkBoxs;

	/**
	 * 
	 */
	private final int checkBoxNumber = 4;



	


	/**
	 * Constructeur commun pour l'ihm de création de table.
	 * @param cm : objet ConnectionManager obtenu lors de la connexion.
	 */
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
	/**
	 * Instancie les boutons.
	 */
	private void createButtons()
	{
		this.buttons = new JButton [this.buttonNumber];
		this.buttons[0] = this.attributeButton = new JButton("Ajouter l'attribut") ;
		this.buttons[1] = this.createTableButton = new JButton("Créer la Table") ;
		this.buttons[2] = this.deleteAttributeButton = new JButton("Supprimer attribut") ;
		this.buttons[3] = this.updateAttributeButton = new JButton("Mofifier attribut") ;
		this.buttons[4] = this.resetButton = new JButton("Reset") ;

	}

	/**
	 * Positionne et dimensionne les boutons.
	 */
	private void bindButtons()
	{
		this.attributeButton.setBounds((int)(1.35*this.margin), (int)(0.37*height), 165, (int)(1.5*this.elementHeight));
		this.createTableButton.setBounds((int)(1.35*this.margin), (int)(0.87*height), 150, (int)(1.5*this.elementHeight));
		this.deleteAttributeButton.setBounds((int)(1.35*this.margin), (int)(0.80*height), 150, (int)(1.5*this.elementHeight));
		this.updateAttributeButton.setBounds((int)(1.35*this.margin)+ 160, (int)(0.80*height), 150, (int)(1.5*this.elementHeight));
		this.resetButton.setBounds(800, (int)(0.80*height), 80, (int)(1.5*this.elementHeight));
	}

	/**
	 * Gère les caractéristiques des boutons.
	 */
	private void initButtons()
	{
		this.deleteAttributeButton.setEnabled(false);
		this.updateAttributeButton.setEnabled(false);
	}
	
	/**
	 * Associe les boutons à this.
	 */
	private void addButtons()
	{
		for (JButton jb : this.buttons) {
			jb.addActionListener(this);
			this.add(jb);
		}
	}

	/**
	 * Instancie, positionne, dimensionne et associe les boutons.
	 */
	private void handleButtons()
	{
		this.createButtons();
		this.initButtons();
		this.bindButtons();
		this.addButtons();
	}



	// ==========================FIELDS========================
	/**
	 * Instancie les boîtes de saisie.
	 */
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

	/**
	 * Positionne et dimensionne les boîtes de saisie.
	 */
	private void bindFields()
	{
		this.tableNameField.setBounds((int)((1.35*this.margin)+140), (int)(0.09*height), 100, (int)(1.5*this.elementHeight));	
		this.attributeNameField.setBounds((int)(1.35*this.margin), (int)(0.28*height), 100, (int)(1.5*this.elementHeight));	
		this.attributeSizeField.setBounds((int)((1.35*this.margin)+210), (int)(0.28*height), 50, (int)(1.5*this.elementHeight));	
		this.fkTableNameField.setBounds((int)((1.35*this.margin)+675), (int)(0.28*height), 75, (int)(1.5*this.elementHeight));	
		this.fkAttributeNameField.setBounds((int)((1.35*this.margin)+765), (int)(0.28*height), 75, (int)(1.5*this.elementHeight));
	}
	
	/**
	 * Gère les caractéristiques des Fields.
	 */
	private void initFields()
	{
		this.attributeSizeField.addKeyListener(intKey);
		this.tableNameField.addKeyListener(stringKey);
		this.attributeNameField.addKeyListener(stringKey);
		this.fkTableNameField.addKeyListener(stringKey);
		this.fkAttributeNameField.addKeyListener(stringKey);		
		this.tableNameField.setDocument(fieldInputSize(30));
		this.attributeNameField.setDocument(fieldInputSize(64));
		this.attributeSizeField.setDocument(fieldInputSize(6));
		this.attributeNameField.setText("nomAttribut");
		this.attributeSizeField.setText("Taille");
		this.fkTableNameField.setText("nomTable");
		this.fkAttributeNameField.setText("nomAttribut");
		this.fkTableNameField.setEnabled(false);
		this.fkAttributeNameField.setEnabled(false);	
	}

	/**
	 * Associe les boîtes de saisie à this.
	 */
	private void addFields()
	{
		for (JTextField jtf : this.fields) {
			this.add(jtf);
		}
	}


	/**
	 * Instancie, positionne, dimensionne et associe les boîtes de saisie.
	 */
	private void handleFields()
	{
		this.createFields();
		this.initFields();
		this.bindFields();
		this.addFields();
	}

	// ==========================LABEL========================
	/**
	 * Instancie les labels.
	 */
	private void createLabels()
	{
		this.labels = new JLabel [this.labelNumber];
		this.labels[0] = this.tableLabel = new JLabel("Table :");
		this.labels[1] = this.attributeLabel = new JLabel("Attribut :");
		this.labels[2] = this.tableNameLabel= new JLabel("Nom de la table :");
		this.labels[3] = this.errorAttributesLabel= new JLabel("");
	}

	/**
	 * Positionne et dimensionne les labels.
	 */
	private void bindLabels()
	{
		this.tableLabel.setBounds(this.margin, (int)(0.03*height), 100, (int)(1.5*this.elementHeight));
		this.attributeLabel.setBounds(this.margin, (int)(0.20*height), 100, (int)(1.5*this.elementHeight));
		this.tableNameLabel.setBounds((int)(1.35*this.margin), (int)(0.09*height), 140, (int)(1.5*this.elementHeight));
		this.errorAttributesLabel.setBounds(300,(int)(0.37*height), 600, (int)(1.5*this.elementHeight));
	}

	/**
	 * Gère les caractéristiques des Fields.
	 */
	private void initLabels()
	{
		this.tableLabel.setFont(new Font(FONT, Font.BOLD, 18));
		this.attributeLabel.setFont(new Font(FONT, Font.BOLD, 18));
		this.tableNameLabel.setFont(new Font(FONT, Font.CENTER_BASELINE, 14));
		this.errorAttributesLabel.setFont(new Font(FONT, Font.CENTER_BASELINE, 14));
	}
	
	/**
	 * Associe les labels à this.
	 */
	private void addLabels()
	{
		for (JLabel jl : this.labels) {
			this.add(jl);
		}
	}

	/**
	 * Instancie, positionne, dimensionne et associe les labels.
	 */
	private void handleLabels()
	{
		this.createLabels();
		this.initLabels();
		this.bindLabels();
		this.addLabels();
	}

	// ==========================PANELS========================
	/**
	 * Instancie les panels.
	 */
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

	/**
	 * Positionne, dimensionne et gère les caractéristiques des panels.
	 */
	private void bindPanels()
	{
		this.panels[0].setBounds(0, (int)(0.45*height), width-5,  (int)(0.5*height)-100);
		this.tables[0].setFillsViewportHeight(true);
		this.tables[0].setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.scrollPanes[0].setVisible(true);
		this.panels[0].add(this.scrollPanes[0]);
	}
	
	/*private void initPanels(){
	
	}*/
	
	/**
	 * Associe les panels à this.
	 */
	private void addPanels()
	{
		for (JPanel jl : this.panels) {
			this.add(jl);
		}
	}

	/**
	 * Instancie, positionne, dimensionne et associe les panels.
	 */
	private void handlePanels()
	{
		this.createPanels();
		this.bindPanels();
		this.addPanels();
	}

	// ==========================COMBOBOX========================
	/**
	 * Instancie les comboBoxs.
	 */
	private void createComboBox()
	{
		this.comboBox = new JComboBox [this.comboBoxNumber];
		this.comboBox[0] = this.attributeTypeComboBox = new JComboBox(types);
	}


	/**
	 * Positionne et dimensionne les comboBoxs.
	 */
	private void bindComboBox()
	{
		this.attributeTypeComboBox.setBounds((int)((1.35*this.margin)+110), (int)(0.28*height), 85, (int)(1.5*this.elementHeight));
	}
	
	private void initComboBox(){
		this.attributeTypeComboBox.addActionListener (new ActionListener () {
		    public void actionPerformed(ActionEvent e) {
		    	  JComboBox comboBox = (JComboBox) e.getSource();

	                Object selected = comboBox.getSelectedItem();
	                if(selected.toString().equals("DATE")){
	                	setEnabledSizeField(false);
	                }else{
	                	setEnabledSizeField(true);
	                }
		    }
		});
	}

	/**
	 * Associe les comboBoxs à this.
	 */
	private void addComboBox()
	{
		for (JComboBox jc : this.comboBox) {
			this.add(jc);
		}
	}

	
	/**
	 * Instancie, positionne, dimensionne et associe les comboBoxs.
	 */
	private void handleComboBox()
	{
		this.createComboBox();
		initComboBox();
		this.bindComboBox();
		this.addComboBox();
	}

	// ==========================CHECKBOX========================
	/**
	 * Instancie les checkBoxs.
	 */
	private void createCheckBox()
	{
		this.checkBoxs = new JCheckBox [this.checkBoxNumber];
		this.checkBoxs[0] = this.notNullCheck = new JCheckBox("NOT NULL");
		this.checkBoxs[1] = this.uniqueCheck = new JCheckBox("UNIQUE");
		this.checkBoxs[2] = this.pkCheck = new JCheckBox("PRIMARY KEY");
		this.checkBoxs[3] = this.fkCheck = new JCheckBox("FOREIGN KEY");
	}

	/**
	 * Positionne et dimensionne les checkBoxs.
	 */
	private void bindCheckBox()
	{
		this.notNullCheck.setBounds((int)((1.35*this.margin)+265), (int)(0.28*height), 100, (int)(1.5*this.elementHeight));	
		this.uniqueCheck.setBounds((int)((1.35*this.margin)+365), (int)(0.28*height), 80, (int)(1.5*this.elementHeight));	
		this.pkCheck.setBounds((int)((1.35*this.margin)+442), (int)(0.28*height), 115, (int)(1.5*this.elementHeight));	
		this.fkCheck.setBounds((int)((1.35*this.margin)+555), (int)(0.28*height), 115, (int)(1.5*this.elementHeight));	
	}
	
	/**
	 * Gère les caractéristiques des checkBoxs.
	 */
	private void initCheckBox()
	{
		this.pkCheck.addItemListener(this);
		this.fkCheck.addItemListener(this);
	}
	
	/**
	 * Associe les checkBoxs à this.
	 */
	private void addCheckBox()
	{
		for (JCheckBox jl : this.checkBoxs) {
			this.add(jl);
		}
	}

	/**
	 * Instancie, positionne, dimensionne et associe les checkBoxs.
	 */
	private void handleCheckBox()
	{
		this.createCheckBox();
		this.initCheckBox();
		this.bindCheckBox();
		this.addCheckBox();
	}

	/**
	 * Définit certaines propriétés de l'IHM.
	 */
	private void setProperties()
	{
		this.setSize(width, height);
		this.setLocationRelativeTo(null); 
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  
		this.setVisible(true);    
		this.setResizable(false);
	}
	
	/**
	 * Réinit les champs des attributs.
	 */
	private void clearAttribute()
	{
		this.attributeNameField.setText("");
		this.attributeSizeField.setText("");
		this.notNullCheck.setSelected(false);
		this.uniqueCheck.setSelected(false);
		this.fkCheck.setSelected(false);
		this.pkCheck.setSelected(false);
	}
	
	/**
	 * Réinit tout les champs.
	 */
	public void resetView()
	{
		this.setValues("nomAttribut", "VARCHAR", "Taille", false, false, false, false, "nomTable", "nomAttribut");
		this.tableNameField.setText("");
		this.talk("");
		this.initButtons();
		this.models[0].removeAll();
	}
	
	
	/**
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
	 * Fonction pour savoir si un String est un entier
	 * @param i : un entier int
	 * @return boolean
	 */
	public boolean isInteger(String i)
	{
		try {
		    Integer.parseInt(i);
		    return true;
		} catch (NumberFormatException nfe) {
		    return false;
		}
	}
	
	/**
	 * Fonction pour savoir si tous les champs d'un attribut sont remplis
	 * @return boolean
	 */
	public boolean isCompleteAttribute()
	{
		if(this.attributeNameField.getText().equals((String)"") || this.attributeSizeField.getText().equals((String)"")){
			return false;
		}else{
			return true;
		}
	}
	
	/**
	 * Fonction pour savoir si tous les champs de la table sont remplis
	 * @return boolean
	 */
	public boolean isCompleteTable()
	{
		if(this.models[0].getRowCount()==0 || this.tableNameField.getText().equals("")){
			this.talk(errorAttribute+"Il n'y a pas d'Attribut OU Il manque le nom de la Table");
			return false;
		}else{
			return true;
		}
	}
	
	/**
	 * @param size : un entier int
	 * @return boolean
	 */
	public boolean isGoodSizeValue(int size)
	{
		if(size>0){
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * Fonction permetant de savoir si un String est un entier de bonne taille
	 * @param size : une chaine String
	 * @return boolean
	 */
	public boolean isGoodSize(String size)
	{
		/*
		 *TODO : Non, dans le cas des NUMBERS, la taille peut être décimale.
		 *par exemple, 10,2 veut dire "dix chiffres avant la virgule et deux après"
		 */
		if(isInteger(size)){
			if(isGoodSizeValue(Integer.parseInt(size))){
				return true;				
			}else{
				this.talk(errorAttribute + "La taille de l'attribut doit être supérieure à 0.");
				return false;
			}
		}else{
			this.talk(errorAttribute + "La taille de l'attribut doit être un entier.");
			return false;
		}
	}
	
	/**
	 * Fonction pour savoir si tous les champs de l'attribut sont valides
	 * @return boolean
	 */
	public Boolean isValidateAttributes()
	{
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
	
	/**
	 * Modifie l'état des attributs de modifications du tableau
	 * @param b : un boolean
	 */
	public void setEnableButtonUpdateDelete(boolean b){
		this.deleteAttributeButton.setEnabled(b);
		this.updateAttributeButton.setEnabled(b);
	}
	
	public void setEnabledSizeField(boolean b){
		if(b==true){
			this.attributeSizeField.setEnabled(b);
			this.attributeSizeField.setText("Taille");
		}else{
				this.attributeSizeField.setEnabled(b);
				this.attributeSizeField.setText("1");
		}
	}
	
	/**
	 * Ecrit un message dans le label des messages d'erreurs
	 * @param message : une chaine String
	 */
	public void talk(String message){
		this.errorAttributesLabel.setText("");
		this.errorAttributesLabel.setText(message);
	}
	
	/**
	 * Fonction permettant de modifier les champs d'un attribut.
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
	public void setValues(String name, String type, String size, boolean notNull, 
			boolean unique, boolean pk, boolean fk, 
			String fkTable, String fkAttribute)
	{
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
	
	/**
	 * Fonction pour connaitre l'index d'un Type dans la comboBox
	 * @param type : une chaine String
	 * @return int
	 */
	public int getIndexAttributeTypeComboBox(String type)
	{
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
	public void actionPerformed(ActionEvent e) 
	{
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
	
	public void itemStateChanged(ItemEvent item)
	{
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
	
	/**
	 * Metrre a jour un attribut.
	 */
	private void updateAttributeButtonAction()
	{
		this.talk("");
		int rowIndex = this.tables[0].getSelectedRow();
		Attribute a = this.models[0].getAttributeAt(rowIndex);
		this.setValues(a.name, a.type, Integer.toString(a.size), a.notNull, a.unique, a.primaryKey, a.foreignKey, a.fkTable,a.fkAttribute);
		this.models[0].removeAttributes(this.tables[0].getSelectedRow());
		this.setEnableButtonUpdateDelete(false);
	}
		
	/**
	 * Supprimer un attribut.
	 */
	private void deleteAttributeButtonAction()
	{
		this.models[0].removeAttributes(this.tables[0].getSelectedRow());
		this.talk(succesAttribute+"Attribut supprimé");
		this.setEnableButtonUpdateDelete(false);
	}
	
	/**
	 * Ajouter un attribut.
	 */
	private void addAttributeButtonAction()
	{
		if(isValidateAttributes()){
				if(fkCheck.isSelected()){
					Attribute a = new Attribute(attributeNameField.getText(),(String)attributeTypeComboBox.getSelectedItem(), Integer.parseInt(attributeSizeField.getText()), notNullCheck.isSelected(), uniqueCheck.isSelected(),pkCheck.isSelected(),fkCheck.isSelected(),fkTableNameField.getText(),fkAttributeNameField.getText());
					if (a.checkAttributes()>=0){
					int i = this.models[0].addAttribute(a);
					if( i == 0){
						this.talk(errorAttribute +"Un attribut existant a déja le même nom.");
					}else{
						this.talk(succesAttribute +"Attribut ajouté.");
						this.clearAttribute();
					}
					}else{
						this.talk(errorAttribute +a.errorAttributes(a.checkAttributes()));							
						}
					
					
				}else{
					Attribute a = new Attribute(attributeNameField.getText(),(String)attributeTypeComboBox.getSelectedItem(), Integer.parseInt(attributeSizeField.getText()), notNullCheck.isSelected(), uniqueCheck.isSelected(),pkCheck.isSelected(),fkCheck.isSelected(),"N/A","N/A");	
					if (a.checkAttributes()>=0){
						int i = this.models[0].addAttribute(a);
						if( i == 0){
							this.talk(errorAttribute +"Un attribut existant a déja le même nom.");
						}else{
							this.talk(succesAttribute +"Attribut ajouté.");
							this.clearAttribute();
						}
						}else{
							this.talk(errorAttribute +a.errorAttributes(a.checkAttributes()));							
							}
					}
				}
		}		
		
	}
	

