package other;


/**
 * this enum class helps the server recognize the sent order so that it can
 * behave correctly.
 */
public enum CommandType {
    UPDATEUSER,
    LOGIN,
    SIGNUP,
    SETTING,
    NEWPOST,
    LIKE,
    DISLIKE,
    LOADAPOST,
    RETRIEVEPASS,
    RETRIEVEPASS2NDPART,
    LOGOUT,
    SEARCHUSER,
    COMMENT,
    REPOST,
    LOADCOMMENTS,
    FOLLOW,
    UNFOLLOW,
    LOADFOLLOWINGPOSTS,
    DELETEACCOUNT,
    MUTE,
    UNMUTE,
    BLOCK,
    UNBLOCK,
    /*
     * this part is only for the chat part of the project
     */
    CREATECHATITEM,
    GETUSERS,
    GETCHATS



}
