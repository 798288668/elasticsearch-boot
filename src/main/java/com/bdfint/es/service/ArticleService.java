package com.bdfint.es.service;

import com.bdfint.es.bean.Article;
import com.bdfint.es.common.Global;
import com.bdfint.es.common.Result;
import com.bdfint.es.dao.ArticleRepository;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

@Service
public class ArticleService {

    private static final Logger logger = LoggerFactory.getLogger(ArticleService.class);

    private final ArticleRepository articleRepository;
    private final ElasticsearchTemplate elasticsearchTemplate;

    @Autowired
    public ArticleService(ArticleRepository articleRepository, ElasticsearchTemplate elasticsearchTemplate) {
        this.articleRepository = articleRepository;
        this.elasticsearchTemplate = elasticsearchTemplate;
    }

    public Result initData() {
        articleRepository.deleteAll();
        List<Article> articleList = new ArrayList<>();
        articleList.add(new Article("1", "我们", "我还清晰地记得那天我们坐在江边聊天的情境", "关键词", 0L, new Date()));
        articleList.add(new Article("2", "聊天", "我记得我们聊天的情境", "关键词", 0L, new Date()));
        articleList.add(new Article("3", "我们聊天", "我们聊天的情境", "关键词", 0L, new Date()));
        articleList.add(new Article("4", "热腾腾的牛奶", "薇龙那天穿了一件磁青薄绸旗袍，给他那双绿眼睛一看，她觉得她的手臂像热腾腾的牛奶似的，从青色的壶里倒了出来，管也管不住，整个的自己全泼出来了", "关键词", 0L, new Date()));
        articleList.add(new Article("5", "铜香炉", "请您寻出家傅的霉绿斑斓的铜香炉，点上一炉沉香屑，听我说一支战前香港的故事，您这一炉沉香屑点完了，我的故事也该完了。", "关键词", 0L, new Date()));
        articleList.add(new Article("6", "玫瑰色的窗子", "现在，他前生所做的这个梦，向他缓缓走过来了，裹着银白的纱，云里雾里，向他走过来了。走过玫瑰色的窗子，她变了玫瑰色；走过蓝色的窗子，她变了蓝色；走过金黄色的窗子，她和她的头发燃烧起来了。", "关键词", 0L, new Date()));
        articleList.add(new Article("7", "茉莉香片", "她不是笼子里的鸟。笼子里的鸟，开了笼，还会飞出来。她是绣在屏风上的鸟——悒郁的紫色缎子屏风上，织金云朵里的一只白鸟。年深月久了，羽毛暗了，霉了，给虫蛀了，死也还死在屏风上。", "关键词", 0L, new Date()));
        articleList.add(new Article("8", "茉莉香片", "云和树一般被风嘘溜溜吹着，东边浓了，西边稀了，推推挤挤，一会儿黑压压拥成了一团，一会儿又化为一蓬绿气，散了开来。林子里的风，呜呜吼着，像捌犬的怒声。较远的还有海面上的风，因为远，就有点凄然，像哀哀的狗哭。", "关键词", 0L, new Date()));
        articleList.add(new Article("9", "金锁记", "她到了窗前，揭开了那边上缀有小绒球的墨绿洋式窗帘，季泽正在弄堂里望外走，长衫搭在臂上，晴天的风像一群白鸽子钻进他的纺绸褂里去，哪儿都钻到了，飘飘拍着翅子。", "关键词", 0L, new Date()));
        articleList.add(new Article("10", "窗外", "窗外还是那使人汗毛凛凛的反常的明月──漆黑的天上一个灼灼的小而白的太阳。屋里看得分明那玫瑰紫绣花椅披桌布，大红平金五凤齐飞的围屏，水红软缎对联，绣着盘花篆字。梳妆台上红绿丝网络着银粉缸、银漱盂、银花瓶，里面满满盛着喜，帐檐上垂下五彩攒金绕绒花球、花盆、如意、粽子，下面滴溜溜坠着指头大的琉璃珠和尺来长的桃红穗子。偌大一间房里充塞着箱笼、被褥、铺陈，不见得她就找不出一条汗巾子来上吊，她又倒到床上去。月光里，她脚没有一点血色──青、绿、紫、冷去的尸身的颜色。", "关键词", 0L, new Date()));
        articleList.add(new Article("11", "要遇见的人", "于千万人之中遇见你所要遇见的人，于千万年之中，时间的无涯的荒野里，没有早一步，也没有晚一步，刚巧遇上了，那也没有别的话可说，唯有轻轻地问一声：噢，你也在这里。", "关键词", 0L, new Date()));
        articleList.add(new Article("12", "欢喜", "见了他，她变得很低很低，低到尘埃里。但她心里是欢喜的，从尘埃里开出花来。", "关键词", 0L, new Date()));
        articleRepository.saveAll(articleList);
        return Result.of("init success!");
    }

    public Result save(Article article) throws IOException {
        article.setId(UUID.randomUUID().toString().replaceAll("-", ""));
        articleRepository.save(article);

        /*new XContentFactory();
        XContentBuilder builder = XContentFactory.jsonBuilder()
                .startObject()
                .startObject("article_index")
                .startObject("properties")
                .startObject("id").field("type", "string").field("store", "yes").endObject()
                .startObject("title").field("type", "string").field("store", "yes")
                .field("indexAnalyzer", "ik_max_word").field("searchAnalyzer", "ik").endObject()
                .startObject("content").field("type", "string").field("store", "yes")
                .field("indexAnalyzer", "ik_max_word").field("searchAnalyzer", "ik").endObject()
                .endObject()
                .endObject()
                .endObject();
        PutMappingRequest mapping = Requests.putMappingRequest("article_index").type("article").source(builder);
        elasticsearchTemplate.getClient().admin().indices().putMapping(mapping).actionGet();*/

        return Result.of("save success!");
    }


    public List<Article> search(Integer pageNo, Integer pageSize, String searchContent) {

        //方案一：
        SearchRequestBuilder builder = elasticsearchTemplate.getClient().prepareSearch(Global.INDEX_ARTICLE);
        builder.setTypes(Global.TYPE_ARTICLE);
        builder.setFrom(pageNo);
        builder.setSize(pageSize);
        //设置查询类型：1.SearchType.DFS_QUERY_THEN_FETCH 精确查询； 2.SearchType.SCAN 扫描查询,无序
        builder.setSearchType(SearchType.DFS_QUERY_THEN_FETCH);
        //设置查询关键词
        if (StringUtils.isNotEmpty(searchContent)) {
            builder.setQuery(QueryBuilders.multiMatchQuery(searchContent, "title", "content"));
        }
        //设置是否按查询匹配度排序
        builder.setExplain(true);
        //设置高亮显示
        HighlightBuilder highlightBuilder = new HighlightBuilder().field("*").requireFieldMatch(false);
        highlightBuilder.preTags("<span style=\"color:red\">");
        highlightBuilder.postTags("</span>");
        builder.highlighter(highlightBuilder);

        //执行搜索,返回搜索响应信息
        SearchResponse searchResponse = builder.get();
        SearchHits searchHits = searchResponse.getHits();

        SearchHit[] hits = searchHits.getHits();
        List<Article> list = Lists.newArrayList();
        Article article;
        for (SearchHit hit : hits) {
            Map<String, HighlightField> highlightFields = hit.getHighlightFields();
            //title高亮
            HighlightField titleField = highlightFields.get("title");
            Map<String, Object> source = hit.getSource();
            article = new Article();
            if (titleField != null) {
                Text[] fragments = titleField.fragments();
                StringBuilder title = new StringBuilder();
                for (Text text : fragments) {
                    title.append(text);
                }
                article.setTitle(title.toString());
            } else {
                article.setTitle(source.get("title").toString());
            }

            //content高亮
            HighlightField contentField = highlightFields.get("content");
            if (contentField != null) {
                Text[] fragments = contentField.fragments();
                StringBuilder content = new StringBuilder();
                for (Text text : fragments) {
                    content.append(text);
                }
                article.setContent(content.toString());
            } else {
                article.setContent(source.get("content").toString());
            }
            if (titleField != null || contentField != null) {
                list.add(article);
            }
        }


        // 方案二
        /*QueryBuilder builder = QueryBuilders.multiMatchQuery(searchContent, "title", "content");
        FunctionScoreQueryBuilder functionScoreQueryBuilder = QueryBuilders.functionScoreQuery(builder);
        Pageable pageable = PageRequest.of(pageNo, pageSize);

        List<String> heightFields = Lists.newArrayList("title", "content");
        HighlightBuilder.Field[] hfields = new HighlightBuilder.Field[heightFields.size()];
        for (int i = 0; i < heightFields.size(); i++) {
            hfields[i] = new HighlightBuilder.Field(heightFields.get(i)).preTags("<span style=\"color:red\">").postTags("</span>").fragmentSize(250);
        }
        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withPageable(pageable)
                .withQuery(functionScoreQueryBuilder)
                .withHighlightFields(hfields)
                .build();
        //Page<Article> result = articleRepository.search(searchQuery);
        Page<Article> result = elasticsearchTemplate.queryForPage(searchQuery, Article.class, new SearchResultMapper() {
            @Override
            public <T> AggregatedPage<T> mapResults(SearchResponse response, Class<T> clazz, Pageable pageable) {
                List<T> chunk = Lists.newArrayList();
                for (SearchHit searchHit : response.getHits()) {
                    if (response.getHits().getHits().length <= 0) {
                        return null;
                    }
                    Map<String, Object> entityMap = searchHit.getSource();
                    for (String highName : heightFields) {
                        HighlightField highlightField = searchHit.getHighlightFields().get(highName);
                        if (highlightField != null) {
                            String highValue = highlightField.fragments()[0].toString();
                            entityMap.put(highName, highValue);
                        }
                    }
                    chunk.add(BeanMapper.map(entityMap, clazz));
                }
                if (chunk.size() > 0) {
                    return new AggregatedPageImpl<>(chunk);
                }
                return null;
            }
        });*/

        return list;
    }


}
