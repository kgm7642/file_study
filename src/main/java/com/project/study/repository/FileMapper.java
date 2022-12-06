package com.project.study.repository;

import java.util.LinkedList;

import org.apache.ibatis.annotations.Mapper;

import com.project.study.domain.Item;

@Mapper
public interface FileMapper {

	public void saveFile(Item item);
	public Item findAttach(String boardNumber);
	public LinkedList<Item> findImages(String boardNumber);
	
}
