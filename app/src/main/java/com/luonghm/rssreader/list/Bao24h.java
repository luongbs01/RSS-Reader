package com.luonghm.rssreader.list;

import com.luonghm.rssreader.Newspaper_model;

import java.util.LinkedHashMap;

public class Bao24h {
    private String name = "Báo 24h";
    private String image = "https://anh.24h.com.vn/upload/footer/2009-12-02/20091202162630_logo-chan-trang-24h.jpg";
    private LinkedHashMap<String, String> linkedHashMap = new LinkedHashMap<>();

    public Newspaper_model getAll() {
        linkedHashMap.put("Tin tức trong ngày", "https://cdn.24h.com.vn/upload/rss/tintuctrongngay.rss");
        linkedHashMap.put("Bóng đá", "https://cdn.24h.com.vn/upload/rss/bongda.rss");
        linkedHashMap.put("An ninh - Hình sự", "https://cdn.24h.com.vn/upload/rss/anninhhinhsu.rss");
        linkedHashMap.put("Thời trang", "https://cdn.24h.com.vn/upload/rss/thoitrang.rss");
        linkedHashMap.put("Thời trang Hi-tech", "https://cdn.24h.com.vn/upload/rss/thoitranghitech.rss");
        linkedHashMap.put("Tài chính - Bất động sản", "https://cdn.24h.com.vn/upload/rss/taichinhbatdongsan.rss");
        linkedHashMap.put("Ẩm thực", "https://cdn.24h.com.vn/upload/rss/amthuc.rss");
        linkedHashMap.put("Làm đẹp", "https://cdn.24h.com.vn/upload/rss/lamdep.rss");
        linkedHashMap.put("Phim", "https://cdn.24h.com.vn/upload/rss/phim.rss");
        linkedHashMap.put("Giáo dục - Du học", "https://cdn.24h.com.vn/upload/rss/giaoducduhoc.rss");
        linkedHashMap.put("Bạn trẻ - Cuộc sống", "https://cdn.24h.com.vn/upload/rss/bantrecuocsong.rss");
        linkedHashMap.put("Ca nhạc - MTV", "https://cdn.24h.com.vn/upload/rss/canhacmtv.rss");
        linkedHashMap.put("Thể thao", "https://cdn.24h.com.vn/upload/rss/thethao.rss");
        linkedHashMap.put("Phi thường - Kỳ quặc", "https://cdn.24h.com.vn/upload/rss/phithuongkyquac.rss");
        linkedHashMap.put("Công nghệ thông tin", "https://cdn.24h.com.vn/upload/rss/congnghethongtin.rss");
        linkedHashMap.put("Ô tô", "https://cdn.24h.com.vn/upload/rss/oto.rss");
        linkedHashMap.put("Thi trường - Tiêu dùng", "https://cdn.24h.com.vn/upload/rss/thitruongtieudung.rss");
        linkedHashMap.put("Du lịch", "https://cdn.24h.com.vn/upload/rss/dulich.rss");
        linkedHashMap.put("Sức khỏe - Đời sống", "https://cdn.24h.com.vn/upload/rss/suckhoedoisong.rss");
        linkedHashMap.put("Cười 24h", "https://cdn.24h.com.vn/upload/rss/cuoi24h.rss");
        return new Newspaper_model(name, image, linkedHashMap);
    }
}
