<?php

namespace App\Http\Controllers;

use App\Models\GuruMengajar;
use App\Models\Jadwal;
use Illuminate\Http\Request;

class GuruMengajarController extends Controller
{
    public function index()
    {
        return response()->json(GuruMengajar::with('jadwal')->get());
    }

    public function store(Request $request)
    {
        // ✅ Validasi input
        $data = $request->validate([
            'hari' => 'required|string',
            'kelas_id' => 'required|exists:kelas,id',
            'guru_id' => 'required|exists:gurus,id',
            'mapel_id' => 'required|exists:mapels,id',
            'jam_ke' => 'required|string',
            'status' => 'required|in:Masuk,Tidak Masuk',
            'keterangan' => 'nullable|string',
        ]);

        // ✅ Cari jadwal_id dari tabel jadwals
        $jadwal = Jadwal::where('hari', $data['hari'])
            ->where('kelas_id', $data['kelas_id'])
            ->where('guru_id', $data['guru_id'])
            ->where('mapel_id', $data['mapel_id'])
            ->where('jam_ke', $data['jam_ke'])
            ->first();

        if (!$jadwal) {
            return response()->json(['message' => 'Jadwal tidak ditemukan'], 404);
        }

        // ✅ Simpan ke tabel guru_mengajars
        $gm = GuruMengajar::create([
            'jadwal_id' => $jadwal->id,
            'status' => $data['status'],
            'keterangan' => $data['keterangan'] ?? null,
        ]);

        return response()->json($gm->load('jadwal'), 201);
    }


    public function show(GuruMengajar $guruMengajar)
    {
        return response()->json($guruMengajar->load('jadwal'));
    }

    public function update(Request $request, $id)
    {
        $jadwal = GuruMengajar::findOrFail($id);
        $jadwal->status = $request->status;
        $jadwal->keterangan = $request->keterangan;
        $jadwal->save();

        return response()->json($jadwal);
    }

    public function destroy($id)
    {
        $jadwal = GuruMengajar::findOrFail($id);
        $jadwal->delete();

        return response()->json(['message' => 'Jadwal berhasil dihapus']);
    }

    public function getByHariKelas(Request $request)
    {
        $data = $request->validate([
            'hari' => 'required|string',
            'kelas_id' => 'required|exists:kelas,id',
        ]);

        $result = GuruMengajar::with(['jadwal.guru', 'jadwal.mapel'])
            ->whereHas('jadwal', function ($q) use ($data) {
                $q->where('hari', $data['hari'])
                ->where('kelas_id', $data['kelas_id']);
            })
            ->get()
            ->map(function ($gm) {
                return [
                    'id' => $gm->id,
                    'nama_guru' => $gm->jadwal->guru->guru ?? null,
                    'mapel' => $gm->jadwal->mapel->mapel ?? null,
                    'status' => $gm->status,
                    'keterangan' => $gm->keterangan,
                ];
            });

        return response()->json($result);
    }

    public function getTidakMasukByHariKelas(Request $request)
    {
        $data = $request->validate([
            'hari' => 'required|string',
            'kelas_id' => 'required|exists:kelas,id',
        ]);

        $result = GuruMengajar::with(['jadwal.guru', 'jadwal.mapel'])
            ->where('status', 'Tidak Masuk')
            ->whereHas('jadwal', function ($q) use ($data) {
                $q->where('hari', $data['hari'])
                ->where('kelas_id', $data['kelas_id']);
            })
            ->get()
            ->map(function ($gm) {
                return [
                    'nama_guru' => $gm->jadwal->guru->guru ?? null,
                    'mapel' => $gm->jadwal->mapel->mapel ?? null,
                    'jam_ke' => $gm->jadwal->jam_ke,
                    'status' => $gm->status,
                    'keterangan' => $gm->keterangan,
                ];
            });

        return response()->json($result);
    }


}
