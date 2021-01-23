package filesync;

import filesync.filesystem.FileSystem;
import filesync.filesystem.LocalFileSystem;

public class Main {
    public static void main(String[] args)
    {

        LocalFileSystem lf=new LocalFileSystem("/home/mobis","/fs");
       FileSystem cplf=Utils.clone(lf,"/home/mobis","/fs2");
        Synchronizer synchronizer=new Synchronizer(lf,cplf);
        new Thread(synchronizer).start();



    }
}
