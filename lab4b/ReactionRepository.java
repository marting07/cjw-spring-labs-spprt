package mx.dwtraining.springtter.repositories;

import mx.dwtraining.springtter.interfaces.IReactionRepository;
import mx.dwtraining.springtter.models.dto.ReactionDTO;
import mx.dwtraining.springtter.models.entity.Reaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ReactionRepository implements IReactionRepository {
    private static final String SQL_SELECT_REACTIONS = "select reactions.id, users.complete_name, users.username, users.profile_image, reactions.`date`, reactions.`comment` "+
        "from reactions inner join users on reactions.reacted_user_id = users.id inner join reaction_types on reactions.reaction_type_id = reaction_types.id "+
        "where reactions.springtter_id = ? and reaction_types.reaction = ?";
    private static final String SQL_INSERT_REACTION = "insert into reactions "+
        "(springtter_id, reacted_user_id, reaction_type_id, `comment`, `date`) "+
        "values (?, ?, ?, ?, ?)";
    private DataSource dataSource;

    @Autowired
    public ReactionRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Iterable<ReactionDTO> findAll(long springtterId, String reaction) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            connection = dataSource.getConnection();
            statement = connection.prepareStatement(SQL_SELECT_REACTIONS);
            statement.setLong(1, springtterId);
            statement.setString(2, reaction);
            resultSet = statement.executeQuery();
            List<ReactionDTO> reactionDTOList = new ArrayList<>();
            while (resultSet.next()) {
                ReactionDTO reactionDTO = new ReactionDTO(
                    Long.parseLong(resultSet.getString("id")),
                    resultSet.getString("complete_name"),
                    resultSet.getString("username"),
                    resultSet.getString("profile_image"),
                    resultSet.getTimestamp("date").toLocalDateTime(),
                    resultSet.getString("comment")
                );
                reactionDTOList.add(reactionDTO);
            }
            return reactionDTOList;
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
    public boolean add(Reaction reaction) {
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = dataSource.getConnection();
            statement = connection.prepareStatement(SQL_INSERT_REACTION);
            statement.setLong(1, reaction.getSpringtterId());
            statement.setLong(2, reaction.getReactedUserId());
            statement.setLong(3, reaction.getReactionTypeId());
            statement.setString(4, reaction.getComment());
            statement.setDate(5, Date.valueOf(reaction.getDate().toLocalDate()));
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
