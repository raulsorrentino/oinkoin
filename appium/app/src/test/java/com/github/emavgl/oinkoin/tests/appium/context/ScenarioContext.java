package com.github.emavgl.oinkoin.tests.appium.context;

import java.util.HashMap;
import java.util.Map;

public class ScenarioContext {

    private final Map<String, Object> context = new HashMap<>();

    public void set(String key, Object value) {
        context.put(key, value);
    }

    @SuppressWarnings("unchecked")
    public <T> T get(String key) {
        return (T) context.get(key);
    }

    public void clear() {
        context.clear();
    }
}
