package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static jm.task.core.jdbc.util.Util.getConnection;

public class UserDaoJDBCImpl implements UserDao {

    private final String CREATE_TABLE_STATEMENT = "CREATE TABLE IF NOT EXISTS `user` (" +
            "id BIGINT NOT NULL AUTO_INCREMENT,\n" +
            "name varchar(30) NOT NULL,\n" +
            "lastName varchar(30) NOT NULL,\n" +
            "age TINYINT NOT NULL,\n" +
            "PRIMARY KEY (id)" +
            ");";

    private final String DROP_USER_STATEMENT = "DROP TABLE IF EXISTS `user`;";
    private final String SAVE_USER_STATEMENT = "INSERT INTO `user` (name, lastName, age)" +
            " VALUES (?, ?, ?)";
    private final String CLEAR_USER_STATEMENT = "DELETE FROM `user`";
    private final String GET_ALL_USER_STATEMENT = "SELECT * FROM `user`";
    private final String DELETE_USER_STATEMENT = "DELETE FROM `user` WHERE id = ?";

    private Connection connection = getConnection();

    public UserDaoJDBCImpl() {

    }

    public void executeStatement(String sql) {
        try {
            Connection connection = getConnection();
            Statement statement = connection.createStatement();
            statement.execute(sql);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public void createUsersTable() {
        executeStatement(CREATE_TABLE_STATEMENT);
    }

    public void dropUsersTable() {
        executeStatement(DROP_USER_STATEMENT);
    }

    public void saveUser(String name, String lastName, byte age) {

        try {
            Connection connection = getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SAVE_USER_STATEMENT);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, lastName);
            preparedStatement.setByte(3, age);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void removeUserById(long id) {
        PreparedStatement preparedStatement = null;
        try {

            preparedStatement = connection.prepareStatement(DELETE_USER_STATEMENT);
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public List<User> getAllUsers() {

        List<User> users = new ArrayList<User>();
        try {
            Connection connection = getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(GET_ALL_USER_STATEMENT);
            while (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getLong("id"));
                user.setName(resultSet.getString("name"));
                user.setLastName(resultSet.getString("lastName"));
                user.setAge(resultSet.getByte("age"));
                users.add(user);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return users;
    }

    public void cleanUsersTable() {
        executeStatement(CLEAR_USER_STATEMENT);
    }
}
