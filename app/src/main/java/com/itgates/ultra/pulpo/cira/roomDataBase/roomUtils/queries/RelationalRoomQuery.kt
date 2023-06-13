package com.itgates.ultra.pulpo.cira.roomDataBase.roomUtils.queries

import com.itgates.ultra.pulpo.cira.roomDataBase.roomUtils.TablesNames
import com.itgates.ultra.pulpo.cira.roomDataBase.roomUtils.enums.IdAndNameTablesNamesEnum
import com.itgates.ultra.pulpo.cira.roomDataBase.roomUtils.tablesEnums.*

object RelationalRoomQuery {

    private const val classesIdsQuery = "SELECT DISTINCT doc.${DoctorColumns.CLASS_ID}" +
            " FROM ${TablesNames.DoctorTable} AS doc" +
            " JOIN ${TablesNames.AccountTable} AS acc" +
            " ON doc.account_id = acc.id" +
            " WHERE acc.${AccountColumns.DIVISION_ID} IN (:divIds)" +
            " AND acc.${AccountColumns.BRICK_ID} IN (:brickIds)" +
            " AND acc.${AccountColumns.TBL} IN (:accTypeTables)"

    const val classesQuery = "SELECT * FROM ${TablesNames.ClassTable}" +
            " WHERE ${ClassColumns.ID} IN (${classesIdsQuery})"

    private const val plannedVisitsListSelectPart =
        "SELECT planned.*, div.embedded_division_name As divName, div.team_id As teamId," +
                " accType.embedded_account_type_name AS accTypeName, accType.cat_id AS categoryId," +
                " acc.embedded_account_name AS accName," +
                " acc.ll_first AS firstLL, acc.lg_first AS firstLG," +
                " doc.embedded_doctor_name AS docName," +
                " brick.embedded_brick_name AS brickName, brick.id AS brickId" // brick need div check

    const val plannedVisitsListQuery =
        plannedVisitsListSelectPart +
                " FROM ${TablesNames.PlannedVisitTable} AS planned" +
                " LEFT JOIN ${TablesNames.DivisionTable} AS div" +
                " ON planned.div_id = div.id" +
                " LEFT JOIN ${TablesNames.AccountTypeTable} AS accType" +
                " ON planned.acc_type_id = accType.id" +
                " LEFT JOIN ${TablesNames.AccountTable} AS acc" +
                " ON planned.item_id = acc.id AND acc.tbl = accType.tbl" +
                " LEFT JOIN ${TablesNames.DoctorTable} AS doc" +
                " ON planned.item_doctor_id = doc.id AND doc.tbl = accType.tbl" +
                " LEFT JOIN ${TablesNames.BrickTable} AS brick" +
                " ON acc.brick_id = brick.id" +
                " WHERE planned.div_id <> -1" // -1 for office work

    const val todayPlannedVisitsListQuery =
        plannedVisitsListSelectPart +
                " FROM ${TablesNames.PlannedVisitTable} AS planned" +
                " LEFT JOIN ${TablesNames.DivisionTable} AS div" +
                " ON planned.div_id = div.id" +
                " LEFT JOIN ${TablesNames.AccountTypeTable} AS accType" +
                " ON planned.acc_type_id = accType.id" +
                " LEFT JOIN ${TablesNames.AccountTable} AS acc" +
                " ON planned.item_id = acc.id AND acc.tbl = accType.tbl" +
                " LEFT JOIN ${TablesNames.DoctorTable} AS doc" +
                " ON planned.item_doctor_id = doc.id AND doc.tbl = accType.tbl" +
                " LEFT JOIN ${TablesNames.BrickTable} AS brick" +
                " ON acc.brick_id = brick.id" +
                " WHERE planned.visit_date = :today AND planned.div_id <> -1" // -1 for office work

    private const val plannedOfficeWorksListSelectPart =
        "SELECT planned.*, idName.embedded_entity_name AS officeWorkName"

    const val plannedOfficeWorksListQuery =
        plannedOfficeWorksListSelectPart +
                " FROM ${TablesNames.PlannedVisitTable} AS planned" +
                " LEFT JOIN ${TablesNames.IdAndNameTable} AS idName" +
                " ON planned.acc_type_id = idName.id" +
                " AND idName.table_identifier = :tableId" +
                " WHERE planned.div_id = -1" // -1 for office work


    private const val actualVisitsListSelectPart =
        "SELECT actual.*, div.embedded_division_name As divName," +
                " accType.embedded_account_type_name AS accTypeName," +
                " acc.embedded_account_name AS accName," +
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
                " ON actual.item_id = acc.id AND acc.tbl = accType.tbl" +
                " LEFT JOIN ${TablesNames.DoctorTable} AS doc" +
                " ON actual.item_doctor_id = doc.id AND doc.tbl = accType.tbl" +
                " LEFT JOIN ${TablesNames.BrickTable} AS brick" +
                " ON acc.brick_id = brick.id" +
                " WHERE actual.division_id <> -1" // -1 for office work

    private const val actualOfficeWorksListSelectPart =
        "SELECT actual.*, idName.embedded_entity_name AS officeWorkName"

    const val actualOfficeWorksListQuery =
        actualOfficeWorksListSelectPart +
                " FROM ${TablesNames.ActualVisitTable} AS actual" +
                " LEFT JOIN ${TablesNames.IdAndNameTable} AS idName" +
                " ON actual.account_type_id = idName.id" +
                " AND idName.table_identifier = :tableId" +
                " WHERE actual.division_id = -1" // -1 for office work

    const val accountsListQuery =
        "SELECT acc.*, div.embedded_division_name As divName," +
                " accType.embedded_account_type_name AS accTypeName," +
                " cls.embedded_class_name AS className," +
                " brick.embedded_brick_name AS brickName" +

                " FROM ${TablesNames.AccountTable} AS acc" +
                " LEFT JOIN ${TablesNames.DivisionTable} AS div" +
                " ON acc.division_id = div.id" +
                " LEFT JOIN ${TablesNames.AccountTypeTable} AS accType" +
                " ON acc.tbl = accType.tbl" +
                " LEFT JOIN ${TablesNames.ClassTable} AS cls" +
                " ON acc.class_id = cls.id" +
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
                " cls.embedded_class_name AS className," +
                " brick.embedded_brick_name AS brickName, accType.cat_id AS catId" +

                " FROM ${TablesNames.DoctorTable} AS doc" +
                " LEFT JOIN ${TablesNames.IdAndNameTable} AS spec" +
                " ON doc.specialization_id = spec.id AND spec.table_identifier = :tableId" +
                " LEFT JOIN ${TablesNames.AccountTable} AS acc" +
                " ON doc.account_id = acc.id AND doc.tbl = acc.tbl" +
                " LEFT JOIN ${TablesNames.AccountTypeTable} AS accType" +
                " ON doc.tbl = accType.tbl" +
                " LEFT JOIN ${TablesNames.DivisionTable} AS div" +
                " ON acc.division_id = div.id" +
                " LEFT JOIN ${TablesNames.ClassTable} AS cls" +
                " ON acc.class_id = cls.id" +
                " LEFT JOIN ${TablesNames.BrickTable} AS brick" +
                " ON acc.brick_id = brick.id"

    private const val newPlanListSelectPart =
        "SELECT newPlan.*, div.embedded_division_name As divName, div.team_id As teamId," +
                " accType.embedded_account_type_name AS accTypeName, accType.cat_id AS categoryId," +
                " acc.embedded_account_name AS accName," +
                " acc.ll_first AS firstLL, acc.lg_first AS firstLG," +
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
                " ON newPlan.item_id = acc.id AND acc.tbl = accType.tbl" +
                " LEFT JOIN ${TablesNames.DoctorTable} AS doc" +
                " ON newPlan.item_doctor_id = doc.id AND doc.tbl = accType.tbl" +
                " LEFT JOIN ${TablesNames.BrickTable} AS brick" +
                " ON acc.brick_id = brick.id" +
                " WHERE newPlan.div_id <> -1" // -1 for office work

}