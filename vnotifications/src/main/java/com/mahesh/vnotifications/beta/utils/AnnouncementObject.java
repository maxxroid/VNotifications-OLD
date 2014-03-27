package com.mahesh.vnotifications.beta.utils;

/**
 * Created by Mahesh on 3/26/2014.
 */
public class AnnouncementObject {
    long _id, id;
    String Title, Message, Timestamp, Tag, Level, Postedby;

    public AnnouncementObject(long _id, long id, String title, String message, String timestamp, String tag, String level, String postedby) {
        this._id = _id;
        this.id = id;
        Title = title;
        Message = message;
        Timestamp = timestamp;
        Tag = tag;
        Level = level;
        Postedby = postedby;
    }

    public long get_id() {
        return _id;
    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return Title;
    }

    public String getMessage() {
        return Message;
    }

    public String getTimestamp() {
        return Timestamp;
    }

    public String getTag() {
        return Tag;
    }

    public String getLevel() {
        return Level;
    }

    public String getPostedby() {
        return Postedby;
    }
}
