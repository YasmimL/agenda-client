package br.com.ifce.network.rmi;

import br.com.ifce.model.Contact;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Set;

public interface AgendaService extends Remote {

    static String name() throws RemoteException {
        return "AgendaService";
    }

    String checkConnection() throws RemoteException;

    void add(Contact contact) throws RemoteException;

    void remove(Contact contact) throws RemoteException;

    List<Contact> getAll() throws RemoteException;

    void update(Contact contact, String phoneNumber) throws RemoteException;

    void sync(Set<Contact> contacts) throws RemoteException;
}
