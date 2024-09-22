package br.com.ifce.model;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum Agenda {

    AGENDA_ONE("agenda1"),
    AGENDA_TWO("agenda2"),
    AGENDA_THREE("agenda3");

    private final String name;

    Agenda(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static List<Agenda> getAll() {
        return Arrays.stream(Agenda.values()).collect(Collectors.toList());
    }

    @Override
    public String toString() {
        return name;
    }
}
