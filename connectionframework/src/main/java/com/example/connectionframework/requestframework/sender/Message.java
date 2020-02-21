package com.example.connectionframework.requestframework.sender;

import com.example.connectionframework.requestframework.constants.MessagingFrameworkConstant;
import com.example.connectionframework.requestframework.json.JsonData;

import java.util.HashMap;
import java.util.List;

/**
 * <p>
 * This is the bare bones Message class that is used for messaging for mobile
 * applications. It is essentially a data carrier and contains no methods other
 * than accessors. It is designed to be immutable, once created, the fields are
 * all read-only.
 * </p>
 *
 * <p>
 * It contains the following fields:
 * </p>
 *
 * <p>
 * <hr/>
 * <b>source</b>: The transceiver id of the sender (the source of the message).
 * The source id must be globally unique. <br/>
 * <br/>
 * <b>target</b>: The transceiver id of the receiver (the target of the
 * message). The target id must be globally unique. <br/>
 * <br/>
 * <b>relatedView</b>: The id of the related view; if the direction of the
 * message is client-to-server this would be the view at the origin of the
 * message, if the direction of the message is server-to-client, this would be
 * the view at the receiving end of the message. <br/>
 * <br/>
 * <b>action</b>: This is the action id, identifying the exact action taken on
 * the user interface resulting in the creation of this message. This may be
 * considered the "verb" of the action to be done, the rest is contained in the
 * data. <br/>
 * <br/>
 * <b>data</b>: This is a generic object array, containing the "payload" of the
 * message as necessary. This field is only normally unpacked at the endpoints
 * of the communication chain. Note that you can supply any sort of list in the
 * constructor. <br/>
 * <br/>
 * <b>errorCode</b>: This is a field used for error reporting; if an error
 * occurs, the errorCode is set to a non-zero value. The error code is not
 * pushed into the data array so that it can be reported or logged at any point
 * in the communication chain. <br/>
 * <br/>
 * </p>
 *
 * @author Dr. Yasar Safkan <yasar.safkan@denizbank.com>
 *
 */

public class Message extends JsonData {
    private int source;
    private int target;
    private int relatedView;
    private int action;

    transient private int animationType;

    private boolean forceStartActivity;

    /**
     * Can contain various data that will be passed and tailored to receiver<br/>
     * Some generic that are used everywhere for LocalActions are:<br/>
     * <ul>
     * <li>
     * {@link HashMap}(String, Object) Containing:
     * <ul>
     * <li><b>paramBundle: Bundle -></b> will be passed to receiver activity intent with same key "paramBundle"</li>
     * <li><b>transitionName: String -></b> is used for shared transition</li>
     * <li><b>sourceView: View -></b> is used for shared transition and <b>deleted</b> directly after use to avoid memory leaks</li>
     * </ul>
     * </li>
     * </ul>
     */
    private List<Object> data;

    private int statusCode;
    private String sessionId;
    private String uniqueMessageId;
    private String securityMessageId;
    private int sessionExpired;
    private boolean cacheable;

    public boolean isCacheable() {
        return cacheable;
    }

    public void setCacheable(boolean cacheable) {
        this.cacheable = cacheable;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public int getAnimationType() {
        return animationType;
    }

    public void setAnimationType(int animationType) {
        this.animationType = animationType;
    }

    /**
     * Creates a Message object.
     *
     * @param source      the source id
     * @param target      the target id
     * @param relatedView related view id
     * @param action      the id of the action taken
     * @param data        the action-specific payload data
     */

    public Message(int source, int target, int relatedView, int action,
                   List<Object> data) {
        this(source, target, relatedView, action, data, MessagingFrameworkConstant.STATUS_CODES.Success, MessagingFrameworkConstant.ANIMATION_TYPES.OpenNewPage);
    }

    /**
     * Creates a message object, including an error code
     *
     * @param source      the source id
     * @param target      the target id
     * @param relatedView related view id
     * @param action      the id of the action taken
     * @param data        the action-specific payload data
     */
    public Message(int source, int target, int relatedView, int action,
                   final List<Object> data, int statusCode, int animationType,
                   boolean forceStartActivity) {
        this.source = source;
        this.target = target;
        this.relatedView = relatedView;
        this.action = action;
        this.data = data;
        this.statusCode = statusCode;
        this.animationType = animationType;
        this.forceStartActivity = forceStartActivity;
    }

    public Message(int source, int target, int relatedView, int action,
                   final List<Object> data, int statusCode, int animationType) {
        this.source = source;
        this.target = target;
        this.relatedView = relatedView;
        this.action = action;
        this.data = data;
        this.statusCode = statusCode;
        this.animationType = animationType;
    }

    /**
     * Constructs a blank message. This should not be used except for low-level
     * JSon deserialization code which accesses fields directly.
     */

    public Message() {
    }

    public boolean getForceStartActivity() {
        return forceStartActivity;
    }

    /**
     * Returns the error code. Zero indicates that this is not an error
     * condition.
     *
     * @return the error code
     */

    public int getStatusCode() {
        return statusCode;
    }

    /**
     * Returns the globally unique identifier of the related view
     *
     * @return related view id
     */

    public int getRelatedView() {
        return relatedView;
    }

    public void setRelatedView(int _relatedView) {
        relatedView = _relatedView;
    }

    public void setData(List<Object> data) {
        this.data = data;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    /**
     * Returns the action id that caused the generation of this message.
     *
     * @return the action id
     */

    public int getAction() {
        return action;
    }

    /**
     * Returns the action-specific {@link #data} in this message as an ArrayList.
     *
     * @return an array list, containing the action-specific data.
     * @see #data
     */
    public List<Object> getData() {
        return data;
    }


    /**
     * Returns the source id of the message.
     *
     * @return the source id
     */

    public int getSource() {
        return source;
    }

    /**
     * Returns the target id of the message.
     *
     * @return the target id
     */

    public int getTarget() {
        return target;
    }

    /**
     * Set target of the message
     */

    public void setTarget(int target) {
        this.target = target;
    }

    @Override
    public String toString() {
        return "Source : " + source + " Target : " + target + " RelatedView : "
                + relatedView + " Action : " + action + " ErrorCode : "
                + statusCode;
    }

    public String getUniqueMessageId() {
        return uniqueMessageId;
    }

    public void setUniqueMessageId(String uniqueMessageId) {
        this.uniqueMessageId = uniqueMessageId;
    }

    public String getSecurityMessageId() {
        return securityMessageId;
    }

    public void setSecurityMessageId(String securityMessageId) {
        this.securityMessageId = securityMessageId;
    }

    public int getSessionExpired() {
        return sessionExpired;
    }

    public void setSessionExpired(int sessionExpired) {
        this.sessionExpired = sessionExpired;
    }

    public void setForceStartActivity(boolean p_val) {
        forceStartActivity = p_val;
    }

    public enum AnimationType {
        Default, SharedTransition
    }
}
