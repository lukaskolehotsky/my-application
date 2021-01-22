package svk.health.behealthy;

import android.content.Context;

import svk.health.behealthy.service.FileService;

import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(MockitoJUnitRunner.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class FileServiceTest {

    private static final String FILE_NAME = "recommended_daily_dose.txt";

    @InjectMocks
    private FileService fileService;

    @Mock
    private Context context;

    @Mock
    private FileOutputStream fileOutputStream;

    @Rule
    public TemporaryFolder mTempFolder = new TemporaryFolder();

    @Before
    public void setUp() throws IOException {
        initMocks(this);
        when(context.getFilesDir()).thenReturn(mTempFolder.newFolder());
        when(context.openFileInput(FILE_NAME)).thenReturn(new FileInputStream(FILE_NAME));
        when(context.deleteFile(FILE_NAME)).thenReturn(true);
        when(context.openFileOutput(FILE_NAME, context.MODE_PRIVATE)).thenReturn(new FileOutputStream(FILE_NAME));

    }

    @Test
    public void test1_createFile() {
        this.fileService.createFile(FILE_NAME);

        String createdEmptyFile = this.fileService.loadFile(FILE_NAME);
        Assert.assertEquals("", createdEmptyFile);
    }

    @Test
    public void test2_saveFile() throws IOException {
        doNothing().when(fileOutputStream).write(new String("test").getBytes());

        this.fileService.saveFile("test", FILE_NAME);

        String response = this.fileService.loadFile(FILE_NAME);

        Assert.assertEquals("test\n", response);
    }


    @Test
    public void test3_deleteFile() {
        this.fileService.deleteFile(FILE_NAME);

        String response = this.fileService.loadFile(FILE_NAME);
        Assert.assertEquals("", response);
    }

}
