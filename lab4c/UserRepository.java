package mx.dwtraining.springtter.repositories;

import mx.dwtraining.springtter.interfaces.IUserRepository;
import mx.dwtraining.springtter.models.dto.UserDTO;
import mx.dwtraining.springtter.models.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class UserRepository implements IUserRepository {
    private static final String SQL_SELECT_USERS = "select id, email, username, `password`, complete_name, profile_image, description from users";
    private static final String SQL_SELECT_USERS_TO_FOLLOW = "select id, username, complete_name, profile_image from users "+
        "where id <> ? and id not in (select friendships.user_id from friendships where friendships.follower_user_id = ?)";

    private DataSource dataSource;

    @Autowired
    public UserRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Iterable<User> findAll() {
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            Connection connection = dataSource.getConnection();
            statement = connection.prepareStatement(SQL_SELECT_USERS);
            resultSet = statement.executeQuery();
            List<User> userList = new ArrayList<>();
            while (resultSet.next()) {
                User user = new User(
                    Long.parseLong(resultSet.getString("id")),
                    resultSet.getString("email"),
                    resultSet.getString("username"),
                    "",
                    resultSet.getString("complete_name"),
                    resultSet.getString("profile_image"),
                    resultSet.getString("description")
                );
                userList.add(user);
            }
            return userList;
        } catch (SQLException exception) {
            exception.printStackTrace();
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException exception) {
                    exception.printStackTrace();
                }
            }
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException exception) {
                    exception.printStackTrace();
                }
            }
        }
        return null;
    }

    @Override
    public Iterable<UserDTO> findAllToFollow(long id) {
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            Connection connection = dataSource.getConnection();
            statement = connection.prepareStatement(SQL_SELECT_USERS_TO_FOLLOW);
            statement.setLong(1, id);
            statement.setLong(2, id);
            resultSet = statement.executeQuery();
            List<UserDTO> userList = new ArrayList<>();
            while (resultSet.next()) {
                UserDTO user = new UserDTO(
                    Long.parseLong(resultSet.getString("id")),
                    resultSet.getString("username"),
                    resultSet.getString("complete_name"),
                    resultSet.getString("profile_image")
                );
                userList.add(user);
            }
            return userList;
        } catch (SQLException exception) {
            exception.printStackTrace();
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException exception) {
                    exception.printStackTrace();
                }
            }
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException exception) {
                    exception.printStackTrace();
                }
            }
        }
        return null;
    }

    @Override
    public boolean add(User user) {
        return false;
    }

    @Override
    public boolean edit(User user) {
        return false;
    }

    @Override
    public void delete(long id) {

    }
}
