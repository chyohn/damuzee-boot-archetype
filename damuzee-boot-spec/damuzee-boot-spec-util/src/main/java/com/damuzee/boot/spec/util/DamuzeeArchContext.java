package com.damuzee.boot.spec.util;

import java.util.HashMap;

public enum DamuzeeArchContext {
    GROUP_ID("damuzee-group-id"),
    TENANT_ID("damuzee-tenant-id");

    private static final ThreadLocal<Opaque> CONTEXT = ThreadLocal.withInitial(Opaque::new);

    final String key;

    DamuzeeArchContext(String key) {
        this.key = key;
    }

    public void set(String value) {
        if (value == null) {
            CONTEXT.get().remove(key);
        } else {
            CONTEXT.get().put(key, value);
        }
    }

    public String get() {
        return CONTEXT.get().get(key);
    }

    public static Opaque capture() {
        Opaque data = new Opaque();
        data.putAll(CONTEXT.get());
        return data;
    }

    public static void restore(Opaque data) {
        CONTEXT.get().putAll(data);
    }

    public static void clear() {
        CONTEXT.remove();
    }

    public static class Opaque extends HashMap<String, String> {

    }
}
