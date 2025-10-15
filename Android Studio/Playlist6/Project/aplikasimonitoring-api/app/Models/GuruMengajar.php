<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;

class GuruMengajar extends Model
{
    use HasFactory;

    protected $fillable = [
        'jadwal_id',
        'keterangan',
        'status',
    ];

    public function jadwal()
    {
        return $this->belongsTo(Jadwal::class);
    }
}
