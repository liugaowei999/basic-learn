package com.cttic.liugw.lucene.pojo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Product implements Iterator<Product.Item> {
    Map<String, Item> contentMap = new HashMap<>();
    List<Item> contenctList = new ArrayList<>();
    Item item;

    Iterator<Item> iterator = contenctList.iterator();

    public void reset() {
        iterator = contenctList.iterator();
    }

    @Override
    public boolean hasNext() {
        return iterator.hasNext();
    }

    @Override
    public Product.Item next() {

        return iterator.next();
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (Map.Entry<String, Item> entry : contentMap.entrySet()) {
            stringBuilder.append("ItemName:" + entry.getValue().getItemName())
                    .append(", ItemValue:" + entry.getValue().itemValue);
        }
        return stringBuilder.toString();
    }

    public void add(String fieldName, Object value) {
        Item item = new Item();
        item.setItemName(fieldName);
        item.setItemValue(value);
        contentMap.put(fieldName, item);
        contenctList.add(item);
    }

    public Object get(String fieldName) {
        return contentMap.get(fieldName).getItemValue();
    }

    public <T> T get(String fieldName, Class<T> cls) {
        return contentMap.get(fieldName).getItemValue(cls);
    }

    public class Item {
        private String itemName;
        private Object itemValue;

        public String getItemName() {
            return itemName;
        }

        public void setItemName(String itemName) {
            this.itemName = itemName;
        }

        public Object getItemValue() {
            return itemValue;
        }

        @SuppressWarnings("unchecked")
        public <T> T getItemValue(Class<T> cls) {
            if (cls.isInstance(itemValue)) {
                return (T) itemValue;
            }
            return null;
        }

        public void setItemValue(Object itemValue) {
            this.itemValue = itemValue;
        }

    }

}
