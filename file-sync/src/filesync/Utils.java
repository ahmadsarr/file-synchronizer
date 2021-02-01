package filesync;

import filesync.action.BaseAction;
import filesync.action.RenameAction;
import filesync.filesystem.*;
import filesync.filesystem.Node;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;

public final class Utils {
    public final static void walk(Node root){
        Stack<Node> dirs=new Stack<>();
        dirs.push(root);
        Node current=null;
        while (!dirs.isEmpty())
        {
            current=dirs.pop();

            try {
                List<Path> paths= Files.walk(current.getPath(),1).collect(Collectors.toList());
                Path  p=null;
                for(int i=1;i<paths.toArray().length;i++){
                    p=paths.get(i);
                    Node node=null;
                    if(Files.isDirectory(p)){
                        node=new Dir(current,p.toString(), root.getBase());
                        dirs.push(node);
                    }else{
                        node=new File(current,p.toString(), root.getBase());
                    }
                    current.addchild(node);

                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
     //   System.out.println(root.getChildren().size());
    }
    public final static List<Path> walk(Path path){

            try {
             return Files.walk(path).collect(Collectors.toList());

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        //   System.out.println(root.getChildren().size());

    }

    public static FileSystem clone(FileSystem localfs, java.lang.String base, java.lang.String root)
    {
        java.lang.String basePath=base+root+"/"+localfs.getRoot().getPath().getFileName().toString();

        LocalFileSystem  remotefs=null;//=new LocalFileSystem(basePath,"/"+localfs.getRoot().getPath().getFileName().toString());

        try {
            List<Path> paths= Files.walk(localfs.getRoot().getPath()).collect(Collectors.toList());
            int len=localfs.getRoot().getPath().toString().length();
            for(int i=1;i<paths.size();i++) {
                java.lang.String relativepath=paths.get(i).toString().substring(len);

                Path p= Paths.get(basePath+"/"+relativepath);
               // System.out.println(p);
                if(!Files.exists(p))
                {
                    if(Files.isDirectory(p) || p.toString().indexOf(".")<0) {
                      //  localfs.createDirectory(p);
                        p.toFile().mkdirs();
                    } else{

                        Files.createFile(p);
                        /*BufferedReader reader = new BufferedReader(new FileReader((source)));
                        PrintWriter writer = new PrintWriter(path.toFile());
                        String line;
                        while ((line = reader.readLine()) != null)
                            writer.println(line);
                        reader.close();
                        writer.close();*/

                    }
                }

            }
            remotefs=new LocalFileSystem(base+root,"/"+localfs.getRoot().getPath().getFileName().toString());

        }catch (Exception ex){
            ex.printStackTrace();
        }
        return remotefs;
    }
    public static void undo(BaseAction ac,FileSystem src){
        if(ac.getAction().equals("RENAME"))
            src.rename( ac.getFilename(),src,((RenameAction) ac).getNewFileName());
    }
    public static void update(BaseAction ac,FileSystem src,FileSystem dst,String relativepath)
    {
        Path p1 = Paths.get(src.getBase() + relativepath);
        switch (ac.getAction()) {
            case "DELETED":
                dst.remove(relativepath, src);
                break;
            case "RENAME":
                dst.rename(((RenameAction) ac).getFilename(),src,((RenameAction) ac).getNewFileName());
                break;
            case "UPDATE":
                dst.replace(p1.toString(),dst,relativepath);
                break;
            case "ADD":
                dst.createFile(relativepath);
                if(Files.exists(p1)&& !Files.isDirectory(p1))
                    dst.replace(p1.toString(),dst,relativepath);
                break;

        }
    }
}
