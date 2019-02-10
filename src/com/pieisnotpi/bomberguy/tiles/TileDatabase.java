package com.pieisnotpi.bomberguy.tiles;

import java.util.HashMap;
import java.util.Map;

public class TileDatabase
{
    private Map<Integer, TileEntry> tileEntryMap;

    public TileDatabase()
    {
        tileEntryMap = new HashMap<>();
    }

    public void addEntry(int entryValue, TileEntry entry)
    {
        tileEntryMap.put(entryValue, entry);
    }

    public TileEntry getEntry(int entryValue)
    {
        return tileEntryMap.get(entryValue);
    }
}
