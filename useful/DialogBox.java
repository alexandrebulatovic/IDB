package useful;

import java.awt.Dialog;
import java.awt.Window;
import java.awt.event.HierarchyEvent;
import java.awt.event.HierarchyListener;

import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;

/**
 * Permet d'afficher des messages à l'utilisateur sous forme de boîte de dialogue
 * grâce aux méthodes {@code showError}, {@code showMessage} ou encore {@code showTable}
 * pour afficher un objet {@code JTable} à l'utilisateur.
 * <P>
 * Exemples d'utilisation :
 * <PRE>
 *          DialogBox.showError("Vous n'avez pas rentré le bon identifiant");
 *          
 *          DialogBox.showTable(new JTable());
 *          
 *          DialogBox.showMessage("Merci d'avoir utilisé cette application");
 * </PRE>
 */
public final class DialogBox 
{
	private DialogBox() {}

	/**
	 * Permet d'afficher un objet {@code JTable} à l'utilisateur.
	 * @param table : {@code JTable} à afficher.
	 * @param dialogBoxName : nom de la boîte de dialogue.
	 */
	public static void showTable(final JTable table, String dialogBoxName)
	{
		// cette portion de code sert à rendre la JOptionPane redimensionnable
		table.addHierarchyListener(new HierarchyListener() 
		{
			public void hierarchyChanged(HierarchyEvent e) 
			{
				Window window = SwingUtilities.getWindowAncestor(table);
				if (window instanceof Dialog) 
				{
					Dialog dialog = (Dialog)window;

					if (!dialog.isResizable()) 
						dialog.setResizable(true);
				}
			}
		});

		JOptionPane.showMessageDialog(null, new JScrollPane(table), dialogBoxName, JOptionPane.PLAIN_MESSAGE);
	}

	/**
	 * Permet d'afficher un message d'erreur à l'utilisateur.
	 * @param errorMessage : message d'erreur à afficher.
	 */
	public static void showError(String errorMessage)
	{
		JOptionPane.showMessageDialog(null, errorMessage, "Erreur", JOptionPane.ERROR_MESSAGE);
	}

	/**
	 * Permet d'afficher un message d'information à l'utilisateur.
	 * @param message : message à afficher.
	 * @param dialogBoxName : nom de la boîte de dialogue.
	 */
	public static void showMessage(String message, String dialogBoxName)
	{
		JOptionPane.showMessageDialog(null, message, dialogBoxName, JOptionPane.INFORMATION_MESSAGE);
	}
}