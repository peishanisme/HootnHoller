package com.firstapp.hootnholler.entity;

public class Announcement {

    String announcementTitle,announcementContent;
    long timestamp;

public  Announcement(){

}
    public Announcement(String announcementTitle,String announcementContent,long timestamp){
        this.announcementContent=announcementContent;
        this.announcementTitle=announcementTitle;
        this.timestamp=timestamp;
    }

    public String getAnnouncementTitle() {
        return announcementTitle;
    }

    public void setAnnouncementTitle(String announcementTitle) {
        this.announcementTitle = announcementTitle;
    }

    public String getAnnouncementContent() {
        return announcementContent;
    }

    public void setAnnouncementContent(String announcementContent) {
        this.announcementContent = announcementContent;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}

