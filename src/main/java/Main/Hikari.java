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
        config.setUsername(Credentials.PostgresUser);
        config.setPassword(Credentials.PostgresPassword);
        config.addDataSourceProperty("sslmode", "require");
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        ds = new HikariDataSource(config);
    }

    public static Connection getConnection() throws SQLException
    {
        return ds.getConnection();
    }
}
