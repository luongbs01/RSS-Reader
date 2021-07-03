package com.luonghm.rssreader.list;

import com.luonghm.rssreader.Newspaper_model;

import java.util.LinkedHashMap;

public class DoiSongPhapLuat {
    private String name = "Báo Đời Sống Và Pháp Luật";
    private String image = "https://images.app.goo.gl/P5nvut8RQMup3Ua79";
    private LinkedHashMap<String, String> linkedHashMap = new LinkedHashMap<>();

    public Newspaper_model getAll() {
        linkedHashMap = new LinkedHashMap<>();
        linkedHashMap.put("Trang chủ", "https://www.doisongphapluat.com/trang-chu.rss");
        linkedHashMap.put("Tin tức", "https://www.doisongphapluat.com/rss/tin-tuc.rss");
        linkedHashMap.put("Pháp luật", "https://www.doisongphapluat.com/rss/phap-luat.rss");
        linkedHashMap.put("Kinh doanh", "https://www.doisongphapluat.com/rss/kinh-doanh.rss");
        linkedHashMap.put("Đời sống", "https://www.doisongphapluat.com/rss/doi-song.rss");
        linkedHashMap.put("Giải trí", "https://www.doisongphapluat.com/rss/giai-tri.rss");
        linkedHashMap.put("Thể thao", "https://www.doisongphapluat.com/rss/the-thao.rss");
        linkedHashMap.put("Công nghệ", "https://www.doisongphapluat.com/rss/cong-nghe.rss");
        linkedHashMap.put("Ô tô - Xe máy", "https://www.doisongphapluat.com/rss/oto-xemay.rss");
        linkedHashMap.put("Cần biết", "https://www.doisongphapluat.com/rss/can-biet.rss");
        return new Newspaper_model(name, image, linkedHashMap);
    }
}
