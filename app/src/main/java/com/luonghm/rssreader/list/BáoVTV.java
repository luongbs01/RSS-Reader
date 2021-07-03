package com.luonghm.rssreader.list;

import com.luonghm.rssreader.Newspaper_model;

import java.util.LinkedHashMap;

public class BáoVTV {
    private String name = "Báo VTV";
    private String image = "https://images.app.goo.gl/P5nvut8RQMup3Ua79";
    private LinkedHashMap<String, String> linkedHashMap = new LinkedHashMap<>();

    public Newspaper_model getAll() {
        linkedHashMap = new LinkedHashMap<>();
        linkedHashMap.put("Trong nước", "https://vtv.vn/trong-nuoc.rss");
        linkedHashMap.put("Thế giới", "https://vtv.vn/the-gioi.rss");
        linkedHashMap.put("Kinh tế", "https://vtv.vn/kinh-te.rss");
        linkedHashMap.put("Truyền hình", "https://vtv.vn/truyen-hinh.rss");
        linkedHashMap.put("Phim - VTV", "https://vtv.vn/phim-vtv.rss");
        linkedHashMap.put("Văn hóa giải trí", "https://vtv.vn/van-hoa-giai-tri.rss");
        linkedHashMap.put("Thể thao", "https://vtv.vn/the-thao.rss");
        linkedHashMap.put("Giáo dục", "https://vtv.vn/giao-duc.rss");
        linkedHashMap.put("Công nghệ", "https://vtv.vn/cong-nghe.rss");
        linkedHashMap.put("Đời sống", "https://vtv.vn/doi-song.rss");
        linkedHashMap.put("Góc khán giả", "https://vtv.vn/goc-khan-gia.rss");
        linkedHashMap.put("Tấm lòng việt", "https://vtv.vn/tam-long-viet.rss");
        return new Newspaper_model(name, image, linkedHashMap);
    }
}
