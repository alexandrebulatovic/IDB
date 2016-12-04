package connect;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import create.MaxLengthTextDocument;
import interf.BasicGUI;

@SuppressWarnings("serial")
public class ConnectionGUI 
extends BasicGUI
implements ActionListener
{
	//Attributs
	/**Controleur de connexion.*/
	private ConnectionController control;

	/**Etiquette des pilôtes.*/
	private JLabel driverLabel;
	
	/**Liste déroulante des différents pilôtes de SGBD.*/
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
	 * Déclare un écouteur sur les touches pressés.
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
	public ConnectionGUI(ConnectionController cc)
	{
		super("Connexion", null, 450, 410, 20);
		this.control = cc;
		this.createAndBindComponents();
		this.handleFieldsSize();
		this.limitCharacters();
		this.listenFields();
		this.setDefaultValues();
		this.setProperties();
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
	
	
	//Privées
	private void createAndBindComponents()
	{
		//Pilôtes.
		this.driverLabel = new JLabel("Pilôte :");
		this.bindElements(this.driverLabel);
		
		this.driverCombo = new JComboBox<String>();
		//TODO : ajouter d'autres pilôtes
		//TODO : créer une classe statique avec des constantes sur les nom des pilôtes.
		this.driverCombo.addItem("Oracle");
		this.bindElements(this.driverCombo);
		
		//Adresse IP
		this.ipLabel = new JLabel("Adesse IP du serveur :");
		this.bindElements(this.ipLabel);
		
		this.ipField = new JTextField();
		this.bindElements(this.ipField);
		
		//Utilisateur
		this.userLabel = new JLabel("Nom d'utilisateur : ");
		this.bindElements(this.userLabel);
		
		this.userField = new JTextField();
		this.bindElements(this.userField);
		
		//Mot de passe
		this.passwordLabel = new JLabel("Mot de passe : ");
		this.bindElements(this.passwordLabel);
		
		this.passwordField = new JPasswordField();
		this.bindElements(this.passwordField);
		
		//Base de données
		this.bdLabel = new JLabel("Nom de base de données : ");
		this.bindElements(this.bdLabel);
		
		this.bdField = new JTextField();
		this.bindElements(this.bdField);
		
		//Numéro de port
		this.portLabel = new JLabel("Numéro de port : ");
		this.bindElements(this.portLabel);
		this.portField = new JTextField();
		this.bindElements(this.portField);
		
		//Bouton valider
		this.okButton = new JButton("Se connecter");
		this.okButton.setActionCommand("OK");
		this.okButton.addActionListener(this);
		this.bindElements(this.okButton, 40);
	}


	/**
	 * Gère les actions après un clic sur le bouton 'Valider'.
	 */
	private void okButtonAction()
	{
		if (!this.isComplete()) {
			this.talk("Erreur : les champs doivent être remplis.");
		}
		else {
			this.rawPassword();
			ConnectionStrings parameters = new ConnectionStrings(
					this.driverCombo.getSelectedItem().toString(),
					this.ipField.getText(),
					this.userField.getText(), 
					this.rawPassword(), 
					this.bdField.getText(), 
					this.portField.getText());
			this.control.connect(parameters);
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
	 * Inscrit des valeurs par défaut dans les boites de saisies.
	 */
	private void setDefaultValues()
	{
		DefaultValueManager dvm = new DefaultValueManager();
		this.ipField.setText(dvm.getUrl());
		this.userField.setText(dvm.getUser());
		this.bdField.setText(dvm.getDataBase());
		this.portField.setText(dvm.getPort());
	}
	
	
	/**
	 * Retourne une chaîne de caractères différente du mot de passe
	 * saisi si et seulement si ce dernier commence ou termine par un espace.
	 * Retourne le mot de passe saisi dans les autres cas.
	 * 
	 * @return String
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
