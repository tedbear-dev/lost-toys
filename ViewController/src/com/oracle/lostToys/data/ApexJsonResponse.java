package com.oracle.lostToys.data;

import oracle.adfmf.json.JSONArray;
import oracle.adfmf.json.JSONObject;

public class ApexJsonResponse {
    
    public ApexJsonResponse() {
        super();
    }

    private JSONObject next;

    public void setNext(JSONObject next) {
        this.next = next;
    }

    public JSONObject getNext() {
        return next;
    }

    public void setItems(JSONArray items) {
        this.items = items;
    }

    public JSONArray getItems() {
        return items;
    }
    private JSONArray items;
}
