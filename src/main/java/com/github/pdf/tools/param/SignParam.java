package com.github.pdf.tools.param;

import lombok.Builder;
import lombok.Data;

/**
 * @author wangdongbo
 * @since 2019/7/13.
 */
@Data
@Builder
public class SignParam {

    /**
     * 是否签章
     */
    @Builder.Default
    private Boolean sign = Boolean.FALSE;

    /**
     * X坐标
     */
    private Float posX;

    /**
     * Y坐标
     */
    private Float posY;

    /**
     * 按关键字位置偏移[暂时不支持]
     */
    private String keyWord;

    /**
     * 签章图片地址
     */
    private String picturePath;

    /**
     * 图片显示宽
     */
    @Builder.Default
    private Integer picWidth = 150;

    /**
     * 图片显示高
     */
    @Builder.Default
    private Integer picHeight = 150;

    /**
     * 页码
     */
    @Builder.Default
    private Integer pageNo = 1;

    public void checkPos() {
        if (posX == null) {
            posX = 0F;
        }
        if (posY == null) {
            posY = 0F;
        }
    }
}
