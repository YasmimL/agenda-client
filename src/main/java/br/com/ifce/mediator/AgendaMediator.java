package br.com.ifce.mediator;

import br.com.ifce.model.Agenda;
import br.com.ifce.model.Contact;

import java.util.List;

public class AgendaMediator {

    private Agenda agenda;

    private DataChangeListener listener;

    private static final AgendaMediator INSTANCE = new AgendaMediator();

    public static AgendaMediator getInstance() {
        return INSTANCE;
    }

    private AgendaMediator() {
    }

    public void setListener(DataChangeListener listener) {
        this.listener = listener;
    }

    public Agenda getAgenda() {
        return agenda;
    }

    public void setAgenda(Agenda agenda) {
        this.agenda = agenda;
        if (this.listener != null) this.listener.onAgendaChange(agenda);
    }

    public void updateContacts(List<Contact> contacts) {
        if (this.listener != null) this.listener.onContactsChange(contacts);
    }
}
