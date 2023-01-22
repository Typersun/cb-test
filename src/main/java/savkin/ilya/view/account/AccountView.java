package savkin.ilya.view.account;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import savkin.ilya.MainLayout;
import savkin.ilya.model.Account;
import savkin.ilya.service.AccountService;
import savkin.ilya.service.ClientService;
import savkin.ilya.view.account.forms.AccountCreationForm;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

@Route(value = "accounts", layout = MainLayout.class)
@PageTitle("Аккаунты")
public class AccountView extends VerticalLayout {

    private final Grid<Account> grid = new Grid<>(Account.class, false);
    private AccountCreationForm accountCreationForm;

    @Inject
    private AccountService accountService;
    @Inject
    private ClientService clientService;

    @PostConstruct
    public void init() {
        addClassName("accounts-view");
        setSizeFull();

        configureCreationForm();
        configureGrid();

        add(getToolbar(), getContent());
        updateGrid();

    }

    private Component getContent() {
        HorizontalLayout content = new HorizontalLayout(grid, accountCreationForm);
        content.setFlexGrow(2, grid);
        content.setFlexGrow(1, accountCreationForm);
        content.addClassNames("content");
        content.setSizeFull();
        return content;
    }

    private HorizontalLayout getToolbar() {
        Button addContactButton = new Button("Добавить счёт");
        addContactButton.addClickListener(click -> addAccount());

        HorizontalLayout toolbar = new HorizontalLayout(addContactButton);
        toolbar.addClassName("toolbar");
        return toolbar;
    }

    private void configureGrid() {
        grid.addColumn(Account::getNumber).setHeader("Номер счёта");
        grid.addColumn(Account::getAmount).setHeader("Сумма на счёте");
        grid.addColumn(account -> account.getCurrency().toString()).setHeader("Валюта счёта");
        grid.addColumn(account -> account.getStatus().toString()).setHeader("Статус");
        grid.addColumn(Account::getBIC).setHeader("БИК");
        grid.addColumn(Account::getClientFullName).setHeader("ФИО владельца");

        grid.getColumns().forEach(col -> col.setAutoWidth(true));
    }

    private void configureCreationForm() {
        accountCreationForm = new AccountCreationForm(clientService.findAllClients());
        accountCreationForm.setWidth("30em");
        accountCreationForm.addListener(AccountCreationForm.SaveEvent.class, this::saveAccount);
        accountCreationForm.addListener(AccountCreationForm.CloseEvent.class, e -> closeCreationEditor());
        accountCreationForm.setVisible(false);
    }

    private void addAccount() {
        grid.asSingleSelect().clear();
        accountCreationForm.setVisible(true);
        accountCreationForm.setAccount(new Account());
        addClassName("creation");
    }

    private void saveAccount(AccountCreationForm.SaveEvent event) {
        accountService.saveAccount(event.getAccount(), event.getClient());
        updateGrid();
        closeCreationEditor();
    }

    private void updateGrid() {
        grid.setItems(accountService.findAllAccounts());
    }

    private void closeCreationEditor() {
        accountCreationForm.setAccount(null);
        accountCreationForm.setVisible(false);
        removeClassName("creation");
    }

}
