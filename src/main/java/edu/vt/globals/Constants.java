/*
 * Created by Team 5 (Alex Kyer, Ben Dunko, Emaan Rana, David Carroll, Chris Koehler) on 2021.12.7
 * Copyright © 2021 Team 5. All rights reserved.
 */
package edu.vt.globals;

public final class Constants {
    /*
    ==================================================
    |   Use of Class Variables as Global Constants   |
    ==================================================
    All of the variables in this class are Class Variables (typed with the "static" keyword)
    with Constant values, which can be accessed in any class in the project by specifying
    Constants.CONSTANTNAME, i.e., ClassName.ClassVariableNameInCaps

    Constants are specified in capital letters.

    =====================================================
    |   Our Design Decision for Use of External Files   |
    =====================================================
    We decided to use directories external to our application for the storage and retrieval of user's files.
    We do not want to store/retrieve external files into/from our database for the following reasons:

        (a) Database storage and retrieval of large files as BLOB (Binary Large OBject) degrades performance.
        (b) BLOBs increase the database complexity.
        (c) The operating system handles the external files instead of the database management system.

    WildFly provides an internal web server, named Undertow, to access and display external files.
    See https://docs.wildfly.org/24/Admin_Guide.html#Undertow
     */

    //---------------
    // To run locally
    //---------------

    // Windows
    public static final String PHOTOS_ABSOLUTE_PATH = "C:/Users/chriskoehler/DocRoot/CS3754-Team5-FileStorage/";

    // Unix (macOS) or Linux
    //public static final String PHOTOS_ABSOLUTE_PATH = "/Users/chriskoehler/DocRoot/CS3754-Team5-FileStorage/";

    //-------------------------------------------------------------------
    // To run on your AWS EC2 instance, on VENUS or JUPITER course server
    //-------------------------------------------------------------------
    //public static final String PHOTOS_ABSOLUTE_PATH = "/opt/wildfly/DocRoot/CS3754-Team5-FileStorage/";

    /*
    =================================================================================================
    |   For displaying external files to the user in an XHTML page, we use the Undertow subsystem.  |
    =================================================================================================
     We configured WildFly Undertow subsystem so that
     http://localhost:8080/musicianconnectphotos/ displays file p from /Users/chriskoehler/DocRoot/CS3754-Team5-FileStorage/
     */

    //---------------
    // To run locally
    //---------------
    public static final String PHOTOS_URI = "http://localhost:8080/musicianconnectphotos/";

    //-----------------------------------------------------
    // To run on your AWS EC2 instance with your IP address
    //-----------------------------------------------------
    //public static final String PHOTOS_URI = "http://34.227.79.54:8080/musicianconnectphotos/";


    /*
    =============================================
    |   Our Design Decision for Profile Photo   |
    =============================================
    We do not want to use the uploaded user profile photo as is, which may be very large
    degrading performance. We scale it down to size 200x200 called the Thumbnail photo size.
     */
    public static final Integer THUMBNAIL_SIZE = 200;

    /*
     United States postal state abbreviations (codes)
     */
    public static final String[] STATES = {"AK", "AL", "AR", "AZ", "CA", "CO", "CT",
            "DC", "DE", "FL", "GA", "GU", "HI", "IA", "ID", "IL", "IN", "KS", "KY", "LA", "MA",
            "MD", "ME", "MH", "MI", "MN", "MO", "MS", "MT", "NC", "ND", "NE", "NH", "NJ", "NM",
            "NV", "NY", "OH", "OK", "OR", "PA", "PR", "PW", "RI", "SC", "SD", "TN", "TX", "UT",
            "VA", "VI", "VT", "WA", "WI", "WV", "WY"};

    /*
     A security question is selected and answered by the user at the time of account creation.
     The selected question/answer is used as a second level of authentication for
     (a) resetting user's password, and (b) deleting user's account.
     */
    public static final String[] QUESTIONS = {
            "In what city or town did your mother and father meet?",
            "In what city or town were you born?",
            "What did you want to be when you grew up?",
            "What do you remember most from your childhood?",
            "What is the name of the boy or girl that you first kissed?",
            "What is the name of the first school you attended?",
            "What is the name of your favorite childhood friend?",
            "What is the name of your first pet?",
            "What is your mother's maiden name?",
            "What was your favorite place to visit as a child?"
    };

    public static final String[] EXPERIENCES = {
            "None",
            "Beginner",
            "Intermediate",
            "Advanced",
            "Expert"
    };

    public static final String[] GENRES = {
            "None",
            "Rock",
            "Pop",
            "Indie",
            "Country",
            "RnB",
            "Hip Hop",
            "Electronic",
            "Jazz",
            "Blues",
            "Classical"
    };

    public static final String[] LANGUAGES = {
            "English",
            "Spanish"
    };

    public static final String LYRICS_OVH_BASE_URL = "https://api.lyrics.ovh/v1/";
    public static final String SONGSTERR_BASE_URL = "http://www.songsterr.com/a/ra/songs.json?pattern=";
    public static final String YOUTUBE_API_KEY = "REDACTED"
}
