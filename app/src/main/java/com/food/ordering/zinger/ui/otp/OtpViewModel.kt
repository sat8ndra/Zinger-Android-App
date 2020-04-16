package com.food.ordering.zinger.ui.otp

import androidx.lifecycle.*
import com.food.ordering.zinger.data.local.Resource
import com.food.ordering.zinger.data.model.LoginRequest
import com.food.ordering.zinger.data.model.LoginResponse
import com.food.ordering.zinger.data.model.Shop
import com.food.ordering.zinger.data.retrofit.UserRepository
import kotlinx.coroutines.launch

import java.net.UnknownHostException
import java.util.ArrayList


class OtpViewModel(private val userRepository: UserRepository) : ViewModel() {

    //LOGIN
    private val performLogin = MutableLiveData<Resource<LoginResponse>>()
    val performLoginStatus: LiveData<Resource<LoginResponse>>
        get() = performLogin

    fun login(loginRequest: LoginRequest) {
        viewModelScope.launch {
            try {
                performLogin.value = Resource.loading()
                val response = userRepository.login(loginRequest)
                if(response.code==1){
                    performLogin.value = Resource.success(response)
                }else{
                    performLogin.value = Resource.error(null, message = response.message)
                }
            } catch (e: Exception) {
                println("fetch stats failed ${e.message}")
                if (e is UnknownHostException) {
                    performLogin.value = Resource.offlineError()
                } else {
                    //different type of error
                    performLogin.value = Resource.error(e,message = "Something went wrong!")
                }
            }
        }
    }


}