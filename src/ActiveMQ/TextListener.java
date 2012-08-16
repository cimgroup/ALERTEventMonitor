/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ActiveMQ;

import javax.jms.*;
import alerteventmonitor.*;
import java.sql.Timestamp;

/**
 *
 * @author dusan.marjanovic
 */
public class TextListener implements MessageListener{
    
    Integer iNumMess = 0;
    
    @Override
    public void onMessage(final Message message) 
    {
        Runnable rListener = new Runnable() {
            @Override
            public void run() {
                TextMessage msg = null;
                try{
                    if (message instanceof TextMessage)
                    {
                        msg = (TextMessage) message;
                        java.util.Date dtmNow = new java.util.Date();
                        iNumMess++;
                        System.out.println("Receiving message " + iNumMess.toString() + ". at: " + new Timestamp(dtmNow.getTime()));
                        Global.ReceiveXML(msg.getText());
                    } 
                    else 
                    {
                        System.out.println("Message of wrong type: " + message.getClass().getName());
                    }
                }
                catch (JMSException e)
                {
                    System.out.println("JMSException in onMessage(): " + e.toString());
                }
                catch (Throwable t)
                {
                    System.out.println("Exception in onMessage():" + t.getMessage());
                }
            }
        };
        Thread thrListener = new Thread(rListener);
        thrListener.setPriority(9);
        thrListener.start();
    }                 
}
