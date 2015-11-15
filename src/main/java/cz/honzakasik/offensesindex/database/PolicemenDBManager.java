package cz.honzakasik.offensesindex.database;

import com.healthmarketscience.sqlbuilder.*;
import com.healthmarketscience.sqlbuilder.dbspec.basic.DbTable;

import java.sql.ResultSet;
import java.time.LocalDate;

import static cz.honzakasik.offensesindex.database.DatabaseNames.*;

/**
 * Created by Jan Kasik on 15.11.15.
 */
public class PolicemenDBManager {

    private DbTable policemenTable;
    private DbTable departmentsTable;
    private DbTable eventsTable;

    private DBManager dbManager;

    public PolicemenDBManager(DBManager dbManager) {
        this.dbManager = dbManager;
        policemenTable = dbManager.getPolicemenTable();
        eventsTable = dbManager.getPolicemenTable();
        departmentsTable = dbManager.getDepartmentsTable();
    }

    public ResultSet getPolicemenFromDepartmentWithinYear(String department, int year) {
        LocalDate start = LocalDate.ofYearDay(year, 1);
        LocalDate end = LocalDate.ofYearDay(year, start.isLeapYear() ? 366 : 365);
        Condition departmentCondition = BinaryCondition.equalTo(departmentsTable.findColumn(NAME), department);
        Condition comboCondition = ComboCondition.and(
                departmentCondition,
                BinaryCondition.greaterThan(eventsTable.findColumn(DATE), start, true),
                BinaryCondition.lessThan(eventsTable.findColumn(DATE), end, true));
        return getPolicemen(comboCondition);
    }

    public ResultSet getAllPolicemen() {
        return getPolicemen(BinaryCondition.EMPTY);
    }

    private ResultSet getPolicemen(Condition condition) {
        String policemenQuery = new SelectQuery()
                .addCustomColumns(
                        policemenTable.findColumn(NAME),
                        policemenTable.findColumn(SURNAME),
                        policemenTable.findColumn(NUMBER))
                .addAliasedColumn( FunctionCall.count().addColumnParams(eventsTable.findColumn(ID)), OFFENSES_COUNT)
                .addJoin(SelectQuery.JoinType.INNER,
                        eventsTable,
                        policemenTable,
                        BinaryCondition.equalTo(eventsTable.findColumn(POLICEMAN_ID), policemenTable.findColumn(ID)))
                .addJoin(SelectQuery.JoinType.INNER,
                        policemenTable,
                        departmentsTable,
                        BinaryCondition.equalTo(
                                policemenTable.findColumn(DEPARTMENT_ID),
                                departmentsTable.findColumn(ID)))
                .addCondition(condition)
                .addGroupings(
                        policemenTable.findColumn(NAME),
                        policemenTable.findColumn(SURNAME),
                        policemenTable.findColumn(NUMBER))
                .validate().toString();
        return dbManager.executeSQL(policemenQuery);
    }

}
