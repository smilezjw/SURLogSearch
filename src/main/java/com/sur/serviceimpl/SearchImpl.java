package com.sur.serviceimpl;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.regex.Pattern;

import javax.annotation.Resource;
import javax.mail.internet.HeaderTokenizer.Token;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.core.StopFilter;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import org.springframework.test.context.ContextConfiguration;

import com.sur.constants.Constants;
import com.sur.service.Search;

@ContextConfiguration("classpath:spring-mongodb.xml")
@Service("searchService")
public class SearchImpl implements Search {

	@Resource
	private MongoTemplate mongoTemplate;

	public ArrayList<Document> hitDocs;

	public String buildQuery(String log) {
		
		String queryStr = "";
		StandardAnalyzer analyzer = new StandardAnalyzer();
		try {
			TokenStream stream = analyzer.tokenStream("", new StringReader(log));
			CharTermAttribute cta = stream.addAttribute(CharTermAttribute.class);
			stream.reset();
			while(stream.incrementToken()){
				if(queryStr.length() == 0){
					queryStr += "*" + cta + "*";
				}else{
					queryStr += " OR " + "*" + cta + "*";
				}
			}
			stream.end();
			stream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return queryStr;
	}

	@Override
	public ArrayList<Document> search(String log) {
		hitDocs = new ArrayList<Document>();
		StandardAnalyzer luceneAnalyzer = new StandardAnalyzer();

		try {
			// 索引在硬盘上的存储路径
			Directory directory = FSDirectory.open(Paths.get(Constants.INDEX_DIRECTORY));
			// 读取索引
			DirectoryReader directoryReader = DirectoryReader.open(directory);
			// 创建索引检索对象
			IndexSearcher searcher = new IndexSearcher(directoryReader);
			// 创建Query，查询description包含compute node
			String[] fields = { "description", "title" };
			QueryParser queryParser = new MultiFieldQueryParser(fields, luceneAnalyzer);
			queryParser.setAllowLeadingWildcard(true);
			String queryStr = buildQuery(log);
			System.out.println("queryStr = " + queryStr);
			if(queryStr.length() == 0){
				return null;
			}
			Query query = queryParser.parse(queryStr);
			// 检索索引，获取符合条件的前HITDOCS_NUM条记录
			TopDocs topDocs = searcher.search(query, Constants.HITDOCS_NUM);
			if (topDocs != null) {
				System.out.println("Total: " + topDocs.totalHits);
				for (int i = 0; i < topDocs.scoreDocs.length; i++) {
					Document doc = searcher.doc(topDocs.scoreDocs[i].doc);
					hitDocs.add(doc);
				}

			}
			directory.close();
			directoryReader.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return hitDocs;
	}

}
