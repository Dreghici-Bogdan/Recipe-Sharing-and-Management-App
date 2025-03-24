package com.example.booknestapp.di

import android.app.Application
import androidx.room.Room
import com.example.booknestapp.feature.book.data.repository.RecipeRepositoryImpl
import com.example.booknestapp.feature.book.data.source.RecipeDataBase
import com.example.booknestapp.feature.book.domain.repository.RecipeRepository
import com.example.booknestapp.feature.book.domain.use_case.AddRecipe
import com.example.booknestapp.feature.book.domain.use_case.REcipeUseCases
import com.example.booknestapp.feature.book.domain.use_case.DeleteRecipe
import com.example.booknestapp.feature.book.domain.use_case.GetRecipe
import com.example.booknestapp.feature.book.domain.use_case.GetRecipes
import com.example.booknestapp.feature.book.server.WebSocketClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideBookDatabase(app: Application): RecipeDataBase {
        return Room.databaseBuilder(
            app,
            RecipeDataBase::class.java,
            RecipeDataBase.DATABASE_NAME
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideBookRepository(db: RecipeDataBase, ws: WebSocketClient): RecipeRepository {
        return RecipeRepositoryImpl(db.recipeDao,ws)
    }

    @Provides
    @Singleton
    fun provideBookUseCases(repository: RecipeRepository): REcipeUseCases {
        return REcipeUseCases(
            getRecipes = GetRecipes(repository),
            deleteRecipe = DeleteRecipe(repository),
            addRecipe = AddRecipe(repository),
            getRecipe = GetRecipe(repository),
//            destroyBook = DestroyBook(repository)
        )
    }

    @Provides
    @Singleton
    fun provideWebSocketClient(): WebSocketClient {
        return WebSocketClient()
    }
}