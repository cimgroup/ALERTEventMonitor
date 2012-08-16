/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package alerteventmonitor;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

/**
 *
 * @author Sasa.Stojanovic
 */
public class Global {
    
    public static EventMonitor oEventMonitor;
    public static int iEventNumber = 0;
    
    public static void main(String args[]) {
        oEventMonitor = new EventMonitor(); 
        oEventMonitor.setVisible(true);
        oEventMonitor.SetControls();
    }
    
    public static ArrayList <CustomTreeNode> oTreeNodes = new ArrayList<CustomTreeNode>();
    
    public static CustomTreeNode GetNodeForHashCode(int iHashCode)
    {
        CustomTreeNode oNode = null;
        for (int i = 0; i < oTreeNodes.size(); i++)
        {
            if (oTreeNodes.get(i).iHashCode == iHashCode)
                oNode = oTreeNodes.get(i);
        }
        return oNode;
    }
    
    public static ArrayList <CustomTreeNode> GetNodesForName(String sEventName)
    {
        ArrayList <CustomTreeNode> oNodes = new ArrayList<CustomTreeNode>();
        for (int i = 0; i < oTreeNodes.size(); i++)
        {
            if (oTreeNodes.get(i).sEventName.equals(sEventName))
                oNodes.add(oTreeNodes.get(i));
        }
        return oNodes;
    }

    public static void ReceiveXML(String sXML) {
        try
        {
            iEventNumber++;
            String sTimeStamp = ((Long)System.currentTimeMillis()).toString();
            String sCurrentTime = new Timestamp((new java.util.Date()).getTime()).toString();
            String sEventName = GetTagValue(sXML, "ns1:eventName");
            String sEventId = GetTagValue(sXML, "ns1:eventId");
            ArrayList <CustomTreeNode> oNodes = GetNodesForName(sEventName);
            if (oNodes != null && oNodes.size() > 0)
            {
                for (int i = 0; i < oNodes.size(); i++)
                {
                    CustomTreeNode oNode = oNodes.get(i);
                    oNode.bReceived = true;
                    oNode.sTimeStamps.put(sEventId, sTimeStamp);
                    oNode.tnNode.setUserObject(oNode.sEventName + " (" + GetAverageTime(oNode) + "ms / " + ((Integer)oNode.sTimeStamps.size()).toString() + " events)");
                }

                Long lReceivedAt = Long.parseLong(sTimeStamp);
                
                Long lParentTime = 0l;
                CustomTreeNode oParentNode = GetNodeForHashCode(oNodes.get(0).iParentHashCode);
                if (oParentNode != null)
                {
                    String sParentTimestamp = oParentNode.sTimeStamps.get(sEventId);
                    Long lParentReceivedAt = lReceivedAt;
                    if (sParentTimestamp != null)
                        lParentReceivedAt = Long.parseLong(sParentTimestamp);
                    lParentTime = lReceivedAt - lParentReceivedAt;
                }
                
                Long lRootTime = 0l;
                CustomTreeNode oRootNode = oTreeNodes.get(0);
                if (oRootNode != null)
                {
                    String sRootTimestamp = oRootNode.sTimeStamps.get(sEventId);
                    Long lRootReceivedAt = lReceivedAt;
                    if (sRootTimestamp != null)
                        lRootReceivedAt = Long.parseLong(sRootTimestamp);
                    lRootTime = lReceivedAt - lRootReceivedAt;
                }

                oEventMonitor.AddEventToTable(iEventNumber, sEventId, sEventName, lParentTime, lRootTime, sCurrentTime);
                oEventMonitor.RefreshTree();
            }
        }
        catch (Exception ex)
        {
            String sTest = "test";
        }
    }
    
    public static String GetTagValue(String sXML, String sTag)
    {
        String sValue = "";
        int iBeginIndex = sXML.indexOf("<" + sTag + ">") + sTag.length() + 2;
        int iEndIndex = sXML.indexOf("</" + sTag + ">");
        sValue = sXML.substring(iBeginIndex, iEndIndex);
        return sValue;
    }
    
    public static String GetAverageTime(CustomTreeNode oNode)
    {
        String sAverageTime = "0";
        CustomTreeNode oParentNode = GetNodeForHashCode(oNode.iParentHashCode);
        if (oParentNode != null)
        {
            Long lAverageTime;
            Long lSum = 0l;
            for (Map.Entry<String, String> eTimeStamp : oNode.sTimeStamps.entrySet()) 
            {
                Long lReceivedAt = Long.parseLong(eTimeStamp.getValue());
                String sParentTimestamp = oParentNode.sTimeStamps.get(eTimeStamp.getKey());
                Long lParentReceivedAt = lReceivedAt;
                if (sParentTimestamp != null)
                    lParentReceivedAt = Long.parseLong(sParentTimestamp);
                lSum += lReceivedAt - lParentReceivedAt;
            }
            lAverageTime = lSum / oNode.sTimeStamps.size();
            sAverageTime = lAverageTime.toString();
        }
        return sAverageTime;
    }
}
