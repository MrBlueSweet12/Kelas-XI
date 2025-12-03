<?php

use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\Schema;

return new class extends Migration
{
    /**
     * Run the migrations.
     */
    public function up(): void
    {
        // Skip this migration as guru_asli_id and assigned_by columns
        // are already added in 2025_10_17_000001_update_teacher_attendances_add_diganti_status migration
        // This migration is kept for backward compatibility with existing databases
        if (Schema::hasColumn('teacher_attendances', 'guru_asli_id')) {
            return;
        }

        Schema::table('teacher_attendances', function (Blueprint $table) {
            $table->foreignId('guru_asli_id')->nullable()->constrained('users')->onDelete('set null');
            $table->foreignId('assigned_by')->nullable()->constrained('users')->onDelete('set null');
        });
    }

    /**
     * Reverse the migrations.
     */
    public function down(): void
    {
        Schema::table('teacher_attendances', function (Blueprint $table) {
            $table->dropForeign(['guru_asli_id']);
            $table->dropForeign(['assigned_by']);
            $table->dropColumn(['guru_asli_id', 'assigned_by']);
        });
    }
};
