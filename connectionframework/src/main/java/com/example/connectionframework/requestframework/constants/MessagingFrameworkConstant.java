package com.example.connectionframework.requestframework.constants;

public class MessagingFrameworkConstant {

	/***
	 * 
	 * @author Kadriye Bağlıoğlu (kadriye.baglioglu@gmail.com)
	 */

	 
	 public static class ROUTING_ADDRESS {
		 
		 public static final int CLIENT = 1;
		 public static final int SERVER= 2;
		 public static final int LOOP_BACK_SERVICE = 3;
		 public static final int ASYNC_CLIENT = 4;
        public static final int BKT_SERVER = 5;
        public static final int CLIENT_STACK_NAVIGATOR = 6;
    }

	 public static class ANIMATION_TYPES {
		 public static final int OpenNewPage = 0;
		 public static final int OpenPreviousPage = 1;
		 public static final int OpenTabbedPage= 2;
		 public static final int OpenWithoutAnimation = 3;


         /**
          * if animationType is sharedTransition, one of elements of @var data should be
          * HashMap String, Object with
          * "TransitionName" key with String value
          * "TransitionView" key with View value
          */
         public static final int SharedTransition = 4;
     }

    public static class TRANSITION_NAMES {
        public static final String APP_LOGO = "appLogo";
        public static final String CAMPAIGN_IMAGE = "campaignImage";
    }

	 public static class STATUS_CODES {
		 
		 public static final int Success = 200;
		 public static final int Confirmation = 600;
		 public static final int ConfirmationWithInput = 601;
		 public static final int Warning = 400;
		 public static final int WarningAndRefreshPage = 440;
		 public static final int ConnectionFailed = 408;
		 public static final int ConnectionTimedOut = 700; //409;
		 public static final int Error = 503;
		 public static final int WarningWithoutAlert = 220;
		 public static final int Inform = 210;	 
		 public static final int SwitchToHttp = 101;
		 public static final int MobileSignatureDelay=221;
		 public static final int SecureLogOff=701;

	 } 
		
}
