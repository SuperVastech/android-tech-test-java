package com.bridge.androidtechnicaltest.network;

import java.util.UUID;

public class RequestHeaderHelper {

    public static String generateRequestId() {
        return UUID.randomUUID().toString();
    }

    public static String getUserAgent() {
        return "BridgeApp/1.0.0 (Android)";
    }
}