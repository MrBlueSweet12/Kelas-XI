@extends('layouts.app')

@section('title', 'Data Guru')

@section('content')
<div class="container">
    <h2 class="mb-4">Data Guru</h2>

    <!-- Tombol Tambah Guru -->
    <button class="btn btn-primary mb-3 w-100" data-bs-toggle="modal" data-bs-target="#addGuruModal">
        + Tambah Guru
    </button>

    <!-- Tabel Guru -->
    <table class="table table-bordered">
        <thead class="table-dark">
            <tr>
                <th>No</th>
                <th>Kode Guru</th>
                <th>Guru</th>
                <th>Telepon</th>
                <th>Aksi</th>
            </tr>
        </thead>
        <tbody>
            @foreach ($gurus as $index => $guru)
                <tr>
                    <td>{{ $index+1 }}</td>
                    <td>{{ $guru->kode_guru }}</td>
                    <td>{{ $guru->guru }}</td>
                    <td>{{ $guru->telepon }}</td>
                    <td>
                        <!-- Tombol Edit -->
                        <button class="btn btn-warning btn-sm editBtn"
                            data-id="{{ $guru->id }}"
                            data-kode="{{ $guru->kode_guru }}"
                            data-nama="{{ $guru->guru }}"
                            data-telepon="{{ $guru->telepon }}"
                            data-bs-toggle="modal"
                            data-bs-target="#editGuruModal">
                            Edit
                        </button>

                        <!-- Tombol Delete -->
                        <form action="{{ route('admin.guru.destroy', $guru->id) }}" method="POST" class="d-inline">
                            @csrf
                            @method('DELETE')
                            <button type="submit" class="btn btn-danger btn-sm"
                                onclick="return confirm('Yakin hapus guru ini?')">
                                Delete
                            </button>
                        </form>
                    </td>
                </tr>
            @endforeach
        </tbody>
    </table>
</div>

<!-- Modal Tambah Guru -->
<div class="modal fade" id="addGuruModal" tabindex="-1" aria-hidden="true">
    <div class="modal-dialog">
        <form action="{{ route('admin.guru.store') }}" method="POST">
            @csrf
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title">Tambah Guru</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                </div>
                <div class="modal-body">
                    <div class="mb-3">
                        <label>Kode Guru</label>
                        <input type="text" name="kode_guru" class="form-control" required>
                    </div>
                    <div class="mb-3">
                        <label>Guru</label>
                        <input type="text" name="guru" class="form-control" required>
                    </div>
                    <div class="mb-3">
                        <label>Telepon</label>
                        <input type="text" name="telepon" class="form-control" required>
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

<!-- Modal Edit Guru -->
<div class="modal fade" id="editGuruModal" tabindex="-1" aria-hidden="true">
    <div class="modal-dialog">
        <form id="editGuruForm" method="POST">
            @csrf
            @method('PUT')
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title">Edit Guru</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                </div>
                <div class="modal-body">
                    <div class="mb-3">
                        <label>Kode Guru</label>
                        <input type="text" name="kode_guru" id="editKode" class="form-control" required>
                    </div>
                    <div class="mb-3">
                        <label>Guru</label>
                        <input type="text" name="guru" id="editNama" class="form-control" required>
                    </div>
                    <div class="mb-3">
                        <label>Telepon</label>
                        <input type="text" name="telepon" id="editTelepon" class="form-control" required>
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

<!-- Script untuk isi modal Edit -->
<script>
document.addEventListener('DOMContentLoaded', function() {
    const editBtns = document.querySelectorAll('.editBtn');
    editBtns.forEach(btn => {
        btn.addEventListener('click', function() {
            let id = this.dataset.id;
            let kode = this.dataset.kode;
            let nama = this.dataset.nama;
            let telepon = this.dataset.telepon;

            document.getElementById('editKode').value = kode;
            document.getElementById('editNama').value = nama;
            document.getElementById('editTelepon').value = telepon;

            let form = document.getElementById('editGuruForm');
            form.action = "{{ url('admin/guru') }}/" + id;
        });
    });
});
</script>
@endsection
