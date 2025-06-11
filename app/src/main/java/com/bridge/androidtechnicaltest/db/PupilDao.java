package com.bridge.androidtechnicaltest.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import io.reactivex.Single;

@Dao
public interface PupilDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertPupil(Pupil pupil);

    @Query("SELECT * FROM pupils ORDER BY is_synced ASC, created_at DESC")
    Single<List<Pupil>> getPupils();



    @Query("SELECT * FROM pupils ORDER BY is_synced ASC, created_at DESC LIMIT :limit OFFSET :offset")
    Single<List<Pupil>> getPupilsPaginated(int limit, int offset);

    @Query("SELECT COUNT(*) FROM pupils")
    Single<Integer> getPupilCount();



    @Query("SELECT * FROM pupils WHERE pupil_id = :pupilId")
    Single<Pupil> getPupilById(int pupilId);

    @Delete
    void deletePupil(Pupil pupil);

    @Query("SELECT * FROM pupils WHERE is_synced = 0")
    Single<List<Pupil>> getUnsyncedPupils();

    @Query("SELECT COUNT(*) FROM pupils WHERE is_synced = 0")
    Single<Integer> getUnsyncedPupilCount();



    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Pupil> pupils);

    @Query("DELETE FROM pupils WHERE is_synced = 1")
    void clearSyncedPupils();



    @Query("DELETE FROM pupils WHERE is_synced = 0")
    void deleteUnsyncedPupils();


}