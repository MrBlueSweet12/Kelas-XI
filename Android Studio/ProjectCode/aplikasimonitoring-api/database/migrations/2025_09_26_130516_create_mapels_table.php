<?php

use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\Schema;

class CreateMapelsTable extends Migration
{
    public function up()
    {
        Schema::create('mapels', function (Blueprint $table) {
            $table->id();
            $table->string('kode_mapel')->unique();
            $table->string('mapel');
            $table->timestamps();
        });
    }

    public function down()
    {
        Schema::dropIfExists('mapels');
    }
}
