package com.datamelt.dataprotection.rest;

public class Response
{
    private final long uuid;
    private final String result;

    public Response(long uuid, String result) {
        this.uuid = uuid;
        this.result = result;
    }

    public long getUuid() {
        return uuid;
    }

    public String getResult() {
        return result;
    }
}
