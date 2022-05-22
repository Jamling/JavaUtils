package cn.ieclipse.util.swing;

import cn.ieclipse.util.swing.annotation.ConfigItem;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.io.File;

import javax.swing.JFrame;
import javax.swing.UIManager;

public class PropConfigEditorTest {
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
                    PropConfigEditor.Options opt = new PropConfigEditor.Options();
                    TestConf conf = PropConfig.Builder.newInstance().setFile(new File("test.conf")).create(TestConf.class);
                    conf.read();
                    opt.conf = conf;
                    PropConfigEditor editor = new PropConfigEditor(opt);
                    frame.getContentPane().add(editor, BorderLayout.CENTER);
                    frame.setVisible(true);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    static class TestConf extends PropConfig {
        @ConfigItem(desc = "姓名")
        private String name = "简体中文ABC";
        @ConfigItem(desc = "是否老年人")
        private Boolean older;
        @ConfigItem(desc = "years")
        private int age;
    }
}
