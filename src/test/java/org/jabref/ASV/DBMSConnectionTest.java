package org.jabref.ASV;

import org.jabref.logic.shared.DBMSConnection;
import org.jabref.logic.shared.DBMSConnectionProperties;
import org.jabref.logic.shared.TestConnector;
import org.jabref.logic.shared.exception.InvalidDBMSConnectionPropertiesException;
import org.jabref.model.database.shared.DBMSType;
import org.jabref.testutils.category.DatabaseTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class DBMSConnectionTest {

    public DBMSConnection dbmsConnection;

    @Test
    void getAvailableDBMSTypes() {
        dbmsConnection = mock(DBMSConnection.class);
        assertTrue(dbmsConnection.getAvailableDBMSTypes().toString().contains("MySQL"));
    }
}
