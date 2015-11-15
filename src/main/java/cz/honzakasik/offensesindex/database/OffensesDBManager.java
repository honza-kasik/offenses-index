package cz.honzakasik.offensesindex.database;

import com.healthmarketscience.sqlbuilder.*;
import com.healthmarketscience.sqlbuilder.dbspec.basic.DbTable;

import java.sql.ResultSet;

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

    public ResultSet getOffenses(Condition condition) {
        String query = new SelectQuery()
                .addColumns(
                        offensesTable.findColumn(NAME))
                .addAliasedColumn(
                        FunctionCall.count().addColumnParams(eventsTable.findColumns(ID)),
                        OFFENSES_COUNT)
                .addCustomJoin(SelectQuery.JoinType.INNER,
                        eventsTable,
                        offensesTable,
                        BinaryCondition.equalTo(eventsTable.findColumn(OFFENSE_ID), offensesTable.findColumn(ID)))
                .addCustomGroupings(offensesTable.findColumn(NAME))
                .addCondition(condition)
                .validate().toString();
        return dbManager.executeSQL(query);
    }

    public ResultSet getAllOffenses() {
        return getOffenses(BinaryCondition.EMPTY);
    }

    public ResultSet getOffensesWithinMonths(int fromMonth, int toMonth) {
        CustomSql monthFromColumn = new CustomSql("MONTH(" + eventsTable.findColumn(DATE).getColumnNameSQL() + ")");
        return getOffenses(ComboCondition.and()
                .addCondition(BinaryCondition
                        .greaterThan(new CustomSql("MONTH(" + monthFromColumn + ")"),
                                fromMonth, true))
                .addCondition(BinaryCondition
                        .lessThan(new CustomSql("MONTH(" + monthFromColumn + ")"),
                                toMonth, true)));
    }
}
