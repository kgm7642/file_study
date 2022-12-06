package com.project.study.domain;

import java.util.List;

import lombok.Data;

@Data
public class SaveImages {

	private String boardNumber;
	private List<UploadFile> imageFiles;
	private String Attach;
}
