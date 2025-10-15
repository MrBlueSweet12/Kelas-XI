<?php

use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\Schema;

class CreateTahunAjaransTable extends Migration
{
    public function up()
    {
        Schema::create('tahun_ajarans', function (Blueprint $table) {
            $table->id();
            $table->string('tahun');
            $table->boolean('flag')->default(1); // 0 atau 1, default 1
            $table->timestamps();
        });
    }

    public function down()
    {
        Schema::dropIfExists('tahun_ajarans');
    }
}
