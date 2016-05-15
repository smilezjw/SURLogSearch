package com.sur.test;

import java.io.IOException;
import java.nio.file.Paths;

import javax.annotation.Resource;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.FuzzyQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.WildcardQuery;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring-mongodb.xml")
public class TestMongodb {

	@Resource
	MongoTemplate mongoTemplate;
	
	@Test
	public void test(){
//		DB db = mongoTemplate.getDb();
//		System.out.println("db is " + db);
//		DBCollection collection = mongoTemplate.getCollection("ask_openstack");
//		DBCursor cursor = collection.find();
		
		StandardAnalyzer luceneAnalyzer = new StandardAnalyzer();
		
		IndexWriterConfig indexWriterConfig = new IndexWriterConfig(luceneAnalyzer);
//		indexWriterConfig.setOpenMode(OpenMode.CREATE_OR_APPEND);
		indexWriterConfig.setOpenMode(OpenMode.CREATE);
		Directory directory = null;
//		IndexWriter indexWriter = null;
		try {
			//索引在硬盘上的存储路径
			directory = FSDirectory.open(Paths.get("C:\\Users\\dell\\Documents\\EclipseWorkspace\\logsearch\\LuceneIndex\\index"));
			//indexWriter用来创建索引文件
//			indexWriter = new IndexWriter(directory, indexWriterConfig);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
//		while(cursor.hasNext()){
//			DBObject object = cursor.next();
//			System.out.println(object);
//			Document doc = new Document();
//			doc.add(new StringField("description", (String) object.get("description"),Field.Store.YES));
//			doc.add(new StringField("title", (String) object.get("title"), Field.Store.YES));
//			doc.add(new StringField("answer", (String) object.get("answer"), Field.Store.YES));
//			try {
//				indexWriter.addDocument(doc);
//				indexWriter.commit();
//				indexWriter.close();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		}
		
		
		try {
			//读取索引
			DirectoryReader directoryReader = DirectoryReader.open(directory);
			//创建索引检索对象
			IndexSearcher searcher = new IndexSearcher(directoryReader);
			//创建Query，查询description包含compute node
			String[] fields = {"description", "title"};
			QueryParser queryParser = new MultiFieldQueryParser(fields, luceneAnalyzer);
			queryParser.setAllowLeadingWildcard(true);
			Query query = queryParser.parse("*openstack*");
			//检索索引，获取符合条件的前3条记录
			TopDocs topDocs = searcher.search(query, 3);
			if(topDocs != null){
				System.out.println("Total: " + topDocs.totalHits);
				for(int i=0; i < topDocs.scoreDocs.length; i++){
					Document doc = searcher.doc(topDocs.scoreDocs[i].doc);
					System.out.println("description: " + doc.get("description"));
					System.out.println("title: " + doc.get("title"));
					System.out.println("answer: " + doc.get("answer"));
				}
	
			}else{
				System.out.println("Sorry, there is no related solutions.");
			}
			
			directory.close();
			directoryReader.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
}
