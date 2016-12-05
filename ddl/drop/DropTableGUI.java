package ddl.drop;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;

import ddl.DDLController;

import interf.BasicGUI;

@SuppressWarnings("serial")
public class DropTableGUI 
extends BasicGUI
implements ActionListener
{
	//Instance
	/** Instance singleton en cours.*/
	private static DropTableGUI INSTANCE;
	
	//Attributes
	/** Controleur pour supprimer des tables.*/
	private DDLController control;
	
	/** Liste déroulantes contenant les tables disponibles.*/
	private JComboBox<String> tableComboBox;
	
	/** Case à cocher pour forcer la suppression en cascade.*/
	private JCheckBox cascadeCheckBox;
	
	/** Bouton pour envoyer la requête de suppression.*/
	private JButton okButton;
	
	
	//Constructeur
	/**
	 * Constructeur lambda.
	 */
	private DropTableGUI()
	{
		super("Suppression de table", null, 400, 400, 30);
		INSTANCE = this;
		this.control = DDLController.getInstance();
		this.handleComponents();
		this.setProperties(DISPOSE_ON_CLOSE);
	}
	
	
	//Méthodes
	/**
	 * Retourne l'instance en cours si et seulement si elle existe déjà,
	 * retourne une nouvelle instance sinon.
	 * 
	 * @return 
	 */
	public static DropTableGUI getInstance()
	{
		if (INSTANCE == null) new DropTableGUI();
		return INSTANCE;
	}
	
	
	@Override
	public boolean isComplete() {
		// TODO Auto-generated method stub
		return false;
	}
	
	
	@Override
	public void windowClosing(WindowEvent arg0) {INSTANCE = null;}
	
	
	@Override
	public void actionPerformed(ActionEvent arg0) 
	{
		// TODO Auto-generated method stub	
	}


	//Privées
	/**
	 * Instancie, positionne et dimensionne les différents composants de $this.
	 */
	private void handleComponents()
	{
		//Liste déroulante
		this.tableComboBox = new JComboBox<String>();
		this.bindElements(this.tableComboBox);
		
		//Case à cocher.
		this.cascadeCheckBox = new JCheckBox("supression en cascade.");
		this.bindElements(this.cascadeCheckBox);

		//Bouton
		this.okButton = new JButton("Supprimer");
		this.okButton.addActionListener(this);
		this.bindElements(this.okButton);
	}
}
