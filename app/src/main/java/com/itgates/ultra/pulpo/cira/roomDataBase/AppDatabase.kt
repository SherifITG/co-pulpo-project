package com.itgates.ultra.pulpo.cira.roomDataBase

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.itgates.ultra.pulpo.cira.roomDataBase.converters.Converters
import com.itgates.ultra.pulpo.cira.roomDataBase.daos.*
import com.itgates.ultra.pulpo.cira.roomDataBase.entity.e_detailing.Presentation
import com.itgates.ultra.pulpo.cira.roomDataBase.entity.e_detailing.Slide
import com.itgates.ultra.pulpo.cira.roomDataBase.entity.generalData.*
import com.itgates.ultra.pulpo.cira.roomDataBase.entity.masterData.*

@Database(
    entities = [
        IdAndNameEntity::class, AccountType::class, Brick::class, Class::class, Division::class,
        Setting::class, Account::class, Doctor::class, ActualVisit::class, PlannedVisit::class,
        ItgFile::class, OfflineLog::class, OfflineLoc::class, Presentation::class, Slide::class,
        NewPlanEntity::class
    ],
    version = 2,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase: RoomDatabase() {
    abstract fun accountTypeDao(): AccountTypeDao
    abstract fun brickDao(): BrickDao
    abstract fun classDao(): ClassDao
    abstract fun divisionDao(): DivisionDao
    abstract fun settingDao(): SettingDao
    abstract fun idAndNameDao(): IdAndNameDao

    abstract fun accountDao(): AccountDao
    abstract fun doctorDao(): DoctorDao

    abstract fun presentationDao(): PresentationDao
    abstract fun slideDao(): SlideDao

    abstract fun plannedVisitDao(): PlannedVisitDao
    abstract fun actualVisitDao(): ActualVisitDao

    abstract fun itgFileDao(): ItgFileDao

    abstract fun offlineLogDao(): OfflineLogDao
    abstract fun offlineLocDao(): OfflineLocDao

    abstract fun newPlanDao(): NewPlanDao
}