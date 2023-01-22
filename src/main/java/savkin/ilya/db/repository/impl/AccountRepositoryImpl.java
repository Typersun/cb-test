package savkin.ilya.db.repository.impl;

import savkin.ilya.db.connection.PostgreSQLConnectionFactory;
import savkin.ilya.db.repository.AccountRepository;
import savkin.ilya.model.Account;
import savkin.ilya.model.enums.AccountCurrency;
import savkin.ilya.model.enums.AccountStatus;

import javax.enterprise.inject.Default;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Default
public class AccountRepositoryImpl implements AccountRepository {
    @Override
    public Optional<Account> save(Account account) {
        Connection connection = PostgreSQLConnectionFactory.getConnection();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO accounts(number, amount, status, \"BIC\", currency, client_id) " +
                    "VALUES (?, ?, ?, ?, ?, ?)" );
            preparedStatement.setString(1, account.getNumber());
            preparedStatement.setBigDecimal(2, account.getAmount());
            preparedStatement.setString(3, account.getStatus().toString());
            preparedStatement.setString(4, account.getBIC());
            preparedStatement.setString(5, account.getCurrency().toString());
            preparedStatement.setInt(6, account.getClientId());
            preparedStatement.execute();
            return Optional.of(account);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public Optional<Account> update(Account account) {
        return Optional.empty();
    }

    @Override
    public void delete(Account account) {

    }

    @Override
    public List<Account> findAll() {
        Connection connection = PostgreSQLConnectionFactory.getConnection();

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT accounts.id, \"number\", amount, status, \"BIC\"," +
                    " currency, client_id, clients.full_name\n" +
                    "\tFROM accounts\n" +
                    "\tLEFT JOIN clients ON accounts.client_id=public.clients.id");
            List<Account> accounts = new ArrayList<>();
            while (resultSet.next()) {
                accounts.add(Account.builder()
                                .id(resultSet.getInt("id"))
                                .number(resultSet.getString("number"))
                                .amount(resultSet.getBigDecimal("amount"))
                                .status(AccountStatus.valueOf(resultSet.getString("status")))
                                .BIC(resultSet.getString("BIC"))
                                .currency(AccountCurrency.valueOf(resultSet.getString("currency")))
                                .clientFullName(resultSet.getString("full_name"))
                                .clientId(resultSet.getInt("client_id"))
                        .build());
            }
            return accounts;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return new ArrayList<>();
    }
}
