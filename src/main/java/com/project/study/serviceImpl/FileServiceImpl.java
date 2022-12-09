package com.project.study.serviceImpl;

import java.util.LinkedList;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.study.domain.Item;
import com.project.study.domain.SaveAttach;
import com.project.study.domain.SaveImages;
import com.project.study.domain.UploadFile;
import com.project.study.repository.FileMapper;
import com.project.study.service.FileService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService{
	
	private final FileMapper mapper;
	
	@Override
	@Transactional
	public void saveFile(SaveAttach attach, SaveImages images) { 
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

	@Override
	@Transactional
	public Item findAttach(String boardNumber) {
		return mapper.findAttach(boardNumber);
	}
	
	@Override
	@Transactional
	public LinkedList<Item> findImages(String boardNumber) {
		return mapper.findImages(boardNumber);
	}
}
