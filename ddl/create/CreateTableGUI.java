package ddl.create;

import interf.ListeningGUI;

import java.awt.BorderLayout;
import java.awt.event.*;
import java.util.List;
import java.awt.Font;

import javax.swing.*;

import useful.CustomizedResponseWithData;
import useful.MaxLengthTextDocument;
import ddl.Attribute;
import ddl.DDLController;
import ddl.Table;

@SuppressWarnings("serial")
/**
 * IHM pour créer des tables dans la base de données.
 * 
 * @author MAURY Adrian
 */
public class CreateTableGUI
extends ListeningGUI 
implements ActionListener, ItemListener
{
	// ==========================VARIABLES========================
	private static CreateTableGUI INSTANCE;

	private static final String FONT = null;

	/**
	 * Controleur lié à l'IHM.
	 */
	private DDLController control;

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

	private CustomizedResponseWithData<String> res;
	/**
	 * 
	 */
	private Object[] types = new Object[]{"VARCHAR", "NUMBER", "DATE", "CHAR"};

	private DefaultComboBoxModel foreignKeyTableComboBoxModel;

	private DefaultComboBoxModel foreignKeyAttributeComboBoxModel;

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
	 * Contient toutes les boîtes de saisie.
	 */
	private JTextField [] fields;

	/**
	 * Nombre de boîtes de saisie.
	 */
	private final int fieldNumber = 3;

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

	private JButton upPositionAttributeButton;

	private JButton downPositionAttributeButton;
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
	private final int buttonNumber = 7;


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
	private JComboBox<String> attributeTypeComboBox;

	private JComboBox<String> fkAtrributeNameComboBox;

	private JComboBox<String> fkTableNameComboBox;

	/**
	 * Contient toutes les ComboBoxs.
	 */
	private JComboBox [] comboBox;

	/**
	 * Nombre de comboBox.
	 */
	private final int comboBoxNumber = 3;


	// ==========================CHECKBOXS========================
	/**
	 * 
	 */
	private JCheckBox notNullCheckBox;

	/**
	 * 
	 */
	private JCheckBox uniqueCheckBox;

	/**
	 * 
	 */
	private JCheckBox primaryKeyCheckBox;

	/**
	 * 
	 */
	private JCheckBox foreignKeyCheckBox;

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
	protected CreateTableGUI()

	{
		super("Création de table");
		INSTANCE = this;
		this.control = DDLController.getInstance();
		this.setLayout(null);
		this.handlePanels();
		this.handleComboBox();
		this.handleFields();
		this.handleLabels();
		this.handleButtons();
		this.handleCheckBox();
		this.setProperties();
		this.addWindowListener(this);
	}

	/**
	 * Retourne l'IHM active si et seulement si elle existe déjà.
	 * Retourne une nouvelle IHM sinon.
	 * 
	 * @return CreateTableView
	 */
	public static CreateTableGUI getInstance()
	{
		if (INSTANCE == null) new CreateTableGUI();
		return INSTANCE;
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
		this.buttons[5] = this.downPositionAttributeButton = new JButton("DOWN") ;
		this.buttons[6] = this.upPositionAttributeButton = new JButton("UP") ;

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
		this.upPositionAttributeButton.setBounds(575, (int)(0.80*height), 75, (int)(1.5*this.elementHeight));
		this.downPositionAttributeButton.setBounds(665, (int)(0.80*height), 75, (int)(1.5*this.elementHeight));
	}

	/**
	 * Gère les caractéristiques des boutons.
	 */
	private void initButtons()
	{
		this.setEnableButtonUpdateDeleteUpDown(false);
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
	}

	KeyAdapter intKey = new KeyAdapter() {
		public void keyTyped(KeyEvent evt) {
			char c = evt.getKeyChar();
			if (c >= '0' && c <= '9') {
			} else {
				evt.consume();
			}
		}
	};

	KeyAdapter stringKey = new KeyAdapter() {
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
	}

	/**
	 * Gère les caractéristiques des Fields.
	 */
	private void initFields()
	{
		this.attributeSizeField.addKeyListener(intKey);
		this.tableNameField.addKeyListener(stringKey);
		this.attributeNameField.addKeyListener(stringKey);	
		this.tableNameField.setDocument(fieldInputSize(30));
		this.attributeNameField.setDocument(fieldInputSize(29));
		this.attributeSizeField.setDocument(fieldInputSize(3));
		this.attributeNameField.setText("nomAttribut");
		this.attributeSizeField.setText("Taille");
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
		this.foreignKeyAttributeComboBoxModel = new DefaultComboBoxModel();
		this.foreignKeyAttributeComboBoxModel.addElement("Nom Attribut");
		this.foreignKeyTableComboBoxModel = new DefaultComboBoxModel();
		this.foreignKeyTableComboBoxModel.addElement("Nom Table");
		this.comboBox[1] = this.fkTableNameComboBox = new JComboBox(foreignKeyTableComboBoxModel);
		this.comboBox[2] = this.fkAtrributeNameComboBox = new JComboBox(foreignKeyAttributeComboBoxModel);
	}


	/**
	 * Positionne et dimensionne les comboBoxs.
	 */
	private void bindComboBox()
	{
		this.attributeTypeComboBox.setBounds((int)((1.35*this.margin)+110), (int)(0.28*height), 85, (int)(1.5*this.elementHeight));
		this.fkTableNameComboBox.setBounds((int)((1.35*this.margin)+670), (int)(0.28*height), 85, (int)(1.5*this.elementHeight));	
		this.fkAtrributeNameComboBox.setBounds((int)((1.35*this.margin)+765), (int)(0.28*height), 85, (int)(1.5*this.elementHeight));
	}

	private void initComboBox(){
		this.fkTableNameComboBox.setEnabled(false);
		this.fkAtrributeNameComboBox.setEnabled(false);
		this.fkTableNameComboBox.addActionListener(this);
		this.attributeTypeComboBox.addActionListener(this);
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
		this.checkBoxs[0] = this.notNullCheckBox = new JCheckBox("NOT NULL");
		this.checkBoxs[1] = this.uniqueCheckBox = new JCheckBox("UNIQUE");
		this.checkBoxs[2] = this.primaryKeyCheckBox = new JCheckBox("PRIMARY KEY");
		this.checkBoxs[3] = this.foreignKeyCheckBox = new JCheckBox("FOREIGN KEY");
	}

	/**
	 * Positionne et dimensionne les checkBoxs.
	 */
	private void bindCheckBox()
	{
		this.notNullCheckBox.setBounds((int)((1.35*this.margin)+265), (int)(0.28*height), 100, (int)(1.5*this.elementHeight));	
		this.uniqueCheckBox.setBounds((int)((1.35*this.margin)+365), (int)(0.28*height), 80, (int)(1.5*this.elementHeight));	
		this.primaryKeyCheckBox.setBounds((int)((1.35*this.margin)+442), (int)(0.28*height), 115, (int)(1.5*this.elementHeight));	
		this.foreignKeyCheckBox.setBounds((int)((1.35*this.margin)+555), (int)(0.28*height), 115, (int)(1.5*this.elementHeight));	
	}

	/**
	 * Gère les caractéristiques des checkBoxs.
	 */
	private void initCheckBox()
	{
		this.primaryKeyCheckBox.addItemListener(this);
		this.foreignKeyCheckBox.addItemListener(this);
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
	{	this.setAutoRequestFocus(true);
	this.setSize(width, height);
	this.setLocationRelativeTo(null); 
	this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);  
	this.setVisible(true);    
	this.setResizable(false);
	}

	/**
	 * Réinitialise les champs des attributs.
	 */
	private void clearAttribute()
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
	 * Ecrit un message dans le label des messages d'erreurs.
	 * 
	 * @param message : une chaine String
	 */
	public void talk(String message){
		this.errorAttributesLabel.setText("");
		this.errorAttributesLabel.setText(message);
	}

	/**
	 * Réinitialise tous les champs de la vue.
	 */
	public void resetView()
	{
		this.setAttributesValues("nomAttribut", "VARCHAR", "Taille", false, false, false, false, "nomTable", "nomAttribut");
		this.tableNameField.setText("");
		this.talk("");
		this.initButtons();
		this.models[0].removeAll();
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
			this.addAttributeToTable(a);
		}
		this.setTableName(tableName);
	}

	/**
	 * Modifie l'état des attributs de modifications du tableau 
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
		if(this.attributeNameField.getText().equals((String)"") 
				|| this.attributeSizeField.getText().equals((String)"") 
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
		if(this.models[0].getRowCount()==0 || this.tableNameField.getText().equals("")){
			this.talk(errorAttribute+"Il n'y a pas d'Attribut OU Il manque le nom de la Table");
			return false;
		}else{
			return true;
		}
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
		if(b==true){
			this.attributeSizeField.setEnabled(b);
			this.attributeSizeField.setText("Taille");
		}else{
			this.attributeSizeField.setEnabled(b);
			this.attributeSizeField.setText("1");
		}
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
		this.foreignKeyAttributeComboBoxModel.setSelectedItem(fkAttribute);
		this.foreignKeyTableComboBoxModel.setSelectedItem(fkTable);
	}

	/**
	 * Retourne l'index de String dans la ComboBox des types
	 * d'un attribut. 
	 * 0 si VARCHAR, 1 si NUMBER, 2 si DATE, 3 si CHAR.
	 * 
	 * @param type : une chaine String corespondant au type d'un attribut
	 * @return int
	 */
	private int getIndexAttributeTypeComboBox(String type)
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
		CustomizedResponseWithData<String> result = control.getPrimaryKey(table);
		//TODO : Il faut controler qu'il n'y ait pas eu d'erreur ! 
		//TODO : le for each boucle sur un null si erreur !
		for (String s : result.getCollection()) {
			this.foreignKeyAttributeComboBoxModel.addElement(s);
		}
	}

	/**
	 * Ajoute l'attribut passé en paramètre à la table 
	 * contenant les attributs.
	 * 
	 * @param attribute : un objet Attribute
	 */
	private void addAttributeToTable(Attribute attribute){
		if (attribute.checkSizeAttributes()>=0){
			int i = this.models[0].addAttribute(attribute);
			if( i == 0){
				this.talk(errorAttribute +"Un attribut existant a déja le même nom.");
			}else{
				this.talk(succesAttribute +"Attribut ajouté.");
				this.clearAttribute();
			}
		}else{
			this.talk(this.errorAttribute +attribute.attributeSizeError(attribute.checkSizeAttributes()));							
		}
	}

	/**
	 * Détermine ce qu'il se passe lors d'une action sur
	 * le bouton "UP" ou le bouton "DOWN".
	 */
	private void positionAttributButtonAction(String direction){
		if(this.tables[0].getSelectedRow()!=-1){
			int rowIndex = this.tables[0].getSelectedRow();
			if(direction.equals("UP") && rowIndex!=0){
				this.models[0].changeAttributePosition(direction, rowIndex);
				this.tables[0].setRowSelectionInterval(rowIndex-1, rowIndex-1);
			}
			if(direction.equals("DOWN") && rowIndex!=this.tables[0].getRowCount()-1){
				this.models[0].changeAttributePosition(direction, rowIndex);
				this.tables[0].setRowSelectionInterval(rowIndex+1, rowIndex+1);
			}
		}
	}

	/**
	 * Détermine ce qu'il se passe lors d'une action sur
	 * le bouton "Ajouter l'attribut".
	 */
	private void addAttributeButtonAction()
	{
		if(this.isCompleteAttribute()){
			if(foreignKeyCheckBox.isSelected()){
				Attribute a = new Attribute(attributeNameField.getText(),
						(String)attributeTypeComboBox.getSelectedItem(), 
						Integer.parseInt(attributeSizeField.getText()), 
						this.notNullCheckBox.isSelected(), 
						this.uniqueCheckBox.isSelected(),
						this.primaryKeyCheckBox.isSelected(),
						this.foreignKeyCheckBox.isSelected(),
						this.foreignKeyTableComboBoxModel.getSelectedItem().toString(),
						this.foreignKeyAttributeComboBoxModel.getSelectedItem().toString());
				if (a.checkSizeAttributes()>=0){
					int i = this.models[0].addAttribute(a);
					if( i == 0){
						this.talk(errorAttribute +"Un attribut existant a déja le même nom.");
					}else{
						this.talk(succesAttribute +"Attribut ajouté.");
						this.clearAttribute();
					}
				}else{
					this.talk(errorAttribute +a.attributeSizeError(a.checkSizeAttributes()));							
				}
			}else{
				Attribute a = new Attribute(attributeNameField.getText(),(String)attributeTypeComboBox.getSelectedItem(), Integer.parseInt(attributeSizeField.getText()), notNullCheckBox.isSelected(), uniqueCheckBox.isSelected(),primaryKeyCheckBox.isSelected(),foreignKeyCheckBox.isSelected(),"N/A","N/A");	
				this.addAttributeToTable(a);
			}
		}
	}

	/**
	 * Détermine ce qu'il se passe lors de la sélection d'une DATE
	 * dans la ComboBox du type de l'attribut.
	 */
	private void selectSizeDateComboBoxAction() {
		Object selected = this.attributeTypeComboBox.getSelectedItem();
		if(selected.toString().equals("DATE")){
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
			if(!(selected.toString().equals("Nom Attribut"))){
				this.initComboBoxFkAttributeName(selected.toString());
			}
		}
	}

	/**
	 * Détermine ce qu'il se passe lors d'une action sur
	 * le bouton "Modifier l'attribut".
	 */
	private void updateAttributeButtonAction()
	{
		this.talk("");
		int rowIndex = this.tables[0].getSelectedRow();
		Attribute a = this.models[0].getAttributeAt(rowIndex);
		this.setAttributesValues(a.name, a.type, Integer.toString(a.size), a.notNull, a.unique, a.primaryKey, a.foreignKey, a.fkTable,a.fkAttribute);
		this.tables[0].getSelectedRow();
		this.models[0].removeAttributes(this.tables[0].getSelectedRow());
		this.setEnableButtonUpdateDeleteUpDown(false);
	}

	/**
	 * Détermine ce qu'il se passe lors d'une action sur
	 * le bouton "Supprimer l'attribut".
	 */
	private void deleteAttributeButtonAction()
	{
		this.models[0].removeAttributes(this.tables[0].getSelectedRow());
		this.talk(succesAttribute+"Attribut supprimé");
		this.setEnableButtonUpdateDeleteUpDown(false);
	}

	@Override
	public void windowClosing(WindowEvent we){INSTANCE = null;}


	@Override
	public void actionPerformed(ActionEvent e) 
	{
		if (e.getSource() == this.attributeButton) {
			this.addAttributeButtonAction();
		}
		if (e.getSource() == this.createTableButton) {
			if(this.isCompleteTable()){
				this.control.createTable(new Table(
						this.tableNameField.getText(),
						this.models[0].getAttributes()));
			}
		}
		if (e.getSource() == this.deleteAttributeButton) {
			this.deleteAttributeButtonAction();
		}
		if (e.getSource() == this.updateAttributeButton) {
			this.updateAttributeButtonAction();
		}
		if (e.getSource() == this.resetButton) {
			this.resetView();
		}
		if (e.getSource() == this.upPositionAttributeButton) {
			this.positionAttributButtonAction("UP");
		}
		if (e.getSource() == this.downPositionAttributeButton) {
			this.positionAttributButtonAction("DOWN");
		}
		if (e.getSource() == this.attributeTypeComboBox) {
			selectSizeDateComboBoxAction();
		}
		if (e.getSource() == this.fkTableNameComboBox) {
			selectForeignKeyTableComboBoxAction();
		}
	}

	@Override
	public void itemStateChanged(ItemEvent item)
	{
		Object obj = item.getItem();
		int status = item.getStateChange();
		if((JCheckBox)obj==this.primaryKeyCheckBox){
			if (status == ItemEvent.SELECTED){
				this.setEnabledSelectedUniqueNotNullCheckBox(false);
			}else if(status == ItemEvent.DESELECTED){
				this.setEnabledSelectedUniqueNotNullCheckBox(true);
			}
		}
		if((JCheckBox)obj==this.foreignKeyCheckBox){
			if (status == ItemEvent.SELECTED){
				this.initComboBoxFkTableAttributte(true);
			}else if(status == ItemEvent.DESELECTED){
				this.initComboBoxFkTableAttributte(false);
			}
		}
	}

}


