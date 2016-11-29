package menugraphique;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class MenuPrincipalView extends JFrame implements ActionListener{

	/**Hauteur de l'IHM.*/
	private final int height=500;

	/**Largeur de l'IHM.*/
	private final int width=400;

	/**Bouton "Créer une table".*/
	private JButton creerTable;

	/** Marge coin superieur gauche.*/
	private int margin = 20;

	public MenuPrincipalView(){
		super("Menu Principal");
		this.setProperties();
		this.handleButtons();
	}

	/** Instancie les boutons. */
	private void createButtons(){this.creerTable = new JButton("Créer une table");}

	/** Positionne et dimensionne les boutons. */
	private void bindButtons(){this.creerTable.setBounds(this.margin,this.margin, 200, 40);}

	/** Ajoute les boutons et leur action listener à la view. */
	private void addButtons(){	
		this.add(this.creerTable);
		this.creerTable.addActionListener(this);
	}

	/** Instancie, positionne, dimensionne et associe les boutons et leur listener à la view.*/
	private void handleButtons()
	{
		this.createButtons();
		this.bindButtons();
		this.addButtons();
	}



	/** Définit certaines propriétés de l'IHM */
	private void setProperties()
	{
		this.setLayout(null);
		this.setSize(this.width, this.height);
		this.setLocationRelativeTo(null); 
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  
		this.setVisible(true);    
		this.setResizable(false);
	}

	/**Gestionnaire d'évènements
	 * @param e : un événement attrapé par l'IHM.
	 */
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == this.creerTable) {
			// TODO
		}
	}
}
