package com.theperiodpurse.app.data.module

import com.theperiodpurse.app.data.DateDAO
import com.theperiodpurse.app.data.UserDAO
import com.theperiodpurse.app.data.repository.DateRepository
import com.theperiodpurse.app.data.repository.UserRepository
import com.theperiodpurse.app.ui.viewmodel.AppViewModel
import com.theperiodpurse.app.ui.viewmodel.CalendarViewModel
import com.theperiodpurse.app.ui.viewmodel.OnboardViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
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
    fun provideAppViewModel(userRepository: UserRepository, dateRepository: DateRepository): AppViewModel {
        return AppViewModel(userRepository, dateRepository)
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
