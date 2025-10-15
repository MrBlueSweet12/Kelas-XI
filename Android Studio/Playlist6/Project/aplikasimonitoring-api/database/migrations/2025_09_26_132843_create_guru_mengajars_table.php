<?php

use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\Schema;

class CreateGuruMengajarsTable extends Migration
{
    public function up()
    {
        Schema::create('guru_mengajars', function (Blueprint $table) {
            $table->id();
            $table->foreignId('jadwal_id')->constrained('jadwals')->onDelete('cascade');
            $table->string('keterangan')->nullable();
            $table->enum('status', ['Masuk', 'Tidak Masuk'])->default('Tidak Masuk');
            $table->timestamps();
        });
    }

    public function down()
    {
        Schema::dropIfExists('guru_mengajars');
    }
}
