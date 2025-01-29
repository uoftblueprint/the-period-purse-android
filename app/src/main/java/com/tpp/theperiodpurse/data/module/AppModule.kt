package com.tpp.theperiodpurse.data.module
import android.content.Context
import com.tpp.theperiodpurse.data.dao.DateDAO
import com.tpp.theperiodpurse.data.dao.UserDAO
import com.tpp.theperiodpurse.data.repository.DateRepository
import com.tpp.theperiodpurse.data.repository.UserRepository
import com.tpp.theperiodpurse.ui.viewmodel.AppViewModel
import com.tpp.theperiodpurse.ui.viewmodel.CalendarViewModel
import com.tpp.theperiodpurse.ui.viewmodel.OnboardViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Singleton
    @Provides
    fun provideUserRepository(userDAO: UserDAO): UserRepository {
        return UserRepository(userDAO)
    }

    @Singleton
    @Provides
    fun provideDateRepository(dateDAO: DateDAO): DateRepository {
        return DateRepository(dateDAO)
    }

    @Singleton
    @Provides
    fun provideAppViewModel(
        userRepository: UserRepository,
        dateRepository: DateRepository,
        @ApplicationContext context: Context // Correct usage of @ApplicationContext
    ): AppViewModel {
        return AppViewModel(userRepository, dateRepository, context)
    }

    @Singleton
    @Provides
    fun provideOnboardViewModel(userRepository: UserRepository): OnboardViewModel {
        return OnboardViewModel(userRepository)
    }

    @Singleton
    @Provides
    fun provideCalendarViewModel(): CalendarViewModel {
        return CalendarViewModel()
    }
}
