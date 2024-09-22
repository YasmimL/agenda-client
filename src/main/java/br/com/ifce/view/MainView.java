package br.com.ifce.view;

import br.com.ifce.mediator.DataChangeListener;
import br.com.ifce.model.Agenda;
import br.com.ifce.model.Contact;
import br.com.ifce.network.rmi.SyncService;

import javax.swing.*;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.rmi.RemoteException;
import java.util.List;

public class MainView implements DataChangeListener {

    private final JFrame frame = new JFrame("Agenda");

    private final JLabel agendaLabel = new JLabel("");

    private final DefaultListModel<Contact> contactListModel = new DefaultListModel<>();

    private final JList<Contact> contactList = new JList<>(this.contactListModel);

    public void show() throws UnsupportedLookAndFeelException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        final int frameWidth = 300;
        final int frameHeight = 450;
        this.frame.setSize(frameWidth, frameHeight);
        this.frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        JPanel contentPanel = new JPanel();
        contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        this.frame.setContentPane(contentPanel);

        this.renderHeadingPanel();
        this.renderMainPanel();

        this.frame.setVisible(true);
    }

    private void renderHeadingPanel() {
        JPanel panel = new JPanel();

        this.agendaLabel.setFont(new Font("Serif", Font.PLAIN, 24));
        panel.add(this.agendaLabel, BorderLayout.WEST);

        this.frame.add(panel);
    }

    private void renderMainPanel() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.X_AXIS));

        mainPanel.add(this.renderContactsPanel());

        this.frame.add(mainPanel);
    }

    private JPanel renderContactsPanel() {
        final int width = 300;
        final int height = 400;

        JPanel contactsPanel = new JPanel(new BorderLayout());
        contactsPanel.setPreferredSize(new Dimension(width, height));

        JTextPane header = new JTextPane();
        header.setEditable(false);
        header.setBackground(new Color(1, 87, 155));
        header.setForeground(Color.WHITE);
        header.setFont(new Font(Font.MONOSPACED, Font.BOLD, 14));
        header.setText("Contacts");
        StyledDocument doc = header.getStyledDocument();
        SimpleAttributeSet center = new SimpleAttributeSet();
        StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
        doc.setParagraphAttributes(0, doc.getLength(), center, false);
        contactsPanel.add(header, BorderLayout.NORTH);

        this.contactList.setSelectionMode(0);
        this.contactList.setBounds(100, 100, 75, 75);
        this.contactList.setFixedCellHeight(30);

        JScrollPane scrollPane = new JScrollPane(this.contactList);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setPreferredSize(new Dimension(width, 200));
        contactsPanel.add(scrollPane, BorderLayout.CENTER);

        contactsPanel.add(this.renderActionsPanel(), BorderLayout.SOUTH);

        return contactsPanel;
    }

    private JPanel renderActionsPanel() {
        JPanel actionsPanel = new JPanel();
        actionsPanel.setLayout(new BoxLayout(actionsPanel, BoxLayout.X_AXIS));

        actionsPanel.add(this.renderAddContactButton());
        actionsPanel.add(Box.createRigidArea(new Dimension(5, 0)));
        actionsPanel.add(this.renderUpdateContactButton());
        actionsPanel.add(Box.createRigidArea(new Dimension(5, 0)));
        actionsPanel.add(this.renderRemoveContactButton());

        return actionsPanel;
    }

    private JButton renderAddContactButton() {
        JButton button = new JButton("Add");
        button.addActionListener(e -> this.showAddContactDialog());

        return button;
    }

    private void showAddContactDialog() {
        final var nameField = new JTextField();
        final var phoneNumberField = new JTextField();
        final var fields = new Object[]{
            "Name", nameField,
            "Phone Number", phoneNumberField
        };

        var option = JOptionPane.showConfirmDialog(frame, fields, "Add Contact", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (option != JOptionPane.OK_OPTION) return;

        final var name = nameField.getText();
        final var phoneNumber = phoneNumberField.getText();

        if (name.isBlank() || phoneNumber.isBlank()) return;

        this.handleAddContact(new Contact(name.trim(), phoneNumber.trim()));
    }

    private void handleAddContact(Contact contact) {
        try {
            final var remoteService = SyncService.getRemoteService();
            remoteService.add(contact);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private JButton renderUpdateContactButton() {
        JButton button = new JButton("Update");
        button.addActionListener(e -> this.showUpdateContactDialog());

        return button;
    }

    private void showUpdateContactDialog() {
        if (this.contactList.getSelectedValue() == null) return;

        final var selectedContact = this.contactList.getSelectedValue();
        final var phoneNumberField = new JTextField(selectedContact.phoneNumber());
        final var fields = new Object[]{
            "Phone Number", phoneNumberField
        };

        var option = JOptionPane.showConfirmDialog(frame, fields, "Update " + selectedContact.name(), JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (option != JOptionPane.OK_OPTION) return;

        final var phoneNumber = phoneNumberField.getText();
        if (phoneNumber.isBlank()) return;

        this.handleUpdateContact(selectedContact, phoneNumber.trim());
    }

    private void handleUpdateContact(Contact contact, String phoneNumber) {
        try {
            final var remoteService = SyncService.getRemoteService();
            remoteService.update(contact, phoneNumber);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private JButton renderRemoveContactButton() {
        JButton button = new JButton("Remove");
        button.addActionListener(e -> {
            if (this.contactList.getSelectedValue() == null) return;
            try {
                final var remoteService = SyncService.getRemoteService();
                remoteService.remove(this.contactList.getSelectedValue());
            } catch (RemoteException ex) {
                ex.printStackTrace();
            }
        });

        return button;
    }

    @Override
    public void onAgendaChange(Agenda agenda) {
        if (agenda != null) {
            this.agendaLabel.setText("Connected to " + agenda);
        } else {
            this.agendaLabel.setText("Disconnected...");
        }
    }

    @Override
    public void onContactsChange(List<Contact> contacts) {
        final var selectedContact = this.contactList.getSelectedValue();
        this.contactListModel.clear();
        this.contactListModel.addAll(contacts);
        if (selectedContact != null && this.contactListModel.contains(selectedContact)) {
            this.contactList.setSelectedValue(selectedContact, true);
        }
    }
}
