package com.itgates.ultra.pulpo.cira.roomDataBase.roomUtils.queries

import com.itgates.ultra.pulpo.cira.roomDataBase.roomUtils.TablesNames
import com.itgates.ultra.pulpo.cira.roomDataBase.roomUtils.tablesEnums.*

object MainRoomQuery {

    const val updateAccountLocationQuery = "UPDATE ${TablesNames.AccountTable}" +
            " SET ${AccountColumns.LL_FIRST} = :llFirst, ${AccountColumns.LG_FIRST} = :lgFirst" +
            " WHERE ${AccountColumns.ID} = :id"

    const val settingByNamesListQuery = "SELECT * FROM ${TablesNames.SettingTable}" +
            " WHERE ${SettingColumns.EMBEDDED_SETTING_NAME} IN (:names)"

    private const val accountTypesNamesQueryWithoutBrick = "SELECT DISTINCT ${AccountColumns.TBL}" +
            " FROM ${TablesNames.AccountTable} WHERE ${AccountColumns.DIVISION_ID} IN (:divIds)"

    const val accountTypesQueryWithoutBrick = "SELECT * FROM ${TablesNames.AccountTypeTable}" +
            " WHERE ${AccountTypeColumns.TBL} IN ($accountTypesNamesQueryWithoutBrick)"

    private const val accountTypesNamesQuery = "SELECT DISTINCT ${AccountColumns.TBL}" +
            " FROM ${TablesNames.AccountTable} WHERE ${AccountColumns.DIVISION_ID} IN (:divIds)" +
            " AND ${AccountColumns.BRICK_ID} IN (:brickIds)"

    const val accountTypesQuery = "SELECT * FROM ${TablesNames.AccountTypeTable}" +
            " WHERE ${AccountTypeColumns.TBL} IN ($accountTypesNamesQuery)"

    const val divisionsQuery = "SELECT * FROM ${TablesNames.DivisionTable}" +
            " WHERE ${DivisionColumns.ID} IN (:ids)"

    const val bricksQuery = "SELECT * FROM ${TablesNames.BrickTable}" +
            " WHERE ${BrickColumns.TERRITORY_ID} IN (:divIds)"

    const val accountsQueryWithoutBrick = "SELECT * FROM ${TablesNames.AccountTable}" +
            " WHERE ${AccountColumns.DIVISION_ID} = :divId" +
            " AND ${AccountColumns.TBL} = :accTypeTable"

    const val accountsQuery = "SELECT * FROM ${TablesNames.AccountTable}" +
            " WHERE ${AccountColumns.DIVISION_ID} = :divId" +
            " AND ${AccountColumns.BRICK_ID} = :brickIds" +
            " AND ${AccountColumns.TBL} = :accTypeTable"

    const val doctorsQuery = "SELECT * FROM ${TablesNames.DoctorTable}" +
            " WHERE ${DoctorColumns.ACCOUNT_ID} = :accountId" +
            " AND ${DoctorColumns.TBL} = :table"

    const val idAndNameObjectsQuery = "SELECT * FROM ${TablesNames.IdAndNameTable}" +
            " WHERE ${IdAndNameEntityColumns.TABLE_IDENTIFIER} = :tableId"

    const val idAndNameObjectsByTablesListQuery = "SELECT * FROM ${TablesNames.IdAndNameTable}" +
            " WHERE ${IdAndNameEntityColumns.TABLE_IDENTIFIER} IN (:tableIds)"

    const val unSyncedActualVisitQuery = "SELECT * FROM ${TablesNames.ActualVisitTable}" +
            " WHERE is_synced = :isSynced"

    const val updateSyncedActualVisitQuery = "UPDATE ${TablesNames.ActualVisitTable}" +
            " SET online_id = :onlineId, sync_date = :syncDate, sync_time = :syncTime, is_synced = :isSynced" +
            " WHERE id = :offlineId"

    // ---------------------------------------------------------------------------------------------
    private const val actualVisitValidationQuery = "SELECT 1 FROM ${TablesNames.ActualVisitTable}" +
            " WHERE user_id = :userId AND start_date = :startDate " +
            "AND item_id = :itemId AND item_doctor_id = :itemDoctorId " +
            "AND account_type_id = :accountTypeId"

    private const val officeWorkValidationQuery = "SELECT 1 FROM ${TablesNames.ActualVisitTable}" +
            " WHERE user_id = :userId AND start_date = :startDate AND shift = :shift"

    private const val insertActualVisitWithValidationFirstPartQuery =
        "INSERT INTO ${TablesNames.ActualVisitTable} (" +
                " online_id, division_id, account_type_id, item_id, item_doctor_id, no_of_doctors," +
                " planned_visit_id, multiplicity, start_date, start_time, end_date, end_time, shift," +
                " comments, insertion_date, insertion_time, user_id, team_id, ll_start, lg_start," +
                " ll_end, lg_end, visit_duration, visit_deviation, is_synced, planned_visit_id," +
                " sync_date, sync_time, added_date, multiple_lists_info" +
                " ) SELECT " +
                " :onlineId, :divisionId, :accountTypeId, :itemId, :itemDoctorId, :noOfDoctors," +
                " :plannedVisitId, :multiplicity, :startDate, :startTime, :endDate, :endTime," +
                " :shift, :comments, :insertionDate, :insertionTime, :userId, :teamId, :llStart," +
                " :lgStart, :llEnd, :lgEnd, :visitDuration, :visitDeviation, :isSynced," +
                " :plannedVisitId, :syncDate, :syncTime, :addedDate, :multipleListsInfo"

    const val insertActualVisitWithValidationQuery =
        "$insertActualVisitWithValidationFirstPartQuery WHERE NOT EXISTS ( $actualVisitValidationQuery )"

    const val insertOfficeWorkWithValidationQuery =
        "$insertActualVisitWithValidationFirstPartQuery WHERE NOT EXISTS ( $officeWorkValidationQuery )"





    private const val updateValQuery = " SET online_id = :onlineId, is_synced = :isSynced" + " WHERE id = :offlineId"
    const val updateSyncedOfflineLogQuery = "UPDATE ${TablesNames.OfflineLogTable}" + updateValQuery
    const val updateSyncedOfflineLocQuery = "UPDATE ${TablesNames.OfflineLocTable}" + updateValQuery

    const val unSyncedOfflineLogQuery = "SELECT * FROM ${TablesNames.OfflineLogTable} WHERE is_synced = :isSynced"
    const val unSyncedOfflineLocQuery = "SELECT * FROM ${TablesNames.OfflineLocTable} WHERE is_synced = :isSynced"


}