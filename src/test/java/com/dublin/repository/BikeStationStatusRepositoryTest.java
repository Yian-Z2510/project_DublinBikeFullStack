package com.dublin.repository;

import com.dublin.entity.BikeStationStatus;
import org.apache.ibatis.builder.xml.XMLMapperBuilder;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.h2.jdbcx.JdbcDataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BikeStationStatusRepositoryTest {

    private SqlSessionFactory sqlSessionFactory;

    @BeforeEach
    void setUp() throws Exception {
        JdbcDataSource dataSource = new JdbcDataSource();
        dataSource.setURL("jdbc:h2:mem:bike-status;MODE=MySQL;DB_CLOSE_DELAY=-1");
        dataSource.setUser("sa");

        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute("DROP TABLE IF EXISTS bike_station_status");
            statement.execute("CREATE TABLE bike_station_status ("
                    + "id BIGINT AUTO_INCREMENT PRIMARY KEY, "
                    + "number INTEGER NOT NULL, "
                    + "last_update TIMESTAMP NOT NULL, "
                    + "available_bikes INTEGER, "
                    + "available_bike_stands INTEGER, "
                    + "status VARCHAR(128), "
                    + "CONSTRAINT uk_bike_station_status_number_last_update UNIQUE (number, last_update))");
        }

        Environment environment = new Environment("test", new JdbcTransactionFactory(), dataSource);
        Configuration configuration = new Configuration(environment);
        configuration.setMapUnderscoreToCamelCase(true);
        try (InputStream mapperXml = Resources.getResourceAsStream("mapper/BikeStationStatusMapper.xml")) {
            new XMLMapperBuilder(
                    mapperXml,
                    configuration,
                    "mapper/BikeStationStatusMapper.xml",
                    configuration.getSqlFragments()
            ).parse();
        }
        sqlSessionFactory = new SqlSessionFactoryBuilder().build(configuration);
    }

    @Test
    void duplicateUpstreamSnapshotIsStoredOnlyOnce() {
        LocalDateTime timestamp = LocalDateTime.of(2026, 8, 25, 12, 0);

        try (SqlSession session = sqlSessionFactory.openSession(true)) {
            BikeStationStatusRepository repository = session.getMapper(BikeStationStatusRepository.class);
            repository.insertBatch(Arrays.asList(snapshot(42, timestamp, 5)));
            repository.insertBatch(Arrays.asList(snapshot(42, timestamp, 7)));

            List<BikeStationStatus> persisted = repository.findByNumber(42);
            assertEquals(1, persisted.size());
            assertEquals(7, persisted.get(0).getAvailableBikes());
        }
    }

    @Test
    void latestAndRecentQueriesUseStableNewestFirstOrdering() {
        LocalDateTime oldest = LocalDateTime.of(2026, 8, 25, 10, 0);
        LocalDateTime middle = LocalDateTime.of(2026, 8, 25, 11, 0);
        LocalDateTime newest = LocalDateTime.of(2026, 8, 25, 12, 0);

        try (SqlSession session = sqlSessionFactory.openSession(true)) {
            BikeStationStatusRepository repository = session.getMapper(BikeStationStatusRepository.class);
            repository.insertBatch(Arrays.asList(
                    snapshot(42, middle, 6),
                    snapshot(42, oldest, 4),
                    snapshot(42, newest, 8)
            ));

            assertEquals(newest, repository.findLatestByNumber(42).getLastUpdate());
            List<BikeStationStatus> recent = repository.findRecentByNumber(42, 3);
            assertEquals(Arrays.asList(newest, middle, oldest), Arrays.asList(
                    recent.get(0).getLastUpdate(),
                    recent.get(1).getLastUpdate(),
                    recent.get(2).getLastUpdate()
            ));
        }
    }

    private BikeStationStatus snapshot(Integer number, LocalDateTime timestamp, Integer availableBikes) {
        BikeStationStatus status = new BikeStationStatus();
        status.setNumber(number);
        status.setLastUpdate(timestamp);
        status.setAvailableBikes(availableBikes);
        status.setAvailableBikeStands(20 - availableBikes);
        status.setStatus("OPEN");
        return status;
    }
}
