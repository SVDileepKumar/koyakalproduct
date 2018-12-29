package com.koyakal.common.config;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;

public class JsonConfigReader {

	@SuppressWarnings("unchecked")
	public static Map<String, Object> readFile(String path) throws NoAppConfigException, URISyntaxException{

		if(path == null){
			throw new NoAppConfigException("Path is null");
		}
		
		Map<String,Object> resultMap = new HashMap<String, Object>();

		long codeStart = System.currentTimeMillis();
		
		File folder = new File(Thread.currentThread().getContextClassLoader().getResource(path).getFile());
		File[] listOfFiles = folder.listFiles();

		if(listOfFiles == null){
			throw new NoAppConfigException("Folder is empty or does not exist");
		}

		for (int i = 0; i < listOfFiles.length; i++) {
			System.out.println("Reading File: " + listOfFiles[i].getName());
			if (listOfFiles[i].isFile()) {
				Reader reader = null;
				try{
					if(listOfFiles[i].getName() != null && listOfFiles[i].getName().endsWith(".json")){
						reader = new FileReader(listOfFiles[i]);

						if(reader != null){
							Gson gson = new Gson();
							Map<String,Object> configKeys = gson.fromJson(reader, Map.class);

							if(configKeys != null && ! configKeys.isEmpty()){
								for(Map.Entry<String,Object> each : configKeys.entrySet()){
									if(!resultMap.containsKey(each.getKey())){
										resultMap.put(each.getKey(), each.getValue());
									}
								}

							}
						}	
					}
				}catch(Exception ex){
					throw new NoAppConfigException(ex.getMessage(), ex);	
				}finally{
					System.out.println("Time taken to retrieve config:  " + (System.currentTimeMillis() - codeStart) + " ms");
					if(reader != null){
						try {
							reader.close();
						} catch (IOException e) {
							throw new NoAppConfigException(e.getMessage());
						}	
					}
				}

			}
		}

		return resultMap;
	}

}
