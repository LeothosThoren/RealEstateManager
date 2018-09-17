package com.openclassrooms.realestatemanager.entities;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;
import android.content.ContentValues;
import android.support.annotation.Nullable;

import java.util.Date;
import java.util.List;


@Entity(foreignKeys = @ForeignKey(entity = User.class,
        parentColumns = "id",
        childColumns = "userId"))
public class RealEstate {

    @PrimaryKey(autoGenerate = true)
    private long id;

    private String type;
    private String area;
    private String description;
    private long price;
    private int surface;
    private int room;
    private int bathroom;
    private int bedroom;
    @Embedded
    private Address address;
    private boolean status;
    private Date entryDate;
    private Date soldDate;
    private long userId;
    private List<String> poi;
    @Nullable
    private List<String> pictureUrl;
    private List<String> title;
    private String urlVideo;


    public RealEstate() {
    }

    public RealEstate(String type, String area, String description, long price, int surface, int room,
                      int bathroom, int bedroom, @Nullable List<String> pictureUrl, List<String> title,
                      Address address, Date entryDate, List<String> poiList, String urlVideo, long userId) {
        this.type = type;
        this.area = area;
        this.description = description;
        this.price = price;
        this.surface = surface;
        this.room = room;
        this.bathroom = bathroom;
        this.bedroom = bedroom;
        this.pictureUrl = pictureUrl;
        this.title = title;
        this.address = address;
        this.userId = userId;
        this.status = false;
        this.poi = poiList;
        this.entryDate = entryDate;
        this.soldDate = null;
        this.urlVideo = urlVideo;
    }


    public RealEstate(String type, String area, long price, long userId) {
        this.type = type;
        this.area = area;
        this.price = price;
        this.userId = userId;
        this.status = false;
        this.entryDate = null;
        this.soldDate = null;
    }

    // --- GETTERS / SETTERS ---

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public int getSurface() {
        return surface;
    }

    public void setSurface(int surface) {
        this.surface = surface;
    }

    public int getRoom() {
        return room;
    }

    public void setRoom(int room) {
        this.room = room;
    }

    public int getBathroom() {
        return bathroom;
    }

    public void setBathroom(int bathroom) {
        this.bathroom = bathroom;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public List<String> getTitle() {
        return title;
    }

    public void setTitle(List<String> title) {
        this.title = title;
    }

    public List<String> getPoi() {
        return poi;
    }

    public void setPoi(List<String> poi) {
        this.poi = poi;
    }


    public int getBedroom() {
        return bedroom;
    }

    public void setBedroom(int bedroom) {
        this.bedroom = bedroom;
    }

    @Nullable
    public List<String> getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(@Nullable List<String> pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public Date getEntryDate() {
        return entryDate;
    }

    public void setEntryDate(Date entryDate) {
        this.entryDate = entryDate;
    }

    public Date getSoldDate() {
        return soldDate;
    }

    public void setSoldDate(Date soldDate) {
        this.soldDate = soldDate;
    }

    public String getUrlVideo() {return urlVideo;
    }

    public void setUrlVideo(String urlVideo) {this.urlVideo = urlVideo;}

    // --- Utils ---

    public static RealEstate fromContentValues(ContentValues contentValues) {
        final RealEstate realEstate = new RealEstate();
        if (contentValues.containsKey("type")) realEstate.setType(contentValues.getAsString("type"));
        if (contentValues.containsKey("area")) realEstate.setArea(contentValues.getAsString("area"));
        if (contentValues.containsKey("description")) realEstate.setDescription(contentValues.getAsString("description"));
        if (contentValues.containsKey("price")) realEstate.setPrice(contentValues.getAsLong("price"));
        if (contentValues.containsKey("surface")) realEstate.setSurface(contentValues.getAsInteger("surface"));
        if (contentValues.containsKey("room")) realEstate.setRoom(contentValues.getAsInteger("room"));
        if (contentValues.containsKey("bathroom")) realEstate.setBathroom(contentValues.getAsInteger("bathroom"));
        if (contentValues.containsKey("bedroom")) realEstate.setBedroom(contentValues.getAsInteger("bedroom"));
        if (contentValues.containsKey("pictureUrl")) realEstate.setPictureUrl((List<String>) contentValues.get("pictureUrl"));
        if (contentValues.containsKey("title")) realEstate.setTitle((List<String>) contentValues.get("title"));
        if (contentValues.containsKey("detail_address_line1_num")) realEstate.setAddress((Address) contentValues.get("detail_address_line1_num")); //Risk
        if (contentValues.containsKey("status")) realEstate.setStatus(contentValues.getAsBoolean("status"));
        if (contentValues.containsKey("entryDate")) realEstate.setEntryDate((Date) contentValues.get("entryDate"));//Risk
        if (contentValues.containsKey("soldDate")) realEstate.setSoldDate((Date) contentValues.get("soldDate"));//Risk
        if (contentValues.containsKey("poi")) realEstate.setPoi((List<String>) contentValues.get("poi"));//Risk
        if (contentValues.containsKey("urlVideo")) realEstate.setUrlVideo(contentValues.getAsString("urlVideo"));
        if (contentValues.containsKey("userId")) realEstate.setUserId(contentValues.getAsLong("userId"));
        return realEstate;
    }
}
