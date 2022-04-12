package com.detection.diseases.maize.helpers;

/**
 * This class holds all the constants that are used in the application
 *
 */
public class AppConstants {
    /**
     * Base open weather api link
     */
    public static final String OPENWEATHERAPI="https://api.openweathermap.org/data/2.5/weather";
    /**
     * Account api key for open weather api
     */
    public static final String OPENWEATHERAPIKEY= "47173cae29386c36db52a1678fb5c144";

    /**
     * Base api link for the application. The api houses all the logic for the app
     */
    public static final String BASE_API_URL = "http://137.184.146.52:8080/api/v1" ;
    /**
     * A key for putting and getting intent extras for results returned by thhe deep learning model
     *
     * The results are transfered between activities
     */
    public static final String MODEL_RESULTS = "model_results";

    /**
     * Key for cancelling uploading image to API for inference
     */
    public static final Object POST_IMAGE_TAG_REQUEST = "SendImagdjdsjhdsj";

    /**
     * A key for cancelling a request for getting community issues
     */
    public static final Object GET_COMMUNITY_ISSUES = "GetCommunitIssueahshdsan";

    /**
     * Key for accessing and putting access token in the session. Usesd Share preferences
     *
     * @se {@link android.content.SharedPreferences}
     */
    public static final String API_ACCESS_TOKEN = "Usser_access_tokess";

    /**
     * Key for accessing and putting a logged in user in the session. Uses Shared preferences
     */
    public static final String LOGGED_IN_USER_SESSION_KEY = "Setloggedinuserindf the applicattion";

    /**
     * Key for accessing and putting access token in the session. Uses Shared preferences
     */
    public static final String USER_ROLE = "ygdugdihreorihfksuhfewiofhoewidjewopjdweop";

    public static final String MODEL_RESPONSE_RESULTS = "iygedidugweodhwepodkml,jdnwe;odjew;dmew;";
    public static final String ACTIVITY_SOURCE = "activity";
    public static final String DIRECT_DETECTION = "direct_detection";
    public static final String FIRST_DISEASE_SELECTION_HELPER = "Model_result_first_selection";
    public static final String SECOND_DISEASE_SELECTION_HELPER = "Modewl_second_choice";
    public static final String CAPTURED_IMAGE_URL = "captured_image_url";
    public static final String ISSUE_TO_ANSWER = "issue_to_answer";
    public static final Object SEND_ISSUE_ANSWER = "ehueihdewodhwipqdjiqwioijdwodjqwpojdp";
    public static final Object GET_ISSUE_ANSWERS = "hdddjehdlewjd;qewkljdbhqeli.djewp";
}
