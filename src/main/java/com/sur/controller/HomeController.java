package com.sur.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.lucene.document.Document;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.sur.service.LogProcess;
import com.sur.service.Search;

@Controller
public class HomeController {
	
	@Resource(name="searchService")
	private Search searchService;

	@Resource(name = "logProcessService")
	private LogProcess logProcessService;

	@RequestMapping("/index")
	public String index(HttpServletRequest request, HttpServletResponse response) {
		return "index";
	}

	@RequestMapping("/uploadlog")
	public ModelAndView fileUpload(@RequestParam("file") CommonsMultipartFile file, HttpServletRequest request,
			ModelMap model) {
		
		//preprocess logs
		HashSet<String> logErrors = logProcessService.preprocess(file);
		HashMap<String, ArrayList<Document>> searchResult = new HashMap<>();
		
		//search logs in Lucene
		for(String error:logErrors){
			System.out.println("error: " + error);
			ArrayList<Document> hitDocs = searchService.search(error);
			if(hitDocs != null && hitDocs.size() > 0){
				System.out.println("~~~~~~~~~~~~~~~~~ " + hitDocs.size());
				searchResult.put(error, hitDocs);
//				for(Document doc : hitDocs){
//					System.out.println("description: " + doc.get("description"));
//					System.out.println("title: " + doc.get("title"));
//					System.out.println("answer: " + doc.get("answer"));
//					System.out.println("----------------------------------------------");
//				}
			}else{
				System.out.println("There is no related results...");
			}
		}
		ModelAndView mv = new ModelAndView();
		mv.addObject("searchResult", searchResult);
		mv.addObject("filename", file.getOriginalFilename());
		mv.setViewName("showResults");
		return mv;
	}

}
