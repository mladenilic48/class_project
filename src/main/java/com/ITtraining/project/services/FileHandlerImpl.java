package com.ITtraining.project.services;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileHandlerImpl implements FileHandler {

	// Save the uploaded file to this folder
	private static String UPLOADED_FOLDER = "D://Java//temp//";

	@Override
	public String singleFileUpload(Integer offerId, MultipartFile file) throws IOException {

		if (file.isEmpty()) {
			return null;
		}

		try {

			Path dir = Paths.get(UPLOADED_FOLDER + offerId.toString());
			
			if (!Files.exists(dir))
				Files.createDirectories(dir);

			// Get the file and save it somewhere
			byte[] bytes = file.getBytes();
			Path path = Paths.get(UPLOADED_FOLDER + offerId.toString(), file.getOriginalFilename());
			Files.write(path, bytes);

			return path.toString();

		} catch (IOException e) {
			throw e;
		}
	}
}
