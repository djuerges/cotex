package de.uniluebeck.collaboratex.session;

import com.google.appengine.api.channel.ChannelService;
import com.google.appengine.api.channel.ChannelServiceFactory;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/**
 * Session Manager handles
 *
 * a) the tokens for each client, mapping them by the session id b) the openend
 * documents for each client, also mapping them by session id
 *
 * @author Daniel Jürges <djuerges@googlemail.com>
 */
public class SessionManager implements HttpSessionListener {

    private static final ChannelService channelService = ChannelServiceFactory.getChannelService();

    private static final Map<String, Long> openedDocuments = new HashMap<>();
    public static final Map<String, String> channelTokens = new HashMap<>();

    @Override
    public void sessionCreated(HttpSessionEvent event) {
        /* get session id and create token */
        String sessionId = event.getSession().getId();
        String token = channelService.createChannel(sessionId);

        /* map token to session id */
        channelTokens.put(sessionId, token);
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent event) {
        /* remove token from map when session is destroyed*/
        channelTokens.remove(event.getSession().getId());
    }

    public static Map<String, Long> getOpenedDocuments() {
        return openedDocuments;
    }

    public static Long getOpenedDocumentId(String sessionId) {
        return openedDocuments.get(sessionId);
    }

    public static void setNewOpenedDocument(String sessionId, Long id) {
        openedDocuments.put(sessionId, id);
    }

    public static boolean removeFromOpenedDocument(String sessionId, Long id) {
        if (openedDocuments.get(sessionId) != null) {
            if (openedDocuments.get(sessionId).equals(id)) {
                openedDocuments.remove(sessionId);
                return true;
            }
        }
        return false;
    }

    public static String getChannelToken(String sessionId) {
        return channelTokens.get(sessionId);
    }
}
