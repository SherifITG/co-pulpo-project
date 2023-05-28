package com.itgates.ultra.pulpo.cira.di

import android.content.Context
import androidx.room.Room
import com.itgates.ultra.pulpo.cira.R
import com.itgates.ultra.pulpo.cira.roomDataBase.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {

    @Singleton
    @Provides
    fun provideRoomDataBase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context = context,
            klass = AppDatabase::class.java,
            name = context.getString(R.string.database_name)
        ).fallbackToDestructiveMigration().build()
    }
}