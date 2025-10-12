<?php

namespace App\Http\Controllers;

use App\Models\Jadwal;
use Illuminate\Http\Request;

class JadwalController extends Controller
{
    public function index()
    {
        return response()->json(Jadwal::with(['guru', 'mapel', 'tahunAjaran', 'kelas'])->get());
    }

    public function store(Request $request)
    {
        $data = $request->validate([
            'guru_id' => 'required|exists:gurus,id',
            'mapel_id' => 'required|exists:mapels,id',
            'tahun_ajaran_id' => 'required|exists:tahun_ajarans,id',
            'kelas_id' => 'required|exists:kelas,id',
            'jam_ke' => 'required|string',
            'hari' => 'required|string',
        ]);

        $jadwal = Jadwal::create($data);
        return response()->json($jadwal, 201);
    }

    public function show(Jadwal $jadwal)
    {
        return response()->json($jadwal->load(['guru', 'mapel', 'tahunAjaran', 'kelas']));
    }

    public function update(Request $request, Jadwal $jadwal)
    {
        $data = $request->validate([
            'guru_id' => 'required|exists:gurus,id',
            'mapel_id' => 'required|exists:mapels,id',
            'tahun_ajaran_id' => 'required|exists:tahun_ajarans,id',
            'kelas_id' => 'required|exists:kelas,id',
            'jam_ke' => 'required|string',
            'hari' => 'required|string',
        ]);

        $jadwal->update($data);
        return response()->json($jadwal);
    }

    public function destroy(Jadwal $jadwal)
    {
        $jadwal->delete();
        return response()->json(['message' => 'Deleted']);
    }

    // ✅ Fungsi 1: Jam Ke, Mata Pelajaran, Kode Guru, Nama Guru berdasarkan kelas_id & hari
    public function getByClassAndDay($kelas_id, $hari)
    {
        $data = Jadwal::with(['guru', 'mapel'])
            ->where('kelas_id', $kelas_id)
            ->where('hari', $hari)
            ->get()
            ->map(function ($item) {
                return [
                    'jam_ke' => $item->jam_ke,
                    'mata_pelajaran' => $item->mapel->mapel ?? '-',
                    'kode_guru' => $item->guru->kode_guru ?? '-',
                    'nama_guru' => $item->guru->guru ?? '-',
                ];
            });

        return response()->json($data);
    }

    // ✅ Fungsi 2: Nama Guru, Mapel, Tahun Ajaran, Jam Ke berdasarkan kelas_id & hari
    public function getScheduleByClassAndDay($kelas_id, $hari)
    {
        $data = Jadwal::with(['guru', 'mapel', 'tahunAjaran'])
            ->where('kelas_id', $kelas_id)
            ->where('hari', $hari)
            ->get()
            ->map(function ($item) {
                return [
                    'nama_guru' => $item->guru->guru ?? '-',
                    'mata_pelajaran' => $item->mapel->mapel ?? '-',
                    'tahun_ajaran' => $item->tahunAjaran->tahun ?? '-',
                    'jam_ke' => $item->jam_ke,
                ];
            });

        return response()->json($data);
    }

    // 1) Dapatkan daftar guru yang mengajar di hari + kelas (distinct guru id + nama)
    // Route: GET /api/jadwal/guru-by-hari-kelas?hari=Senin&kelas_id=1
    public function getGurusByHariKelas(Request $request)
    {
        $data = $request->validate([
            'hari' => 'required|string',
            'kelas_id' => 'required|exists:kelas,id',
        ]);

        $gurus = Jadwal::where('hari', $data['hari'])
            ->where('kelas_id', $data['kelas_id'])
            ->join('gurus', 'jadwals.guru_id', '=', 'gurus.id')
            ->select('gurus.id as id', 'gurus.guru as guru')
            ->distinct()
            ->get();

        return response()->json($gurus);
    }

    // 2) Dapatkan daftar mapel + jam_ke berdasarkan hari + kelas + guru
    // Route: GET /api/jadwal/mapel-by-hari-kelas-guru?hari=Senin&kelas_id=1&guru_id=2
    public function getMapelAndJamByHariKelasGuru(Request $request)
    {
        $data = $request->validate([
            'hari' => 'required|string',
            'kelas_id' => 'required|exists:kelas,id',
            'guru_id' => 'required|exists:gurus,id',
        ]);

        $items = Jadwal::where('hari', $data['hari'])
            ->where('kelas_id', $data['kelas_id'])
            ->where('guru_id', $data['guru_id'])
            ->join('mapels', 'jadwals.mapel_id', '=', 'mapels.id')
            ->select('mapels.id as mapel_id', 'mapels.mapel as mapel', 'jadwals.jam_ke as jam_ke')
            ->distinct()
            ->get();

        return response()->json($items);
    }

    // 3) (Opsional) endpoint jadwal per kelas & hari yang sudah ada sebelumnya
    // Route: GET /api/jadwal/kelas/{kelas_id}/hari/{hari}
    public function scheduleByKelasHari($kelas_id, $hari)
    {
        $items = Jadwal::where('kelas_id', $kelas_id)
            ->where('hari', $hari)
            ->with(['guru:id,guru,kode_guru', 'mapel:id,mapel'])
            ->get()
            ->map(function ($j) {
                return [
                    'jam_ke' => $j->jam_ke,
                    'mata_pelajaran' => $j->mapel->mapel ?? null,
                    'kode_guru' => $j->guru->kode_guru ?? null,
                    'nama_guru' => $j->guru->guru ?? null,
                ];
            });

        return response()->json($items);
    }
}


