package filesync;
import filesync.Node;
import java.io.File;
import java.util.List;

public interface FileSystem {
    public Node getRoot();
    public Node getParent(String path);

    public List<Node> getChildren(String path);

    public List<Node> getAncestors(String path);

    public String getAbsolutePath(String relativePath);

    public String getRelativePath(String absolutePath);

    //public void replace(String absolutePathTargetFS, FileSystemfs Source, String absolutePathSourceFS);

    public FileSystem getReference();

    public File createDirectory(String path);

    public void fileCopy(File input, File output) throws Exception;
}
