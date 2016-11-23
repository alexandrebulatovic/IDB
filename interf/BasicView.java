package interf;

import java.util.ArrayList;
import javax.swing.*;
import java.awt.LayoutManager;

@SuppressWarnings("serial")
public abstract class BasicView 
extends JFrame
implements IDBFrame
{

	//Attributs
	/**
	 * Hauteur de l'IHM.
	 */
	private final int height;
	
	/**
	 * Largeur de l'IHM.
	 */
	private final int width;
	
	/**
	 * Marge de l'IHM.
	 */
	private int elementLeft;
	
	/**
	 * Hauteur des éléments.
	 */
	private final int elementHeight = 40;
	
	/**
	 * Largeur des éléments.
	 */
	private int elementWidth;
	
	/**
	 * Incrément pour positionner les éléments sur la hauteur.
	 */
	private int elementTop;
	
	/**
	 * Etiquette pour dialoguer avec l'utilisateur.
	 */
	private JLabel messageLabel;
	
	/**
	 * Contients tous les composants de $this.
	 */
	protected ArrayList <JComponent> components;
	
	
	//Constructeur
	protected BasicView(String name, String title, LayoutManager lm,int width, int height)
	{
		super(name);
		this.setLayout(lm);
		this.components = new ArrayList<JComponent>();
		this.width = width;
		this.height = height;
		this.setDimension();
		this.components.add(this.messageLabel = new JLabel());
		this.bindElements(this.messageLabel);
	}
	
	
	//Protected
	/**
	 * Associe tous les composants de $this à $this.
	 */
	protected void addComponents()
	{
		for (JComponent jc : this.components) {
			this.add(jc);
		}
	}
	
	
	/**
	 * Dimensionne et positionne $element.
	 * Détermine l'emplacement du prochain élément.
	 * 
	 * @param element : un objet JComponent
	 */
	protected void bindElements(JComponent element)
	{
		element.setBounds(this.elementLeft, this.elementTop, this.elementWidth, this.elementHeight);
		this.increaseTop(this.elementHeight);
	}
	
	
	/**
	 * Dimensionne et positionne $element, avec une hauteur de $height pixels.
	 * Détermine l'emplacement du prochain élément.
	 * 
	 * @param element : un objet JComponent
	 * @param height :  un entier naturel > 0 et < à la hauteur de $this
	 */
	protected void bindElements(JComponent element, int height)
	{
		element.setBounds(this.elementLeft, this.elementTop, this.elementWidth, height);
		this.increaseTop(height);
	}
	

	/**
	 * Dimensionne et positionne $element, avec une largeur $nb fois 
	 * petite que par défaut.
	 * Si $alignNext est vrai, le prochain élément sera placé à droite de $element,
	 * sinon en bas.
	 * Lorsque cette méthode est appellée avec $alignNext vrai, il faut impérativement
	 * la réutiliser en série jusqu'à passer un faux.
	 * 
	 * @param element : un objet JComponenet
	 * @param nb : un entier naturel > 0
	 * @param alignNext : vrai pour aligner le prochain élément à droite,
	 * faux pour l'aligner en bas.
	 */
	protected void bindElements(JComponent element, int nb, boolean alignNext)
	{
		int width = (int) (this.elementWidth/nb);
		element.setBounds(
				this.elementLeft, this.elementTop, width, this.elementHeight);
		if (alignNext) {
			this.increaseLeft(width);
		}
		else{
			this.increaseTop(this.elementHeight);
			this.elementLeft = (int) (0.05 * this.width);
		}
	}
	
	
	/**
	 * Définit certaines propriétés de l'IHM.
	 */
	protected void setProperties()
	{
		this.setSize(this.width, this.height);
		this.setLocationRelativeTo(null); 
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  
		this.setVisible(true);    
		this.setResizable(false);
	}
	
	//Méthode
	/**
	 * {@inheritDoc}
	 */
	public void talk(String msg)
	{
		this.messageLabel.setText(msg);
	}
	
	
	//Privates
	/**
	 * Dimensionne l'IHM.
	 */
	private void setDimension()
	{
		this.elementTop = 0;
		this.elementLeft = (int) (0.05 * this.width);
		this.elementWidth = (int) (0.9 * this.width);
	}
	
	
	/**
	 * Incrémente la position du prochain élément 
	 * d'un peu plus de $add pixels en ordonnée.
	 * 
	 * @param add : un entier naturel > 0 et < la hauteur de $this.
	 */
	private void increaseTop(int add)
	{
		//TODO : Supprimer le +10 car risque d'erreur
		this.elementTop += add + 10;
	}
	
	
	/**
	 * Incrémente la position du prochain élément 
	 * d'un peu plus de $add pixels en abscisse.
	 * 
	 * @param add
	 */
	private void increaseLeft(int add)
	{
		//TODO : sSécuriser pour ne pas dépasser
		this.elementLeft += add +5;
	}
}
