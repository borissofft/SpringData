package com.example.demoWeb.util;

public interface XmlConverter {

    <T> T deserialize(String input, Class<T> type);

    String serialize(Object o);

}
