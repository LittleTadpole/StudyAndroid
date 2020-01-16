package com.wxzd.gfzdj.global.utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 */
public class HttpBody {
    JSONObject result;

    public HttpBody() {
        result = new JSONObject();
    }

    public HttpBody addParams(String key, Object value) throws JSONException {
        result.put(key, value);
        return this;
    }


    public RequestBody toBody() {
        return RequestBody.create(MediaType.parse("application/json"), result.toString());
    }


    public static List<MultipartBody.Part> filesToMultipartBodyParts(List<String> lists, String
            key) {

        List<MultipartBody.Part> parts = new ArrayList<>(lists.size());
        for (String filePath : lists) {
            File file = new File(filePath);
            RequestBody requestBody = RequestBody.create(MediaType.parse("image/png"), file);
            MultipartBody.Part part = MultipartBody.Part.createFormData(key, file.getName(), requestBody);
            parts.add(part);
        }
        return parts;
    }

}
