/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ActiveMQ;

import alerteventmonitor.Constants;
import javax.jms.*;
import javax.naming.*;
import java.io.*;
import java.util.Properties;

/**
 *
 * @author dusan.marjanovic
 */
public class SimpleTopicSubscriber {
    
    
    public static void StartListening() {
        
        Context jndiContext = null;
        TopicConnectionFactory topicConnectionFactory = null;
        TopicConnection topicConnection = null;
        TopicSession topicSession = null;
        Topic[] topics = new Topic[Constants.sTopics.size()];
        TopicSubscriber[] topicSubscribers = new TopicSubscriber[Constants.sTopics.size()];
        TextListener topicListener = null;
        TextMessage message = null;
        InputStreamReader inputStreamReader = null;
               
        try {
            Properties env = new Properties( );
            env.setProperty(Context.INITIAL_CONTEXT_FACTORY,"org.apache.activemq.jndi.ActiveMQInitialContextFactory");
            env.setProperty(Context.PROVIDER_URL, Constants.sActiveMQAddress);
            for(int i=0; i<Constants.sTopics.size(); i++)
            {
                env.setProperty("topic." + Constants.sTopics.get(i), Constants.sTopics.get(i));
            }
            jndiContext = new InitialContext(env);
            } catch (NamingException e) {
                System.out.println("Could not create JNDI API " + "context: " + e.toString());
                e.printStackTrace();
                System.exit(1);
        }
        
//*  Look up connection factory and topic. If either does not exist, exit.
        
        try {
            topicConnectionFactory = (TopicConnectionFactory)
            jndiContext.lookup("TopicConnectionFactory");
            
            for(int i=0; i<Constants.sTopics.size(); i++)
            {
                topics[i] = (Topic) jndiContext.lookup(Constants.sTopics.get(i));
            }
            } catch (NamingException e) {
                System.out.println("JNDI API lookup failed: " + e.toString());
                e.printStackTrace();
                System.exit(1);
            }
        
/*
    * Create connection.
    * Create session from connection; false means session is
    * not transacted.
    * Create subscriber.
    * Register message listener (TextListener).
    * Receive text messages from topic.
    * When all messages have been received, enter Q to quit.
    * Close connection.
*/     
        
        try{
            topicConnection = topicConnectionFactory.createTopicConnection();
            topicConnection.setClientID("ALERTEventMonitor");
            topicSession = topicConnection.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);
            topicListener = new TextListener();
            
            for(int i=0; i<Constants.sTopics.size(); i++)
            {
                topicSubscribers[i] = topicSession.createSubscriber(topics[i]);
                //topicSubscribers[i] = topicSession.createDurableSubscriber(topics[i], "EventMonitor." + topics[i].getTopicName());
                topicSubscribers[i].setMessageListener(topicListener);
            }

            topicConnection.start();
            System.out.println("Event monitor is listening for new events.");
            inputStreamReader = new InputStreamReader(System.in);
            while (!Constants.bMonitorStop) {
                
            }
        }
        catch (JMSException e)
        {
            System.out.println("Exception occurred: " + e.toString());
        }
        finally
        {
            if (topicConnection != null)
            {
                try
                {
                    topicConnection.close();
                    System.out.println("Event monitor is stopped.");
                }
                catch (JMSException e){}
            }
        }
    }
}