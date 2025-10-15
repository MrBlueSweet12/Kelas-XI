<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use App\Models\TahunAjaran;

class TahunAjaranWebController extends Controller
{
    public function index()
    {
        $tahun_ajarans = TahunAjaran::all();
        return view('admin.tahunajaran.index', compact('tahun_ajarans'));
    }

    public function store(Request $request)
    {
        $request->validate([
            'tahun' => 'required|unique:tahun_ajarans,tahun',
            'flag' => 'required|in:0,1'
        ]);

        TahunAjaran::create($request->all());

        return redirect()->route('admin.tahunajaran.index')->with('success', 'Tahun Ajaran berhasil ditambahkan.');
    }

    public function update(Request $request, $id)
    {
        $ta = TahunAjaran::findOrFail($id);

        $request->validate([
            'tahun' => 'required|unique:tahun_ajarans,tahun,' . $id,
            'flag' => 'required|in:0,1'
        ]);

        $ta->update($request->all());

        return redirect()->route('admin.tahunajaran.index')->with('success', 'Tahun Ajaran berhasil diupdate.');
    }

    public function destroy($id)
    {
        $ta = TahunAjaran::findOrFail($id);
        $ta->delete();

        return redirect()->route('admin.tahunajaran.index')->with('success', 'Tahun Ajaran berhasil dihapus.');
    }
}
