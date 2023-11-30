package mx.dwtraining.springtter.repositories;

import mx.dwtraining.springtter.interfaces.IFriendshipRepository;
import mx.dwtraining.springtter.models.entity.Friendship;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@Repository
public class FriendshipRepository implements IFriendshipRepository {
    public static final String SQL_INSERT_FRIENDSHIP = "insert into friendships (user_id, follower_user_id) values (?, ?)";

    private DataSource dataSource;

    @Autowired
    public FriendshipRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public boolean add(Friendship friendship) {
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = dataSource.getConnection();
            statement = connection.prepareStatement(SQL_INSERT_FRIENDSHIP);
            statement.setLong(1, friendship.getUserId());
            statement.setLong(2, friendship.getFollowerUserId());
            statement.execute();
        } catch (SQLException exception) {
            exception.printStackTrace();
            return false;
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException exception) {
                exception.printStackTrace();
                return false;
            }
        }
        return true;
    }
}
