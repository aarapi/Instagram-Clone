package com.instacommerce.annoyingprojects.data;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.instacommerce.annoyingprojects.mobile.basemodels.BaseModel;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import static com.instacommerce.connectionframework.requestframework.constants.Constants.ANNOYING_PROJECTS_STORY_PREF;


public class StoryModel extends BaseModel {

    @SerializedName("ID")
    public String ID;

    @SerializedName("Name")
    public String Name;

    @SerializedName("SeoName")
    public String SeoName;

    @SerializedName("Title")
    public String Title;

    @SerializedName("username")
    public String username;

    @SerializedName("storyDuration")
    private long storyDuration;

    @SerializedName("RelatedCampaignID")
    public String RelatedCampaignID;

    @SerializedName("RelatedUrl")
    public String RelatedUrl;

    @SerializedName("Image1")
    private String Image1;

    @SerializedName("Image2")
    private String Image2;

    @SerializedName("portraitImage")
    private String portraitImage;

    /**
     * This value contained old Campaign html content,
     * that is not used anymore
     */
    @Deprecated
    @SerializedName("Content")
    private String Content;

    @SerializedName("link")
    private String link;

    @SerializedName("buttonTitle")
    private String buttonTitle;

    /**
     * @return story duration defined on server, or 4000 ms as default if not set.
     */
    public long getStoryDuration() {
        return storyDuration > 0 ? storyDuration : 4000L;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    /**
     * @return link label considering app locale and if is set on story or not.
     */
    @NonNull
    public String getLinkLabel() {
        if (buttonTitle != null && !buttonTitle.isEmpty()) {
            return buttonTitle;
        }
        return "View More";
    }

    public String getSmallImage() {
        return Image1;
    }

    public String getPortraitImage() {
        return portraitImage != null ? portraitImage : Image2;
    }

    /**
     * It is used to hold the seenSet like a cache, to avoid accessing to much shared preferences,
     * because it has performance problems and can cause glitches.
     */
    transient private static Set<String> seenSetCache = null;

    /**
     * Set seen status of the campaign
     *
     * @param context used to access shared preferences
     * @param seen    to set
     * @return model instance, useful in chains
     */
    public StoryModel setIsSeen(@NonNull Context context, boolean seen) {
        Set<String> seenSet = getSeenSet(context);
        if (seen) {
            seenSet.add(ID);
        } else {
            seenSet.remove(ID);
        }
        setSeenSet(context, seenSet);
        return this;
    }

    public boolean isSeen(@NonNull Context context) {
        Set<String> seenSet = getSeenSet(context);
        return seenSet.contains(ID);
    }

    /**
     * Find and returns SeenSet from Shared Preferences, first it check the cache,
     * if cache is not present it will load from shared preferences.
     *
     * @param context to access shared preferences
     * @return found seenSet, or creates a empty set
     */
    @NonNull
    public static Set<String> getSeenSet(Context context) {
        if (seenSetCache != null) {
            return seenSetCache;
        }
        SharedPreferences preferences = context.getSharedPreferences(ANNOYING_PROJECTS_STORY_PREF, Context.MODE_PRIVATE);
        Set<String> seenSet = preferences.getStringSet("seenSet", null);
        seenSetCache = seenSet;
        if (seenSet == null) {
            seenSet = new HashSet<>();
        }
        return seenSet;
    }

    /**
     * Store a seenSet to sharedPreferences
     *
     * @param context to access shared preferences
     * @param seenSet to store, if null will remove it from shared preferences
     */
    public static void setSeenSet(Context context, @Nullable Set<String> seenSet) {
        seenSetCache = seenSet;
        SharedPreferences preferences = context.getSharedPreferences(ANNOYING_PROJECTS_STORY_PREF, Context.MODE_PRIVATE);
        preferences.edit()
                .putStringSet("seenSet", seenSet)
                .apply();
    }

    /**
     * finds a campaign inside a campaign list
     *
     * @param list to be searched
     * @param id   of requested campaign
     * @return found item, or null if not present in given array
     */
    @Nullable
    public static StoryModel findCampaignWithId(@Nullable ArrayList<StoryModel> list, @Nullable String id) {
        if (list == null || id == null) {
            return null;
        }
        for (StoryModel item : list) {
            if (item.ID.equals(id)) {
                return item;
            }
        }
        return null;
    }

    /**
     * Extracts campaign list from response object.
     *
     * @param responseObject received from web service
     * @return list with campaigns, of empty array if an error occurred or there are not campaigns
     */

}
