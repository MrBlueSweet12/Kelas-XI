<?php
use Illuminate\Support\Facades\Route;
use App\Http\Controllers\AuthController;
use App\Http\Controllers\GuruWebController;
use App\Http\Controllers\JadwalWebController;
use App\Http\Controllers\KelasWebController;
use App\Http\Controllers\MapelWebController;
use App\Http\Controllers\TahunAjaranWebController;
use App\Http\Controllers\UserWebController;

// Halaman login
Route::get('/', function () {
    return view('auth.login');
})->name('login');

Route::post('/login', [AuthController::class, 'loginAdmin'])->name('login.submit');

// Dashboard
Route::get('/dashboard', function () {
    return view('admin.dashboard');
})->name('admin.dashboard');

// Menu admin
Route::prefix('admin')->group(function () {
    Route::get('/user', fn() => view('admin.user.index'))->name('admin.user.index');
    Route::get('/guru', fn() => view('admin.guru.index'))->name('admin.guru.index');
    Route::get('/kelas', fn() => view('admin.kelas.index'))->name('admin.kelas.index');
    Route::get('/mapel', fn() => view('admin.mapel.index'))->name('admin.mapel.index');
    Route::get('/tahunajaran', fn() => view('admin.tahunajaran.index'))->name('admin.tahunajaran.index');
    Route::get('/jadwal', fn() => view('admin.jadwal.index'))->name('admin.jadwal.index');
});

Route::prefix('admin')->name('admin.')->group(function () {
    Route::resource('user', UserWebController::class)->except(['show']);
});

Route::prefix('admin')->group(function () {
    Route::resource('guru', GuruWebController::class)->names([
        'index' => 'admin.guru.index',
        'store' => 'admin.guru.store',
        'update' => 'admin.guru.update',
        'destroy' => 'admin.guru.destroy',
    ]);
});

Route::prefix('admin')->group(function () {
    Route::resource('kelas', KelasWebController::class)->names([
        'index' => 'admin.kelas.index',
        'store' => 'admin.kelas.store',
        'update' => 'admin.kelas.update',
        'destroy' => 'admin.kelas.destroy',
    ]);
});

Route::prefix('admin')->group(function () {
    Route::resource('mapel', MapelWebController::class)->names([
        'index' => 'admin.mapel.index',
        'store' => 'admin.mapel.store',
        'update' => 'admin.mapel.update',
        'destroy' => 'admin.mapel.destroy',
    ]);
});


Route::prefix('admin')->group(function () {
    Route::resource('tahunajaran', TahunAjaranWebController::class)->names([
        'index' => 'admin.tahunajaran.index',
        'store' => 'admin.tahunajaran.store',
        'update' => 'admin.tahunajaran.update',
        'destroy' => 'admin.tahunajaran.destroy',
    ]);
});

Route::prefix('admin')->group(function () {
    Route::resource('jadwal', JadwalWebController::class)->names([
        'index' => 'admin.jadwal.index',
        'store' => 'admin.jadwal.store',
        'update' => 'admin.jadwal.update',
        'destroy' => 'admin.jadwal.destroy',
    ]);
});
