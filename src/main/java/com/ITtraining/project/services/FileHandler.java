package com.ITtraining.project.services;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

public interface FileHandler {

	public String singleFileUpload(Integer offerId, MultipartFile file) throws IOException;
}
