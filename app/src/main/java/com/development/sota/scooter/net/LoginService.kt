package com.development.sota.scooter.net

import com.development.sota.scooter.ui.login.domain.entities.ClientLoginResponse
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface LoginService {
    @POST("ClientLogIn/")
    @Headers("Content-Type: application/json")
    fun clientLogin(
        @Body loginData: LoginData
    ): Observable<ClientLoginResponse>
}

data class LoginData(
    val client_name: String,
    val phone: String
)