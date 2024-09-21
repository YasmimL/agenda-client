package br.com.ifce.model;

import java.io.Serializable;
import java.util.Objects;

public record Contact(String name, String phoneNumber) implements Serializable {
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Contact contact = (Contact) o;
        return Objects.equals(name, contact.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return name + " " + phoneNumber;
    }
}
