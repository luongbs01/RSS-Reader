package com.luonghm.rssreader;

import com.luonghm.rssreader.list.Bao24h;
import com.luonghm.rssreader.list.BáoVTV;
import com.luonghm.rssreader.list.DoiSongPhapLuat;
import com.luonghm.rssreader.list.ThanhNien;
import com.luonghm.rssreader.list.TuoiTre;
import com.luonghm.rssreader.list.Vnexpress;

import java.util.ArrayList;
import java.util.List;

public class NewsPaperList {
    List<Newspaper_model> list = new ArrayList<>();

    public NewsPaperList() {
        list.add(new Bao24h().getAll());
        list.add(new Vnexpress().getAll());
        //list.add(new ThanhNien().getAll());
        list.add(new TuoiTre().getAll());
        list.add(new BáoVTV().getAll());
        //list.add(new DoiSongPhapLuat().getAll());
    }

    public List<Newspaper_model> getList() {
        return list;
    }

    public Newspaper_model getItem(int position) {
        return list.get(position);
    }
}
