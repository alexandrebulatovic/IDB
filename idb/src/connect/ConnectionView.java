package connect;

import java.awt.event.*;
import javax.swing.*;

/**
 * Gère la vue pour se connecter à un SGBD.
 */
public class ConnectionView 
extends JFrame 
implements ActionListener
{
	//Attributes
	/**
	 * Controlleur de connexion.
	 */
	private ConnectionController control;
	
	/**
	 * Hauteur de l'IHM.
	 */
	private final int height = 500;
	
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
	private final int margin = 30;
	
	/**
	 * Bouton 'Valider'.
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
	 * Boîte de saisie de l'adresse du SGBD.
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
	 * Contient toutes les boîtes de saisie.
	 */
	private JTextField [] fields;
	
	/**
	 * Nombre de boîtes de saisie.
	 */
	private final int fieldNumber = 3;
	
	/**
	 * Etiquette pour l'adresse du SGBD.
	 */
	private JLabel urlLabel;
	
	/**
	 * Etiquette pour le nom d'utilisateur.
	 */
	private JLabel userLabel;
	
	/**
	 * Etiquette pour le mot de passe.
	 */
	private JLabel passwordLabel;
	
	/**
	 * Etiquette pour communiquer.
	 */
	private JLabel messageLabel;
	
	/**
	 * Contient toutes les étiquettes
	 */
	private JLabel [] labels;
	
	/**
	 * Nombre d'étiquettes.
	 */
	private final int labelNumber = 4;
	
	
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
	 * @param e : un évènement attrapé par l'IHM.
	 */
	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource() == this.okButton) {
			this.okButtonAction();
		}
	}
	
	
	/**
	 * Retourne vrai si et seulement si l'une des boîtes de saisie
	 * de $this est vide, faux sinon.
	 * 
	 * @return boolean
	 */
	public boolean hasEmptyField()
	{
		for (JTextField jtf : this.fields) {
			if (jtf.getText().equals("")) return true;
		}
		return false;
	}
	
	
	//Privates
	private void setDimension()
	{
		this.elementWidth = (int) (0.7 * this.width);
	}
	
	
	/**
	 * Instancie les boîtes de saisies.
	 */
	private void createFields()
	{	
		this.fields = new JTextField [this.fieldNumber];
		this.fields[0] = this.urlField = new JTextField();
		this.fields[1] = this.userField = new JTextField();
		this.fields[2] = this.passwordField = new JTextField(); 	
	}
	
	
	/**
	 * Positionne et dimensionne les boîtes de saisie.
	 */
	private void bindFields()
	{
		this.urlField		.setBounds(this.margin, 40, this.elementWidth, this.elementHeight);
		this.userField		.setBounds(this.margin, 120, this.elementWidth, this.elementHeight);
		this.passwordField	.setBounds(this.margin, 200, this.elementWidth, this.elementHeight);
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
		this.labels[0] = this.urlLabel = new JLabel("URL du serveur : ");
		this.labels[1] = this.userLabel = new JLabel("Utilisateur : ");
		this.labels[2] = this.passwordLabel = new JLabel("Mot de passe : ");
		this.labels[3] = this.messageLabel = new JLabel("");
	}
	
	
	/**
	 * Positionne et dimensionne les étiquettes.
	 */
	private void bindLabels()
	{
		this.urlLabel.setBounds(this.margin, 20, this.elementWidth, this.elementHeight);
		this.userLabel.setBounds(this.margin, 100, this.elementWidth, this.elementHeight);
		this.passwordLabel.setBounds(this.margin, 180, this.elementWidth, this.elementHeight);
		this.messageLabel.setBounds(this.margin, 300, this.elementWidth, this.elementHeight);
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
		this.okButton.setBounds(this.margin, 250, this.elementWidth, 30);
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
		//this.setTitle(" Connexion "); 
		this.setSize(400, 400);
		this.setLocationRelativeTo(null); 
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  
		this.setVisible(true);    
		this.setResizable(false);
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
		//TO DO : extraire les valeurs par défaut depuis quelque part.
		this.urlField.setText("jdbc:oracle:thin:@162.38.222.149:1521:IUT");
	}
	
	
	/**
	 * Gère les actions après un clic sur le bouton 'Valider'.
	 */
	private void okButtonAction()
	{
		if (this.hasEmptyField()) {
			this.talk("Erreur : les champs doivent être remplis.");
		}
		else {
			ConnectionResponse response;
			StringBuilder msg = new StringBuilder();
			this.talk("Tentative de connexion...");
			response = this.control.connect(
					this.urlField.getText(), 
					this.userField.getText(), 
					this.passwordField.getText());
			if (! response.success()) msg.append("Erreur : ");
			this.talk(msg.toString() + response.message());
		}
	}
	
	
	/**
	 * Communique avec l'utilisateur en affichant $msg.
	 * 
	 * @param msg : un message à transmettre à l'utilisateur.
	 */
	private void talk(String msg)
	{
		this.messageLabel.setText(msg);
	}
}
