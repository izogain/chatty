
package chatty.util.api.pubsub;

import java.util.Map;
import java.util.logging.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 * Received message.
 * 
 * @author tduva
 */
public class Message {
    
    private static final Logger LOGGER = Logger.getLogger(Message.class.getName());
    
    /**
     * Basic type of the message. Should never be null.
     */
    public final String type;
    
    /**
     * Message identifier. Can be null.
     */
    public final String nonce;
    
    /**
     * Data of the message. Can be null.
     */
    public final MessageData data;
    
    /**
     * Attached error message. Can be null.
     */
    public final String error;
    
    public Message(String type, String nonce, MessageData data, String error) {
        this.type = type;
        this.nonce = nonce;
        this.data = data;
        this.error = error;
    }
    
    public static Message fromJson(String json, Map<Long, String> userIds) {
        try {
            JSONParser parser = new JSONParser();
            JSONObject root = (JSONObject)parser.parse(json);
            
            String type = (String)root.get("type");
            if (type == null) {
                LOGGER.warning("PubSub message type null");
                return null;
            }
            
            String nonce = (String)root.get("nonce");
            String error = (String)root.get("error");
            
            MessageData data = MessageData.decode((JSONObject)root.get("data"), userIds);
            return new Message(type, nonce, data, error);
        } catch (Exception ex) {
            LOGGER.warning("Error parsing PubSub message: "+ex);
            return null;
        }
    }
    
}
