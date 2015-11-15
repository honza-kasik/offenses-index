package cz.honzakasik.offensesindex.database;

import com.healthmarketscience.sqlbuilder.*;
import com.healthmarketscience.sqlbuilder.dbspec.basic.DbTable;
import cz.honzakasik.offensesindex.departments.DepartmentTableItem;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.logging.Level;

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

    public ObservableList<DepartmentTableItem> getDepartments(Condition condition) {
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
        return transformDepartmentTableData(dbManager.executeSQL(customQuery));
    }

    public ObservableList<DepartmentTableItem> getAllDepartments() {
        return getDepartments(BinaryCondition.EMPTY);
    }

    public ResultSet getAllDepartmentsNames() {
        String query = new SelectQuery(true)
                .addColumns(departmentsTable.findColumns(NAME))
                .validate().toString();
        return dbManager.executeSQL(query);
    }

    public ObservableList<DepartmentTableItem> getDepartmentsWithinYear(int year) {
        LocalDate start = LocalDate.ofYearDay(year, 1);
        LocalDate end = LocalDate.ofYearDay(year, start.isLeapYear() ? 366 : 365);
        return getDepartments(ComboCondition.and(
                BinaryCondition.greaterThan(eventsTable.findColumn(DATE), start, true),
                BinaryCondition.lessThan(eventsTable.findColumn(DATE), end, true)));
    }

    public static ObservableList<DepartmentTableItem> transformDepartmentTableData(ResultSet result) {
        ObservableList<DepartmentTableItem> data = FXCollections.observableArrayList();
        try {
            while (result != null && result.next()) {
                DepartmentTableItem item = new DepartmentTableItem(
                        result.getString(NAME),
                        result.getString(CITY),
                        result.getInt(OFFENSES_COUNT),
                        result.getInt(ID)
                );
                data.add(item);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return data;
    }
}
