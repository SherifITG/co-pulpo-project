package com.itgates.co.pulpo.ultra.di

import com.itgates.co.pulpo.ultra.dataStore.DataStoreService
import com.itgates.co.pulpo.ultra.network.retrofit.ConfigDataRetrofitApi
import com.itgates.co.pulpo.ultra.network.retrofit.DataRetrofitApi
import com.itgates.co.pulpo.ultra.network.retrofit.FilesDataRetrofitApi
import com.itgates.co.pulpo.ultra.repository.*
import com.itgates.co.pulpo.ultra.roomDataBase.AppDatabase
import com.itgates.co.pulpo.ultra.viewModels.LocationViewModel
import com.itgates.co.pulpo.ultra.viewModels.OnlineLogViewModel
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
    fun provideOnlineLogViewModel(onlineDataRepo: OnlineDataRepo, dataStoreService: DataStoreService): OnlineLogViewModel {
        return OnlineLogViewModel(onlineDataRepo, dataStoreService)
    }

    @Singleton
    @Provides
    fun provideLocationViewModel(offlineDataRepo: OfflineDataRepo, dataStoreService: DataStoreService): LocationViewModel {
        return LocationViewModel(offlineDataRepo, dataStoreService)
    }

    @Singleton
    @Provides
    fun provideOnlineRepo(dataRetrofitApi: DataRetrofitApi, filesDataRetrofitApi: FilesDataRetrofitApi): OnlineDataRepo {
        return OnlineDataRepoImpl(dataRetrofitApi, filesDataRetrofitApi)
    }

    @Singleton
    @Provides
    fun provideConfigRepo(
        configDataRetrofitApi: ConfigDataRetrofitApi
    ): ConfigDataRepo {
        return ConfigDataRepoImpl(configDataRetrofitApi)
    }

    @Singleton
    @Provides
    fun provideOfflineRepo(
        roomDataBase: AppDatabase,
    ): OfflineDataRepo {
        return OfflineDataRepoImpl(
            roomDataBase.accountTypeDao(), roomDataBase.lineDao(), roomDataBase.brickDao(),
            roomDataBase.divisionDao(), roomDataBase.settingDao(), roomDataBase.vacationTypeDao(),
            roomDataBase.idAndNameDao(), roomDataBase.accountDao(), roomDataBase.doctorDao(),
            roomDataBase.presentationDao(), roomDataBase.slideDao(), roomDataBase.plannedVisitDao(),
            roomDataBase.plannedOWDao(), roomDataBase.actualVisitDao(), roomDataBase.officeWorkDao(),
            roomDataBase.offlineLogDao(), roomDataBase.offlineLocDao(), roomDataBase.newPlanDao(),
            roomDataBase.vacationDao()
        )
    }
}