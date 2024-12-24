package com.ftn.eventhopper.clients.deserializers;

import com.google.gson.*;
import java.lang.reflect.Type;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class LocalTimeAdapter implements JsonDeserializer<LocalTime>, JsonSerializer<LocalTime> {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_TIME;

    @Override
    public LocalTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return LocalTime.parse(json.getAsString(), formatter);
    }

    @Override
    public JsonElement serialize(LocalTime src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(src.format(formatter));
    }
}
