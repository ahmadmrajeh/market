package com.example.market.di

import androidx.room.Room
import com.example.market.core.ApiClient
import com.example.market.core.ApiService
import com.example.market.core.ProductRepository
import com.example.market.data.local.room.ProductDao
import com.example.market.data.local.room.ProductDatabase
import com.example.market.ui.ProductViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    // Provide Database instance
    single {
        Room.databaseBuilder(androidContext(), ProductDatabase::class.java, "product_db")
            .fallbackToDestructiveMigration()
            .build()
    }

    // Provide DAO instance
    single<ProductDao> { get<ProductDatabase>().productDao() }

    // Provide API service
    single<ApiService> { ApiClient() }

    // Provide Repository (Ensure parameters match constructor)
    single { ProductRepository(get(), get()) }

    // Provide ViewModel
    viewModel { ProductViewModel(get()) }
}
