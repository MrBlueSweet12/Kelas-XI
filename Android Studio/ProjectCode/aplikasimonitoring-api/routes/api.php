<?php

use App\Http\Controllers\GuruController;
use App\Http\Controllers\MapelController;
use App\Http\Controllers\KelasController;
use App\Http\Controllers\UserController;
use App\Http\Controllers\TahunAjaranController;
use App\Http\Controllers\JadwalController;
use App\Http\Controllers\GuruMengajarController;
use Illuminate\Support\Facades\Route;
use App\Http\Controllers\AuthController;

Route::post('/login', [AuthController::class, 'login']);
Route::middleware('auth:sanctum')->post('/logout', [AuthController::class, 'logout']);

Route::apiResource('gurus', GuruController::class);

Route::apiResource('mapels', MapelController::class);

Route::apiResource('tahun-ajarans', TahunAjaranController::class);

Route::apiResource('kelas', KelasController::class);

Route::apiResource('users', UserController::class);

Route::get('jadwal/guru-by-hari-kelas', [JadwalController::class, 'getGurusByHariKelas']);
Route::get('jadwal/mapel-by-hari-kelas-guru', [JadwalController::class, 'getMapelAndJamByHariKelasGuru']);
Route::get('jadwal/kelas/{kelas_id}/hari/{hari}', [JadwalController::class, 'scheduleByKelasHari']);
Route::get('/jadwal/kelas/{kelas_id}/hari/{hari}', [JadwalController::class, 'getByClassAndDay']);
Route::get('/jadwal/schedule/kelas/{kelas_id}/hari/{hari}', [JadwalController::class, 'getScheduleByClassAndDay']);
Route::apiResource('jadwals', JadwalController::class);

Route::post('/guru-mengajar/tidak-masuk', [GuruMengajarController::class, 'getTidakMasukByHariKelas']);
Route::post('/guru-mengajar/by-hari-kelas', [GuruMengajarController::class, 'getByHariKelas']);
Route::apiResource('guru-mengajars', GuruMengajarController::class);