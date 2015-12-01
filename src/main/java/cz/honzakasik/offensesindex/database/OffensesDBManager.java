package cz.honzakasik.offensesindex.database;

import com.healthmarketscience.sqlbuilder.*;
import com.healthmarketscience.sqlbuilder.dbspec.basic.DbTable;
import cz.honzakasik.offensesindex.Helper;
import cz.honzakasik.offensesindex.offenses.OffensesTableItem;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

import static cz.honzakasik.offensesindex.database.DatabaseNames.*;

/**
 * Created by Jan Kasik on 15.11.15.
 */
public class OffensesDBManager {

    private DbTable offensesTable;
    private DbTable eventsTable;

    private DBManager dbManager;

    public OffensesDBManager(DBManager dbManager) {
        this.dbManager = dbManager;
        offensesTable = dbManager.getOffensesTable();
        eventsTable = dbManager.getEventsTable();
    }

    public ObservableList<OffensesTableItem> getOffenses(Condition condition) {
        String query = new SelectQuery()
                .addColumns(
                        offensesTable.findColumn(NAME),
                        offensesTable.findColumn(POINT_COUNT))
                .addAliasedColumn(
                        FunctionCall.count().addColumnParams(eventsTable.findColumns(ID)),
                        OFFENSES_COUNT)
                .addCustomJoin(SelectQuery.JoinType.INNER,
                        eventsTable,
                        offensesTable,
                        BinaryCondition.equalTo(eventsTable.findColumn(OFFENSE_ID), offensesTable.findColumn(ID)))
                .addCustomGroupings(offensesTable.findColumn(NAME), offensesTable.findColumn(POINT_COUNT))
                .addCondition(condition)
                .validate().toString();
        return transformResultToOffenseTableItemList(dbManager.executeSQL(query));
    }

    public ObservableList<OffensesTableItem> getAllOffenses() {
        return getOffenses(BinaryCondition.EMPTY);
    }

    public ObservableList<OffensesTableItem> getOffensesWithinMonths(LocalDate fromMonth, LocalDate toMonth, int pointCount) {
        CustomSql monthFromColumn = new CustomSql(eventsTable.findColumn(DATE).getColumnNameSQL());
        return getOffenses(ComboCondition.and()
                .addCondition(BinaryCondition
                        .greaterThan(monthFromColumn, fromMonth, true))
                .addCondition(BinaryCondition
                        .lessThan(monthFromColumn, toMonth, true))
                .addCondition(BinaryCondition
                        .equalTo(offensesTable.findColumn(POINT_COUNT), pointCount)));
    }

    public ObservableList<String> getDistinctOffensesCategories() {
        String query = new SelectQuery(true)
                .addColumns(offensesTable.findColumns(POINT_COUNT))
                .validate().toString();
        return DBHelper.transformStringData(dbManager.executeSQL(query), POINT_COUNT);
    }

    private ObservableList<OffensesTableItem> transformResultToOffenseTableItemList(ResultSet result) {
        ObservableList<OffensesTableItem> data = FXCollections.observableArrayList();
        try {
            while (result != null && result.next()) {
                OffensesTableItem item = new OffensesTableItem(
                        result.getString(NAME),
                        result.getInt(OFFENSES_COUNT)
                );
                data.add(item);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return data;
    }
}
