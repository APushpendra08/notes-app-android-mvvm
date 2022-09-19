package com.example.notesapp_mvvm.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.notesapp_mvvm.api.UserAPI
import com.example.notesapp_mvvm.models.UserRequest
import com.example.notesapp_mvvm.models.UserResponse
import com.example.notesapp_mvvm.utils.Constants.TAG
import com.example.notesapp_mvvm.utils.NetworkResult
import org.json.JSONObject
import retrofit2.Response
import javax.inject.Inject

// Talks to local data source or remote data source
class UserRepository @Inject constructor(private val userAPI: UserAPI) {

    private val _userResponseLiveData = MutableLiveData<NetworkResult<UserResponse>>()
    val userResponseLiveData: LiveData<NetworkResult<UserResponse>>
        get() = _userResponseLiveData


    suspend fun loginUser(userRequest: UserRequest) {

        _userResponseLiveData.postValue(NetworkResult.Loading())

        val response = userAPI.signin(userRequest)
        handleResponse(response)
        Log.d(TAG, response.body().toString())
    }

    private fun handleResponse(response: Response<UserResponse>) {
        if (response.isSuccessful && response.body() != null) {
            _userResponseLiveData.postValue(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            val errorBody = JSONObject(response.errorBody()!!.charStream().readText())
            _userResponseLiveData.postValue((NetworkResult.Error(errorBody.getString("message"))))
        } else {
            _userResponseLiveData.postValue((NetworkResult.Error("Something went wrong")))
        }
    }

    suspend fun registerUser(userRequest: UserRequest) {
        val response = userAPI.signup(userRequest)
        handleResponse(response)
    }


}