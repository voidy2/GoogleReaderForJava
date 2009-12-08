/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package googlereader;

/**
 *
 * @author fcs04065
 */
public class Const {

    public static final String URI_LOGIN;
    public static final String URI_PREFIXE_READER;
    public static final String URI_PREFIXE_ATOM;
    public static final String URI_PREFIXE_API;
    public static final String URI_PREFIXE_VIEW;
    public static final String ATOM_GET_FEED;
    public static final String ATOM_PREFIXE_USER;
    public static final String ATOM_PREFIXE_USER_NUMBER;
    public static final String ATOM_PREFIXE_LABEL;
    public static final String ATOM_PREFIXE_STATE_GOOGLE;
    public static final String ATOM_STATE_READ;
    public static final String ATOM_STATE_UNREAD;
    public static final String ATOM_STATE_FRESH;
    public static final String ATOM_STATE_READING_LIST;
    public static final String ATOM_STATE_BROADCAST;
    public static final String ATOM_STATE_STARRED;
    public static final String ATOM_SUBSCRIPTIONS;
    public static final String API_EDIT_SUBSCRIPTION;
    public static final String API_EDIT_TAG;
    public static final String API_LIST_PREFERENCE;
    public static final String API_LIST_SUBSCRIPTION;
    public static final String API_LIST_TAG;
    public static final String API_LIST_UNREAD_COUNT;
    public static final String API_TOKEN;
    public static final String URI_QUICKADD;
    public static final String OUTPUT_XML;
    public static final String OUTPUT_JSON;

    static {
        URI_LOGIN = "https://www.google.com/accounts/ClientLogin";
        URI_PREFIXE_READER = "http://www.google.com/reader/";
        URI_PREFIXE_ATOM = URI_PREFIXE_READER + "atom/";
        URI_PREFIXE_API = URI_PREFIXE_READER + "api/0/";
        URI_PREFIXE_VIEW = URI_PREFIXE_READER + "view/";
        ATOM_GET_FEED = "feed/";
        ATOM_PREFIXE_USER = "user/-/";
        ATOM_PREFIXE_USER_NUMBER = "user/" + "00000000000000000000" + "/";
        ATOM_PREFIXE_LABEL = ATOM_PREFIXE_USER + "label/";
        ATOM_PREFIXE_STATE_GOOGLE = ATOM_PREFIXE_USER + "state/com.google/";
        ATOM_STATE_READ = ATOM_PREFIXE_STATE_GOOGLE + "read";
        ATOM_STATE_UNREAD = ATOM_PREFIXE_STATE_GOOGLE + "kept-unread";
        ATOM_STATE_FRESH = ATOM_PREFIXE_STATE_GOOGLE + "fresh";
        ATOM_STATE_READING_LIST = ATOM_PREFIXE_STATE_GOOGLE + "reading-list";
        ATOM_STATE_BROADCAST = ATOM_PREFIXE_STATE_GOOGLE + "broadcast";
        ATOM_STATE_STARRED = ATOM_PREFIXE_STATE_GOOGLE + "starred";
        ATOM_SUBSCRIPTIONS = ATOM_PREFIXE_STATE_GOOGLE + "subscriptions";
        API_EDIT_SUBSCRIPTION = "subscription/edit";
        API_EDIT_TAG = "edit-tag";
        API_LIST_PREFERENCE = "preference/list";
        API_LIST_SUBSCRIPTION = "subscription/list";
        API_LIST_TAG = "tag/list";
        API_LIST_UNREAD_COUNT = "unread-count";
        API_TOKEN = "token";
        URI_QUICKADD = URI_PREFIXE_READER + "quickadd";
        OUTPUT_XML = "xml";
        OUTPUT_JSON = "json";
    }
}
