package com.github.tetrisanalyzer.text;

public class Parameter {
    public final String name;
    public final String value;

    public static Parameter fromLong(String name, long value) {
        return new Parameter(name, format(value));
    }

    public static Parameter fromString(String name, String value) {
        return new Parameter(name, value);
    }

    private Parameter(String name, String value) {
        this.name = name;
        this.value = value;
    }

    private static String format(long value) {
        return String.format("%,d", value).replace((char)160, (char)32);
    }

    @Override
    public boolean equals(Object that) {
        if (this == that) return true;
        if (that == null || getClass() != that.getClass()) return false;

        Parameter parameter = (Parameter) that;

        if (name != null ? !name.equals(parameter.name) : parameter.name != null) return false;
        if (value != null ? !value.equals(parameter.value) : parameter.value != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (value != null ? value.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Parameter{" +
                "name='" + name + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
