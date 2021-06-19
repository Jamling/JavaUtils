package cn.ieclipse.util.swing;

import cn.ieclipse.util.swing.annotation.ConfItem;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.io.File;

import javax.swing.JFrame;
import javax.swing.UIManager;

public class PropConfEditorTest {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
        
        }
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    JFrame frame = new JFrame();
                    frame.setTitle("PropConfEditorTest");
                    frame.setBounds(100, 100, 450, 300);
                    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    PropConfEditor.Options opt = new PropConfEditor.Options();
                    TestConf conf = new TestConf();
                    conf.file = new File("test.properties");
                    conf.read();
                    opt.conf = conf;
                    PropConfEditor editor = new PropConfEditor(opt);
                    frame.getContentPane().add(editor, BorderLayout.CENTER);
                    frame.setVisible(true);
                    
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    static class TestConf extends PropConfigable{
        @ConfItem(desc = "姓名")
        private String name;
        @ConfItem(desc = "是否老年人")
        private Boolean older;
        @ConfItem(desc = "years")
        private int age;
    }
}
