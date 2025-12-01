package com.user.management.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

@Service
public class SchemaService {
    @Autowired
    private DataSource dataSource;

    public void createCompanySchemaWithTransactionTable(String schemaName) {
        String schemaSql = "CREATE SCHEMA IF NOT EXISTS \"" + schemaName + "\"";
        String tableSql = "CREATE TABLE IF NOT EXISTS \"" + schemaName + "\".transaction (" +
                "id UUID PRIMARY KEY, " +
                "company_name VARCHAR(255), " +
                "device_id VARCHAR(255), " +
                "header VARCHAR(255), " +
                "raw_sms VARCHAR(255), " +
                "received_at TIMESTAMP(6), " +
                "staff_name VARCHAR(255), " +
                "timestamp TIMESTAMP(6), " +
                "user_id VARCHAR(255))";

        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(schemaSql);
            stmt.execute(tableSql);
        } catch (SQLException e) {
            throw new RuntimeException("Schema setup failed for company: " + schemaName, e);
        }
    }
}
