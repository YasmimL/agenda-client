package br.com.ifce;

import br.com.ifce.mediator.AgendaMediator;
import br.com.ifce.network.rmi.SyncService;
import br.com.ifce.view.MainView;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                var view = new MainView();
                AgendaMediator.getInstance().setListener(view);
                AgendaMediator.getInstance().setAgenda(SyncService.getAvailableAgenda());
                SyncService.periodicallyUpdateAgenda();
                view.show();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }
}