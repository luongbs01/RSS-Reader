package com.luonghm.rssreader.list;

import com.luonghm.rssreader.Newspaper_model;

import java.util.LinkedHashMap;

public class Vnexpress {
    private String name = "Báo Vnexpress";
    private String image = "https://s.vnecdn.net/vnexpress/i/v20/logos/vne_logo_rss.png";
    private LinkedHashMap<String, String> linkedHashMap = new LinkedHashMap<>();

    public Newspaper_model getAll() {
        linkedHashMap = new LinkedHashMap<>();
        linkedHashMap.put("Trang chủ", "https://vnexpress.net/rss/tin-moi-nhat.rss");
        linkedHashMap.put("Thời sự", "https://vnexpress.net/rss/thoi-su.rss");
        linkedHashMap.put("Thế giới", "https://vnexpress.net/rss/the-gioi.rss");
        linkedHashMap.put("Kinh doanh", "https://vnexpress.net/rss/kinh-doanh.rss");
        linkedHashMap.put("Startup", "https://vnexpress.net/rss/startup.rss");
        linkedHashMap.put("Giải trí", "https://vnexpress.net/rss/giai-tri.rss");
        linkedHashMap.put("Thể thao", "https://vnexpress.net/rss/the-thao.rss");
        linkedHashMap.put("Pháp luật", "https://vnexpress.net/rss/phap-luat.rss");
        linkedHashMap.put("Giáo dục", "https://vnexpress.net/rss/giao-duc.rss");
        linkedHashMap.put("Sức khỏe", "https://vnexpress.net/rss/suc-khoe.rss");
        linkedHashMap.put("Đời sống", "https://vnexpress.net/rss/doi-song.rss");
        linkedHashMap.put("Du lịch", "https://vnexpress.net/rss/du-lich.rss");
        linkedHashMap.put("Khoa học", "https://vnexpress.net/rss/khoa-hoc.rss");
        linkedHashMap.put("Số hóa", "https://vnexpress.net/rss/so-hoa.rss");
        linkedHashMap.put("Xe", "https://vnexpress.net/rss/oto-xe-may.rss");
        linkedHashMap.put("Ý kiến", "https://vnexpress.net/rss/y-kien.rss");
        linkedHashMap.put("Tâm sự", "https://vnexpress.net/rss/tam-su.rss");
        linkedHashMap.put("Cười", "https://vnexpress.net/rss/cuoi.rss");
        linkedHashMap.put("Tin xem nhiều", "https://vnexpress.net/rss/tin-xem-nhieu.rss");
        linkedHashMap.put("Tin nổi bật", "https://vnexpress.net/rss/tin-noi-bat.rss");
        return new Newspaper_model(name, image, linkedHashMap);
    }
}
