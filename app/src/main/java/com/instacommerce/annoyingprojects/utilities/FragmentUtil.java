
package com.instacommerce.annoyingprojects.utilities;

import android.content.Context;
import android.content.CursorLoader;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;

import androidx.core.graphics.drawable.DrawableCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.instacommerce.annoyingprojects.R;
import com.instacommerce.annoyingprojects.mobile.ui.afterlogin.home.HomeFragment;
import com.instacommerce.annoyingprojects.mobile.ui.afterlogin.messages.FragmentMessageUsers;
import com.instacommerce.annoyingprojects.mobile.ui.afterlogin.messages.FragmentUserMessages;
import com.instacommerce.annoyingprojects.mobile.ui.afterlogin.search.SearchFragment;
import com.instacommerce.annoyingprojects.mobile.ui.afterlogin.userprofile.UserProfileFragment;
import com.instacommerce.annoyingprojects.mobile.ui.beforelogin.FragmentLogIn;
import com.instacommerce.annoyingprojects.mobile.ui.beforelogin.FragmentSignUp;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class FragmentUtil {

    private static String CURRENT_TAG = null;
    private static Map<String, Typeface> TYPEFACE = new HashMap<String, Typeface>();
    public static final String HOME_FRAGMENT = "HOME_FRAMGNET";
    public static final String USER_PROFILE_FRAGMENT = "USER_PROFILE_FRAGMENT";
    public static final String SEARCH_FRAGMENT = "SEARCH_FRAGMENT";
    public static final String LOG_IN_FRAGMENT = "LOG_IN_FRAGMENT";
    public static final String SIGN_UP_FRAGMENT = "SIGN_UP_FRAGMENT";
    public static final String MESSAGE_USERS_FRAGMENT = "MESSAGE_USERS_FRAGMENT";
    public static final String USER_MESSAGES_FRAGMENT = "USER_MESSAGES_FRAGMENT";
    public static final String EDIT_PROFILE_FRAGMENT = "EDIT_PROFILE_FRAGMENT";
    public static final String ADD_STORY_FRAGMENT = "ADD_STORY_FRAGMENT";


    public static int getToolbarHeight(Context context) {
        int height = (int) context.getResources().getDimension(
                R.dimen.abc_action_bar_default_height_material);
        return height;
    }


    public static Drawable tintMyDrawable(Drawable drawable, int color) {
        drawable = DrawableCompat.wrap(drawable);
        DrawableCompat.setTint(drawable, color);
        DrawableCompat.setTintMode(drawable, PorterDuff.Mode.SRC_IN);
        return drawable;
    }

    public static String getRealPathFromURI(Uri contentUri, Context mContext) {
        String[] proj = {MediaStore.Images.Media.DATA};
        CursorLoader loader = new CursorLoader(mContext, contentUri, proj,
                null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(column_index);
        cursor.close();
        return result;
    }

    /**
     * Convert milliseconds into time hh:mm:ss
     *
     * @param milliseconds
     * @return time in String
     */
    public static String getDuration(long milliseconds) {
        long sec = (milliseconds / 1000) % 60;
        long min = (milliseconds / (60 * 1000)) % 60;
        long hour = milliseconds / (60 * 60 * 1000);

        String s = (sec < 10) ? "0" + sec : "" + sec;
        String m = (min < 10) ? "0" + min : "" + min;
        String h = "" + hour;

        String time = "";
        if (hour > 0) {
            time = h + ":" + m + ":" + s;
        } else {
            time = m + ":" + s;
        }
        return time;
    }

    public static int dpToPx(Context context, int dp) {
        float density = context.getResources().getDisplayMetrics().density;
        return Math.round((float) dp * density);
    }

    public static void switchFragmentWithAnimation(int id, Fragment fragment,
                                                   FragmentActivity activity, String TAG, AnimationType transitionStyle) {

        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager
                .beginTransaction();

        if (transitionStyle != null) {
            switch (transitionStyle) {
                case SLIDE_DOWN:

                    // Exit from down
                    fragmentTransaction.setCustomAnimations(R.anim.donot_move,
                            R.anim.slide_down);

                    break;

                case SLIDE_UP:

                    // Enter from Up
                    fragmentTransaction.setCustomAnimations(R.anim.slide_in_up,
                            R.anim.slide_out_up);

                    break;

                case SLIDE_LEFT:

                    // Enter from left
                    fragmentTransaction.setCustomAnimations(R.anim.slide_left,
                            R.anim.slide_out_left);

                    break;

                // Enter from right
                case SLIDE_RIGHT:
                    fragmentTransaction.setCustomAnimations(R.anim.slide_right,
                            R.anim.slide_out_right);

                    break;

                case FADE_IN:
                    fragmentTransaction.setCustomAnimations(R.anim.fade_in,
                            R.anim.fade_out);

                case FADE_OUT:
                    fragmentTransaction.setCustomAnimations(R.anim.fade_in,
                            R.anim.donot_move);

                    break;

                case SLIDE_IN_SLIDE_OUT:

                    fragmentTransaction.setCustomAnimations(R.anim.slide_left,
                            R.anim.slide_out_left);

                    break;

                default:
                    break;
            }
        }

        CURRENT_TAG = TAG;

        fragmentTransaction.replace(id, fragment, TAG);
        fragmentTransaction.addToBackStack(TAG);
        fragmentTransaction.commit();
    }
    public static void addFragmentWithAnimation(int id, Fragment fragment,
                                                FragmentActivity activity, String TAG, AnimationType transitionStyle) {

        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager
                .beginTransaction();

        if (transitionStyle != null) {
            switch (transitionStyle) {
                case SLIDE_DOWN:

                    // Exit from down
                    fragmentTransaction.setCustomAnimations(R.anim.slide_up,
                            R.anim.slide_down);

                    break;

                case SLIDE_UP:

                    // Enter from Up
                    fragmentTransaction.setCustomAnimations(R.anim.slide_in_up,
                            R.anim.slide_out_up);

                    break;

                case SLIDE_LEFT:

                    // Enter from left
                    fragmentTransaction.setCustomAnimations(R.anim.slide_left,
                            R.anim.slide_out_left);

                    break;

                // Enter from right
                case SLIDE_RIGHT:
                    fragmentTransaction.setCustomAnimations(R.anim.slide_right,
                            R.anim.slide_out_right);

                    break;

                case FADE_IN:
                    fragmentTransaction.setCustomAnimations(R.anim.fade_in,
                            R.anim.fade_out);

                case FADE_OUT:
                    fragmentTransaction.setCustomAnimations(R.anim.fade_in,
                            R.anim.donot_move);

                    break;

                case SLIDE_IN_SLIDE_OUT:

                    fragmentTransaction.setCustomAnimations(R.anim.slide_left,
                            R.anim.slide_out_left);

                    break;

                default:
                    break;
            }
        }

        CURRENT_TAG = TAG;

        fragmentTransaction.add(id, fragment);
        fragmentTransaction.addToBackStack(TAG);
        fragmentTransaction.commit();
    }
    public static void switchContent(int id, String TAG,
                                     FragmentActivity baseActivity, AnimationType transitionStyle) {

        Fragment fragmentToReplace = null;

        FragmentManager fragmentManager = baseActivity
                .getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        // If our current fragment is null, or the new fragment is different, we
        // need to change our current fragment
        if (CURRENT_TAG == null || !TAG.equals(CURRENT_TAG)) {

            if (transitionStyle != null) {
                switch (transitionStyle) {
                    case SLIDE_DOWN:
                        // Exit from down
                        transaction.setCustomAnimations(R.anim.donot_move,
                                R.anim.slide_down);

                        break;
                    case SLIDE_UP:
                        // Enter from Up
                        transaction.setCustomAnimations(R.anim.slide_in_up,
                                R.anim.slide_out_up);
                        break;
                    case SLIDE_LEFT:
                        // Enter from left
                        transaction.setCustomAnimations(R.anim.slide_left,
                                R.anim.slide_out_left);
                        break;
                    // Enter from right
                    case SLIDE_RIGHT:
                        transaction.setCustomAnimations(R.anim.slide_right,
                                R.anim.slide_out_right);
                        break;
                    case FADE_IN:
                        transaction.setCustomAnimations(R.anim.fade_in,
                                R.anim.fade_out);
                    case FADE_OUT:
                        transaction.setCustomAnimations(R.anim.fade_in,
                                R.anim.donot_move);
                        break;
                    case SLIDE_IN_SLIDE_OUT:
                        transaction.setCustomAnimations(R.anim.slide_left,
                                R.anim.slide_out_left);
                        break;
                    default:
                        break;
                }
            }

            // Try to find the fragment we are switching to
            Fragment fragment = fragmentManager.findFragmentByTag(TAG);

            // If the new fragment can't be found in the manager, create a new
            // one
            if (fragment == null) {

                if (TAG.equals(HOME_FRAGMENT)) {
                    fragmentToReplace = new HomeFragment();
                }else if (TAG.equals(USER_PROFILE_FRAGMENT)) {
                    fragmentToReplace = new UserProfileFragment();
                } else if (TAG.equals(SEARCH_FRAGMENT)) {
                    fragmentToReplace = new SearchFragment();
                } else if (TAG.equals(LOG_IN_FRAGMENT)) {
                    fragmentToReplace = new FragmentLogIn();
                } else if (TAG.equals(SIGN_UP_FRAGMENT)) {
                    fragmentToReplace = new FragmentSignUp();
                } else if (TAG.equals(MESSAGE_USERS_FRAGMENT)) {
                    fragmentToReplace = new FragmentMessageUsers();
                } else if (TAG.equals(USER_MESSAGES_FRAGMENT)) {
                    fragmentToReplace = new FragmentUserMessages();
                }

            } else{
                if (TAG.equals(HOME_FRAGMENT)) {
                    fragmentToReplace = (HomeFragment) fragment;
                }else  if (TAG.equals(USER_PROFILE_FRAGMENT)) {
                    fragmentToReplace = (UserProfileFragment) fragment;
                } else if (TAG.equals(SEARCH_FRAGMENT)) {
                    fragmentToReplace = (SearchFragment) fragment;
                } else if (TAG.equals(LOG_IN_FRAGMENT)) {
                    fragmentToReplace = (FragmentLogIn) fragment;
                } else if (TAG.equals(SIGN_UP_FRAGMENT)) {
                    fragmentToReplace = (FragmentSignUp) fragment;
                } else if (TAG.equals(MESSAGE_USERS_FRAGMENT)) {
                    fragmentToReplace = (FragmentMessageUsers) fragment;
                } else if (TAG.equals(USER_MESSAGES_FRAGMENT)) {
                    fragmentToReplace = (FragmentUserMessages) fragment;
                }
            }

            CURRENT_TAG = TAG;

            // Replace our current fragment with the one we are changing to
            transaction.replace(id, fragmentToReplace, TAG);
            transaction.commit();

        } else

        {
            // Do nothing since we are already on the fragment being changed to
        }
    }

    public static String getVersion(Context context) {
        try {
            PackageInfo pInfo = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), PackageManager.GET_META_DATA);
            return String.valueOf(pInfo.versionCode) + " " + pInfo.versionName;

        } catch (NameNotFoundException e) {
            return "1.0.1";
        }
    }

    public static Typeface getFonts(Context context, String fontName) {
        Typeface typeface = TYPEFACE.get(fontName);
        if (typeface == null) {
            typeface = Typeface.createFromAsset(context.getAssets(), "font/"
                    + fontName);
            TYPEFACE.put(fontName, typeface);
        }
        return typeface;
    }

    public enum AnimationType {
        SLIDE_LEFT, SLIDE_RIGHT, SLIDE_UP, SLIDE_DOWN, FADE_IN, SLIDE_IN_SLIDE_OUT, FADE_OUT
    }


    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }

        // The directory is now empty so delete it

        return dir.delete();
    }


}
