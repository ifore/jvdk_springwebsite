package site.service;

import org.openid4java.association.AssociationException;
import org.openid4java.consumer.ConsumerException;
import org.openid4java.consumer.ConsumerManager;
import org.openid4java.consumer.VerificationResult;
import org.openid4java.discovery.DiscoveryException;
import org.openid4java.discovery.DiscoveryInformation;
import org.openid4java.discovery.Identifier;
import org.openid4java.message.AuthRequest;
import org.openid4java.message.MessageException;
import org.openid4java.message.ParameterList;

import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * Generate a new openid object
 *
 * SteamOpenID openid = new SteamOpenID()
 *
 *
 * Redirect the user to the steam login page
 *
 * response.redirect(openid.login("http://www.mysite.com/postLogin"));
 *
 * This will return null or a string containing the long variant of
 * the steam id (known as the community id)
 *
 *
 *
 */
@Service
public class SteamOpenID {
    private static final String STEAM_OPENID = "http://steamcommunity.com/openid";
    private final ConsumerManager manager;
    private final Pattern STEAM_REGEX = Pattern.compile("(\\d+)");
    private DiscoveryInformation discovered;

    /**
     * Creates the {@link ConsumerManager} and sets up
     * the {@link DiscoveryInformation}
     */

    public SteamOpenID() {
        System.setProperty("org.apache.commons.logging.Log",
                "org.apache.commons.logging.impl.NoOpLog");
        manager = new ConsumerManager();
        manager.setMaxAssocAttempts(0);
        try {
            discovered = manager.associate(manager.discover(STEAM_OPENID));
        } catch (DiscoveryException e) {
            e.printStackTrace();
            discovered = null;
        }
    }

    /**
     * Perform a login then redirect to the callback url. When the
     * callback url is opened, you are responsible for
     * verifying the OpenID login.
     *
     * @param callbackUrl A String of a url that this login page should
     *                    take you to. This should be an absolute URL.
     * @return Returns the URL of the OpenID login page. You should
     * redirect your user to this.
     */
    public String login(String callbackUrl) {
        if (this.discovered == null) {
            return null;
        }
        try {
            AuthRequest authReq = manager.authenticate(this.discovered, callbackUrl);
            return authReq.getDestinationUrl(true);
        } catch (MessageException | ConsumerException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Verify the Steam OpenID Login
     *
     * @param receivingUrl The url that received the Login (this should be the
     *                     same as the callbackUrl that you used in
     *                     the {@link #login(String)} method.
     * @param responseMap  A {@link Map} that contains the response values from the login.
     * @return Returns the Steam Community ID as a string.
     */
    public String verify(String receivingUrl, Map responseMap) {
        if (this.discovered == null) {
            return null;
        }
        Map<String,String[]> ss = responseMap;
        for(Map.Entry<String,String[]> entry : ss.entrySet()) {
            System.out.println(entry.getKey()+ " " + entry.getValue());
        }
        ParameterList responseList = new ParameterList(responseMap);
        try {
            VerificationResult verification = manager.verify(receivingUrl, responseList, this.discovered);
            Identifier verifiedId = verification.getVerifiedId();
            if (verifiedId != null) {
                String id = verifiedId.getIdentifier();
                Matcher matcher = STEAM_REGEX.matcher(id);
                if (matcher.find()) {
                    System.out.println();
                    return matcher.group(1);
                }
            }
        } catch (MessageException | DiscoveryException | AssociationException e) {
            e.printStackTrace();
        }
        return null;
    }
}