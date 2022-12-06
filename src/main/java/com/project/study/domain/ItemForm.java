package com.project.study.domain;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

@Data
public class ItemForm {
	private String fileNumber;
	private String boardNumber;
	private MultipartFile attachFile;
	private List<MultipartFile> imageFiles;
}