package connect;

import interf.IDBFrame;

import java.awt.event.*;

import javax.swing.*;

/**
 * Gère l'IHM pour se connecter à un SGBD.
 */
public class ConnectionView 
extends JFrame 
implements ActionListener,ItemListener, IDBFrame
{
	//Attributes
	/**
	 * Controlleur de connexion.
	 */
	private ConnectionController control;
	
	/**
	 * Hauteur de l'IHM.
	 */
	private final int height = 400;
	
	/**
	 * Largeur de l'IHM.
	 */
	private final int width = 500;
	
	/**
	 * Hauteur des éléments.
	 */
	private final int elementHeight = 20;
	
	
	/**
	 * Largeur des éléments.
	 */
	private int elementWidth;
	
	/**
	 * Marge à gauche des éléments.
	 */
	private int margin = 30;
	
	/**
	 * Bouton 'Connexion'.
	 */
	private JButton okButton;
	
	/**
	 * Contient tous les boutons.
	 */
	private JButton [] buttons;
	
	/**
	 * Nombre de boutons.
	 */
	private final int buttonNumber = 1;
	
	/**
	 * La JComboBox de choix des drivers
	 */
	private JComboBox driverCombo;
	
	/**
	 * Contiens toutes les JComboBox
	 */
	private JComboBox[]combos;
	
	/**
	 * nombre de JComboBox
	 */
	private final int comboNumber = 1;
	
	/**
	 * Boîte de saisie de l'adresse du SGBD.
	 * Valeur Hôte/ip
	 */
	private JTextField urlField;
	
	/**
	 * Boîte de saisie du nom d'utilisateur.
	 */
	private JTextField userField;
	
	/**
	 * Boite de saisie du mot de passe.
	 */
	private JTextField passwordField;
	
	/**
	 * Le nom de la BDD
	 */
	private JTextField baseNameField;
	
	/**
	 * Le numéro de port
	 */
	private JTextField portField;
	
	/**
	 * Contient toutes les boîtes de saisie.
	 */
	private JTextField [] fields;
	
	/**
	 * Nombre de boîtes de saisie.
	 */
	private final int fieldNumber = 5;
	
	/**
	 * Etiquette du label de choix des drivers;
	 */
	@SuppressWarnings("unused")
	private JLabel driverLabel;
	
	/**
	 * Etiquette pour l'adresse du SGBD.
	 */
	@SuppressWarnings("unused")
	private JLabel urlLabel;
	
	/**
	 * Etiquette pour le nom d'utilisateur.
	 */
	@SuppressWarnings("unused")
	private JLabel userLabel;
	
	/**
	 * Etiquette pour le mot de passe.
	 */
	@SuppressWarnings("unused")
	private JLabel passwordLabel;
	
	/**
	 * Eiquette pour le nom de la base
	 */
	@SuppressWarnings("unused")
	private JLabel baseLabel;
	
	/**
	 * Etiquette pour le port
	 */
	@SuppressWarnings("unused")
	private JLabel portLabel;
	
	/**
	 * Etiquette pour communiquer.
	 */
	@SuppressWarnings("unused")
	private JLabel messageLabel;
	
	/**
	 * Contient toutes les étiquettes
	 */
	private JLabel [] labels;
	
	/**
	 * Nombre d'étiquettes.
	 */
	private final int labelNumber = 7;
	
	
	//Constructeurs
	/**
	 * Constructeur commun pour l'ihm de connexion.
	 */
	public ConnectionView(ConnectionController cc)
	{
		super("Connexion");
		this.control = cc;
		this.setLayout(null);
		this.setDimension();
		this.handleCombos();
		this.handleFields();
		this.handleLabels();
		this.handleButtons();
		this.setDefaultValues();
		this.setProperties();
	}
	
	
	//Publics
	/**
	 * Gestionnaire d'évènements.
	 * 
	 * @param e : un événement attrapé par l'IHM.
	 */
	public void actionPerformed(ActionEvent e)
	{	
		if (e.getSource() == this.okButton) {
			this.okButtonAction();
		}
	}
	
	
	/**
	 * 
	 * @param e : événement
	 */
	public void itemStateChanged(ItemEvent e){
		if(e.getStateChange()==1){//le nouvel item
			if (e.getItem().toString().equals("Oracle")){
				this.setValuesOracle();
				
			}
			
		}

		
		
	}
	
	
	



	/**
	 * {@inheritDoc}
	 */
	public boolean isComplete()
	{
		for (JTextField jtf : this.fields) {
			if (jtf.getText().equals("") && jtf !=this.passwordField) return false;
		}
		return true;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	public void talk(String msg)
	{
		this.messageLabel.setText(msg);
	}
	
	
	//Privates
	/**
	 * 
	 */
	private void setDimension()
	{
		this.elementWidth = (int) (0.7 * this.width);
		this.margin = (int)((0.3*this.width)/2);
	}
	
	/**
	 * Instancie les combobox
	 */
	private void createCombos()
	{
		this.combos = new JComboBox[this.comboNumber];
		this.combos[0] = this.driverCombo = new JComboBox<String>();
		this.driverCombo.addItem("Oracle");
	}
	
	private void bindCombos(){
		driverCombo.setBounds(this.margin, this.elementHeight, this.elementWidth, this.elementHeight);
		
		
	}
	
	private void addCombos(){
		for (JComboBox<String> c : this.combos){
			c.addItemListener(this);
			this.add(c);
		}			
	}
	
	/**
	 * Instancie les boîtes de saisies.
	 */
	private void createFields()
	{	
		this.fields = new JTextField [this.fieldNumber];
		this.fields[0] = this.urlField = new JTextField();
		this.fields[1] = this.userField = new JTextField();
		this.fields[2] = this.passwordField = new JPasswordField(); 
		this.fields[3] = this.baseNameField = new JTextField();
		this.fields[4] = this.portField = new JTextField();
	}
	
	
	/**
	 * Positionne et dimensionne les boîtes de saisie.
	 */
	private void bindFields()
	{
		int foot = (int)(0.25*this.fieldNumber*this.elementHeight);
		foot*=2;//on ajoute la taille des labels
		int jump = this.elementHeight*4;//et ici le textBox
		
		for(final JTextField field : this.fields){
			field.setBounds(this.margin, jump-10, this.elementWidth, this.elementHeight);
			jump+=foot;				
		}
		/*Les méthodes statiques seront gardés en cas de rollback
		this.urlField		.setBounds(this.margin, jump, this.elementWidth, this.elementHeight);
		this.userField		.setBounds(this.margin, jump, this.elementWidth, this.elementHeight);
		this.passwordField	.setBounds(this.margin, 200, this.elementWidth, this.elementHeight);
		this.baseNameField  .setBounds(this.margin, 260, this.elementWidth, this.elementHeight);
		this.portField		.setBounds(this.margin, 310, this.elementWidth, this.elementHeight);
		*/
	}
	
	
	/**
	 * Associe les boîtes de saisie à l'IHM.
	 */
	private void addFields()
	{
		for (JTextField jtf : this.fields) {
			this.add(jtf);
		}
	}
	
	
	/**
	 * Instancie les étiquettes.
	 */
	private void createLabels()
	{
		this.labels = new JLabel [this.labelNumber];
		this.labels[0] = this.driverLabel = new JLabel("Pilote :");
		this.labels[1] = this.urlLabel = new JLabel("URL du serveur :");
		this.labels[2] = this.userLabel = new JLabel("Utilisateur :");
		this.labels[3] = this.passwordLabel = new JLabel("Mot de passe :");
		this.labels[4] = this.baseLabel = new JLabel("Nom de la Base de Données :");
		this.labels[5] = this.portLabel = new JLabel("Port du Serveur :");
		this.labels[6] = this.messageLabel = new JLabel("");
	}
	
	
	/**
	 * Positionne et dimensionne les étiquettes.
	 */
	private void bindLabels()
	{
		int foot = (int)(0.25*this.fieldNumber*this.elementHeight);
		foot*=2;
		int jump = 0;//et ici le textBox
		
		for(final JLabel label : this.labels){
			label.setBounds(this.margin, jump, this.elementWidth, this.elementHeight);
			jump+=foot;				
		}
		/*
		this.urlLabel.setBounds(this.margin, 20, this.elementWidth, this.elementHeight);
		this.userLabel.setBounds(this.margin, 100, this.elementWidth, this.elementHeight);
		this.passwordLabel.setBounds(this.margin, 180, this.elementWidth, this.elementHeight);
		this.messageLabel.setBounds(this.margin, 300, this.elementWidth, this.elementHeight);
		this.baseLabel.setBounds(this.margin, 300+60, elementWidth, elementHeight);
		this.portLabel.setBounds(this.margin, 300+60+60, elementWidth, elementHeight);
		*/
	}
	
	
	/**
	 * Associe les étiquettes à l'IHM.
	 */
	private void addLabels()
	{
		for (JLabel jl : this.labels) {
			this.add(jl);
		}
	}
	
	
	/**
	 * Instancie les boutons.
	 */
	private void createButtons()
	{
		this.buttons = new JButton [this.buttonNumber];
		this.buttons[0] = this.okButton = new JButton("Connexion");
		this.okButton.setActionCommand("OK");
	}
	
	
	/**
	 * Positionne et dimensionne les boutons.
	 */
	private void bindButtons()
	{
		this.okButton.setBounds(this.margin, (int)(0.75*this.height)+20, this.elementWidth, 30);
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
	 * Définit certaines propriétés de l'IHM.
	 */
	private void setProperties()
	{
		this.setSize(this.width, this.height);
		this.setLocationRelativeTo(null); 
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  
		this.setVisible(true);    
		this.setResizable(false);
	}
	
	private void handleCombos()
	{
		
		this.createCombos();
		this.bindCombos();
		this.addCombos();
	}
	
	
	/**
	 * Instancie, positionne, dimensionne et associe
	 * les boîtes de saisie.
	 */
	private void handleFields()
	{
		this.createFields();
		this.bindFields();
		this.addFields();
	}
	
	
	/**
	 * Instancie, positionne, dimensionne et associe
	 * les étiquettes.
	 */
	private void handleLabels()
	{
		this.createLabels();
		this.bindLabels();
		this.addLabels();
	}
	
	
	/**
	 * Instancie, positionne, dimensionne et associe
	 * les boutons.
	 */
	private void handleButtons()
	{
		this.createButtons();
		this.bindButtons();
		this.addButtons();
	}
	
	
	/**
	 * Inscrit des valeurs par défaut dans les boites de saisies.
	 * 
	 */
	private void setDefaultValues()
	{
//		DefaultValueManager dvm = new DefaultValueManager();
//		this.urlField.setText(dvm.getUrl());
//		this.userField.setText(dvm.getUser());
//		this.baseNameField.setText(dvm.getDataBase());
//		this.portField.setText(dvm.getPort());
		//TODO : extraire les valeurs par défaut depuis quelque part.
		//this.urlField.setText("jdbc:oracle:thin:@162.38.222.149:1521:IUT");
		
		this.setValuesOracle();
	}
	
	private void setValuesOracle() {
		// TODO Auto-generated method stub
		this.urlField.setText("162.38.222.149");
		this.portField.setText("1521");
		this.baseNameField.setText("IUT");
	}
	
	
	/**
	 * Gère les actions après un clic sur le bouton 'Valider'.
	 */
	private void okButtonAction()
	{
		if (! this.isComplete()) {
			this.talk("Erreur : les champs doivent être remplis.");
		}
		else {
			int port = Integer.parseInt(this.portField.getText());
			this.control.connect(
					this.driverCombo.getSelectedItem().toString(),
					this.urlField.getText(), 
					this.userField.getText(), 
					this.passwordField.getText(),
					this.baseNameField.getText(),
					port
					);
		}
	}
	
}
