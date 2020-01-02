import com.alibaba.fastjson.JSON;
import com.ysma.ppt.PptApplication;
import com.ysma.ppt.es.EsLogDto;
import com.ysma.ppt.es.EsLogIndex;
import com.ysma.ppt.es.service.EsLogService;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.*;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.ParseException;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = PptApplication.class)// 指定启动类
public class EsTest {

	@Autowired
	private ElasticsearchTemplate elasticsearchTemplate;

	/*调试代码 不推荐直接使用
	@Autowired
	private EsAggRepository esAggRepository;*/

	@Autowired
	private EsLogService esLogService;

	@Autowired
	private EsLogIndex esLogIndex;

	@Test
    public void testEsIndex(){
        boolean exist = elasticsearchTemplate.indexExists(esLogIndex.getIndexName());
        if(exist){
            System.out.println("<============");
        } else {
            System.out.println("====================>");
        }
    }

	@Test
    public void testEsDel(){
		boolean exist = esLogService.indexReAdd(EsLogDto.class);
		Assert.assertTrue(exist);
    }

    @Test
	public void testEsTime(){
		Page<EsLogDto> ds = esLogService.queryByAddTime(
		        "2019-08-22 10:00:08",
                "2019-08-22 12:35:21",
                0,
                10);

		System.out.println(JSON.toJSON(ds.getContent()));

        /*调试代码，不推荐直接使用
        String begin = "2019-08-21 18:00:00";
        String end = "2019-08-21 21:00:00";
        List<EsLogDto> dto3 = esAggRepository.findByAddTimeBetween(begin, end);
        System.out.println("日期查===》"+ JSON.toJSONString(dto3));*/

        /*Iterable<EsLogDto> all = esLogService.queryAll();
        System.out.println("d=>"+JSON.toJSONString(all));*/

	}

    @Test
	public void testEsService() {
		//esLogService
		/*boolean exist = esLogService.indexExist(EsLogDto.class);
		Assert.assertTrue(exist);

		List<EsLogDto> list = new ArrayList<>();
		Date date = new Date();
		for(int i = 0; i<100; i++){
			EsLogDto dto = new EsLogDto();
			dto.setId("ysmaIds-"+i);
			dto.setApiCode("ysmaApiCodes-"+i);
			dto.setReq("mock requests-" + i);
			dto.setRes("mock resps-" + i);
			dto.setAddTime(date);
			list.add(dto);
		}

		esLogService.saveAll(list);*/

		System.out.println("=========清场===========");
		EsLogDto dtoId = esLogService.queryById("ysmaIds-" + 99);
		System.out.println("dtoId:["+JSON.toJSON(dtoId));
		System.out.println("=========清场===========");
		Page<EsLogDto> dtoApiCode = esLogService.queryByApiCode("ysmaApiCodes-" + 88, 0, 10);
		System.out.println("dtoApiCode:["+JSON.toJSON(dtoApiCode));
		System.out.println("=========清场===========");
		Page<EsLogDto> dtoAddTime =
				esLogService.queryByAddTime(
				        "2019-12-03 20:00:00",
                        "2019-12-31 22:00:00",
                        0,
                        10);
		System.out.println("dtoAddTime:["+JSON.toJSON(dtoAddTime));
	}

	@Test
	public void testAlias(){
		elasticsearchTemplate.addAlias(new AliasBuilder()
				.withAliasName("channel_agg_index-all")
				.withIndexName("channel_agg_index-2019-12-26")
				.build());

	}

	@Test
	public void testQueryPage(){
		System.out.println("=========清场===========");

		QueryBuilder queryBuilder = QueryBuilders
				.rangeQuery("addTime")
				.gte("2019-12-05")
				.lt("2019-12-27");
		NativeSearchQuery query3 = new NativeSearchQueryBuilder()
				.withTypes(EsLogIndex.INDEX_TYPE)
				.withIndices(EsLogIndex.INDEX_QUERY)
				.withQuery(queryBuilder)
				.withPageable(PageRequest.of(0, 10)).build();

		Page<EsLogDto> page = elasticsearchTemplate.queryForPage(query3, EsLogDto.class);

		System.out.println(JSON.toJSONString(page));
	}

	@Test
	public void testQueryObj(){
		System.out.println("=========清场===========");

		Criteria condit = new Criteria().or("_id").is("ysmaIds-99");
		CriteriaQuery query2 = new CriteriaQuery(condit);
		query2.addIndices(EsLogIndex.INDEX_QUERY);
		query2.addTypes(EsLogIndex.INDEX_TYPE);
		EsLogDto dto = elasticsearchTemplate.queryForObject(query2, EsLogDto.class);
		System.out.println(dto.toString());


	}

	@Test
	public void testQuery(){
		System.out.println("=========清场===========");

		/*GetResponse response = elasticsearchTemplate.getClient().prepareGet()
				.setIndex("channel_agg_index-all")
				.setType("channel_agg")
				.setId("ysmaId-99").execute().get();
		String string = response.getSourceAsString();
		System.out.println("===>"+string);*/

		SearchRequestBuilder requestBuilder = elasticsearchTemplate.getClient()
				.prepareSearch(EsLogIndex.INDEX_QUERY)
				.setTypes(EsLogIndex.INDEX_TYPE)
				.setSearchType(SearchType.QUERY_THEN_FETCH);


		/*QueryBuilder query = QueryBuilders.boolQuery()
				.should(QueryBuilders.termsQuery("apiCode", "ysmaApiCode-99"));*/
		QueryBuilder query = QueryBuilders.termsQuery("_id", "ysmaId-0");

		requestBuilder
//				.setIndices("channel_agg_index-*")
//				.setTypes("channel_agg")
				//.setSource(SearchSourceBuilder.)
				.setQuery(query);

		SearchResponse response = requestBuilder.get();

		long a = response.getHits().totalHits;
		System.out.println("====>"+ a);
		if(a >0){
			String aa = response.getHits().getAt(0).getSourceAsString();
			System.out.println(aa);
		}

	}

	@Test
	public void testEs() throws ParseException {
		boolean exist = elasticsearchTemplate.indexExists(EsLogDto.class);
		if(exist){
			//InitListener 创建
			System.out.println("======>已存在");

			/*EsLogDto dto = new EsLogDto();
			dto.setId("ysmaTest");
			dto.setApiCode("test");
			dto.setReq("mock request");
			dto.setRes("mock resp");
			dto.setAddTime(new Date());

			EsLogDto dto2 = esAggRepository.save(dto);
			System.out.println("====>存储结果："+dto2.toString());*/

            /*调试代码 不推荐直接使用
            String begin = "2019-08-21 18:00:00";
            String end = "2019-08-21 21:00:00";
            List<EsLogDto> dto3 = esAggRepository.findByAddTimeBetween(begin, end);
            System.out.println("日期查===》"+ JSON.toJSONString(dto3));*/

			/*调试代码 不推荐直接使用
			List<EsLogDto> esLogDtoList = esAggRepository.findByApiCodeStartsWith("test");
            System.out.println("查列表=="+ JSON.toJSONString(esLogDtoList));*/

		} else {
			elasticsearchTemplate.createIndex(EsLogDto.class);
			System.out.println("====>创建");
			exist = elasticsearchTemplate.indexExists(EsLogDto.class);
			System.out.println("===>结果：" + exist);
		}
	}
}
