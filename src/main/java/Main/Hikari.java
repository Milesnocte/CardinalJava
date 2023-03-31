package Main;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class Hikari
{

    private static HikariConfig config = new HikariConfig();
    private static HikariDataSource ds;

    static
    {
        config.setDriverClassName("org.postgresql.Driver");
        config.setJdbcUrl("jdbc:postgresql://" + Credentials.PostgresIP + "/Cardinal");
        config.setUsername("postgres");
        config.setPassword(Credentials.PostgresPW);
        config.addDataSourceProperty("sslmode", "require");
        ds = new HikariDataSource(config);
    }

    public static Connection getConnection() throws SQLException
    {
        return ds.getConnection();
    }
}
