package interf;

import java.util.ArrayList;
import javax.swing.*;

import java.awt.LayoutManager;
import java.awt.Rectangle;

@SuppressWarnings("serial")
public abstract class BasicView 
extends JFrame
implements IDBFrame
{
	//Attributs
	/**
	 * Nom de l'IHM.
	 */
	private String name;
	
	/**
	 * Hauteur de l'IHM.
	 */
	private int height;
	
	/**
	 * Largeur de l'IHM.
	 */
	private int width;
	
	/**
	 * Marge de l'IHM.
	 */
	private int elementLeft;
	
	/**
	 * Hauteur des éléments.
	 */
	private int elementHeight;
	
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
	protected BasicView(String name, LayoutManager lm, int width, int height, int elementHeight)
	{
		super(name);
		this.setLayout(lm);
		this.name = name;
		this.components = new ArrayList<JComponent>();
		this.setDimension(width, height, elementHeight);
		this.messageLabel = new JLabel();
		this.bindElements(this.messageLabel);
	}
	
	
	//Protected
	/**
	 * Dimensionne et positionne $element selon les attributs de $this.
	 * Associe $element à $this.
	 * Détermine l'emplacement du prochain élément.
	 * 
	 * @param element : un objet JComponent
	 */
	protected void bindElements(JComponent element)
	{
		Rectangle r = new Rectangle(
				this.elementLeft, this.elementTop, this.elementWidth, this.elementHeight);
		this.bindAndAdd(element, r);
		this.increaseTop(this.elementHeight);
	}
	
	
	/**
	 * Dimensionne et positionne $element selon les attributs de $this,
	 * sauf pour la hauteur qui est déterminé sur $height pixels.
	 * Associe $element à $this.
	 * Détermine l'emplacement du prochain élément.
	 * 
	 * @param element : un objet JComponent
	 * @param height :  un entier naturel > 0 et < à la hauteur de $this
	 */
	protected void bindElements(JComponent element, int height)
	{
		Rectangle r = new Rectangle(
				this.elementLeft, this.elementTop, this.elementWidth, height);
		this.bindAndAdd(element, r);
		this.increaseTop(height);
	}
	

	/**
	 * Dimensionne et positionne $element, avec une largeur $nb fois 
	 * plus petite que celle définit par les attributs de $this.
	 * Si $alignNext est vrai, le PROCHAIN élément sera placé à droite de $element,
	 * sinon en bas.
	 * Lorsque cette méthode est appellée avec $alignNext vrai, il faut impérativement
	 * la réutiliser en série jusqu'à passer un faux.
	 * Associe $element à $this.
	 * Détermine l'emplacement du prochain élément.
	 * 
	 * @param element : un objet JComponenet
	 * @param nb : un entier naturel > 0
	 * @param alignNext : vrai pour aligner le prochain élément à droite,
	 * faux pour l'aligner en bas.
	 */
	protected void bindElements(JComponent element, int nb, boolean alignNext)
	{
		int width = (int) (this.elementWidth/nb);
		Rectangle r = new Rectangle(
				this.elementLeft, this.elementTop, width, this.elementHeight);
		this.bindAndAdd(element, r);
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
	@Override
	public void talk(String msg)
	{
		this.messageLabel.setText(msg);
	}
	
	
	@Override
	public String toString()
	{
		int i = 0;
		StringBuilder result = new StringBuilder();
		result.append("IHM ");
		result.append(this.name + "\n");
		result.append("Nombre de composants : ");
		result.append(this.components.size() + "\n");
		for (JComponent jc : this.components) {
			result.append("Composant ");
			result.append(i + " : {");
			result.append(jc.getX() + ", ");
			result.append(jc.getY() + ", ");
			result.append(jc.getWidth() + ", ");
			result.append(jc.getHeight() + "}\n");
			i++;
		}
		return result.toString();
	}
	
	
	//Privates
	/**
	 * Dimensionne l'IHM.
	 */
	private void setDimension(int width, int height, int elementHeight)
	{
		this.width = width;
		this.height = height;
		this.elementHeight = elementHeight;
		this.elementTop = 0;
		this.elementLeft = (int) (0.05 * this.width);
		this.elementWidth = (int) (0.9 * this.width);
	}
	
	
	/**
	 * Dimensionne, positionne et associe $element à l'IHM,
	 * en fonction des valeurs de $r.
	 * 
	 * @param element : un objet JComponent
	 * @param r : un objet Rectangle
	 */
	private void bindAndAdd(JComponent element, Rectangle r)
	{
		element.setBounds(r);
		this.add(element);
		this.components.add(element);
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
