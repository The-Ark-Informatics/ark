package au.org.theark.core.model.config.entity;

public enum PropertyType {
    CHARACTER("Character"), //0
    DATE("Date"), //1
    NUMBER("Number"), //2
    FILE("File"); //3

    private final String name;

    PropertyType(String s) {
        name = s;
    }
}
