package com.instacommerce.connectionframework.requestframework.constants;


public class Constants {

    public static boolean DEBUG = false;
    public static String ANNOYING_PROJECTS_STORY_PREF = "annoying_shared_prefs";

    public static class Application {

        public static final int CONNECTION_TIMEOUT = 30000;
        public static final String CONNECTION_TIMED_OUT_ERROR_MESSAGE = "Timeout error on service call";
        public static final String CONNECTION_OTHER_EXCEPTION = "Error on service call";
        public static final String CERTIFICATE_ERROR = "CertificateException";

    }

}