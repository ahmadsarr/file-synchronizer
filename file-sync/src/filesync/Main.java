package filesync;

import filesync.filesystem.FileSystem;
import filesync.filesystem.LocalFileSystem;

import java.util.Scanner;

public class Main {
    public static void main(String[] args)
    {

        LocalFileSystem lf=new LocalFileSystem("/home/mobis/fs","/dir1");
       FileSystem cplf=Utils.clone(lf,"/home/mobis","/fs2");
        Synchronizer synchronizer=new Synchronizer(lf,cplf);
       // new Thread(synchronizer).start();
        Scanner sc=new Scanner(System.in);
        while (true){
            System.out.println("Entrez une touche ...");
            sc.nextLine();
            System.out.println("Reconciliation");
            synchronizer.reconcile(lf.computerDirty(), cplf.computerDirty(), "/dir1");
        }



    }
}
