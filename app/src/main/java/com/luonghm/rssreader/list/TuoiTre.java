package com.luonghm.rssreader.list;

import com.luonghm.rssreader.Newspaper_model;

import java.util.LinkedHashMap;

public class TuoiTre {
    private String name = "Báo Tuổi trẻ";
    private String image = "https://cdn1.tuoitre.vn/zoom/80_50/2020/4/27/logo-15879891740181583819041-crop-16193611735331161592263.jpg";
    private LinkedHashMap<String, String> linkedHashMap = new LinkedHashMap<>();

    public Newspaper_model getAll() {
        linkedHashMap.put("Trang chủ", "https://tuoitre.vn/rss/tin-moi-nhat.rss");
        linkedHashMap.put("Thời sự", "https://tuoitre.vn/rss/thoi-su.rss");
        linkedHashMap.put("Thế giới", "https://tuoitre.vn/rss/the-gioi.rss");
        linkedHashMap.put("Pháp luật", "https://tuoitre.vn/rss/phap-luat.rss");
        linkedHashMap.put("Kinh doanh", "https://tuoitre.vn/rss/kinh-doanh.rss");
        linkedHashMap.put("Công nghệ", "https://tuoitre.vn/rss/nhip-song-so.rss");
        linkedHashMap.put("Xe", "https://tuoitre.vn/rss/xe.rss");
        linkedHashMap.put("Nhip sống trẻ", "https://tuoitre.vn/rss/nhip-song-tre.rss");
        linkedHashMap.put("Văn hóa", "https://tuoitre.vn/rss/van-hoa.rss");
        linkedHashMap.put("Giải trí", "https://tuoitre.vn/rss/giai-tri.rss");
        linkedHashMap.put("Thể thao", "https://tuoitre.vn/rss/the-thao.rss");
        linkedHashMap.put("Giáo dục", "https://tuoitre.vn/rss/giao-duc.rss");
        linkedHashMap.put("Khoa học", "https://tuoitre.vn/rss/khoa-hoc.rss");
        linkedHashMap.put("Sức khỏe", "https://tuoitre.vn/rss/suc-khoe.rss");
        linkedHashMap.put("Giả thật", "https://tuoitre.vn/rss/gia-that.rss");
        linkedHashMap.put("Thư giãn", "https://tuoitre.vn/rss/thu-gian.rss");
        linkedHashMap.put("Du lịch", "https://tuoitre.vn/rss/du-lich.rss");
        return new Newspaper_model(name, image, linkedHashMap);
    }
}
