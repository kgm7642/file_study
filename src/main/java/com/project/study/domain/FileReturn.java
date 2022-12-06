package com.project.study.domain;

import java.util.LinkedList;

import lombok.Data;

@Data
public class FileReturn {
	
	private Item item;
	private LinkedList<Item> items;
	
}
