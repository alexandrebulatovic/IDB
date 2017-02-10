package controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Regroupe des attributs par indexs.
 */
public class AttributeGroup 
{
	//Attributs
	/** Liste des couples index / attributs.*/
	private List<String []> origin;
	
	/** Liste des différents index*/
	private List<String> indexs;
	
	/** Liste du nombre d'attributs présents dans l'index.*/
	private List<Integer> numbers;
	
	/** Liste des groupes d'attributs présents dans l'index.*/
	private List<String []> groups;
	
	/** Cellue de $origin où se trouve le nom d'index.*/
	private int indexCell;
	
	/** Cellule de origin où se trouve le nom de l'attribut.*/
	private int attributeCell;
	
	
	//Constructeur
	/**
	 * Constructeur commun.
	 * 
	 * @param group : Liste des couples index / attribut, null interdit.
	 */
	public AttributeGroup(List<String[]> group, int indexCell, int attributeCell)
	{
		this.indexCell = indexCell;
		this.attributeCell = attributeCell;
		this.killPhantom(group);
		this.initIndexs();
		this.initCountone();
		this.initCountTwo();
		this.initGroups();
		
		System.out.println(this);
	}
	
	
	//Méthodes
	/**
	 * @return la liste des indexs différents.
	 */
	public List<String> getIndexs()
	{
		return this.indexs;
	}
	
	
	/**
	 * @param index : nom de l'index, null interdit.
	 * @return un groupe d'attribut soumis à la même contrainte $index.
	 */
	public String [] getGroup(String index)
	{
		int id = this.getIdByName(index);
		return this.groups.get(id);
	}
	
	
	/**
	 * @return une liste d'ensemble d'attribut partageant une contrainte.
	 */
	public List<String[]> getGroups()
	{
		return this.groups;
	}
	
	
	@Override
	public String toString()
	{
		StringBuilder result = new StringBuilder();
		result.append("\nORIGIN:\n");
		for (String [] ss : this.origin) {
			for (String s : ss) {
				result.append(s);
				result.append(", ");
			}
			result.deleteCharAt(result.length()-1);
			result.deleteCharAt(result.length()-1);
			result.append('\n');
		}
		
		int i = 0;
		result.append("\nINDEX ET NOMBRES :\n");
		for (String s : this.indexs) {
			result.append(s);
			result.append(" : ");
			result.append(this.numbers.get(i));
			result.append(", ");
			i++;
		}
		result.deleteCharAt(result.length()-1);
		result.deleteCharAt(result.length()-1);
		result.append('\n');
		
		result.append("\nGROUPES\n");
		for (String [] ss : this.groups) {
			for (String s : ss) {
				result.append(s);
				result.append(", ");
			}
			result.deleteCharAt(result.length()-1);
			result.deleteCharAt(result.length()-1);
			result.append('\n');
		}
		return result.toString();
	}
	
	
	//Privées
	/**
	 * Ajoute tous les couples non nulls.
	 * @param group : liste des couples index / attributs, null interdit.
	 */
	private void killPhantom(List<String[]> group)
	{
		this.origin = new ArrayList<String[]>();
		for (String [] ins_uns : group) {
			if (ins_uns[0] != null && ins_uns[0] != null) {
				this.origin.add(ins_uns);
			}
		}
	}
	
	
	/**
	 * Initialise le groupe en repérant tous les index différents.
	 */
	private void initIndexs()
	{
		this.indexs = new ArrayList<String>();
		for (String [] ins_uns : this.origin) {
			if (! this.containsIndex(ins_uns[0])) {
				this.indexs.add(ins_uns[0]);
			}
		}
	}
	
	
	/**
	 * Initialise sur zéro le nombre d'attribut par index.
	 */
	private void initCountone()
	{
		this.numbers = new ArrayList<Integer>();
		for (int i=0; i < this.indexs.size(); i++) {
			this.numbers.add(new Integer(0));
		}
	}
	
	
	private void initGroups()
	{
		this.groups = new ArrayList<String[]>();
		for (String index : this.indexs) {
			this.groups.add(this.initGroup(index));
		}
	}


	/**
	 * Compte le nombre d'attribut par index et met à jours
	 * le groupe en conséquence.
	 */
	private void initCountTwo()
	{
		int i = 0;
		int count;

		for (String in : this.indexs) {
			count = 0;
			for (String [] in2 : this.origin) {
				if (in.equals(in2[0])) {
					count ++;
				}
			}
			this.numbers.set(i, new Integer(count));
			i++;
		}
	}
	
	
	/**
	 * @param index : nom de l'index, null interdit.
	 * @return un groupe d'attribut soumis à la même contrainte $index.
	 */
	private String [] initGroup(String index)
	{
		int id = this.getIdByName(index);
		int size = this.numbers.get(id);
		String [] result = new String [size], next;
		String index2, attribut;
		int count = 0;
		
		Iterator<String []> it = this.origin.iterator();
		while (count < size && it.hasNext()) {
			next = it.next();
			index2 = next[0];
			attribut = next[1];
			if (index2.equals(index)) {
				result[count] = attribut;
				count++;
			}
		}
		return result;
	}
	
	
	/**
	 * @param index : nom d'index ou de contrainte, null interdit.
	 * @return vrai ssi index est déjà connu du groupe, faux sinon.
	 */
	private boolean containsIndex(String index)
	{
		for (String in : this.indexs) {
			if (in.equals(index)) return true; 
		}
		return false;
	}
	
	/**
	 * @param index : un nom d'index, null interdit.
	 * @return l'identifiant de l'$index dans le groupe.
	 */
	private int getIdByName(String index)
	{
		int i = 0, size = this.indexs.size(); 
		boolean found = false;
		
		while (i < size && !found) {
			found = this.indexs.get(i).equals(index);
			i++;
		}
		return found ? i -1 : -1;
	}
}
