/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package alerteventmonitor;

import java.util.LinkedHashMap;
import java.util.Map;
import javax.swing.tree.DefaultMutableTreeNode;

/**
 *
 * @author Sasa.Stojanovic
 */
public class CustomTreeNode {
    public int iHashCode;
    public int iParentHashCode;
    public String sEventName;
    public DefaultMutableTreeNode tnNode = null;
    Map<String, String> sTimeStamps = new LinkedHashMap<String, String>();
    public Boolean bReceived = null;
}