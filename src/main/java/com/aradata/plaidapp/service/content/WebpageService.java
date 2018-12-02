package com.aradata.plaidapp.service.content;

import com.aradata.plaidapp.model.content.Content;
import com.aradata.plaidapp.model.content.Image;
import com.aradata.plaidapp.model.content.Type;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class WebpageService {

//	@Autowired
//	ContentService service;
//
//	public Content createContentFromUrl(String url) throws IOException {
//		Content content = new Content();
//
//		Image image = new Image();
//
//		Document doc = Jsoup.connect(url).get();
//		String imageUrl = null;
//		Elements metaOgImage = doc.select("meta[property=og:image]");
//		if (metaOgImage!=null) {
//			imageUrl = metaOgImage.attr("content");
//		}
//
//		image.setUrl(imageUrl);
//
//		content.addImage(image);
//		content.setType(Type.WEBPAGE);
//		content.setUrl(url);
//		content.setTitle(doc.title());
//		content.setText(doc.select("meta[name=description]").get(0)
//				.attr("content"));
//		content.setDescription("");
//		Content content1 = service.saveContent(content);
//
//		return content1;
//
//	}

}
