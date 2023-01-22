package savkin.ilya.view.account.forms;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.BigDecimalField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.shared.Registration;
import savkin.ilya.model.Account;
import savkin.ilya.model.Client;
import savkin.ilya.model.enums.AccountCurrency;
import savkin.ilya.model.enums.AccountStatus;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


public class AccountCreationForm extends FormLayout {
    private final TextField number = new TextField("Номер счёта");
    private final BigDecimalField amount = new BigDecimalField("Первоначальная сумма");
    private final ComboBox<AccountStatus> status = new ComboBox<>("Статус");
    private final ComboBox<AccountCurrency> currency = new ComboBox<>("Валюта");

    private final TextField BIC = new TextField("БИК");
    private final ComboBox<Client> client = new ComboBox<>("Клиент");

    Binder<Account> binder = new BeanValidationBinder<>(Account.class);

    Button save = new Button("Сохранить");
    Button close = new Button("Отмена");

    private Account account;

    public AccountCreationForm(List<Client> clients) {
        addClassName("account-creation");

        status.setItems(AccountStatus.values());
        currency.setItems(AccountCurrency.values());
        client.setItems(clients);
        client.setItemLabelGenerator(Client::getFullName);

        binder.bindInstanceFields(this);

        add(number, amount, status, currency, BIC, client,
                createButtonsLayout());
    }

    public void setAccount(Account account) {
        this.account = account;
        binder.readBean(account);
    }



    private HorizontalLayout createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        save.addClickShortcut(Key.ENTER);
        close.addClickShortcut(Key.ESCAPE);

        save.addClickListener(event -> validateAndSave());
        close.addClickListener(event -> fireEvent(new AccountCreationForm.CloseEvent(this)));

        binder.addStatusChangeListener(e -> save.setEnabled(binder.isValid()));

        return new HorizontalLayout(save, close);
    }

    private void validateAndSave() {
        try {
            binder.writeBean(account);
            fireEvent(new SaveEvent(this, account, client.getValue()));
        } catch (ValidationException e) {
            e.printStackTrace();
        }
    }

    public static abstract class AccountCreationFormEvent extends ComponentEvent<AccountCreationForm> {
        private Account account;
        private Client client;

        protected AccountCreationFormEvent(AccountCreationForm source, Account account, Client client) {
            super(source, false);
            this.account = account;
            this.client = client;
        }

        public Account getAccount() {
            return account;
        }

        public Client getClient() {
            return client;
        }
    }

    public static class SaveEvent extends AccountCreationFormEvent {
        SaveEvent(AccountCreationForm source, Account account, Client client) {
            super(source, account, client);
        }
    }

    public static class CloseEvent extends AccountCreationFormEvent{
        CloseEvent(AccountCreationForm source) {
            super(source, null, null);
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                  ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }
}
