package com.cttic.liugw.ordinary.ADT.tree234;

/**
 * 保存实际数据的类
 * @author liugaowei
 *
 */
public class DataItem {
    public long dData; // 只有一个数据属性

    public DataItem(long dData) {
        this.dData = dData;
    }

    public void displayItem() {
        System.out.print("/" + dData);
    }

    public int compareTo(DataItem dataItem) {
        if (dData == dataItem.dData) {
            return 0;
        }

        return dData > dataItem.dData ? 1 : -1;
    }
}
