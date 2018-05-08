package com.cttic.liugw.lucene.analizer.ik;

import org.apache.lucene.analysis.Analyzer;

/**
 * IK分词器，Lucene Analyzer接口实现 兼容Lucene 7.3.0版本
 */
public class IKAnalyzer4Lucene7 extends Analyzer {
    // 
    private boolean useSmart;

    /**
     * IK分词器Lucene Analyzer接口实现类
     *
     * 构造函数： 默认细粒度切分算法
     */
    public IKAnalyzer4Lucene7() {
        this(false);
    }

    /**
     * IK分词器Lucene Analyzer接口实现类
     * 
     * 有参构造函数
     *
     * @param useSmart
     *            当为true时，分词器进行智能切分
     */
    public IKAnalyzer4Lucene7(boolean useSmart) {
        super();
        this.useSmart = useSmart;
    }

    public boolean isUseSmart() {
        return useSmart;
    }

    public void setUseSmart(boolean useSmart) {
        this.useSmart = useSmart;
    }

    /**
     * 重载Analyzer接口，构造分词组件
     */
    @Override
    protected TokenStreamComponents createComponents(String fieldName) {
        IKTokenizer4Lucene7 iKTokenizer4Lucene7 = new IKTokenizer4Lucene7(useSmart);
        return new TokenStreamComponents(iKTokenizer4Lucene7);
    }

}
