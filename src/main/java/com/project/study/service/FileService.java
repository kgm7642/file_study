package com.project.study.service;

import java.util.LinkedList;

import org.springframework.stereotype.Service;

import com.project.study.domain.Item;
import com.project.study.domain.SaveAttach;
import com.project.study.domain.SaveImages;
import com.project.study.domain.UploadFile;
import com.project.study.repository.FileMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FileService {
	
	private final FileMapper mapper;
	
	public void saveFile(SaveAttach attach, SaveImages images) {
		
		// 
		Item item = new Item();
		
		// 첨부파일 저장
		item.setBoardNumber(attach.getBoardNumber());
		item.setUploadFileName(attach.getAttachFile().getUploadFileName());
		item.setStoreFileName(attach.getAttachFile().getStoreFileName());
		item.setAttach(attach.getAttach());
		mapper.saveFile(item);
		
		// 이미지파일들 저장
		for(UploadFile u : images.getImageFiles()) {
			item.setBoardNumber(images.getBoardNumber());
			item.setUploadFileName(u.getUploadFileName());
			item.setStoreFileName(u.getStoreFileName());
			item.setAttach(images.getAttach());
			mapper.saveFile(item);
		}
	}
	
	public Item findAttach(String boardNumber) {
		return mapper.findAttach(boardNumber);
	}
	
	public LinkedList<Item> findImages(String boardNumber) {
		return mapper.findImages(boardNumber);
	}
	
}
