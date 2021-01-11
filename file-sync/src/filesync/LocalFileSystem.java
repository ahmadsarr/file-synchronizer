package filesync;

import java.io.File;
import java.util.List;

public class LocalFileSystem implements FileSystem{

    @Override
    public String getRoot() {
        return null;
    }

    @Override
    public String getParent(String path) {
        return null;
    }

    @Override
    public List<String> getChildren(String path) {
        return null;
    }

    @Override
    public List<String> getAncestors(String path) {
        return null;
    }

    @Override
    public String getAbsolutePath(String relativePath) {
        return null;
    }

    @Override
    public String getRelativePath(String absolutePath) {
        return null;
    }

    @Override
    public FileSystem getReference() {
        return null;
    }

    @Override
    public File createDirectory(String path) {
        return null;
    }

    @Override
    public void fileCopy(File input, File output) throws Exception {

    }
}
