package com.itgates.co.pulpo.ultra.roomDataBase

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.itgates.co.pulpo.ultra.roomDataBase.converters.Converters
import com.itgates.co.pulpo.ultra.roomDataBase.daos.*
import com.itgates.co.pulpo.ultra.roomDataBase.entity.e_detailing.Presentation
import com.itgates.co.pulpo.ultra.roomDataBase.entity.e_detailing.Slide
import com.itgates.co.pulpo.ultra.roomDataBase.entity.generalData.*
import com.itgates.co.pulpo.ultra.roomDataBase.entity.masterData.*
import com.itgates.co.pulpo.ultra.roomDataBase.roomUtils.TablesNames

@Database(
    entities = [
        IdAndNameEntity::class, AccountType::class, Line::class, Brick::class, Division::class,
        Setting::class, VacationType::class, Account::class, Doctor::class, ActualVisit::class,
        OfficeWork::class, PlannedVisit::class, PlannedOW::class, OfflineLog::class,
        OfflineLoc::class, Presentation::class, Slide::class, NewPlanEntity::class, Vacation::class
    ],
    version = 8,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase: RoomDatabase() {

    companion object {
        val MIGRATION_3_4: Migration = object : Migration(3, 4) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // Add migration SQL statements for version 3 to version 4

                // Create a new table without the unwanted column
                database.execSQL(
                    "CREATE TABLE IF NOT EXISTS `${TablesNames.OfflineLogTable}_temp` (" +
                            "`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                            "`online_id` INTEGER NOT NULL, " +
                            "`message_id` INTEGER NOT NULL, " +
                            "`user_id` INTEGER NOT NULL, " +
                            "`android_id` TEXT NOT NULL, " +  // Renamed column
                            "`device_name` TEXT NOT NULL DEFAULT '-', " +
                            "`app_version` TEXT NOT NULL, " +
                            "`form_id` INTEGER NOT NULL, " +
                            "`is_synced` INTEGER NOT NULL, " +
                            "`created_on` TEXT NOT NULL" +
                            ")"
                )

                // Copy values from the old column to the new column
                database.execSQL(
                    "INSERT INTO ${TablesNames.OfflineLogTable}_temp (" +
                            " id, online_id, message_id, user_id, android_id," +
                            " app_version, form_id, is_synced, created_on" +
                            " )" +
                            " SELECT" +
                            " id, online_id, message_id, user_id, ip," +
                            " app_version, form_id, is_synced, created_on" +
                            " FROM ${TablesNames.OfflineLogTable}"
                )

                // Drop the old table
                database.execSQL("DROP TABLE IF EXISTS `${TablesNames.OfflineLogTable}`")

                // Rename the new table to the old table name
                database.execSQL("ALTER TABLE `${TablesNames.OfflineLogTable}_temp` RENAME TO `${TablesNames.OfflineLogTable}`")
            }
        }
    }

    abstract fun accountTypeDao(): AccountTypeDao
    abstract fun lineDao(): LineDao
    abstract fun brickDao(): BrickDao
    abstract fun divisionDao(): DivisionDao
    abstract fun settingDao(): SettingDao
    abstract fun vacationTypeDao(): VacationTypeDao
    abstract fun idAndNameDao(): IdAndNameDao

    abstract fun accountDao(): AccountDao
    abstract fun doctorDao(): DoctorDao

    abstract fun presentationDao(): PresentationDao
    abstract fun slideDao(): SlideDao

    abstract fun plannedVisitDao(): PlannedVisitDao
    abstract fun plannedOWDao(): PlannedOWDao
    abstract fun actualVisitDao(): ActualVisitDao
    abstract fun officeWorkDao(): OfficeWorkDao

    abstract fun offlineLogDao(): OfflineLogDao
    abstract fun offlineLocDao(): OfflineLocDao

    abstract fun newPlanDao(): NewPlanDao
    abstract fun vacationDao(): VacationDao
}