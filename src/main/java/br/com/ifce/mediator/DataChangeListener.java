package br.com.ifce.mediator;

import br.com.ifce.model.Agenda;
import br.com.ifce.model.Contact;

import java.util.List;

public interface DataChangeListener {

    void onAgendaChange(Agenda agenda);

    void onContactsChange(List<Contact> contacts);
}
