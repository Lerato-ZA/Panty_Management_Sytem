package com.example.pantrymanagementsystem.data.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.pantrymanagementsystem.data.DatabaseContract.PantryItemEntry;
import com.example.pantrymanagementsystem.data.DatabaseHelper;
import com.example.pantrymanagementsystem.data.entity.PantryItem;

import java.util.ArrayList;
import java.util.List;

// SQLite Operations
public class PantryItemDao {

    private final DatabaseHelper dbHelper;

    public PantryItemDao(Context context) {
        this.dbHelper = DatabaseHelper.getInstance(context);
    }

    // Create
    public long insert(PantryItem item) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        return db.insert(PantryItemEntry.TABLE_NAME, null, toContentValues(item));
    }

    // Read
    public PantryItem getById(long id) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selection = PantryItemEntry._ID + " = ?";
        String[] args = { String.valueOf(id) };
        try (Cursor cursor = db.query(PantryItemEntry.TABLE_NAME, null,
                selection, args, null, null, null)) {
            return cursor.moveToFirst() ? fromCursor(cursor) : null;
        }
    }

    public List<PantryItem> getAll() {
        List<PantryItem> items = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String orderBy = PantryItemEntry.COLUMN_NAME + " ASC";
        try (Cursor cursor = db.query(PantryItemEntry.TABLE_NAME, null,
                null, null, null, null, orderBy)) {
            while (cursor.moveToNext()) {
                items.add(fromCursor(cursor));
            }
        }
        return items;
    }

    public List<PantryItem> search(String query) {
        List<PantryItem> items = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selection = PantryItemEntry.COLUMN_NAME + " LIKE ?";
        String[] args = { "%" + query + "%" };
        String orderBy = PantryItemEntry.COLUMN_NAME + " ASC";
        try (Cursor cursor = db.query(PantryItemEntry.TABLE_NAME, null,
                selection, args, null, null, orderBy)) {
            while (cursor.moveToNext()) {
                items.add(fromCursor(cursor));
            }
        }
        return items;
    }

    public List<PantryItem> getExpiringBefore(long thresholdEpochMillis) {
        List<PantryItem> items = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selection = PantryItemEntry.COLUMN_EXPIRY_DATE + " IS NOT NULL AND " +
                PantryItemEntry.COLUMN_EXPIRY_DATE + " <= ?";
        String[] args = { String.valueOf(thresholdEpochMillis) };
        String orderBy = PantryItemEntry.COLUMN_EXPIRY_DATE + " ASC";
        try (Cursor cursor = db.query(PantryItemEntry.TABLE_NAME, null,
                selection, args, null, null, orderBy)) {
            while (cursor.moveToNext()) {
                items.add(fromCursor(cursor));
            }
        }
        return items;
    }

    //  Update
    public int update(PantryItem item) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String selection = PantryItemEntry._ID + " = ?";
        String[] args = { String.valueOf(item.getId()) };
        return db.update(PantryItemEntry.TABLE_NAME, toContentValues(item), selection, args);
    }

    //  Delete
    public int delete(long id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String selection = PantryItemEntry._ID + " = ?";
        String[] args = { String.valueOf(id) };
        return db.delete(PantryItemEntry.TABLE_NAME, selection, args);
    }

    //  Mapping helpers
    private ContentValues toContentValues(PantryItem item) {
        ContentValues values = new ContentValues();
        values.put(PantryItemEntry.COLUMN_NAME, item.getName());
        values.put(PantryItemEntry.COLUMN_QUANTITY, item.getQuantity());
        values.put(PantryItemEntry.COLUMN_UNIT, item.getUnit());
        values.put(PantryItemEntry.COLUMN_CATEGORY, item.getCategory());
        if (item.getExpiryDate() != null) {
            values.put(PantryItemEntry.COLUMN_EXPIRY_DATE, item.getExpiryDate());
        } else {
            values.putNull(PantryItemEntry.COLUMN_EXPIRY_DATE);
        }
        values.put(PantryItemEntry.COLUMN_DATE_ADDED, item.getDateAdded());
        return values;
    }

    private PantryItem fromCursor(Cursor cursor) {
        long id = cursor.getLong(cursor.getColumnIndexOrThrow(PantryItemEntry._ID));
        String name = cursor.getString(cursor.getColumnIndexOrThrow(PantryItemEntry.COLUMN_NAME));
        double quantity = cursor.getDouble(cursor.getColumnIndexOrThrow(PantryItemEntry.COLUMN_QUANTITY));
        String unit = cursor.getString(cursor.getColumnIndexOrThrow(PantryItemEntry.COLUMN_UNIT));
        String category = cursor.getString(cursor.getColumnIndexOrThrow(PantryItemEntry.COLUMN_CATEGORY));

        int expiryIndex = cursor.getColumnIndexOrThrow(PantryItemEntry.COLUMN_EXPIRY_DATE);
        Long expiryDate = cursor.isNull(expiryIndex) ? null : cursor.getLong(expiryIndex);

        long dateAdded = cursor.getLong(cursor.getColumnIndexOrThrow(PantryItemEntry.COLUMN_DATE_ADDED));

        PantryItem item = new PantryItem(name, quantity, unit, category, expiryDate, dateAdded);
        item.setId(id);
        return item;
    }
}