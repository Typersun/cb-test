package savkin.ilya.db.repository.impl;

import savkin.ilya.db.connection.PostgreSQLConnectionFactory;
import savkin.ilya.db.repository.ClientRepository;
import savkin.ilya.model.Client;

import javax.enterprise.inject.Default;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Default
public class ClientRepositoryImpl implements ClientRepository {

    @Override
    public List<Client> findAll() {
        Connection connection = PostgreSQLConnectionFactory.getConnection();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM clients");
            List<Client> clients = new ArrayList<>();
            while (resultSet.next()) {
                clients.add(Client.builder()
                        .id(resultSet.getInt("id"))
                        .fullName(resultSet.getString("full_name"))
                        .phoneNumber(resultSet.getString("phone_number"))
                        .inn(resultSet.getString("inn"))
                        .address(resultSet.getString("address"))
                        .build());
            }
            return clients;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return new ArrayList<>();
    }

    @Override
    public Optional<Client> save(Client client) {
        Connection connection = PostgreSQLConnectionFactory.getConnection();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO clients(full_name, phone_number," +
                    " inn, address) VALUES (?, ?, ?, ?);");
            preparedStatement.setString(1, client.getFullName());
            preparedStatement.setString(2, client.getPhoneNumber());
            preparedStatement.setString(3, client.getInn());
            preparedStatement.setString(4, client.getAddress());
            preparedStatement.execute();
            return Optional.of(client);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public Optional<Client> update(Client client) {
        Connection connection = PostgreSQLConnectionFactory.getConnection();

        try {
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE clients SET full_name = ?," +
                    " phone_number = ?, inn = ?, address = ? WHERE id = ?;");
            preparedStatement.setString(1, client.getFullName());
            preparedStatement.setString(2, client.getPhoneNumber());
            preparedStatement.setString(3, client.getInn());
            preparedStatement.setString(4, client.getAddress());
            preparedStatement.setInt(5, client.getId());
            preparedStatement.execute();
            return Optional.of(client);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public void delete(Client client) {
        Connection connection = PostgreSQLConnectionFactory.getConnection();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM clients WHERE id = (?);");
            preparedStatement.setInt(1, client.getId());
            preparedStatement.execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
