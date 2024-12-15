package com.zhi.pin.utils;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicSessionCredentials;
import com.qcloud.cos.exception.CosClientException;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.model.PutObjectResult;
import com.qcloud.cos.region.Region;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;

@Component
public class CosUtils {
    
    @Value("${cos.secret-id}")
    private String secretId;
    
    @Value("${cos.secret-key}")
    private String secretKey;
    
    @Value("${cos.session-token}")
    private String sessionToken;
    
    @Value("${cos.bucket-name}")
    private String bucketName;
    
    @Value("${cos.region}")
    private String regionName;
    
    private static COSClient cosClient;
    
    @PostConstruct
    public void init() {
        BasicSessionCredentials cred = new BasicSessionCredentials(secretId, secretKey, sessionToken);
        Region region = new Region(regionName);
        ClientConfig clientConfig = new ClientConfig(region);
        cosClient = new COSClient(cred, clientConfig);
    }

    public String uploadFile(File file, String fileName) throws CosClientException, IOException {
        FileInputStream inputStream = new FileInputStream(file);
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, fileName, inputStream, null);
        
        PutObjectResult putObjectResult = cosClient.putObject(putObjectRequest);
        URL objectUrl = cosClient.getObjectUrl(bucketName, fileName);
        
        inputStream.close();
        return objectUrl.toString();
    }

    public boolean deleteFile(String fileName) {
        try {
            cosClient.deleteObject(bucketName, fileName);
            return true;
        } catch (CosClientException cce) {
            cce.printStackTrace();
            return false;
        }
    }
}
