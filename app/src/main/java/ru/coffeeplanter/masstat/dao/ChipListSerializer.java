package ru.coffeeplanter.masstat.dao;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.plumillonforge.android.chipview.Chip;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import ru.coffeeplanter.masstat.entities.Keyword;

/**
 * Class helping gson library to (de)serialize List<Chip> objects.
 */

public class ChipListSerializer implements JsonSerializer<List<Chip>>, JsonDeserializer<List<Keyword>> {

    @Override
    public JsonElement serialize(List<Chip> src, Type typeOfSrc, JsonSerializationContext context) {

        JsonArray keywords = new JsonArray();

        Gson gson = new Gson();
        JsonParser parser = new JsonParser();

        for (Chip keyword : src) {
            keywords.add(parser.parse(gson.toJson(keyword)).getAsJsonObject());
        }

        return keywords;

    }

    @Override
    public List<Keyword> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

        JsonArray jsonArray = json.getAsJsonArray();

        List<Keyword> keywords = new ArrayList<>();

        for (JsonElement keyword : jsonArray) {
            JsonObject jsonKeyword = keyword.getAsJsonObject();
            int id = jsonKeyword.get("id").getAsInt();
            String name = jsonKeyword.get("name").getAsString();
            int personId = jsonKeyword.get("person_id").getAsInt();
            Keyword keywordObject = new Keyword(id, name, personId);
            keywords.add(keywordObject);
        }

        Collections.sort(keywords, new Comparator<Keyword>() {
            @Override
            public int compare(Keyword o1, Keyword o2) {
                return o1.getName().compareTo(o2.getName());
            }
        });

        return keywords;

    }

}
