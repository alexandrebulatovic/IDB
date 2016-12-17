package interf;

import java.util.ArrayList;
import javax.swing.*;

import useful.Response;

import java.awt.LayoutManager;
import java.awt.Rectangle;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

/**
 * Propose un moyen simple de créer des vues simples. <br/><br/>
 * L'objectif de cette classe abstraite est de permettre à ses classes filles de ne plus
 * se soucier : <br/>
 * -de la liaison avec l'IHM, <br/>
 * -des coordonnées, <br/>
 * -et des dimensions de chaque composant placé sur l'IHM.<br/><br/>
 * 
 *  Pour cela, elle met à disposition la méthode 'bindElement()' et ses différentes surcharges.
 *  Chacune de ces méthodes effectue les opérations évoquées précedemment et limitent l'implication
 *  du développeur. Chaque appel à une de ces méthodes place automatiquement le composant concerné
 *  à un endroit différent du précédent composant. La largeur des composants est gérée 
 *  dynamiquement en fonction de la taille de l'IHM.<br/><br/>
 *  
 *  Toutefois ces méthodes ne vérifient pas que l'IHM soit suffisamment grande pour accueillir tous les 
 *  composants. C'est au programmeur de calibrer la taille initiale de l'IHM. <br/><br/>
 *  
 *  En plus de cela, cette classe redéfinit toutes les méthodes de l'interface WindowListener,
 *  et force la classe à "s'écouter" elle-même. De ce fait, les classes filles n'ont pas besoin de
 *  réécrire toutes les méthodes de l'interface WindowListener, et peuvent se contenter de
 *  redéfinir uniquement la ou les méthodes voulues.<br/><br/>
 *  
 *  Toute classe fille qui spécialise cette classe abstraite doit terminer son constructeur
 *  par un appel de la méthode 'setProperties()'.
 * 
 * @author UGOLINI Romain
 */
@SuppressWarnings("serial")
public abstract class BasicGUI 
extends JFrame
implements IDBGUI, WindowListener, ActionListener
{
	//Attributs
	/** Nom de l'IHM.*/
	private String name;
	
	/** Hauteur de l'IHM.*/
	private int height;
	
	/** Largeur de l'IHM.*/
	private int width;
	
	/** Marge de l'IHM.*/
	private int componentLeft;
	
	/** Hauteur des composants.*/
	private int componentHeight;
	
	/** Largeur des composants.*/
	private int componentWidth;
	
	/** Incrément pour positionner les composants sur la hauteur.*/
	private int componentTop;
	
	/** Etiquette pour dialoguer avec l'utilisateur.*/
	private JLabel messageLabel;
	
	/** Contients tous les composants de l'IHM.*/
	protected ArrayList <JComponent> components;
	
	
	//Constructeur
	/**
	 * Constructeur commun.
	 * 
	 * @param name : nom de l'IHM, null interdit.
	 * @param lm : gestionnaire de bordure, null autorisé.
	 * @param width : largeur de l'IHM, 0 < width.
	 * @param height : hauteur de l'IHM, 0 < height.
	 * @param componentHeight : hauteur par défaut des composants de l'IHM, 0 < componentHeight.
	 */
	protected BasicGUI(String name, LayoutManager lm, int width, int height, int componentHeight)
	{
		super(name);
		this.setLayout(lm);
		this.setDimension(width, height, componentHeight);
		
		this.name = name;
		this.components = new ArrayList<JComponent>();
		this.messageLabel = new JLabel();
		this.bindElement(this.messageLabel);
		this.addWindowListener(this);
	}
	
	
	//Protected
	/**
	 * Dimensionne $component avec une hauteur par défaut et 
	 * une largeur calculée en fonction de celle de l'IHM. <br/>
	 * Positionne $component en dessous du précédent composant.<br/>
	 * Associe $component à l'IHM.<br/>
	 * Détermine l'emplacement du prochain composant.
	 * 
	 * @param component : un objet JComponent, null interdit.
	 */
	protected void bindElement(JComponent component)
	{
		Rectangle r = new Rectangle(
				this.componentLeft, this.componentTop, this.componentWidth, this.componentHeight);
		this.bindAndAdd(component, r);
		this.increaseTop(this.componentHeight);
	}
	
	
	/**
	 * Dimensionne $component avec une hauteur $height et 
	 * une largeur calculée en fonction de celle de l'IHM. <br/>
	 * Positionne $component en dessous du précédent composant.<br/>
	 * Associe $component à l'IHM.<br/>
	 * Détermine l'emplacement du prochain composant.
	 * 
	 * @param component : un objet JComponent, null interdit.
	 * @param height :  un entier naturel > 0 et < à la hauteur de l'IHM.
	 */
	protected void bindElement(JComponent component, int height)
	{
		Rectangle r = new Rectangle(
				this.componentLeft, this.componentTop, this.componentWidth, height);
		this.bindAndAdd(component, r);
		this.increaseTop(height);
	}
	

	/**
	 * Dimensionne $component avec une hauteur par défaut et 
	 * une largeur $nb fois plus petite que celle habituellement calculée 
	 * avec la largeur de l'IHM. <br/><br/>
	 * Positionne $component à droite du précédent composant si et seulement
	 * si le précédent appel de cet méthode avait $alignNext vrai,
	 * positionne $component en dessous du précédent composant sinon.<br/><br/>
	 * Associe $component à l'IHM.<br/><br/>
	 * Détermine l'emplacement du prochain composant.
	 * 
	 * @param component : un objet JComponent, null interdit.
	 * @param nb : un entier naturel > 0.
	 * @param alignNext : vrai si et seulement si le prochain composant doit
	 * être aligné à droite, faux sinon.
	 */
	protected void bindElement(JComponent component, int nb, boolean alignNext)
	{
		int width = this.componentWidth/nb;
		Rectangle r = new Rectangle(
				this.componentLeft, this.componentTop, width, this.componentHeight);
		this.bindAndAdd(component, r);
		if (alignNext) {
			this.increaseLeft(width);
		}
		else{
			this.increaseTop(this.componentHeight);
			this.componentLeft = (int) (0.05 * this.width);
		}
	}
	
	
	/**
	 * Définit certaines propriétés de l'IHM : <br/>
	 * -les dimensions (de l'IHM et ses composants),<br/>
	 * -la position relative,<br/>
	 * -ce que doit faire le système après un clic sur la croix,<br/>
	 * Affiche l'IHM.<br/>
	 * N'autorise pas le redimensionnement.<br/>
	 * 
	 * Cette méthode doit être appelée en fin  
	 * de construction d'une instance de classe fille.<br/>
	 * 
	 * @param closeOperation : constante pour indiquer ce qu'il
	 * se passe en cliquant sur la croix.
	 */
	protected void setProperties(int closeOperation)
	{
		this.setSize(this.width, this.height);
		this.setLocationRelativeTo(null); 
		this.setDefaultCloseOperation(closeOperation);  
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
	public void talk(Response response)
	{
		this.messageLabel.setText(response.toString());	
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
	private void setDimension(int width, int height, int componentHeight)
	{
		this.width = width;
		this.height = height;
		this.componentHeight = componentHeight;
		this.componentTop = 0;
		this.componentLeft = (int) (0.05 * this.width);
		this.componentWidth = (int) (0.9 * this.width);
	}
	
	
	/**
	 * Dimensionne, positionne et associe $component à l'IHM,
	 * en fonction des valeurs de $r.
	 * 
	 * @param component : un objet JComponent
	 * @param r : un objet Rectangle
	 */
	private void bindAndAdd(JComponent component, Rectangle r)
	{
		component.setBounds(r);
		this.add(component);
		this.components.add(component);
	}


	/**
	 * Incrémente la position du prochain composant d'un peu plus de $add pixels en ordonnée.
	 * 
	 * @param add : 0 < add < hauteur de l'IHM.
	 */
	private void increaseTop(int add){this.componentTop += add +5;}
	
	
	/**
	 * Incrémente la position du prochain composant d'un peu plus de $add pixels en abscisse.
	 * 
	 * @param add : 0 < add < largeur de l'IHM.
	 */
	private void increaseLeft(int add){this.componentLeft += add +5;}
	
	
	@Override
	public void windowActivated(WindowEvent arg0) {}

	@Override
	public void windowClosed(WindowEvent arg0) {}

	@Override
	public void windowClosing(WindowEvent arg0) {}

	@Override
	public void windowDeactivated(WindowEvent arg0) {}

	@Override
	public void windowDeiconified(WindowEvent arg0) {}

	@Override
	public void windowIconified(WindowEvent arg0) {}

	@Override
	public void windowOpened(WindowEvent arg0) {}	
}
