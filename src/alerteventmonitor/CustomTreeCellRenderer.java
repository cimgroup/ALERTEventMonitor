/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package alerteventmonitor;

import java.awt.Color;
import java.awt.Component;
import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellEditor;
import javax.swing.tree.DefaultTreeCellRenderer;

/**
 *
 * @author Sasa.Stojanovic
 */
public class CustomTreeCellRenderer extends DefaultTreeCellRenderer {
    ImageIcon iiGray;
    ImageIcon iiOk;
    ImageIcon iiWaiting;
    ImageIcon iiReceived;
    
    public CustomTreeCellRenderer() {
        super();
        iiGray = new ImageIcon(OSValidator.GetAppPath() + "/img/grayIcon.gif");
        iiOk = new ImageIcon(OSValidator.GetAppPath() + "/img/okIcon.gif");
        iiWaiting = new ImageIcon(OSValidator.GetAppPath() + "/img/waitingIcon.gif");
        iiReceived = new ImageIcon(OSValidator.GetAppPath() + "/img/receivedIcon.gif");
    }
    
    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus)
    {
        super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);        

        CustomTreeNode oNode = Global.GetNodeForHashCode(((DefaultMutableTreeNode)value).hashCode());
        if (oNode != null && oNode.bReceived != null)
        {
            if (oNode.bReceived) {
                setIcon(iiReceived);
            } else {
                setIcon(iiWaiting);
            } 
        }
        else
        {
            if (oNode != null)
                setIcon(iiOk);
            else
                setIcon(iiGray);
        }
        return this;
    }
}