package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [ViolationEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun violationDao(): ViolationDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "school_discipline_db"
                )
                .addCallback(DatabaseCallback(scope))
                .build()
                INSTANCE = instance
                instance
            }
        }

        private class DatabaseCallback(
            private val scope: CoroutineScope
        ) : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    scope.launch(Dispatchers.IO) {
                        populateInitialData(database.violationDao())
                    }
                }
            }

            override fun onOpen(db: SupportSQLiteDatabase) {
                super.onOpen(db)
                INSTANCE?.let { database ->
                    scope.launch(Dispatchers.IO) {
                        if (database.violationDao().getCount() == 0) {
                            populateInitialData(database.violationDao())
                        }
                    }
                }
            }

            private suspend fun populateInitialData(dao: ViolationDao) {
                val sampleViolations = listOf(
                    ViolationEntity(
                        studentName = "Rizky Ramadhan",
                        studentNis = "202410045",
                        studentClass = "XII MIPA 3",
                        category = "Pelanggaran Berat",
                        violationTitle = "Terlibat Perkelahian Fisik di Luar Gerbang Sekolah",
                        violationPoints = 75,
                        location = "Jl. Depan Gerbang Barat",
                        reporterName = "Agus Gunawan (Petugas Keamanan)",
                        incidentDate = "08 Mar 2025",
                        incidentTime = "14:15 WIB",
                        description = "Siswa kedapatan adu mulut yang berujung bentrok fisik dengan siswa sekolah lain usai jam kepulangan. Telah diamankan petugas dan dibawa ke pos keamanan.",
                        recommendedSanction = "Skorsing Akademik 5 Hari Kerja & Pemanggilan Orang Tua Tahap 3",
                        requiresApproval = true,
                        status = "PENDING",
                        principalNote = null,
                        principalDecisionDate = null,
                        timestamp = System.currentTimeMillis() - 3600000L * 2
                    ),
                    ViolationEntity(
                        studentName = "Bagas Arya Pratama",
                        studentNis = "202410112",
                        studentClass = "XI IPS 1",
                        category = "Etika & Perilaku",
                        violationTitle = "Membawa & Menggunakan Rokok Elektrik (Vape) di Toilet",
                        violationPoints = 40,
                        location = "Toilet Siswa Lantai 2 Sayap Timur",
                        reporterName = "Drs. Bambang Sudiro (Guru Piket)",
                        incidentDate = "08 Mar 2025",
                        incidentTime = "10:30 WIB",
                        description = "Terbukti menyimpan 1 unit vape dan liquid di saku jaket serta menggunakannya bersama 2 siswa lain saat jam istirahat pertama.",
                        recommendedSanction = "Penyitaan Barang Bukti, SP-2, dan Konseling Intensif BK 2 Pekan",
                        requiresApproval = true,
                        status = "PENDING",
                        principalNote = null,
                        principalDecisionDate = null,
                        timestamp = System.currentTimeMillis() - 3600000L * 5
                    ),
                    ViolationEntity(
                        studentName = "Dimas Aditya",
                        studentNis = "202410219",
                        studentClass = "X MIPA 1",
                        category = "Kehadiran (Bolos)",
                        violationTitle = "Meninggalkan Jam KBM Tanpa Izin Melompati Pagar Belakang",
                        violationPoints = 25,
                        location = "Area Pagar Belakang Lapangan Tenis",
                        reporterName = "Sri Wahyuni, S.Pd (Wali Kelas)",
                        incidentDate = "07 Mar 2025",
                        incidentTime = "11:45 WIB",
                        description = "Tidak berada di kelas selama jam pelajaran Matematika dan Kimia. Ditemukan warga sedang berada di warung sekitar sekolah dengan seragam lengkap.",
                        recommendedSanction = "Surat Peringatan 1 (SP-1) & Kerja Sosial Membersihkan Perpustakaan 3 Hari",
                        requiresApproval = true,
                        status = "PENDING",
                        principalNote = null,
                        principalDecisionDate = null,
                        timestamp = System.currentTimeMillis() - 3600000L * 24
                    ),
                    ViolationEntity(
                        studentName = "Ananda Putri Lestari",
                        studentNis = "202410088",
                        studentClass = "XI MIPA 2",
                        category = "Kedisiplinan & Keterlambatan",
                        violationTitle = "Terlambat Hadir Lebih dari 30 Menit (Akumulasi 4 Kali)",
                        violationPoints = 20,
                        location = "Gerbang Utama Sekolah",
                        reporterName = "Budi Santoso, S.Pd (Tim Tatib)",
                        incidentDate = "06 Mar 2025",
                        incidentTime = "07:35 WIB",
                        description = "Siswa terlambat 4 hari dalam periode dua pekan. Diberikan pembinaan kedisiplinan dan edukasi manajemen waktu.",
                        recommendedSanction = "Panggilan Orang Tua & Tugas Literasi Kedisiplinan",
                        requiresApproval = true,
                        status = "APPROVED",
                        principalNote = "Disetujui. Lakukan pembinaan persuasif bersama orang tua agar siswa memperbaiki rutinitas pagi.",
                        principalDecisionDate = "06 Mar 2025 13:00 WIB",
                        timestamp = System.currentTimeMillis() - 3600000L * 48
                    ),
                    ViolationEntity(
                        studentName = "Fajar Nugraha",
                        studentNis = "202410190",
                        studentClass = "XII IPS 2",
                        category = "Atribut & Seragam",
                        violationTitle = "Seragam Tidak Sesuai Aturan & Mengenakan Sepatu Bebas",
                        violationPoints = 10,
                        location = "Ruang Kelas XII IPS 2",
                        reporterName = "Hendra Setiawan, S.Pd (Guru BK)",
                        incidentDate = "05 Mar 2025",
                        incidentTime = "07:05 WIB",
                        description = "Mengenakan celana pensil dan sepatu warna-warni tanpa surat keterangan dokter/izin khusus.",
                        recommendedSanction = "Peringatan Lisan & Penggantian Sepatu Standar",
                        requiresApproval = false,
                        status = "APPROVED",
                        principalNote = "Disetujui oleh Tim Tatib",
                        principalDecisionDate = "05 Mar 2025 09:00 WIB",
                        timestamp = System.currentTimeMillis() - 3600000L * 72
                    ),
                    ViolationEntity(
                        studentName = "Kevin Maulana",
                        studentNis = "202410304",
                        studentClass = "X IPS 3",
                        category = "Etika & Perilaku",
                        violationTitle = "Penggunaan Gadget Saat Ujian Tengah Semester",
                        violationPoints = 30,
                        location = "Ruang Ujian 04",
                        reporterName = "Dra. Siti Rahayu (Pengawas)",
                        incidentDate = "04 Mar 2025",
                        incidentTime = "09:15 WIB",
                        description = "Kedapatan membuka browser saat ujian berlangsung. Lembar jawaban disita sementara.",
                        recommendedSanction = "Pembatalan Nilai Ujian Sesi & Ujian Susulan dengan Pengawasan Khusus",
                        requiresApproval = true,
                        status = "REJECTED",
                        principalNote = "Sanksi pembatalan nilai disetujui, namun mohon diutamakan asesmen diagnostik penyebab kendala belajar sebelum sanksi diterapkan.",
                        principalDecisionDate = "04 Mar 2025 15:30 WIB",
                        timestamp = System.currentTimeMillis() - 3600000L * 96
                    )
                )
                dao.insertViolations(sampleViolations)
            }
        }
    }
}
