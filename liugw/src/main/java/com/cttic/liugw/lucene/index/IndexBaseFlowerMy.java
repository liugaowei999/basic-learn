package com.cttic.liugw.lucene.index;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.IntPoint;
import org.apache.lucene.document.StoredField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import com.cttic.liugw.lucene.analizer.ik.IKAnalyzer4Lucene7;
import com.cttic.liugw.lucene.pojo.Product;
import com.cttic.liugw.lucene.pojo.Product.Item;

public class IndexBaseFlowerMy {
    private static final List<Product> prodList = new ArrayList<>();
    // 模拟构建 产品数据
    static {

        // 第1个产品
        Product product = new Product();
        product.add("prodId", "p0001");
        product.add("name", "ThinkPad X1 Carbon 20KH0009CD/25CD 超极本轻薄笔记本电脑联想");
        product.add("simpleIntro", "ThinkPad X1 Carbon");
        product.add("price", 999);
        prodList.add(product);

        // 第2个产品
        product = new Product();
        product.add("prodId", "p0002");
        product.add("name", "华为P20手机 64GB 蓝色");
        product.add("simpleIntro", "P20");
        product.add("price", (float) 5999.8);
        prodList.add(product);
    }

    public static void genIndex() throws IOException {
        // 1. 创建使用的分词器
        Analyzer analyzer = new IKAnalyzer4Lucene7(true);

        // 2. 定制索引配置对象
        IndexWriterConfig config = new IndexWriterConfig(analyzer); // 设置索引使用的分词器
        config.setOpenMode(OpenMode.CREATE_OR_APPEND); // 设置索引库的打开模式：新建或追加

        // 3. 设置索引的存储位置
        //////3.1 存放在文件系统中
        Directory directory = FSDirectory.open(new File("d:/tmp/index").toPath());
        //////3.1 存放在内存中
        //Directory directory2 = new RAMDirectory();

        // 4. 创建写索引工具对象
        IndexWriter writer = new IndexWriter(directory, config);

        // 5. 为现有的业务数据生成索引
        for (Product product : prodList) {
            //System.out.println(product);

            // 创建document
            Document document = new Document();
            product.reset();
            Item item;
            while (product.hasNext()) {
                item = product.next();
                System.out.println(item.getItemName() + ":" + item.getItemValue());
                if (item.getItemValue() instanceof Integer) {
                    System.out.println("int value:" + item.getItemValue(Number.class));
                    document.add(new IntPoint(item.getItemName(), (int) item.getItemValue()));
                } else if (item.getItemValue() instanceof Float) {
                    System.out.println("float value:" + item.getItemValue(Number.class));
                    document.add(new StoredField(item.getItemName(), (float) item.getItemValue()));
                } else if (item.getItemValue() instanceof Double) {
                    System.out.println("double value:" + item.getItemValue(Number.class));
                    document.add(new StoredField(item.getItemName(), (double) item.getItemValue()));
                } else {
                    document.add(new StoredField(item.getItemName(), (String) item.getItemValue()));
                }
            }
            // 将文档添加到索引
            writer.addDocument(document);

        }
        // 刷新
        writer.flush();

        // 提交
        writer.commit();

        // 回归
        writer.rollback();

        // 关闭（自动提交)
        writer.close();
        directory.close();

    }

    public static void main(String[] args) throws IOException {
        for (int i = 0; i < 1; i++)
            genIndex();
    }
}
