package com.huashengke.com.tools;

import javax.sql.DataSource;
import java.sql.*;
import java.util.*;
import java.util.Date;

public class QueryUtil {
    private DataSource dataSource;

    protected QueryUtil(DataSource aDataSource) {
        super();
        this.dataSource = aDataSource;
    }

    protected void close(Statement aStatement, ResultSet aResult) {
        if (aStatement != null) {
            try {
                aStatement.close();
            } catch (Exception e) {
                // ignore
            }
        }
        if (aResult != null) {
            try {
                aResult.close();
            } catch (Exception e) {
                // ignore
            }
        }
        ConnectionProvider.closeConnection();
    }

    protected <T> T queryObject(Class<T> aClass, String aQuery, JoinOn aJoinOn,
                                Object... anArguments) {
        return queryObject(aClass, aQuery, aJoinOn, null, anArguments);
    }

    protected <T> T queryObject(Class<T> aClass, String aQuery, JoinOn aJoinOn,
                                Set<String> objectFieldSet, Object... anArguments) {

        T object = null;
        Connection connection = ConnectionProvider.connection(this.dataSource);
        PreparedStatement selectStatement = null;
        ResultSet result = null;

        try {
            selectStatement = connection.prepareStatement(aQuery);

            this.setStatementArguments(selectStatement, anArguments);

            result = selectStatement.executeQuery();

            if (result.next()) {
                object = this.mapResultToType(result, aClass, aJoinOn,
                        objectFieldSet);
            }

        } catch (Exception e) {
            throw new IllegalStateException("Cannot query: " + aQuery, e);
        } finally {
            this.close(selectStatement, result);
        }

        return object;
    }

    protected <T> Collection<T> queryObjects(Class<T> aClass, String aQuery,
                                             JoinOn aJoinOn, Object... anArguments) {
        return queryObjects(aClass, aQuery, aJoinOn, null, anArguments);
    }

    protected <T> Collection<T> queryObjects(Class<T> aClass, String aQuery,
                                             JoinOn aJoinOn, Set<String> objectFieldSet, Object... anArguments) {

        List<T> objects = new ArrayList<T>();

        Connection connection = ConnectionProvider.connection(this.dataSource);
        PreparedStatement selectStatement = null;
        ResultSet result = null;

        try {
            selectStatement = connection.prepareStatement(aQuery);

            this.setStatementArguments(selectStatement, anArguments);

            result = selectStatement.executeQuery();

            while (result.next()) {
                T object = this.mapResultToType(result, aClass, aJoinOn,
                        objectFieldSet);

                objects.add(object);
            }

        } catch (Exception e) {
            throw new IllegalStateException("Cannot query: " + aQuery, e);
        } finally {
            this.close(selectStatement, result);
        }

        return objects;
    }

    protected Set<String> queryStrings(String anQuery, Object... anArguments) {
        Set<String> values = new LinkedHashSet<String>();

        Connection connection = ConnectionProvider.connection(this.dataSource);
        PreparedStatement selectStatement = null;
        ResultSet result = null;

        try {
            selectStatement = connection.prepareStatement(anQuery);

            this.setStatementArguments(selectStatement, anArguments);

            result = selectStatement.executeQuery();

            while (result.next()) {
                values.add(result.getString(1));
            }

        } catch (Exception e) {
            throw new IllegalStateException("Cannot query: " + anQuery, e);
        } finally {
            this.close(selectStatement, result);
        }

        return values;
    }

    protected String queryString(String aQuery, Object... anArguments) {

        String value = null;

        Connection connection = ConnectionProvider.connection(this.dataSource);
        PreparedStatement selectStatement = null;
        ResultSet result = null;

        try {
            selectStatement = connection.prepareStatement(aQuery);

            this.setStatementArguments(selectStatement, anArguments);

            result = selectStatement.executeQuery();

            if (result.next()) {
                value = result.getString(1);
            }

        } catch (Exception e) {
            throw new IllegalStateException("Cannot query: " + aQuery, e);
        } finally {
            this.close(selectStatement, result);
        }

        return value;
    }

    protected java.io.InputStream queryStream(String aQuery, Object... args) {
        java.io.InputStream value = null;

        Connection connection = ConnectionProvider.connection(this.dataSource);
        PreparedStatement selectStatement = null;
        ResultSet result = null;

        try {
            selectStatement = connection.prepareStatement(aQuery);

            this.setStatementArguments(selectStatement, args);

            result = selectStatement.executeQuery();

            if (result.next()) {
                value = result.getBinaryStream(1);
            }

        } catch (Exception e) {
            throw new IllegalStateException("Cannot query: " + aQuery, e);
        } finally {
            this.close(selectStatement, result);
        }

        return value;
    }

    private <T> T mapResultToType(ResultSet aResultSet, Class<T> aClass,
                                  JoinOn aJoinOn, Set<String> objectFieldSet) {
        ResultSetObjectMapper<T> mapper = new ResultSetObjectMapper<T>(
                aResultSet, aClass, aJoinOn, objectFieldSet);

        return mapper.mapResultToType();
    }

    private void setStatementArguments(PreparedStatement aPreparedStatement,
                                       Object[] anArguments) throws SQLException {

        for (int idx = 0; idx < anArguments.length; ++idx) {
            Object argument = anArguments[idx];
            Class<?> argumentType = argument.getClass();

            if (argumentType == String.class) {
                aPreparedStatement.setString(idx + 1, (String) argument);
            } else if (argumentType == Integer.class) {
                aPreparedStatement.setInt(idx + 1, (Integer) argument);
            } else if (argumentType == Long.class) {
                aPreparedStatement.setLong(idx + 1, (Long) argument);
            } else if (argumentType == Boolean.class) {
                aPreparedStatement.setBoolean(idx + 1, (Boolean) argument);
            } else if (argumentType == Date.class) {
                java.sql.Date sqlDate = new java.sql.Date(
                        ((Date) argument).getTime());
                aPreparedStatement.setDate(idx + 1, sqlDate);
            } else if (argumentType == Double.class) {
                aPreparedStatement.setDouble(idx + 1, (Double) argument);
            } else if (argumentType == Float.class) {
                aPreparedStatement.setFloat(idx + 1, (Float) argument);
            }
        }
    }
}
