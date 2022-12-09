package com.project.study.service;

import java.util.LinkedList;

import com.project.study.domain.Item;
import com.project.study.domain.SaveAttach;
import com.project.study.domain.SaveImages;

public interface FileService {
	public void saveFile(SaveAttach attach, SaveImages images);
	public Item findAttach(String boardNumber);
	public LinkedList<Item> findImages(String boardNumber);
}
