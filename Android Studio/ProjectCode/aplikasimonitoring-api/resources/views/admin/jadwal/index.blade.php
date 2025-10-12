@extends('layouts.app')

@section('title', 'Jadwal')

@section('content')
<div class="container">
    <h2 class="mb-4">Data Jadwal</h2>

    <!-- Tombol Tambah Jadwal -->
    <button class="btn btn-primary mb-3 w-100" data-bs-toggle="modal" data-bs-target="#addJadwalModal">
        + Tambah Jadwal
    </button>

    <!-- Tabel Jadwal -->
    <table class="table table-bordered">
        <thead class="table-dark">
            <tr>
                <th>No</th>
                <th>Nama Guru</th>
                <th>Mapel</th>
                <th>Tahun</th>
                <th>Kelas</th>
                <th>Jam Ke</th>
                <th>Hari</th>
                <th>Aksi</th>
            </tr>
        </thead>
        <tbody>
            @foreach ($jadwals as $index => $jadwal)
                <tr>
                    <td>{{ $index+1 }}</td>
                    <td>{{ $jadwal->guru->guru }}</td>
                    <td>{{ $jadwal->mapel->mapel }}</td>
                    <td>{{ $jadwal->tahunAjaran->tahun }}</td>
                    <td>{{ $jadwal->kelas->kelas }}</td>
                    <td>{{ $jadwal->jam_ke }}</td>
                    <td>{{ $jadwal->hari }}</td>
                    <td>
                        <button class="btn btn-warning btn-sm editBtn"
                            data-id="{{ $jadwal->id }}"
                            data-guru="{{ $jadwal->guru_id }}"
                            data-mapel="{{ $jadwal->mapel_id }}"
                            data-tahun="{{ $jadwal->tahun_ajaran_id }}"
                            data-kelas="{{ $jadwal->kelas_id }}"
                            data-jam="{{ $jadwal->jam_ke }}"
                            data-hari="{{ $jadwal->hari }}"
                            data-bs-toggle="modal"
                            data-bs-target="#editJadwalModal">
                            Edit
                        </button>

                        <form action="{{ route('admin.jadwal.destroy', $jadwal->id) }}" method="POST" class="d-inline">
                            @csrf
                            @method('DELETE')
                            <button type="submit" class="btn btn-danger btn-sm"
                                onclick="return confirm('Yakin hapus jadwal ini?')">
                                Delete
                            </button>
                        </form>
                    </td>
                </tr>
            @endforeach
        </tbody>
    </table>
</div>

<!-- Modal Tambah Jadwal -->
<div class="modal fade" id="addJadwalModal" tabindex="-1" aria-hidden="true">
    <div class="modal-dialog">
        <form action="{{ route('admin.jadwal.store') }}" method="POST">
            @csrf
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title">Tambah Jadwal</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                </div>
                <div class="modal-body">
                    <div class="mb-3">
                        <label>Guru</label>
                        <select name="guru_id" class="form-control" required>
                            <option value="">-- Pilih Guru --</option>
                            @foreach($gurus as $guru)
                                <option value="{{ $guru->id }}">{{ $guru->guru }}</option>
                            @endforeach
                        </select>
                    </div>
                    <div class="mb-3">
                        <label>Mapel</label>
                        <select name="mapel_id" class="form-control" required>
                            <option value="">-- Pilih Mapel --</option>
                            @foreach($mapels as $mapel)
                                <option value="{{ $mapel->id }}">{{ $mapel->mapel }}</option>
                            @endforeach
                        </select>
                    </div>
                    <div class="mb-3">
                        <label>Tahun Ajaran</label>
                        <select name="tahun_ajaran_id" class="form-control" required>
                            <option value="">-- Pilih Tahun --</option>
                            @foreach($tahun_ajarans as $tahun)
                                <option value="{{ $tahun->id }}">{{ $tahun->tahun }}</option>
                            @endforeach
                        </select>
                    </div>
                    <div class="mb-3">
                        <label>Kelas</label>
                        <select name="kelas_id" class="form-control" required>
                            <option value="">-- Pilih Kelas --</option>
                            @foreach($kelas as $k)
                                <option value="{{ $k->id }}">{{ $k->kelas }}</option>
                            @endforeach
                        </select>
                    </div>
                    <div class="mb-3">
                        <label>Jam Ke</label>
                        <input name="jam_ke" class="form-control" min="1" required>
                    </div>
                    <div class="mb-3">
                        <label>Hari</label>
                        <select name="hari" class="form-control" required>
                            @foreach(['Senin','Selasa','Rabu','Kamis','Jumat','Sabtu','Minggu'] as $hari)
                                <option value="{{ $hari }}">{{ $hari }}</option>
                            @endforeach
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

<!-- Modal Edit Jadwal -->
<div class="modal fade" id="editJadwalModal" tabindex="-1" aria-hidden="true">
    <div class="modal-dialog">
        <form id="editJadwalForm" method="POST">
            @csrf
            @method('PUT')
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title">Edit Jadwal</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                </div>
                <div class="modal-body">
                    <input type="hidden" id="editJadwalId">
                    <div class="mb-3">
                        <label>Guru</label>
                        <select name="guru_id" id="editGuru" class="form-control" required>
                            <option value="">-- Pilih Guru --</option>
                            @foreach($gurus as $guru)
                                <option value="{{ $guru->id }}">{{ $guru->guru }}</option>
                            @endforeach
                        </select>
                    </div>
                    <div class="mb-3">
                        <label>Mapel</label>
                        <select name="mapel_id" id="editMapel" class="form-control" required>
                            <option value="">-- Pilih Mapel --</option>
                            @foreach($mapels as $mapel)
                                <option value="{{ $mapel->id }}">{{ $mapel->mapel }}</option>
                            @endforeach
                        </select>
                    </div>
                    <div class="mb-3">
                        <label>Tahun Ajaran</label>
                        <select name="tahun_ajaran_id" id="editTahun" class="form-control" required>
                            <option value="">-- Pilih Tahun --</option>
                            @foreach($tahun_ajarans as $tahun)
                                <option value="{{ $tahun->id }}">{{ $tahun->tahun }}</option>
                            @endforeach
                        </select>
                    </div>
                    <div class="mb-3">
                        <label>Kelas</label>
                        <select name="kelas_id" id="editKelas" class="form-control" required>
                            <option value="">-- Pilih Kelas --</option>
                            @foreach($kelas as $k)
                                <option value="{{ $k->id }}">{{ $k->kelas }}</option>
                            @endforeach
                        </select>
                    </div>
                    <div class="mb-3">
                        <label>Jam Ke</label>
                        <input name="jam_ke" id="editJam" class="form-control" min="1" required>
                    </div>
                    <div class="mb-3">
                        <label>Hari</label>
                        <select name="hari" id="editHari" class="form-control" required>
                            @foreach(['Senin','Selasa','Rabu','Kamis','Jumat','Sabtu','Minggu'] as $hari)
                                <option value="{{ $hari }}">{{ $hari }}</option>
                            @endforeach
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
            let guru = this.dataset.guru;
            let mapel = this.dataset.mapel;
            let tahun = this.dataset.tahun;
            let kelas = this.dataset.kelas;
            let jam = this.dataset.jam;
            let hari = this.dataset.hari;

            document.getElementById('editGuru').value = guru;
            document.getElementById('editMapel').value = mapel;
            document.getElementById('editTahun').value = tahun;
            document.getElementById('editKelas').value = kelas;
            document.getElementById('editJam').value = jam;
            document.getElementById('editHari').value = hari;

            const form = document.getElementById('editJadwalForm');
            form.action = "/admin/jadwal/" + id;
        });
    });
});
</script>
@endsection
