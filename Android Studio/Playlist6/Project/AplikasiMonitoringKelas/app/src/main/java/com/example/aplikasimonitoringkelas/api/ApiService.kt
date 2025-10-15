package com.example.aplikasimonitoringkelas.api

import com.example.aplikasimonitoringkelas.BodyData
import com.example.aplikasimonitoringkelas.DeleteResponse
import com.example.aplikasimonitoringkelas.GuruMengajarRequest
import com.example.aplikasimonitoringkelas.GuruMengajarResponse
import com.example.aplikasimonitoringkelas.GuruSimple
import com.example.aplikasimonitoringkelas.JadwalKepsek
import com.example.aplikasimonitoringkelas.JadwalKepsekResponse
import com.example.aplikasimonitoringkelas.JadwalKurikulum
import com.example.aplikasimonitoringkelas.JadwalResponseListKurikulum
import com.example.aplikasimonitoringkelas.JadwalResponseSiswa
import com.example.aplikasimonitoringkelas.KelasKepsek
import com.example.aplikasimonitoringkelas.KelasKurikulum
import com.example.aplikasimonitoringkelas.KelasSiswa
import com.example.aplikasimonitoringkelas.LoginRequest
import com.example.aplikasimonitoringkelas.LoginResponse
import com.example.aplikasimonitoringkelas.MapelJam
import com.example.aplikasimonitoringkelas.RequestBodyHariKelas
import com.example.aplikasimonitoringkelas.UpdateJadwalRequest
import com.example.aplikasimonitoringkelas.admin.GuruResponse
import com.example.aplikasimonitoringkelas.admin.JadwalRequestAdmin
import com.example.aplikasimonitoringkelas.admin.JadwalResponseAdmin
import com.example.aplikasimonitoringkelas.admin.KelasResponseAdmin
import com.example.aplikasimonitoringkelas.admin.MapelResponse
import com.example.aplikasimonitoringkelas.admin.TahunResponse
import com.example.aplikasimonitoringkelas.admin.UserRequest
import com.example.aplikasimonitoringkelas.admin.UserResponse
import com.example.aplikasimonitoringkelas.data.JadwalResponse
import com.example.aplikasimonitoringkelas.data.KelasResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @POST("users")
    fun createUser(@Body request: UserRequest): Call<UserResponse>

    @GET("users")
    fun getUsers(): Call<List<UserResponse>>

    @PUT("users/{id}")
    fun updateUser(
        @Path("id") id: Int,
        @Body request: UserRequest
    ): Call<UserResponse>

    @DELETE("api/users/{id}")
    fun deleteUser(@Path("id") id: Int): Call<Void>

    @GET("kelas")
    suspend fun getKelas(): List<KelasResponse>

    @GET("jadwal/schedule/kelas/{kelas_id}/hari/{hari}")
    suspend fun getJadwal(
        @Path("kelas_id") kelasId: Int,
        @Path("hari") hari: String
    ): List<JadwalResponse>

    @GET("kelas") fun getKelasAdmin(): Call<List<KelasResponseAdmin>>
    @GET("gurus") fun getGuru(): Call<List<GuruResponse>>
    @GET("mapels") fun getMapel(): Call<List<MapelResponse>>
    @GET("tahun-ajarans") fun getTahun(): Call<List<TahunResponse>>

    @POST("jadwals") fun createJadwal(@Body request: JadwalRequestAdmin): Call<JadwalResponseAdmin>

    @GET("kelas")
    suspend fun getKelasKepsek(): List<KelasKepsek>

    @GET("jadwal/kelas/{kelas_id}/hari/{hari}")
    suspend fun getJadwalKepsek(
        @Path("kelas_id") kelasId: Int,
        @Path("hari") hari: String
    ): List<JadwalKepsek>

    @POST("guru-mengajar/tidak-masuk")
    suspend fun getGuruMengajarTidakMasuk(@Body body: BodyData): List<GuruMengajarResponse>

    @POST("guru-mengajar/by-hari-kelas")
    suspend fun getJadwalByHariKelas(@Body body: BodyData): List<JadwalKepsekResponse>

    @GET("kelas")
    suspend fun getKelasKurikulum(): List<KelasKurikulum>

    @GET("jadwal/kelas/{kelas_id}/hari/{hari}")
    suspend fun getJadwalKurikulum(
        @Path("kelas_id") kelasId: Int,
        @Path("hari") hari: String
    ): List<JadwalKurikulum>

    @GET("jadwal/guru-by-hari-kelas")
    suspend fun getGurusByHariKelas(
        @Query("hari") hari: String,
        @Query("kelas_id") kelas_id: Int
    ): List<GuruSimple>

    @GET("jadwal/mapel-by-hari-kelas-guru")
    suspend fun getMapelAndJamByHariKelasGuru(
        @Query("hari") hari: String,
        @Query("kelas_id") kelas_id: Int,
        @Query("guru_id") guru_id: Int
    ): List<MapelJam>

    @POST("guru-mengajars")
    suspend fun addGuruMengajar(@Body body: GuruMengajarRequest): okhttp3.ResponseBody

    @POST("guru-mengajar/by-hari-kelas")
    suspend fun getJadwalListKurikulum(@Body body: RequestBodyHariKelas): List<JadwalResponseListKurikulum>

    @PUT("guru-mengajars/{id}")
    suspend fun updateJadwal(
        @Path("id") id: Int,
        @Body body: UpdateJadwalRequest
    ): JadwalResponseListKurikulum

    @DELETE("guru-mengajars/{id}")
    suspend fun deleteJadwal(@Path("id") id: Int): DeleteResponse

    @GET("kelas")
    suspend fun getKelasSiswa(): List<KelasSiswa>

    @GET("jadwal/kelas/{kelas_id}/hari/{hari}")
    suspend fun getJadwalSiswa(
        @Path("kelas_id") kelasId: Int,
        @Path("hari") hari: String
    ): List<JadwalResponseSiswa>

    @POST("login")
    suspend fun login(@Body request: LoginRequest): LoginResponse

}