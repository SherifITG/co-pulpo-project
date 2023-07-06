package com.itgates.ultra.pulpo.cira.di

import com.itgates.ultra.pulpo.cira.network.retrofit.DataRetrofitApi
import com.itgates.ultra.pulpo.cira.network.retrofit.FilesDataRetrofitApi
import com.itgates.ultra.pulpo.cira.repository.OfflineDataRepo
import com.itgates.ultra.pulpo.cira.repository.OfflineDataRepoImpl
import com.itgates.ultra.pulpo.cira.repository.OnlineDataRepo
import com.itgates.ultra.pulpo.cira.repository.OnlineDataRepoImpl
import com.itgates.ultra.pulpo.cira.roomDataBase.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepoModule {

    @Singleton
    @Provides
    fun provideOnlineRepo(dataRetrofitApi: DataRetrofitApi, filesDataRetrofitApi: FilesDataRetrofitApi): OnlineDataRepo {
        return OnlineDataRepoImpl(dataRetrofitApi, filesDataRetrofitApi)
    }

    @Singleton
    @Provides
    fun provideOfflineRepo(
        roomDataBase: AppDatabase,
    ): OfflineDataRepo {
        return OfflineDataRepoImpl(
            roomDataBase.accountTypeDao(), roomDataBase.brickDao(), roomDataBase.classDao(),
            roomDataBase.divisionDao(), roomDataBase.settingDao(), roomDataBase.idAndNameDao(),
            roomDataBase.accountDao(), roomDataBase.doctorDao(),roomDataBase.presentationDao(),
            roomDataBase.slideDao(), roomDataBase.plannedVisitDao(), roomDataBase.actualVisitDao(),
            roomDataBase.offlineLogDao(), roomDataBase.offlineLocDao(),
            roomDataBase.newPlanDao()

        )
    }
}