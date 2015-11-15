package cz.honzakasik.offensesindex.database;

import com.healthmarketscience.sqlbuilder.*;
import com.healthmarketscience.sqlbuilder.dbspec.basic.DbTable;

import java.sql.ResultSet;
import java.time.LocalDate;

import static cz.honzakasik.offensesindex.database.DatabaseNames.*;
import static cz.honzakasik.offensesindex.database.DatabaseNames.DATE;

/**
 * Created by Jan Kasik on 15.11.15.
 */
public class DepartmentsDBManager {

    private DbTable departmentsTable;
    private DbTable policemenTable;
    private DbTable eventsTable;

    private DBManager dbManager;

    public DepartmentsDBManager(DBManager dbManager) {
        this.dbManager = dbManager;
        departmentsTable = dbManager.getDepartmentsTable();
        policemenTable = dbManager.getPolicemenTable();
        eventsTable = dbManager.getEventsTable();
    }

    public ResultSet getDepartments(Condition condition) {
        String customQuery = new SelectQuery()
                .addCustomColumns(
                        departmentsTable.findColumn(CITY),
                        departmentsTable.findColumn(NAME),
                        departmentsTable.findColumn(ID))
                .addAliasedColumn(FunctionCall.count().addColumnParams(eventsTable.findColumn(ID)),
                        OFFENSES_COUNT)
                .addCustomJoin(SelectQuery.JoinType.INNER,
                        eventsTable, policemenTable,
                        BinaryCondition.equalTo(
                                eventsTable.findColumn(POLICEMAN_ID),
                                policemenTable.findColumn(ID)))
                .addCustomJoin(SelectQuery.JoinType.INNER,
                        policemenTable, departmentsTable,
                        BinaryCondition.equalTo(
                                policemenTable.findColumn(DEPARTMENT_ID),
                                departmentsTable.findColumn(ID)))
                .addCustomGroupings
                        (departmentsTable.findColumn(CITY),
                                departmentsTable.findColumn(NAME),
                                departmentsTable.findColumn(ID))
                .addCondition(condition)
                .validate().toString();
        return dbManager.executeSQL(customQuery);
    }

    public ResultSet getAllDepartments() {
        return getDepartments(BinaryCondition.EMPTY);
    }

    public ResultSet getAllDepartmentsNames() {
        String query = new SelectQuery(true)
                .addColumns(departmentsTable.findColumns(NAME))
                .validate().toString();
        return dbManager.executeSQL(query);
    }

    public ResultSet getDepartmentsWithinYear(int year) {
        LocalDate start = LocalDate.ofYearDay(year, 1);
        LocalDate end = LocalDate.ofYearDay(year, start.isLeapYear() ? 366 : 365);
        return getDepartments(ComboCondition.and(
                BinaryCondition.greaterThan(eventsTable.findColumn(DATE), start, true),
                BinaryCondition.lessThan(eventsTable.findColumn(DATE), end, true)));
    }
}
