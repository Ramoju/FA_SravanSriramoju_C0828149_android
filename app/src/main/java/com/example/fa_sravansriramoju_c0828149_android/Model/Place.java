package com.example.fa_sravansriramoju_c0828149_android.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class Place implements Parcelable {
    private int id;
    private String title;
    private String address;
    private int visited;
    private double latitude;
    private double longitude;

    protected Place(Parcel in) {
        id = in.readInt();
        title = in.readString();
        address = in.readString();
        visited = in.readInt();
        latitude = in.readDouble();
        longitude = in.readDouble();
    }

    public Place(){

    }

    public static final Creator<Place> CREATOR = new Creator<Place>() {
        @Override
        public Place createFromParcel(Parcel in) {
            return new Place(in);
        }

        @Override
        public Place[] newArray(int size) {
            return new Place[size];
        }
    };

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int isVisited() {
        return visited;
    }

    public void setVisited(int visited) {
        this.visited = visited;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(title);
        parcel.writeString(address);
        parcel.writeInt(visited);
        parcel.writeDouble(latitude);
        parcel.writeDouble(longitude);
    }
}
