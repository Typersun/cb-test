package savkin.ilya.view.client;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import savkin.ilya.MainLayout;
import savkin.ilya.model.Client;
import savkin.ilya.service.ClientService;
import savkin.ilya.view.client.forms.ClientCreationForm;
import savkin.ilya.view.client.forms.ClientEditForm;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

@Route(value = "", layout = MainLayout.class)
@PageTitle("Клиенты")
public class ClientsView extends VerticalLayout {
    private final Grid<Client> grid = new Grid<>(Client.class);
    @Inject
    private ClientService clientService;

    private ClientEditForm clientEditForm;
    private ClientCreationForm clientCreationForm;

    @PostConstruct
    public void init() {
        addClassName("client-view");
        setSizeFull();
        configureCreationForm();
        configureGrid();
        configureEditForm();

        add(getToolbar(), getContent());
        updateGrid();
    }

    private Component getContent() {
        HorizontalLayout content = new HorizontalLayout(grid, clientEditForm, clientCreationForm);
        content.setFlexGrow(2, grid);
        content.setFlexGrow(1, clientEditForm);
        content.setFlexGrow(1, clientCreationForm);
        content.addClassNames("content");
        content.setSizeFull();
        return content;
    }

    private HorizontalLayout getToolbar() {
        Button addContactButton = new Button("Добавить клиента");
        addContactButton.addClickListener(click -> addClient());

        HorizontalLayout toolbar = new HorizontalLayout(addContactButton);
        toolbar.addClassName("toolbar");
        return toolbar;
    }

    private void configureGrid() {
        grid.addClassNames("client-grid");
        grid.setSizeFull();
        grid.setColumns("fullName", "phoneNumber", "inn", "address");
        grid.getColumns().forEach(col -> col.setAutoWidth(true));

        grid.asSingleSelect().addValueChangeListener(event ->
                editClient(event.getValue()));
    }

    private void configureCreationForm() {
        clientCreationForm = new ClientCreationForm();
        clientCreationForm.setWidth("30em");
        clientCreationForm.addListener(ClientCreationForm.SaveEvent.class, this::saveClient);
        clientCreationForm.addListener(ClientCreationForm.CloseEvent.class, e -> closeCreationEditor());
        clientCreationForm.setVisible(false);
    }

    private void configureEditForm() {
        clientEditForm = new ClientEditForm();
        clientEditForm.setWidth("25em");
        clientEditForm.addListener(ClientEditForm.UpdateEvent.class, this::updateClient);
        clientEditForm.addListener(ClientEditForm.DeleteEvent.class, this::deleteContact);
        clientEditForm.addListener(ClientEditForm.CloseEvent.class, e -> closeEditor());
        clientEditForm.setVisible(false);
    }

    private void updateGrid() {
        grid.setItems(clientService.findAllClients());
    }

    private void saveClient(ClientCreationForm.SaveEvent event) {
        clientService.saveClient(event.getClient());
        updateGrid();
        closeCreationEditor();
    }

    private void updateClient(ClientEditForm.UpdateEvent event) {
        clientService.updateClient(event.getClient());
        updateGrid();
        closeEditor();
    }

    private void deleteContact(ClientEditForm.DeleteEvent event) {
        clientService.deleteClient(event.getClient());
        updateGrid();
        closeEditor();
    }

    public void editClient(Client client) {
        if (client == null) {
            closeEditor();
        } else {
            clientEditForm.setClient(client);
            clientCreationForm.setVisible(false);
            clientEditForm.setVisible(true);
            addClassName("editing");
        }
    }

    private void addClient() {
        grid.asSingleSelect().clear();
        clientEditForm.setVisible(false);
        clientCreationForm.setClient(new Client());
        clientCreationForm.setVisible(true);
        addClassName("creation");
    }

    private void closeEditor() {
        clientEditForm.setClient(null);
        clientEditForm.setVisible(false);
        removeClassName("editing");
    }

    private void closeCreationEditor() {
        clientCreationForm.setClient(null);
        clientCreationForm.setVisible(false);
        removeClassName("creation");
    }
}
