-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Waktu pembuatan: 01 Okt 2025 pada 09.14
-- Versi server: 10.4.32-MariaDB
-- Versi PHP: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `aplikasimonitoringkelas`
--

-- --------------------------------------------------------

--
-- Struktur dari tabel `cache`
--

CREATE TABLE `cache` (
  `key` varchar(255) NOT NULL,
  `value` mediumtext NOT NULL,
  `expiration` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Struktur dari tabel `cache_locks`
--

CREATE TABLE `cache_locks` (
  `key` varchar(255) NOT NULL,
  `owner` varchar(255) NOT NULL,
  `expiration` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Struktur dari tabel `failed_jobs`
--

CREATE TABLE `failed_jobs` (
  `id` bigint(20) UNSIGNED NOT NULL,
  `uuid` varchar(255) NOT NULL,
  `connection` text NOT NULL,
  `queue` text NOT NULL,
  `payload` longtext NOT NULL,
  `exception` longtext NOT NULL,
  `failed_at` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Struktur dari tabel `gurus`
--

CREATE TABLE `gurus` (
  `id` bigint(20) UNSIGNED NOT NULL,
  `kode_guru` varchar(255) NOT NULL,
  `guru` varchar(255) NOT NULL,
  `telepon` varchar(255) DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data untuk tabel `gurus`
--

INSERT INTO `gurus` (`id`, `kode_guru`, `guru`, `telepon`, `created_at`, `updated_at`) VALUES
(1, 'G001', 'Budi Setiawan', '08547382950', '2025-09-26 06:37:18', '2025-09-26 06:37:18'),
(2, 'G002', 'Joko', '08547382950', '2025-09-26 06:37:45', '2025-09-26 06:37:45'),
(3, 'G004', 'Agung', '08547382950', '2025-09-26 06:37:52', '2025-09-26 06:37:52'),
(4, 'G003', 'Agus', '08547382950', '2025-09-26 06:37:57', '2025-09-26 06:37:57');

-- --------------------------------------------------------

--
-- Struktur dari tabel `guru_mengajars`
--

CREATE TABLE `guru_mengajars` (
  `id` bigint(20) UNSIGNED NOT NULL,
  `jadwal_id` bigint(20) UNSIGNED NOT NULL,
  `keterangan` varchar(255) DEFAULT NULL,
  `status` enum('Masuk','Tidak Masuk') NOT NULL DEFAULT 'Tidak Masuk',
  `created_at` timestamp NULL DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data untuk tabel `guru_mengajars`
--

INSERT INTO `guru_mengajars` (`id`, `jadwal_id`, `keterangan`, `status`, `created_at`, `updated_at`) VALUES
(2, 2, 'Guru Ada Rapat Di Gantikan Pak Agung', 'Tidak Masuk', '2025-09-26 06:44:41', '2025-09-26 06:44:41'),
(3, 3, 'Datang Tepat Waktu', 'Masuk', '2025-09-26 06:44:55', '2025-09-26 06:44:55'),
(6, 1, 'Datang tepat waktu', 'Masuk', '2025-09-28 19:47:07', '2025-09-28 19:47:07'),
(8, 3, 'ada rapat', 'Tidak Masuk', '2025-09-29 18:55:26', '2025-09-29 18:55:26'),
(9, 2, 'Izin Pulang Sebentar', 'Tidak Masuk', '2025-09-29 19:11:49', '2025-09-29 19:11:49');

-- --------------------------------------------------------

--
-- Struktur dari tabel `jadwals`
--

CREATE TABLE `jadwals` (
  `id` bigint(20) UNSIGNED NOT NULL,
  `guru_id` bigint(20) UNSIGNED NOT NULL,
  `mapel_id` bigint(20) UNSIGNED NOT NULL,
  `tahun_ajaran_id` bigint(20) UNSIGNED NOT NULL,
  `kelas_id` bigint(20) UNSIGNED NOT NULL,
  `jam_ke` varchar(255) NOT NULL,
  `hari` varchar(255) NOT NULL,
  `created_at` timestamp NULL DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data untuk tabel `jadwals`
--

INSERT INTO `jadwals` (`id`, `guru_id`, `mapel_id`, `tahun_ajaran_id`, `kelas_id`, `jam_ke`, `hari`, `created_at`, `updated_at`) VALUES
(1, 1, 1, 1, 1, 'Jam Ke 1-2', 'Senin', '2025-09-26 06:42:03', '2025-09-26 06:42:03'),
(2, 2, 2, 1, 1, 'Jam Ke 3-4', 'Senin', '2025-09-26 06:42:23', '2025-09-26 06:42:23'),
(3, 2, 2, 1, 2, 'Jam Ke 1-2', 'Senin', '2025-09-26 06:42:33', '2025-09-26 06:42:33'),
(4, 1, 1, 1, 2, 'Jam Ke 3-4', 'Senin', '2025-09-26 06:42:39', '2025-09-26 06:43:09'),
(5, 1, 2, 3, 3, 'Jam Ke 1-4', 'Rabu', '2025-09-28 22:50:30', '2025-09-28 22:50:30'),
(6, 2, 3, 2, 2, 'Jam Ke 1-4', 'Selasa', '2025-09-29 00:00:13', '2025-09-29 00:00:13');

-- --------------------------------------------------------

--
-- Struktur dari tabel `jobs`
--

CREATE TABLE `jobs` (
  `id` bigint(20) UNSIGNED NOT NULL,
  `queue` varchar(255) NOT NULL,
  `payload` longtext NOT NULL,
  `attempts` tinyint(3) UNSIGNED NOT NULL,
  `reserved_at` int(10) UNSIGNED DEFAULT NULL,
  `available_at` int(10) UNSIGNED NOT NULL,
  `created_at` int(10) UNSIGNED NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Struktur dari tabel `job_batches`
--

CREATE TABLE `job_batches` (
  `id` varchar(255) NOT NULL,
  `name` varchar(255) NOT NULL,
  `total_jobs` int(11) NOT NULL,
  `pending_jobs` int(11) NOT NULL,
  `failed_jobs` int(11) NOT NULL,
  `failed_job_ids` longtext NOT NULL,
  `options` mediumtext DEFAULT NULL,
  `cancelled_at` int(11) DEFAULT NULL,
  `created_at` int(11) NOT NULL,
  `finished_at` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Struktur dari tabel `kelas`
--

CREATE TABLE `kelas` (
  `id` bigint(20) UNSIGNED NOT NULL,
  `kelas` varchar(255) NOT NULL,
  `created_at` timestamp NULL DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data untuk tabel `kelas`
--

INSERT INTO `kelas` (`id`, `kelas`, `created_at`, `updated_at`) VALUES
(1, 'X RPL', '2025-09-26 06:38:36', '2025-09-26 06:38:36'),
(2, 'XI RPL', '2025-09-26 06:38:38', '2025-09-26 06:38:38'),
(3, 'XII RPL', '2025-09-26 06:38:39', '2025-09-26 06:38:39');

-- --------------------------------------------------------

--
-- Struktur dari tabel `mapels`
--

CREATE TABLE `mapels` (
  `id` bigint(20) UNSIGNED NOT NULL,
  `kode_mapel` varchar(255) NOT NULL,
  `mapel` varchar(255) NOT NULL,
  `created_at` timestamp NULL DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data untuk tabel `mapels`
--

INSERT INTO `mapels` (`id`, `kode_mapel`, `mapel`, `created_at`, `updated_at`) VALUES
(1, 'M01', 'Bahasa', '2025-09-26 06:39:19', '2025-09-26 06:39:19'),
(2, 'M02', 'Matematika', '2025-09-26 06:39:27', '2025-09-26 06:39:27'),
(3, 'M03', 'IPA', '2025-09-26 06:39:32', '2025-09-26 06:39:32'),
(4, 'M04', 'IPS', '2025-09-26 06:39:36', '2025-09-26 06:39:36'),
(5, 'M05', 'PKN', '2025-09-26 06:39:48', '2025-09-26 06:39:48');

-- --------------------------------------------------------

--
-- Struktur dari tabel `migrations`
--

CREATE TABLE `migrations` (
  `id` int(10) UNSIGNED NOT NULL,
  `migration` varchar(255) NOT NULL,
  `batch` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data untuk tabel `migrations`
--

INSERT INTO `migrations` (`id`, `migration`, `batch`) VALUES
(1, '0001_01_01_000000_create_users_table', 1),
(2, '0001_01_01_000001_create_cache_table', 1),
(3, '0001_01_01_000002_create_jobs_table', 1),
(4, '2025_09_26_130515_create_gurus_table', 1),
(5, '2025_09_26_130516_create_kelas_table', 1),
(6, '2025_09_26_130516_create_mapels_table', 1),
(7, '2025_09_26_130516_create_tahun_ajarans_table', 1),
(8, '2025_09_26_131702_create_personal_access_tokens_table', 1),
(9, '2025_09_26_132826_create_jadwals_table', 2),
(10, '2025_09_26_132843_create_guru_mengajars_table', 2);

-- --------------------------------------------------------

--
-- Struktur dari tabel `personal_access_tokens`
--

CREATE TABLE `personal_access_tokens` (
  `id` bigint(20) UNSIGNED NOT NULL,
  `tokenable_type` varchar(255) NOT NULL,
  `tokenable_id` bigint(20) UNSIGNED NOT NULL,
  `name` text NOT NULL,
  `token` varchar(64) NOT NULL,
  `abilities` text DEFAULT NULL,
  `last_used_at` timestamp NULL DEFAULT NULL,
  `expires_at` timestamp NULL DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data untuk tabel `personal_access_tokens`
--

INSERT INTO `personal_access_tokens` (`id`, `tokenable_type`, `tokenable_id`, `name`, `token`, `abilities`, `last_used_at`, `expires_at`, `created_at`, `updated_at`) VALUES
(1, 'App\\Models\\User', 1, 'auth_token', '5bac526f4147c16efbae79d12b323fbf195fe622b3b00442d0132f4db793a866', '[\"*\"]', NULL, NULL, '2025-09-29 19:28:04', '2025-09-29 19:28:04'),
(2, 'App\\Models\\User', 1, 'auth_token', '67978c527c7b9b8943f4d80c5473984cd40b1129604913b0dd7e52d81e7ce6fd', '[\"*\"]', NULL, NULL, '2025-09-29 19:30:03', '2025-09-29 19:30:03'),
(3, 'App\\Models\\User', 2, 'auth_token', '6ae26f5785c5567304afb22a39e1823c349bc79a9cd955fbd6abe3062d8b9ae2', '[\"*\"]', NULL, NULL, '2025-09-29 19:30:26', '2025-09-29 19:30:26'),
(4, 'App\\Models\\User', 4, 'auth_token', 'de562ac09bc4170bc2477e50ca86b4bb94211588deeb5eeae19a09e9298db36d', '[\"*\"]', NULL, NULL, '2025-09-29 19:30:53', '2025-09-29 19:30:53'),
(5, 'App\\Models\\User', 3, 'auth_token', '783210d31c09d10c942241ffe0f02daf48f32407556c0249771a1b6d7e5434d4', '[\"*\"]', NULL, NULL, '2025-09-29 19:32:15', '2025-09-29 19:32:15');

-- --------------------------------------------------------

--
-- Struktur dari tabel `tahun_ajarans`
--

CREATE TABLE `tahun_ajarans` (
  `id` bigint(20) UNSIGNED NOT NULL,
  `tahun` varchar(255) NOT NULL,
  `flag` tinyint(1) NOT NULL DEFAULT 1,
  `created_at` timestamp NULL DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data untuk tabel `tahun_ajarans`
--

INSERT INTO `tahun_ajarans` (`id`, `tahun`, `flag`, `created_at`, `updated_at`) VALUES
(1, '2023/2024', 1, '2025-09-26 06:40:33', '2025-09-26 06:40:33'),
(2, '2024/2025', 1, '2025-09-26 06:40:42', '2025-09-26 06:40:42'),
(3, '2025/2026', 1, '2025-09-26 06:40:47', '2025-09-26 06:40:47'),
(4, '2026/2027', 1, '2025-09-26 06:40:52', '2025-09-26 06:40:52'),
(5, '2030/3031', 0, '2025-09-30 06:26:20', '2025-09-30 06:26:35');

-- --------------------------------------------------------

--
-- Struktur dari tabel `users`
--

CREATE TABLE `users` (
  `id` bigint(20) UNSIGNED NOT NULL,
  `nama` varchar(255) NOT NULL,
  `email` varchar(255) NOT NULL,
  `email_verified_at` timestamp NULL DEFAULT NULL,
  `password` varchar(255) NOT NULL,
  `role` enum('Siswa','Waka Kurikulum','Kepala Sekolah','Admin') NOT NULL DEFAULT 'Siswa',
  `remember_token` varchar(100) DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data untuk tabel `users`
--

INSERT INTO `users` (`id`, `nama`, `email`, `email_verified_at`, `password`, `role`, `remember_token`, `created_at`, `updated_at`) VALUES
(1, 'Siregar', 'siregar@example.com', NULL, '$2y$12$BlmXEmEC1yk9JN5ZG9uAJOeWRsxQ7G36jNH7H.TX5qIQUoWryOPDC', 'Siswa', NULL, '2025-09-26 06:34:36', '2025-09-26 06:36:02'),
(2, 'Raka', 'raka@example.com', NULL, '$2y$12$2a8e82q5ICCuzRZi4oHzCes.KMPVP4ofAy/924jl2vm8adU1RNWGW', 'Kepala Sekolah', NULL, '2025-09-26 06:34:53', '2025-09-26 06:34:53'),
(3, 'Dyra', 'dyra@example.com', NULL, '$2y$12$RmjT1SCnw3y7nwtrjTwGmedaYoS4ZywzNIP0Vth2c5l3RK3/7EeIi', 'Waka Kurikulum', NULL, '2025-09-26 06:35:08', '2025-09-26 06:35:08'),
(4, 'Admin', 'admin@example.com', NULL, '$2y$12$yPCnSnHxGPZTOYmOrxZXq.wsvwF6HIUGSTwbj43WOEKBPi4t1YCSi', 'Admin', NULL, '2025-09-26 06:35:19', '2025-09-26 06:35:19');

--
-- Indexes for dumped tables
--

--
-- Indeks untuk tabel `cache`
--
ALTER TABLE `cache`
  ADD PRIMARY KEY (`key`);

--
-- Indeks untuk tabel `cache_locks`
--
ALTER TABLE `cache_locks`
  ADD PRIMARY KEY (`key`);

--
-- Indeks untuk tabel `failed_jobs`
--
ALTER TABLE `failed_jobs`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `failed_jobs_uuid_unique` (`uuid`);

--
-- Indeks untuk tabel `gurus`
--
ALTER TABLE `gurus`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `gurus_kode_guru_unique` (`kode_guru`);

--
-- Indeks untuk tabel `guru_mengajars`
--
ALTER TABLE `guru_mengajars`
  ADD PRIMARY KEY (`id`),
  ADD KEY `guru_mengajars_jadwal_id_foreign` (`jadwal_id`);

--
-- Indeks untuk tabel `jadwals`
--
ALTER TABLE `jadwals`
  ADD PRIMARY KEY (`id`),
  ADD KEY `jadwals_guru_id_foreign` (`guru_id`),
  ADD KEY `jadwals_mapel_id_foreign` (`mapel_id`),
  ADD KEY `jadwals_tahun_ajaran_id_foreign` (`tahun_ajaran_id`),
  ADD KEY `jadwals_kelas_id_foreign` (`kelas_id`);

--
-- Indeks untuk tabel `jobs`
--
ALTER TABLE `jobs`
  ADD PRIMARY KEY (`id`),
  ADD KEY `jobs_queue_index` (`queue`);

--
-- Indeks untuk tabel `job_batches`
--
ALTER TABLE `job_batches`
  ADD PRIMARY KEY (`id`);

--
-- Indeks untuk tabel `kelas`
--
ALTER TABLE `kelas`
  ADD PRIMARY KEY (`id`);

--
-- Indeks untuk tabel `mapels`
--
ALTER TABLE `mapels`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `mapels_kode_mapel_unique` (`kode_mapel`);

--
-- Indeks untuk tabel `migrations`
--
ALTER TABLE `migrations`
  ADD PRIMARY KEY (`id`);

--
-- Indeks untuk tabel `personal_access_tokens`
--
ALTER TABLE `personal_access_tokens`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `personal_access_tokens_token_unique` (`token`),
  ADD KEY `personal_access_tokens_tokenable_type_tokenable_id_index` (`tokenable_type`,`tokenable_id`),
  ADD KEY `personal_access_tokens_expires_at_index` (`expires_at`);

--
-- Indeks untuk tabel `tahun_ajarans`
--
ALTER TABLE `tahun_ajarans`
  ADD PRIMARY KEY (`id`);

--
-- Indeks untuk tabel `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `users_email_unique` (`email`);

--
-- AUTO_INCREMENT untuk tabel yang dibuang
--

--
-- AUTO_INCREMENT untuk tabel `failed_jobs`
--
ALTER TABLE `failed_jobs`
  MODIFY `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT untuk tabel `gurus`
--
ALTER TABLE `gurus`
  MODIFY `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT untuk tabel `guru_mengajars`
--
ALTER TABLE `guru_mengajars`
  MODIFY `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=10;

--
-- AUTO_INCREMENT untuk tabel `jadwals`
--
ALTER TABLE `jadwals`
  MODIFY `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- AUTO_INCREMENT untuk tabel `jobs`
--
ALTER TABLE `jobs`
  MODIFY `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT untuk tabel `kelas`
--
ALTER TABLE `kelas`
  MODIFY `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT untuk tabel `mapels`
--
ALTER TABLE `mapels`
  MODIFY `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT untuk tabel `migrations`
--
ALTER TABLE `migrations`
  MODIFY `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- AUTO_INCREMENT untuk tabel `personal_access_tokens`
--
ALTER TABLE `personal_access_tokens`
  MODIFY `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT untuk tabel `tahun_ajarans`
--
ALTER TABLE `tahun_ajarans`
  MODIFY `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT untuk tabel `users`
--
ALTER TABLE `users`
  MODIFY `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- Ketidakleluasaan untuk tabel pelimpahan (Dumped Tables)
--

--
-- Ketidakleluasaan untuk tabel `guru_mengajars`
--
ALTER TABLE `guru_mengajars`
  ADD CONSTRAINT `guru_mengajars_jadwal_id_foreign` FOREIGN KEY (`jadwal_id`) REFERENCES `jadwals` (`id`) ON DELETE CASCADE;

--
-- Ketidakleluasaan untuk tabel `jadwals`
--
ALTER TABLE `jadwals`
  ADD CONSTRAINT `jadwals_guru_id_foreign` FOREIGN KEY (`guru_id`) REFERENCES `gurus` (`id`) ON DELETE CASCADE,
  ADD CONSTRAINT `jadwals_kelas_id_foreign` FOREIGN KEY (`kelas_id`) REFERENCES `kelas` (`id`) ON DELETE CASCADE,
  ADD CONSTRAINT `jadwals_mapel_id_foreign` FOREIGN KEY (`mapel_id`) REFERENCES `mapels` (`id`) ON DELETE CASCADE,
  ADD CONSTRAINT `jadwals_tahun_ajaran_id_foreign` FOREIGN KEY (`tahun_ajaran_id`) REFERENCES `tahun_ajarans` (`id`) ON DELETE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
