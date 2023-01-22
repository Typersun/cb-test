package savkin.ilya.view.client.forms;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.shared.Registration;
import savkin.ilya.model.Client;

public class ClientCreationForm extends FormLayout {
    private final TextField fullName = new TextField("ФИО");
    private final TextField phoneNumber = new TextField("Телефонный номер");
    private final TextField inn = new TextField("ИНН");
    private final TextField address = new TextField("Адрес");

    Binder<Client> binder = new BeanValidationBinder<>(Client.class);

    private Client client;

    Button save = new Button("Сохранить");
    Button close = new Button("Отмена");

    public ClientCreationForm() {
        addClassName("client-creation-form");
        fullName.setHelperText("Например: Иванов Иван Иванович");
        phoneNumber.setPlaceholder("+7");
        phoneNumber.setHelperText("Должен быть уникальный");
        binder.bindInstanceFields(this);

        add(fullName, phoneNumber, inn, address,
                createButtonsLayout());
    }

    public void setClient(Client client) {
        this.client = client;
        binder.readBean(client);
    }

    private HorizontalLayout createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        save.addClickShortcut(Key.ENTER);
        close.addClickShortcut(Key.ESCAPE);

        save.addClickListener(event -> validateAndSave());
        close.addClickListener(event -> fireEvent(new ClientCreationForm.CloseEvent(this)));

        binder.addStatusChangeListener(e -> save.setEnabled(binder.isValid()));

        return new HorizontalLayout(save, close);
    }

    private void validateAndSave() {
        try {
            binder.writeBean(client);
            fireEvent(new ClientCreationForm.SaveEvent(this, client));
        } catch (ValidationException e) {
            e.printStackTrace();
        }
    }

    public static abstract class ClientCreationFormEvent extends ComponentEvent<ClientCreationForm> {
        private Client client;

        protected ClientCreationFormEvent(ClientCreationForm source, Client client) {
            super(source, false);
            this.client = client;
        }

        public Client getClient() {
            return client;
        }
    }

    public static class SaveEvent extends ClientCreationForm.ClientCreationFormEvent {
        SaveEvent(ClientCreationForm source, Client client) {
            super(source, client);
        }
    }

    public static class CloseEvent extends ClientCreationForm.ClientCreationFormEvent  {
        CloseEvent(ClientCreationForm source) {
            super(source, null);
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                  ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }
}
