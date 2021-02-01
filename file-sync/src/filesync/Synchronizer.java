package filesync;


import filesync.action.BaseAction;
import filesync.filesystem.FileSystem;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Synchronizer implements Runnable {
    protected FileSystem fsA;
    protected FileSystem fsB;

    public Synchronizer(FileSystem A, FileSystem B) {
        this.fsA = A;
        this.fsB = B;
    }

    public void reconcile1(List<String> dirtyA, List<String> dirtyB, String relativepath) {
        if (dirtyA.isEmpty() && dirtyB.isEmpty())
            return;
        if (!dirtyA.contains(relativepath) && !dirtyB.contains(relativepath)) {
            return;
        }

        Path p1 = Paths.get(this.fsA.getBase() + relativepath);
        Path p2 = Paths.get(this.fsB.getBase() + relativepath);
        if (Files.isDirectory(p1) && Files.isDirectory(p2)) {
            Set<String> childrenAB = new HashSet<>();
            List<String> enf = fsA.getChildren(relativepath);
            if (enf != null && !enf.isEmpty())
                childrenAB.addAll(enf);
            enf = (fsB.getChildren(relativepath));
            if (enf != null && !enf.isEmpty())
                childrenAB.addAll(enf);
            if (childrenAB.isEmpty())
                return;
            for (String e : childrenAB)
                reconcile1(dirtyA, dirtyB, e);
        } else if (!dirtyA.contains(relativepath)) {
            fsA.replace(relativepath, fsB, fsB.getBase() + relativepath);
        } else if (!dirtyB.contains(relativepath)) {
            fsB.replace(relativepath, fsA, fsA.getBase() + relativepath);
        }


    }

    public void reconcile(List<BaseAction> dirtyA, List<BaseAction> dirtyB, String relativepath) {
        System.out.println(relativepath);
        if (dirtyA.isEmpty() && dirtyB.isEmpty())
            return;
        List<BaseAction> a = dirtyA.stream().filter(baseAction -> baseAction.getFilename().equals(relativepath)).collect(Collectors.toList());
        List<BaseAction> b = dirtyB.stream().filter(baseAction -> baseAction.getFilename().equals(relativepath)).collect(Collectors.toList());
        if (a.isEmpty() && b.isEmpty()) {
            return;
        }
        Path p1 = Paths.get(this.fsA.getBase() + relativepath);
        Path p2 = Paths.get(this.fsB.getBase() + relativepath);
        if (Files.isDirectory(p1) || Files.isDirectory(p2)) {
           List<BaseAction>l= a.stream().filter(baseAction -> baseAction.getAction().equals("DELETE")).collect(Collectors.toList());
          List<BaseAction>l2=  b.stream().filter(baseAction -> baseAction.getAction().equals("DELETE")).collect(Collectors.toList());//.forEach(baseAction -> Utils.update(baseAction, fsB, fsA, relativepath));
            if(!l.isEmpty()||!l2.isEmpty())
            {
                System.out.println("collision:");
                System.out.println(""+this.fsA.getBase()+"");
                l.stream().forEach(baseAction -> System.out.println(baseAction));
                l.stream().forEach(baseAction -> Utils.update(baseAction, fsA, fsB, relativepath));
                System.out.println(""+this.fsB.getBase()+"");
                l2.stream().forEach(baseAction -> System.out.println(baseAction));
                l2.stream().forEach(baseAction -> Utils.update(baseAction, fsB, fsA, relativepath));
                return;
            }
            a.stream().forEach(baseAction -> Utils.update(baseAction, fsA, fsB, relativepath));
            b.stream().forEach(baseAction -> Utils.update(baseAction, fsB, fsA, relativepath));
            List<String> enf = fsA.getChildren(relativepath);
            if (enf != null && !enf.isEmpty())
                enf.stream().forEach(e ->{reconcile(dirtyA,dirtyB,e);});
            enf = fsB.getChildren(relativepath);
            if (enf != null && !enf.isEmpty())
                enf.stream().forEach(e ->{reconcile(dirtyA,dirtyB,e);});


        } else if (!a.isEmpty() && !b.isEmpty()) {
            System.out.println("Collision :::" + relativepath);
            for (BaseAction ba : a) {
                if (ba.getAction().equals("DELETED") && ba.getFilename().equals(relativepath)) {
                    Utils.update(ba, fsA, fsB, relativepath);
                }
                if (ba.getAction().equals("RENAME") && ba.getFilename().equals(relativepath)) {
                    System.out.println("undo Rename : " + ba.getFilename());
                    Utils.undo(ba, fsA);
                }

            }
            for (BaseAction ba : b) {
                if (ba.getAction().equals("DELETED") && ba.getFilename().equals(relativepath)) {
                    Utils.update(ba, fsB, fsA, relativepath);
                }
                if (ba.getAction().equals("RENAME") && ba.getFilename().equals(relativepath)) {
                    System.out.println("undo Rename : " + ba.getFilename());
                    Utils.undo(ba, fsB);
                }
            }
        } else if (!a.isEmpty()) {
            Utils.update(a.get(0), fsA, fsB, relativepath);

        } else {
            Utils.update(b.get(0), fsB, fsA, relativepath);
        }

    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(55000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            reconcile(this.fsA.computerDirty(), this.fsB.computerDirty(), "/dir1");

        }
    }
}
