package com.ocr.ocrdocs.utils;

import java.util.Objects;

public enum DocsTypes {
    MAIL("Письмо", "письма"),
    PROTOCOL("Протокол", "протоколы"),
    ORDER("Приказ", "приказы"),
    REGULATION("Регламент", "регламенты"),
    CONTRACT("Договор", "договоры"),
    ACT("Акт", "акты"),
    COMPLAIN("Претензия", "претензии"),
    REFERENCE("Справка", "справки"),
    UNKNOWN("Неизвестные", "несортированные");
    private String description;
    private String path;

    DocsTypes(String description, String path) {
        this.path = path;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public String getPath() {
        return path;
    }

    public static DocsTypes findType(String type) {
        for (DocsTypes val : values()) {
            if (Objects.equals(val.description.toLowerCase(), type.toLowerCase())) {
                return val;
            }
        }

        return UNKNOWN;

    }
}
