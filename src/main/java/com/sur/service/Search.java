package com.sur.service;

import java.util.ArrayList;

import org.apache.lucene.document.Document;

public interface Search {

	public ArrayList<Document> search(String log);

}
