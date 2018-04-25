package com.huashengke.com.tools;

import com.google.gson.*;
import jdk.nashorn.internal.ir.annotations.Ignore;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Date;

public class AbstractSerializer {

    private Gson gson;

    protected AbstractSerializer(boolean isCompact) {
        this(false, isCompact);
    }

    protected AbstractSerializer(boolean isPretty, boolean isCompact) {
        super();

        if (isPretty && isCompact) {
            this.buildForPrettyCompact();
        } else if (isCompact) {
            this.buildForCompact();
        } else {
            this.build();
        }
    }

    protected Gson gson() {
        return this.gson;
    }

    private void build() {
        this.gson = new GsonBuilder()
                .addSerializationExclusionStrategy(new IgnoreStrategy())
                .addDeserializationExclusionStrategy(new IgnoreStrategy())
                .registerTypeAdapter(Date.class, new DateSerializer())
                .registerTypeAdapter(Date.class, new DateDeserializer()).serializeNulls().create();
    }

    private void buildForCompact() {
        this.gson = new GsonBuilder().registerTypeAdapter(Date.class, new DateSerializer())
                .registerTypeAdapter(Date.class, new DateDeserializer()).create();
    }

    private void buildForPrettyCompact() {
        this.gson = new GsonBuilder().registerTypeAdapter(Date.class, new DateSerializer())
                .registerTypeAdapter(Date.class, new DateDeserializer()).setPrettyPrinting().create();
    }

    private class DateSerializer implements JsonSerializer<Date> {
        public JsonElement serialize(Date source, Type typeOfSource, JsonSerializationContext context) {
            return new JsonPrimitive(Long.toString(source.getTime()));
        }
    }

    private class DateDeserializer implements JsonDeserializer<Date> {
        public Date deserialize(JsonElement json, Type typeOfTarget, JsonDeserializationContext context) throws JsonParseException {
            long time = Long.parseLong(json.getAsJsonPrimitive().getAsString());
            return new Date(time);
        }
    }

    static class IgnoreStrategy implements ExclusionStrategy {

        @Override
        public boolean shouldSkipField(FieldAttributes fieldAttributes) {
            Collection<Annotation> annotations = fieldAttributes.getAnnotations();
            for (Annotation annotation : annotations) {
                if (annotation.annotationType() == Ignore.class) {
                    return true;
                }
            }
            return false;
        }

        @Override
        public boolean shouldSkipClass(Class<?> aClass) {
            return false;
        }
    }
}
