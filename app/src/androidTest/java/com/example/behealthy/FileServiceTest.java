package com.example.behealthy;

import android.content.Context;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.example.behealthy.service.FileService;

import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

@RunWith(AndroidJUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class FileServiceTest {

    private static final String FILE_NAME = "recommended_daily_dose.txt";

    FileService fileService;

    @Before
    public void init(){
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        fileService = new FileService(context);
    }

    @Test
    public void test1_createFile(){
        this.fileService.createFile("FILE_NAME");
        String createdEmptyFile = this.fileService.loadFile("FILE_NAME");
        Assert.assertEquals("", createdEmptyFile);
    }

    @Test
    public void test2_saveFile(){
        this.fileService.saveFile("test", "FILE_NAME");
        String response = this.fileService.loadFile("FILE_NAME");
        Assert.assertEquals("test\n", response);
    }

    @Test
    public void test3_deleteFile(){
        this.fileService.deleteFile("FILE_NAME");
        String response = this.fileService.loadFile("FILE_NAME");
        Assert.assertEquals(null, response);
    }

}
