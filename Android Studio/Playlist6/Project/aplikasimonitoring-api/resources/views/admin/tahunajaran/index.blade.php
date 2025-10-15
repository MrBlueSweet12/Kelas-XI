@extends('layouts.app')

@section('title', 'Data Tahun Ajaran')

@section('content')
<div class="container">
    <h2 class="mb-4">Data Tahun Ajaran</h2>

    <!-- Tombol Tambah Tahun Ajaran -->
    <button class="btn btn-primary mb-3 w-100" data-bs-toggle="modal" data-bs-target="#addTahunAjaranModal">
        + Tambah Tahun Ajaran
    </button>

    <!-- Tabel Tahun Ajaran -->
    <table class="table table-bordered">
        <thead class="table-dark">
            <tr>
                <th>No</th>
                <th>Tahun</th>
                <th>Flag</th>
                <th>Aksi</th>
            </tr>
        </thead>
        <tbody>
            @foreach ($tahun_ajarans as $index => $t)
                <tr>
                    <td>{{ $index + 1 }}</td>
                    <td>{{ $t->tahun }}</td>
                    <td>{{ $t->flag }}</td>
                    <td>
                        <!-- Tombol Edit -->
                        <button class="btn btn-warning btn-sm editBtn"
                            data-id="{{ $t->id }}"
                            data-tahun="{{ $t->tahun }}"
                            data-flag="{{ $t->flag }}"
                            data-bs-toggle="modal"
                            data-bs-target="#editTahunAjaranModal">
                            Edit
                        </button>

                        <!-- Tombol Delete -->
                        <form action="{{ route('admin.tahunajaran.destroy', $t->id) }}" method="POST" class="d-inline">
                            @csrf
                            @method('DELETE')
                            <button type="submit" class="btn btn-danger btn-sm"
                                onclick="return confirm('Yakin hapus tahun ajaran ini?')">
                                Delete
                            </button>
                        </form>
                    </td>
                </tr>
            @endforeach
        </tbody>
    </table>
</div>

<!-- Modal Tambah Tahun Ajaran -->
<div class="modal fade" id="addTahunAjaranModal" tabindex="-1" aria-hidden="true">
    <div class="modal-dialog">
        <form action="{{ route('admin.tahunajaran.store') }}" method="POST">
            @csrf
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title">Tambah Tahun Ajaran</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                </div>
                <div class="modal-body">
                    <div class="mb-3">
                        <label>Tahun</label>
                        <input type="text" name="tahun" class="form-control" required>
                    </div>
                    <div class="mb-3">
                        <label>Flag</label>
                        <select name="flag" class="form-control" required>
                            <option value="1">1</option>
                            <option value="0">0</option>
                        </select>
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

<!-- Modal Edit Tahun Ajaran -->
<div class="modal fade" id="editTahunAjaranModal" tabindex="-1" aria-hidden="true">
    <div class="modal-dialog">
        <form id="editTahunAjaranForm" method="POST">
            @csrf
            @method('PUT')
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title">Edit Tahun Ajaran</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                </div>
                <div class="modal-body">
                    <div class="mb-3">
                        <label>Tahun</label>
                        <input type="text" name="tahun" id="editTahun" class="form-control" required>
                    </div>
                    <div class="mb-3">
                        <label>Flag</label>
                        <select name="flag" id="editFlag" class="form-control" required>
                            <option value="1">1</option>
                            <option value="0">0</option>
                        </select>
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
            let tahun = this.dataset.tahun;
            let flag = this.dataset.flag;

            document.getElementById('editTahun').value = tahun;
            document.getElementById('editFlag').value = flag;

            let form = document.getElementById('editTahunAjaranForm');
            form.action = "{{ url('admin/tahunajaran') }}/" + id;
        });
    });
});
</script>
@endsection
