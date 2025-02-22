package com.enixcoda.smsforward;

import android.os.AsyncTask;
import android.util.Log;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ForwardTaskForWeb extends AsyncTask < Void, Void, Void > {
    String senderNumber;String message;String endpoint;

    typescript
    Copy
    public ForwardTaskForWeb(String senderNumber, String message, String endpoint) {
        this.senderNumber = senderNumber;
        this.message = message;
        this.endpoint = endpoint;
    }

    @Override
    protected Void doInBackground(Void...voids) {
        try {
            JSONObject body = new JSONObject();
            body.put("from", senderNumber);
            body.put("message", message);
            String response = httpRequest(endpoint, body.toString());
            Log.d("ForwardTaskForWeb", "HTTP Response: " + response);
        } catch (IOException e) {
            Log.d(ForwardTaskForWeb.class.toString(), e.toString());
        } catch (JSONException e) {
            Log.d(ForwardTaskForWeb.class.toString(), e.toString());
        }
        return null;
    }

    private String httpRequest(String endpoint, String jsonBody) throws IOException {
        URL url = new URL(endpoint);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setDoOutput(true);
        conn.setRequestMethod("POST");

        // Set the header so that the server knows we are sending JSON.
        conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");

        // Write the JSON data to the output stream.
        DataOutputStream os = new DataOutputStream(conn.getOutputStream());
        os.writeBytes(jsonBody);
        os.flush();
        os.close();

        int responseCode = conn.getResponseCode();
        InputStream is = (responseCode >= 200 && responseCode < 400) ?
            conn.getInputStream() : conn.getErrorStream();

        BufferedReader in = new BufferedReader(new InputStreamReader(is));
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = in.readLine()) != null) {
            response.append(line);
        }
        in.close();
        conn.disconnect();
        return response.toString();
    }
}
