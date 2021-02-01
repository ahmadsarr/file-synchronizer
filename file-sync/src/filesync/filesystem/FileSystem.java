package filesync.filesystem;
import filesync.action.BaseAction;

import java.io.File;
import java.nio.file.Path;
import java.util.List;

public interface FileSystem {
    public Node getRoot();
    public java.lang.String getBase();
    public Node getParent(java.lang.String path);

    public List<String> getChildren(String path);

    public List<String> getAncestors(String path);

    public java.lang.String getAbsolutePath(java.lang.String relativePath);

    public java.lang.String getRelativePath(java.lang.String absolutePath);

    public void replace(String absolutePathTargetFS, FileSystem Source, String absolutePathSourceFS);

    public FileSystem getReference();

    public File createDirectory(String path);
    public void remove(String absolutePathTargetFS,FileSystem src);
    public void rename(String absolutePathTargetFS, FileSystem Source, String absolutePathSourceFS);

    public void fileCopy(File input, File output) throws Exception;
    public List<BaseAction> computerDirty();
    public void createFile(String absolutePathTargetFS);
}
