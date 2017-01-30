package connection;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import useful.ConnectionStrings;
import useful.Response;
import useful.MaxLengthTextDocument;
import gui.BasicGUI;
import home.HomeController;
import home.HomeGUI;


/**
 * IHM pour se connecter à un SGBD.
 * 
 * @author ALCANTUD Gaël
 * @author UGOLINI Romain
 *
 */
@SuppressWarnings("serial")
public class ConnectionGUI 
extends BasicGUI
implements ActionListener, ItemListener
{
	//Attributs
	/**Controleur principal de l'application.*/
	private HomeController control;

	/**Etiquette des pilotes.*/
	private JLabel driverLabel;
	
	/**Liste déroulante des différents pilotes de SGBD.*/
	private JComboBox<String> driverCombo;
	
	/**Etiquette de l'adresse IP.*/
	private JLabel ipLabel;
	
	/**Boite de saisie de l'adresse IP.*/
	private JTextField ipField;
	
	/**Etiquette du nom d'utilisateur.*/
	private JLabel userLabel;
	
	/**Boite de saisie du nom d'utilisateur.*/
	private JTextField userField;
	
	/**Etiquette du mot de passe.*/
	private JLabel passwordLabel;
	
	/**Boite de saisie du mot de passe.*/
	private JPasswordField passwordField;
	
	/**Etiquette du nom de base de données.*/
	private JLabel bdLabel;
	
	/**Boîte de saisie du nom de base de données.*/
	private JTextField bdField;
	
	/**Etiquette du numéro de port.*/
	private JLabel portLabel;
	
	/**Boîte de saisie du numéro de port.*/
	private JTextField portField;
	
	/**Bouton pour lancer la connexion.*/
	private JButton okButton;
	
	
	//Listener
	/**
	 * Déclare un écouteur sur les touches pressées.
	 */
	KeyListener keylistener = new KeyListener(){

        @Override
        public void keyTyped(KeyEvent e) {}

        @Override
        public void keyReleased(KeyEvent e) {}

        @Override
        public void keyPressed(KeyEvent e) {
        	if (e.getKeyCode()== KeyEvent.VK_ENTER) okButtonAction();
        }
	};
	
	
	//Constructeur
	/**
	 * Constructeur commun.
	 */
	public ConnectionGUI(HomeController control)
	{
		super("Connexion", null, 450, 410, 20);
		this.control = control;
		this.createAndBindComponents();
		this.handleFieldsSize();
		this.limitCharacters();
		this.listenFields();
		this.setDefaultValues(false);
		this.setProperties(EXIT_ON_CLOSE);
	}
	
	
	//Méthodes
	@Override
	public boolean isComplete() {
		for (JComponent jc : this.components) {
			if (jc.getClass().getName().equals("javax.swing.JTextField")){
				if (((JTextField)jc).getText().isEmpty())
					return false;
			}
		}
		return true;
	}


	@Override
	public void actionPerformed(ActionEvent e)
	{	
		if (e.getSource() == this.okButton) this.okButtonAction();
	}
	
	
	@Override
	public void itemStateChanged(ItemEvent e) {
		if (e.getSource() == this.driverCombo) this.setDefaultValues(true);
	}


	//Privées
	/**
	 * Instancie, dimensionne et positionne les différentes éléments de l'IHM.
	 * Associe certains composants à des écouteurs si besoin.
	 */
	private void createAndBindComponents()
	{
		//pilotes.
		this.driverLabel = new JLabel("Pilote :");
		this.bindAndAdd(this.driverLabel);
		
		this.driverCombo = new JComboBox<String>();
		//TODO : ajouter d'autres pilotes
		//TODO : créer une classe statique avec des constantes sur les nom des pilotes.
		this.driverCombo.addItem("Oracle");
		this.driverCombo.addItem("MySQL");
		this.bindAndAdd(this.driverCombo);
		this.driverCombo.addItemListener(this);
		
		//Adresse IP
		this.ipLabel = new JLabel("Adesse IP du serveur :");
		this.bindAndAdd(this.ipLabel);
		
		this.ipField = new JTextField();
		this.bindAndAdd(this.ipField);
		
		//Utilisateur
		this.userLabel = new JLabel("Nom d'utilisateur : ");
		this.bindAndAdd(this.userLabel);
		
		this.userField = new JTextField();
		this.bindAndAdd(this.userField);
		
		//Mot de passe
		this.passwordLabel = new JLabel("Mot de passe : ");
		this.bindAndAdd(this.passwordLabel);
		
		this.passwordField = new JPasswordField();
		this.bindAndAdd(this.passwordField);
		
		//Base de données
		this.bdLabel = new JLabel("Nom de base de données : ");
		this.bindAndAdd(this.bdLabel);
		
		this.bdField = new JTextField();
		this.bindAndAdd(this.bdField);
		
		//Numéro de port
		this.portLabel = new JLabel("Numéro de port : ");
		this.bindAndAdd(this.portLabel);
		this.portField = new JTextField();
		this.bindAndAdd(this.portField);
		
		//Bouton valider
		this.okButton = new JButton("Se connecter");
		this.okButton.setActionCommand("OK");
		this.okButton.addActionListener(this);
		this.bindAndAdd(this.okButton, 40);
	}


	/**
	 * Gère les actions après un clic sur le bouton 'Valider'.
	 */
	private void okButtonAction()
	{
		if (!this.isComplete()) {
			this.talk("Les champs doivent être remplis.");
		}
		else {
			ConnectionStrings parameters = new ConnectionStrings(
					this.driverCombo.getSelectedItem().toString(),
					this.ipField.getText(),
					this.userField.getText().trim(), 
					this.rawPassword(), 
					this.bdField.getText(), 
					this.portField.getText());
			
			Response response = this.control.connect(parameters);
			this.talk(response);
			if (response.hasSuccess()) {
				this.control.saveDefaultValue(parameters);
				this.dispose();
				new HomeGUI(this.control);
			}
		}
	}
	
	
	/**
	 * Pose des limitations de tailles sur les boites de saisies.
	 */
	private void handleFieldsSize()
	{
		setFieldSize(this.ipField, 15);
		setFieldSize(this.userField, 32);
		setFieldSize(this.bdField, 50);
		setFieldSize(this.passwordField, 50);
		setFieldSize(this.portField, 5);
	}
	
	
	/**
	 * Limite les caractères pouvant être saisis dans certaines boites de saisie.
	 */
	private void limitCharacters()
	{
		this.ipField.addKeyListener(
				new java.awt.event.KeyAdapter() {
			        public void keyTyped(KeyEvent evt) {
			        	char c = evt.getKeyChar();
			        	if (c==',') evt.consume();
			        }
				}
			);
		this.portField.addKeyListener(
				new java.awt.event.KeyAdapter() {
			        public void keyTyped(KeyEvent evt) {
			        	char c = evt.getKeyChar();
			        	if (c==',') evt.consume();
			        	if(c<'0' || c>'9'){
			        		evt.consume();
			        	}
			        }
				}
			);
	}
	
	
	/**
	 * Associe le listener déclaré dans la classe aux boites de saisie.
	 * Associe le listener déclaré dans la classe à la liste déroulante.
	 */
	private void listenFields()
	{
		for (JComponent jc: this.components) {
			if (jc.getClass().getName().equals("javax.swing.JTextField")
					|| jc.getClass().getName().equals("javax.swing.JPasswordField")) {
				jc.addKeyListener(keylistener);			
			}
		}
		this.driverCombo.addKeyListener(keylistener);
	}
	
	
	/**
	 * Inscrit les valeurs par défaut de la dernière connexion dans l'IHM.
	 * Si $combo est vrai, les valeurs sont prises en fonction du pilote
	 * affiché dans la liste déroulante, sinon en fonction de ce qui est écrit
	 * dans le fichier xml des valeurs.
	 * 
	 * @param combo : vrai si et seulement si les valeurs dépendent du pilote
	 * choisi dans la liste déroulante, faux sinon.
	 */
	private void setDefaultValues(boolean combo)
	{
		ConnectionStrings cs;
		if (combo) {
			
			cs = this.control.getDefaultValues(
					this.driverCombo.getSelectedItem().toString());
		}
		else {
			cs = this.control.getDefaultValues();
			this.driverCombo.setSelectedItem(cs.driver);
		}
		
		this.ipField.setText(cs.url);
		this.userField.setText(cs.user);
		this.bdField.setText(cs.baseName);
		this.portField.setText(cs.port);
	}
	
	
	/**
	 * @return Une chaîne de caractères différente du mot de passe
	 * saisi si et seulement si ce dernier commence ou termine par un espace.
	 * Retourne le mot de passe saisi dans les autres cas.
	 */
	private String rawPassword()
	{
		char [] tab = this.passwordField.getPassword();
		String result = new String(tab);
		if (result.startsWith(" ") || result.endsWith(" "))
			result += 'a';
		return result;
	}
	
	
	/**
	 * Limite le nombre de caractères saisissables dans $field à $size. 
	 * 
	 * @param field : une boite de saisie de $this.
	 * @param size : nombre de caractères maximum.
	 */
	private static void setFieldSize(JTextField field, int size)
	{
		MaxLengthTextDocument mltd = new MaxLengthTextDocument();
		mltd.setMaxChars(15);
		field.setDocument(mltd);
	}
}
