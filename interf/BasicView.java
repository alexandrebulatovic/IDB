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
	private int margin;
	
	/**
	 * Hauteur des éléments.
	 */
	private final int elementHeight = 40;
	
	/**
	 * Largeur des éléments.
	 */
	private int elementWidth;
	
	/**
	 * Etiquette pour dialoguer avec l'utilisateur.
	 */
	private JLabel messageLabel;
	
	/**
	 * Contients tous les composants de $this.
	 */
	protected ArrayList <JComponent> components;
	
	/**
	 * Incrément pour positionner les éléments sur la hauteur.
	 */
	protected int topElement;
	
	
	//Protected
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
	 * {@inheritDoc}
	 */
	public void talk(String msg)
	{
		this.messageLabel.setText(msg);
	}
	
	
	/**
	 * Dimensionne et positionne $element.
	 * Détermine l'emplacement du prochain élément.
	 * 
	 * @param element : un objet JComponent
	 */
	protected void bindElements(JComponent element)
	{
		element.setBounds(this.margin, this.topElement, this.elementWidth, this.elementHeight);
		this.increaseTopElement(this.elementHeight);
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
		element.setBounds(this.margin, this.topElement, this.elementWidth, height);
		this.increaseTopElement(height);
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
	
	
	/**
	 * Dimensionne l'IHM.
	 */
	private void setDimension()
	{
		this.topElement = 0;
		this.margin = (int) (0.05 * this.width);
		this.elementWidth = (int) (0.9 * this.width);
	}
	
	
	/**
	 * Incrémente la position du prochain élément de $add pixels.
	 * 
	 * @param add : un entier naturel > 0 et < la hauteur de $this.
	 */
	private void increaseTopElement(int add)
	{
		//TODO : Supprimer le +10 car risque d'erreur
		this.topElement += add + 10;
	}
}
