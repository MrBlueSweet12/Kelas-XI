<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use App\Models\Mapel;

class MapelWebController extends Controller
{
    public function index()
    {
        $mapels = Mapel::all();
        return view('admin.mapel.index', compact('mapels'));
    }

    public function store(Request $request)
    {
        $request->validate([
            'kode_mapel' => 'required|unique:mapels,kode_mapel',
            'mapel' => 'required'
        ]);

        Mapel::create($request->all());

        return redirect()->route('admin.mapel.index')->with('success', 'Mapel berhasil ditambahkan.');
    }

    public function update(Request $request, $id)
    {
        $mapel = Mapel::findOrFail($id);

        $request->validate([
            'kode_mapel' => 'required|unique:mapels,kode_mapel,' . $id,
            'mapel' => 'required'
        ]);

        $mapel->update($request->all());

        return redirect()->route('admin.mapel.index')->with('success', 'Mapel berhasil diupdate.');
    }

    public function destroy($id)
    {
        $mapel = Mapel::findOrFail($id);
        $mapel->delete();

        return redirect()->route('admin.mapel.index')->with('success', 'Mapel berhasil dihapus.');
    }
}
