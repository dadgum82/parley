package com.sidequest.parley.exception;

import java.sql.SQLException;

public class ForeignKeyConstraintException extends SQLException {
    public ForeignKeyConstraintException() {
        super();
    }

    public ForeignKeyConstraintException(String message) {
        super(message);
    }
}
