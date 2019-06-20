package com.example.siditoure.androidtranscription;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface User_dao {
    @Insert(onConflict=OnConflictStrategy.IGNORE)
    public void adduser(User user);
    @Query("select * from users where login = :login")
    public List<User> getUser(String login);
    @Query("select * from users")
    public List<User> getUsers();
    @Delete
    public void deletuser(User user);
    @Update
    public void updatuser(User user);
}
