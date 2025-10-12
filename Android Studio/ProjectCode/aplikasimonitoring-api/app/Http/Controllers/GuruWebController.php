<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use App\Models\Guru;

class GuruWebController extends Controller
{
    // Tampilkan semua guru
    public function index()
    {
        $gurus = Guru::all();
        return view('admin.guru.index', compact('gurus'));
    }

    // Simpan guru baru
    public function store(Request $request)
    {
        $request->validate([
            'kode_guru' => 'required|unique:gurus,kode_guru',
            'guru' => 'required',
            'telepon' => 'required'
        ]);

        Guru::create($request->all());

        return redirect()->route('admin.guru.index')->with('success', 'Guru berhasil ditambahkan.');
    }

    // Update guru
    public function update(Request $request, $id)
    {
        $guru = Guru::findOrFail($id);

        $request->validate([
            'kode_guru' => 'required|unique:gurus,kode_guru,' . $id,
            'guru' => 'required',
            'telepon' => 'required'
        ]);

        $guru->update($request->all());

        return redirect()->route('admin.guru.index')->with('success', 'Guru berhasil diupdate.');
    }

    // Hapus guru
    public function destroy($id)
    {
        $guru = Guru::findOrFail($id);
        $guru->delete();

        return redirect()->route('admin.guru.index')->with('success', 'Guru berhasil dihapus.');
    }
}
