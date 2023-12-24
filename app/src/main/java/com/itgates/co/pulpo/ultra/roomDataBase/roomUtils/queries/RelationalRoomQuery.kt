package com.itgates.co.pulpo.ultra.roomDataBase.roomUtils.queries

import com.itgates.co.pulpo.ultra.roomDataBase.roomUtils.TablesNames
import com.itgates.co.pulpo.ultra.roomDataBase.roomUtils.enums.IdAndNameTablesNamesEnum
import com.itgates.co.pulpo.ultra.roomDataBase.roomUtils.tablesEnums.*
import com.itgates.co.pulpo.ultra.utilities.GlobalFormats
import java.util.*

object RelationalRoomQuery {

    private const val classesIdsQuery = "SELECT DISTINCT doc.${DoctorColumns.CLASS_ID}" +
            " FROM ${TablesNames.DoctorTable} AS doc" +
            " JOIN ${TablesNames.AccountTable} AS acc" +
            " ON doc.${DoctorColumns.ACCOUNT_ID} = acc.${AccountColumns.ID}" +
            " WHERE acc.${AccountColumns.DIVISION_ID} IN (:divIds)" +
            " AND acc.${AccountColumns.BRICK_ID} IN (:brickIds)" +
            " AND acc.${AccountColumns.ACCOUNT_TYPE_ID} IN (:accTypeIds)"

    const val classesQuery = "SELECT * FROM ${TablesNames.IdAndNameTable}" +
            " WHERE ${IdAndNameEntityColumns.ID} IN (${classesIdsQuery})" +
            " AND ${IdAndNameEntityColumns.TABLE_IDENTIFIER} = :tableId"

    private const val plannedVisitsListSelectPart =
        "SELECT planned.*," +
                " div.${DivisionColumns.EMBEDDED_DIVISION_NAME} As divName," +
//                " div.${DivisionColumns.NOT_MANAGER} As notDivManager," +
                " div.${DivisionColumns.LINE_ID} As lineId," +
                " accType.${AccountTypeColumns.EMBEDDED_ACCOUNT_TYPE_NAME} AS accTypeName," +
                " accType.${AccountTypeColumns.ACCEPTED_DISTANCE} AS accTypeDistance," +
                " accType.${AccountTypeColumns.SHIFT_ID} AS shiftId," +
                " acc.${AccountColumns.EMBEDDED_ACCOUNT_NAME} AS accName," +
                " acc.${AccountColumns.LL_FIRST} AS firstLL," +
                " acc.${AccountColumns.LG_FIRST} AS firstLG," +
                " doc.${DoctorColumns.EMBEDDED_DOCTOR_NAME} AS docName," +
                " brick.${BrickColumns.EMBEDDED_BRICK_NAME} AS brickName," +
                " brick.${BrickColumns.ID} AS brickId" // brick need div check

    const val plannedVisitsListQuery =
        plannedVisitsListSelectPart +
                " FROM ${TablesNames.PlannedVisitTable} AS planned" +
                " LEFT JOIN ${TablesNames.DivisionTable} AS div" +
                " ON planned.div_id = div.id" +
                " LEFT JOIN ${TablesNames.AccountTypeTable} AS accType" +
                " ON planned.acc_type_id = accType.id" +
                " LEFT JOIN ${TablesNames.AccountTable} AS acc" +
                " ON planned.account_id = acc.id" +
                " AND acc.${AccountColumns.ACCOUNT_TYPE_ID} = accType.${AccountTypeColumns.ID}" +
                " LEFT JOIN ${TablesNames.DoctorTable} AS doc" +
                " ON planned.account_doctor_id = doc.id" +
                " AND doc.${DoctorColumns.ACCOUNT_TYPE_ID} = accType.${AccountTypeColumns.ID}" +
                " LEFT JOIN ${TablesNames.BrickTable} AS brick" +
                " ON acc.brick_id = brick.id"

    const val todayPlannedVisitsListQuery =
        plannedVisitsListSelectPart +
                " FROM ${TablesNames.PlannedVisitTable} AS planned" +
                " LEFT JOIN ${TablesNames.DivisionTable} AS div" +
                " ON planned.div_id = div.id" +
                " LEFT JOIN ${TablesNames.AccountTypeTable} AS accType" +
                " ON planned.acc_type_id = accType.id" +
                " LEFT JOIN ${TablesNames.AccountTable} AS acc" +
                " ON planned.account_id = acc.id" +
                " AND acc.${AccountColumns.ACCOUNT_TYPE_ID} = accType.${AccountTypeColumns.ID}" +
                " LEFT JOIN ${TablesNames.DoctorTable} AS doc" +
                " ON planned.account_doctor_id = doc.id" +
                " AND doc.${DoctorColumns.ACCOUNT_TYPE_ID} = accType.${AccountTypeColumns.ID}" +
                " LEFT JOIN ${TablesNames.BrickTable} AS brick" +
                " ON acc.brick_id = brick.id" +
                " WHERE planned.visit_date = :today"

    private const val plannedOfficeWorksListSelectPart =
        "SELECT planned.*, idName.embedded_entity_name AS officeWorkName"

    const val plannedOfficeWorksListQuery =
        plannedOfficeWorksListSelectPart +
                " FROM ${TablesNames.PlannedOWTable} AS planned" +
                " LEFT JOIN ${TablesNames.IdAndNameTable} AS idName" +
                " ON planned.ow_type_id = idName.id" +
                " AND idName.table_identifier = :tableId"

    const val todayPlannedOfficeWorksListQuery =
        plannedOfficeWorksListSelectPart +
                " FROM ${TablesNames.PlannedOWTable} AS planned" +
                " LEFT JOIN ${TablesNames.IdAndNameTable} AS idName" +
                " ON planned.ow_type_id = idName.id" +
                " AND idName.table_identifier = :tableId" +
                " WHERE planned.ow_date = :today"

    const val todayPlannedVisitsAndOWCountQuery =
        "SELECT COUNT(*)" +
                " FROM ${TablesNames.PlannedVisitTable} AS planned" +
                " WHERE planned.visit_date = :today AND planned.is_done = false"

    const val relationalLinesListQuery =
        "SELECT line.*, COALESCE(line_unplanned_coverage, 0) AS unplannedCoverage" +
                " FROM ${TablesNames.LineTable} AS line" +
                " LEFT JOIN (" +
                "       SELECT line_id, COUNT(*) AS line_unplanned_coverage" +
                "       FROM ${TablesNames.ActualVisitTable}" +
                "       WHERE added_date == :today" +
                "       AND planned_visit_id == 0" +
                "       GROUP BY line_id " +
                ") AS actual" +
                " ON line.id = actual.line_id"


    private const val actualVisitsListSelectPart =
        "SELECT actual.*, div.embedded_division_name As divName," +
                " accType.embedded_account_type_name AS accTypeName," +
                " acc.embedded_account_name AS accName," +
                " acc.${AccountColumns.LL_FIRST} AS accLLFirst," +
                " acc.${AccountColumns.LG_FIRST} AS accLGFirst," +
                " doc.embedded_doctor_name AS docName," +
                " brick.embedded_brick_name AS brickName"

    const val actualVisitsListQuery =
        actualVisitsListSelectPart +
                " FROM ${TablesNames.ActualVisitTable} AS actual" +
                " LEFT JOIN ${TablesNames.DivisionTable} AS div" +
                " ON actual.division_id = div.id" +
                " LEFT JOIN ${TablesNames.AccountTypeTable} AS accType" +
                " ON actual.account_type_id = accType.id" +
                " LEFT JOIN ${TablesNames.AccountTable} AS acc" +
                " ON actual.account_id = acc.id" +
                " AND acc.${AccountColumns.ACCOUNT_TYPE_ID} = accType.${AccountTypeColumns.ID}" +
                " LEFT JOIN ${TablesNames.DoctorTable} AS doc" +
                " ON actual.account_doctor_id = doc.id" +
                " AND doc.${DoctorColumns.ACCOUNT_TYPE_ID} = accType.${AccountTypeColumns.ID}" +
                " LEFT JOIN ${TablesNames.BrickTable} AS brick" +
                " ON acc.brick_id = brick.id"

    private const val actualOfficeWorksListSelectPart =
        "SELECT ow.*, idName.embedded_entity_name AS officeWorkName"

    const val actualOfficeWorksListQuery =
        actualOfficeWorksListSelectPart +
                " FROM ${TablesNames.OfficeWorkTable} AS ow" +
                " LEFT JOIN ${TablesNames.IdAndNameTable} AS idName" +
                " ON ow.ow_type_id = idName.id" +
                " AND idName.table_identifier = :tableId"

    const val accountsListQuery =
        "SELECT acc.*, div.embedded_division_name As divName," +
                " accType.embedded_account_type_name AS accTypeName," +
                " cls.embedded_entity_name AS className," +
                " brick.embedded_brick_name AS brickName" +

                " FROM ${TablesNames.AccountTable} AS acc" +
                " LEFT JOIN ${TablesNames.DivisionTable} AS div" +
                " ON acc.division_id = div.id" +
                " LEFT JOIN ${TablesNames.AccountTypeTable} AS accType" +
                " ON acc." + AccountColumns.ACCOUNT_TYPE_ID + " = accType." + AccountTypeColumns.ID +
                " LEFT JOIN ${TablesNames.IdAndNameTable} AS cls" +
                " ON acc.class_id = cls.id AND cls.table_identifier = :classId" +
                " LEFT JOIN ${TablesNames.BrickTable} AS brick" +
                " ON acc.brick_id = brick.id"

    const val doctorsListQuery =
        "SELECT doc.*, spec.embedded_entity_name AS specialityName" +

                " FROM ${TablesNames.DoctorTable} AS doc" +
                " LEFT JOIN ${TablesNames.IdAndNameTable} AS spec" +
                " ON doc.specialization_id = spec.id AND spec.table_identifier = :tableId"

    const val doctorsPlanningListQuery =
        "SELECT doc.*, spec.embedded_entity_name AS specialityName, acc.division_id AS divId," +
                " acc.brick_id AS brickId, acc.class_id AS classId," +
                " div.embedded_division_name As divName," +
                " accType.embedded_account_type_name AS accTypeName, accType.id AS accTypeId," +
                " acc.embedded_account_name AS accName," +
                " cls.embedded_entity_name AS className," +
                " brick.embedded_brick_name AS brickName," +
                " accType.shift_id AS shiftId," +
                " COALESCE(doctor_coverage, 0) AS coverage" +

                " FROM ${TablesNames.DoctorTable} AS doc" +
                " LEFT JOIN ${TablesNames.IdAndNameTable} AS spec" +
                " ON doc.${DoctorColumns.SPECIALIZATION_ID} = spec.${IdAndNameEntityColumns.ID}" +
                " AND spec.${IdAndNameEntityColumns.TABLE_IDENTIFIER} = :specId" +
                " LEFT JOIN ${TablesNames.AccountTable} AS acc" +
                " ON doc.${DoctorColumns.ACCOUNT_ID} = acc.${AccountColumns.ID}" +
                " AND doc.${DoctorColumns.ACCOUNT_TYPE_ID} = acc.${AccountColumns.ACCOUNT_TYPE_ID}" +
                " LEFT JOIN ${TablesNames.AccountTypeTable} AS accType" +
                " ON doc." + DoctorColumns.ACCOUNT_TYPE_ID + " = accType." + AccountTypeColumns.ID +
                " LEFT JOIN ${TablesNames.DivisionTable} AS div" +
                " ON acc." + AccountColumns.DIVISION_ID + " = div.id" +
                " LEFT JOIN ${TablesNames.IdAndNameTable} AS cls" +
                " ON acc.class_id = cls.id AND cls.table_identifier = :classId" +
                " LEFT JOIN ${TablesNames.BrickTable} AS brick" +
                " ON acc.brick_id = brick.id" +
                " LEFT JOIN (" +
                "       SELECT account_doctor_id, account_type_id, added_date, COUNT(*) AS doctor_coverage" +
                "       FROM ${TablesNames.ActualVisitTable}" +
                "       WHERE added_date LIKE :currentDateString" +
                "       GROUP BY account_doctor_id, account_type_id " +
                ") AS actual" +
                " ON doc.id = actual.account_doctor_id" +
                " AND accType.id = actual.account_type_id"


    private const val newPlanListSelectPart =
        "SELECT newPlan.*, div.embedded_division_name As divName, div.line_id As lineId," +
                " accType.embedded_account_type_name AS accTypeName, accType.shift_id AS shiftId," +
                " acc." + AccountColumns.EMBEDDED_ACCOUNT_NAME + " AS accName," +
                " acc." + AccountColumns.LL_FIRST + " AS firstLL," +
                " acc." + AccountColumns.LG_FIRST + " AS firstLG," +
                " doc.embedded_doctor_name AS docName," +
                " brick.embedded_brick_name AS brickName, brick.id AS brickId" // brick need div check

    const val newPlanListQuery =
        newPlanListSelectPart +
                " FROM ${TablesNames.NewPlanTable} AS newPlan" +
                " LEFT JOIN ${TablesNames.DivisionTable} AS div" +
                " ON newPlan.div_id = div.id" +
                " LEFT JOIN ${TablesNames.AccountTypeTable} AS accType" +
                " ON newPlan.acc_type_id = accType.id" +
                " LEFT JOIN ${TablesNames.AccountTable} AS acc" +
                " ON newPlan.account_id = acc." + AccountColumns.ID +
                " AND acc.${AccountColumns.ACCOUNT_TYPE_ID} = accType.${AccountTypeColumns.ID}" +
                " LEFT JOIN ${TablesNames.DoctorTable} AS doc" +
                " ON newPlan.account_doctor_id = doc.id" +
                " AND doc.${DoctorColumns.ACCOUNT_TYPE_ID} = accType.${AccountTypeColumns.ID}" +
                " LEFT JOIN ${TablesNames.BrickTable} AS brick" +
                " ON acc.brick_id = brick.id"

}