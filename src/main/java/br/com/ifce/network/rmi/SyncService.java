package br.com.ifce.network.rmi;

import br.com.ifce.model.Agenda;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class SyncService {

    private Agenda agenda;

    private static final SyncService INSTANCE = new SyncService();

    public static SyncService getInstance() {
        return INSTANCE;
    }

    private SyncService() {

    }

    private AgendaService lookupService(Agenda agenda) throws MalformedURLException, NotBoundException {
        try {
            return (AgendaService) Naming.lookup(agenda + "/" + AgendaService.name());
        } catch (RemoteException e) {
            return null;
        }
    }

    public AgendaService getRemoteService() {
        try {
            AgendaService service = this.lookupService(this.agenda);
            while (service == null) {
                for (var agenda : Agenda.getAllExcept(this.agenda)) {
                    service = this.lookupService(agenda);
                    if (service != null) break;
                }
                break;
            }

            return service;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public Agenda getAgenda() {
        return agenda;
    }

    public void setAgenda(Agenda agenda) {
        this.agenda = agenda;
    }
}
