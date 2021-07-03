package com.luonghm.rssreader.list;

import com.luonghm.rssreader.Newspaper_model;

import java.util.LinkedHashMap;

public class ThanhNien {
    private String name = "Báo Thanh Niên";
    private String image = "https://static.thanhnien.vn/v2/App_Themes/images/logo-tn-2.png";
    private LinkedHashMap<String, String> linkedHashMap = new LinkedHashMap<>();

    public Newspaper_model getAll() {
        linkedHashMap.put("Trang chủ", "https://thanhnien.vn/rss/home.rss");
        linkedHashMap.put("Thời sự", "https://thanhnien.vn/rss/thoi-su.rss");
        linkedHashMap.put("Thế giới", "https://thanhnien.vn/rss/the-gioi.rss");
        linkedHashMap.put("Văn hóa", "https://thanhnien.vn/rss/van-hoa.rss");
        linkedHashMap.put("Giải trí", "https://thanhnien.vn/rss/giai-tri.rss");
        linkedHashMap.put("Thể thao", "https://thethao.thanhnien.vn/rss/home.rss");
        linkedHashMap.put("Đời sống", "https://thanhnien.vn/rss/doi-song.rss");
        linkedHashMap.put("Tài chính - Kinh doanh", "https://thanhnien.vn/rss/tai-chinh-kinh-doanh.rss");
        linkedHashMap.put("Giới trẻ", "https://thanhnien.vn/rss/gioi-tre.rss");
        linkedHashMap.put("Giáo dục", "https://thanhnien.vn/rss/giao-duc.rss");
        linkedHashMap.put("Công nghệ", "https://thanhnien.vn/rss/cong-nghe.rss");
        linkedHashMap.put("Game", "https://game.thanhnien.vn/rss/home.rss");
        linkedHashMap.put("Du lịch", "https://thanhnien.vn/rss/du-lich.rss");
        linkedHashMap.put("Xe", "https://xe.thanhnien.vn/rss/home.rss");
        linkedHashMap.put("Thời trang trẻ", "https://thanhnien.vn/rss/ttt.rss");
        return new Newspaper_model(name, image, linkedHashMap);
    }
}
