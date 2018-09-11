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
    @Nullable
    private String PictureUrl;
    @Embedded
    private Address address;
    private boolean status;
    private Date entryDate;
    private Date soldDate;
    private long userId;
    private List<String> poi;
    //Miss carousel of picture and description maybe a list of Nested object


    public RealEstate() {
    }

    public RealEstate(String type, String area, String description, long price, int surface, int room,
                      int bathroom, int bedroom, @Nullable String pictureUrl, Address address,
                      Date entryDate,List<String> poiList, long userId) {
        this.type = type;
        this.area = area;
        this.description = description;
        this.price = price;
        this.surface = surface;
        this.room = room;
        this.bathroom = bathroom;
        this.bedroom = bedroom;
        PictureUrl = pictureUrl;
        this.address = address;
        this.userId = userId;
        this.status = false;
        this.poi = poiList;
        this.entryDate = entryDate;
        this.soldDate = null;
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

    // --- GETTER ---

    public long getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public String getArea() {
        return area;
    }

    public String getDescription() {
        return description;
    }

    public long getPrice() {
        return price;
    }

    public int getSurface() {
        return surface;
    }

    public int getRoom() {
        return room;
    }

    public int getBathroom() {
        return bathroom;
    }

    public int getBedroom() {
        return bedroom;
    }

    @Nullable
    public String getPictureUrl() {
        return PictureUrl;
    }

    public Address getAddress() {
        return address;
    }

    public boolean isStatus() {
        return status;
    }

    public Date getEntryDate() {
        return entryDate;
    }

    public Date getSoldDate() {
        return soldDate;
    }

    public long getUserId() {
        return userId;
    }

    public List<String> getPoi() {
        return poi;
    }

    // --- SETTER ---


    public void setId(long id) {
        this.id = id;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public void setSurface(int surface) {
        this.surface = surface;
    }

    public void setRoom(int room) {
        this.room = room;
    }

    public void setBathroom(int bathroom) {
        this.bathroom = bathroom;
    }

    public void setBedroom(int bedroom) {
        this.bedroom = bedroom;
    }

    public void setPictureUrl(@Nullable String pictureUrl) {
        PictureUrl = pictureUrl;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public void setEntryDate(Date entryDate) {
        this.entryDate = entryDate;
    }

    public void setSoldDate(Date soldDate) {
        this.soldDate = soldDate;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public void setPoi(List<String> poi) {
        this.poi = poi;
    }

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
        if (contentValues.containsKey("pictureUrl")) realEstate.setPictureUrl(contentValues.getAsString("pictureUrl"));
        if (contentValues.containsKey("detail_address_line1_num")) realEstate.setAddress((Address) contentValues.get("detail_address_line1_num")); //Risk
        if (contentValues.containsKey("status")) realEstate.setStatus(contentValues.getAsBoolean("status"));
        if (contentValues.containsKey("entryDate")) realEstate.setEntryDate((Date) contentValues.get("entryDate"));//Risk
        if (contentValues.containsKey("soldDate")) realEstate.setSoldDate((Date) contentValues.get("soldDate"));//Risk
        if (contentValues.containsKey("poi")) realEstate.setPoi((List<String>) contentValues.get("poi"));//Risk
        if (contentValues.containsKey("userId")) realEstate.setUserId(contentValues.getAsLong("userId"));
        return realEstate;
    }
}
