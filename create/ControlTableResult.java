package create;

import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class ControlTableResult  implements ListSelectionListener{
	CreateTableView createFrame;
public ControlTableResult(CreateTableView c){
	this.createFrame=c;
	
}
	
public void valueChanged(ListSelectionEvent listSelectionEvent){
    ListSelectionModel lsm = (ListSelectionModel)listSelectionEvent.getSource();
    if (!(lsm.isSelectionEmpty())) {
    	createFrame.setEnableButtonUpdateDelete(true);
    }

}
}
