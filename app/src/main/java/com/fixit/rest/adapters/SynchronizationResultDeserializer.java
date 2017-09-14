package com.fixit.rest.adapters;

import com.fixit.data.DataModelObject;
import com.fixit.data.JobReason;
import com.fixit.data.Profession;
import com.fixit.synchronization.SynchronizationAction;
import com.fixit.synchronization.SynchronizationResult;
import com.fixit.utils.Constants;
import com.fixit.utils.FILog;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by konstantin on 4/3/2017.
 */

public class SynchronizationResultDeserializer implements JsonDeserializer<SynchronizationResult> {

    @Override
    public SynchronizationResult deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

        JsonObject jsonObject = json.getAsJsonObject();

        String name = jsonObject.get("name").getAsString();
        JsonArray resultsJson = jsonObject.getAsJsonArray("results");

        if(resultsJson != null && resultsJson.size() > 0) {
            if (name.equalsIgnoreCase(Profession.class.getSimpleName())) {
                return deserialize(name, resultsJson, Profession.class);
            } else if(name.equalsIgnoreCase(JobReason.class.getSimpleName())) {
                return deserialize(name, resultsJson, JobReason.class);
            } else {
                FILog.w(Constants.LOG_TAG_SYNCHRONIZATION, name + " is unsupported");
            }
        }

        return new Gson().fromJson(json, SynchronizationResult.class);
    }

    @SuppressWarnings("unchecked")
    private <T extends DataModelObject> SynchronizationResult<T> deserialize(String name, JsonArray resultsJson, Class<T> tClass) {
        Gson gson = new Gson();
        SynchronizationResult<T> synchronizationResult = new SynchronizationResult<>();
        synchronizationResult.setName(name);

        SynchronizationResult.Result<T>[] results = (SynchronizationResult.Result<T>[]) new SynchronizationResult.Result[resultsJson.size()];
        for(int i = 0; i < results.length; i++) {
            JsonObject jsonResult = resultsJson.get(i).getAsJsonObject();
            SynchronizationAction action = gson.fromJson(jsonResult.get("action"), SynchronizationAction.class);

            SynchronizationResult.Result<T> result = new SynchronizationResult.Result<>();
            result.setAction(action);

            if(jsonResult.has("data")) {
                JsonArray jsonData = jsonResult.getAsJsonArray("data");

                List<T> data = new ArrayList<>();
                for(int j = 0; j < jsonData.size(); j++) {
                    data.add(gson.fromJson(jsonData.get(j), tClass));
                }

                result.setData(data);
            } else if(jsonResult.has("ids")) {
                JsonArray jsonIds = jsonResult.getAsJsonArray("ids");

                Type idType = new TypeToken<Set<Long>>() {}.getType();
                Set<Long> ids = gson.fromJson(jsonIds, idType);

                result.setIds(ids);
            }
            results[i] = result;
        }
        synchronizationResult.setResults(results);
        synchronizationResult.setSupported(true);

        return synchronizationResult;
    }

    /*public static void main(String[] args) {
        SynchronizationResult<Profession> result = new SynchronizationResult();
        result.setName("profession");

        SynchronizationResult.Result<Profession> resultData = new SynchronizationResult.Result<>();
        resultData.setAction(new SynchronizationAction("insert", new Date()));
        List<Profession> professions = new ArrayList<>();
        professions.add(new Profession(1, "Plumber", "All you plumbing needs", "", true, new Date()));
        professions.add(new Profession(2, "Mechanic", "All you plumbing needs", "", true, new Date()));
        professions.add(new Profession(3, "Locksmith", "All you plumbing needs", "", true, new Date()));
        professions.add(new Profession(4, "Electrician", "All you plumbing needs", "", true, new Date()));
        professions.add(new Profession(5, "Air Con Repair Man", "All you plumbing needs", "", true, new Date()));
        resultData.setData(professions);

        SynchronizationResult.Result[] results = new SynchronizationResult.Result[1];
        results[0] = resultData;

        result.setResults(results);

        Gson gson = new GsonBuilder().registerTypeAdapter(SynchronizationResult.class, new SynchronizationResultDeserializer()).create();
        String json = gson.toJson(result);
        System.out.println(json);

        System.out.println("==============================");

        SynchronizationResult<Profession> dResult = gson.fromJson(json, SynchronizationResult.class);
        System.out.println(dResult);
    }*/

}
