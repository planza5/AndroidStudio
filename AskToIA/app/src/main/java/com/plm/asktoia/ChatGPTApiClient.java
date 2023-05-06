package com.plm.asktoia;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import okhttp3.*;
import java.io.IOException;

public class ChatGPTApiClient {
    private static final String API_KEY = "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";
    private static final String API_URL = "https://api.openai.com/v1/completions";
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private OkHttpClient httpClient;
    private Gson gson;

    public ChatGPTApiClient() {
        this.httpClient = new OkHttpClient();
        this.gson = new Gson();
    }

    public String sendTextToChatGPT(String inputText) {
        JsonObject payload = createPayload(inputText);
        RequestBody requestBody = RequestBody.create(gson.toJson(payload), JSON);

        Request request = new Request.Builder()
                .url(API_URL)
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "Bearer " + API_KEY)
                .post(requestBody)
                .build();

        try (Response response = httpClient.newCall(request).execute()) {
            if (response.isSuccessful()) {
                String responseBody = response.body().string();
                return extractAnswerFromResponse(responseBody);
            } else {
                throw new IOException("Error en la solicitud: " + response);
            }
        } catch (IOException e) {
            return e.getMessage();
        }
    }

    private JsonObject createPayload(String inputText) {
        JsonObject payload = new JsonObject();
        payload.addProperty("model","text-davinci-003");
        payload.addProperty("prompt", inputText);
        payload.addProperty("temperature", 1.0);
        payload.addProperty("max_tokens", 150);
        return payload;
    }


    private String extractAnswerFromResponse(String responseBody) {
        JsonObject responseObject = gson.fromJson(responseBody, JsonObject.class);
        return responseObject.getAsJsonArray("choices").get(0).getAsJsonObject().get("text").getAsString().trim();
    }


}
