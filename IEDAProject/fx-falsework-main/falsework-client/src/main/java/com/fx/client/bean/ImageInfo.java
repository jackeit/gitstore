package com.fx.client.bean;

import lombok.Data;

/**
 * @className: ImageInfo
 * @author: liwen
 * @date: 2022/5/26 10:52
 */
@Data
public class ImageInfo {
    private String imageName;
    private String imagePath;

    public ImageInfo(String imageName, String imagePath) {
        this.imageName = imageName;
        this.imagePath = imagePath;
    }
}
