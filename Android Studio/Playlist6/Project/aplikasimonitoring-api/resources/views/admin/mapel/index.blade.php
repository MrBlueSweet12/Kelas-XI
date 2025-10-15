@extends('layouts.app')

@section('title', 'Data Mapel')

@section('content')
<div class="container">
    <h2 class="mb-4">Data Mapel</h2>

    <!-- Tombol Tambah Mapel -->
    <button class="btn btn-primary mb-3 w-100" data-bs-toggle="modal" data-bs-target="#addMapelModal">
        + Tambah Mapel
    </button>

    <!-- Tabel Mapel -->
    <table class="table table-bordered">
        <thead class="table-dark">
            <tr>
                <th>No</th>
                <th>Kode Mapel</th>
                <th>Mapel</th>
                <th>Aksi</th>
            </tr>
        </thead>
        <tbody>
            @foreach ($mapels as $index => $m)
                <tr>
                    <td>{{ $index + 1 }}</td>
                    <td>{{ $m->kode_mapel }}</td>
                    <td>{{ $m->mapel }}</td>
                    <td>
                        <!-- Tombol Edit -->
                        <button class="btn btn-warning btn-sm editBtn"
                            data-id="{{ $m->id }}"
                            data-kode="{{ $m->kode_mapel }}"
                            data-nama="{{ $m->mapel }}"
                            data-bs-toggle="modal"
                            data-bs-target="#editMapelModal">
                            Edit
                        </button>

                        <!-- Tombol Delete -->
                        <form action="{{ route('admin.mapel.destroy', $m->id) }}" method="POST" class="d-inline">
                            @csrf
                            @method('DELETE')
                            <button type="submit" class="btn btn-danger btn-sm"
                                onclick="return confirm('Yakin hapus mapel ini?')">
                                Delete
                            </button>
                        </form>
                    </td>
                </tr>
            @endforeach
        </tbody>
    </table>
</div>

<!-- Modal Tambah Mapel -->
<div class="modal fade" id="addMapelModal" tabindex="-1" aria-hidden="true">
    <div class="modal-dialog">
        <form action="{{ route('admin.mapel.store') }}" method="POST">
            @csrf
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title">Tambah Mapel</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                </div>
                <div class="modal-body">
                    <div class="mb-3">
                        <label>Kode Mapel</label>
                        <input type="text" name="kode_mapel" class="form-control" required>
                    </div>
                    <div class="mb-3">
                        <label>Nama Mapel</label>
                        <input type="text" name="mapel" class="form-control" required>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Batal</button>
                    <button type="submit" class="btn btn-success">Simpan</button>
                </div>
            </div>
        </form>
    </div>
</div>

<!-- Modal Edit Mapel -->
<div class="modal fade" id="editMapelModal" tabindex="-1" aria-hidden="true">
    <div class="modal-dialog">
        <form id="editMapelForm" method="POST">
            @csrf
            @method('PUT')
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title">Edit Mapel</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                </div>
                <div class="modal-body">
                    <div class="mb-3">
                        <label>Kode Mapel</label>
                        <input type="text" name="kode_mapel" id="editKodeMapel" class="form-control" required>
                    </div>
                    <div class="mb-3">
                        <label>Nama Mapel</label>
                        <input type="text" name="mapel" id="editNamaMapel" class="form-control" required>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Batal</button>
                    <button type="submit" class="btn btn-success">Update</button>
                </div>
            </div>
        </form>
    </div>
</div>

<script>
document.addEventListener('DOMContentLoaded', function() {
    const editBtns = document.querySelectorAll('.editBtn');
    editBtns.forEach(btn => {
        btn.addEventListener('click', function() {
            let id = this.dataset.id;
            let kode = this.dataset.kode;
            let nama = this.dataset.nama;

            document.getElementById('editKodeMapel').value = kode;
            document.getElementById('editNamaMapel').value = nama;

            let form = document.getElementById('editMapelForm');
            form.action = "{{ url('admin/mapel') }}/" + id;
        });
    });
});
</script>
@endsection
