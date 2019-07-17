package com.github.pdf.tools.param;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author wangdongbo
 * @since 2019/7/12.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Page {

    /**
     * 宽
     */
    private Float width;

    /**
     * 高
     */
    private Float height;

    /**
     * 横向边距
     */
    private Float x;

    /**
     * 纵向边距
     */
    private Float y;

    public boolean check() {
        return width != null && height != null;
    }

    public boolean checkAll() {
        return check() && x != null && y != null;
    }

}
