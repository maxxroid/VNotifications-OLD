package com.mahesh.vnotifications.beta.utils;

public class DrawerItem {

    String ItemName;
    int unreadCount;
    String title;

    public DrawerItem(String itemName, int unreadCount) {
        ItemName = itemName;
        this.unreadCount = unreadCount;
    }


    public DrawerItem(String title) {
        this(null, 0);
        this.title = title;
    }

    public String getItemName() {
        return ItemName;
    }

    public void setItemName(String itemName) {
        ItemName = itemName;
    }

    public int getunreadCount() {
        return unreadCount;
    }

    public void setunreadCount(int unreadCount) {
        this.unreadCount = unreadCount;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


}
