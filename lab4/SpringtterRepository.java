package mx.dwtraining.springtter.repositories;

import mx.dwtraining.springtter.interfaces.ISpringtterRepository;
import mx.dwtraining.springtter.models.dto.SpringtterDTO;
import mx.dwtraining.springtter.models.entity.Springtter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class SpringtterRepository implements ISpringtterRepository {
    private static final String SQL_SELECT_SPRINGTTERS = ""+
        "select springtters.id, springtters.user_id, users.complete_name, users.username, users.profile_image, "+
        "springtters.message, springtters.`date`, springtters.`image`, springtters.blocked "+
        "from springtters inner join users on springtters.user_id = users.id";
    private static final String SQL_INSERT_SPRINGTTER = "insert into springtters (user_id, message, date, image, blocked) values (?, ?, ?, ?, ?)";
    private static final String SQL_UPDATE_SPRINGTTER = "update springtters set message = ? where id = ?";
    private static final String SQL_DELETE_SPRINGTTER = "delete from springtters where id = ?";

    private DataSource dataSource;

    @Autowired
    public SpringtterRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Iterable<SpringtterDTO> findAll() {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            connection = dataSource.getConnection();
            statement = connection.prepareStatement(SQL_SELECT_SPRINGTTERS);
            resultSet = statement.executeQuery();
            List<SpringtterDTO> springtterList = new ArrayList<>();
            while (resultSet.next()) {
                SpringtterDTO springtter = new SpringtterDTO(
                    Long.parseLong(resultSet.getString("id")),
                    Long.parseLong(resultSet.getString("user_id")),
                    resultSet.getString("complete_name"),
                    resultSet.getString("username"),
                    resultSet.getString("profile_image"),
                    resultSet.getString("message"),
                    resultSet.getTimestamp("date").toLocalDateTime(),
                    resultSet.getString("image"),
                    Boolean.parseBoolean(resultSet.getString("blocked"))
                );
                springtterList.add(springtter);
            }
            return springtterList;
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
    public boolean add(Springtter springtter) {
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = dataSource.getConnection();
            statement = connection.prepareStatement(SQL_INSERT_SPRINGTTER);
            statement.setLong(1, springtter.getUserId());
            statement.setString(2, springtter.getMessage());
            statement.setDate(3, Date.valueOf(springtter.getDate().toLocalDate()));
            statement.setString(4, springtter.getImage());
            statement.setBoolean(5, springtter.isBlocked());
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

    public boolean edit(long id, String message) {
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = dataSource.getConnection();
            statement = connection.prepareStatement(SQL_UPDATE_SPRINGTTER);
            statement.setString(1, message);
            statement.setLong(2, id);
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

    @Override
    public void delete(long id) {
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = dataSource.getConnection();
            statement = connection.prepareStatement(SQL_DELETE_SPRINGTTER);
            statement.setLong(1, id);
            statement.execute();
        } catch (SQLException exception) {
            exception.printStackTrace();
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
            }
        }
    }
}
