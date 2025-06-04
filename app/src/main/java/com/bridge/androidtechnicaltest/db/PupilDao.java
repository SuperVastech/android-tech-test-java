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

    @Insert
    void insertPupil(Pupil pupil);

    @Query("SELECT * FROM pupils ORDER BY name ASC")
    Single<List<Pupil>> getPupils();

    @Query("SELECT * FROM pupils WHERE pupil_id = :pupilId")
    Single<Pupil> getPupilById(int pupilId);

    @Delete
    void deletePupil(Pupil pupil);

    @Query("SELECT * FROM pupils WHERE is_synced = 0")
    Single<List<Pupil>> getUnsyncedPupils();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Pupil> pupils);

    // Option 1: Return void (simplest fix)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Pupil pupil);

    @Delete
    void delete(Pupil pupil);
}