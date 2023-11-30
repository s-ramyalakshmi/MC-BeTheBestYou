package com.example.mcprojectv5;

import com.google.gson.annotations.SerializedName;

public class DistanceMatrixResponse {
    @SerializedName("destination_addresses")
    public String[] destinationAddresses;

    @SerializedName("origin_addresses")
    public String[] originAddresses;

    @SerializedName("rows")
    public Row[] rows;

    @SerializedName("status")
    public String status;

    public static class Row {
        @SerializedName("elements")
        public Element[] elements;
    }

    public static class Element {
        @SerializedName("distance")
        public Info distance;

        @SerializedName("duration")
        public Info duration;

        @SerializedName("duration_in_traffic")
        public Info duration_in_traffic;

        @SerializedName("status")
        public String status;
    }

    public static class Info {
        @SerializedName("text")
        public String text;

        @SerializedName("value")
        public long value;
    }
}
