package br.com.ifce.model;

import java.util.Arrays;
import java.util.List;

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

    public static Agenda from(String agenda) {
        return Arrays.stream(Agenda.values()).filter(it -> it.name.equals(agenda))
            .findFirst()
            .orElse(null);
    }

    public static List<Agenda> getAll() {
        return Arrays.stream(Agenda.values()).toList();
    }

    public static List<Agenda> getAllExcept(Agenda agenda) {
        return Arrays.stream(Agenda.values())
            .filter(it -> !it.name.equals(agenda.name))
            .toList();
    }

    @Override
    public String toString() {
        return name;
    }
}
