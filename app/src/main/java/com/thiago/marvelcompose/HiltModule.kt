package com.thiago.marvelcompose

import android.content.Context
import androidx.room.Room
import com.thiago.marvelcompose.connectivity.ConnectivityMonitor
import com.thiago.marvelcompose.model.api.ApiService
import com.thiago.marvelcompose.model.api.MarvelApiRepo
import com.thiago.marvelcompose.model.db.CharacterDao
import com.thiago.marvelcompose.model.db.CollectionDb
import com.thiago.marvelcompose.model.db.CollectionDbRepoImpl
import com.thiago.marvelcompose.model.db.CollectionDbRepository
import com.thiago.marvelcompose.model.db.Constants.DB
import com.thiago.marvelcompose.model.db.NoteDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext

@Module
@InstallIn(ViewModelComponent::class)
class HiltModule {

    @Provides
    fun provideApiRepo() = MarvelApiRepo(ApiService.api)


    @Provides
    fun provideCollectionDb(@ApplicationContext context: Context) =
        Room.databaseBuilder(context, CollectionDb::class.java, DB).build()

    @Provides
    fun provideCharacterDao(collectionDb: CollectionDb) = collectionDb.characterDao()


    @Provides
    fun provideNoteDao(collectionDb: CollectionDb) = collectionDb.noteDao()


    @Provides
    fun provideDbRepoImpl(characterDao: CharacterDao, noteDao: NoteDao): CollectionDbRepository =
        CollectionDbRepoImpl(characterDao, noteDao)

    @Provides
    fun provideConnectivityManager(@ApplicationContext context: Context) =
        ConnectivityMonitor.getInstance(context)


}