package mx.dwtraining.springtter.repositories;

import mx.dwtraining.springtter.interfaces.IReactionTypeRepository;
import mx.dwtraining.springtter.models.entity.ReactionType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;

@Repository
public class ReactionTypeRepository implements IReactionTypeRepository {
    private static final String SQL_SELECT_REACTION_TYPE = "select id, reaction from reaction_types where reaction = ?";
    private static final String SQL_INSERT_REACTION_TYPE = "insert into reaction_types (reaction) values (?)";
    private DataSource dataSource;

    @Autowired
    public ReactionTypeRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public ReactionType findOne(String reaction) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            connection = dataSource.getConnection();
            statement = connection.prepareStatement(SQL_SELECT_REACTION_TYPE);
            statement.setString(1, reaction);
            resultSet = statement.executeQuery();
            ReactionType reactionType = null;
            if (resultSet.next()) {
                reactionType = new ReactionType(
                    Long.parseLong(resultSet.getString("id")),
                    resultSet.getString("reaction")
                );
            }
            return reactionType;
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
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException exception) {
                    exception.printStackTrace();
                }
            }
        }
        return null;
    }

    @Override
    public boolean add(String reaction) {
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = dataSource.getConnection();
            statement = connection.prepareStatement(SQL_INSERT_REACTION_TYPE);
            statement.setString(1, reaction);
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
